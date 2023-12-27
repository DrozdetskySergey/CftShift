package ru.cft.drozdrtskiy.sorting.sorter;

import ru.cft.drozdrtskiy.sorting.element.Element;
import ru.cft.drozdrtskiy.sorting.reader.ElementReader;

final class ElementExtractor {

    private final ElementReader elementReader;
    private Element element;

    public ElementExtractor(ElementReader elementReader) {
        this.elementReader = elementReader;
        element = elementReader.next();
    }

    public Element getElement() {
        return element;
    }

    public void update() {
        element = elementReader.next();
    }
}
