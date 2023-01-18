package ru.cft.drozdrtskiy.sorting.supplier.file.impl;

import ru.cft.drozdrtskiy.sorting.element.file.impl.StringFileElement;
import ru.cft.drozdrtskiy.sorting.supplier.file.AbstractFileElementSupplier;

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
                invalidLinesCount++;
                continue;
            }

            return new StringFileElement(line);
        }

        return null;
    }
}
