package ru.cft.drozdrtskiy22.merge_files_sorting;

import ru.cft.drozdrtskiy22.merge_files_sorting.element.FileElement;
import ru.cft.drozdrtskiy22.merge_files_sorting.supplier.FileElementSupplierFactory;
import ru.cft.drozdrtskiy22.merge_files_sorting.supplier.SupplierFactory;
import ru.cft.drozdrtskiy22.merge_files_sorting.utility.args.Args;
import ru.cft.drozdrtskiy22.merge_files_sorting.utility.args.ArgsException;
import ru.cft.drozdrtskiy22.merge_files_sorting.utility.args.ElementType;
import ru.cft.drozdrtskiy22.merge_files_sorting.utility.args.SortDirection;
import ru.cft.drozdrtskiy22.merge_files_sorting.utility.message.Message;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        args = new String[]{"-i", "out.txt", "in1.txt", "in2.txt", "in3.txt"};

        if (args.length < 3) {
            Message.INFO.show();
            Message.PARAMS.show();
            Message.KEYS.show();

            return;
        }

        Args arg;

        try {
            arg = Args.fromArray(args);
        } catch (ArgsException e) {
            System.out.println(e.getMessage());
            Message.PARAMS.show();

            return;
        }

        SortDirection sortDirection = arg.getSortDirection();
        ElementType elementType = arg.getElementType();
        Path outputFile = arg.getOutputFile();
        List<Path> inputFiles = arg.getInputFiles();

        Comparator<FileElement> comparator =
                sortDirection == SortDirection.DESC ? Comparator.reverseOrder() : Comparator.naturalOrder();

        SupplierFactory supplierFactory = new FileElementSupplierFactory(elementType, comparator);

        try (MergeFilesSort mergeFilesSort = new MergeFilesSort(comparator, supplierFactory, outputFile, inputFiles)) {
            mergeFilesSort.sort();
            System.out.printf("Done. Result file: \"%s\"%n", outputFile.toAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
