package ru.cft.drozdrtskiy.sorting.supplier.file;

import org.apache.commons.io.LineIterator;
import ru.cft.drozdrtskiy.sorting.element.file.FileElement;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class AbstractFileElementSupplier implements FileElementSupplier {

    protected final LineIterator lineIterator;
    protected final Path path;
    protected int invalidFileElementCount;

    protected AbstractFileElementSupplier(Path path) throws IOException {
        lineIterator = new LineIterator(Files.newBufferedReader(path));
        this.path = path;
    }

    protected boolean isInvalidLine(String line) {
        return line.isBlank() || line.contains(" ");
    }

    @Override
    public abstract FileElement next();

    @Override
    public void close() throws Exception {
        StringBuilder exceptionMessage = new StringBuilder();

        if (invalidFileElementCount > 0) {
            exceptionMessage.append(String.format("Файл %s. Проигнорированно ошибочных строк - %d шт. "
                    , path.getFileName(), invalidFileElementCount));
        }

        if (lineIterator != null) {
            try {
                lineIterator.close();
            } catch (IOException e) {
                exceptionMessage.append(String.format("Сбой закрытия потока чтения. %s", e.getMessage()));
            }
        }

        if (exceptionMessage.length() > 0) {
            throw new Exception(exceptionMessage.toString());
        }
    }
}
