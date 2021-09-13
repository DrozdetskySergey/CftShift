package ru.cft.drozdrtskiy21.merge_files_sorting;

import com.sun.jdi.InvalidTypeException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ElementReader {
    private final List<BufferedReader> readers = new ArrayList<>();
    private final List<Object> elements = new ArrayList<>();
    private int size;
    private final ElementType type;

    public ElementReader(List<BufferedReader> readers, ElementType type) throws InvalidTypeException {
        for (BufferedReader r : readers) {
            String nextLine = null;

            try {
                nextLine = r.readLine();
            } catch (IOException ignored) {
            }

            if (nextLine != null) {
                try {
                    switch (type) {
                        case INTEGER -> elements.add(Integer.valueOf(nextLine));
                        case STRING -> elements.add(nextLine);
                        default -> throw new InvalidTypeException();
                    }

                    this.readers.add(r);
                    size++;
                } catch (NumberFormatException ignored) {
                }
            }
        }

        this.type = type;
    }

    public int getSize() {
        return size;
    }

    public Object getElement(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index = " + index + ", valid value: [0, " + (size - 1) + "].");
        }

        return elements.get(index);
    }

    public boolean isUpdatedElement(int index) throws InvalidTypeException {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index = " + index + ", valid value: [0, " + (size - 1) + "].");
        }

        String nextLine;

        try {
            nextLine = readers.get(index).readLine();
        } catch (IOException e) {
            nextLine = null;
        }

        if (nextLine != null) {
            try {
                switch (type) {
                    case INTEGER -> elements.set(index, Integer.valueOf(nextLine));
                    case STRING -> elements.set(index, nextLine);
                    default -> throw new InvalidTypeException();
                }

                return true;
            } catch (NumberFormatException ignored) {
            }
        }

        elements.remove(index);
        readers.remove(index);
        size--;

        return false;
    }
}
