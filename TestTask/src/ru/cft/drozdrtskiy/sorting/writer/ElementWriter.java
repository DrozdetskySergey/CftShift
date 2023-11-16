package ru.cft.drozdrtskiy.sorting.writer;

import ru.cft.drozdrtskiy.sorting.element.Element;

public interface ElementWriter<E extends  Element> extends AutoCloseable {

    void write(E element) throws Exception;
}
