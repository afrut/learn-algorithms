package mylibs.ds;
import java.util.Iterator;
import edu.princeton.cs.algs4.StdIn;
import mylibs.ds.Bag;

public class BST<Key extends Comparable<Key>, Value>
{
    private class Node
    {
        Key key;
        Value value;
        Node left;
        Node right;
        int N;
    }

    private class KeysIterable implements Iterable<Key>
    {
        private Key from;
        private Key to;

        private class KeysIterator implements Iterator<Key>
        {
            private int idx;
            private int N;
            private Key[] keys;
            private Key key1;
            private Key key2;
            public KeysIterator(Node node, Key key1, Key key2)
            {
                this.key1 = key1;
                this.key2 = key2;

                if(node == null) this.N = 0;
                else
                {
                    this.N = node.N;
                    keys = (Key[]) new Comparable[this.N];
                    
                    // populate array of keys
                    idx = 0;
                    f(node);
                }
                // reset index of array for user
                idx = 0;
            }
            public boolean hasNext() {return idx < N && keys[idx] != null;}
            public Key next() {return keys[idx++];}
            public void remove() {}
            private void f(Node node)
            {
                if(node == null) return;
                f(node.left);

                // check if this key is to be added based on key limits
                if(this.key1 == null)
                {
                    if(this.key2 == null) keys[idx++] = node.key;
                    else if(this.key2.compareTo(node.key) >= 0) keys[idx++] = node.key;
                }
                else
                {
                    if(this.key2 == null)
                    {
                        if(this.key1.compareTo(node.key) <= 0) {keys[idx++] = node.key;}
                    }
                    else
                    {
                        if(this.key1.compareTo(node.key) <= 0 && this.key2.compareTo(node.key) >= 0)
                        {
                            keys[idx++] = node.key;
                        }
                    }
                }
                f(node.right);
                return;
            }
        }

        public KeysIterable(Key lo, Key hi)
        {
            this.from = lo;
            this.to = hi;
        }

        public KeysIterator iterator()
        {
            return new KeysIterator(root, this.from, this.to);
        }
    }

    private Node root;

    public BST() {root = null;}

    // Searches for key and replaces its value. If not found, inserts key and value.
    public void put(Key key, Value value)
    {
        if(root == null)
        {
            root = new Node();
            root.key = key;
            root.value = value;
            root.N = 1;
        }
        else
        {
            Node node = root;
            Bag<Node> bag = new Bag<Node>();
            boolean contained = false;

            while(true)
            {
                if(key.compareTo(node.key) < 0)
                {
                    bag.add(node);
                    if(node.left == null)
                    {
                        Node newNode = new Node();
                        newNode.key = key;
                        newNode.value = value;
                        newNode.N = 1;
                        node.left = newNode;
                        break;
                    }
                    else
                    {
                        node = node.left;
                    }
                }
                else if(key.compareTo(node.key) > 0)
                {
                    bag.add(node);
                    if(node.right == null)
                    {
                        Node newNode = new Node();
                        newNode.key = key;
                        newNode.value = value;
                        newNode.N = 1;
                        node.right = newNode;
                        break;
                    }
                    else
                    {
                        node = node.right;
                    }
                }
                else
                {
                    node.key = key;
                    node.value = value;
                    contained = true;
                    break;
                }
            }

            if(!contained)
            {
                for(Node n : bag)
                    n.N++;
            }
        }
    }

    // Return the value associated with key.
    public Value get(Key key)
    {
        Node node = root;
        while(true)
        {
            if(node == null) return null;
            else if(key.compareTo(node.key) < 0) node = node.left;
            else if(key.compareTo(node.key) > 0) node = node.right;
            else return node.value;
        }
    }

    // Remove the node that is associated with key.
    public void delete(Key key)
    {
        Node node = root;
        Node prev = root;
        boolean leftLink = true;
        Bag<Node> bag = new Bag<Node>();
        while(true)
        {
            if(node == null) return;
            else if(key.compareTo(node.key) < 0)
            {
                prev = node;
                bag.add(node);
                node = node.left;
                leftLink = true;
            }
            else if(key.compareTo(node.key) > 0)
            {
                prev = node;
                bag.add(node);
                node = node.right;
                leftLink = false;
            }
            else break;
        }
        // node now contains the node where node.key == key

        // Determine successor.
        Node successor = ceiling(node.right, key);
        if(successor == null) successor = node.left;
        else
        {
            // Modify tree to remove node.
            successor.left = node.left;
            successor.right = deleteMin(node.right);
        }

        // Link successor to parent.
        node.left = null;
        node.right = null;
        if(leftLink) prev.left = successor;
        else prev.right = successor;

        // Update Node.N for all relevant nodes in the tree path to the deleted node.
        for(Node n : bag)
            n.N--;
    }

