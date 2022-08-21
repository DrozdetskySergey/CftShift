package ru.cft.drozdrtskiy22.merge_files_sorting.supplier;

import ru.cft.drozdrtskiy22.merge_files_sorting.element.FileElement;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import org.apache.commons.io.LineIterator; // 2.11.0
import ru.cft.drozdrtskiy22.merge_files_sorting.element.IntegerFileElement;

public class IntegerFileElementSupplier implements FileElementSupplier {

    private final LineIterator lineIterator;
    private final Comparator<FileElement> comparator;
    private IntegerFileElement prevIntegerFileElement;

    public IntegerFileElementSupplier(Path path, Comparator<FileElement> comparator) throws IOException {
        lineIterator = new LineIterator(Files.newBufferedReader(path));
        this.comparator = comparator;
    }

    @Override
    public FileElement next() {
        while (lineIterator.hasNext()) {
            String line = lineIterator.nextLine();

            if (line.isEmpty() || line.contains(" ")) {
                continue;
            }

            try {
                IntegerFileElement integerFileElement = new IntegerFileElement(Integer.valueOf(line));

                if (prevIntegerFileElement != null && comparator.compare(integerFileElement, prevIntegerFileElement) < 0) {
                    continue;
                }

                prevIntegerFileElement = integerFileElement;

                return integerFileElement;
            } catch (NumberFormatException ignored) {
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
