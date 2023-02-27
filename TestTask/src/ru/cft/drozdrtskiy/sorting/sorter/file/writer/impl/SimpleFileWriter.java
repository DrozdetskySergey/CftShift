package ru.cft.drozdrtskiy.sorting.sorter.file.writer.impl;

import ru.cft.drozdrtskiy.sorting.element.file.FileElement;
import ru.cft.drozdrtskiy.sorting.sorter.file.writer.FileWriter;

import java.io.*;

public final class SimpleFileWriter implements FileWriter {

    private final BufferedWriter fileWriter;

    public static SimpleFileWriter from(BufferedWriter fileWriter) {
        return new SimpleFileWriter(fileWriter);
    }

    private SimpleFileWriter(BufferedWriter fileWriter) {
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
