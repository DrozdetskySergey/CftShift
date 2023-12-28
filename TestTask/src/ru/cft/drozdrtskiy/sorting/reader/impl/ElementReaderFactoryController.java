package ru.cft.drozdrtskiy.sorting.reader.impl;

import ru.cft.drozdrtskiy.sorting.ElementType;
import ru.cft.drozdrtskiy.sorting.reader.ElementReaderFactory;

public final class ElementReaderFactoryController {

    public static ElementReaderFactory build(ElementType elementType) {
        return elementType == ElementType.INTEGER ?
                new IntegerFileElementReaderFactory() :
                new StringFileElementReaderFactory();
    }
}
