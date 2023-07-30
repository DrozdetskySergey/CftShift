package ru.cft.drozdrtskiy.sorting.selector;

import ru.cft.drozdrtskiy.sorting.element.Element;
import ru.cft.drozdrtskiy.sorting.supplier.ElementSupplier;

final class ElementExtractor<E extends Element> {

    private final ElementSupplier<E> elementSupplier;
    private E element;

    public ElementExtractor(ElementSupplier<E> elementSupplier) {
        this.elementSupplier = elementSupplier;
        element = elementSupplier.next();
    }

    public E getElement() {
        return element;
    }

    public void update() {
        element = elementSupplier.next();
    }
}
