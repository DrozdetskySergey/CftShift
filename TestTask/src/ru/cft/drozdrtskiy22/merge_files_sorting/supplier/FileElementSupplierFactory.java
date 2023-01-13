package ru.cft.drozdrtskiy22.merge_files_sorting.supplier;

import ru.cft.drozdrtskiy22.merge_files_sorting.utility.args.ElementType;
import ru.cft.drozdrtskiy22.merge_files_sorting.element.FileElement;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Comparator;

public class FileElementSupplierFactory implements SupplierFactory {

    private final ElementType elementType;
    private final Comparator<FileElement> comparator;

    public FileElementSupplierFactory(ElementType elementType, Comparator<FileElement> comparator) {
        this.elementType = elementType;
        this.comparator = comparator;
    }

    @Override
    public FileElementSupplier get(Path path) throws IOException {
        return elementType == ElementType.INTEGER ?
                new IntegerFileElementSupplier(path, comparator) :
                new StringFileElementSupplier(path, comparator);
    }
}
