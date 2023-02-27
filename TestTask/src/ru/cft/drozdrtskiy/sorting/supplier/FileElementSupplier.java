package ru.cft.drozdrtskiy.sorting.supplier;

import ru.cft.drozdrtskiy.sorting.element.FileElement;

public interface FileElementSupplier extends ElementSupplier {

    @Override
    FileElement next();

    @Override
    void close() throws Exception;
}
