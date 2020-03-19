package Graph;

import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class GraphUnImproved implements Graph{
    private static final int FIRST_EDGE = 0;
    private static final int SECOND_EDGE = 1;
    private static final int QUEUE_HEAD = 0;
    private static final int NO_PATH = -1;

    private List<LinkedList<Integer>> graph;
    public GraphUnImproved() {
        graph = new ArrayList<>();
        graph.add(new LinkedList<>());
    }
    public void setGraph (int[][] edges) {
        initializeGraph(edges);
    }
    public void setGraph (String filename) throws FileNotFoundException {
        initializeGraph(filename);
    }
    private void initializeGraph (int[][] edges) {
        for (int i = 0;i < edges.length; i++) {
            addEdge(edges[i][FIRST_EDGE], edges[i][SECOND_EDGE]);
        }
    }
    private void initializeGraph (String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filename));
        while (scanner.hasNextInt()) {
            //TODO validate graph
            int firstVertex = scanner.nextInt();
            int secondVertex = scanner.nextInt();
            addEdge(firstVertex, secondVertex);
        }
    }

    public boolean addEdge(int firstVertex, int secondVertex) {
        if (edgeExists(firstVertex, secondVertex)) {
            return false;
        }
        if (graph.size() > firstVertex) {
            graph.get(firstVertex).add(secondVertex);
        } else {
            LinkedList<Integer> edges = new LinkedList<>();
            edges.add(secondVertex);
            graph.add(firstVertex, edges);
        }
        addEdgesList(secondVertex);
        return true;
    }

    private void addEdgesList (int vertex) {
        if (graph.size() <= vertex) {
            graph.add(vertex, new LinkedList<>());
        }
    }

    private boolean edgeExists(int firstVertex, int secondVertex) {
        if (graph.size() <= firstVertex) {
            return false;
        }
        LinkedList<Integer> edges = graph.get(firstVertex);
        for (Integer vertex : edges) {
            if (vertex == secondVertex) {
                return true;
            }
        }
        return false;
    }

    public boolean removeEdge (int firstVertex, int secondVertex) {
        if (!edgeExists(firstVertex, secondVertex)) {
            return false;
        }
        graph.get(firstVertex).remove(secondVertex);
        return true;
    }

    public int getShortestPath (int firstNode, int targetNode) {
        if (graph.size() <= Math.max(firstNode, targetNode)) {
            return -1;
        }
        List<Pair<Integer, Integer>> queue = new ArrayList<>();
        queue.add(new Pair<>(firstNode, 0));

        while (!queue.isEmpty()) {
            Pair<Integer, Integer> currentPair = queue.remove(QUEUE_HEAD);
            Integer currentNode = currentPair.getKey();
            Integer currentLevel = currentPair.getValue();

            if (currentNode == targetNode) {
                return currentLevel;
            }
            LinkedList<Integer> neighbours = graph.get(currentNode);
            if (neighbours == null) {
                continue;
            }
            for (Integer entry : neighbours) {
                queue.add(new Pair<>(entry, currentLevel+1));
            }
        }
        return NO_PATH;
    }
}
