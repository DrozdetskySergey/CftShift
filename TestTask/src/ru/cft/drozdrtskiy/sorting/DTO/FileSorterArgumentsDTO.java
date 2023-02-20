package ru.cft.drozdrtskiy.sorting.DTO;

import ru.cft.drozdrtskiy.sorting.argument.ElementType;
import ru.cft.drozdrtskiy.sorting.argument.SortDirection;

import java.nio.file.Path;
import java.util.List;

public class FileSorterArgumentsDTO {

    public SortDirection sortDirection;
    public ElementType elementType;
    public boolean isUnsortedFileElementsIgnore;
    public Path outputFile;
    public List<Path> inputFiles;
}
