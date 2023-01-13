package ru.cft.drozdrtskiy22.merge_files_sorting.supplier;

import ru.cft.drozdrtskiy22.merge_files_sorting.element.FileElement;

public interface FileElementSupplier extends AutoCloseable {

    FileElement next();
}
