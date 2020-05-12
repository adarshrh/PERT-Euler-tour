/**
 * IDSA Long Project 2
 * Group members:
 * Adarsh Raghupati   axh190002
 * Akash Akki         apa190001
 * Keerti Keerti      kxk190012
 * Stewart cannon     sjc160330
 */
package axh190002;

import axh190002.Graph.Edge;
import axh190002.Graph.Factory;
import axh190002.Graph.GraphAlgorithm;
import axh190002.Graph.Vertex;

import java.io.File;
import java.util.*;


public class Euler extends GraphAlgorithm<Euler.EulerVertex> {
    static int VERBOSE = 1;
    Vertex start;
    LinkedList<Vertex> tour;
    
	// You need this if you want to store something at each node
    static class EulerVertex implements Factory {

        EulerVertex(Vertex u) {

        }
	public EulerVertex make(Vertex u) { return new EulerVertex(u); }

    }

    // To do: function to find an Euler tour
	public Euler(Graph g, Vertex start) {
	super(g, new EulerVertex(null));
	this.start = start;

	tour = new LinkedList<>();
    }

    /* To do: test if the graph is Eulerian.
     * If the graph is not Eulerian, it prints the message:
     * "Graph is not Eulerian" and one reason why, such as
     * "inDegree = 5, outDegree = 3 at Vertex 37" or
     * "Graph is not strongly connected"
     */
    public boolean isEulerian() {
        Vertex[] vertexArr = this.g.getVertexArray();

        for (Vertex v : vertexArr){
            if(v.inDegree()!= v.outDegree()){
                System.out.println("Graph is not Eulerian");
                System.out.println("inDegree = "+v.inDegree()+" outDegree = "+v.outDegree()+ " at Vertex "+v.name);
                return false;
            }
        }
      DFS dfs = new DFS(this.g);
       dfs = dfs.stronglyConnectedComponents(this.g);
       if(dfs.connectedComponents()!=1){
           System.out.println("Graph is not Eulerian");
           System.out.println("Graph is not strongly connected");
           return false;
       }
        return true;
	}

    /**
     * Returns the list of vertices in the order of Euler path
     * @return
     */
    public List<Vertex> findEulerTour() {
	if(!isEulerian()) { return new LinkedList<>(); }
        Stack<Vertex> stack = new Stack<>();
	    stack.push(start);
	    int totalEdges = this.g.m+1;
        Edge e = this.g.adj(start).outEdges.get(0);
        Vertex u = e.to;
        e.setWeight(Integer.MIN_VALUE);
	    while (!stack.empty() && totalEdges > 0){
	        Vertex adjacent = getAdjacentVertex(u,this.g);
	        if(null==adjacent){
	            tour.addFirst(u);
                totalEdges--;
	            if(!stack.empty()){
	                u = stack.pop();
	            }
            } else {
	            stack.push(u);
	            u = adjacent;
            }

        }
	    while(!stack.empty() ) {
	        tour.addFirst(stack.pop());
        }
	    tour.addFirst(start);
	return tour;
    }

    /**
     * Returns the vertex which is not yet visited
     * @param u
     * @param g
     * @return
     */
    private Vertex getAdjacentVertex(Vertex u, Graph g) {
        Vertex adjacent = null;
        for (Edge e: g.adj(u).outEdges){
            if(e.getWeight()!=Integer.MIN_VALUE){
                adjacent = e.to;
                e.setWeight(Integer.MIN_VALUE);
                return adjacent;
            }
        }
        return adjacent;
    }

    public static void main(String[] args) throws Exception {
        Scanner in;
        if (args.length > 0) {
            in =  new Scanner(new File(args[0]));
        } else {
	    String input = "8 12 1 2 1 2 3 1 3 1 1 2 6 1 4 5 1 4 2 1 5 8 1 5 7 1 6 5 1 6 4 1 7 6 1 8 4 1";
            in = new Scanner(input);
        }
        int start = 1;
        if(args.length > 1) {
	    start = Integer.parseInt(args[1]);
		}
		// output can be suppressed by passing 0 as third argument
		if(args.length > 2) {
            VERBOSE = Integer.parseInt(args[2]);
        }
        Graph g = Graph.readDirectedGraph(in);
        Vertex startVertex = g.getVertex(start);
        Timer timer = new Timer();

    	Euler euler = new Euler(g, startVertex);
	List<Vertex> tour = euler.findEulerTour();
	
		
        timer.end();
        if(VERBOSE > 0) {
	    System.out.println("Output:");
	    for (Vertex v: tour){
	        System.out.print(v.name+",");
        }
            // print the tour as sequence of vertices (e.g., 3,4,6,5,2,5,1,3)
	    System.out.println();
        }
        System.out.println(timer);
        System.out.println("Size="+tour.size());
        System.out.println("Start Vertex is "+euler.start.name);

    }

    public void setVerbose(int ver) {
	VERBOSE = ver;
    }
}
