package ru.cft.drozdrtskiy.sorting.supplier.file_impl.factory;

import ru.cft.drozdrtskiy.sorting.argument.ElementType;
import ru.cft.drozdrtskiy.sorting.supplier.FileElementSupplier;
import ru.cft.drozdrtskiy.sorting.supplier.file_impl.*;

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

    public FileElementSupplier createForFile(Path path) throws IOException {
        FileElementSupplier result = null;

        if (elementType == ElementType.INTEGER) {
            result = new IntegerFileElementSupplier(path);
        } else if (elementType == ElementType.STRING) {
            result = new StringFileElementSupplier(path);
        }

        return result;
    }
}
