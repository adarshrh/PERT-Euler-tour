/**
 * IDSA Long Project 2
 * Group members:
 * Adarsh Raghupati   axh190002
 * Akash Akki         apa190001
 * Keerti Keerti      kxk190012
 * Stewart cannon     sjc160330
 */

package axh190002;

import axh190002.Graph.Vertex;

import java.io.File;
import java.util.*;

public class DFS extends Graph.GraphAlgorithm<DFS.DFSVertex> {
  public static boolean isCyclic;
  private List<List<Integer>> sccSet = new ArrayList<>();
  private int numberOfSCC;


  public void setNumberOfSCC(int numberOfSCC) {
    this.numberOfSCC = numberOfSCC;
  }

  public void setSccSet(List<List<Integer>> sccSet) {
    this.sccSet = sccSet;
  }

  // Enum to save the state of each vertex
  public enum VertexState {
    NEW,
    ACTIVE,
    COMPLETE;
  }

  public static class DFSVertex implements Graph.Factory {
    int cno;
    public Graph.Vertex parent;
    VertexState vertexState;

    /**
     * Initializes Vertex with parent, component number and vertex state
     *
     * @param u
     */
    public DFSVertex(Graph.Vertex u) {
      this.parent = null;
      this.cno = 0;
      vertexState = VertexState.NEW;
    }

    public DFSVertex make(Graph.Vertex u) {
      return new DFSVertex(u);
    }
  }

  public DFS(Graph g) {
    super(g, new DFSVertex(null));
    isCyclic = false;
  }

  /**
   * Method runs DFS algorithm to find the topological order of the DAG
   *
   * @return List of vertices in topological order
   */
  public LinkedList<Graph.Vertex> depthFirstSearch() {
    for (Graph.Vertex u : g) {
      get(u).parent = null;
      get(u).vertexState = VertexState.NEW;
    }
    LinkedList<Graph.Vertex> topologicalOrderList = new LinkedList<Graph.Vertex>();
    for (Graph.Vertex u : g) {
      if (get(u).vertexState == VertexState.NEW) {
        doDFSVisit(u, topologicalOrderList);
      }
    }
    return topologicalOrderList;
  }

  /**
   * @param u Starts DFS visit of vertices starting from the vertex u. Updates the
   *     topologicalOrderList with the visited vertices
   */
  private void doDFSVisit(Graph.Vertex u, LinkedList<Graph.Vertex> topologicalOrderList) {
    get(u).vertexState = VertexState.ACTIVE;
    for (Graph.Edge e : g.incident(u)) {
      Graph.Vertex v = e.otherEnd(u);
      if (get(v).vertexState == VertexState.NEW) {
        get(v).parent = u;
        get(v).cno = get(u).cno;
        doDFSVisit(v, topologicalOrderList);
      } else {
        if (get(v).vertexState == VertexState.ACTIVE) {
          isCyclic = true;
        }
      }
    }
    get(u).vertexState = VertexState.COMPLETE;
    topologicalOrderList.addFirst(u);
  }

  // Member function to find topological order
  public List<Graph.Vertex> topologicalOrder1() {
    LinkedList<Graph.Vertex> topologicalOrderList = depthFirstSearch();
    if (isCyclic) {
      return null;
    }
    return topologicalOrderList;
  }

  // Find the number of connected components of the graph g by running dfs.
  // Enter the component number of each vertex u in u.cno.
  // Note that the graph g is available as a class field via GraphAlgorithm.
  public int connectedComponents() {
    return numberOfSCC;
  }

  // After running the connected components algorithm, the component no of each vertex can be
  // queried.
  public int cno(Graph.Vertex u) {
    return get(u).cno;
  }

  /**
   * Finds strongly connected components of the graph g
   * @param g
   * @return
   */
  public DFS stronglyConnectedComponents(Graph g) {
    Stack<Integer> stack = new Stack<>();
    boolean visited[] = new boolean[g.n+1];
    DFS d = new DFS(g);

    for(int i = 0; i < visited.length; i++)
      visited[i] = false;

    for (int i = 1; i <= g.size(); i++)
      if (visited[i] == false)
        DFSUtil(g.getVertex(i), visited, stack);

      g.reverseGraph();

    for(int i = 0; i < visited.length; i++)
      visited[i] = false;

    while (stack.empty() == false)
    {
      List<Integer> set = new ArrayList<>();
      // Pop a vertex from stack
      int v = (int)stack.pop();

      // Print Strongly connected component of the popped vertex
      if (visited[v] == false)
      {
        DFSUtilForReverseGraph(g.getVertex(v), visited,set);

        if(set.size()>0){
          sccSet.add(set);
          numberOfSCC++;
        }

        System.out.println();
      }
    }
    d.setSccSet(sccSet);
    d.setNumberOfSCC(numberOfSCC);
    g.reverseGraph();
    return d;
  }

  /**
   * Runs dfs starting from vertex v and adds the visited vertex to resultSet which is a strongly connected component
   * @param u
   * @param visited
   * @param resultSet
   */
  private void DFSUtilForReverseGraph(Vertex u, boolean[] visited, List<Integer> resultSet) {

    visited[u.getName()] = true;
    resultSet.add(u.getName());
    Vertex node;
    Iterator<Graph.Edge> itr = g.outEdges(u).iterator();
    while (itr.hasNext())
    {
      node = itr.next().from;
      if (!visited[node.name])
        DFSUtilForReverseGraph(node,visited,resultSet);
    }

  }

  /**
   * Runs dfs starting from vertex v and adds the visited vertex to stack
   * @param v
   * @param visited
   * @param stack
   */
  private void DFSUtil(Vertex v, boolean[] visited, Stack<Integer> stack) {
    visited[v.getName()] = true;
    Iterator<Graph.Edge> itr = g.outEdges(v).iterator();
    while (itr.hasNext()){
      Vertex node = itr.next().to;
      if(!visited[node.name]){
        DFSUtil(node,visited,stack);
      }
    }
    stack.push(v.name);
  }

  // Find topological oder of a DAG using DFS. Returns null if g is not a DAG.
  public static List<Graph.Vertex> topologicalOrder1(Graph g) {
    DFS d = new DFS(g);
    return d.topologicalOrder1();
  }

  // Find topological oder of a DAG using the second algorithm. Returns null if g is not a DAG.
  public static List<Graph.Vertex> topologicalOrder2(Graph g) {
    return null;
  }

  public static void main(String[] args) throws Exception {
    String string = "3 4   1 2 2   2 3 3   3 1 5   1 3 1 0";

    Scanner in;
    // If there is a command line argument, use it as file from which
    // input is read, otherwise use input from string.
    in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(string);

    // Read graph from input
    Graph g = Graph.readDirectedGraph(in);
      g.printGraph(false);
    DFS d = new DFS(g);
    d = d.stronglyConnectedComponents(g);
    System.out.println("Number of strongly connected components: "+d.connectedComponents());
    System.out.println("Components are:");
    d.sccSet.forEach(
        set -> {
          System.out.print(set);
          //System.out.println();
        });
  }
}
