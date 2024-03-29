package mylibs;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;
import mylibs.Queue;
import mylibs.Stack;

public class DirectedCycleDFS
{
	private Digraph digraph;
	private boolean[] marked;
	private boolean trace;
	private Queue<Integer>[] queues;
	private Stack<Integer> stack;
	private StringBuilder indent;
	private int[] edgeTo;
	private boolean[] onStack;
	private Queue<Stack<Integer>> cycles;

	public DirectedCycleDFS(Digraph digraph) {this(digraph, false);}

	public DirectedCycleDFS(Digraph digraph, boolean trace)
	{
		this.digraph = digraph;
		this.trace = trace;
		indent = new StringBuilder();
		marked = new boolean[digraph.V()];
		stack = new Stack<Integer>();
		edgeTo = new int[digraph.V()];
		onStack = new boolean[digraph.V()];
		cycles = new Queue<Stack<Integer>>();
		queues = (Queue<Integer>[]) new Queue[digraph.V()];
		for (int v = 0; v < digraph.V(); v++)
			queues[v] = new Queue<Integer>();

		for (int v = 0; v < digraph.V(); v++)
			if (!marked[v])
			{
				edgeTo[v] = v;
				dfs(v);
			}
	}

	private void dfs(int v)
	{
		while (true)
		{
			if (!marked[v])
			{
				if (trace) System.out.println(indent + "marking " + v);
				marked[v] = true;
				for (Integer w : digraph.adj(v))
				{
					if (!marked[w])
					{
						if (trace)
							System.out.println(indent + "|  adding " + w);
						queues[v].enqueue(w);
					}
					else if (onStack[w])
					{
						// cycle detected
						Stack<Integer> cycle = new Stack<Integer>();
						cycle.push(w);
						cycle.push(v);
						int x = v;
						while (true) {
							x = edgeTo[x];
							cycle.push(x);
							if (x == w)
								break;
						}
						cycles.enqueue(cycle);
						if (trace)
							System.out.println(indent + "|  cycle detected: " + cycle.toString());
					}
				}
			}

			if (!queues[v].isEmpty())
			{
				stack.push(v);
				onStack[v] = true;
				int temp = v;
				v = queues[v].dequeue();
				edgeTo[v] = temp;
				indent.append("|  ");
				if (trace) System.out.println(indent + "checking " + v);
			}
			else if (!stack.isEmpty())
			{
				if (trace) System.out.println(indent + "done " + v);
				v = stack.pop();
				onStack[v] = false;
				indent.setLength(indent.length() - 3);
			}
			else break;
		}
	}

	public boolean hasCycle() {return cycles.size() > 0;}

	public Iterable<Integer> cycle()
	{
		Iterable<Integer> ret = null;
		for(Stack<Integer> cycle : cycles)
		{
			ret = cycle;
			break;
		}
		return ret;
	}

	public static void main(String[] args) throws FileNotFoundException
	{
		boolean test = false;
		String filename = "";
		String delim = " ";
		for (int cnt = 0; cnt < args.length; cnt++)
		{
			if (args[cnt].compareTo("-test") == 0) test = true;
			else
			{
				filename = args[cnt++];
				delim = args[cnt];
			}
		}

		Digraph digraph = new Digraph(filename, delim);
		if (test)
		{
			DirectedCycleDFS dc = new DirectedCycleDFS(digraph, false);
			assert (dc.hasCycle()) : "Digraph has a cycle [5, 4, 3, 5]";
			Iterable<Integer> cycle = dc.cycle();
			int v = -1;
			int first = -1;
			int w = -1;
			Iterator<Integer> it = cycle.iterator();

			while (it.hasNext())
			{
				w = it.next();
				if (v == -1)
				{
					v = w;
					first = w;
				}
				else
					assert (digraph.hasEdge(v, w)) : "Digraph should have edge " + v + "->" + w;
				v = w;
			}
			assert (first == w) : "Cycle " + cycle.toString() + " should have the same first and last points: (" + first + ", " + w + ")";

			System.out.println("PASS");

		}
		else
		{
			DirectedCycleDFS dc = new DirectedCycleDFS(digraph, true);
			System.out.println("has cycle? " + dc.hasCycle());
			System.out.println("sample cycle: " + dc.cycle().toString());
		}
	}
}
