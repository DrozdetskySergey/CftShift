package ru.cft.drozdrtskiy.sorting.element.impl;

import ru.cft.drozdrtskiy.sorting.element.Element;

abstract class FileElement<T> implements Element {

    protected final T value;

    protected FileElement(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("%s%n", value);
    }

    @Override
    public abstract int compareTo(Element o);
}
