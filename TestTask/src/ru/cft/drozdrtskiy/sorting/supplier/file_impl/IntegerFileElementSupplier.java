package ru.cft.drozdrtskiy.sorting.supplier.file_impl;

import ru.cft.drozdrtskiy.sorting.element.file_impl.IntegerFileElement;

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
                invalidFileElementCount++;
                continue;
            }

            try {

                return new IntegerFileElement(Integer.valueOf(line));
            } catch (NumberFormatException e) {
                invalidFileElementCount++;
            }
        }

        return null;
    }
}
