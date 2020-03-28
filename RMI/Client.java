import java.io.File;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {
	  private Client() {}

	    public static void main(String[] args) {
	    	int clientNo =0;
	    	new Thread(() -> {
	    		String host = (args.length < 1) ? null : args[0];
		        try {
		            Registry registry = LocateRegistry.getRegistry(host);
		            Graph stub = (Graph) registry.lookup("Graph");;
		            String batchFileName =	args[1];
		            
		            Scanner scanner = new Scanner(new File(batchFileName));
		            String op = scanner.next();
	                int firstVertex = scanner.nextInt();
	                int secondVertex = scanner.nextInt();
	                if(op.equals("Q")) {
	                	System.out.println(stub.getShortestPath(firstVertex, secondVertex));
	                }else if(op.equals("A")){
	                	stub.addEdge(firstVertex, secondVertex);
	                }else if(op.equals("D")){
	                	stub.removeEdge(firstVertex, secondVertex);
	                }	           
		        } catch (Exception e) {
		            System.err.println("Client exception: " + e.toString());
		            e.printStackTrace();
		        }
	    	},Integer.toString(clientNo++)).start();

	        
	    }
}
