package ru.cft.drozdrtskiy22.merge_files_sorting.utility.args;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
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
                .filter(s -> s != null && !s.isBlank())
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
        if (arguments.contains(Key.DESCENDING_ORDER.getSign())) {

            return SortDirection.DESC;
        }

        return SortDirection.ASC;
    }

    private ElementType fetchElementType() {
        if (arguments.contains(Key.STRING_TYPE.getSign())) {

            return ElementType.STRING;
        } else if (arguments.contains(Key.INTEGER_TYPE.getSign())) {

            return ElementType.INTEGER;
        }

        return null;
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

        for (Key k : Key.values()) {
            keyArguments.removeIf(s -> s.equals(k.getSign()));
        }

        if (keyArguments.size() > 0) {
            throw new ArgsException(String.format("Не верный параметр: %s", keyArguments.get(0)));
        }
    }

    private void checkForKeysConflict() throws ArgsException {
        if (arguments.contains(Key.ASCENDING_ORDER.getSign())
                && arguments.contains(Key.DESCENDING_ORDER.getSign())) {
            throw new ArgsException("Конфликт параметров. (-a либо -d)");
        }

        if (arguments.contains(Key.STRING_TYPE.getSign())
                && arguments.contains(Key.INTEGER_TYPE.getSign())) {
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
        return !arguments.contains(Key.OVERWRITE_OUTPUT_FILE.getSign());
    }

    private void checkOutputFileIsNotExists() throws ArgsException {
        if (Files.exists(outputFile)) {
            throw new ArgsException(String.format("Файл %s уже существует.", outputFile.getFileName()));
        }
    }
}