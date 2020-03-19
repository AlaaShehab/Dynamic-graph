import Graph.Graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main (String[] args) throws FileNotFoundException {
        Graph graph = new Graph();
        graph.setGraph("graph.txt");
        graph.addEdge(4,5);
        System.out.println(graph.getShortestPath(5,1));
    }
}
