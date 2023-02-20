package ru.cft.drozdrtskiy.sorting;

import ru.cft.drozdrtskiy.sorting.DTO.FileSorterArgumentsDTO;
import ru.cft.drozdrtskiy.sorting.argument.ArgumentsException;
import ru.cft.drozdrtskiy.sorting.argument.file.FileSorterArguments;
import ru.cft.drozdrtskiy.sorting.sorter.Sorter;
import ru.cft.drozdrtskiy.sorting.sorter.file.by_merge.FileSorterByMerge;
import ru.cft.drozdrtskiy.sorting.util.Writer;

import static ru.cft.drozdrtskiy.sorting.util.Message.*;

public final class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            Writer.write(FILE_SORT_INFO);
            Writer.write(FILE_SORT_PARAMS);
            Writer.write(FILE_SORT_KEYS);

            return;
        }

        FileSorterArguments fileSorterArguments;

        try {
            fileSorterArguments = FileSorterArguments.from(args);
        } catch (ArgumentsException e) {
            Writer.write(e.getMessage());
            Writer.write(FILE_SORT_PARAMS);

            return;
        }

        FileSorterArgumentsDTO fileSorterArgumentsDTO = fileSorterArguments.getDTO();
        Sorter sorter = FileSorterByMerge.from(fileSorterArgumentsDTO);
        sorter.sort();
    }
}
