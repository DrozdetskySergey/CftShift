package ru.cft.drozdrtskiy.sorting.sorter.impl;

import ru.cft.drozdrtskiy.sorting.DTO.FileSorterArgumentsDTO;
import ru.cft.drozdrtskiy.sorting.argument.SortDirection;
import ru.cft.drozdrtskiy.sorting.element.FileElement;
import ru.cft.drozdrtskiy.sorting.sorter.Sorter;
import ru.cft.drozdrtskiy.sorting.writer.FileWriter;
import ru.cft.drozdrtskiy.sorting.writer.impl.*;
import ru.cft.drozdrtskiy.sorting.selector.ElementSelector;
import ru.cft.drozdrtskiy.sorting.supplier.ElementSupplier;
import ru.cft.drozdrtskiy.sorting.supplier.impl.FileElementSupplierFactory;
import ru.cft.drozdrtskiy.sorting.util.MessagePrinter;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public final class FileSorterByMerge implements Sorter {

    private final Path outputFile;
    private final List<Path> inputFiles;
    private final boolean isUnsortedFileElementsIgnore;
    private final FileElementSupplierFactory fileElementSupplierFactory;
    private final Comparator<FileElement> comparator;

    public FileSorterByMerge(FileSorterArgumentsDTO DTO) {
        outputFile = Paths.get(DTO.outputFile);
        inputFiles = DTO.inputFiles.stream()
                .map(Paths::get)
                .collect(Collectors.toList());
        isUnsortedFileElementsIgnore = DTO.isUnsortedFileElementsIgnore;
        fileElementSupplierFactory = FileElementSupplierFactory.from(DTO.elementType);
        comparator = DTO.sortDirection == SortDirection.DESC ?
                Comparator.reverseOrder() :
                Comparator.naturalOrder();
    }

    @Override
    public void sort() {
        try {
            sortInputFilesBySelectorAndWriteOutputFile();
            MessagePrinter.print(String.format("Результат в файле: %s", outputFile.toAbsolutePath()));
        } catch (IOException e) {
            MessagePrinter.print(String.format("Не удачное чтение/запись файла. %s", e.getMessage()));
        } catch (Exception e) {
            MessagePrinter.print(e.getMessage());
        }
    }

    private void sortInputFilesBySelectorAndWriteOutputFile() throws Exception {
        List<ElementSupplier<FileElement>> fileElementSuppliers = new ArrayList<>(inputFiles.size());

        try {
            for (Path file : inputFiles) {
                fileElementSuppliers.add(fileElementSupplierFactory.createForFile(file));
            }

            ElementSelector<FileElement> elementSelector = new ElementSelector<>(fileElementSuppliers, comparator);
            useElementSelectorToWriteOutputFile(elementSelector);
        } finally {
            closeSuppliers(fileElementSuppliers);
        }
    }

    private void closeSuppliers(List<ElementSupplier<FileElement>> fileElementSuppliers) {
        for (ElementSupplier<FileElement> supplier : fileElementSuppliers) {
            if (supplier != null) {
                try {
                    supplier.close();
                } catch (Exception e) {
                    MessagePrinter.print(e.getMessage());
                }
            }
        }
    }

    private void useElementSelectorToWriteOutputFile(ElementSelector<FileElement> elementSelector) throws Exception {
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(outputFile);
             FileWriter fileWriter = isUnsortedFileElementsIgnore ?
                     new FileWriterWithIgnoring(bufferedWriter, comparator) :
                     new SimpleFileWriter(bufferedWriter)) {

            FileElement fileElement = elementSelector.next();

            while (fileElement != null) {
                fileWriter.write(fileElement);
                fileElement = elementSelector.next();
            }
        }
    }
}