package ru.cft.drozdrtskiy.sorting.supplier.file_impl.factory;

import ru.cft.drozdrtskiy.sorting.argument.ElementType;
import ru.cft.drozdrtskiy.sorting.supplier.FileElementSupplier;
import ru.cft.drozdrtskiy.sorting.supplier.file_impl.IntegerFileElementSupplier;
import ru.cft.drozdrtskiy.sorting.supplier.file_impl.StringFileElementSupplier;

import java.io.IOException;
import java.nio.file.Path;

public final class FileElementSupplierFactory {

    private final ElementType elementType;

    public static FileElementSupplierFactory from(ElementType elementType) {
        return new FileElementSupplierFactory(elementType);
    }

    private FileElementSupplierFactory(ElementType elementType) {
        this.elementType = elementType;
    }

    public FileElementSupplier createFor(Path path) throws IOException {
        return elementType == ElementType.INTEGER ?
                new IntegerFileElementSupplier(path) :
                new StringFileElementSupplier(path);
    }
}
