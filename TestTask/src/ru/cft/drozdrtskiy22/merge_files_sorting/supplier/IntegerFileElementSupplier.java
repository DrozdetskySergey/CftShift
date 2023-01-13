package ru.cft.drozdrtskiy22.merge_files_sorting.supplier;

import org.apache.commons.io.LineIterator;
import ru.cft.drozdrtskiy22.merge_files_sorting.element.FileElement;
import ru.cft.drozdrtskiy22.merge_files_sorting.element.IntegerFileElement;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class IntegerFileElementSupplier implements FileElementSupplier {

    private final LineIterator lineIterator;
    int invalidLinesCount;

    public static IntegerFileElementSupplier forFile(Path path) throws IOException {
        return new IntegerFileElementSupplier(path);
    }

    private IntegerFileElementSupplier(Path path) throws IOException {
        lineIterator = new LineIterator(Files.newBufferedReader(path));
        invalidLinesCount = 0;
    }

    @Override
    public int getInvalidLinesCount() {
        return invalidLinesCount;
    }

    @Override
    public FileElement next() {
        while (lineIterator.hasNext()) {
            String line = lineIterator.nextLine();

            if (line.isEmpty() || line.contains(" ")) {
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

    @Override
    public void close() throws IOException {
        if (lineIterator != null) {
            lineIterator.close();
        }
    }
}
