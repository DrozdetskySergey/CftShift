package ru.cft.drozdrtskiy.sorting.writer.impl;


import ru.cft.drozdrtskiy.sorting.element.Element;
import ru.cft.drozdrtskiy.sorting.writer.ElementWriter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Comparator;

import static ru.cft.drozdrtskiy.sorting.util.MessagePrinter.print;

public final class StrictFileElementWriter implements ElementWriter {

    private final Comparator<Element> comparator;
    private final BufferedWriter fileWriter;
    private int IgnoredFileElementCount;
    private Element previousFileElement;

    public StrictFileElementWriter(BufferedWriter fileWriter, Comparator<Element> comparator) {
        this.comparator = comparator;
        this.fileWriter = fileWriter;
    }

    @Override
    public void write(Element element) throws IOException {
        if (previousFileElement == null || comparator.compare(element, previousFileElement) >= 0) {
            fileWriter.write(element.toString());
            previousFileElement = element;
        } else {
            IgnoredFileElementCount++;
        }
    }

    @Override
    public void close() {
        if (IgnoredFileElementCount > 0) {
            print(String.format("Были проигнорированны строки нарушающие сортировку "
                    + "в исходных файлах - %d шт.", IgnoredFileElementCount));
        }
    }
}
