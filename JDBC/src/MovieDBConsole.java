import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class MovieDBConsole {
	
	private Connection connection;
	private Scanner s;

	private String promptForString(String prompt)
	{
		System.out.print(prompt);
		return s.nextLine();
	}
	
	public void resetConnection() {
		connection = null;
	}
	
	public MovieDBConsole()
	{
		System.out.println(">>>>>>>>>> Welcome to MovieDB <<<<<<<<<<\n");
		connection = null;
		s = new Scanner(System.in);
	}
	
	public void connect()
	{
		System.out.println("\n>>>>>>>>>> Connecting to the Database... <<<<<<<<<<\n");
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
				System.out.print("Enter Username: ");
				String username = s.nextLine();
				
				System.out.print("Enter Password: ");
				String password = s.nextLine();
				
				connection = DriverManager.getConnection("jdbc:mysql:///moviedb", username, password);
//				connection = DriverManager.getConnection("jdbc:mysql:///moviedb", "kevinke", "0000");
//				connection = DriverManager.getConnection("jdbc:mysql:///moviedb", "mytestuser", "mypassword");
			}
			catch(SQLException e)
			{
				System.out.println("Invalid Username and/or Password. Try Again.");
			}
		}
		System.out.println("\n>>>>>>>>>> Connected Successfully <<<<<<<<<<\n");
	}
	
	public void printMoviesWithStars()
	{
		Statement statement = null;
		System.out.println("\n>>>>>>>>>> Searching Movies by Stars <<<<<<<<<<\n");
		try {	
			statement = connection.createStatement();
			ResultSet result;
			
			System.out.print("Enter Star's Name or ID (Name Could Be First Name and/or Last Name): ");
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
	
	public void insertNewStar()
	{
		System.out.println("\n>>>>>>>>>> Inserting A New Star <<<<<<<<<<\n");
		
		String firstName = "";
		String lastName = "";
		String dob;
		String photoURL;
		
		while(firstName.isEmpty() && lastName.isEmpty())
		{
			System.out.println("Please Enter Star's First Name and/or Last Name");
			firstName = promptForString("First Name: ");
			lastName = promptForString("Last Name: ");
			if(!firstName.isEmpty() && lastName.isEmpty())
			{
				lastName = firstName;
				firstName = "";
			}
		}
		
		dob = promptForString("Please Enter Star's Date of Birth (YYYY-MM-DD) (Optional): ");
		photoURL = promptForString("Please Enter Star's Photo URL (Optional): ");
		
		String insertStarSQL = "insert into stars "
							   + "(first_name, last_name, dob, photo_url) "
							   + "values ( ?, ?, ?, ?)";
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(insertStarSQL);
			
			statement.setString(1, firstName);
			statement.setString(2, lastName);
			statement.setDate(3, dob.isEmpty() ? null: Date.valueOf(dob));
			statement.setString(4, photoURL);
			
			if(statement.executeUpdate() == 1)
			{
				System.out.println("A New Star Inserted Successfully.");
			}
			else
			{
				System.out.println("Insertion Failed. ");
			}
		} catch (SQLException e) {
			System.out.println(e);
			System.out.println("Insertion Failed. ");
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
		System.out.println("\n>>>>>>>>>> Insertion Succeeded (Star) <<<<<<<<<<\n");
	}
	
	public void insertNewCustomer()
	{
		System.out.println("\n>>>>>>>>>> Inserting A New Customer <<<<<<<<<<\n");
		
		String firstName = "";
		String lastName= "";
		String creditCardID = "";
		String address = "";
		String email = "";
		String password = "";
		
		while(firstName.isEmpty() && lastName.isEmpty())
		{
			System.out.println("Please Enter Customer's First Name and/or Last Name");
			firstName = promptForString("First Name: ");
			lastName = promptForString("Last Name: ");
			if(!firstName.isEmpty() && lastName.isEmpty())
			{
				lastName = firstName;
				firstName = "";
			}
		}
		
		while(creditCardID.isEmpty())
		{
			creditCardID = promptForString("Please Enter Customer's Credit Card ID (Required): ");
		}
		
		while(address.isEmpty())
		{
			address = promptForString("Please Enter Customer's Address (Required): " );
		}
		
		while(email.isEmpty())
		{
			email = promptForString("Please Enter Customer's Email (Required): " );
		}
		
		while(password.isEmpty())
		{
			password = promptForString("Please Enter Customer's password (Required): " );
		}
		
		// Check if the credit card information is valid.
		String ccIDSQL = "select count(*) from creditcards where id = '" + creditCardID +"'";
		Statement ccStatement = null;
		
		try {
			ccStatement = connection.createStatement();
			ResultSet result = ccStatement.executeQuery(ccIDSQL);
			result.next();
			
			if(result.getInt(1) == 0)
			{
				System.out.println("Invalid Credit Card Information. ");
			}
		} catch (SQLException e1) {
			System.out.println("Invalid Credit Card Information. ");
		}
		finally
		{
			if(ccStatement != null)
			{
				try {
					ccStatement.close();
				} catch (SQLException e) {
					System.out.println("Insertion Failed. [SQLException: Cannot Close \"ccStatement\".]");
					return;
				}
			}
		} // Finished checking credit card.
		
		String insertCustomerSQL = "insert into customers "
				   + "(first_name, last_name, cc_id, address, email, password) "
				   + "values (?, ?, ?, ?, ?, ?)";
		
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(insertCustomerSQL);
			
			statement.setString(1, firstName);
			statement.setString(2, lastName);
			statement.setString(3, creditCardID);
			statement.setString(4, address);
			statement.setString(5, email);
			statement.setString(6, password);
		
			if(statement.executeUpdate() == 1)
			{
				System.out.println("\n>>>>>>>>>> A New Customer Inserted Successfully <<<<<<<<<<\n");
			}
			else
			{
				System.out.println("Insertion Failed. ");
			}
		}
		catch (SQLException e) {
			System.out.println(e);
			System.out.println("Insertion Failed. ");
		}
		finally
		{
			if(statement != null)
			{
				try {
					statement.close();
				} catch (SQLException e) {
					System.out.println("Failed to close \"statement\". ");
				}
			}
		}
	}
	
	public void deleteACustomer() {
		System.out.println("\n>>>>>>>>>> Removing A Customer <<<<<<<<<<\n");
		
		String customerID = "";
		
		while(customerID.isEmpty()) 
		{
			System.out.println("Please Enter the ID of the customer that will be removed");
			customerID = promptForString("Customer ID: ");
		}
		
		String deleteCustomerSQL = "DELETE FROM customers WHERE id = ?";
		PreparedStatement deleteStatement = null;
		
		try {
			deleteStatement = connection.prepareStatement(deleteCustomerSQL);
			deleteStatement.setInt(1, Integer.parseInt(customerID));
			
			if(deleteStatement.executeUpdate() == 1)
			{
				System.out.println("\n>>>>>>>>>> A Customer is Deleted Successfully <<<<<<<<<<\n");
			} else {
				System.out.println("Ah! Invalid ID!!!!");
			}
		} catch (SQLException e) {
			System.out.println("Removal Failed.");
		}
		finally
		{
			if(deleteStatement != null)
			{
				try {
					deleteStatement.close();
				} catch (SQLException e) {
					System.out.println("Failed to close \"deleteStatement\".");
				}
			}
		}
	}
	
	// Print out the name of each table and, for each table, each attribute and its type.
	public void printMetadata() {
		Statement statement = null;
		String[] listOfTables = {"creditcards", "customers", "genres", "genres_in_movies", 
									"movies", "sales", "stars", "stars_in_movies"};
		
		System.out.println ("TableName               ColumnName     Type  ");
        System.out.println ("---------------------------------------------------------");
        
		for(int i = 0; i < listOfTables.length; ++i) {
			try {
				statement = connection.createStatement();
				ResultSet result = statement.executeQuery("SELECT * FROM " + listOfTables[i]);
				ResultSetMetaData metaResult = result.getMetaData();
				int colCount = metaResult.getColumnCount();
				
				for(int j = 1; j <= colCount; ++j) {
					System.out.format("%-24s%-15s%s\n", listOfTables[i], metaResult.getColumnName(j), metaResult.getColumnTypeName(j));
				}
			} catch (SQLException e) {
				System.out.println(e);
			}
		}
	}
	
	public void respondToValidCommand() {
		System.out.println("\n>>>>>>>>>> Entering A Valid Command <<<<<<<<<<\n");
		
		String command = "";
		int flag = 0;
		while(flag == 0) {
			command = promptForString("Please enter a command for the database: ");

			if(command.substring(0, 6).toUpperCase().equals("SELECT")) {
				selectCommand(command);
				++flag;
			} else if (command.substring(0, 6).toUpperCase().equals("DELETE")) {
				deleteCommand(command);
				++flag;
			} else if (command.substring(0, 6).toUpperCase().equals("UPDATE")) {
				updateCommand(command);
				++flag;
			} else if (command.substring(0, 6).toUpperCase().equals("INSERT")) {
				insertCommand(command);
				++flag;
			} else {
				System.out.println("\nSorry. Invalid Command. Please try again.\n");
			}
		}
	}
	
	private void selectCommand(String command) {
		Statement statement = null;
		ArrayList<String> attributes = new ArrayList<String>();
		
		try {
			String[] splitCommand = command.split(" ");
			if(!splitCommand[1].equals("*")) {
				
				for(int i = 0; i < splitCommand.length; ++i) {
					
					if(splitCommand[i].toUpperCase().equals("FROM")) {
						break;
					}
					
					String[] temp = splitCommand[i].split(",");
					
					for(int j = 0; j < temp.length; ++j) {
						if(temp[j].toUpperCase().equals("SELECT")) {
							continue;
						}
						
						attributes.add(temp[j]);
					}
				}
				
				statement = connection.createStatement();
				ResultSet result = statement.executeQuery(command);
				
				System.out.println();
				for(int i = 0; i < attributes.size(); ++i) {
					if(attributes.get(i).toLowerCase().equals("address") || attributes.get(i).toLowerCase().equals("title")) {
						System.out.format("%-50s", attributes.get(i));
					} else if(attributes.get(i).toLowerCase().equals("id")) {
						System.out.format("%-8s", attributes.get(i));
					} else if(attributes.get(i).toLowerCase().equals("first_name") || attributes.get(i).toLowerCase().equals("last_name")) {
						System.out.format("%-15s", attributes.get(i));
					} else if(attributes.get(i).toLowerCase().equals("cc_id") || attributes.get(i).toLowerCase().equals("email")) {
						System.out.format("%-25s", attributes.get(i));
					} else if(attributes.get(i).toLowerCase().equals("banner_url") || attributes.get(i).toLowerCase().equals("trailer_url")
							|| attributes.get(i).toLowerCase().equals("photo_url")) {
						System.out.format("%-150s", attributes.get(i));
					} else if(attributes.get(i).toLowerCase().equals("director")) {
						System.out.format("%-40s", attributes.get(i));
					} else if(attributes.get(i).toLowerCase().equals("year")) {
						System.out.format("%-6s", attributes.get(i));
					} else {
						System.out.format("%-20s", attributes.get(i));
					}
				}
				System.out.println();
				for(int i = 0; i < attributes.size(); ++i) {
					if(attributes.get(i).toLowerCase().equals("address") || attributes.get(i).toLowerCase().equals("title")) {
						System.out.print("--------------------------------------------------");
					} else if(attributes.get(i).toLowerCase().equals("id")) {
						System.out.print("--------");
					} else if(attributes.get(i).toLowerCase().equals("first_name") || attributes.get(i).toLowerCase().equals("last_name")) {
						System.out.print("---------------");
					} else if(attributes.get(i).toLowerCase().equals("cc_id") || attributes.get(i).toLowerCase().equals("email")) {
						System.out.print("-------------------------");
					} else if(attributes.get(i).toLowerCase().equals("banner_url") || attributes.get(i).toLowerCase().equals("trailer_url")
							|| attributes.get(i).toLowerCase().equals("photo_url")) {
						System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------");
					} else if(attributes.get(i).toLowerCase().equals("director")) {
						System.out.println("----------------------------------------");
					} else if(attributes.get(i).toLowerCase().equals("year")) {
						System.out.println("------");
					}else {
						System.out.print("--------------------");
					}
				}
				System.out.println();
				
				while(result.next()) {
					for(int i = 0; i < attributes.size(); ++i) {
						if(attributes.get(i).toLowerCase().equals("address") || attributes.get(i).toLowerCase().equals("title")) {
							System.out.format("%-50s", result.getString(attributes.get(i)));
						} else if(attributes.get(i).toLowerCase().equals("id")) {
							System.out.format("%-8s", result.getString(attributes.get(i)));
						} else if(attributes.get(i).toLowerCase().equals("first_name") || attributes.get(i).toLowerCase().equals("last_name")) {
							System.out.format("%-15s", result.getString(attributes.get(i)));
						} else if(attributes.get(i).toLowerCase().equals("cc_id") || attributes.get(i).toLowerCase().equals("email")) {
							System.out.format("%-25s", result.getString(attributes.get(i)));
						} else if (attributes.get(i).toLowerCase().equals("banner_url") || attributes.get(i).toLowerCase().equals("trailer_url")
								|| attributes.get(i).toLowerCase().equals("photo_url")) {
							System.out.format("%-150s", result.getString(attributes.get(i)));
						} else if(attributes.get(i).toLowerCase().equals("director")) {
							System.out.format("%-40s", result.getString(attributes.get(i)));
						} else if(attributes.get(i).toLowerCase().equals("year")) {
							System.out.format("%-6s", result.getString(attributes.get(i)));
						} else {
							System.out.format("%-20s", result.getString(attributes.get(i)));
						}
					}
					System.out.println();
				}
			} else {
				statement = connection.createStatement();
				ResultSet result = statement.executeQuery(command);
				ResultSetMetaData metaResult = result.getMetaData();
				int colCount = metaResult.getColumnCount();
				
				for(int i = 1; i <= colCount; ++i) {
					attributes.add(metaResult.getColumnName(i));
				}
				
				System.out.println();
				for(int i = 0; i < attributes.size(); ++i) {
					if(attributes.get(i).toLowerCase().equals("address") || attributes.get(i).toLowerCase().equals("title")) {
						System.out.format("%-50s", attributes.get(i));
					} else if(attributes.get(i).toLowerCase().equals("id")) {
						System.out.format("%-8s", attributes.get(i));
					} else if(attributes.get(i).toLowerCase().equals("first_name") || attributes.get(i).toLowerCase().equals("last_name")) {
						System.out.format("%-15s", attributes.get(i));
					} else if(attributes.get(i).toLowerCase().equals("cc_id") || attributes.get(i).toLowerCase().equals("email")) {
						System.out.format("%-25s", attributes.get(i));
					} else if(attributes.get(i).toLowerCase().equals("banner_url") || attributes.get(i).toLowerCase().equals("trailer_url")
							|| attributes.get(i).toLowerCase().equals("photo_url")) {
						System.out.format("%-150s", attributes.get(i));
					} else if(attributes.get(i).toLowerCase().equals("director")) {
						System.out.format("%-40s", attributes.get(i));
					} else if(attributes.get(i).toLowerCase().equals("year")) {
						System.out.format("%-6s", attributes.get(i));
					} else {
						System.out.format("%-20s", attributes.get(i));
					}
				}
				System.out.println();
				for(int i = 0; i < attributes.size(); ++i) {
					if(attributes.get(i).toLowerCase().equals("address") || attributes.get(i).toLowerCase().equals("title")) {
						System.out.print("--------------------------------------------------");
					} else if(attributes.get(i).toLowerCase().equals("id")) {
						System.out.print("--------");
					} else if(attributes.get(i).toLowerCase().equals("first_name") || attributes.get(i).toLowerCase().equals("last_name")) {
						System.out.print("---------------");
					} else if(attributes.get(i).toLowerCase().equals("cc_id") || attributes.get(i).toLowerCase().equals("email")) {
						System.out.print("-------------------------");
					} else if(attributes.get(i).toLowerCase().equals("banner_url") || attributes.get(i).toLowerCase().equals("trailer_url")
							|| attributes.get(i).toLowerCase().equals("photo_url")) {
						System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------");
					} else if(attributes.get(i).toLowerCase().equals("director")) {
						System.out.println("----------------------------------------");
					} else if(attributes.get(i).toLowerCase().equals("year")) {
						System.out.println("------");
					}else {
						System.out.print("--------------------");
					}
				}
				System.out.println();
				
				while(result.next()) {
					for(int i = 0; i < attributes.size(); ++i) {
						if(attributes.get(i).toLowerCase().equals("address") || attributes.get(i).toLowerCase().equals("title")) {
							System.out.format("%-50s", result.getString(attributes.get(i)));
						} else if(attributes.get(i).toLowerCase().equals("id")) {
							System.out.format("%-8s", result.getString(attributes.get(i)));
						} else if(attributes.get(i).toLowerCase().equals("first_name") || attributes.get(i).toLowerCase().equals("last_name")) {
							System.out.format("%-15s", result.getString(attributes.get(i)));
						} else if(attributes.get(i).toLowerCase().equals("cc_id") || attributes.get(i).toLowerCase().equals("email")) {
							System.out.format("%-25s", result.getString(attributes.get(i)));
						} else if (attributes.get(i).toLowerCase().equals("banner_url") || attributes.get(i).toLowerCase().equals("trailer_url")
								|| attributes.get(i).toLowerCase().equals("photo_url")) {
							System.out.format("%-150s", result.getString(attributes.get(i)));
						} else if(attributes.get(i).toLowerCase().equals("director")) {
							System.out.format("%-40s", result.getString(attributes.get(i)));
						} else if(attributes.get(i).toLowerCase().equals("year")) {
							System.out.format("%-6s", result.getString(attributes.get(i)));
						} else {
							System.out.format("%-20s", result.getString(attributes.get(i)));
						}
					}
					System.out.println();
				}
			}
			
		} catch (SQLException e1) {
			System.out.println(e1.getMessage());
			System.out.println("Invalid Select SQL Command. [SQLException]");
		}
		finally
		{
			if(statement != null)
			{
				try {
					statement.close();
				} catch (SQLException e) {
					System.out.println("Failed to close \"statement\".");
					return;
				}
			}
		}
		
		System.out.println("\n>>>>>>>>>> Finished the SELECT Mission <<<<<<<<<<\n");
	}

	private void deleteCommand(String command) {
		Statement statement = null;
		try {
			statement = connection.createStatement();
			int count = statement.executeUpdate(command);
			if(count > 1) {
				System.out.println("Deleted " + count + " instances from " + command.split(" ")[2] + " successfully.");
			} else if(count == 1) {
				System.out.println("Deleted " + count + " instance from " + command.split(" ")[2] + " successfully.");
			} else {
				System.out.println("No instance found.");
			}
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			System.out.println("Invalid Delete SQL Command.");
		} finally {
			if(statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					System.out.println("Failed to close \"statement\".");
					return;
				}
			}
		}
		
		System.out.println("\n>>>>>>>>>> Finished the DELETE Mission <<<<<<<<<<\n");
	}
	
	private void updateCommand(String command) {
		Statement statement = null;
		
		try {
			statement = connection.createStatement();
			int count = statement.executeUpdate(command);
			if(count > 1) {
				System.out.println("Updated " + count + " instances from " + command.split(" ")[1] + " successfully.");
			} else if(count == 1) {
				System.out.println("Updated " + count + " instance from " + command.split(" ")[1] + " successfully.");
			} else {
				System.out.println("No instance found.");
			}
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			System.out.println("Invalid Update SQL Command.");
		} finally {
			if(statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					System.out.println("Failed to close \"statement\".");
					return;
				}
			}
		}
		
		System.out.println("\n>>>>>>>>>> Finished the UPDATE Mission <<<<<<<<<<\n");
	}
	
	private void insertCommand(String command) {
		Statement statement = null;
		
		try {
			statement = connection.createStatement();
			int flag = statement.executeUpdate(command);
			if(flag != 0) {
				System.out.println("Inserted an instance into " + command.split(" ")[2] + " successfully.");
			}
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			System.out.println("Invalid Insert SQL Command.");
		} finally {
			if(statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					System.out.println("Failed to close \"statement\".");
					return;
				}
			}
		}
		
		System.out.println("\n>>>>>>>>>> Finished the INSERT Mission <<<<<<<<<<\n");
	}
}
