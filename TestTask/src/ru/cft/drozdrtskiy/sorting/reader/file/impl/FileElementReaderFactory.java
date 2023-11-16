package ru.cft.drozdrtskiy.sorting.reader.file.impl;

import ru.cft.drozdrtskiy.sorting.ElementType;
import ru.cft.drozdrtskiy.sorting.reader.file.FileElementReader;

import java.io.IOException;
import java.nio.file.Path;

public final class FileElementReaderFactory {

    private final ElementType elementType;

    public static FileElementReaderFactory from(ElementType elementType) {
        return new FileElementReaderFactory(elementType);
    }

    private FileElementReaderFactory(ElementType elementType) {
        this.elementType = elementType;
    }

    public FileElementReader createForFile(Path path) throws IOException {
        FileElementReader result = null;

        if (elementType == ElementType.INTEGER) {
            result = new IntegerFileElementReader(path);
        } else if (elementType == ElementType.STRING) {
            result = new StringFileElementReader(path);
        }

        return result;
    }
}
