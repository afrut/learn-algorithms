package libs.algs;
import java.util.Iterator;
import edu.princeton.cs.algs4.StdIn;

public class RecursiveBST<Key extends Comparable<Key>, Value>
{
    private class Node
    {
        Key key;
        Value value;
        Node left;
        Node right;
        int N;
        int H;
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

    public RecursiveBST() {root = null;}

    // Searches for key and replaces its value. If not found, inserts key and value.
    public void put(Key key, Value value) {root = put(root, key, value);}

    // Private recursive function to insert a key-value pair.
    private Node put(Node node, Key key, Value value)
    {
        if(node == null)
        {
            Node ret = new Node();
            ret.key = key;
            ret.value = value;
            ret.N = 1;
            ret.H = 1;
            return ret;
        }
        else if(key.compareTo(node.key) < 0)
        {
            Node ret = put(node.left, key, value);
            node.left = ret;
            updateNodeN(node);
            return node;
        }
        else if(key.compareTo(node.key) > 0)
        {
            Node ret = put(node.right, key, value);
            node.right = ret;
            updateNodeN(node);
            return node;
        }
        else
        {
            node.key = key;
            node.value = value;
            return node;
        }
    }

    private void updateNodeN(Node node)
    {
        if(node == null) return;
        int ret = 0;
        node.H = 1;
        if(node.left != null)
        {
            node.H += node.left.H;
            ret += node.left.N;
        }
        if(node.right != null)
        {
            int rightH = 1 + node.right.H;
            if(node.H < rightH) node.H = rightH;
            ret += node.right.N;
        }
        node.N = ret + 1;
    }

    // Return the value associated with key.
    public Value get(Key key)
    {
        Node node = get(root, key);
        if(node == null) return null;
        else return node.value;
    }

    // Return the Node associated with key within the binary search tree rooted
    // at node.
    private Node get(Node node, Key key)
    {
        if(node == null) return null;
        if(key.compareTo(node.key) < 0) return get(node.left, key);
        else if(key.compareTo(node.key) > 0) return get(node.right, key);
        else return node;
    }

    // Remove the node that is associated with key.
    public void delete(Key key) {root = delete(root, key);}

    // Delete the node associated with key within the binary search tree rooted
    // at node.
    private Node delete(Node node, Key key)
    {
        if(node == null) return null;
        else if(key.compareTo(node.key) < 0)
        {
            node.left = delete(node.left, key);
            updateNodeN(node);
            return node;
        }
        else if(key.compareTo(node.key) > 0)
        {
            node.right = delete(node.right, key);
            updateNodeN(node);
            return node;
        }
        else
        {
            // Node with key found. Store it in a temporary variable.
            // node is the Node to delete.
            // Find the ceiling of this node within its binary tree.
            Node successor = ceiling(node.right, node.key);
            if(successor != null)
            {
                // The successor should not have a node on its left.
                // If it did, it would not be the ceiling of node.
                // Set the successor's left to be the node left of node to delete.
                successor.right = deleteMin(node.right);
                successor.left = node.left;
                node.left = null;
                node.right = null;
            }
            else
            {
                successor = node.left;
            }

            // Update the counts of successor.
            updateNodeN(successor);

            // Return the successor so that it can be properly linked to the
            // node above node to delete.
            return successor;
        }
    }

    // Checks if the binary search tree contains a node associated with key.
    public boolean contains(Key key)
    {
        if(contains(root, key) != null) return true;
        else return false;
    }

    // Returns the Node associated with key that is within the binary search
    // tree rooted at node.
    private Node contains(Node node, Key key)
    {
        if(node == null) return null;
        else if(key.compareTo(node.key) < 0) return contains(node.left, key);
        else if(key.compareTo(node.key) > 0) return contains(node.right, key);
        else return node;
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
        Node ret = min(root);
        if(ret == null) return null;
        else return ret.key;
    }

