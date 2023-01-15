package ru.cft.drozdrtskiy22.merge_files_sorting.element.file;

import ru.cft.drozdrtskiy22.merge_files_sorting.element.Element;

public class IntegerFileElement implements Element {

    private final Integer value;

    public IntegerFileElement(Integer value) {
        this.value = value;
    }

    @Override
    public String toWritableFormat() {
        return String.format("%s%n", value);
    }

    @Override
    public int compareTo(Element o) {
        return value.compareTo(((IntegerFileElement) o).value);
    }
}
