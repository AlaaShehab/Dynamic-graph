package Graph;

import java.io.FileNotFoundException;

public interface Graph {

    public void setGraph (int[][] edges);
    public void setGraph (String filename) throws FileNotFoundException;
    public boolean addEdge(int firstVertex, int secondVertex);
    public boolean removeEdge (int firstVertex, int secondVertex);
    public int getShortestPath (int firstNode, int targetNode);
}
