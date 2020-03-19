package Graph;

import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class GraphImproved implements Graph{
    private static final int FIRST_EDGE = 0;
    private static final int SECOND_EDGE = 1;
    private static final int QUEUE_HEAD = 0;
    private static final int NO_PATH = -1;

    private HashMap<Integer, HashSet<Integer>> graph;
    public GraphImproved() {
        graph = new HashMap<>();
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
        if (graph.containsKey(firstVertex)) {
            HashSet<Integer> set = graph.get(firstVertex);
            if (set.contains(secondVertex)) {
                return false;
            }
            set.add(secondVertex);
        } else {
            HashSet<Integer> set = new HashSet<>();
            set.add(secondVertex);
            graph.put(firstVertex, set);
        }
        return true;
    }

    public boolean removeEdge (int firstVertex, int secondVertex) {
        if (graph.containsKey(firstVertex)) {
            HashSet<Integer> set = graph.get(firstVertex);
            if (!set.contains(secondVertex)) {
                return false;
            }
            set.remove(secondVertex);
            if (!set.isEmpty()) {
                graph.put(firstVertex, set);
            } else {
                graph.remove(firstVertex);
            }
        }
        return true;
    }

    public int getShortestPath (int firstNode, int targetNode) {
        List<Pair<Integer, Integer>> queue = new ArrayList<>();
        queue.add(new Pair<>(firstNode, 0));

        while (!queue.isEmpty()) {
            Pair<Integer, Integer> currentPair = queue.remove(QUEUE_HEAD);
            Integer currentNode = currentPair.getKey();
            Integer currentLevel = currentPair.getValue();

            if (currentNode == targetNode) {
                return currentLevel;
            }
            HashSet<Integer> neighbours = graph.containsKey(currentNode)
                    ? graph.get(currentNode)
                    : new HashSet<>();
            for (Integer entry : neighbours) {
                queue.add(new Pair<>(entry, currentLevel+1));
            }
        }
        return NO_PATH;
    }
}