package ru.cft.drozdrtskiy22.merge_files_sorting.supplier.file;

import ru.cft.drozdrtskiy22.merge_files_sorting.element.Element;
import ru.cft.drozdrtskiy22.merge_files_sorting.element.file.StringFileElement;

import java.io.IOException;
import java.nio.file.Path;

public final class StringFileElementSupplier extends FileElementSupplier {

//    private final LineIterator lineIterator;
//    private final Path path;
//    private int invalidLinesCount;

    public static StringFileElementSupplier fromFile(Path path) throws IOException {
        return new StringFileElementSupplier(path);
    }

    private StringFileElementSupplier(Path path) throws IOException {
        super(path);
//        lineIterator = new LineIterator(Files.newBufferedReader(path));
//        this.path = path;
    }

    @Override
    public Element next() {
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

//    @Override
//    public void close() {
//        if (invalidLinesCount > 0) {
//            System.out.printf("В файле %s содержались ошибочные строки - %d шт.%n", path.getFileName(), invalidLinesCount);
//        }
//
//        if (lineIterator != null) {
//            try {
//                lineIterator.close();
//            } catch (IOException e) {
//                System.out.printf("Закрытие файла %s Что-то пошло не так. %s%n", path.getFileName(), e.getMessage());
//            }
//        }
//    }
}
