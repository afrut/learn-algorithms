// TODO: list all cases of inserting into 2-3 trees
// TODO: translate them in terms of red-black trees
// TODO: map out all cases on red-black trees
package libs.algs.st;
import java.util.Iterator;
import edu.princeton.cs.algs4.StdIn;
import libs.algs.st.SymbolTable;

// --------------------------------------------------------------------------------
//
// 2-3 Tree Construction
//
// Inserting into a single 2-node: Make it a 3-node.
//
// Inserting into a single 3-node: Make it a 4-node. Then split the 4-node into
// 3 2-nodes where the middle key is the parent, the lesser key is the left child
// and the larger key is the right child.
//
// Inserting into a child that is a 2-node: Make it a 3-node.
//
// Inserting into a child that is a 3-node: Make it a 4-node. Then split the 4-node
// into 3 2-nodes. Pass up the middle key to the parent. If the parent becomes
// a 4-node, repeat this operation of splitting and passing up the middle key.
// Worst case, the root becomes a 4-node, which then gets split into 3 2-nodes
// with the middle key becoming the new root.
//
// --------------------------------------------------------------------------------

// --------------------------------------------------------------------------------
//
// Red-Black BST Construction
// A 3-node is encoded by specifying that the link to a node is red. A red node
// is a node pointed to by a red link.
//
// Invariants:
// - Red links always lean left.
// - No node has two red links connected to it.
// - The tree has perfect black balance: the number of black links to every
//   from root to any null link is the same.
//
// ----------------------------------------
// Inserting into a single 2-node: Make it a 3-node. Always insert as a red node.
// Case 1: If the new node is less than the parent, the red node is naturally
// the left child.
//
// Case 2: If the new node is greater than the parent, the red node leans right.
// rotateLeft() fixes this.
// ----------------------------------------
// Inserting into a single 3-node: Always insert as a red node and make a 4-node.
// Case 1: If the new node is greater than the parent, the red node leans right and
// we obtain a node whose left and right child are both red nodes. This is remedied
// by making both children black and making the parent red, effectively passing up
// the middle key. flipColors(parent) fixes this.
//
// Case 2: If the new node is less than the two keys in the 3-node, it is inserted
// as a red node that is the left child of the already-existing red node. We now
// have a node whose left branch contains two subsequent red nodes. That is,
// parent.left is red and parent.left.left is also red. rotateRight(parent) followed
// by flipColors(parent) fixes this.
//
// Case 3: If the new node is between the two keys in the 3-node, it is inserted
// as a red node that is the right child of the already-existing red node. We now
// have a node whose left branch contains two subsequent red nodes. That is,
// parent.left is red and parent.left.right is also red. rotateLeft(parent.left),
// rotateRight(parent), and flipColors(parent) fixes this.
// ----------------------------------------
// Inserting into a child that is a 2-node: Same as inserting into a single 2-node.
// ----------------------------------------
// Inserting into a child that is a 3-node: Same as inserting into a single 3-node.
// But, keep in mind that turning the parent red will mean it has to be processed
// going back up the recursive calls.
//
// --------------------------------------------------------------------------------

public class RedBlackBST<Key extends Comparable<Key>, Value> implements SymbolTable<Key, Value>
{
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    // ----------------------------------------
    // Private classes
    // ----------------------------------------
    private class Node
    {
        Key key;
        Value value;
        Node left;
        Node right;
        int N;
        int H;
        boolean color;
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

    // ----------------------------------------
    // Private members
    // ----------------------------------------
    private Node root;

    // ----------------------------------------
    // Private helper functions
    // ----------------------------------------
    private boolean isRed(Node node)
    {
        if(node == null) return BLACK;
        else return node.color == RED;
    }

    private Node rotateLeft(Node node)
    {
        if(isRed(node.right) && !isRed(node.left))
        {
            Node temp = node.right;
            node.right = temp.left;
            updateNodeN(node);
            temp.left = node;
            temp.color = node.color;
            node.color = RED;
            node = temp;
            updateNodeN(node);
        }
        return node;
    }

    private Node rotateRight(Node node)
    {
        if(isRed(node.left) && node.left.left != null && isRed(node.left.left))
        {
            Node mid = node.left;
            Node temp = mid.right;
            mid.right = node;
            node.left = temp;
            updateNodeN(node);
            updateNodeN(mid);
            mid.color = node.color;
            node.color = RED;
            node = mid;
        }
        return node;
    }

    private Node flipColors(Node node)
    {
        if(node.left != null && node.right != null && isRed(node.left) && isRed(node.right))
        {
            node.left.color = BLACK;
            node.right.color = BLACK;
            node.color = RED;
        }
        return node;
    }

