package ru.cft.drozdrtskiy.sorting.reader.impl;

import ru.cft.drozdrtskiy.sorting.reader.ElementReader;
import ru.cft.drozdrtskiy.sorting.reader.ElementReaderFactory;

import java.io.IOException;
import java.nio.file.Path;

final class StringFileElementReaderFactory implements ElementReaderFactory {

    @Override
    public ElementReader createFor(Path path) throws IOException {
        return new StringFileElementReader(path);
    }
}
