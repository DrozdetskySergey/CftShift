package ru.cft.drozdrtskiy22.merge_files_sorting;

import ru.cft.drozdrtskiy22.merge_files_sorting.supplier.FileElementSupplier;
import ru.cft.drozdrtskiy22.merge_files_sorting.supplier.IntegerFileElementSupplier;
import ru.cft.drozdrtskiy22.merge_files_sorting.supplier.StringFileElementSupplier;
import ru.cft.drozdrtskiy22.merge_files_sorting.argument.ElementType;

import java.io.IOException;
import java.nio.file.Path;

public final class FileElementSupplierFactory {

    private final ElementType elementType;

    public static FileElementSupplierFactory withElementType(ElementType elementType) {
        return new FileElementSupplierFactory(elementType);
    }

    private FileElementSupplierFactory(ElementType elementType) {
        this.elementType = elementType;
    }

    public FileElementSupplier createForFile(Path path) throws IOException {
        return elementType == ElementType.INTEGER ?
                IntegerFileElementSupplier.fromFile(path) :
                StringFileElementSupplier.fromFile(path);
    }
}
