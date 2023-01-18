package ru.cft.drozdrtskiy.sorting.supplier.file;

import ru.cft.drozdrtskiy.sorting.element.file.FileElement;
import ru.cft.drozdrtskiy.sorting.supplier.ElementSupplier;

public interface FileElementSupplier extends ElementSupplier {

    FileElement next();
}
