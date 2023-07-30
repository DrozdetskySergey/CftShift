package ru.cft.drozdrtskiy.sorting.argument.file;

import ru.cft.drozdrtskiy.sorting.DTO.FileSorterArgumentsDTO;
import ru.cft.drozdrtskiy.sorting.argument.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ru.cft.drozdrtskiy.sorting.argument.file.Key.*;

public final class FileSorterArguments {

    private List<String> keys;
    private List<Path> files;
    private final SortDirection sortDirection;
    private final ElementType elementType;
    private final Path outputFile;
    private final List<Path> inputFiles;

    public FileSorterArguments(String[] args) throws ArgsException {
        List<String> arguments = Arrays.asList(args);
        parseArguments(arguments);

        sortDirection = fetchSortDirection();
        elementType = fetchElementType();
        outputFile = fetchOutputFile();
        inputFiles = fetchInputFiles();

        checkAvailabilityAllArguments();
        checkKeysCorrect();
        checkKeysDoNotConflict();
        checkOutputFileNotMatchesWithInputFile();
        checkInputFilesIsReadable();
        checkOutputFileCanBeWritten();
    }

    public FileSorterArgumentsDTO getDTO() {
        FileSorterArgumentsDTO DTO = new FileSorterArgumentsDTO();

        DTO.sortDirection = sortDirection;
        DTO.elementType = elementType;
        DTO.isUnsortedFileElementsIgnore = fetchUnsortedFileElementsIgnore();
        DTO.outputFile = outputFile.toString();
        DTO.inputFiles = inputFiles.stream()
                .map(Path::toString)
                .collect(Collectors.toList());

        return DTO;
    }

    private void parseArguments(List<String> strings) throws ArgsException {
        List<String> validArguments = strings.stream()
                .filter(Objects::nonNull)
                .filter(Predicate.not(String::isBlank))
                .map(String::strip)
                .collect(Collectors.toList());

        keys = validArguments.stream()
                .filter(s -> s.startsWith("-"))
                .collect(Collectors.toList());

        try {
            files = validArguments.stream()
                    .filter(s -> !s.startsWith("-"))
                    .map(Paths::get)
                    .collect(Collectors.toList());
        } catch (InvalidPathException e) {
            throw new ArgsException(String.format("Не допустимое имя файла. %s", e.getMessage()));
        }
    }

    private SortDirection fetchSortDirection() {
        SortDirection result = SortDirection.ASC;

        if (keys.contains(DESCENDING_ORDER.key)) {
            result = SortDirection.DESC;
        }

        return result;
    }

    private ElementType fetchElementType() {
        ElementType result = null;

        if (keys.contains(STRING_TYPE.key)) {
            result = ElementType.STRING;
        } else if (keys.contains(INTEGER_TYPE.key)) {
            result = ElementType.INTEGER;
        }

        return result;
    }

    private Path fetchOutputFile() {
        return files.stream()
                .findFirst()
                .orElse(null);
    }

    private List<Path> fetchInputFiles() {
        return files.stream()
                .skip(1)
                .collect(Collectors.toList());
    }

    private boolean fetchUnsortedFileElementsIgnore() {
        return keys.contains(IGNORE_UNSORTED.key);
    }

    private void checkAvailabilityAllArguments() throws ArgsException {
        if (sortDirection == null || elementType == null || outputFile == null || inputFiles.isEmpty()) {
            throw new ArgsException("Необходимые аргументы не указаны.");
        }
    }

    private void checkKeysCorrect() throws ArgsException {
        List<String> allowedKeys = Arrays.stream(Key.values())
                .map(Key::key)
                .collect(Collectors.toList());

        String unknownKeysJoining = keys.stream()
                .filter(Predicate.not(allowedKeys::contains))
                .collect(Collectors.joining("], [", "[", "]"));

        boolean hasUnknownKeys = !unknownKeysJoining.equals("[]");

        if (hasUnknownKeys) {
            throw new ArgsException(String.format("Не известные параметры: %s", unknownKeysJoining));
        }
    }

    private void checkKeysDoNotConflict() throws ArgsException {
        if (keys.contains(ASCENDING_ORDER.key) && keys.contains(DESCENDING_ORDER.key)) {
            throw new ArgsException(String.format("Конфликт параметров. (%s либо %s)", ASCENDING_ORDER.key, DESCENDING_ORDER.key));
        }

        if (keys.contains(STRING_TYPE.key) && keys.contains(INTEGER_TYPE.key)) {
            throw new ArgsException(String.format("Конфликт параметров. [%s либо %s]", STRING_TYPE.key, INTEGER_TYPE.key));
        }
    }

    private void checkOutputFileNotMatchesWithInputFile() throws ArgsException {
        if (inputFiles.contains(outputFile)) {
            throw new ArgsException(String.format("Совпадение имён входного файла %s и файла для результата.", outputFile));
        }
    }

    private void checkInputFilesIsReadable() throws ArgsException {
        for (Path file : inputFiles) {
            boolean isFileNotReadable = !Files.isReadable(file);

            if (isFileNotReadable) {
                throw new ArgsException(String.format("Входящий файл %s недоступен для чтения.", file.getFileName()));
            }
        }
    }

    private void checkOutputFileCanBeWritten() throws ArgsException {
        if (Files.exists(outputFile)) {
            checkOutputFileCanBeOverwritten();
        } else {
            createOutputFile();
        }

        boolean isFileNotWritable = !Files.isWritable(outputFile);

        if (isFileNotWritable) {
            throw new ArgsException(String.format("Файл %s не может быть записан.", outputFile.getFileName()));
        }
    }

    private void checkOutputFileCanBeOverwritten() throws ArgsException {
        boolean isNotHaveKeyToOverwriteOutputFile = !keys.contains(OVERWRITE_OUTPUT_FILE.key);

        if (isNotHaveKeyToOverwriteOutputFile) {
            throw new ArgsException(String.format("Файл %s уже существует.", outputFile.getFileName()));
        }
    }

    private void createOutputFile() throws ArgsException {
        try {
            Files.createFile(outputFile);
        } catch (IOException e) {
            throw new ArgsException(String.format("Не удалось создать файл %s. %s"
                    , outputFile.getFileName(), e.getMessage()));
        }
    }
}