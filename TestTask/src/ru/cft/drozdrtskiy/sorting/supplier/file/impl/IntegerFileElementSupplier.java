package ru.cft.drozdrtskiy.sorting.supplier.file.impl;

import ru.cft.drozdrtskiy.sorting.element.file.impl.IntegerFileElement;
import ru.cft.drozdrtskiy.sorting.supplier.file.AbstractFileElementSupplier;

import java.io.IOException;
import java.nio.file.Path;

public final class IntegerFileElementSupplier extends AbstractFileElementSupplier {

    public static IntegerFileElementSupplier from(Path path) throws IOException {
        return new IntegerFileElementSupplier(path);
    }

    private IntegerFileElementSupplier(Path path) throws IOException {
        super(path);
    }

    @Override
    public IntegerFileElement next() {
        while (lineIterator.hasNext()) {
            String line = lineIterator.nextLine();

            if (isInvalidLine(line)) {
                invalidLinesCount++;
                continue;
            }

            try {

                return new IntegerFileElement(Integer.valueOf(line));
            } catch (NumberFormatException e) {
                invalidLinesCount++;
            }
        }

        return null;
    }
}