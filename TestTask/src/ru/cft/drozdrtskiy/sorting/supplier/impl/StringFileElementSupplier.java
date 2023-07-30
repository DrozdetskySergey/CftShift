package ru.cft.drozdrtskiy.sorting.supplier.impl;

import ru.cft.drozdrtskiy.sorting.element.file_impl.StringFileElement;

import java.io.IOException;
import java.nio.file.Path;

final class StringFileElementSupplier extends AbstractFileElementSupplier {

    public StringFileElementSupplier(Path path) throws IOException {
        super(path);
    }

    @Override
    public StringFileElement next() {
        StringFileElement result = null;

        while (lineIterator.hasNext() && result == null) {
            String line = lineIterator.nextLine();

            if (isInvalidLine(line)) {
                invalidFileElementCount++;
            } else {
                result = new StringFileElement(line);
            }
        }

        return result;
    }
}