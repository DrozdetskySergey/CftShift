package ru.cft.drozdrtskiy22.merge_files_sorting.supplier;

import ru.cft.drozdrtskiy22.merge_files_sorting.element.StringFileElement;

import java.io.IOException;
import java.nio.file.Path;

public final class StringFileElementSupplier extends FileElementSupplier {

    public static StringFileElementSupplier fromFile(Path path) throws IOException {
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
