package ru.cft.drozdrtskiy21.merge_files_sorting;

public interface ElementsStreamFromReaders {
    int getReadersCount();

    Object getElement(int readerIndex);

    boolean isUpdatedElement(int readerIndex);
}
