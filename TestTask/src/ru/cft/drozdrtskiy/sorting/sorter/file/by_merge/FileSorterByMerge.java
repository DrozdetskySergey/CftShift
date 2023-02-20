package ru.cft.drozdrtskiy.sorting.sorter.file.by_merge;

import ru.cft.drozdrtskiy.sorting.DTO.FileSorterArgumentsDTO;
import ru.cft.drozdrtskiy.sorting.argument.SortDirection;
import ru.cft.drozdrtskiy.sorting.element.Element;
import ru.cft.drozdrtskiy.sorting.element.file.FileElement;
import ru.cft.drozdrtskiy.sorting.sorter.Sorter;
import ru.cft.drozdrtskiy.sorting.sorter.selector.ElementSelector;
import ru.cft.drozdrtskiy.sorting.supplier.ElementSupplier;
import ru.cft.drozdrtskiy.sorting.supplier.file.FileElementSupplierFactory;
import ru.cft.drozdrtskiy.sorting.util.Writer;

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
    private int IgnoredFileElementCount;
    private FileElement previousFileElement;

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
            Writer.write(String.format("Результат в файле: %s", outputFile.toAbsolutePath()));
        } catch (IOException e) {
            Writer.write(String.format("Не удачное чтение/запись файла(ов). %s", e.getMessage()));
        }
    }

    private void sortInputFilesAndWriteOutputFile() throws IOException {
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
                    Writer.write(e.getMessage());
                }
            }
        }
    }

    private void useElementSelectorToWriteOutputFile(ElementSelector elementSelector) throws IOException {
        try (BufferedWriter fileWriter = Files.newBufferedWriter(outputFile)) {
            while (elementSelector.hasNext()) {
                FileElement fileElement = (FileElement) elementSelector.next();
                writeFileElementToFileOrIgnore(fileElement, fileWriter);
            }
        } catch (IllegalAccessException e) {
            Writer.write(String.format("Неожиданная потеря доступа до элементов. %s", e.getMessage()));
        } finally {
            if (IgnoredFileElementCount > 0) {
                Writer.write(String.format("Были проигнорированны строки нарушающие сортировку "
                        + "в исходных файлах - %d шт.", IgnoredFileElementCount));
            }
        }
    }

    private void writeFileElementToFileOrIgnore(FileElement fileElement, BufferedWriter fileWriter) throws IOException {
        boolean isNotNeedToIgnore = !isUnsortedFileElementsIgnore;

        if (isNotNeedToIgnore) {
            fileWriter.write(fileElement.toWritableFormat());
        } else if (previousFileElement == null || comparator.compare(fileElement, previousFileElement) >= 0) {
            fileWriter.write(fileElement.toWritableFormat());
            previousFileElement = fileElement;
        } else {
            IgnoredFileElementCount++;
        }
    }
}