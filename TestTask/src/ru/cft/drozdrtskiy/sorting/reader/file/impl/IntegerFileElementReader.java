package ru.cft.drozdrtskiy.sorting.reader.file.impl;

import ru.cft.drozdrtskiy.sorting.element.file.impl.IntegerFileElement;

import java.io.IOException;
import java.nio.file.Path;

final class IntegerFileElementReader extends AbstractFileElementReader {

    public IntegerFileElementReader(Path path) throws IOException {
        super(path);
    }

    @Override
    public IntegerFileElement next() {
        IntegerFileElement result = null;

        while (lineIterator.hasNext() && result == null) {
            String line = lineIterator.nextLine();

            if (isInvalidLine(line)) {
                invalidFileElementCount++;
            } else {
                try {
                    result = new IntegerFileElement(Integer.valueOf(line));
                } catch (NumberFormatException e) {
                    invalidFileElementCount++;
                }
            }
        }

        return result;
    }
}