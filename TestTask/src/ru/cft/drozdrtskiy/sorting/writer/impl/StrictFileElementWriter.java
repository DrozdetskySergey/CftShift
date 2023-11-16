package ru.cft.drozdrtskiy.sorting.writer.impl;

import ru.cft.drozdrtskiy.sorting.element.file.FileElement;
import ru.cft.drozdrtskiy.sorting.writer.FileElementWriter;

import static ru.cft.drozdrtskiy.sorting.util.MessagePrinter.print;

import java.io.*;
import java.util.Comparator;

public final class StrictFileElementWriter implements FileElementWriter {

    private final Comparator<FileElement> comparator;
    private final BufferedWriter fileWriter;
    private int IgnoredFileElementCount;
    private FileElement previousFileElement;

    public StrictFileElementWriter(BufferedWriter fileWriter, Comparator<FileElement> comparator) {
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
            print(String.format("Были проигнорированны строки нарушающие сортировку "
                    + "в исходных файлах - %d шт.", IgnoredFileElementCount));
        }
    }
}
