package ru.cft.drozdrtskiy.sorting.reader.file.impl;

import ru.cft.drozdrtskiy.sorting.element.file.impl.StringFileElement;

import java.io.IOException;
import java.nio.file.Path;

final class StringFileElementReader extends AbstractFileElementReader {

    public StringFileElementReader(Path path) throws IOException {
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