package ru.cft.drozdrtskiy.sorting.reader;

import ru.cft.drozdrtskiy.sorting.element.Element;

public interface ElementReader<E extends Element> extends AutoCloseable {

    E next();
}
