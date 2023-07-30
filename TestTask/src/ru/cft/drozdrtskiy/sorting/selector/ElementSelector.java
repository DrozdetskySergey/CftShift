package ru.cft.drozdrtskiy.sorting.selector;

import ru.cft.drozdrtskiy.sorting.element.Element;
import ru.cft.drozdrtskiy.sorting.supplier.ElementSupplier;

import java.util.*;

public final class ElementSelector<E extends Element> {

    private final List<ElementExtractor<E>> elementExtractors;
    private final Comparator<E> comparator;

    public ElementSelector(List<ElementSupplier<E>> elementSuppliers, Comparator<E> comparator) {
        this.comparator = comparator;
        elementExtractors = new ArrayList<>(elementSuppliers.size());
        fillElementExtractors(elementSuppliers);
    }

    private void fillElementExtractors(List<ElementSupplier<E>> elementSuppliers) {
        for (ElementSupplier<E> supplier : elementSuppliers) {
            ElementExtractor<E> elementExtractor = new ElementExtractor<>(supplier);

            if (elementExtractor.getElement() != null) {
                elementExtractors.add(elementExtractor);
            }
        }
    }

    public E next() {
        if (elementExtractors.size() == 0) {
            return null;
        }

        ElementExtractor<E> firstExtractor = elementExtractors.get(0);
        E element = firstExtractor.getElement();
        int elementExtractorIndex = 0;

        for (int i = 1; i < elementExtractors.size(); i++) {
            ElementExtractor<E> anotherExtractor = elementExtractors.get(i);
            E anotherElement = anotherExtractor.getElement();

            if (comparator.compare(element, anotherElement) > 0) {
                element = anotherElement;
                elementExtractorIndex = i;
            }
        }

        updateOrRemoveElementExtractorByIndex(elementExtractorIndex);

        return element;
    }

    private void updateOrRemoveElementExtractorByIndex(int index) {
        ElementExtractor<E> elementExtractor = elementExtractors.get(index);
        elementExtractor.update();

        if (elementExtractor.getElement() == null) {
            elementExtractors.remove(index);
        }
    }
}
