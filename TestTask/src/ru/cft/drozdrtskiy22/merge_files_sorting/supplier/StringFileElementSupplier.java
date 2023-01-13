package ru.cft.drozdrtskiy22.merge_files_sorting.supplier;

import org.apache.commons.io.LineIterator;
import ru.cft.drozdrtskiy22.merge_files_sorting.element.FileElement;
import ru.cft.drozdrtskiy22.merge_files_sorting.element.StringFileElement;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class StringFileElementSupplier implements FileElementSupplier {

    private final LineIterator lineIterator;
    int invalidLinesCount;

    public static StringFileElementSupplier forFile(Path path) throws IOException {
        return new StringFileElementSupplier(path);
    }

    private StringFileElementSupplier(Path path) throws IOException {
        lineIterator = new LineIterator(Files.newBufferedReader(path));
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

            return new StringFileElement(line);
        }

        return null;
    }

    @Override
    public void close() throws IOException {
        if (lineIterator != null) {
            lineIterator.close();
        }

        System.out.println("invalidLinesCount = " + invalidLinesCount);
    }
}
