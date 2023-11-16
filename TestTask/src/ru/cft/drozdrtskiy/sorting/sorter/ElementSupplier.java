package ru.cft.drozdrtskiy.sorting.sorter;

import ru.cft.drozdrtskiy.sorting.element.Element;
import ru.cft.drozdrtskiy.sorting.reader.ElementReader;

import java.util.*;

public final class ElementSupplier<E extends Element> {

    private final List<ElementExtractor<E>> elementExtractors;
    private final Comparator<E> comparator;

    public ElementSupplier(List<ElementReader<E>> elementReaders, Comparator<E> comparator) {
        this.comparator = comparator;
        elementExtractors = new ArrayList<>(elementReaders.size());
        fillElementExtractors(elementReaders);
    }

    private void fillElementExtractors(List<ElementReader<E>> elementReaders) {
        for (ElementReader<E> reader : elementReaders) {
            ElementExtractor<E> elementExtractor = new ElementExtractor<>(reader);

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
