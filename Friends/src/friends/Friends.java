package friends;

import structures.Queue;

import structures.Stack;

import java.util.*;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null if there is no
	 *         path from p1 to p2
	 */
	
	//helper methods
	private static boolean isNull(Graph g, String A, String B)
	{
		if((g == null || g.equals(null))
				||(A==null || A.equals(null) || A.equals(""))
				||(B==null || B.equals(null) || B.equals("")))
		{
			return true;
		}
		return false;
	}
	private static int[] initialize(int [] list)
	{
		for(int i : list)
		{
			list[i] = -2;
		}
		return list;
	}
	private static ArrayList<String> buildPath(Graph g, int[] prevVisited, Person End)
	{
		ArrayList<String> path = new ArrayList<>();
		Stack<String> nameStack = new Stack<>();
		
		int prevVisIndex = prevVisited[g.map.get(End.name)];
		
		nameStack.push(End.name);
		
		while(prevVisIndex != -2)
		{
			nameStack.push(g.members[prevVisIndex].name);
			prevVisIndex = prevVisited[prevVisIndex];
		}
		while(!nameStack.isEmpty())
			path.add(nameStack.pop());
		
		
		return path;
	}
	
	//Depth-First Search Algorithm helper method
	private static void DepthFSearch(Graph g, ArrayList<String> ConnectorList,
			boolean[] hasVisited, boolean[] hasBack,
			int[] DFSlist, int[] BackList,
			int point, int i, int prev)
	{
		if(!hasVisited[point])
		{
			hasVisited[point]=true;
			DFSlist[point]=DFSlist[prev]+1;
			BackList[point]=DFSlist[point];
			
			for(Friend ptfr = g.members[point].first; ptfr!=null; ptfr=ptfr.next)
			{
				int friendIndex=ptfr.fnum;
				
				if(!hasVisited[friendIndex])
				{
					DepthFSearch(g, ConnectorList, hasVisited, hasBack, DFSlist, BackList, friendIndex, i, point);
					
					if(DFSlist[point] > BackList[friendIndex])
					{
						BackList[point] = Math.min(BackList[point], BackList[friendIndex]);
					}
					if((BackList[friendIndex] >= DFSlist[point]) && !ConnectorList.contains(g.members[point].name))
					{
						if(point!=i || hasBack[point])
						{
							ConnectorList.add(g.members[point].name);
						}
					}
					hasBack[point] = true;
				}
				else
				{
					BackList[point]=Math.min(BackList[point],DFSlist[friendIndex]);
				}
			}
		}
	}
	
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		
		if(isNull(g, p1, p2) || (g.map.get(p1)==null || g.map.get(p2)==null))
		{
			System.out.println("A path could not be made.");
			return null;
		}
		
		boolean[] hasVisited = new boolean[g.members.length];
		int[] prevVisited = initialize(new int[g.members.length]);
		
		// the initialize method initializes every element in the array to -2, because null doesn't work and 0 can exist as a location, i think(I don't want to take chances) and -1 isn't original enough.
		
		Person Enter = g.members[g.map.get(p1)]; //Point A
		Person End = g.members[g.map.get(p2)];	//Point B
		
		boolean pathFound = false; //Boolean tells us whether a path was found
		
		Queue<Person> PersonQ = new Queue<>();
		for(PersonQ.enqueue(Enter);!PersonQ.isEmpty();)
		{
			Person point = PersonQ.dequeue();
			
			if(point!=End)
			{
				int i = g.map.get(point.name);
				hasVisited[i] = true;
				
				for(Friend ptfr = g.members[i].first; ptfr != null; ptfr=ptfr.next)
				{
					int friendIndex=ptfr.fnum;
					if(!hasVisited[friendIndex])
					{
						PersonQ.enqueue(g.members[friendIndex]);
						prevVisited[friendIndex] = i;
						hasVisited[friendIndex] = true;
					}
				}
			}
			else
			{
				pathFound = true;			
				break;
			}
			
		}
		if(!pathFound) 
		{
			System.out.println("A path could not be made.");
			return null;
		}
		return buildPath(g, prevVisited, End);
	
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		
		if(isNull(g, school, school))
		{
			System.out.println("Cannot establish clique because one of the arguments is null.");
			return null;
		} //null if school is null
		
			ArrayList<ArrayList<String>> MasterList = new ArrayList<ArrayList<String>>();
			boolean[] hasVisited = new boolean[g.members.length];
			
			for(Person i : g.members)
			{	
				ArrayList<String> friendIslet = new ArrayList<String>();
				Queue<Person> PersonQ = new Queue<Person>();
				int index = g.map.get(i.name);
				
				if(i.school != null) //down the rabbit hole
				{
					if(!hasVisited[index] && i.school.equals(school)) //has this node been visited?
					{
						for(PersonQ.enqueue(i); !PersonQ.isEmpty();) //throw it in the queue
						{
							Person point = PersonQ.dequeue();
							friendIslet.add(point.name);
							hasVisited[g.map.get(point.name)] = true; //It's been visited now
							
							for(Friend ptfr = g.members[g.map.get(point.name)].first; 
									ptfr != null;
									ptfr = ptfr.next) //travel through their friend's list and find all friends who are in the same school. Because islands exist we do this for all the islet social groups
							{
								int friendIndex = ptfr.fnum;
								String school2 = g.members[friendIndex].school;
								
								if(school2!=null)
								{
									if(!hasVisited[friendIndex] && school2.equals(school))
									{
										PersonQ.enqueue(g.members[friendIndex]);
										hasVisited[friendIndex] = true;
									}
								}
							}
							
						}
						MasterList.add(friendIslet);
					}
				}
			}
			if (MasterList.size()<1)
			{
				System.out.println("No students at the school.");
				return null;
			}
			
			return MasterList;
	}
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {

		if(isNull(g, "temp","temp"))
		{
			System.out.println("Your graph is missing, silly...");
			return null;
		}
		
		boolean[] hasVisited = new boolean[g.members.length], hasBack = new boolean[g.members.length];
		int[] DFSlist = initialize(new int[g.members.length]), BackList = initialize(new int[g.members.length]);
		
		ArrayList<String> ConnectorList = new ArrayList<>();
		for(int i=0;i<g.members.length; i++)
		{
			if(!hasVisited[i])
				DepthFSearch(g, ConnectorList, hasVisited, hasBack, DFSlist, BackList, i,i,i);
		}
		if (ConnectorList.size()<1)
		{
			System.out.println("No connectors found.");
			return null;
		}
		return ConnectorList;
	}
}