    // update count and height of nodes
    private void updateNodeN(Node node)
    {
        // TODO: account for red nodes
        if(node == null) return;
        int cnt = 0;
        node.H = 1;
        if(node.left != null)
        {
            node.H += node.left.H;
            cnt += node.left.N;
        }
        if(node.right != null)
        {
            int rightH = 1 + node.right.H;
            if(node.H < rightH) node.H = rightH;
            cnt += node.right.N;
        }
        node.N = cnt + 1;
    }

    // ----------------------------------------
    // Private sub-tree functions
    // ----------------------------------------
    // Insert (key, value) in the sub-tree rooted at node and return the root
    // of the sub-tree
    private Node put(Node node, Key key, Value value)
    {
        if(node == null)
        {
            Node ret = new Node();
            ret.key = key;
            ret.value = value;
            ret.N = 1;
            ret.H = 1;
            ret.color = RED;
            return ret;
        }
        else if(key.compareTo(node.key) < 0)
        {
            node.left = put(node.left, key, value);
            node = rotateRight(node);
            node = flipColors(node);
            return node;
        }
        else if(key.compareTo(node.key) > 0)
        {
            Node ret = put(node.right, key, value);
            node.right = ret;
            node = rotateLeft(node);
            node = flipColors(node);
            return node;
        }
        else
        {
            node.key = key;
            node.value = value;
            return node;
        }
    }

    // Return the Node associated with key within the binary search tree rooted
    // at node.
    private Node get(Node node, Key key)
    {
        if(node == null) return null;
        int res = key.compareTo(node.key);
        if(res < 0) return get(node.left, key);
        else if(res > 0) return get(node.right, key);
        else return node;
    }

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
            // TODO: write a function that finds the ceiling and deletes it in one function
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
                node.left = null;
            }

            // Update the counts of successor.
            updateNodeN(successor);

