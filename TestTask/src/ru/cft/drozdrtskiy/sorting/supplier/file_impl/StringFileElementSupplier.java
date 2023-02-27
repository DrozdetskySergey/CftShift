package ru.cft.drozdrtskiy.sorting.supplier.file_impl;

import ru.cft.drozdrtskiy.sorting.element.file_impl.StringFileElement;

import java.io.IOException;
import java.nio.file.Path;

public final class StringFileElementSupplier extends AbstractFileElementSupplier {

    public static StringFileElementSupplier from(Path path) throws IOException {
        return new StringFileElementSupplier(path);
    }

    private StringFileElementSupplier(Path path) throws IOException {
        super(path);
    }

    @Override
    public StringFileElement next() {
        while (lineIterator.hasNext()) {
            String line = lineIterator.nextLine();

            if (isInvalidLine(line)) {
                invalidFileElementCount++;
                continue;
            }

            return new StringFileElement(line);
        }

        return null;
    }
}
