package ru.cft.drozdrtskiy.sorting.sorter.file.writer;

import ru.cft.drozdrtskiy.sorting.element.file.FileElement;

import java.io.IOException;

public interface FileWriter extends AutoCloseable {

    void write(FileElement fileElement) throws IOException;
}
