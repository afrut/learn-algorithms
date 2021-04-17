package mylibs;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;
import mylibs.Digraph;

public class DirectedCycle
{
	private Digraph digraph;
	private boolean[] marked;
	private boolean trace;
	private LinkedList<Integer>[] queues;
	private LinkedList<Integer> stack;
	private StringBuilder indent;
	private int[] edgeTo;
	private boolean[] onStack;
	private LinkedList<LinkedList<Integer>> cycles;

	public DirectedCycle(Digraph digraph) {
		this(digraph, false);
	}

	public DirectedCycle(Digraph digraph, boolean trace) {
		this.digraph = digraph;
		this.trace = trace;
		indent = new StringBuilder();
		marked = new boolean[digraph.V()];
		stack = new LinkedList<Integer>();
		edgeTo = new int[digraph.V()];
		onStack = new boolean[digraph.V()];
		cycles = new LinkedList<LinkedList<Integer>>();
		queues = (LinkedList<Integer>[]) new LinkedList[digraph.V()];
		for (int v = 0; v < digraph.V(); v++)
			queues[v] = new LinkedList<Integer>();

		for (int v = 0; v < digraph.V(); v++)
			if (!marked[v]) {
				edgeTo[v] = v;
				dfs(v);
			}
	}

	private void dfs(int v) {
		while (true) {
			if (!marked[v]) {
				if (trace)
					System.out.println(indent + "marking " + v);
				marked[v] = true;
				for (Integer w : digraph.adj(v)) {
					if (!marked[w]) {
						if (trace)
							System.out.println(indent + "|  adding " + w);
						queues[v].add(w);
					} else if (onStack[w]) {
						// cycle detected
						LinkedList<Integer> cycle = new LinkedList<Integer>();
						cycle.push(w);
						cycle.push(v);
						int x = v;
						while (true) {
							x = edgeTo[x];
							cycle.push(x);
							if (x == w)
								break;
						}
						cycles.add(cycle);
						if (trace)
							System.out.println(indent + "|  cycle detected: " + cycle.toString());
					}
				}
			}

			if (!queues[v].isEmpty()) {
				stack.push(v);
				onStack[v] = true;
				edgeTo[queues[v].peek()] = v;
				v = queues[v].poll();
				indent.append("|  ");
				if (trace)
					System.out.println(indent + "checking " + v);
			} else if (!stack.isEmpty()) {
				if (trace)
					System.out.println(indent + "done " + v);
				v = stack.pop();
				onStack[v] = false;
				indent.setLength(indent.length() - 3);
			} else {
				break;
			}
		}
	}

	public boolean hasCycle() {
		return cycles.size() > 0;
	}

	public Iterable<Integer> cycle() {
		return cycles.peek();
	}

	public static void main(String[] args) throws FileNotFoundException {
		boolean test = false;
		String filename = "";
		String delim = " ";
		for (int cnt = 0; cnt < args.length; cnt++) {
			if (args[cnt].compareTo("-test") == 0)
				test = true;
			else {
				filename = args[cnt++];
				delim = args[cnt];
			}
		}

		Digraph digraph = new Digraph(filename, delim);
		if (test) {
			DirectedCycle dc = new DirectedCycle(digraph, false);
			assert (dc.hasCycle()) : "Digraph has a cycle [5, 4, 3, 5]";
			Iterable<Integer> cycle = dc.cycle();
			int v = -1;
			int first = -1;
			int w = -1;
			Iterator<Integer> it = cycle.iterator();

			while (it.hasNext()) {
				w = it.next();
				if (v == -1) {
					v = w;
					first = w;
				} else
					assert (digraph.hasEdge(v, w)) : "Digraph should have edge " + v + "->" + w;
				v = w;
			}
			assert (first == w) : "Cycle " + cycle.toString() + " should have the same first and last points: (" + first
					+ ", " + w + ")";

			System.out.println("PASS");

		} else {
			DirectedCycle dc = new DirectedCycle(digraph, true);
			System.out.println("has cycle? " + dc.hasCycle());
			System.out.println("sample cycle: " + dc.cycle().toString());
		}
	}
}