    // Checks if the binary search tree contains a node associated with key.
    public boolean contains(Key key)
    {
        Node node = root;
        while(true)
        {
            if(node == null) return false;
            else if(key.compareTo(node.key) < 0) node = node.left;
            else if(key.compareTo(node.key) > 0) node = node.right;
            else return true;
        }
    }

    // Check if the binary search tree is null.
    public boolean isEmpty() {return root == null;}

    // Return the total number of nodes.
    public int size()
    {
        if(root == null) return 0;
        else return root.N;
    }

    // Return the smallest Key.
    public Key min()
    {
        Node node = root;
        while(true)
        {
            if(node == null) return null;
            else if(node.left == null) return node.key;
            else node = node.left;
        }
    }

    // Return the largest key.
    public Key max()
    {
        Node node = root;
        while(true)
        {
            if(node == null) return null;
            else if(node.right == null) return node.key;
            else node = node.right;
        }
    }

    // Return the largest key that is less than key.
    public Key floor(Key key)
    {
        Node node = root;
        Node ret = null;
        while(true)
        {
            if(node == null) break;
            else if(key.compareTo(node.key) < 0) node = node.left;
            else if(key.compareTo(node.key) > 0)
            {
                ret = node;
                node = node.right;
            }
            else
            {
                ret = node.left;
                break;
            }
        }
        if(ret == null) return null;
        else return ret.key;
    }

    // Return the smallest key that is greater than key.
    public Key ceiling(Key key)
    {
        Node node = root;
        Node ret = null;
        while(true)
        {
            if(node == null) break;
            else if(key.compareTo(node.key) < 0)
            {
                ret = node;
                node = node.left;
            }
            else if(key.compareTo(node.key) > 0) node = node.right;
            else
            {
                ret = node.right;
                break;
            }
        }
        if(ret == null) return null;
        else return ret.key;
    }

    // Return the node containing the smallest key greater than node.key within
    // the binary search tree rooted at node.
    private Node ceiling(Node node, Key key)
    {
        Node currentNode = node;
        Node ret = null;
        while(true)
        {
            if(currentNode == null) break;
            else if(key.compareTo(currentNode.key) < 0)
            {
                ret = node;
                node = node.left;
            }
            else if(key.compareTo(currentNode.key) > 0) {node = node.right;}
            else
            {
                ret = node.right;
                break;
            }
        }
        return ret;
    }

    // Return the number of keys that are less than key.
    // Keys have to be unique.
    public int rank(Key key)
    {
        Node node = root;
        int ret = 0;
        while(true)
        {
            if(node == null) return ret;
            else if(key.compareTo(node.key) < 0)
            {
                ret += 1;
                if(node.left != null) ret += node.left.N;
                node = node.right;
            }
            else if(key.compareTo(node.key) > 0) {node = node.left;}
            else
            {
                if(node.left != null) ret += node.left.N;
                return ret;
            }
        }
    }

    // Return the key that has k keys less than it.
    public Key select(int k)
    {
        Node node = select(root, k);
        if(node == null) return null;
        else return node.key;
    }

    // Return the Node that has k keys less than it within the binary search tree
    // rooted at node.
    private Node select(Node node, int k)
    {
        if(k < 0) return null;
        else if(node == null) return null;
        int cntleft = 0, cntright = 0;
        if(node.left != null) cntleft = node.left.N;
        if(node.right != null) cntright = node.right.N;
        if(node.left != null)
        {
            if(node.left.N > k)
            {
                return select(node.left, k);
            }
            else if(node.left.N < k)
            {
                return select(node.right, k - node.left.N - 1);
            }
            else
            {
                return node;
            }
        }
        else if(k == 0) return node;
        else if(node.right != null)
        {
            if(node.right.N > k)
            {
                return select(node.right, k - 1);
            }
            else return null;
        }
        else return null;
    }