            // Return the successor so that it can be properly linked to the
            // node above node to delete.
            return successor;
        }
    }

    // Returns the Node associated with key that is within the binary search
    // tree rooted at node.
    private Node contains(Node node, Key key)
    {
        int res;
        if(node == null) return null;
        else res = key.compareTo(node.key);

        if(res < 0) return contains(node.left, key);
        else if(res > 0) return contains(node.right, key);
        else return node;
    }

    // Return the node containing the smallest key in the binary tree rooted at
    // node.
    private Node min(Node node)
    {
        if(node == null) return null;
        if(node.left != null) return min(node.left);
        else return node;
    }

    // Return the Node that is associated with the largest key within the binary
    // search tree rooted at node.
    private Node max(Node node)
    {
        if(node == null) return null;
        if(node.right != null) return max(node.right);
        else return node;
    }

    // Return the node with the largest key that is less than node.key
    // within the binary search tree rooted at node.
    private Node floor(Node node, Key key)
    {
        if(node == null) return null;
        int res = key.compareTo(node.key);
        if(res < 0) {return floor(node.left, key);}
        else if(res > 0)
        {
            Node ret = floor(node.right, key);
            if(ret == null) return node;
            else return ret;
        }
        else {return floor(node.left, key);}
    }

    // Return the node containing the smallest key greater than node.key within
    // the binary search tree rooted at node.
    private Node ceiling(Node node, Key key)
    {
        if(node == null) return null;
        int res = key.compareTo(node.key);
        if(res < 0)
        {
            Node ret = ceiling(node.left, key);
            if(ret == null) return node;
            else return ret;
        }
        else if(res > 0) {return ceiling(node.right, key);}
        else {return ceiling(node.right, key);}
    }

    // Returns the number of keys less than key within the binary search tree
    // at node.
    private int rank(Node node, Key key)
    {
        if(node == null) return 0;
        int res = key.compareTo(node.key);
        if(res < 0)
        {
            return rank(node.left, key);
        }
        else if(res > 0)
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

    // Return the Node that has k keys less than it within the binary search tree
    // rooted at node.
    private Node select(Node node, int k)
    {
        // TODO: refactor this
        if(k < 0 || node == null) return null;
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

    // Get the number of Keys between from and to, inclusive starting from Node.
    private int size(Node node, Key from, Key to)
    {
        if(node == null) return 0;
        int fromRes = from.compareTo(node.key);
        int toRes = to.compareTo(node.key);
        if(fromRes > 0)
        {
            return size(node.right, from, to);
        }
        else if(fromRes == 0)
        {
            return 1 + size(node.right, from, to);
        }
        else if(fromRes < 0 &&  toRes > 0)
        {
            return 1 + size(node.left, from, to) + size(node.right, from, to);
        }
        else if(toRes == 0)
        {
            return 1 + size(node.left, from, to);
        }
        else
        {
            return size(node.left, from, to);
        }
    }

    // Determine the height of the BST by examining every element rooted at node.
    private int heightCompute(Node node)
    {
        // TODO: account for red nodes
        if(node == null) return 0;
        else
        {
            int heightLeft = heightCompute(node.left);
            int heightRight = heightCompute(node.right);
            if(heightLeft > heightRight) return 1 + heightLeft;
            else return 1 + heightRight;
        }
    }

    // Return a string representation of all nodes rooted at node, in ascending order
    private String toString(Node node)
    {
        StringBuilder sb = new StringBuilder();
        toString(node, sb);
        return sb.toString();
    }

    // Uses StringBuilder to construct a string representation of all nodes
    // rooted at node, in ascending order
    private void toString(Node node, StringBuilder sb)
    {
        if(node == null) return;
        if(node.left != null) toString(node.left, sb);
        sb.append("(" + node.key + ", " + node.value + "), ");
        if(node.right != null) toString(node.right, sb);
    }

    // Returns a string representation of the keys of all nodes, in ascending order
    private String toStringIterator()
    {
        StringBuilder sb = new StringBuilder();
        for(Key key : keys()) sb.append(key.toString() + ", ");
        if(sb.length() > 0)
            sb.setLength(sb.length() - 2);
        return sb.toString();
    }

    // Returns a string representation of the keys of all nodes between from
    // and to in ascending order
    private String toStringIterator(Key from, Key to)
    {
        StringBuilder sb = new StringBuilder();
        for(Key key : keys(from, to)) sb.append(key.toString() + ", ");
        if(sb.length() > 0)
            sb.setLength(sb.length() - 2);
        return sb.toString();
    }

    // ----------------------------------------
    // Public methods
    // ----------------------------------------
    public RedBlackBST() {root = null;}

    // ----------------------------------------
    // tree operations
    // ----------------------------------------
    // Searches for key and replaces its value. If not found, inserts key and value.
    public void put(Key key, Value value)
    {
        root = put(root, key, value);
        root.color = BLACK;
    }

    // Return the value associated with key.
    public Value get(Key key)
    {
        Node node = get(root, key);
        if(node == null) return null;
        else return node.value;
    }

    // Remove the node that is associated with key.
    public void delete(Key key) {root = delete(root, key);}

    // Checks if the binary search tree contains a node associated with key.
    public boolean contains(Key key)
    {
        if(contains(root, key) != null) return true;
        else return false;
    }

    // Return the smallest Key.
    public Key min()
    {
        Node ret = min(root);
        if(ret == null) return null;
        else return ret.key;
    }

    // Return the largest key.
    public Key max()
    {
        Node ret = max(root);
        if(ret == null) return null;
        else return ret.key;
    }

    // Return the largest key that is less than key.
    public Key floor(Key key)
    {
        Node node = floor(root, key);
        if(node == null) return null;
        else return node.key;
    }

    // Return the smallest key that is greater than key.
    public Key ceiling(Key key)
    {
        Node node = ceiling(root, key);
        if(node == null) return null;
        else return node.key;
    }

    // Return the number of keys that are less than key.
    // Keys have to be unique.
    public int rank(Key key) {return rank(root, key);}

    // Return the key that has k keys less than it.
    public Key select(int k)
    {
        Node node = select(root, k);
        if(node == null) return null;
        else return node.key;
    }

    // Remove the node with the smallest key.
    public void deleteMin() {root = deleteMin(root);}

    // Remove the pair with the greatest key.
    public void deleteMax() {root = deleteMax(root);}

    // Get the number of Keys between from and to, inclusive.
    public int size(Key from, Key to) {return size(root, from, to);}

    // Determine the height of the BST by examining every element.
    public int heightCompute() {return heightCompute(root);}

    // ----------------------------------------
    // convenience
    // ----------------------------------------
    // Check if the binary search tree is null.
    public boolean isEmpty() {return root == null;}

    // Return the total number of nodes.
    public int size()
    {
        if(root == null) return 0;
        else return root.N;
    }

    // Return the height of the binary search tree using Node.H.
    public int height()
    {
        if(root == null) return 0;
        else return root.H;
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
            RedBlackBST<String, Integer> st = new RedBlackBST <String, Integer>();
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
            RedBlackBST<String, Integer> st =
                new RedBlackBST<String, Integer>();
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
