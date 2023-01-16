package ru.cft.drozdrtskiy22.merge_files_sorting.argument;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class Args {

    private final List<String> arguments;
    private final SortDirection sortDirection;
    private final ElementType elementType;
    private final Path outputFile;
    private final List<Path> inputFiles;

    public static Args fromArray(String[] array) throws ArgsException {
        return new Args(Arrays.asList(array));
    }

    private Args(List<String> arguments) throws ArgsException {
        this.arguments = arguments.stream()
                .filter(Objects::nonNull)
                .filter(Predicate.not(String::isBlank))
                .map(String::trim)
                .collect(Collectors.toList());

        sortDirection = fetchSortDirection();
        elementType = fetchElementType();
        outputFile = fetchOutputFile();
        inputFiles = fetchInputFiles();

        checkValidityArguments();
        checkForUnknownKeys();
        checkForKeysConflict();
        checkOutputFileNotMatchesWithInputFiles();
        checkInputFilesIsReadable();

        if (isOutputFileCanNotBeOverwritten()) {
            checkOutputFileIsNotExists();
        }
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

    private SortDirection fetchSortDirection() {
        SortDirection result = SortDirection.ASC;

        if (arguments.contains(KeyList.DESCENDING_ORDER.getKey())) {
            result = SortDirection.DESC;
        }

        return result;
    }

    private ElementType fetchElementType() {
        ElementType result = null;

        if (arguments.contains(KeyList.STRING_TYPE.getKey())) {
            result = ElementType.STRING;
        } else if (arguments.contains(KeyList.INTEGER_TYPE.getKey())) {
            result = ElementType.INTEGER;
        }

        return result;
    }

    private Path fetchOutputFile() {
        return arguments.stream()
                .filter(s -> !s.startsWith("-"))
                .map(Paths::get)
                .findFirst()
                .orElse(null);
    }

    private List<Path> fetchInputFiles() {
        return arguments.stream()
                .filter(s -> !s.startsWith("-"))
                .skip(1)
                .map(Paths::get)
                .collect(Collectors.toList());
    }

    private void checkValidityArguments() throws ArgsException {
        if (sortDirection == null || elementType == null || outputFile == null || inputFiles.isEmpty()) {
            throw new ArgsException("Необходимые параметры не указаны.");
        }
    }

    private void checkForUnknownKeys() throws ArgsException {
        List<String> keyArguments = arguments.stream()
                .filter(e -> e.startsWith("-"))
                .collect(Collectors.toList());

        for (KeyList k : KeyList.values()) {
            keyArguments.removeIf(s -> s.equals(k.getKey()));
        }

        if (keyArguments.size() > 0) {
            throw new ArgsException(String.format("Не верный параметр: %s", keyArguments.get(0)));
        }
    }

    private void checkForKeysConflict() throws ArgsException {
        if (arguments.contains(KeyList.ASCENDING_ORDER.getKey())
                && arguments.contains(KeyList.DESCENDING_ORDER.getKey())) {
            throw new ArgsException("Конфликт параметров. (-a либо -d)");
        }

        if (arguments.contains(KeyList.STRING_TYPE.getKey())
                && arguments.contains(KeyList.INTEGER_TYPE.getKey())) {
            throw new ArgsException("Конфликт параметров. [-s либо -i]");
        }
    }

    private void checkOutputFileNotMatchesWithInputFiles() throws ArgsException {
        if (inputFiles.contains(outputFile)) {
            throw new ArgsException("Совпадение имён входного файла и файла для результата.");
        }
    }

    private void checkInputFilesIsReadable() throws ArgsException {
        for (Path p : inputFiles) {
            if (!Files.isReadable(p)) {
                throw new ArgsException(String.format("Входящий файл %s недоступен для чтения.", p.getFileName()));
            }
        }
    }

    private boolean isOutputFileCanNotBeOverwritten() {
        return !arguments.contains(KeyList.OVERWRITE_OUTPUT_FILE.getKey());
    }

    private void checkOutputFileIsNotExists() throws ArgsException {
        if (Files.exists(outputFile)) {
            throw new ArgsException(String.format("Файл %s уже существует.", outputFile.getFileName()));
        }
    }
}