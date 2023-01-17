package ru.cft.drozdrtskiy22.merge_files_sorting.element;

public class StringFileElement extends AbstractFileElement<String> {

    public StringFileElement(String value) {
        super(value);
    }

    @Override
    public int compareTo(Element o) {
        return value.compareTo(((StringFileElement) o).value);
    }
}
