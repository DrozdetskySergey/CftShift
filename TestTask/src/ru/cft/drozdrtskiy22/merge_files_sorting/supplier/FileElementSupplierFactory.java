package ru.cft.drozdrtskiy22.merge_files_sorting.supplier;

import ru.cft.drozdrtskiy22.merge_files_sorting.utility.args.ElementType;

import java.io.IOException;
import java.nio.file.Path;

public class FileElementSupplierFactory {

    private final ElementType elementType;

    public static FileElementSupplierFactory forElementType(ElementType elementType) {
        return new FileElementSupplierFactory(elementType);
    }

    private FileElementSupplierFactory(ElementType elementType) {
        this.elementType = elementType;
    }

    public FileElementSupplier get(Path path) throws IOException {
        return elementType == ElementType.INTEGER ?
                IntegerFileElementSupplier.forFile(path) :
                StringFileElementSupplier.forFile(path);
    }
}
