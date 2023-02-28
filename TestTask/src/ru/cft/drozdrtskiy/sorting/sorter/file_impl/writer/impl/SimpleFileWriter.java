package ru.cft.drozdrtskiy.sorting.sorter.file_impl.writer.impl;

import ru.cft.drozdrtskiy.sorting.element.FileElement;
import ru.cft.drozdrtskiy.sorting.sorter.file_impl.writer.FileWriter;

import java.io.*;

public final class SimpleFileWriter implements FileWriter {

    private final BufferedWriter fileWriter;

    public SimpleFileWriter(BufferedWriter fileWriter) {
        this.fileWriter = fileWriter;
    }

    @Override
    public void write(FileElement fileElement) throws IOException {
        fileWriter.write(fileElement.toWritableFormat());
    }

    @Override
    public void close() {
    }
}
