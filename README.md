# Network Flows Optimization

Shortest Path Problem Algorithms:

1 Dynamic (no cycle - topologically ordered node list)

2 Dijkstra 

3 Dial

4 Heap Dijkstra

5 RadixHeap Dijkstra

6 Label correcting 

7 Modified label correcting (FIFO)

8 Dequeue label correcting


Maximum Flow Problem Algorithms:

1 Labeling

2 Preflow Push (Heap implementation)


Minimum Cost Flow Problem Algorithms

1 Successive Shortest Paths / Primal - Dual Algorithm

2 Cycle canceling

_______________________________

Options to test the program:

Select problem, then select graph from list (example graph - random graph - load graph).

To load a graph you need a text file with this structure:

Adjacency matrix dimension (ex. 5)

Mass balance excess of nodes (ex. 25  0  0  0  -25)
Adjacency Matrix (ex.
 0  1  1  0  0
 0  0  1  1  0
 0  0  0  1  1
 0  0  0  0  1
 0  0  0  0  0
)

Cost Matrix (ex.
 0  7  6  0  0
 0  0  6  4  0
 0  0  0  2  2
 0  0  0  0  1
 0  0  0  0  0
 )
 
Capacity Matrix (ex.
 0 30 20  0  0
 0  0 25 10  0
 0  0  0 20 25
 0  0  0  0 20
 0  0  0  0  0
 )
