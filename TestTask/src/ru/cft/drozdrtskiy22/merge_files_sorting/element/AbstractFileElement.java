package ru.cft.drozdrtskiy22.merge_files_sorting.element;

abstract class AbstractFileElement<T> implements FileElement {

    protected final T value;

    protected AbstractFileElement(T value) {
        this.value = value;
    }

    @Override
    public String toWritableFormat() {
        return String.format("%s%n", value);
    }
}
