package ru.cft.drozdrtskiy.sorting.writer.impl;

import ru.cft.drozdrtskiy.sorting.element.FileElement;
import ru.cft.drozdrtskiy.sorting.writer.FileElementWriter;

import java.io.*;

public final class SimpleFileElementWriter implements FileElementWriter {

    private final BufferedWriter fileWriter;

    public SimpleFileElementWriter(BufferedWriter fileWriter) {
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
