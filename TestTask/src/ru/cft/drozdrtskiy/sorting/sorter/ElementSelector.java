package ru.cft.drozdrtskiy.sorting.sorter;

import ru.cft.drozdrtskiy.sorting.element.Element;
import ru.cft.drozdrtskiy.sorting.supplier.ElementSupplier;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class ElementSelector {

    private final List<ElementSupplier> suppliers;
    private int size;
    private final List<Element> oneFromEachSupplierElements;
    private final Comparator<Element> comparator;

    public static ElementSelector from(List<ElementSupplier> suppliers, Comparator<Element> comparator) {
        return new ElementSelector(suppliers, comparator);
    }

    private ElementSelector(List<ElementSupplier> suppliers, Comparator<Element> comparator) {
        this.suppliers = new ArrayList<>(suppliers);
        size = suppliers.size();
        oneFromEachSupplierElements = getFirstElementsFromSuppliers();
        this.comparator = comparator;
    }

    private List<Element> getFirstElementsFromSuppliers() {
        List<Element> result = new ArrayList<>(size);

        for (int i = 0; i < size; ) {
            Element element = suppliers.get(i).next();

            if (element != null) {
                result.add(element);
                i++;
            } else {
                suppliers.remove(i);
                size--;
            }
        }

        return result;
    }

    public boolean hasNext() {
        return size > 0;
    }

    public Element next() throws IllegalAccessException {
        if (size == 0) {
            throw new IllegalAccessException("Отсутствует постовщик элементов.");
        }

        Element result = oneFromEachSupplierElements.get(0);
        int index = 0;

        for (int i = 1; i < size; i++) {
            if (comparator.compare(result, oneFromEachSupplierElements.get(i)) > 0) {
                result = oneFromEachSupplierElements.get(i);
                index = i;
            }
        }

        updateElementOrRemoveSupplier(index);

        return result;
    }

    private void updateElementOrRemoveSupplier(int index) {
        Element element = suppliers.get(index).next();

        if (element != null) {
            oneFromEachSupplierElements.set(index, element);
        } else {
            suppliers.remove(index);
            oneFromEachSupplierElements.remove(index);
            size--;
        }
    }
}
