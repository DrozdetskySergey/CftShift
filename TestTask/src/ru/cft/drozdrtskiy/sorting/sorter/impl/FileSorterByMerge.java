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

public final class FileSorterByMerge implements Sorter {

    private final Path outputFile;
    private final List<Path> inputFiles;
    private final boolean isUnsortedFileElementsIgnore;
    private final FileElementReaderFactory fileElementReaderFactory;
    private final Comparator<FileElement> comparator;

    public FileSorterByMerge(FileSorterArgumentsDTO DTO) {
        outputFile = DTO.outputFile;
        inputFiles = new ArrayList<>(DTO.inputFiles);
        isUnsortedFileElementsIgnore = DTO.isUnsortedFileElementsIgnore;
        fileElementReaderFactory = FileElementReaderFactory.from(DTO.elementType);
        comparator = DTO.sortDirection == SortDirection.DESC ?
                Comparator.reverseOrder() :
                Comparator.naturalOrder();
    }

    @Override
    public void sort() {
        List<ElementReader<FileElement>> fileElementReaders = new ArrayList<>(inputFiles.size());

        try {
            for (Path path : inputFiles) {
                fileElementReaders.add(fileElementReaderFactory.createForFile(path));
            }

            ElementSupplier<FileElement> elementSupplier = new ElementSupplier<>(fileElementReaders, comparator);
            writeOutputFile(elementSupplier);
            print(String.format("Результат в файле: %s", outputFile.toAbsolutePath()));
        } catch (IOException e) {
            print(String.format("Не удачное чтение/запись файла. %s", e.getMessage()));
        } catch (Exception e) {
            print(e.getMessage());
        } finally {
            closeReaders(fileElementReaders);
        }
    }

    private void closeReaders(List<ElementReader<FileElement>> fileElementReaders) {
        for (ElementReader<FileElement> reader : fileElementReaders) {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    print(e.getMessage());
                }
            }
        }
    }

    private void writeOutputFile(ElementSupplier<FileElement> elementSupplier) throws Exception {
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(outputFile)) {
            FileWriter fileWriter = isUnsortedFileElementsIgnore ?
                    new StrictFileWriter(bufferedWriter, comparator) :
                    new SimpleFileWriter(bufferedWriter);

            for (FileElement fileElement = elementSupplier.next(); fileElement != null; ) {
                fileWriter.write(fileElement);
                fileElement = elementSupplier.next();
            }

            fileWriter.close();
        }
    }
}