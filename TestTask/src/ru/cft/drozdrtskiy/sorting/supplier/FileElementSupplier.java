package ru.cft.drozdrtskiy.sorting.supplier;

import ru.cft.drozdrtskiy.sorting.element.FileElement;

public interface FileElementSupplier extends ElementSupplier<FileElement> {

    @Override
    FileElement next();

    @Override
    void close() throws Exception;
}
