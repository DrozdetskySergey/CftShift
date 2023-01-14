package ru.cft.drozdrtskiy22.merge_files_sorting.supplier;

import org.apache.commons.io.LineIterator;
import ru.cft.drozdrtskiy22.merge_files_sorting.element.FileElement;
import ru.cft.drozdrtskiy22.merge_files_sorting.element.IntegerFileElement;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class IntegerFileElementSupplier implements FileElementSupplier {

    private final LineIterator lineIterator;
    private final Path path;
    private int invalidLinesCount;

    public static IntegerFileElementSupplier forFile(Path path) throws IOException {
        return new IntegerFileElementSupplier(path);
    }

    private IntegerFileElementSupplier(Path path) throws IOException {
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

            try {

                return new IntegerFileElement(Integer.valueOf(line));
            } catch (NumberFormatException e) {
                invalidLinesCount++;
            }
        }

        return null;
    }

    @Override
    public void close() {
        if (invalidLinesCount > 0) {
            System.out.printf("В файле %s содержались ошибочные строки - %d шт.%n", path.getFileName(), invalidLinesCount);
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
