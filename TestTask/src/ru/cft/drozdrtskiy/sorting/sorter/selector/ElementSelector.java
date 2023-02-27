package ru.cft.drozdrtskiy.sorting.sorter.selector;

import ru.cft.drozdrtskiy.sorting.element.Element;
import ru.cft.drozdrtskiy.sorting.supplier.ElementSupplier;

import java.util.*;

public final class ElementSelector {

    private final List<ElementExtractor> elementExtractors;
    private final Comparator<Element> comparator;

    public static ElementSelector from(List<ElementSupplier> suppliers, Comparator<Element> comparator) {
        return new ElementSelector(suppliers, comparator);
    }

    private ElementSelector(List<ElementSupplier> suppliers, Comparator<Element> comparator) {
        elementExtractors = new ArrayList<>(suppliers.size());
        fillElementExtractors(suppliers);
        this.comparator = comparator;
    }

    private void fillElementExtractors(List<ElementSupplier> suppliers) {
        for (ElementSupplier supplier : suppliers) {
            ElementExtractor elementExtractor = ElementExtractor.from(supplier);

            if (elementExtractor.getElement() != null) {
                elementExtractors.add(elementExtractor);
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

        updateOrRemoveElementExtractor(elementExtractorIndex);

        return element;
    }

    private void updateOrRemoveElementExtractor(int index) {
        ElementExtractor elementExtractor = elementExtractors.get(index);
        elementExtractor.update();

        if (elementExtractor.getElement() == null) {
            elementExtractors.remove(index);
        }
    }
}
