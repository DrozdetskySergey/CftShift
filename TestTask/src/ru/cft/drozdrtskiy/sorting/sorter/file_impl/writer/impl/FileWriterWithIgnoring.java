package ru.cft.drozdrtskiy.sorting.sorter.file_impl.writer.impl;

import ru.cft.drozdrtskiy.sorting.element.Element;
import ru.cft.drozdrtskiy.sorting.element.FileElement;
import ru.cft.drozdrtskiy.sorting.sorter.file_impl.writer.FileWriter;
import ru.cft.drozdrtskiy.sorting.util.MessagePrinter;

import java.io.*;
import java.util.Comparator;

public final class FileWriterWithIgnoring implements FileWriter {

    private final Comparator<Element> comparator;
    private final BufferedWriter fileWriter;
    private int IgnoredFileElementCount;
    private FileElement previousFileElement;

    public static FileWriterWithIgnoring from(BufferedWriter fileWriter, Comparator<Element> comparator) {
        return new FileWriterWithIgnoring(fileWriter, comparator);
    }

    private FileWriterWithIgnoring(BufferedWriter fileWriter, Comparator<Element> comparator) {
        this.comparator = comparator;
        this.fileWriter = fileWriter;
    }

    @Override
    public void write(FileElement fileElement) throws IOException {
        if (previousFileElement == null || comparator.compare(fileElement, previousFileElement) >= 0) {
            fileWriter.write(fileElement.toWritableFormat());
            previousFileElement = fileElement;
        } else {
            IgnoredFileElementCount++;
        }
    }

    @Override
    public void close() {
        if (IgnoredFileElementCount > 0) {
            MessagePrinter.print(String.format("Были проигнорированны строки нарушающие сортировку "
                    + "в исходных файлах - %d шт.", IgnoredFileElementCount));
        }
    }
}
