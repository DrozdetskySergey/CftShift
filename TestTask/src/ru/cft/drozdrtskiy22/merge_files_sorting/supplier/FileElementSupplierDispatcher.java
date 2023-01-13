package ru.cft.drozdrtskiy22.merge_files_sorting.supplier;

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

    public FileElementSupplier createWithFile(Path path) throws IOException {
        return elementType == ElementType.INTEGER ?
                IntegerFileElementSupplier.forFile(path) :
                StringFileElementSupplier.forFile(path);
    }
}
