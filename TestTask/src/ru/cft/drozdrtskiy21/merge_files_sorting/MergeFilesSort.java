package ru.cft.drozdrtskiy21.merge_files_sorting;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class MergeFilesSort {
    public static void main(String[] args) {
        List<String> arguments = getArgumentsList(args);

        if (arguments.size() < 4) {
            System.out.printf("mergeFilesSorting [-a | -d] [-s | -i] [output_file_name] [input_file_1_name] ([input_file_2_name]..)%n-a  ascending order%n-d  descending order%n-s  String type%n-i  Integer type%n");

            return;
        }

        boolean isAscending = Boolean.parseBoolean(arguments.remove(0));
        ElementType type = ElementType.valueOf(arguments.remove(0));
        String outputFileName = arguments.remove(0);

        List<BufferedReader> readers = new ArrayList<>();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
            for (String s : arguments) {
                readers.add(new BufferedReader(new FileReader(s)));
            }

            ElementsGetterFromReaders elements = new ElementsFromReaders(new ArrayList<>(readers), type);
            Comparator<Object> comparator = getComparator(isAscending, type);

            MergeSort.mergeSort(writer, elements, comparator);
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        } catch (IOException e) {
            System.out.println("Problem with input or output file(s)!");
        } finally {
            for (BufferedReader r : readers) {
                if (r != null) {
                    try {
                        r.close();
                    } catch (IOException ignored) {
                    }
                }
            }
        }
    }

    private static List<String> getArgumentsList(String[] args) {
        if (args.length > Arrays.stream(args).collect(Collectors.toSet()).size()) {
            System.out.println("Invalid parameters: duplicates.");

            return Collections.emptyList();
        }

        List<String> result = new ArrayList<>();
        result.add("true");
        result.add("INTEGER");
        result.add("out.txt");

        boolean orderIsSet = false;
        boolean typeIsSet = false;
        boolean outputFileNameIsSet = false;

        for (String s : args) {
            if ("-a".equals(s)) {
                if (orderIsSet) {
                    System.out.println("Invalid parameters: [-a] or [-d]");

                    return Collections.emptyList();
                }

                result.set(0, "true");
                orderIsSet = true;
            } else if ("-d".equals(s)) {
                if (orderIsSet) {
                    System.out.println("Invalid parameters: [-a] or [-d]");

                    return Collections.emptyList();
                }

                result.set(0, "false");
                orderIsSet = true;
            } else if ("-s".equals(s)) {
                if (typeIsSet) {
                    System.out.println("Invalid parameters: [-s] or [-i]");

                    return Collections.emptyList();
                }

                result.set(1, ElementType.STRING.toString());
                typeIsSet = true;
            } else if ("-i".equals(s)) {
                if (typeIsSet) {
                    System.out.println("Invalid parameters: [-s] or [-i]");

                    return Collections.emptyList();
                }

                result.set(1, ElementType.INTEGER.toString());
                typeIsSet = true;
            } else if (!typeIsSet) {
                return Collections.emptyList();
            } else if (!outputFileNameIsSet) {
                result.set(2, s);
                outputFileNameIsSet = true;
            } else {
                result.add(s);
            }
        }

        if (typeIsSet && outputFileNameIsSet && result.size() >= 4) {
            return result;
        }

        return Collections.emptyList();
    }

    private static Comparator<Object> getComparator(boolean isAscending, ElementType type) {
        if (isAscending) {
            switch (type) {
                case INTEGER -> {
                    return Comparator.comparing(o -> ((Integer) o));
                }
                case STRING -> {
                    return Comparator.comparing(o -> ((String) o));
                }
                default -> throw new UnknownElementTypeException("Unknown element type!");
            }
        } else {
            switch (type) {
                case INTEGER -> {
                    return (o1, o2) -> ((Integer) o2).compareTo((Integer) o1);
                }
                case STRING -> {
                    return (o1, o2) -> ((String) o2).compareTo((String) o1);
                }
                default -> throw new UnknownElementTypeException("Unknown element type!");
            }
        }
    }
}
