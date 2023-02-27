package ru.cft.drozdrtskiy.sorting.element.file_impl;

import ru.cft.drozdrtskiy.sorting.element.Element;
import ru.cft.drozdrtskiy.sorting.element.FileElement;

abstract class AbstractFileElement<T> implements FileElement {

    protected final T value;

    protected AbstractFileElement(T value) {
        this.value = value;
    }

    @Override
    public String toWritableFormat() {
        return String.format("%s%n", value);
    }

    @Override
    public abstract int compareTo(Element o);
}
