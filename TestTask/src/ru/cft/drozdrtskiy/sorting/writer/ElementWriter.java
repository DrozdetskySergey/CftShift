package ru.cft.drozdrtskiy.sorting.writer;

import ru.cft.drozdrtskiy.sorting.element.Element;

public interface ElementWriter extends AutoCloseable {

    void write(Element element) throws Exception;
}
