package ru.cft.drozdrtskiy.sorting.writer.impl;

import ru.cft.drozdrtskiy.sorting.element.Element;
import ru.cft.drozdrtskiy.sorting.writer.ElementWriter;

import java.io.BufferedWriter;
import java.io.IOException;

public final class SimpleFileElementWriter implements ElementWriter {

    private final BufferedWriter fileWriter;

    public SimpleFileElementWriter(BufferedWriter fileWriter) {
        this.fileWriter = fileWriter;
    }

    @Override
    public void write(Element element) throws IOException {
        fileWriter.write(element.toString());
    }

    @Override
    public void close() {
    }
}
