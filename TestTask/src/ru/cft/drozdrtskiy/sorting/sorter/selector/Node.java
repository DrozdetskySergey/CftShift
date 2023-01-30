package ru.cft.drozdrtskiy.sorting.sorter.selector;

import ru.cft.drozdrtskiy.sorting.element.Element;
import ru.cft.drozdrtskiy.sorting.supplier.ElementSupplier;

final class Node {

    private final ElementSupplier elementSupplier;
    private Element element;

    public static Node from(ElementSupplier elementSupplier) {
        return new Node(elementSupplier);
    }

    public Node(ElementSupplier elementSupplier) {
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
