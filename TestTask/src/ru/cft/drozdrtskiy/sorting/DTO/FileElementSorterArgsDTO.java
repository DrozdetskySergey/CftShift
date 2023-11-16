package ru.cft.drozdrtskiy.sorting.DTO;

import ru.cft.drozdrtskiy.sorting.ElementType;
import ru.cft.drozdrtskiy.sorting.SortDirection;

import java.nio.file.Path;
import java.util.List;

public class FileElementSorterArgsDTO {

    public SortDirection sortDirection;
    public ElementType elementType;
    public boolean isUnsortedFileElementsIgnore;
    public Path outputFile;
    public List<Path> inputFiles;
}