    // Return the node containing the smallest key in the binary tree rooted at
    // node.
    private Node min(Node node)
    {
        if(node == null) return null;
        if(node.left != null) return min(node.left);
        else return node;
    }

    // Return the largest key.
    public Key max()
    {
        Node ret = max(root);
        if(ret == null) return null;
        else return ret.key;
    }

    // Return the Node that is associated with the largest key within the binary
    // search tree rooted at node.
    public Node max(Node node)
    {
        if(node == null) return null;
        if(node.right != null) return max(node.right);
        else return node;
    }

    // Return the largest key that is less than key.
    public Key floor(Key key)
    {
        Node node = floor(root, key);
        if(node == null) return null;
        else return node.key;
    }

    // Return the node associated with the largest key that is less than node.key
    // within the binary search tree rooted at node.
    private Node floor(Node node, Key key)
    {
        if(node == null) return null;
        if(key.compareTo(node.key) < 0)
        {
            Node ret = floor(node.left, key);
            if(ret == null) return null;
            else return ret;
        }
        else if(key.compareTo(node.key) > 0)
        {
            Node ret = floor(node.right, key);
            if(ret == null) return node;
            else return ret;
        }
        else
        {
            Node ret = floor(node.left, key);
            if(ret == null) return null;
            else return ret;
        }
    }

    // Return the smallest key that is greater than key.
    public Key ceiling(Key key)
    {
        Node node = ceiling(root, key);
        if(node == null) return null;
        else return node.key;
    }

    // Return the node containing the smallest key greater than node.key within
    // the binary search tree rooted at node.
    private Node ceiling(Node node, Key key)
    {
        if(node == null) return null;
        if(key.compareTo(node.key) < 0)
        {
            Node ret = ceiling(node.left, key);
            if(ret == null) return node;
            else return ret;
        }
        else if(key.compareTo(node.key) > 0)
        {
            Node ret = ceiling(node.right, key);
            if(ret == null) return null;
            else return ret;
        }
        else
        {
            Node ret = ceiling(node.right, key);
            if(ret == null) return null;
            else return ret;
        }
    }

    // Return the number of keys that are less than key.
    // Keys have to be unique.
    public int rank(Key key)
    {
        return rank(root, key);
    }

