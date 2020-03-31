import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.server.UnicastRemoteObject;
import java.awt.Point;
import java.io.File;
import java.util.*;
import java.util.concurrent.Semaphore;

public class Server implements Graph{
	public Server() {
		graph = new ArrayList<>();
        graph.add(new LinkedList<>());
    }
 public static void main(String args[]) {
        
        try {
            Server obj = new Server();
            Graph stub = (Graph) UnicastRemoteObject.exportObject(obj, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("Graph", stub);
            stub.setGraph("/home/rita/RMI/initial");
            System.err.println("R");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
 private static final int FIRST_EDGE = 0;
 private static final int SECOND_EDGE = 1;
 private static final int QUEUE_HEAD = 0;
 private static final int NO_PATH = -1;
 private static final String LOG_FILE = "/home/rita/RMI/log";
 private static Semaphore mutex = new Semaphore(4);
 private static Semaphore wrt = new Semaphore(1);
 private static int rc = 0;

 private List<LinkedList<Integer>> graph;

 public void setGraph (int[][] edges) throws InterruptedException {
     initializeGraph(edges);
     try {
         FileWriter myWriter = new FileWriter(LOG_FILE,true);
         myWriter.append("The Graph was intialized Successfully\n");
         myWriter.close();
       } catch (IOException e) {
         System.out.println("An error occurred.");
         e.printStackTrace();
       }
 }
 public void setGraph (String filename) throws FileNotFoundException, InterruptedException {
     initializeGraph(filename);
     try {
         FileWriter myWriter = new FileWriter(LOG_FILE,true);
         myWriter.append("The Graph was intialized Successfully\n");
         myWriter.close();
       } catch (IOException e) {
         System.out.println("An error occurred.");
         e.printStackTrace();
       }
 }
 private void initializeGraph (int[][] edges) throws InterruptedException {
     for (int i = 0;i < edges.length; i++) {
         addEdge(edges[i][FIRST_EDGE], edges[i][SECOND_EDGE]);
     }
 }
 private void initializeGraph (String filename) throws FileNotFoundException, InterruptedException {
     Scanner scanner = new Scanner(new File(filename));
     while (scanner.hasNextInt()) {
         //TODO validate graph
         int firstVertex = scanner.nextInt();
         int secondVertex = scanner.nextInt();
         addEdge(firstVertex, secondVertex);
     }
 }

 public boolean addEdge(int firstVertex, int secondVertex) throws InterruptedException {
     wrt.acquire();
     if (edgeExists(firstVertex, secondVertex)) {
    	 int rnd = (int) (Math.random()*10000);	//random number from 0 to 10000
         Thread.sleep(rnd);
         log("add", Thread.currentThread().getName(), firstVertex, secondVertex);
         wrt.release();
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
     int rnd = (int) (Math.random()*10000);	//random number from 0 to 10000
     Thread.sleep(rnd);
     log("add", Thread.currentThread().getName(), firstVertex, secondVertex);
     wrt.release();
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

 public boolean removeEdge (int firstVertex, int secondVertex) throws InterruptedException {
     wrt.acquire();
     if (!edgeExists(firstVertex, secondVertex)) {
    	 int rnd = (int) (Math.random()*10000);	//random number from 0 to 10000
         Thread.sleep(rnd);
         log("remove",Thread.currentThread().getName(), firstVertex, secondVertex);
         wrt.release();
         return false;
     }
     graph.get(firstVertex).removeFirstOccurrence(secondVertex);
     int rnd = (int) (Math.random()*10000);	//random number from 0 to 10000
     Thread.sleep(rnd);
     log("remove",Thread.currentThread().getName(), firstVertex, secondVertex);
     wrt.release();
     return true;
 }

 public int getShortestPath (int firstNode, int targetNode) throws InterruptedException {
     mutex.acquire();
     rc++;
     if (rc == 1) {
         wrt.acquire();
     }
     mutex.release();
     if (graph.size() <= Math.max(firstNode, targetNode)) {
         mutex.acquire();
         rc--;
         if (rc == 0) {
             wrt.release();
         }
         int rnd = (int) (Math.random()*10000);	//random number from 0 to 10000
         Thread.sleep(rnd);
         log("query",Thread.currentThread().getName(), firstNode, targetNode);
         mutex.release();
         return -1;
     }
     List<Point> queue = new ArrayList<>();
     HashSet<Integer> visited = new HashSet<>();

     queue.add(new Point(firstNode, 0));
     visited.add(firstNode);

     while (!queue.isEmpty()) {
         Point currentPair = queue.remove(QUEUE_HEAD);
         Integer currentNode = (int) currentPair.getX();
         Integer currentLevel = (int) currentPair.getY();

         if (currentNode == targetNode) {
             mutex.acquire();
             rc--;
             if (rc == 0) {
                 wrt.release();
             }
             int rnd = (int) (Math.random()*10000);	//random number from 0 to 10000
             Thread.sleep(rnd);
             log("query",Thread.currentThread().getName(), firstNode, targetNode);
             mutex.release();
             return currentLevel;
         }
         LinkedList<Integer> neighbours = graph.get(currentNode);
         if (neighbours == null) {
             continue;
         }
         for (Integer entry : neighbours) {
             if (!visited.contains(entry)) {
                 queue.add(new Point(entry, currentLevel + 1));
                 visited.add(entry);
             }
         }
     }
     mutex.acquire();
     rc--;
     if (rc == 0) {
         wrt.release();
     }
     int rnd = (int) (Math.random()*10000);	//random number from 0 to 10000
     Thread.sleep(rnd);
     log("query",Thread.currentThread().getName(), firstNode, targetNode);
     mutex.release();
     return NO_PATH;
 }

    private void log(String operation,String arg0,int arg1,int arg2) {
    	try {
            FileWriter myWriter = new FileWriter(LOG_FILE,true);
            if(operation.equals("query")) {
	            myWriter.append("Client "+arg0+" queried the shortest path from "+arg1+" to "+arg2+" Successfully\n");
	    	}else if (operation.equals("remove")) {
	    		myWriter.append("Client "+arg0+" removed edge from "+arg1+" to "+arg2+" Successfully\n");
	    	}else if(operation.equals("add")) {
	    		myWriter.append("Client "+arg0+" added edge from "+arg1+" to "+arg2+" Successfully\n");
	    	}
            myWriter.close();
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
    	
    	
    }

}
