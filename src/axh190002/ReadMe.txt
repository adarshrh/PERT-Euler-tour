/**
 * IDSA Long Project 2
 * Group members:
 * Adarsh Raghupati   axh190002
 * Akash Akki         apa190001
 * Keerti Keerti      kxk190012
 * Stewart cannon     sjc160330
 */

####Implementation of Strongly connected components, Euler tour, PERT

###### Steps to run the code in IntelliJ IDE
* Create an empty java project
* Unzip the source code files and paste it under the location "Java Project Name"/src folder


###### Steps to run the tests
NOTE: If you are running the programs in intelij/eclipse add the following VM arguments:
-Xss100m

If you are running the programs by command line please give the following JVM arguments:
java -Xss100m <Java class File name>


Run DFS.java to test Strongly connected components
Run Euler.java to test Euler tour
Run PERT.java to test critical path and critical vertices

###### Sample test runs:
Run DFS.java with lp2-scc.txt file as input

Output:
Number of strongly connected components: 5
Components are:
[5][9, 13, 10, 14][1, 7, 6, 2][3, 4, 8, 11, 15, 12][16]

Run Euler.java with lp2-in1.txt file as input
Output:
1,2,3,4,5,7,8,4,7,9,5,6,3,1,

Run PERT.java with lp2-pert5.txt file as input
Output: 15 9
u	Dur	EC	LC	Slack	Critical
1	0	0	0	0	true
2	2	2	2	0	true
3	3	5	5	0	true
4	5	10	10	0	true
5	5	5	5	0	true
6	5	10	10	0	true
7	5	15	15	0	true
8	5	15	15	0	true
9	0	15	15	0	true
