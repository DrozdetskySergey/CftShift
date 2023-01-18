package ru.cft.drozdrtskiy.sorting.supplier;

import ru.cft.drozdrtskiy.sorting.element.Element;

public interface ElementSupplier extends AutoCloseable {

    Element next();
}
