package friends;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FriendsDriver
{
	public static void shortestPath(Scanner userInputScanner, Graph g)
	{
		userInputScanner.nextLine();
		System.out.println("Enter p1: ");
		String p1 = userInputScanner.nextLine();
		//if (p1.equals(""))
		{
			//p1 = "sam";
		}
		
		System.out.println("Enter p2: ");
		String p2 = userInputScanner.nextLine();
		//if (p2.equals(""))
		{
			//p2 = "aparna";
		}
		
		ArrayList<String> shortestChain = Friends.shortestChain(g, p1, p2);
		System.out.println(shortestChain);
	}
	
	public static void cliques(Scanner userInputScanner, Graph g)
	{
		userInputScanner.nextLine();
		System.out.println("Enter school:");
		String school = userInputScanner.nextLine();
		//if (school.equals(""))
		{
			//school = "rutgers";
		}
		
		ArrayList<ArrayList<String>> allCliques = Friends.cliques(g, school);
		System.out.println(allCliques);
	}
	
	public static void connectors(Scanner userInputScanner, Graph g)
	{
		ArrayList<String> connectors = Friends.connectors(g);
		System.out.println(connectors);
	}
	
	public static void main(String[] args) throws FileNotFoundException
	{
		Scanner userInputScanner = new Scanner(System.in);
		System.out.println("Enter the name of the input file: ");
		String fileName = userInputScanner.nextLine();
		if (fileName.equals(""))
		{
			fileName = "friends.txt";
		}
		
		File inputFile = new File(fileName);
		Scanner inputFileScanner = new Scanner(inputFile);
		
		Graph g = new Graph(inputFileScanner);
		
		int choice = 1;
		
		while (choice > 0)
		{
			System.out.println();
			System.out.println("0) Quit");
			System.out.println("1) Shortest Path");
			System.out.println("2) Cliques");
			System.out.println("3) Connectors");
			
			System.out.println("WHAT IS YOUR CHOICE?");
			choice = userInputScanner.nextInt();
			
			switch (choice)
			{
				case 1:
					shortestPath(userInputScanner, g);
					break;
				case 2:
					cliques(userInputScanner, g);
					break;
				case 3:
					connectors(userInputScanner, g);
				default:
					break;
			}
		}
	}
}
