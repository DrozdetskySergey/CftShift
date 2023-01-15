package ru.cft.drozdrtskiy22.merge_files_sorting.supplier;

import ru.cft.drozdrtskiy22.merge_files_sorting.element.Element;

public interface ElementSupplier extends AutoCloseable {

    Element next();
}
