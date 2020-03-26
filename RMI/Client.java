import java.io.File;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {
	  private Client() {}

	    public static void main(String[] args) {

	        String host = (args.length < 1) ? null : args[0];
	        try {
	            Registry registry = LocateRegistry.getRegistry(host);
	            Graph stub = (Graph) registry.lookup("Graph8");
	            String intialFileName="/home/rita/RMI/intial";
	            String batchFileName = "/home/rita/RMI/batch";
	            System.out.println("ratroot");
	            stub.setGraph(intialFileName);
	            System.out.println("graphset");
	            Scanner scanner = new Scanner(new File(batchFileName));
	            System.out.println("response_1: "+stub.getShortestPath(1, 3));
	           /* int output = -1;
	            while (scanner.hasNextInt()) {
	                //TODO validate graph
	            	String op = scanner.next();
	                int firstVertex = scanner.nextInt();
	                int secondVertex = scanner.nextInt();
	                if(op.equals("Q")) {
	                	output = this.getShortestPath(firstVertex, secondVertex);
	                }else if(op.equals("A")){
	                	this.addEdge(firstVertex, secondVertex);
	                }else if(op.equals("D")){
	                	this.removeEdge(firstVertex, secondVertex);
	                }
	                System.out.println("response: " + output);
	            }
	            */
	           
	        } catch (Exception e) {
	            System.err.println("Client exception: " + e.toString());
	            e.printStackTrace();
	        }
	    }
}

