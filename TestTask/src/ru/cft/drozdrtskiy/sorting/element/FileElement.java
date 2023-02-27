package ru.cft.drozdrtskiy.sorting.element;

public interface FileElement extends Element {

    String toWritableFormat();

    @Override
    int compareTo(Element o);
}
