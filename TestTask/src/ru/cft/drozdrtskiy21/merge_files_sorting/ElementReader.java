package ru.cft.drozdrtskiy21.merge_files_sorting;

import com.sun.jdi.InvalidTypeException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ElementReader {
    private final List<BufferedReader> readers = new ArrayList<>();
    private final List<Object> elements = new ArrayList<>();
    private int size;
    private final ElementType type;

    public ElementReader(List<BufferedReader> readers, ElementType type) throws InvalidTypeException {
        for (BufferedReader r : readers) {
            String nextLine;

            do {
                try {
                    nextLine = r.readLine();
                } catch (IOException e) {
                    nextLine = null;
                }

                if (nextLine != null) {
                    try {
                        switch (type) {
                            case INTEGER -> elements.add(Integer.valueOf(nextLine));
                            case STRING -> {
                                if (Pattern.compile("\\s").matcher(nextLine).find()) {
                                    throw new StringFormatException();
                                }

                                elements.add(nextLine);
                            }
                            default -> throw new InvalidTypeException();
                        }

                        this.readers.add(r); // first line is good (BufferedReader r)
                        size++;
                        break; // break do {...} while () -> go next BufferedReader
                    } catch (NumberFormatException | StringFormatException ignored) {
                    }
                }

            } while (nextLine != null); // bad format of first line (BufferedReader r) -> try read next
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

        do {
            try {
                nextLine = readers.get(index).readLine();
            } catch (IOException e) {
                nextLine = null;
            }

            if (nextLine != null) {
                try {
                    switch (type) {
                        case INTEGER -> elements.set(index, Integer.valueOf(nextLine));
                        case STRING -> {
                            if (Pattern.compile("\\s").matcher(nextLine).find()) {
                                throw new StringFormatException();
                            }

                            elements.set(index, nextLine);
                        }
                        default -> throw new InvalidTypeException();
                    }

                    return true; // next line is good -> exit
                } catch (NumberFormatException | StringFormatException ignored) {
                }
            }

        } while (nextLine != null); // bad format of next line (readers[index]) -> try read next

        elements.remove(index);
        readers.remove(index);
        size--;

        return false;
    }
}
