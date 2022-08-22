package ru.cft.drozdrtskiy22.merge_files_sorting;

import ru.cft.drozdrtskiy22.merge_files_sorting.element.FileElement;
import ru.cft.drozdrtskiy22.merge_files_sorting.supplier.FileElementSupplierFactory;
import ru.cft.drozdrtskiy22.merge_files_sorting.supplier.SupplierFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        List<String> arguments = Arrays.asList(args);

        SortDirection sortDirection = fetchSortDirection(arguments);
        ElementType elementType = fetchElementType(arguments);
        Path outputFile = fetchOutputFile(arguments);
        List<Path> inputFiles = fetchInputFiles(arguments);

        if (elementType == ElementType.UNKNOWN || outputFile == null || inputFiles.isEmpty()) {
            System.out.printf("Parameters: [-a | -d] [-s | -i] [output_file_name] [input_file_1_name] ([input_file_2_name]..)%n-a  ascending order%n-d  descending order%n-s  String type%n-i  Integer type%n");

            return;
        }

        Comparator<FileElement> comparator = sortDirection == SortDirection.DESC ? Comparator.reverseOrder() : Comparator.naturalOrder();

        SupplierFactory supplierFactory = new FileElementSupplierFactory(elementType, comparator);

        try (MergeFilesSort mergeFilesSort = new MergeFilesSort(comparator, supplierFactory, outputFile, inputFiles)) {
            mergeFilesSort.sort();
            System.out.printf("Done. Result file: \"%s\"%n", outputFile.toAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static SortDirection fetchSortDirection(List<String> arguments) {
        SortDirection result = SortDirection.ASC;

        if (arguments.contains("-d")) {
            result = SortDirection.DESC;
        }

        return result;
    }

    private static ElementType fetchElementType(List<String> arguments) {
        ElementType result = ElementType.UNKNOWN;

        if (arguments.contains("-s")) {
            result = ElementType.STRING;
        } else if (arguments.contains("-i")) {
            result = ElementType.INTEGER;
        }

        return result;
    }

    private static Path fetchOutputFile(List<String> arguments) {
        List<String> strings = new ArrayList<>(arguments);

        strings.removeIf(e -> e.startsWith("-"));

        return strings.size() > 0 ? Paths.get(strings.get(0)) : null;
    }

    private static List<Path> fetchInputFiles(List<String> arguments) {
        return arguments.stream()
                .filter(e -> !e.startsWith("-"))
                .distinct()
                .skip(1)
                .map(Paths::get)
                .collect(Collectors.toList());
    }
}
