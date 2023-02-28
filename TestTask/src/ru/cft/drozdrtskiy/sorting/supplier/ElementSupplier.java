package ru.cft.drozdrtskiy.sorting.supplier;

import ru.cft.drozdrtskiy.sorting.element.Element;

public interface ElementSupplier<E extends Element> extends AutoCloseable {

    E next();
}
