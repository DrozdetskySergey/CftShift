package ru.cft.drozdrtskiy22.merge_files_sorting.element;

public class IntegerFileElement extends AbstractFileElement<Integer> {

    public IntegerFileElement(Integer value) {
        super(value);
    }

    @Override
    public int compareTo(Element o) {
        return value.compareTo(((IntegerFileElement) o).value);
    }
}
