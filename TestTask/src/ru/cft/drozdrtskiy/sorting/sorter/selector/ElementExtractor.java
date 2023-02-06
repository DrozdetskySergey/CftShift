package ru.cft.drozdrtskiy.sorting.sorter.selector;

import ru.cft.drozdrtskiy.sorting.element.Element;
import ru.cft.drozdrtskiy.sorting.supplier.ElementSupplier;

final class ElementExtractor {

    private final ElementSupplier elementSupplier;
    private Element element;

    public static ElementExtractor from(ElementSupplier elementSupplier) {
        return new ElementExtractor(elementSupplier);
    }

    public ElementExtractor(ElementSupplier elementSupplier) {
        this.elementSupplier = elementSupplier;
        element = elementSupplier.next();
    }

    public Element getElement() {
        return element;
    }

    public void update() {
        element = elementSupplier.next();
    }
}
