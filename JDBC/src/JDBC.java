import java.util.Scanner;

public class JDBC {
	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception
	{
        MovieDBConsole console = new MovieDBConsole();
        Scanner s = new Scanner(System.in);
        
        do {
        	System.out.println("[1] Login MovieDB;");
    		System.out.println("[2] Exit MovieDB.\n");
    		System.out.print("Enter a command: ");
    		
	        String command = s.nextLine();

	        if(command.toLowerCase().equals("1")) {
	        	int flag = 0;
	        	if(console.connect() == 0)	return;
	        	
	        	do {
	        		System.out.println("\n***** MENU *****\n");
	        		System.out.println("[1] Search Movies Info by Star;");
	        		System.out.println("[2] Insert a Star;");
	        		System.out.println("[3] Insert a Customer;");
	        		System.out.println("[4] Delete a Customer;");
	        		System.out.println("[5] Show MovieDB Metadata;");
	        		System.out.println("[6] Execute a SQL Command;");
	        		System.out.println("[7] Logout MovieDB;");
	        		System.out.println("[8] Exit MovieDB.\n");
	        		System.out.print("Enter a command: ");
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
	        			System.out.println("\n>>>>>>>>>> Loging Out... <<<<<<<<<<");
	        			++flag;
	        		}else if(action.equals("8")) {
	        			System.out.println("\n>>>>>>>>>> Exiting from MovieDB... <<<<<<<<<<");
	    	        	System.out.println("\nHave a good day! <3");
	        			return;
	        		} else {
	        			System.out.println("\nInvalid Action ID. Please Try Again.");
	        		}
	        	} while(flag == 0);
	        	
	        	System.out.println("\nByebye MovieDB!\n");
	        	console.resetConnection();
	        	
	        } else if(command.toLowerCase().equals("2")){
	        	System.out.println("\n>>>>>>>>>> Exiting from MovieDB... <<<<<<<<<<");
	        	System.out.println("\nHave a good day! <3");
	        	s.close();
	        	return;
	        } else {
	        	System.out.println("\nInvalid Input. Please Try Again.\n");
	        }
        } while (true);
	}
}
