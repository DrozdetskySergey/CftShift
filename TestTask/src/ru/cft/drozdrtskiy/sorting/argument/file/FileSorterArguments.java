package ru.cft.drozdrtskiy.sorting.argument.file;

import ru.cft.drozdrtskiy.sorting.DTO.FileSorterArgumentsDTO;
import ru.cft.drozdrtskiy.sorting.argument.ArgumentsException;
import ru.cft.drozdrtskiy.sorting.argument.ElementType;
import ru.cft.drozdrtskiy.sorting.argument.SortDirection;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class FileSorterArguments {

    private List<String> keyNotations;
    private List<Path> files;
    private final SortDirection sortDirection;
    private final ElementType elementType;
    private final Path outputFile;
    private final List<Path> inputFiles;

    public static FileSorterArguments from(String[] args) throws ArgumentsException {
        return new FileSorterArguments(Arrays.asList(args));
    }

    private FileSorterArguments(List<String> strings) throws ArgumentsException {
        parseArgumentStrings(strings);

        sortDirection = fetchSortDirection();
        elementType = fetchElementType();
        outputFile = fetchOutputFile();
        inputFiles = fetchInputFiles();

        checkAvailabilityAllArguments();
        checkKeysCorrect();
        checkKeysDoNotConflict();
        checkOutputFileNotMatchesWithInputFile();
        checkInputFilesIsReadable();
        checkOutputFileIsWritable();
    }

    public FileSorterArgumentsDTO getDTO() {
        FileSorterArgumentsDTO DTO = new FileSorterArgumentsDTO();

        DTO.sortDirection = sortDirection;
        DTO.elementType = elementType;
        DTO.isUnsortedFileElementsIgnore = fetchUnsortedFileElementsIgnore();
        DTO.outputFile = Paths.get(outputFile.toUri());
        DTO.inputFiles = inputFiles.stream()
                .map(p -> Paths.get(p.toUri()))
                .collect(Collectors.toList());

        return DTO;
    }

    private void parseArgumentStrings(List<String> strings) throws ArgumentsException {
        List<String> validArguments = strings.stream()
                .filter(Objects::nonNull)
                .filter(Predicate.not(String::isBlank))
                .map(String::strip)
                .collect(Collectors.toList());

        keyNotations = validArguments.stream()
                .filter(s -> s.startsWith("-"))
                .collect(Collectors.toList());

        try {
            files = validArguments.stream()
                    .filter(s -> !s.startsWith("-"))
                    .map(Paths::get)
                    .collect(Collectors.toList());
        } catch (InvalidPathException e) {
            throw new ArgumentsException(String.format("Не допустимое имя файла. %s", e.getMessage()));
        }

    }

    private SortDirection fetchSortDirection() {
        SortDirection result = SortDirection.ASC;

        if (keyNotations.contains(Key.DESCENDING_ORDER.notation())) {
            result = SortDirection.DESC;
        }

        return result;
    }

    private ElementType fetchElementType() {
        ElementType result = null;

        if (keyNotations.contains(Key.STRING_TYPE.notation())) {
            result = ElementType.STRING;
        } else if (keyNotations.contains(Key.INTEGER_TYPE.notation())) {
            result = ElementType.INTEGER;
        }

        return result;
    }

    private boolean fetchUnsortedFileElementsIgnore() {
        return keyNotations.contains(Key.IGNORE_UNSORTED.notation());
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

    private void checkAvailabilityAllArguments() throws ArgumentsException {
        if (sortDirection == null || elementType == null || outputFile == null
                || inputFiles == null || inputFiles.isEmpty()) {
            throw new ArgumentsException("Необходимые параметры не указаны.");
        }
    }

    private void checkKeysCorrect() throws ArgumentsException {
        List<String> correctKeyNotations = Arrays.stream(Key.values())
                .map(Key::notation)
                .collect(Collectors.toList());

        String joinedIncorrectKeyNotations = keyNotations.stream()
                .filter(Predicate.not(correctKeyNotations::contains))
                .collect(Collectors.joining("], [", "[", "]"));

        if (!joinedIncorrectKeyNotations.equals("[]")) {
            throw new ArgumentsException(String.format("Не известные параметры: %s", joinedIncorrectKeyNotations));
        }
    }

    private void checkKeysDoNotConflict() throws ArgumentsException {
        if (keyNotations.contains(Key.ASCENDING_ORDER.notation())
                && keyNotations.contains(Key.DESCENDING_ORDER.notation())) {
            throw new ArgumentsException(String.format("Конфликт параметров. (%s либо %s)"
                    , Key.ASCENDING_ORDER.notation(), Key.DESCENDING_ORDER.notation()));
        }

        if (keyNotations.contains(Key.STRING_TYPE.notation())
                && keyNotations.contains(Key.INTEGER_TYPE.notation())) {
            throw new ArgumentsException(String.format("Конфликт параметров. [%s либо %s]"
                    , Key.STRING_TYPE.notation(), Key.INTEGER_TYPE.notation()));
        }
    }

    private void checkOutputFileNotMatchesWithInputFile() throws ArgumentsException {
        if (inputFiles.contains(outputFile)) {
            throw new ArgumentsException(String.format("Совпадение имён входного файла %s и файла для результата."
                    , outputFile));
        }
    }

    private void checkInputFilesIsReadable() throws ArgumentsException {
        for (Path file : inputFiles) {
            if (!Files.isReadable(file)) {
                throw new ArgumentsException(String.format("Входящий файл %s недоступен для чтения.", file.getFileName()));
            }
        }
    }

    private void checkOutputFileIsWritable() throws ArgumentsException {
        if (Files.exists(outputFile)) {
            checkOutputFileCanBeOverwritten();
        } else {
            createOutputFile();
        }

        if (!Files.isWritable(outputFile)) {
            throw new ArgumentsException(String.format("Файл %s не может быть записан.", outputFile.getFileName()));
        }
    }

    private void checkOutputFileCanBeOverwritten() throws ArgumentsException {
        if (!keyNotations.contains(Key.OVERWRITE_OUTPUT_FILE.notation())) {
            throw new ArgumentsException(String.format("Файл %s уже существует.", outputFile.getFileName()));
        }
    }

    private void createOutputFile() throws ArgumentsException {
        try {
            Files.createFile(outputFile);
        } catch (IOException e) {
            throw new ArgumentsException(String.format("Не удалось создать файл %s. %s"
                    , outputFile.getFileName(), e.getMessage()));
        }
    }
}