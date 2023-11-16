package ru.cft.drozdrtskiy.sorting.reader.file;

import ru.cft.drozdrtskiy.sorting.element.file.FileElement;
import ru.cft.drozdrtskiy.sorting.reader.ElementReader;

public interface FileElementReader extends ElementReader<FileElement> {

    @Override
    FileElement next();

    @Override
    void close() throws Exception;
}
