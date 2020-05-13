# CS 6301 Implementation of Data Structures and Algorithms
# Long Project 2: SCC, Euler tours, and PERT

## Project Description
- Implement the algorithm to find strongly connected components of a directed graph.
- Implement one of the algorithms discussed in the class to find an Euler tour of a given directed
  graph.
- Implement the methods in PERT.java (Program Evaluation and Review Technique)  
`public static PERT pert(Graph g, int[] duration);// Run PERT algorithm on graph g.`  
`// Assume that vertex 1 is s and vertex n is t.`    
`// Add edges from s to all vertices and from all vertices to t.`  
`boolean pert();//called after calling the constructor`  
  * public int ec(Vertex u); // Earliest completion time of u  
  * public int lc(Vertex u); // Latest completion time of u
  * public int slack(Vertex u); // Slack of u
  * public int criticalPath(); // Length of critical path
  * public boolean critical(Vertex u); // Is vertex u on a critical path?
  * public int numCritical(); // Number of critical nodes in graph
  