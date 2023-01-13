package ru.cft.drozdrtskiy22.merge_files_sorting.element;

public interface FileElement extends Comparable<FileElement> {

    String toWritableFormat();
}
