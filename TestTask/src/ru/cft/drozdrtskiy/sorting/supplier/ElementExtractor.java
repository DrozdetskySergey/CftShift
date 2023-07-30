package ru.cft.drozdrtskiy.sorting.supplier;

import ru.cft.drozdrtskiy.sorting.element.Element;
import ru.cft.drozdrtskiy.sorting.reader.ElementReader;

final class ElementExtractor<E extends Element> {

    private final ElementReader<E> elementReader;
    private E element;

    public ElementExtractor(ElementReader<E> elementReader) {
        this.elementReader = elementReader;
        element = elementReader.next();
    }

    public E getElement() {
        return element;
    }

    public void update() {
        element = elementReader.next();
    }
}
