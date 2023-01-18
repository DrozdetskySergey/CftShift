package ru.cft.drozdrtskiy.sorting.supplier.file;

import org.apache.commons.io.LineIterator;
import ru.cft.drozdrtskiy.sorting.element.file.FileElement;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class AbstractFileElementSupplier implements FileElementSupplier {

    protected final LineIterator lineIterator;
    protected final Path path;
    protected int invalidLinesCount;

    protected AbstractFileElementSupplier(Path path) throws IOException {
        lineIterator = new LineIterator(Files.newBufferedReader(path));
        this.path = path;
    }

    @Override
    public abstract FileElement next();

    protected boolean isInvalidLine(String line) {
        return line.isBlank() || line.contains(" ");
    }

    @Override
    public void close() {
        if (invalidLinesCount > 0) {
            System.out.printf("В файле %s проигнорированны ошибочные строки - %d шт.%n", path.getFileName(), invalidLinesCount);
        }

        if (lineIterator != null) {
            try {
                lineIterator.close();
            } catch (IOException e) {
                System.out.printf("Закрытие файла %s Что-то пошло не так. %s%n", path.getFileName(), e.getMessage());
            }
        }
    }
}