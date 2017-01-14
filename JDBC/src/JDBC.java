public class JDBC {
	public static void main(String[] args) throws Exception
	{
        MovieDBConsole console = new MovieDBConsole();
        
        console.connect();
//        console.printMoviesWithStars();
//        console.insertNewStar();
//        console.insertNewCustomer();
//        console.deleteACustomer();
//        console.printMetadata();
        console.respondToValidCommand();
	}
}
