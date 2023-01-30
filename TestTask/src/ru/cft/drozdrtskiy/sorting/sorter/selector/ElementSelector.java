package ru.cft.drozdrtskiy.sorting.sorter.selector;

import ru.cft.drozdrtskiy.sorting.element.Element;
import ru.cft.drozdrtskiy.sorting.supplier.ElementSupplier;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class ElementSelector {

    private int nodeCount;
    private List<Node> nodes;
    private final Comparator<Element> comparator;

    public static ElementSelector from(List<ElementSupplier> suppliers, Comparator<Element> comparator) {
        return new ElementSelector(suppliers, comparator);
    }

    private ElementSelector(List<ElementSupplier> suppliers, Comparator<Element> comparator) {
        createNodes(suppliers);
        nodeCount = nodes.size();
        this.comparator = comparator;
    }

    private void createNodes(List<ElementSupplier> suppliers) {
        nodes = new ArrayList<>(suppliers.size());

        for (ElementSupplier supplier : suppliers) {
            Node newNode = Node.from(supplier);

            if (newNode.getElement() != null) {
                nodes.add(newNode);
            }
        }
    }

    public boolean hasNext() {
        return nodeCount > 0;
    }

    public Element next() throws IllegalAccessException {
        if (nodeCount == 0) {
            throw new IllegalAccessException("Отсутствует постовщик элементов.");
        }

        Element element = nodes.get(0).getElement();
        int nodeIndex = 0;

        for (int i = 1; i < nodeCount; i++) {
            Element nextElement = nodes.get(i).getElement();

            if (comparator.compare(element, nextElement) > 0) {
                element = nextElement;
                nodeIndex = i;
            }
        }

        updateOrRemoveNode(nodeIndex);

        return element;
    }

    private void updateOrRemoveNode(int index) {
        Node node = nodes.get(index);
        node.update();

        if (node.getElement() == null) {
            nodes.remove(index);
            nodeCount--;
        }
    }
}
