package ru.cft.drozdrtskiy.sorting.element.impl;

import ru.cft.drozdrtskiy.sorting.element.Element;

public final class IntegerFileElement extends AbstractFileElement<Integer> {

    public IntegerFileElement(Integer value) {
        super(value);
    }

    @Override
    public int compareTo(Element o) {
        return value.compareTo(((IntegerFileElement) o).value);
    }
}
