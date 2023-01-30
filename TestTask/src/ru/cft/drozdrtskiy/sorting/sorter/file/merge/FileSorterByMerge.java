package ru.cft.drozdrtskiy.sorting.sorter.file.merge;

import ru.cft.drozdrtskiy.sorting.argument.SortDirection;
import ru.cft.drozdrtskiy.sorting.argument.file.FileSorterArguments;
import ru.cft.drozdrtskiy.sorting.element.Element;
import ru.cft.drozdrtskiy.sorting.element.file.FileElement;
import ru.cft.drozdrtskiy.sorting.sorter.ElementSelector;
import ru.cft.drozdrtskiy.sorting.sorter.Sorter;
import ru.cft.drozdrtskiy.sorting.supplier.ElementSupplier;
import ru.cft.drozdrtskiy.sorting.supplier.file.FileElementSupplierFactory;
import ru.cft.drozdrtskiy.sorting.util.Writer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class FileSorterByMerge implements Sorter {

    private final Path outputFile;
    private final List<Path> inputFiles;
    private final boolean isUnsortedFileElementsIgnore;
    private final FileElementSupplierFactory fileElementSupplierFactory;
    private final Comparator<Element> comparator;
    private int IgnoredFileElementCount;
    private FileElement previousFileElement;

    public static FileSorterByMerge from(FileSorterArguments arguments) {
        return new FileSorterByMerge(arguments);
    }

    private FileSorterByMerge(FileSorterArguments arguments) {
        this.outputFile = arguments.getOutputFile();
        this.inputFiles = arguments.getInputFiles();
        this.isUnsortedFileElementsIgnore = arguments.isUnsortedFileElementsIgnore();
        this.fileElementSupplierFactory = FileElementSupplierFactory.from(arguments.getElementType());
        comparator = arguments.getSortDirection() == SortDirection.DESC ?
                Comparator.reverseOrder() : Comparator.naturalOrder();
    }

    @Override
    public void sort() {
        try {
            sortInputFilesAndWriteOutputFile();
            Writer.write(String.format("Результат в файле: %s", outputFile.toAbsolutePath()));
        } catch (IOException e) {
            Writer.write(String.format("Не удачное чтение/запись файла(ов). %s", e.getMessage()));
        }
    }

    private void sortInputFilesAndWriteOutputFile() throws IOException {
        List<ElementSupplier> elementSuppliers = new ArrayList<>(inputFiles.size());

        try {
            for (Path inputFile : inputFiles) {
                elementSuppliers.add(fileElementSupplierFactory.create(inputFile));
            }

            ElementSelector elementSelectorWithComparator = ElementSelector.from(elementSuppliers, comparator);
            useElementSelectorToWriteOutputFile(elementSelectorWithComparator);
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
                    Writer.write(String.format("Сбой закрытия потока (входного файла). %s", e.getMessage()));
                }
            }
        }
    }

    private void useElementSelectorToWriteOutputFile(ElementSelector elementSelector) throws IOException {
        try (BufferedWriter outputFileWriter = Files.newBufferedWriter(outputFile)) {
            while (elementSelector.hasNext()) {
                FileElement fileElement = (FileElement) elementSelector.next();
                writeNextElementToFileOrIgnore(fileElement, outputFileWriter);
            }
        } catch (IllegalAccessException e) {
            Writer.write(String.format("%s", e.getMessage()));
        } finally {
            if (IgnoredFileElementCount > 0) {
                Writer.write(String.format("Были проигнорированны строки нарушающие сортировку "
                        + "в исходных файлах - %d шт.", IgnoredFileElementCount));
            }
        }
    }

    private void writeNextElementToFileOrIgnore(FileElement fileElement, BufferedWriter fileWriter)
            throws IOException {
        if (isUnsortedFileElementsIgnore) {
            if (previousFileElement == null || comparator.compare(fileElement, previousFileElement) >= 0) {
                fileWriter.write(fileElement.toWritableFormat());
                previousFileElement = fileElement;
            } else {
                IgnoredFileElementCount++;
            }
        } else {
            fileWriter.write(fileElement.toWritableFormat());
            previousFileElement = fileElement;
        }
    }
}