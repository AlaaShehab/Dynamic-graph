import java.io.FileNotFoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Graph extends Remote {

    public void setGraph (int[][] edges) throws InterruptedException, RemoteException;
    public void setGraph (String filename) throws FileNotFoundException, InterruptedException, RemoteException;
    public boolean addEdge(int firstVertex, int secondVertex) throws InterruptedException, RemoteException;
    public boolean removeEdge (int firstVertex, int secondVertex) throws InterruptedException, RemoteException;
    public int getShortestPath (int firstNode, int targetNode) throws InterruptedException, RemoteException;
}
