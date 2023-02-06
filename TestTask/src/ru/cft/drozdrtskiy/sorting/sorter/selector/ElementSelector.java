package ru.cft.drozdrtskiy.sorting.sorter.selector;

import ru.cft.drozdrtskiy.sorting.element.Element;
import ru.cft.drozdrtskiy.sorting.supplier.ElementSupplier;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class ElementSelector {

    private List<ElementExtractor> elementExtractors;
    private final Comparator<Element> comparator;

    public static ElementSelector from(List<ElementSupplier> suppliers, Comparator<Element> comparator) {
        return new ElementSelector(suppliers, comparator);
    }

    private ElementSelector(List<ElementSupplier> suppliers, Comparator<Element> comparator) {
        createElementExtractors(suppliers);
        this.comparator = comparator;
    }

    private void createElementExtractors(List<ElementSupplier> suppliers) {
        elementExtractors = new ArrayList<>(suppliers.size());

        for (ElementSupplier supplier : suppliers) {
            ElementExtractor elementExtractor = ElementExtractor.from(supplier);

            if (elementExtractor.getElement() != null) {
                elementExtractors.add(elementExtractor);
            }
        }
    }

    public boolean hasNext() {
        return elementExtractors.size() > 0;
    }

    public Element next() throws IllegalAccessException {
        if (elementExtractors.size() == 0) {
            throw new IllegalAccessException("Отсутствует постовщик элементов.");
        }

        Element element = elementExtractors.get(0).getElement();
        int elementExtractorIndex = 0;

        for (int i = 1; i < elementExtractors.size(); i++) {
            Element nextElement = elementExtractors.get(i).getElement();

            if (comparator.compare(element, nextElement) > 0) {
                element = nextElement;
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
