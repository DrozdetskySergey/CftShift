package ru.cft.drozdrtskiy21.merge_files_sorting;

public interface ElementsGetterFromReaders {
    int getReadersCount();

    Object getElement(int readerIndex);

    boolean tryUpdateElement(int readerIndex);
}
