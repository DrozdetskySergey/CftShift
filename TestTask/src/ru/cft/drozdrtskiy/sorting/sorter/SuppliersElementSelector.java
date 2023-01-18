package ru.cft.drozdrtskiy.sorting.sorter;

import ru.cft.drozdrtskiy.sorting.argument.SortDirection;
import ru.cft.drozdrtskiy.sorting.element.Element;
import ru.cft.drozdrtskiy.sorting.supplier.ElementSupplier;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class SuppliersElementSelector {

    private final List<ElementSupplier> suppliers;
    private final List<Element> elements;
    private int size;
    private final Comparator<Element> comparator;

    public static SuppliersElementSelector from(List<ElementSupplier> suppliers
            , SortDirection sortDirection) throws Exception {
        return new SuppliersElementSelector(suppliers, sortDirection);
    }

    private SuppliersElementSelector(List<ElementSupplier> suppliers, SortDirection sortDirection) throws Exception {
        this.suppliers = suppliers;
        size = suppliers.size();
        elements = new ArrayList<>(size);
        comparator = sortDirection == SortDirection.DESC ? Comparator.reverseOrder() : Comparator.naturalOrder();

        readFirstElements();
    }

    private void readFirstElements() throws Exception {
        for (int i = 0; i < size; ) {
            Element element = suppliers.get(i).next();

            if (element == null) {
                closeElementSupplier(suppliers.remove(i));
                size--;
            } else {
                elements.add(element);
                i++;
            }
        }
    }

    public boolean getNext() {
        return size > 0;
    }

    public Element next() throws Exception {
        if (size == 0) {
            throw new IllegalAccessException("Отсутствуют поставщики элементов.");
        }

        Element result = elements.get(0);
        int index = 0;

        for (int i = 1; i < size; i++) {
            if (comparator.compare(result, elements.get(i)) > 0) {
                result = elements.get(i);
                index = i;
            }
        }

        readNextElementByIndex(index);

        return result;
    }

    private void readNextElementByIndex(int index) throws Exception {
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
