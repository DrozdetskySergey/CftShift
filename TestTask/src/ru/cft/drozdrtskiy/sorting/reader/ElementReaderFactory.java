package ru.cft.drozdrtskiy.sorting.reader;

import java.io.IOException;
import java.nio.file.Path;

public interface ElementReaderFactory {

    ElementReader createFor(Path path) throws IOException;
}