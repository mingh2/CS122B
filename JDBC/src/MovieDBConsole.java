import java.sql.*;
import java.util.Scanner;

public class MovieDBConsole {
	
	private Connection connection;
	private Scanner s;

	public MovieDBConsole()
	{
		connection = null;
		s = new Scanner(System.in);
	}
	
	public void connect()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		}
		catch(Exception e)
		{
			System.out.println("Failed to Instantiate a New JDBC Driver. Please Try Again Later.");
			return;
		}
		
		while(connection == null)
		{
			
			try
			{
//				System.out.print("Enter Username: ");
//				String username = s.nextLine();
//				
//				System.out.print("Enter Password: ");
//				String password = s.nextLine();
				
				connection = DriverManager.getConnection("jdbc:mysql:///moviedb", "mytestuser", "mypassword");
			}
			catch(SQLException e)
			{
				System.out.println("Invalid Username and/or Password. Try Again.");
			}
		}
	}
	
	public void printMoviesWithStars()
	{
		Statement statement = null;
		
		try {	
			statement = connection.createStatement();
			ResultSet result;
			
			System.out.print("Enter Star's Name or ID: ");
			if(s.hasNextInt())
			{
				System.out.println(1);
				int id = s.nextInt();
				result = statement.executeQuery(
						"select movies.id, title, year, director, banner_url, trailer_url "
						+ "from movies, stars_in_movies "
						+ "where movies.id = stars_in_movies.movie_id and stars_in_movies.star_id = " + id);
			}
			else
			{
				String[] names = s.nextLine().split(" ");
				if(names.length == 1)
				{
					String name = names[0];
					result = statement.executeQuery(
							"select movies.id, title, year, director, banner_url, trailer_url "
							+ "from movies, stars_in_movies, stars "
							+ "where movies.id = stars_in_movies.movie_id and stars.id = stars_in_movies.star_id "
							+ "and (stars.first_name = '" + name + "' or stars.last_name = '" + name + "')");
					
				}
				else if(names.length == 2)
				{
					String first_name = names[0];
					String last_name = names[1];
					
					result = statement.executeQuery(
							"select movies.id, title, year, director, banner_url, trailer_url "
							+ "from movies, stars_in_movies, stars "
							+ "where movies.id = stars_in_movies.movie_id and stars.id = stars_in_movies.star_id "
							+ "and stars.first_name = '" + first_name + "'and stars.last_name = '" + last_name + "'");
					
				}
				else
				{
					System.out.println("Please Enter a Valid Name or ID");
					return;
				}
			}
			
			if(!result.isBeforeFirst())
			{
				System.out.println("No Record Found.");
			}
			else
			{
				printMoviesResultSet(result);
			}
			
		} catch (SQLException e) {
			System.out.println("Database Access Denied.");
			System.out.println(e.toString());
		}
		finally {
			if(statement != null)
			{
				try {
					statement.close();
				} catch (SQLException e) {
				}
			}
		}
	     
	}
	
	private void printMoviesResultSet(ResultSet result) throws SQLException
	{
		while(result.next())
		{
			int id = result.getInt("id");
			int year = result.getInt("year");
			String title = result.getString("title");
			String director = result.getString("director");
			String banner_url = result.getString("banner_url");
			String trailer_url = result.getString("trailer_url");
			
			System.out.format("|ID: %-8d | Title: %-45s | Year: %d | Director: %-25s | \n"
					+ "|Banner URL: %-106s | \n"
					+ "|Trailer URL: %-105s | \n", id, title, year, director, banner_url, trailer_url);
			System.out.println();
		}
	}
	
	public void insertAStar()
	{
		System.out.print("Enter Star's Name: ");
		String[] names = s.nextLine().split(" ");
		String firstName = "";
		String lastName;
		if(names.length == 1)
		{
			lastName = names[0];
		}
		else if(names.length == 2)
		{
			firstName = names[0];
			lastName = names[1];
		}
		else
		{
			System.out.println("Please Enter a Valid Name");
			return;
		}
		
		System.out.print("Enter Star's Date of Birth (YYYY-MM-DD) (Optional): ");
		String dob = "";
		dob = s.nextLine();
		dob = dob == "" ? dob : "1990-01-01"; 
		
		System.out.print("Enter Star's Photo URL (Optional): ");
		String photoURL = "";
		photoURL = s.nextLine();
		
		String insertStarSQL = "insert into stars "
							   + "(first_name, last_name, dob, photo_url) "
							   + "values ('" + firstName + "', '" + lastName
							   + "', '" + dob + "', '" + photoURL +"')";
		
		Statement statement = null;
		
		try {
			statement = connection.createStatement();
			if(statement.executeUpdate(insertStarSQL) == 1)
			{
				System.out.println("A New Star Inserted Successfully.");
			}
			else
			{
				System.out.println("Insertion Failed. ");
			}
			
			
		} catch (SQLException e) {
			System.out.println(e);
		}
		finally
		{
			if(statement != null)
			{
				try {
					statement.close();
				} catch (SQLException e) {
				}
			}
		}
		
		
	}
}
