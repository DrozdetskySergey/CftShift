package ru.cft.drozdrtskiy22.merge_files_sorting.supplier;

import org.apache.commons.io.LineIterator;
import ru.cft.drozdrtskiy22.merge_files_sorting.element.FileElement;
import ru.cft.drozdrtskiy22.merge_files_sorting.element.StringFileElement;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

public class StringFileElementSupplier implements FileElementSupplier {

    private final LineIterator lineIterator;
    private final Comparator<FileElement> comparator;
    private StringFileElement prevStringFileElement;

    public StringFileElementSupplier(Path path, Comparator<FileElement> comparator) throws IOException {
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
                StringFileElement stringFileElement = new StringFileElement(line);

                if (prevStringFileElement != null && comparator.compare(stringFileElement, prevStringFileElement) < 0) {
                    continue;
                }

                prevStringFileElement = stringFileElement;

                return stringFileElement;
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
