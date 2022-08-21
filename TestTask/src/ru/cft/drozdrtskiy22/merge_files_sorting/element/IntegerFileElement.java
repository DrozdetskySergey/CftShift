package ru.cft.drozdrtskiy22.merge_files_sorting.element;

public class IntegerFileElement implements FileElement {

    private final Integer value;

    public IntegerFileElement(Integer value) {
        this.value = value;
    }

    @Override
    public String toWritableFormat() {
        return String.format("%s%n", value);
    }

    @Override
    public int compareTo(FileElement o) {
        return value.compareTo(((IntegerFileElement) o).value);
    }
}
