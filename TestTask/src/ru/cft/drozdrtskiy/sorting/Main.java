package ru.cft.drozdrtskiy.sorting;

import ru.cft.drozdrtskiy.sorting.argument.ArgsException;
import ru.cft.drozdrtskiy.sorting.argument.file.FileSorterArguments;
import ru.cft.drozdrtskiy.sorting.sorter.Sorter;
import ru.cft.drozdrtskiy.sorting.sorter.file.merge.FileSorterByMerge;
import ru.cft.drozdrtskiy.sorting.util.Message;
import ru.cft.drozdrtskiy.sorting.util.Writer;

public final class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            Message.FILE_SORT_INFO.show();
            Message.FILE_SORT_PARAMS.show();
            Message.FILE_SORT_KEYS.show();

            return;
        }

        FileSorterArguments fileSorterArguments;

        try {
            fileSorterArguments = FileSorterArguments.from(args);
        } catch (ArgsException e) {
            Writer.write(e.getMessage());
            Message.FILE_SORT_PARAMS.show();

            return;
        }

        Sorter sorter = FileSorterByMerge.from(fileSorterArguments);
        sorter.sort();
    }
}
