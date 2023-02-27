package ru.cft.drozdrtskiy.sorting;

import ru.cft.drozdrtskiy.sorting.DTO.FileSorterArgumentsDTO;
import ru.cft.drozdrtskiy.sorting.argument.ArgsException;
import ru.cft.drozdrtskiy.sorting.argument.file.FileSorterArguments;
import ru.cft.drozdrtskiy.sorting.sorter.Sorter;
import ru.cft.drozdrtskiy.sorting.sorter.file.FileSorterByMerge;
import ru.cft.drozdrtskiy.sorting.util.Message;
import ru.cft.drozdrtskiy.sorting.util.MessagePrinter;

public final class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            MessagePrinter.print(Message.FILE_SORT_INFO);
            MessagePrinter.print(Message.FILE_SORT_PARAMS);
            MessagePrinter.print(Message.FILE_SORT_KEYS);

            return;
        }

        FileSorterArguments fileSorterArguments;

        try {
            fileSorterArguments = FileSorterArguments.from(args);
        } catch (ArgsException e) {
            MessagePrinter.print(e.getMessage());
            MessagePrinter.print(Message.FILE_SORT_PARAMS);

            return;
        }

        FileSorterArgumentsDTO fileSorterArgumentsDTO = fileSorterArguments.createDTO();
        Sorter sorter = FileSorterByMerge.from(fileSorterArgumentsDTO);
        sorter.sort();
    }
}
