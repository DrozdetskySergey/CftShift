package ru.cft.drozdrtskiy.sorting.sorter.file_impl;

import ru.cft.drozdrtskiy.sorting.DTO.FileSorterArgumentsDTO;
import ru.cft.drozdrtskiy.sorting.argument.SortDirection;
import ru.cft.drozdrtskiy.sorting.element.Element;
import ru.cft.drozdrtskiy.sorting.element.FileElement;
import ru.cft.drozdrtskiy.sorting.sorter.Sorter;
import ru.cft.drozdrtskiy.sorting.sorter.file_impl.writer.FileWriter;
import ru.cft.drozdrtskiy.sorting.sorter.file_impl.writer.impl.FileWriterWithIgnoring;
import ru.cft.drozdrtskiy.sorting.sorter.file_impl.writer.impl.SimpleFileWriter;
import ru.cft.drozdrtskiy.sorting.sorter.selector.ElementSelector;
import ru.cft.drozdrtskiy.sorting.supplier.ElementSupplier;
import ru.cft.drozdrtskiy.sorting.supplier.file_impl.factory.FileElementSupplierFactory;
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
    private final Comparator<Element> comparator;

    public static FileSorterByMerge from(FileSorterArgumentsDTO DTO) {
        return new FileSorterByMerge(DTO);
    }

    private FileSorterByMerge(FileSorterArgumentsDTO DTO) {
        outputFile = Paths.get(DTO.outputFile.toUri());
        inputFiles = DTO.inputFiles.stream()
                .map(p -> Paths.get(p.toUri()))
                .collect(Collectors.toList());
        isUnsortedFileElementsIgnore = DTO.isUnsortedFileElementsIgnore;
        fileElementSupplierFactory = FileElementSupplierFactory.from(DTO.elementType);
        comparator = DTO.sortDirection == SortDirection.DESC ?
                Comparator.reverseOrder() : Comparator.naturalOrder();
    }

    @Override
    public void sort() {
        try {
            sortInputFilesAndWriteOutputFile();
            MessagePrinter.print(String.format("Результат в файле: %s", outputFile.toAbsolutePath()));
        } catch (IOException e) {
            MessagePrinter.print(String.format("Не удачное чтение/запись файла(ов). %s", e.getMessage()));
        } catch (Exception e) {
            MessagePrinter.print(e.getMessage());
        }
    }

    private void sortInputFilesAndWriteOutputFile() throws Exception {
        List<ElementSupplier> elementSuppliers = new ArrayList<>(inputFiles.size());

        try {
            for (Path file : inputFiles) {
                elementSuppliers.add(fileElementSupplierFactory.create(file));
            }

            ElementSelector elementSelector = ElementSelector.from(elementSuppliers, comparator);
            useElementSelectorToWriteOutputFile(elementSelector);
        } finally {
            closeElementSuppliers(elementSuppliers);
        }
    }

    private void closeElementSuppliers(List<ElementSupplier> elementSuppliers) {
        for (ElementSupplier supplier : elementSuppliers) {
            if (supplier != null) {
                try {
                    supplier.close();
                } catch (Exception e) {
                    MessagePrinter.print(e.getMessage());
                }
            }
        }
    }

    private void useElementSelectorToWriteOutputFile(ElementSelector elementSelector) throws Exception {
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(outputFile);
             FileWriter fileWriter = isUnsortedFileElementsIgnore
                     ? FileWriterWithIgnoring.from(bufferedWriter, comparator)
                     : SimpleFileWriter.from(bufferedWriter)) {

            FileElement fileElement = (FileElement) elementSelector.next();

            while (fileElement != null) {
                fileWriter.write(fileElement);
                fileElement = (FileElement) elementSelector.next();
            }
        }
    }
}