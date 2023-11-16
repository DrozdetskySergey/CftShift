package ru.cft.drozdrtskiy.sorting.writer.file;

import ru.cft.drozdrtskiy.sorting.element.file.FileElement;
import ru.cft.drozdrtskiy.sorting.writer.ElementWriter;

import java.io.IOException;

public interface FileElementWriter extends ElementWriter<FileElement> {

    @Override
    void write(FileElement fileElement) throws IOException;

    @Override
    void close() throws Exception;
}
