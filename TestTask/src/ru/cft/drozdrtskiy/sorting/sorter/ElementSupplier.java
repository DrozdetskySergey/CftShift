package ru.cft.drozdrtskiy.sorting.sorter;

import ru.cft.drozdrtskiy.sorting.element.Element;
import ru.cft.drozdrtskiy.sorting.reader.ElementReader;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class ElementSupplier {

    private final List<ElementExtractor> elementExtractors;
    private final Comparator<Element> comparator;

    public ElementSupplier(List<ElementReader> elementReaders, Comparator<Element> comparator) {
        this.comparator = comparator;
        elementExtractors = new ArrayList<>(elementReaders.size());
        fillElementExtractors(elementReaders);
    }

    private void fillElementExtractors(List<ElementReader> elementReaders) {
        for (ElementReader reader : elementReaders) {
            if (reader != null) {
                ElementExtractor elementExtractor = new ElementExtractor(reader);

                if (elementExtractor.getElement() != null) {
                    elementExtractors.add(elementExtractor);
                }
            }
        }
    }

    public Element next() {
        if (elementExtractors.size() == 0) {
            return null;
        }

        ElementExtractor firstExtractor = elementExtractors.get(0);
        Element element = firstExtractor.getElement();
        int elementExtractorIndex = 0;

        for (int i = 1; i < elementExtractors.size(); i++) {
            ElementExtractor anotherExtractor = elementExtractors.get(i);
            Element anotherElement = anotherExtractor.getElement();

            if (comparator.compare(element, anotherElement) > 0) {
                element = anotherElement;
                elementExtractorIndex = i;
            }
        }

        updateOrRemoveElementExtractorByIndex(elementExtractorIndex);

        return element;
    }

    private void updateOrRemoveElementExtractorByIndex(int index) {
        ElementExtractor elementExtractor = elementExtractors.get(index);
        elementExtractor.update();

        if (elementExtractor.getElement() == null) {
            elementExtractors.remove(index);
        }
    }
}
