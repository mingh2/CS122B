import java.util.Scanner;

public class JDBC {
	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception
	{
        MovieDBConsole console = new MovieDBConsole();
        Scanner s = new Scanner(System.in);
        int outterFlag = 0;
        
        do {
	        System.out.print("Connect to the database? (y/n): ");
	        String command = s.nextLine();

	        if(command.toLowerCase().equals("y")) {
	        	int flag = 0;
	        	console.connect();
	        	do {
	        		System.out.println("\n***** MENU *****\n");
	        		System.out.println("[1] Search Movies Info by Looking at Stars;");
	        		System.out.println("[2] Insert a Star instance;");
	        		System.out.println("[3] Insert a Customer instance;");
	        		System.out.println("[4] Delete a customer instance;");
	        		System.out.println("[5] Show Metadata of MovieDB;");
	        		System.out.println("[6] Create Your Own Command;");
	        		System.out.println("[7] Exit MovieDB.\n");
	        		System.out.print("Your Action: ");
	        		String action = s.nextLine();
	        		
	        		if(action.equals("1")) {
	        			console.printMoviesWithStars();
	        		} else if(action.equals("2")) {
	        			console.insertNewStar();
	        		} else if(action.equals("3")) {
	        			console.insertNewCustomer();
	        		} else if(action.equals("4")) {
	        			console.deleteACustomer();
	        		} else if(action.equals("5")) {
	        			console.printMetadata();
	        		} else if(action.equals("6")) {
	        			console.respondToValidCommand();
	        		} else if(action.equals("7")) {
	        			System.out.println("\n>>>>>>>>>> Exiting from MovieDB... <<<<<<<<<<");
	        			++flag;
	        		} else {
	        			System.out.println("\nInvalid Action ID. Please Try Again.");
	        		}
	        	} while(flag == 0);
	        	
	        	System.out.println("\nByebye MovieDB!\n");
	        	console.resetConnection();
	        	
	        } else if(command.toLowerCase().equals("n")){
	        	System.out.println("\n>>>>>>>>>> Exiting the program... <<<<<<<<<<");
	        	System.out.println("\nHave a good day! <3");
	        	++outterFlag;
	        	s.close();
	        	return;
	        } else {
	        	System.out.println("\nInvalid Input. Please Try Again.\n");
	        }
        } while (outterFlag == 0);
	}
}
