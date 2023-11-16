package ru.cft.drozdrtskiy.sorting.writer.file.impl;

import ru.cft.drozdrtskiy.sorting.element.file.FileElement;
import ru.cft.drozdrtskiy.sorting.writer.file.FileElementWriter;

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
