package ru.cft.drozdrtskiy21.merge_files_sorting;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ElementsFromReaders implements ElementsStreamFromReaders {
    private final List<BufferedReader> readers = new ArrayList<>();
    private final List<Object> elements = new ArrayList<>();
    private int readersCount;
    private final ElementType type;

    public ElementsFromReaders(List<BufferedReader> readers, ElementType type) {
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
                        }

                        this.readers.add(r); // first line is good (BufferedReader r)
                        readersCount++;
                        break; // break do {...} while () -> go next BufferedReader
                    } catch (NumberFormatException | StringFormatException ignored) {
                    }
                }

            } while (nextLine != null); // bad format of first line (BufferedReader r) -> try read next
        }

        this.type = type;
    }

    @Override
    public int getReadersCount() {
        return readersCount;
    }

    @Override
    public Object getElement(int readerIndex) {
        if (readerIndex < 0 || readerIndex >= readersCount) {
            throw new IndexOutOfBoundsException("Index = " + readerIndex + ", valid value: [0, " + (readersCount - 1) + "].");
        }

        return elements.get(readerIndex);
    }

    @Override
    public boolean isUpdatedElement(int readerIndex) {
        if (readerIndex < 0 || readerIndex >= readersCount) {
            throw new IndexOutOfBoundsException("Index = " + readerIndex + ", valid value: [0, " + (readersCount - 1) + "].");
        }

        String nextLine;

        do {
            try {
                nextLine = readers.get(readerIndex).readLine();
            } catch (IOException e) {
                nextLine = null;
            }

            if (nextLine != null) {
                try {
                    switch (type) {
                        case INTEGER -> elements.set(readerIndex, Integer.valueOf(nextLine));
                        case STRING -> {
                            if (Pattern.compile("\\s").matcher(nextLine).find()) {
                                throw new StringFormatException();
                            }

                            elements.set(readerIndex, nextLine);
                        }
                    }

                    return true; // next line is good -> exit
                } catch (NumberFormatException | StringFormatException ignored) {
                }
            }

        } while (nextLine != null); // bad format of next line (readers[index]) -> try read next

        elements.remove(readerIndex);
        readers.remove(readerIndex);
        readersCount--;

        return false;
    }
}
