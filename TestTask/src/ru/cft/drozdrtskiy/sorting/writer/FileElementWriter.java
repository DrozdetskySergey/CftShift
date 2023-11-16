package ru.cft.drozdrtskiy.sorting.writer;

import ru.cft.drozdrtskiy.sorting.element.FileElement;

import java.io.IOException;

public interface FileElementWriter extends AutoCloseable {

    void write(FileElement fileElement) throws IOException;
}