    // Remove the node with the smallest key.
    public void deleteMin() {root = deleteMin(root);}

    // Remove the node with the smallest key within the subtree rooted at node.
    private Node deleteMin(Node node)
    {
        if(node == null) return null;
        if(node.left == null)
        {
            Node ret = node.right;
            node.right = null;
            return ret;
        }
        else
        {
            node.left = deleteMin(node.left);
            updateNodeN(node);
            return node;
        }
    }

    // Remove the pair with the greatest key.
    public void deleteMax() {root = deleteMax(root);}

    // Remove the node with the largest key within the subtree rooted at node.
    private Node deleteMax(Node node)
    {
        if(node == null) return null;
        if(node.right == null)
        {
            Node ret = node.left;
            node.left = null;
            return ret;
        }
        else
        {
            node.right = deleteMax(node.right);
            updateNodeN(node);
            return node;
        }
    }

    // Helper function to update the count of a node.
    private void updateNodeN(Node node)
    {
        if(node == null) return;
        int ret = 0;
        if(node.left != null) ret += node.left.N;
        if(node.right != null) ret += node.right.N;
        node.N = ret + 1;
    }

    public int size(Key from, Key to)
    {
        return size(root, from, to);
    }

    private int size(Node node, Key from, Key to)
    {
        if(node == null) return 0;
        if(from.compareTo(node.key) > 0)
        {
            return size(node.right, from, to);
        }
        else if(from.compareTo(node.key) == 0)
        {
            return 1 + size(node.right, from, to);
        }
        else if(from.compareTo(node.key) < 0 && to.compareTo(node.key) > 0)
        {
            return 1 + size(node.left, from, to) + size(node.right, from, to);
        }
        else if(to.compareTo(node.key) == 0)
        {
            return 1 + size(node.left, from, to);
        }
        else
        {
            return size(node.left, from, to);
        }
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        if(root != null)
        {
            toString(root, sb);
            if(sb.length() > 0)
                sb.setLength(sb.length() - 2);
        }
        return sb.toString();
    }

    private String toString(Node node)
    {
        StringBuilder sb = new StringBuilder();
        toString(node, sb);
        return sb.toString();
    }

    private void toString(Node node, StringBuilder sb)
    {
        if(node == null) return;
        if(node.left != null) toString(node.left, sb);
        sb.append("(" + node.key + ", " + node.value + "), ");
        if(node.right != null) toString(node.right, sb);
    }

    public Iterable<Key> keys() {return new KeysIterable(null, null);}
    public Iterable<Key> keys(Key lo, Key hi) {return new KeysIterable(lo ,hi);}

