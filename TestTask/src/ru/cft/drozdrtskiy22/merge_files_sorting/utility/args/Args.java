package ru.cft.drozdrtskiy22.merge_files_sorting.utility.args;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Args {

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
                .map(String::trim)
                .distinct()
                .collect(Collectors.toList());

        sortDirection = fetchSortDirection();
        elementType = fetchElementType();
        outputFile = fetchOutputFile();
        inputFiles = fetchInputFiles();

        checkArgumentsValid();
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
        return arguments.contains("-d") ? SortDirection.DESC : SortDirection.ASC;
    }

    private ElementType fetchElementType() {
        ElementType result = ElementType.UNKNOWN;

        if (arguments.contains("-s")) {
            result = ElementType.STRING;
        } else if (arguments.contains("-i")) {
            result = ElementType.INTEGER;
        }

        return result;
    }

    private Path fetchOutputFile() {
        return arguments.stream()
                .filter(e -> !e.startsWith("-"))
                .map(Paths::get)
                .findFirst()
                .orElse(null);
    }

    private List<Path> fetchInputFiles() {
        return arguments.stream()
                .filter(e -> !e.startsWith("-"))
                .skip(1)
                .map(Paths::get)
                .collect(Collectors.toList());
    }

    private void checkArgumentsValid() throws ArgsException {
        if (elementType == ElementType.UNKNOWN
                || outputFile == null
                || inputFiles.isEmpty()
                || inputFiles.contains(outputFile)) {
            throw new ArgsException("Не верные параметры.");
        }

        List<String> keys = arguments.stream()
                .filter(e -> e.startsWith("-"))
                .collect(Collectors.toList());

        keys.removeAll(Arrays.asList("-a", "-d", "-s", "-i"));

        if (keys.size() > 0) {
            throw new ArgsException("Не известный параметр: " + keys.get(0));
        }
    }
}