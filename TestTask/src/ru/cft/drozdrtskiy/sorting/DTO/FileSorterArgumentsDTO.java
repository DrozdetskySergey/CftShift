package ru.cft.drozdrtskiy.sorting.DTO;

import ru.cft.drozdrtskiy.sorting.argument.*;

import java.util.List;

public class FileSorterArgumentsDTO {

    public SortDirection sortDirection;
    public ElementType elementType;
    public boolean isUnsortedFileElementsIgnore;
    public String outputFile;
    public List<String> inputFiles;
}