    // Returns the number of keys less than key within the binary search tree
    // at node.
    private int rank(Node node, Key key)
    {
        if(node == null) return 0;
        if(key.compareTo(node.key) < 0)
        {
            return rank(node.left, key);
        }
        else if(key.compareTo(node.key) > 0)
        {
            if(node.left == null) return 1 + rank(node.right, key);
            else return 1 + node.left.N + rank(node.right, key);
        }
        else
        {
            if(node.left == null) return 0;
            else return node.left.N;
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

    // Get the number of Keys between from and to, inclusive.
    public int size(Key from, Key to)
    {
        return size(root, from, to);
    }

    // Get the number of Keys between from and to, inclusive starting from Node.
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

    // Return the height of the binary search tree using Node.H.
    public int height()
    {
        if(root == null) return 0;
        else return root.H;
    }

    // Determine the height of the BST by examining every element.
    public int heightCompute() {return heightCompute(root);}

    // Determine the height of the BST by examining every element rooted at node.
    public int heightCompute(Node node)
    {
        if(node == null) return 0;
        else
        {
            int heightLeft = heightCompute(node.left);
            int heightRight = heightCompute(node.right);
            if(heightLeft > heightRight) return 1 + heightLeft;
            else return 1 + heightRight;
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

    private String toStringIterator()
    {
        StringBuilder sb = new StringBuilder();
        for(Key key : keys()) sb.append(key.toString() + ", ");
        if(sb.length() > 0)
            sb.setLength(sb.length() - 2);
        return sb.toString();
    }

    private String toStringIterator(Key from, Key to)
    {
        StringBuilder sb = new StringBuilder();
        for(Key key : keys(from, to)) sb.append(key.toString() + ", ");
        if(sb.length() > 0)
            sb.setLength(sb.length() - 2);
        return sb.toString();
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
            int ret;
            RecursiveBST<String, Integer> st =
                new RecursiveBST <String, Integer>();
            String pf = "fail";
            System.out.println("Testing all operations on empty symbol table:");
            System.out.println("Contents: " + st.toString());
            pf = "fail"; if(st.isEmpty()) pf = "pass"; System.out.println("    " + pf + " - isEmpty(): " + st.isEmpty());
            pf = "fail"; if(st.size() == 0) pf = "pass"; System.out.println("    " + pf + " - size(): " + st.size());
            pf = "fail"; if(st.size("C", "G") == 0) pf = "pass"; System.out.println("    " + pf + " - size(C, G): " + st.size("C", "G"));
            pf = "fail"; if(st.contains("E") == false) pf = "pass"; System.out.println("    " + pf + " - contains(E): " + st.contains("E"));
            pf = "fail"; if(st.get("E") == null) pf = "pass"; System.out.println("    " + pf + " - get(E): " + st.get("E"));
            pf = "fail"; if(st.min() == null) pf = "pass"; System.out.println("    " + pf + " - min(): " + st.min());
            pf = "fail"; if(st.max() == null) pf = "pass"; System.out.println("    " + pf + " - max(): " + st.max());
            pf = "fail"; if(st.floor("E") == null) pf = "pass"; System.out.println("    " + pf + " - floor(E): " + st.floor("E"));
            pf = "fail"; if(st.ceiling("E") == null) pf = "pass"; System.out.println("    " + pf + " - ceiling(E): " + st.ceiling("E"));
            pf = "fail"; if(st.rank("E") == 0) pf = "pass"; System.out.println("    " + pf + " - rank(E): " + st.rank("E"));
            pf = "fail"; if(st.select(5) == null) pf = "pass"; System.out.println("    " + pf + " - select(5): " + st.select(5));
            pf = "fail"; st.delete("E"); if(st.toString().isEmpty()) pf = "pass"; System.out.println("    " + pf + " - delete(E): " + st.toString());
            pf = "fail"; st.deleteMin(); if(st.toString().isEmpty()) pf = "pass"; System.out.println("    " + pf + " - deleteMin(): " + st.toString());
            pf = "fail"; st.deleteMax(); if(st.toString().isEmpty()) pf = "pass"; System.out.println("    " + pf + " - deleteMax(): " + st.toString());
            pf = "fail"; if(st.toStringIterator().isEmpty()) pf = "pass"; System.out.println("    " + pf + " - keys(): " + st.toStringIterator());
            pf = "fail"; if(st.toStringIterator("C", "P").isEmpty()) pf = "pass"; System.out.println("    " + pf + " - keys(C, P): " + st.toStringIterator("C", "P"));
            pf = "fail"; if(st.toStringIterator("D", "P").isEmpty()) pf = "pass"; System.out.println("    " + pf + " - keys(D, P): " + st.toStringIterator("D", "P"));
            pf = "fail"; if(st.toStringIterator("C", "Q").isEmpty()) pf = "pass"; System.out.println("    " + pf + " - keys(C, Q): " + st.toStringIterator("C", "Q"));
            pf = "fail"; if(st.toStringIterator("D", "Q").isEmpty()) pf = "pass"; System.out.println("    " + pf + " - keys(D, Q): " + st.toStringIterator("D", "Q"));
            pf = "fail"; ret = st.height(); if(ret == 0) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 0) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            System.out.println("");

            System.out.println("Testing all operations with 1 element:");
            pf = "fail"; st.put("G", 3); if(st.toString().equals(st.toString())) pf = "pass"; System.out.println("    " + pf + " - put(G, 3): " + st.toString());
            pf = "fail"; if(!st.isEmpty()) pf = "pass"; System.out.println("    " + pf + " - isEmpty(): " + st.isEmpty());
            pf = "fail"; if(st.size() == 1) pf = "pass"; System.out.println("    " + pf + " - size(): " + st.size());
            pf = "fail"; if(st.size("B", "G") == 1) pf = "pass"; System.out.println("    " + pf + " - size(B, G): " + st.size("B", "G"));
            pf = "fail"; if(st.size("B", "X") == 1) pf = "pass"; System.out.println("    " + pf + " - size(B, X): " + st.size("B", "X"));
            pf = "fail"; if(st.size("C", "D") == 0) pf = "pass"; System.out.println("    " + pf + " - size(C, D): " + st.size("C", "D"));
            pf = "fail"; if(st.size("W", "Z") == 0) pf = "pass"; System.out.println("    " + pf + " - size(W, Z): " + st.size("W", "Z"));
            pf = "fail"; if(st.contains("G")) pf = "pass"; System.out.println("    " + pf + " - contains(G): " + st.contains("G"));
            pf = "fail"; if(!st.contains("W")) pf = "pass"; System.out.println("    " + pf + " - contains(W): " + st.contains("W"));
            pf = "fail"; if(st.get("G") == 3) pf = "pass"; System.out.println("    " + pf + " - get(G): " + st.get("G"));
            pf = "fail"; if(st.get("W") == null) pf = "pass"; System.out.println("    " + pf + " - get(W): " + st.get("W"));
            pf = "fail"; if(st.min().equals("G")) pf = "pass"; System.out.println("    " + pf + " - min(): " + st.min());
            pf = "fail"; if(st.max().equals("G")) pf = "pass"; System.out.println("    " + pf + " - max(): " + st.max());
            pf = "fail"; if(st.floor("A") == null) pf = "pass"; System.out.println("    " + pf + " - floor(A): " + st.floor("A"));
            pf = "fail"; if(st.floor("G") == null) pf = "pass"; System.out.println("    " + pf + " - floor(G): " + st.floor("G"));
            pf = "fail"; if(st.floor("W").equals("G")) pf = "pass"; System.out.println("    " + pf + " - floor(W): " + st.floor("w"));
            pf = "fail"; if(st.ceiling("A").equals("G")) pf = "pass"; System.out.println("    " + pf + " - ceiling(A): " + st.ceiling("A"));
            pf = "fail"; if(st.ceiling("G") == null) pf = "pass"; System.out.println("    " + pf + " - ceiling(G): " + st.ceiling("G"));
            pf = "fail"; if(st.ceiling("W") == null) pf = "pass"; System.out.println("    " + pf + " - ceiling(W): " + st.ceiling("w"));
            pf = "fail"; if(st.rank("G") == 0) pf = "pass"; System.out.println("    " + pf + " - rank(G): " + st.rank("G"));
            pf = "fail"; if(st.rank("W") == 1) pf = "pass"; System.out.println("    " + pf + " - rank(W): " + st.rank("w"));
            pf = "fail"; if(st.select(0).equals("G")) pf = "pass"; System.out.println("    " + pf + " - select(0): " + st.select(0));
            pf = "fail"; if(st.select(3) == null) pf = "pass"; System.out.println("    " + pf + " - select(3): " + st.select(3));
            pf = "fail"; if(st.toStringIterator().equals("G")) pf = "pass"; System.out.println("    " + pf + " - keys(): " + st.toStringIterator());
            pf = "fail"; if(st.toStringIterator("C", "P").equals("G")) pf = "pass"; System.out.println("    " + pf + " - st.keys(C, P): " + st.toStringIterator());
            pf = "fail"; if(st.toStringIterator("D", "P").equals("G")) pf = "pass"; System.out.println("    " + pf + " - st.keys(D, P): " + st.toStringIterator());
            pf = "fail"; if(st.toStringIterator("C", "Q").equals("G")) pf = "pass"; System.out.println("    " + pf + " - st.keys(C, Q): " + st.toStringIterator());
            pf = "fail"; if(st.toStringIterator("D", "Q").equals("G")) pf = "pass"; System.out.println("    " + pf + " - st.keys(D, Q): " + st.toStringIterator());
            pf = "fail"; ret = st.height(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; st.put("A", 3); if(st.toString().equals("(A, 3), (G, 3)")) pf = "pass"; System.out.println("    " + pf + " - put(A, 3): " + st.toString());
            pf = "fail"; st.delete("A"); if(st.toString().equals("(G, 3)")) pf = "pass"; System.out.println("    " + pf + " - delete(A): " + st.toString());
            pf = "fail"; st.put("B", 2); if(st.toString().equals("(B, 2), (G, 3)")) pf = "pass"; System.out.println("    " + pf + " - put(B, 2): " + st.toString());
            pf = "fail"; st.put("C", 7); if(st.toString().equals("(B, 2), (C, 7), (G, 3)")) pf = "pass"; System.out.println("    " + pf + " - put(C, 7): " + st.toString());
            pf = "fail"; ret = st.height(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; st.deleteMin(); if(st.toString().equals("(C, 7), (G, 3)")) pf = "pass"; System.out.println("    " + pf + " - deleteMin(): " + st.toString());
            pf = "fail"; st.deleteMax(); if(st.toString().equals("(C, 7)")) pf = "pass"; System.out.println("    " + pf + " - deleteMax(): " + st.toString());
            pf = "fail"; st.deleteMin(); if(st.toString().equals("")) pf = "pass"; System.out.println("    " + pf + " - deleteMin(): " + st.toString());
            pf = "fail"; st.put("Z", 23); if(st.toString().equals("(Z, 23)")) pf = "pass"; System.out.println("    " + pf + " - put(Z, 23): " + st.toString());
            pf = "fail"; st.deleteMax(); if(st.toString().equals("")) pf = "pass"; System.out.println("    " + pf + " - deleteMax(): " + st.toString());
            System.out.println("");

            System.out.println("Populating symbol table:");
            pf = "fail"; st.put("B", 3); if(st.toString().equals("(B, 3)")) pf = "pass"; System.out.println("    " + pf + " - put(B, 1), size() = " + st.size() + ":  " + st.toString());
            pf = "fail"; st.put("W", 3); if(st.toString().equals("(B, 3), (W, 3)")) pf = "pass"; System.out.println("    " + pf + " - put(W, 2), size() = " + st.size() + ":  " + st.toString());
            pf = "fail"; st.put("O", 3); if(st.toString().equals("(B, 3), (O, 3), (W, 3)")) pf = "pass"; System.out.println("    " + pf + " - put(O, 3), size() = " + st.size() + ":  " + st.toString());
            pf = "fail"; st.put("P", 4); if(st.toString().equals("(B, 3), (O, 3), (P, 4), (W, 3)")) pf = "pass"; System.out.println("    " + pf + " - put(P, 4), size() = " + st.size() + ":  " + st.toString());
            pf = "fail"; st.put("F", 5); if(st.toString().equals("(B, 3), (F, 5), (O, 3), (P, 4), (W, 3)")) pf = "pass"; System.out.println("    " + pf + " - put(F, 5), size() = " + st.size() + ":  " + st.toString());
            pf = "fail"; st.put("R", 6); if(st.toString().equals("(B, 3), (F, 5), (O, 3), (P, 4), (R, 6), (W, 3)")) pf = "pass"; System.out.println("    " + pf + " - put(R, 6), size() = " + st.size() + ":  " + st.toString());
            pf = "fail"; st.put("C", 7); if(st.toString().equals("(B, 3), (C, 7), (F, 5), (O, 3), (P, 4), (R, 6), (W, 3)")) pf = "pass"; System.out.println("    " + pf + " - put(C, 7), size() = " + st.size() + ":  " + st.toString());
            pf = "fail"; ret = st.height(); if(ret == 5) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 5) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            System.out.println("");

            System.out.println("Testing iterators:");
            System.out.println("Contents: " + st.toString());
            pf = "fail"; if(st.toStringIterator().equals("B, C, F, O, P, R, W")) pf = "pass"; System.out.println("    " + pf + " - keys(): " + st.toStringIterator());
            pf = "fail"; if(st.toStringIterator("C", "P").equals("C, F, O, P")) pf = "pass"; System.out.println("    " + pf + " - keys(C, P): " + st.toStringIterator("C", "P"));
            pf = "fail"; if(st.toStringIterator("D", "P").equals("F, O, P")) pf = "pass"; System.out.println("    " + pf + " - keys(D, P): " + st.toStringIterator("D", "P"));
            pf = "fail"; if(st.toStringIterator("C", "Q").equals("C, F, O, P")) pf = "pass"; System.out.println("    " + pf + " - keys(C, Q): " + st.toStringIterator("C", "Q"));
            pf = "fail"; if(st.toStringIterator("D", "Q").equals("F, O, P")) pf = "pass"; System.out.println("    " + pf + " - keys(D, Q): " + st.toStringIterator("D", "Q"));
            System.out.println("");

            System.out.println("Testing with multiple elements:");
            System.out.println("Contents: " + st.toString());
            pf = "fail"; if(!st.isEmpty()) pf = "pass"; System.out.println("    " + pf + " - isEmpty(): " + st.isEmpty());
            pf = "fail"; if(st.size() == 7) pf = "pass"; System.out.println("    " + pf + " - size(): " + st.size());
            pf = "fail"; if(st.size("C", "P") == 4) pf = "pass"; System.out.println("    " + pf + " - size(C, P): " + st.size("C", "P"));
            pf = "fail"; if(st.size("D", "P") == 3) pf = "pass"; System.out.println("    " + pf + " - size(D, P): " + st.size("D", "P"));
            pf = "fail"; if(st.size("D", "Q") == 3) pf = "pass"; System.out.println("    " + pf + " - size(D, Q): " + st.size("D", "Q"));
            pf = "fail"; if(st.size("A", "C") == 2) pf = "pass"; System.out.println("    " + pf + " - size(A, C): " + st.size("A", "C"));
            pf = "fail"; if(st.contains("C")) pf = "pass"; System.out.println("    " + pf + " - contains(C): " + st.contains("C"));
            pf = "fail"; if(!st.contains("D")) pf = "pass"; System.out.println("    " + pf + " - contains(D): " + st.contains("D"));
            pf = "fail"; if(st.get("C") == 7) pf = "pass"; System.out.println("    " + pf + " - get(C): " + st.get("C"));
            pf = "fail"; if(st.get("D") == null) pf = "pass"; System.out.println("    " + pf + " - get(D): " + st.get("D"));
            pf = "fail"; if(st.min().equals("B")) pf = "pass"; System.out.println("    " + pf + " - min(): " + st.min());
            pf = "fail"; if(st.max().equals("W")) pf = "pass"; System.out.println("    " + pf + " - max(): " + st.max());
            pf = "fail"; if(st.floor("C").equals("B")) pf = "pass"; System.out.println("    " + pf + " - floor(C): " + st.floor("C"));
            pf = "fail"; if(st.floor("E").equals("C")) pf = "pass"; System.out.println("    " + pf + " - floor(E): " + st.floor("E"));
            pf = "fail"; if(st.ceiling("C").equals("F")) pf = "pass"; System.out.println("    " + pf + " - ceiling(C): " + st.ceiling("C"));
            pf = "fail"; if(st.ceiling("E").equals("F")) pf = "pass"; System.out.println("    " + pf + " - ceiling(E): " + st.ceiling("E"));
            pf = "fail"; if(st.rank("A") == 0) pf = "pass"; System.out.println("    " + pf + " - rank(A): " + st.rank("A"));
            pf = "fail"; if(st.rank("F") == 2) pf = "pass"; System.out.println("    " + pf + " - rank(F): " + st.rank("F"));
            pf = "fail"; if(st.rank("W") == 6) pf = "pass"; System.out.println("    " + pf + " - rank(W): " + st.rank("W"));
            pf = "fail"; if(st.rank("Z") == 7) pf = "pass"; System.out.println("    " + pf + " - rank(Z): " + st.rank("Z"));
            pf = "fail"; if(st.select(4).equals("P")) pf = "pass"; System.out.println("    " + pf + " - select(4): " + st.select(4));
            pf = "fail"; if(st.select(20) == null) pf = "pass"; System.out.println("    " + pf + " - select(20): " + st.select(20));
            pf = "fail"; if(st.select(-1) == null) pf = "pass"; System.out.println("    " + pf + " - select(-1): " + st.select(-1));
            pf = "fail"; st.delete("B"); if(st.toString().equals("(C, 7), (F, 5), (O, 3), (P, 4), (R, 6), (W, 3)")) pf = "pass"; System.out.println("    " + pf + " - delete(B), size() = " + st.size() + ", " + st.toString());
            pf = "fail"; ret = st.height(); if(ret == 5) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 5) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; st.delete("W"); if(st.toString().equals("(C, 7), (F, 5), (O, 3), (P, 4), (R, 6)")) pf = "pass"; System.out.println("    " + pf + " - delete(W), size() = " + st.size() + ", " + st.toString());
            pf = "fail"; ret = st.height(); if(ret == 4) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 4) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; st.delete("G"); if(st.toString().equals("(C, 7), (F, 5), (O, 3), (P, 4), (R, 6)")) pf = "pass"; System.out.println("    " + pf + " - delete(G), size() = " + st.size() + ", " + st.toString());
            pf = "fail"; ret = st.height(); if(ret == 4) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 4) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; st.deleteMin(); if(st.toString().equals("(F, 5), (O, 3), (P, 4), (R, 6)")) pf = "pass"; System.out.println("    " + pf + " - deleteMin(), size() = " + st.size() + ", " + st.toString());
            pf = "fail"; ret = st.height(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; st.deleteMax(); if(st.toString().equals("(F, 5), (O, 3), (P, 4)")) pf = "pass"; System.out.println("    " + pf + " - deleteMax(), size() = " + st.size() + ", " + st.toString());
            pf = "fail"; ret = st.height(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; st.deleteMin(); if(st.toString().equals("(O, 3), (P, 4)")) pf = "pass"; System.out.println("    " + pf + " - deleteMin(), size() = " + st.size() + ", " + st.toString());
            pf = "fail"; ret = st.height(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; st.deleteMax(); if(st.toString().equals("(O, 3)")) pf = "pass"; System.out.println("    " + pf + " - deleteMax(), size() = " + st.size() + ", " + st.toString());
            pf = "fail"; ret = st.height(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; st.deleteMin(); if(st.toString().equals("")) pf = "pass"; System.out.println("    " + pf + " - deleteMin(), size() = " + st.size() + ", " + st.toString());
            pf = "fail"; ret = st.height(); if(ret == 0) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 0) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; st.delete("X"); if(st.toString().equals("")) pf = "pass"; System.out.println("    " + pf + " - delete(X), size() = " + st.size() + ", " + st.toString());
            pf = "fail"; st.deleteMin(); if(st.toString().equals("")) pf = "pass"; System.out.println("    " + pf + " - deleteMin(), size() = " + st.size() + ", " + st.toString());
            pf = "fail"; st.deleteMax(); if(st.toString().equals("")) pf = "pass"; System.out.println("    " + pf + " - deleteMax(), size() = " + st.size() + ", " + st.toString());
        }
        else
        {
            RecursiveBST<String, Integer> st =
                new RecursiveBST<String, Integer>();
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
            System.out.println("    Contents: " + st.toString());
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
            System.out.println("    Contents: " + st.toString());
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
    }
}

