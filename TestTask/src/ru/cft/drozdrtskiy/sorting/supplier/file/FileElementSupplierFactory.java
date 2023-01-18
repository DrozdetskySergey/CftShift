package ru.cft.drozdrtskiy.sorting.supplier.file;

import ru.cft.drozdrtskiy.sorting.argument.ElementType;
import ru.cft.drozdrtskiy.sorting.supplier.file.impl.IntegerFileElementSupplier;
import ru.cft.drozdrtskiy.sorting.supplier.file.impl.StringFileElementSupplier;

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

    public FileElementSupplier create(Path path) throws IOException {
        return elementType == ElementType.INTEGER ?
                IntegerFileElementSupplier.from(path) :
                StringFileElementSupplier.from(path);
    }
}
