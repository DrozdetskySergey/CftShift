package ru.cft.drozdrtskiy.sorting.sorter.file.merge;

import ru.cft.drozdrtskiy.sorting.argument.SortDirection;
import ru.cft.drozdrtskiy.sorting.argument.file.FileSorterArguments;
import ru.cft.drozdrtskiy.sorting.element.Element;
import ru.cft.drozdrtskiy.sorting.element.file.FileElement;
import ru.cft.drozdrtskiy.sorting.sorter.Sorter;
import ru.cft.drozdrtskiy.sorting.sorter.SuppliersElementSelector;
import ru.cft.drozdrtskiy.sorting.supplier.ElementSupplier;
import ru.cft.drozdrtskiy.sorting.supplier.file.FileElementSupplierFactory;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class FileSorterByMerge implements Sorter {

    private final Path outputFile;
    private final List<Path> inputFiles;
    private final FileElementSupplierFactory fileElementSupplierFactory;
    private final Comparator<Element> comparator;

    public static FileSorterByMerge from(FileSorterArguments arguments) {
        return new FileSorterByMerge(arguments);
    }

    private FileSorterByMerge(FileSorterArguments arguments) {
        this.outputFile = arguments.getOutputFile();
        this.inputFiles = arguments.getInputFiles();
        this.fileElementSupplierFactory = FileElementSupplierFactory.from(arguments.getElementType());
        comparator = arguments.getSortDirection() == SortDirection.DESC ? Comparator.reverseOrder() : Comparator.naturalOrder();
    }

    @Override
    public void sort() throws Exception {
        sortInputFilesWithMergeAndSaveResultToOutputFile();
        System.out.printf("Результат в файле: %s%n", outputFile.toAbsolutePath());
    }

    private void sortInputFilesWithMergeAndSaveResultToOutputFile() throws Exception {
        List<ElementSupplier> elementSuppliers = new ArrayList<>(inputFiles.size());

        for (Path p : inputFiles) {
            elementSuppliers.add(fileElementSupplierFactory.create(p));
        }

        SuppliersElementSelector elementSelector
                = SuppliersElementSelector.from(elementSuppliers, comparator);

        try (BufferedWriter fileWriter = Files.newBufferedWriter(outputFile)) {
            while (elementSelector.hasNext()) {
                FileElement fileElement = (FileElement) elementSelector.next();
                fileWriter.write(fileElement.toWritableFormat());
            }
        }
    }
}