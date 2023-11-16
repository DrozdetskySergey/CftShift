package ru.cft.drozdrtskiy.sorting.reader;

import ru.cft.drozdrtskiy.sorting.element.file.FileElement;

public interface FileElementReader extends ElementReader<FileElement> {

    @Override
    FileElement next();

    @Override
    void close() throws Exception;
}
