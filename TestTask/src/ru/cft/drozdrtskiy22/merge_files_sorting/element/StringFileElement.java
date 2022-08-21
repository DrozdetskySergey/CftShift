package ru.cft.drozdrtskiy22.merge_files_sorting.element;

public class StringFileElement implements FileElement {

    private final String value;

    public StringFileElement(String value) {
        this.value = value;
    }

    @Override
    public String toWritableFormat() {
        return String.format("%s%n", value);
    }

    @Override
    public int compareTo(FileElement o) {
        return value.compareTo(((StringFileElement) o).value);
    }
}
