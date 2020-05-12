/**
 * IDSA Long Project 2
 * Group members:
 * Adarsh Raghupati   axh190002
 * Akash Akki         apa190001
 * Keerti Keerti      kxk190012
 * Stewart cannon     sjc160330
 */

// change package to your netid
package axh190002;

import axh190002.Graph.Edge;
import axh190002.Graph.Factory;
import axh190002.Graph.GraphAlgorithm;
import axh190002.Graph.Vertex;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PERT extends GraphAlgorithm<PERT.PERTVertex>
{
	Graph g;//graph given
	PERTVertex [] nodes;//array of PERTVertexes from graph
	ArrayList <Integer> crit= new ArrayList<Integer>();
	int maxTime;
	//crit contains a list of critical nodes
	public static class PERTVertex implements Factory
	{
		int ec;//earliest completion
		int es=0;//earliest start
		int ls;//latest start
		int lc;//latest completion
		int slack;
		int duration;
		boolean isCrit;//is it a critical node
		Vertex info;//vertex in the graph, for the edges

		/**
		 * takes vertex and adds it to a set of other values for PERT
		 * 
		 * @param u: given vertex
		 * 
		 */
		public PERTVertex(Vertex u)
		{
			ec=-1;
			lc=-1;
			slack=-1;
			info=u;
		}

		public PERTVertex make(Vertex u)
		{
			return new PERTVertex(u);
		}
	}

	/**
	 * turns all vertices in a given graph into equivalent PERTVertices
	 * 
	 * @param g: a given graph
	 */
	public PERT(Graph g)
	{
		super(g, new PERTVertex(null));
		this.g=g;
		Vertex[] temp=g.getVertexArray();
		nodes=new PERTVertex[temp.length];
		for(int i=0;i<temp.length;i++)
		{
			nodes[i]=new PERTVertex(temp[i]);
		}
		
	}



	public void setDuration(Vertex u, int d)
	{
		nodes[u.name-1].duration=d;
	}

	/**
	 * performs PERT algorithm
	 * at the end is left with a pert object with each vertex having the corresponding slack, criticality, and duration
	 * 
	 * @return false if a DAG, true if not a DAG
	 */
	public boolean pert()
	{
		//gets topological order using DFS
		List<Graph.Vertex> topList = DFS.topologicalOrder1(g);
		if(topList==null)
			return true;
		
		//for each node
		for(int i=0;i<nodes.length;i++)
		{
			int index=topList.get(i).name-1;
			nodes[index].ec=nodes[index].es+nodes[index].duration;
			//for each edge out of the node
			for(Edge e: g.outEdges(topList.get(i)))
			{
				//if the early start is less than early completion
				if(nodes[e.to.name-1].es < nodes[index].ec)
				{
					nodes[e.to.name-1].es= nodes[index].ec;
				}
			}
			
			
		}
       maxTime = nodes[topList.size()-1].ec;
		//runs back through the topological order to set late complete
		for(PERTVertex a : nodes)
		{
			a.lc=nodes[nodes.length-1].ec;
		}
		
		for(int i=nodes.length-1;i>=0;i--)
		{
			int index=topList.get(i).name-1;
			nodes[index].ls=nodes[index].lc-nodes[index].duration;
			nodes[index].slack=nodes[index].lc-nodes[index].ec;
			for(Edge e: g.inEdges(topList.get(i)))
			{
				if(nodes[e.from.name-1].lc > nodes[index].ls)
				{
					nodes[e.from.name-1].lc= nodes[index].ls;
				}
			}
		}
		
		//sets criticality to true on critical nodes
		for(Vertex a: topList )
		{
			if(nodes[a.name-1].slack==0)
			{
				crit.add(a.name);
				nodes[a.name-1].isCrit=true;
			}
		}
		
		
		return false;
	}

	public int ec(Vertex u)
	{
		return nodes[u.name-1].ec;
	}

	public int lc(Vertex u)
	{
		return nodes[u.name-1].lc;
	}

	public int slack(Vertex u)
	{
		return nodes[u.name-1].slack;
	}

	/**
	 * gives length of the critical path by searching graph from start
	 * 
	 * @return length of critical path
	 */
	public int criticalPath()
	{
		
	/*	int dur=0;
		PERTVertex temp= nodes[1];
		dur+=temp.duration;
		//searches graph for critical nodes until end node
		while(temp.info.outDegree()!=0)
		{
			for(Edge e: g.outEdges(temp.info))
			{
				if(nodes[e.to.name-1].isCrit)
				{
					temp=nodes[e.to.name-1];
					dur+=temp.duration;
					break;
				}
				
			}
		}
		return dur;*/
	return maxTime;
	}

	public boolean critical(Vertex u)
	{
		
		return nodes[u.name-1].isCrit;
	}

	/**
	 * returns the number of critical nodes
	 * 
	 * @return size of crit, or number of all critical nodes
	 */
	public int numCritical()
	{
		return crit.size();
	}

	// setDuration(u, duration[u.getIndex()]);
	/**
	 * given a graph and durations, creates a PERT object
	 * 
	 * @param g: given graph
	 * @param duration: given durations
	 * @return: corresponding PERT object
	 */
	public static PERT pert(Graph g, int[] duration)
	{
		PERT ret= new PERT(g);
		Vertex source = g.getVertex(1);
		Vertex target = g.getVertex(g.size());
		int i=0;
		for(Vertex u: g)
		{
			if(u.name!=1){
				g.addEdge(source.getIndex(),u.getIndex(),1);
			}
			if(u.name!=g.size()){
				g.addEdge(u.getIndex(),target.getIndex(),1);
			}

			ret.setDuration(u, duration[u.getIndex()]);
		}
		return ret;
	}

	public static void main(String[] args) throws Exception
	{
		String graph = "11 12   2 4 1   2 5 1   3 5 1   3 6 1   4 7 1   5 7 1   5 8 1   6 8 1   6 9 1   7 10 1   8 10 1   9 10 1      0 3 2 3 2 1 3 2 4 1 0";
		graph = "11 13 	1 2 1	2 3 1	2 4 1	2 5 1	3 6 1	4 7 1	4 8 1	5 8 1	6 9 1	7 11 1	8 10 1	9 11 1	10 11 1		3 5 2 4 7 3 7 3 7 2 2";
		graph="7 8		1 2 1	1 3 1	2 4 1	3 5 1	5 6 1	3 7 1	4 7 1	6 7 1	0 1 1 2 2 1 0";
		Scanner in;
		// If there is a command line argument, use it as file from which
		// input is read, otherwise use input from string.
		in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(graph);
		Graph g = Graph.readDirectedGraph(in);
		g.printGraph(false);

		int[] duration = new int[g.size()];
		for(int i=0; i<g.size(); i++) {
			duration[i] = in.nextInt();
		}
		PERT p = pert(g,duration);
		/*for (Vertex u : g)
		{
			p.setDuration(u, in.nextInt());
		}*/
		
		// Run PERT algorithm. Returns null if g is not a DAG
		if (p.pert())
		{
			System.out.println("Invalid graph: not a DAG");
		} else
		{
			System.out.println("Output: "+ p.criticalPath()+" " + p.numCritical());
			System.out.println("u\tDur\tEC\tLC\tSlack\tCritical");
			for (Vertex u : g)
			{
				System.out.println(u + "\t" +duration[u.getIndex()]+"\t"+ p.ec(u) + "\t" + p.lc(u) + "\t" + p.slack(u) + "\t" + p.critical(u));
			}
		}
	}
}
