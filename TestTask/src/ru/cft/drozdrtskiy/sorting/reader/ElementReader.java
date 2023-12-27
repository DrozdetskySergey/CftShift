package ru.cft.drozdrtskiy.sorting.reader;

import ru.cft.drozdrtskiy.sorting.element.Element;

public interface ElementReader extends AutoCloseable {

    Element next();
}
