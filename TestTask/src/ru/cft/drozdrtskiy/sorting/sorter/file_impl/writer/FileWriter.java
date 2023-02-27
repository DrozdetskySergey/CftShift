package ru.cft.drozdrtskiy.sorting.sorter.file_impl.writer;

import ru.cft.drozdrtskiy.sorting.element.FileElement;

import java.io.IOException;

public interface FileWriter extends AutoCloseable {

    void write(FileElement fileElement) throws IOException;
}
