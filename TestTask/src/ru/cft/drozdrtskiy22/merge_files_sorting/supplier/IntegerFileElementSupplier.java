package ru.cft.drozdrtskiy22.merge_files_sorting.supplier;

import ru.cft.drozdrtskiy22.merge_files_sorting.element.FileElement;
import ru.cft.drozdrtskiy22.merge_files_sorting.element.IntegerFileElement;

import java.io.IOException;
import java.nio.file.Path;

public final class IntegerFileElementSupplier extends FileElementSupplier {

    public static IntegerFileElementSupplier fromFile(Path path) throws IOException {
        return new IntegerFileElementSupplier(path);
    }

    private IntegerFileElementSupplier(Path path) throws IOException {
        super(path);
    }

    @Override
    public FileElement next() {
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
