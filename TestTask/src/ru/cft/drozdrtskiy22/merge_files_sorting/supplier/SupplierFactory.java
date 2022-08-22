package ru.cft.drozdrtskiy22.merge_files_sorting.supplier;

import java.io.IOException;
import java.nio.file.Path;

public interface SupplierFactory {
    FileElementSupplier get(Path path) throws IOException;
}
