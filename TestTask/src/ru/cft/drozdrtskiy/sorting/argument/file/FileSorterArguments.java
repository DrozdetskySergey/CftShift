package ru.cft.drozdrtskiy.sorting.argument.file;

import ru.cft.drozdrtskiy.sorting.argument.ArgsException;
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
    private final boolean isUnsortedFileElementsIgnore;
    private final Path outputFile;
    private final List<Path> inputFiles;

    public static FileSorterArguments from(String[] args) throws ArgsException {
        return new FileSorterArguments(Arrays.asList(args));
    }

    private FileSorterArguments(List<String> strings) throws ArgsException {
        parseArgumentStrings(strings);

        sortDirection = fetchSortDirection();
        elementType = fetchElementType();
        isUnsortedFileElementsIgnore = fetchUnsortedFileElementsIgnore();
        outputFile = fetchOutputFile();
        inputFiles = fetchInputFiles();

        checkAvailabilityAllArguments();
        checkKeysCorrect();
        checkKeysDoNotConflict();
        checkOutputFileNotMatchesWithInputFile();
        checkInputFilesIsReadable();
        checkOutputFileIsWritable();
    }

    public SortDirection getSortDirection() {
        return sortDirection;
    }

    public ElementType getElementType() {
        return elementType;
    }

    public Path getOutputFile() {
        return outputFile;
    }

    public List<Path> getInputFiles() {
        return inputFiles;
    }

    public boolean isUnsortedFileElementsIgnore() {
        return isUnsortedFileElementsIgnore;
    }

    private void parseArgumentStrings(List<String> strings) throws ArgsException {
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
            throw new ArgsException(String.format("Не допустимое имя файла. %s", e.getMessage()));
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

    private void checkAvailabilityAllArguments() throws ArgsException {
        if (sortDirection == null || elementType == null || outputFile == null
                || inputFiles == null || inputFiles.isEmpty()) {
            throw new ArgsException("Необходимые параметры не указаны.");
        }
    }

    private void checkKeysCorrect() throws ArgsException {
        List<String> correctKeyNotations = Arrays.stream(Key.values())
                .map(Key::notation)
                .collect(Collectors.toList());

        String joinedIncorrectKeyNotations = keyNotations.stream()
                .filter(Predicate.not(correctKeyNotations::contains))
                .collect(Collectors.joining("], [", "[", "]"));

        if (!joinedIncorrectKeyNotations.equals("[]")) {
            throw new ArgsException(String.format("Не известные параметры: %s", joinedIncorrectKeyNotations));
        }
    }

    private void checkKeysDoNotConflict() throws ArgsException {
        if (keyNotations.contains(Key.ASCENDING_ORDER.notation())
                && keyNotations.contains(Key.DESCENDING_ORDER.notation())) {
            throw new ArgsException(String.format("Конфликт параметров. (%s либо %s)"
                    , Key.ASCENDING_ORDER.notation(), Key.DESCENDING_ORDER.notation()));
        }

        if (keyNotations.contains(Key.STRING_TYPE.notation())
                && keyNotations.contains(Key.INTEGER_TYPE.notation())) {
            throw new ArgsException(String.format("Конфликт параметров. [%s либо %s]"
                    , Key.STRING_TYPE.notation(), Key.INTEGER_TYPE.notation()));
        }
    }

    private void checkOutputFileNotMatchesWithInputFile() throws ArgsException {
        if (inputFiles.contains(outputFile)) {
            throw new ArgsException(String.format("Совпадение имён входного файла %s и файла для результата."
                    , outputFile));
        }
    }

    private void checkInputFilesIsReadable() throws ArgsException {
        for (Path p : inputFiles) {
            if (!Files.isReadable(p)) {
                throw new ArgsException(String.format("Входящий файл %s недоступен для чтения.", p.getFileName()));
            }
        }
    }

    private void checkOutputFileIsWritable() throws ArgsException {
        if (Files.exists(outputFile)) {
            checkOutputFileCanBeOverwritten();
        } else {
            createOutputFile();
        }

        if (!Files.isWritable(outputFile)) {
            throw new ArgsException(String.format("Файл %s не может быть записан.", outputFile.getFileName()));
        }
    }

    private void checkOutputFileCanBeOverwritten() throws ArgsException {
        if (!keyNotations.contains(Key.OVERWRITE_OUTPUT_FILE.notation())) {
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