package ru.cft.drozdrtskiy.sorting.sorter.impl;

import ru.cft.drozdrtskiy.sorting.DTO.FileSorterArgumentsDTO;
import ru.cft.drozdrtskiy.sorting.argument.SortDirection;
import ru.cft.drozdrtskiy.sorting.element.FileElement;
import ru.cft.drozdrtskiy.sorting.sorter.Sorter;
import ru.cft.drozdrtskiy.sorting.writer.FileWriter;
import ru.cft.drozdrtskiy.sorting.writer.impl.*;
import ru.cft.drozdrtskiy.sorting.supplier.ElementSupplier;
import ru.cft.drozdrtskiy.sorting.reader.ElementReader;
import ru.cft.drozdrtskiy.sorting.reader.impl.FileElementReaderFactory;

import static ru.cft.drozdrtskiy.sorting.util.MessagePrinter.print;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public final class FileSorterByMerge implements Sorter {

    private final Path outputFile;
    private final List<Path> inputFiles;
    private final boolean isUnsortedFileElementsIgnore;
    private final FileElementReaderFactory fileElementReaderFactory;
    private final Comparator<FileElement> comparator;

    public FileSorterByMerge(FileSorterArgumentsDTO DTO) {
        outputFile = Paths.get(DTO.outputFile);
        inputFiles = DTO.inputFiles.stream()
                .map(Paths::get)
                .collect(Collectors.toList());
        isUnsortedFileElementsIgnore = DTO.isUnsortedFileElementsIgnore;
        fileElementReaderFactory = FileElementReaderFactory.from(DTO.elementType);
        comparator = DTO.sortDirection == SortDirection.DESC ?
                Comparator.reverseOrder() :
                Comparator.naturalOrder();
    }

    @Override
    public void sort() {
        try {
            createElementSupplierAndWriteOutputFile();
            print(String.format("Результат в файле: %s", outputFile.toAbsolutePath()));
        } catch (IOException e) {
            print(String.format("Не удачное чтение/запись файла. %s", e.getMessage()));
        } catch (Exception e) {
            print(e.getMessage());
        }
    }

    private void createElementSupplierAndWriteOutputFile() throws Exception {
        List<ElementReader<FileElement>> fileElementReaders = new ArrayList<>(inputFiles.size());

        try {
            for (Path file : inputFiles) {
                fileElementReaders.add(fileElementReaderFactory.createForFile(file));
            }

            ElementSupplier<FileElement> elementSupplier = new ElementSupplier<>(fileElementReaders, comparator);
            useElementSupplierToWriteOutputFile(elementSupplier);
        } finally {
            closeReaders(fileElementReaders);
        }
    }

    private void closeReaders(List<ElementReader<FileElement>> fileElementReaders) {
        for (ElementReader<FileElement> supplier : fileElementReaders) {
            if (supplier != null) {
                try {
                    supplier.close();
                } catch (Exception e) {
                    print(e.getMessage());
                }
            }
        }
    }

    private void useElementSupplierToWriteOutputFile(ElementSupplier<FileElement> elementSupplier) throws Exception {
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(outputFile);
             FileWriter fileWriter = isUnsortedFileElementsIgnore ?
                     new FileWriterWithIgnoring(bufferedWriter, comparator) :
                     new SimpleFileWriter(bufferedWriter)) {

            FileElement fileElement = elementSupplier.next();

            while (fileElement != null) {
                fileWriter.write(fileElement);
                fileElement = elementSupplier.next();
            }
        }
    }
}