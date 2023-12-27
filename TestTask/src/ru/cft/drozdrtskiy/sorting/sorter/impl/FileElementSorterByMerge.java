package ru.cft.drozdrtskiy.sorting.sorter.impl;

import ru.cft.drozdrtskiy.sorting.DTO.FileElementSorterArgsDTO;
import ru.cft.drozdrtskiy.sorting.SortDirection;
import ru.cft.drozdrtskiy.sorting.element.Element;
import ru.cft.drozdrtskiy.sorting.reader.ElementReader;
import ru.cft.drozdrtskiy.sorting.reader.impl.ElementReaderFactory;
import ru.cft.drozdrtskiy.sorting.sorter.ElementSorter;
import ru.cft.drozdrtskiy.sorting.sorter.ElementSupplier;
import ru.cft.drozdrtskiy.sorting.writer.ElementWriter;
import ru.cft.drozdrtskiy.sorting.writer.impl.SimpleFileElementWriter;
import ru.cft.drozdrtskiy.sorting.writer.impl.StrictFileElementWriter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static ru.cft.drozdrtskiy.sorting.util.MessagePrinter.print;

public final class FileElementSorterByMerge implements ElementSorter {

    private final Path outputFile;
    private final List<Path> inputFiles;
    private final boolean isUnsortedFileElementsIgnore;
    private final ElementReaderFactory elementReaderFactory;
    private final Comparator<Element> comparator;

    public FileElementSorterByMerge(FileElementSorterArgsDTO DTO) {
        outputFile = DTO.outputFile;
        inputFiles = new ArrayList<>(DTO.inputFiles);
        isUnsortedFileElementsIgnore = DTO.isUnsortedFileElementsIgnore;
        elementReaderFactory = ElementReaderFactory.from(DTO.elementType);
        comparator = DTO.sortDirection == SortDirection.DESC ?
                Comparator.reverseOrder() :
                Comparator.naturalOrder();
    }

    @Override
    public void sort() {
        List<ElementReader> elementReaders = new ArrayList<>(inputFiles.size());

        try {
            for (Path path : inputFiles) {
                elementReaders.add(elementReaderFactory.createFor(path));
            }

            ElementSupplier elementSupplier = new ElementSupplier(elementReaders, comparator);
            writeOutputFile(elementSupplier);
            print(String.format("Результат в файле: %s", outputFile.toAbsolutePath()));
        } catch (IOException e) {
            print(String.format("Не удачное чтение/запись файла. %s", e.getMessage()));
        } catch (Exception e) {
            print(e.getMessage());
        } finally {
            closeReaders(elementReaders);
        }
    }

    private void writeOutputFile(ElementSupplier elementSupplier) throws Exception {
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(outputFile);
             ElementWriter elementWriter = isUnsortedFileElementsIgnore ?
                     new StrictFileElementWriter(bufferedWriter, comparator) :
                     new SimpleFileElementWriter(bufferedWriter)) {

            for (Element element = elementSupplier.next(); element != null; ) {
                elementWriter.write(element);
                element = elementSupplier.next();
            }
        }
    }

    private void closeReaders(List<ElementReader> elementReaders) {
        for (ElementReader reader : elementReaders) {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    print(e.getMessage());
                }
            }
        }
    }
}