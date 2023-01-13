package ru.cft.drozdrtskiy22.merge_files_sorting.supplier;

import org.apache.commons.io.LineIterator;
import ru.cft.drozdrtskiy22.merge_files_sorting.element.FileElement;
import ru.cft.drozdrtskiy22.merge_files_sorting.element.StringFileElement;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class StringFileElementSupplier implements FileElementSupplier {

    private final LineIterator lineIterator;
    private final Path path;
    private int invalidLinesCount;

    public static StringFileElementSupplier forFile(Path path) throws IOException {
        return new StringFileElementSupplier(path);
    }

    private StringFileElementSupplier(Path path) throws IOException {
        lineIterator = new LineIterator(Files.newBufferedReader(path));
        this.path = path;
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
    public void close() {
        if (invalidLinesCount > 0) {
            System.out.println("Файл " + path.getFileName() + " содержал ошибочных строк = " + invalidLinesCount);
        }

        if (lineIterator != null) {
            try {
                lineIterator.close();
            } catch (IOException e) {
                System.out.println("Закрытие файлов. Что-то пошло не так." + e.getMessage());
            }
        }
    }
}
