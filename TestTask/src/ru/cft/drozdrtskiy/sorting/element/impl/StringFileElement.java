package ru.cft.drozdrtskiy.sorting.element.impl;

import ru.cft.drozdrtskiy.sorting.element.Element;

public final class StringFileElement extends FileElement<String> {

    public StringFileElement(String value) {
        super(value);
    }

    @Override
    public int compareTo(Element o) {
        return value.compareTo(((StringFileElement) o).value);
    }
}
