package ru.cft.drozdrtskiy.sorting.sorter;

import ru.cft.drozdrtskiy.sorting.element.Element;
import ru.cft.drozdrtskiy.sorting.supplier.ElementSupplier;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class SuppliersElementSelector {

    private final List<ElementSupplier> suppliers;
    private int size;
    private final List<Element> elements;
    private final Comparator<Element> comparator;

    public static SuppliersElementSelector from(List<ElementSupplier> suppliers
            , Comparator<Element> comparator) throws Exception {
        return new SuppliersElementSelector(suppliers, comparator);
    }

    private SuppliersElementSelector(List<ElementSupplier> suppliers, Comparator<Element> comparator) throws Exception {
        this.suppliers = suppliers;
        size = suppliers.size();
        elements = getFirstElements();
        this.comparator = comparator;
    }

    private List<Element> getFirstElements() throws Exception {
        List<Element> result = new ArrayList<>(size);

        for (int i = 0; i < size; ) {
            Element element = suppliers.get(i).next();

            if (element == null) {
                closeElementSupplier(suppliers.remove(i));
                size--;
            } else {
                result.add(element);
                i++;
            }
        }

        return result;
    }

    public boolean hasNext() {
        return size > 0;
    }

    public Element next() throws Exception {
        if (size == 0) {
            throw new IllegalAccessException("Отсутствует источник элементов.");
        }

        Element result = elements.get(0);
        int index = 0;

        for (int i = 1; i < size; i++) {
            if (comparator.compare(result, elements.get(i)) > 0) {
                result = elements.get(i);
                index = i;
            }
        }

        updateElementOrCloseSupplier(index);

        return result;
    }

    private void updateElementOrCloseSupplier(int index) throws Exception {
        Element element = suppliers.get(index).next();

        if (element == null) {
            closeElementSupplier(suppliers.remove(index));
            elements.remove(index);
            size--;
        } else {
            elements.set(index, element);
        }
    }

    private void closeElementSupplier(ElementSupplier elementSupplier) throws Exception {
        if (elementSupplier != null) {
            elementSupplier.close();
        }
    }
}
