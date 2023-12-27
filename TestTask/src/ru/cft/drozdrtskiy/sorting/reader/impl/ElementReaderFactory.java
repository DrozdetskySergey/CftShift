package ru.cft.drozdrtskiy.sorting.reader.impl;

import ru.cft.drozdrtskiy.sorting.ElementType;
import ru.cft.drozdrtskiy.sorting.reader.ElementReader;

import java.io.IOException;
import java.nio.file.Path;

public final class ElementReaderFactory {

    private final ElementType elementType;

    public static ElementReaderFactory from(ElementType elementType) {
        return new ElementReaderFactory(elementType);
    }

    private ElementReaderFactory(ElementType elementType) {
        this.elementType = elementType;
    }

    public ElementReader createFor(Path path) throws IOException {
        ElementReader result = null;

        if (elementType == ElementType.INTEGER) {
            result = new IntegerFileElementReader(path);
        } else if (elementType == ElementType.STRING) {
            result = new StringFileElementReader(path);
        }

        return result;
    }
}