    public static void main(String[] args)
    {
        boolean test = false;
        if(args.length > 0)
        {
            for(String arg : args) {if(arg.equals("-test")) test = true;}
        }

        if(test)
        {
            BST<String, Integer> st =
                new BST <String, Integer>();
            System.out.println("Testing all operations on empty symbol table:");
            System.out.println("    Contents: " + st.toString());
            System.out.println("    isEmpty(): " + st.isEmpty());
            System.out.println("    size(): " + st.size());
            System.out.println("    size(C, G): " + st.size("C", "G"));
            System.out.println("    contains(E): " + st.contains("E"));
            System.out.println("    get(E): " + st.get("E"));
            System.out.println("    min(): " + st.min());
            System.out.println("    max(): " + st.max());
            System.out.println("    floor(E): " + st.floor("E"));
            System.out.println("    ceiling(E): " + st.ceiling("E"));
            System.out.println("    rank(E): " + st.rank("E"));
            System.out.println("    select(5): " + st.select(5));
            st.delete("E"); System.out.println("    delete(E): " + st.toString());
            st.deleteMin(); System.out.println("    deleteMin(): " + st.toString());
            st.deleteMax(); System.out.println("    deleteMax(): " + st.toString());
            System.out.println("    keys():");
            for(String str : st.keys()) System.out.println("        " + str);
            System.out.println("    keys(C, P):");
            for(String str : st.keys("C", "P")) System.out.println("        " + str);
            System.out.println("    keys(D, P):");
            for(String str : st.keys("D", "P")) System.out.println("        " + str);
            System.out.println("    keys(C, Q):");
            for(String str : st.keys("C", "Q")) System.out.println("        " + str);
            System.out.println("    keys(D, Q):");
            for(String str : st.keys("D", "Q")) System.out.println("        " +
            str);
            System.out.println("");

            System.out.println("Testing all operations with 1 element:");
            st.put("G", 3); System.out.println("    put(G, 3): " + st.toString());
            System.out.println("    isEmpty(): " + st.isEmpty());
            System.out.println("    size(): " + st.size());
            System.out.println("    size(B, G): " + st.size("B", "G"));
            System.out.println("    size(B, X): " + st.size("B", "X"));
            System.out.println("    size(C, D): " + st.size("C", "D"));
            System.out.println("    size(W, Z): " + st.size("W", "Z"));
            System.out.println("    contains(G): " + st.contains("G"));
            System.out.println("    contains(W): " + st.contains("W"));
            System.out.println("    get(G): " + st.get("G"));
            System.out.println("    get(W): " + st.get("W"));
            System.out.println("    min(): " + st.min());
            System.out.println("    max(): " + st.max());
            System.out.println("    floor(A): " + st.floor("A"));
            System.out.println("    floor(G): " + st.floor("G"));
            System.out.println("    floor(W): " + st.floor("w"));
            System.out.println("    ceiling(A): " + st.ceiling("A"));
            System.out.println("    ceiling(G): " + st.ceiling("G"));
            System.out.println("    ceiling(W): " + st.ceiling("w"));
            System.out.println("    rank(G): " + st.rank("G"));
            System.out.println("    rank(W): " + st.rank("w"));
            System.out.println("    select(0): " + st.select(0));
            System.out.println("    select(3): " + st.select(3));
            System.out.println("    keys():");
            for(String str : st.keys()) System.out.println("        " + str);
            System.out.println("    keys(C, P):");
            for(String str : st.keys("C", "P")) System.out.println("        " + str);
            System.out.println("    keys(D, P):");
            for(String str : st.keys("D", "P")) System.out.println("        " + str);
            System.out.println("    keys(C, Q):");
            for(String str : st.keys("C", "Q")) System.out.println("        " + str);
            System.out.println("    keys(D, Q):");
            for(String str : st.keys("D", "Q")) System.out.println("        " + str);
            st.put("A", 3); System.out.println("    put(A, 3): " + st.toString());
            st.delete("A"); System.out.println("    delete(A): " + st.toString());
            st.put("B", 2); System.out.println("    put(B, 2): " + st.toString());
            st.put("C", 7); System.out.println("    put(C, 7): " + st.toString());
            st.deleteMin(); System.out.println("    deleteMin(): " + st.toString());
            st.deleteMax(); System.out.println("    deleteMax(): " + st.toString());
            st.deleteMin(); System.out.println("    deleteMin(): " + st.toString());
            st.put("Z", 23); System.out.println("    put(Z, 23): " + st.toString());
            st.deleteMax(); System.out.println("    deleteMax(): " + st.toString());
            System.out.println("");

            System.out.println("Populating symbol table:");
            st.put("B", 3); System.out.println("    put(B, 1), size() = " + st.size() + ":  " + st.toString());
            st.put("W", 3); System.out.println("    put(W, 2), size() = " + st.size() + ":  " + st.toString());
            st.put("O", 3); System.out.println("    put(O, 3), size() = " + st.size() + ":  " + st.toString());
            st.put("P", 4); System.out.println("    put(P, 4), size() = " + st.size() + ":  " + st.toString());
            st.put("F", 5); System.out.println("    put(F, 5), size() = " + st.size() + ":  " + st.toString());
            st.put("R", 6); System.out.println("    put(R, 6), size() = " + st.size() + ":  " + st.toString());
            st.put("C", 7); System.out.println("    put(C, 7), size() = " + st.size() + ":  " + st.toString());
            System.out.println("");

            System.out.println("Testing iterators:");
            System.out.println("    Contents: " + st.toString());
            System.out.println("    keys():");
            for(String str : st.keys()) System.out.println("        " + str);
            System.out.println("    keys(C, P):");
            for(String str : st.keys("C", "P")) System.out.println("        " + str);
            System.out.println("    keys(D, P):");
            for(String str : st.keys("D", "P")) System.out.println("        " + str);
            System.out.println("    keys(C, Q):");
            for(String str : st.keys("C", "Q")) System.out.println("        " + str);
            System.out.println("    keys(D, Q):");
            for(String str : st.keys("D", "Q")) System.out.println("        " + str);
            System.out.println("");
            
            System.out.println("Testing with multiple elements:");
            System.out.println("    Contents: " + st.toString());
            System.out.println("    isEmpty(): " + st.isEmpty());
            System.out.println("    size(): " + st.size());
            System.out.println("    size(C, P): " + st.size("C", "P"));
            System.out.println("    size(D, P): " + st.size("D", "P"));
            System.out.println("    size(D, Q): " + st.size("D", "Q"));
            System.out.println("    size(A, C): " + st.size("A", "C"));
            System.out.println("    contains(C): " + st.contains("C"));
            System.out.println("    contains(D): " + st.contains("D"));
            System.out.println("    get(C): " + st.get("C"));
            System.out.println("    get(D): " + st.get("D"));
            System.out.println("    min(): " + st.min());
            System.out.println("    max(): " + st.max());
            System.out.println("    floor(C): " + st.floor("C"));
            System.out.println("    floor(E): " + st.floor("E"));
            System.out.println("    ceiling(C): " + st.ceiling("C"));
            System.out.println("    ceiling(E): " + st.ceiling("E"));
            System.out.println("    rank(A): " + st.rank("A"));
            System.out.println("    rank(F): " + st.rank("F"));
            System.out.println("    rank(W): " + st.rank("W"));
            System.out.println("    rank(Z): " + st.rank("Z"));
            System.out.println("    select(4): " + st.select(4));
            System.out.println("    select(20): " + st.select(20));
            System.out.println("    select(-1): " + st.select(-1));
            st.delete("B"); System.out.println("    delete(B), size() = " + st.size() + ", " + st.toString());
            st.delete("W"); System.out.println("    delete(W), size() = " + st.size() + ", " + st.toString());
            st.delete("G"); System.out.println("    delete(G), size() = " + st.size() + ", " + st.toString());
            st.deleteMin(); System.out.println("    deleteMin(), size() = " + st.size() + ", " + st.toString());
            st.deleteMax(); System.out.println("    deleteMax(), size() = " + st.size() + ", " + st.toString());
            st.deleteMin(); System.out.println("    deleteMin(), size() = " + st.size() + ", " + st.toString());
            st.deleteMax(); System.out.println("    deleteMax(), size() = " + st.size() + ", " + st.toString());
            st.deleteMin(); System.out.println("    deleteMin(), size() = " + st.size() + ", " + st.toString());
            st.delete("X"); System.out.println("    delete(X), size() = " + st.size() + ", " + st.toString());
            st.deleteMin(); System.out.println("    deleteMin(), size() = " + st.size() + ", " + st.toString());
            st.deleteMax(); System.out.println("    deleteMax(), size() = " + st.size() + ", " + st.toString());
        }
        else
        {
            /*
            BST<String, Integer> st =
                new BST<String, Integer>();
            // sample input is SEARCHEXAMPLE
            System.out.println("Symbol table empty? " + st.isEmpty());
            System.out.println("Testing put() operation:");
            int cnt = 0;
            while(!StdIn.isEmpty())
            {
                String key = StdIn.readString();
                st.put(key, cnt);
                cnt++;
            }
            System.out.println("    Contents" + st.toString());
            System.out.println("Symbol table empty? " + st.isEmpty());
            System.out.println("");

            System.out.println("Testing get() operation:");
            System.out.println("    Key X has value " + st.get("X"));
            System.out.println("    Key Z has value " + st.get("Z"));
            System.out.println("");

            System.out.println("Testing delete() operation");
            int sz = st.size();
            st.delete("X");
            st.delete("M");
            System.out.println(st.toString());
            System.out.println("    Number of elements decreased by: " + (sz - st.size()));
            System.out.println("");

            System.out.println("Testing contains() operation");
            System.out.println("    Contains X? " + st.contains("X"));
            System.out.println("    Contains R? " + st.contains("R"));
            System.out.println("");

            System.out.println("Testing keys iterator:");
            for(String str : st.keys())
            {
                System.out.println("    " + str);
            }
            System.out.println("");
            }
            */
        }
    }
}

