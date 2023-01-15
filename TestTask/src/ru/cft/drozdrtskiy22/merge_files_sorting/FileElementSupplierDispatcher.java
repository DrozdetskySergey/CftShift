package ru.cft.drozdrtskiy22.merge_files_sorting;

import ru.cft.drozdrtskiy22.merge_files_sorting.supplier.ElementSupplier;
import ru.cft.drozdrtskiy22.merge_files_sorting.supplier.file.IntegerFileElementSupplier;
import ru.cft.drozdrtskiy22.merge_files_sorting.supplier.file.StringFileElementSupplier;
import ru.cft.drozdrtskiy22.merge_files_sorting.utility.args.ElementType;

import java.io.IOException;
import java.nio.file.Path;

public final class FileElementSupplierDispatcher {

    private final ElementType elementType;

    public static FileElementSupplierDispatcher forElementType(ElementType elementType) {
        return new FileElementSupplierDispatcher(elementType);
    }

    private FileElementSupplierDispatcher(ElementType elementType) {
        this.elementType = elementType;
    }

    public ElementSupplier createWithFile(Path path) throws IOException {
        return elementType == ElementType.INTEGER ?
                IntegerFileElementSupplier.fromFile(path) :
                StringFileElementSupplier.fromFile(path);
    }
}
