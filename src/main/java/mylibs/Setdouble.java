package mylibs;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

// --------------------------------------------------------------------------------
//
// Primitive int-key symbol table implementation based on red-black trees
//
// --------------------------------------------------------------------------------
public class Setdouble
{
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    // ----------------------------------------
    // Private classes
    // ----------------------------------------
    private class Node
    {
        double key;
        Node left;
        Node right;
        int N;          // number of 2-nodes
        int H;          // height in terms of only 2-nodes
        int H23;        // height in terms of both 2-nodes and 3-nodes
        boolean color;

        public boolean equals(Node node)
        {
            if
            (
                this.key == node.key &&
                this.N == node.N &&
                this.H == node.H &&
                this.H23 == node.H23
            ) return true;
            else return false;
        }
    }
    
    private class KeysIterable implements Iterable<Double>
    {
        private class KeysIterator implements Iterator<Double>
        {
            private LinkedList<Double> ll;
            private Double next;
            private int cnt;
            private int totalN;

            private KeysIterator()
            {
                ll = new LinkedList<Double>(); // initialize list of keys
                f(root);                        // recursive call to add all keys to list
                totalN = ll.size();
                next = ll.poll();               // initialize the next key to be returned
                cnt = 1;
            }

            public Double next()
            {
                Double ret = next;
                next = ll.poll();
                cnt++;
                return ret;
            }
            public boolean hasNext() {return cnt <= totalN;}
            public void remove() {};

            private void f(Node node)
            {
                if(node == null) return;
                f(node.left);
                ll.add(node.key);
                f(node.right);
            }
        }

        public KeysIterable() {}
        public KeysIterator iterator() {return new KeysIterator();}
    }

    private class KeysRangeIterable implements Iterable<Double>
    {
        private double from, to;

        private class KeysIterator implements Iterator<Double>
        {
            private LinkedList<Double> ll;
            private Double next;
            private int totalN;
            private int cnt;

            public KeysIterator()
            {
                ll = new LinkedList<Double>();
                f(root);
                totalN = ll.size();
                next = ll.poll();
                cnt = 1;
            }

            public boolean hasNext() {return cnt <= totalN;}
            public Double next()
            {
                Double ret = next;
                next = ll.poll();
                cnt++;
                return ret;
            }
            public void remove() {}

            private void f(Node node)
            {
                if(node == null) return;
                f(node.left);
                if(node.key >= from && node.key <= to)
                    ll.add(node.key);
                f(node.right);
            }
        }

        public KeysRangeIterable(double from, double to)
        {
            this.from = from;
            this.to = to;
        }
        public KeysIterator iterator() {return new KeysIterator();}
    }

    // ----------------------------------------
    // Private members
    // ----------------------------------------
    private Node root;

    // ----------------------------------------
    // Private helper functions
    // ----------------------------------------
    // Check if a node is red/connected to its parent by a red link
    private boolean isRed(Node node)
    {
        if(node == null) return BLACK;
        else return node.color == RED;
    }

    // Check if a node is a 3-node or a 4-node
    private boolean is34Node(Node node)
    {
        if(node != null && isRed(node.left)) return true;
        else return false;
    }

    // Method to fix a right-leaning red node
    private Node rotateLeft(Node node)
    {
        Node newRoot = node.right;
        node.right = newRoot.left;
        newRoot.left = node;
        newRoot.color = node.color;
        node.color = RED;
        updateNodeN(node);
        return newRoot;
    }

    // Method to fix two consecutive left-leaning red nodes
    private Node rotateRight(Node node)
    {
        Node newRoot = node.left;
        Node temp = newRoot.right;
        newRoot.right = node;
        node.left = temp;
        newRoot.color = node.color;
        node.color = RED;
        updateNodeN(node);
        return newRoot;
    }

    // Method to fix a node whose left and right children are red
    private Node passUpRed(Node node)
    {
        node.left.color = BLACK;
        node.right.color = BLACK;
        node.color = RED;
        return node;
    }

    // Method to ensure that the left node is a 3/4-node
    private Node prepareLeftNode(Node node)
    {
        if(node.left != null && node.left.left != null && !is34Node(node.left))
        {
            // node.left has 2 children; make a 4-node
            passDownRed(node.left);
        }
        return node;
    }

    // Method to ensure that the right node is a 3/4-node
    private Node prepareRightNode(Node node)
    {
        if(node.right != null && !is34Node(node.right))
        {
            // node.right has 2 children; make a 4-node
            if(isRed(node.right))
            {
                // current node is a 4-node
                // if the right child has children, pass down red link, if not, do nothing
                if(node.right.left != null) passDownRed(node.right);
            }
            else
            {
                // current node must be a 3-node as maintained by delete invariant
                // the returned node is the parent of the current node
                node = rotateRight(node);
                passDownRed(node.right);
            }
        }
        return node;
    }

    // Method to create a 4-node by combining a node in the parent with 2 children
    private void passDownRed(Node node)
    {
        node.color = BLACK;
        node.left.color = RED;
        node.right.color = RED;
    }

    // Update count and height of nodes
    // NOTE: all calls to updateNodeN must be done after flipping colors
    private void updateNodeN(Node node)
    {
        if(node == null) return;
        int cnt = 0;
        node.H = 1;
        node.H23 = 1;
        if(node.left != null)
        {
            node.H += node.left.H;
            cnt += node.left.N;

            // properly update the 2-3 height
            if(isRed(node.left))
            {
                Node left = node.left.left;
                Node mid = node.left.right;
                if(left != null) node.H23 = left.H23;
                if(mid != null && node.H23 < 1 + mid.H23) node.H23 = 1 + mid.H23;
            }
            else
                node.H23 = 1 + node.left.H23;
        }
        if(node.right != null)
        {
            int rightH = 1 + node.right.H;
            if(node.H < rightH) node.H = rightH;
            cnt += node.right.N;

            // properly update the 2-3 height
            if(isRed(node.left))
                if(node.H23 < 1 + node.right.H23) node.H23 = 1 + node.right.H23;
        }
        node.N = cnt + 1;
    }

    // ----------------------------------------
    // Private sub-tree functions
    // ----------------------------------------
    // Insert (key, value) in the sub-tree rooted at node and return the root
    // of the sub-tree
    private Node put(Node node, double key)
    {
        if(node == null)
        {
            Node ret = new Node();
            ret.key = key;
            ret.N = 1;
            ret.H = 1;
            ret.H23 = 1;
            ret.color = RED;
            return ret;
        }

        if(key < node.key)
        {
            node.left = put(node.left, key);
            if(isRed(node.left) && isRed(node.left.left))
            {
                node = rotateRight(node);
                node = passUpRed(node);
            }
            updateNodeN(node);
            return node;
        }
        else if(key > node.key)
        {
            node.right = put(node.right, key);
            if(node.left == null && isRed(node.right)) node = rotateLeft(node);
            if(isRed(node.left) && isRed(node.right)) node = passUpRed(node);
            updateNodeN(node);
            return node;
        }
        else
        {
            node.key = key;
            return node;
        }
    }

    // Return the Node associated with key within the binary search tree rooted
    // at node.
    private Node get(Node node, double key)
    {
        if(node == null) return null;
        if(key < node.key) return get(node.left, key);
        else if(key > node.key) return get(node.right, key);
        else return node;
    }

    // Delete the node associated with key within the binary search tree rooted
    // at node.
    private Node delete(Node node, double key)
    {
        if(node == null) return null;

        if(key < node.key)
        {
            // Direction of tree traversal is down and left
            // Ensure that node.left is either a 3-node or a 4-node
            node = prepareLeftNode(node);
            node.left = delete(node.left, key);
            updateNodeN(node);
        }
        else if(key > node.key)
        {
            // Direction of tree traversal is down and right
            // Ensure that node.right is either a 3-node or a 4-node
            //node.right.left != null && 
            node = prepareRightNode(node);
            node.right = delete(node.right, key);
            updateNodeN(node);
        }
        else
        {
            // Node with key found. Store it in a temporary variable.
            // node is the Node to delete.
            // Find the ceiling of this node within its binary tree.
            // TOOD: prepare next node before deleteMax() or deleteMin()
            Node minnode = min(node.right);
            if(minnode == null)
            {
                minnode = node.left;
                node = prepareLeftNode(node);
                node.left = deleteMax(node.left);
                updateNodeN(node);
            }
            else
            {
                node = prepareRightNode(node);
                if(node.key < key)
                {
                    node = delete(node, key);
                }
                else
                    node.right = deleteMin(node.right);
                updateNodeN(node);
            }
            if(minnode != null)
            {
                if(node.key == key)
                    node.key = minnode.key;
            }
            else node = null;
        }

        // Direction of traversal is now up the tree
        if(node != null)
        {
            if(isRed(node.right) && !isRed(node.left)) node = rotateLeft(node);
            if(isRed(node.left) && isRed(node.left.right) && node.left.right != null)
            {
                node.left = rotateLeft(node.left);
                updateNodeN(node.left);
            }
            if(isRed(node.left) && isRed(node.left.left) && !isRed(node.right))
                node = rotateRight(node);
            if(isRed(node.left) && isRed(node.right)) node = passUpRed(node);
            updateNodeN(node);
        }
        return node;
    }

    // Returns the Node associated with key that is within the binary search
    // tree rooted at node.
    private Node contains(Node node, double key)
    {
        if(node == null) return null;

        if(key < node.key) return contains(node.left, key);
        else if(key > node.key) return contains(node.right, key);
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
    private Node floor(Node node, double key)
    {
        if(node == null) return null;
        if(key < node.key) {return floor(node.left, key);}
        else if(key > node.key)
        {
            Node ret = floor(node.right, key);
            if(ret == null) return node;
            else return ret;
        }
        else {return floor(node.left, key);}
    }

    // Return the node containing the smallest key greater than node.key within
    // the binary search tree rooted at node.
    private Node ceiling(Node node, double key)
    {
        if(node == null) return null;
        if(key < node.key)
        {
            Node ret = ceiling(node.left, key);
            if(ret == null) return node;
            else return ret;
        }
        else if(key > node.key) {return ceiling(node.right, key);}
        else {return ceiling(node.right, key);}
    }

    // Returns the number of keys less than key within the binary search tree
    // at node.
    private int rank(Node node, double key)
    {
        if(node == null) return 0;
        if(key < node.key)
        {
            return rank(node.left, key);
        }
        else if(key > node.key)
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
        if(k < 0 || node == null) return null;
        if(k == 0) return min(node);
        if(node.left != null)
        {
            if(node.left.N > k)
                return select(node.left, k);
            else if(node.left.N < k)
                return select(node.right, k - node.left.N - 1);
            else
                return node;
        }
        else if(node.right != null)
        {
            if(node.right.N > k)
                return select(node.right, k - 1);
            else return null;
        }
        else return null;
    }

    // Remove the node with the smallest key within the subtree rooted at node.
    private Node deleteMin(Node node)
    {
        if(node == null) return null;

        // Direction of tree traversal is down and left
        // Ensure that node.left is either a 3-node or a 4-node
        if(node.left != null)
        {
            if(node.left.left != null && !is34Node(node.left))
                // node.left has 2 children; make a 4-node
                passDownRed(node.left);

            // Recursive call
            node.left = deleteMin(node.left);

            // Direction of tree traversal is now up
            // Check for right-leaning red links on the way back up the tree
            if(isRed(node.right)) node = rotateLeft(node);
            if(isRed(node.left) && isRed(node.left.left)) node = rotateRight(node);
            if(isRed(node.left) && isRed(node.right)) node = passUpRed(node);
            updateNodeN(node);
            return node;
        }
        else
        {
            // Current node is the node to delete
            // If node is the minimum, node.left must be null
            // If node.left is null, node.right must also be null, otherwise, a
            // rotateLeft() in put would have made the node right-leaning
            return null;
        }
    }

    // Remove the node with the largest key within the subtree rooted at node.
    private Node deleteMax(Node node)
    {
        if(node == null) return null;
        Node ret;

        // Direction of tree traversal is down and right
        // Ensure that node.right is either a 3-node or a 4-node
        if(node.right != null)
        {
            if(node.right.left != null && !is34Node(node.right))
            {
                // node.right has 2 children; make a 4-node
                if(isRed(node.right))
                {
                    // current node is a 4-node
                    passDownRed(node.right);
                }
                else
                {
                    // current node must be a 3-node as maintained by delete invariant
                    // the returned node is the parent of the current node
                    node = rotateRight(node);
                    passDownRed(node.right);
                }
            }
            node.right = deleteMax(node.right);
            ret = node;
        }
        else
        {
            // Current node is the node to delete
            // If node is the maximum, node.left may or may not be null
            if(node.left != null) node.left.color = node.color;
            ret = node.left;
            node.left = null;
        }

        // Direction of traversal is now up the tree
        node = ret;
        if(node != null)
        {
            if(isRed(node.right)) node = rotateLeft(node);
            if(isRed(node.left) && isRed(node.left.left)) node = rotateRight(node);
            if(isRed(node.left) && isRed(node.right)) node = passUpRed(node);
            updateNodeN(node);
        }
        return node;
    }

    // Get the number of ints between from and to, inclusive starting from Node.
    private int size(Node node, double from, double to)
    {
        if(node == null) return 0;
        if(from > node.key)
        {
            return size(node.right, from, to);
        }
        else if(from == node.key)
        {
            return 1 + size(node.right, from, to);
        }
        else if(from < node.key &&  to > node.key)
        {
            return 1 + size(node.left, from, to) + size(node.right, from, to);
        }
        else if(to == node.key)
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
        if(node == null) return 0;
        else
        {
            int heightLeft = heightCompute(node.left);
            int heightRight = heightCompute(node.right);
            if(heightLeft > heightRight) return 1 + heightLeft;
            else return 1 + heightRight;
        }
    }

    // Determine the 2-3 height of the red-black BST
    private int heightCompute23(Node node)
    {
        if(node == null) return 0;
        int cntLeft = heightCompute23(node.left);
        int cntRight = heightCompute23(node.right);
        if(cntLeft > 0 && node.left.color == RED) cntLeft--;
        if(cntLeft > cntRight) return cntLeft + 1;
        else return cntRight + 1;
    }

    // Create a clone of the subtree rooted at node
    private Node clone(Node node)
    {
        if(node == null) return null;
        Node ret = new Node();
        ret.key = node.key;
        ret.color = node.color;
        ret.N = node.N;
        ret.H = node.H;
        ret.H23 = node.H23;
        ret.left = clone(node.left);
        ret.right = clone(node.right);
        return ret;
    }

    // Checks if two subtrees rooted at nodes node1 and node2 are equal
    private boolean equals(Node node1, Node node2)
    {
        if(node1 == null && node2 == null) return true;
        if(node1.equals(node2))
        {
            if(equals(node1.left, node2.left)) return equals(node1.right, node2.right);
            else return false;
        }
        else return false;
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
        sb.append(node.key + ", ");
        if(node.right != null) toString(node.right, sb);
    }

    // Returns a tree-like string representation of the BST
    private void treeString(Node node, StringBuilder sb, String space)
    {
        if(node == null) return;
        sb.append(node.key + ", isRed: " + node.color + "\n");
        if(node.left != null)
        {
            sb.append(space + "  left: ");
            treeString(node.left, sb, space + "  ");
        }
        if(node.right != null)
        {
            sb.append(space + "  right: ");
            treeString(node.right, sb, space + "  ");
        }
    }

    // Returns a string representation of the keys of all nodes, in ascending order
    private String toStringIterator()
    {
        StringBuilder sb = new StringBuilder();
        if(this.root != null && this.root.N > 0)
        {
            for(double key : keys()) sb.append(String.format("%.3f, ", key));
            if(sb.length() > 0)
                sb.setLength(sb.length() - 2);
        }
        return sb.toString();
    }

    // Returns a string representation of the keys of all nodes between from
    // and to in ascending order
    private String toStringIterator(double from, double to)
    {
        StringBuilder sb = new StringBuilder();
        if(this.root != null && this.root.N > 0)
        {
            for(double key : keys(from, to)) sb.append(String.format("%.3f, ", key));
            if(sb.length() > 0)
                sb.setLength(sb.length() - 2);
        }
        return sb.toString();
    }

    // ----------------------------------------
    // Test functions
    // ----------------------------------------
    // Checks if the tree rooted at node is perfectly black-balanced
    // A return of -1 indicates that the tree is NOT perfectly black-balanced
    private int checkBlackBalance(Node node)
    {
        if(node == null) return 1;
        int cntLeft = checkBlackBalance(node.left);
        int cntRight = checkBlackBalance(node.right);
        if(cntLeft > 0 && cntRight > 0 && cntLeft == cntRight)
        {
            if(isRed(node)) return cntLeft;
            else return cntLeft + 1;
        }
        else return -1;
    }

    // Checks if the entire tree is perfectly black-balanced
    // A return of -1 indicates that the tree is NOT perfectly black-balanced
    private int checkBlackBalance() {return checkBlackBalance(root) - 1;}

    // ----------------------------------------
    // Public methods
    // ----------------------------------------
    public Setdouble() {root = null;}

    // ----------------------------------------
    // tree operations
    // ----------------------------------------
    // Searches for key and replaces its value. If not found, inserts key and value.
    public void put(double key)
    {
        root = put(root, key);
        root.color = BLACK;
    }

    // Remove the node that is associated with key.
    public void delete(double key)
    {
        // Prepare the root node so that it is red and
        // either a 3-node or a 4-node
        if(root != null && root.left != null && !is34Node(root) )
        {
            // root is a 2-node with 2 children
            root.left.color = RED;
            root.right.color = RED;
        }

        // Recursive call
        root = delete(root, key);

        // Keep root black
        if(root != null) root.color = BLACK;
    }

    // Checks if the binary search tree contains a node associated with key.
    public boolean contains(double key)
    {
        if(contains(root, key) != null) return true;
        else return false;
    }

    // Return the smallest key.
    public Double min()
    {
        Node ret = min(root);
        if(ret == null) return null;
        else return ret.key;
    }

    // Return the largest key.
    public Double max()
    {
        Node ret = max(root);
        if(ret == null) return null;
        else return ret.key;
    }

    // Return the largest key that is less than key.
    public Double floor(double key)
    {
        Node node = floor(root, key);
        if(node == null) return null;
        else return node.key;
    }

    // Return the smallest key that is greater than key.
    public Double ceiling(double key)
    {
        Node node = ceiling(root, key);
        if(node == null) return null;
        else return node.key;
    }

    // Return the number of keys that are less than key.
    // Keys have to be unique.
    public int rank(double key) {return rank(root, key);}

    // Return the key that has k keys less than it.
    public Double select(int k)
    {
        if(k > 0)
        {
            Node node = select(root, k);
            if(node == null) return null;
            else return node.key;
        }
        else if(k < 0) return null;
        else return this.min();
    }

    // Remove the node with the smallest key.
    public void deleteMin()
    {
        // Prepare the root node so that it is red and
        // either a 3-node or a 4-node
        if(root != null && root.left != null && !is34Node(root) )
        {
            // root is a 2-node with 2 children
            root.left.color = RED;
            root.right.color = RED;
        }

        // Recursive call
        root = deleteMin(root);

        // Keep root black
        if(root != null) root.color = BLACK;
    }

    // Remove the pair with the greatest key.
    public void deleteMax()
    {
        // Prepare the root node so that it is red and
        // either a 3-node or a 4-node
        if(root != null && root.left != null && !is34Node(root) )
        {
            // root is a 2-node with 2 children
            root.left.color = RED;
            root.right.color = RED;
        }

        // Recursive call
        root = deleteMax(root);

        // Keep root black
        if(root != null) root.color = BLACK;
    }

    // Get the number of Keys between from and to, inclusive.
    public int size(double from, double to) {return size(root, from, to);}

    // Determine the height of the BST by examining every element.
    public int heightCompute() {return heightCompute(root);}

    // Determine the 2-3 height of the red-black BST by examining every element.
    public int heightCompute23() {return heightCompute23(root);}

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

    // Return the 2-3 height of the red-black BST
    public int height23()
    {
        if(root == null) return 0;
        else return root.H23;
    }

    // Return a copy of the binary search tree
    public Setdouble clone()
    {
        Setdouble ret = new Setdouble();
        ret.root = clone(this.root);
        return ret;
    }

    // Checks if two trees are equal
    public boolean equals(Setdouble tree) {return equals(this.root, tree.root);}

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

    public String treeString()
    {
        StringBuilder sb = new StringBuilder();
        String space = "";
        treeString(root, sb, space);
        return sb.toString();
    }

    public Iterable<Double> keys() {return new KeysIterable();}
    public Iterable<Double> keys(double lo, double hi) {return new KeysRangeIterable(lo ,hi);}

    public static void main(String[] args) throws FileNotFoundException
    {
        boolean test = false;
        if(args.length > 0)
        {
            for(String arg : args) {if(arg.equals("-test")) test = true;}
        }

        if(test)
        {
            Integer ret;
            Double retDbl;
            boolean retBool;
            String retStr;
            Integer sz;
            Setdouble st = new Setdouble();
            String pf = "fail";
            System.out.println("Testing all operations on empty symbol table:");
            System.out.println("Contents: " + st.toString());
            pf = "fail"; retBool = st.isEmpty(); if(retBool) pf = "pass"; System.out.println("    " + pf + " - isEmpty(): " + retBool);
            pf = "fail"; ret = st.size(); if(ret == 0) pf = "pass"; System.out.println("    " + pf + " - size(): " + ret);
            pf = "fail"; ret = st.size(10.046, 45.454); if(ret == 0) pf = "pass"; System.out.println("    " + pf + " - size(10.046, 45.454): " + ret);
            pf = "fail"; retBool = st.contains(30.398); if(!retBool) pf = "pass"; System.out.println("    " + pf + " - contains(30.398): " + retBool);
            pf = "fail"; retDbl = st.min(); if(retDbl == null) pf = "pass"; System.out.println("    " + pf + " - min(): " + retDbl);
            pf = "fail"; retDbl = st.max(); if(retDbl == null) pf = "pass"; System.out.println("    " + pf + " - max(): " + retDbl);
            pf = "fail"; retDbl = st.floor(30.398); if(retDbl == null) pf = "pass"; System.out.println("    " + pf + " - floor(30.398): " + retDbl);
            pf = "fail"; retDbl = st.ceiling(30.398); if(retDbl == null) pf = "pass"; System.out.println("    " + pf + " - ceiling(30.398): " + retDbl);
            pf = "fail"; ret = st.rank(30.398); if(ret == 0) pf = "pass"; System.out.println("    " + pf + " - rank(30.398): " + ret);
            pf = "fail"; retDbl = st.select(5); if(retDbl == null) pf = "pass"; System.out.println("    " + pf + " - select(5): " + retDbl);
            pf = "fail"; st.delete(30.398); if(st.toString().isEmpty()) pf = "pass"; System.out.println("    " + pf + " - delete(30.398): " + st.toString());
            pf = "fail"; st.deleteMin(); if(st.toString().isEmpty()) pf = "pass"; System.out.println("    " + pf + " - deleteMin(): " + st.toString());
            pf = "fail"; st.deleteMax(); if(st.toString().isEmpty()) pf = "pass"; System.out.println("    " + pf + " - deleteMax(): " + st.toString());
            pf = "fail"; if(st.toStringIterator().isEmpty()) pf = "pass"; System.out.println("    " + pf + " - keys(): " + st.toStringIterator());
            pf = "fail"; if(st.toStringIterator(10.046, 45.454).isEmpty()) pf = "pass"; System.out.println("    " + pf + " - keys(10.046, 45.454): " + st.toStringIterator(10.046, 45.454));
            pf = "fail"; if(st.toStringIterator(5.192, 45.454).isEmpty()) pf = "pass"; System.out.println("    " + pf + " - keys(5.192, 45.454): " + st.toStringIterator(5.192, 45.454));
            pf = "fail"; if(st.toStringIterator(10.046, 50).isEmpty()) pf = "pass"; System.out.println("    " + pf + " - keys(10.046, 50): " + st.toStringIterator(10.046, 50));
            pf = "fail"; if(st.toStringIterator(5.192, 50).isEmpty()) pf = "pass"; System.out.println("    " + pf + " - keys(5.192, 50): " + st.toStringIterator(5.192, 50));
            pf = "fail"; ret = st.height(); if(ret == 0) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 0) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 0) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 0) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 0) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            System.out.println("");

            System.out.println("Testing all operations with 1 element:");
            pf = "fail"; st.put(30.398); if(st.toString().equals(st.toString())) pf = "pass"; System.out.println("    " + pf + " - put(30.398): " + st.toString());
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; retBool = st.isEmpty(); if(!retBool) pf = "pass"; System.out.println("    " + pf + " - isEmpty(): " + retBool);
            pf = "fail"; ret = st.size(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - size(): " + st.size());
            pf = "fail"; ret = st.size(30.398, 30.398); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - size(30.398, 30.398): " + ret);
            pf = "fail"; ret = st.size(5.192, 30.398); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - size(5.192, 30.398): " + ret);
            pf = "fail"; ret = st.size(30.398, 50); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - size(30.398, 50): " + ret);
            pf = "fail"; ret = st.size(5.192, 50); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - size(5.192, 50): " + ret);
            pf = "fail"; ret = st.size(5.192, 10.046); if(ret == 0) pf = "pass"; System.out.println("    " + pf + " - size(5.192, 10.046): " + ret);
            pf = "fail"; retBool = st.contains(30.398); if(retBool) pf = "pass"; System.out.println("    " + pf + " - contains(30.398): " + retBool);
            pf = "fail"; retBool = st.contains(5); if(!retBool) pf = "pass"; System.out.println("    " + pf + " - contains(5): " + retBool);
            pf = "fail"; retDbl = st.min(); if(retDbl == 30.398) pf = "pass"; System.out.println("    " + pf + " - min(): " + retDbl);
            pf = "fail"; retDbl = st.max(); if(retDbl == 30.398) pf = "pass"; System.out.println("    " + pf + " - max(): " + retDbl);
            pf = "fail"; retDbl = st.floor(30.398); if(retDbl == null) pf = "pass"; System.out.println("    " + pf + " - floor(30.398): " + retDbl);
            pf = "fail"; retDbl = st.floor(3); if(retDbl == null) pf = "pass"; System.out.println("    " + pf + " - floor(3): " + retDbl);
            pf = "fail"; retDbl = st.floor(50); if(retDbl == 30.398) pf = "pass"; System.out.println("    " + pf + " - floor(50): " + retDbl);
            pf = "fail"; retDbl = st.ceiling(30.398); if(retDbl == null) pf = "pass"; System.out.println("    " + pf + " - ceiling(30.398): " + retDbl);
            pf = "fail"; retDbl = st.ceiling(5); if(retDbl == 30.398) pf = "pass"; System.out.println("    " + pf + " - ceiling(5): " + retDbl);
            pf = "fail"; retDbl = st.ceiling(50); if(retDbl == null) pf = "pass"; System.out.println("    " + pf + " - ceiling(50): " + retDbl);
            pf = "fail"; ret = st.rank(30.398); if(ret == 0) pf = "pass"; System.out.println("    " + pf + " - rank(30.398): " + ret);
            pf = "fail"; ret = st.rank(5); if(ret == 0) pf = "pass"; System.out.println("    " + pf + " - rank(5): " + ret);
            pf = "fail"; ret = st.rank(50); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - rank(50): " + ret);
            pf = "fail"; retDbl = st.select(0); if(retDbl == 30.398) pf = "pass"; System.out.println("    " + pf + " - select(0): " + retDbl);
            pf = "fail"; retDbl = st.select(1); if(retDbl == null) pf = "pass"; System.out.println("    " + pf + " - select(1): " + retDbl);
            pf = "fail"; retDbl = st.select(3); if(retDbl == null) pf = "pass"; System.out.println("    " + pf + " - select(3): " + retDbl);
            pf = "fail"; retStr = st.toStringIterator(); if(retStr.equals("30.398")) pf = "pass"; System.out.println("    " + pf + " - keys(): " + retStr);
            pf = "fail"; retStr = st.toStringIterator(30.398, 30.398); if(retStr.equals("30.398")) pf = "pass"; System.out.println("    " + pf + " - st.keys(30.398, 30.398): " + retStr);
            pf = "fail"; retStr = st.toStringIterator(5.192, 30.398); if(retStr.equals("30.398")) pf = "pass"; System.out.println("    " + pf + " - st.keys(5.192, 30.398): " + retStr);
            pf = "fail"; retStr = st.toStringIterator(30.398, 50); if(retStr.equals("30.398")) pf = "pass"; System.out.println("    " + pf + " - st.keys(30.398, 50): " + retStr);
            pf = "fail"; retStr = st.toStringIterator(5.192, 50); if(retStr.equals("30.398")) pf = "pass"; System.out.println("    " + pf + " - st.keys(5.192, 50): " + retStr);
            pf = "fail"; retStr = st.toStringIterator(5.192, 10.046); if(retStr.equals("")) pf = "pass"; System.out.println("    " + pf + " - st.keys(5.192, 10.046): " + retStr);
            pf = "fail"; ret = st.height(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; st.put(10.046); retStr = st.toString(); if(retStr.equals("10.046, 30.398")) pf = "pass"; System.out.println("    " + pf + " - put(10.046): " + retStr);
            pf = "fail"; ret = st.height(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; st.delete(10.046); retStr = st.toString(); if(retStr.equals("30.398")) pf = "pass"; System.out.println("    " + pf + " - delete(10.046): " + retStr);
            pf = "fail"; ret = st.height(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; st.put(45.454); retStr = st.toString(); if(retStr.equals("30.398, 45.454")) pf = "pass"; System.out.println("    " + pf + " - put(45.454): " + retStr);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; ret = st.height(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; st.put(10.046); retStr = st.toString(); if(retStr.equals("10.046, 30.398, 45.454")) pf = "pass"; System.out.println("    " + pf + " - put(10.046): " + retStr);
            pf = "fail"; ret = st.height(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; st.deleteMin(); retStr = st.toString(); if(retStr.equals("30.398, 45.454")) pf = "pass"; System.out.println("    " + pf + " - deleteMin(): " + retStr);
            pf = "fail"; ret = st.height(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; st.deleteMax(); retStr = st.toString(); if(retStr.equals("30.398")) pf = "pass"; System.out.println("    " + pf + " - deleteMax(): " + retStr);
            pf = "fail"; ret = st.height(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; st.deleteMin(); retStr = st.toString(); if(retStr.equals("")) pf = "pass"; System.out.println("    " + pf + " - deleteMin(): " + retStr);
            pf = "fail"; ret = st.height(); if(ret == 0) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 0) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 0) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 0) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 0) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; st.put(30.398); retStr = st.toString(); if(retStr.equals("30.398")) pf = "pass"; System.out.println("    " + pf + " - put(30.398): " + retStr);
            pf = "fail"; ret = st.height(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; st.deleteMax(); retStr = st.toString(); if(retStr.equals("")) pf = "pass"; System.out.println("    " + pf + " - deleteMax(): " + retStr);
            pf = "fail"; ret = st.height(); if(ret == 0) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 0) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 0) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 0) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 0) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            System.out.println("");

            System.out.println("Populating symbol table:");
            pf = "fail"; st.put(30.398); retStr = st.toString(); if(retStr.equals("30.398")) pf = "pass"; System.out.println("    " + pf + " - put(30.398), size() = " + st.size() + ": " + retStr);
            pf = "fail"; ret = st.height(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; st.put(45.454); retStr = st.toString(); if(retStr.equals("30.398, 45.454")) pf = "pass"; System.out.println("    " + pf + " - put(45.454), size() = " + st.size() + ": " + retStr);
            pf = "fail"; ret = st.height(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; st.put(10.046); retStr = st.toString(); if(retStr.equals("10.046, 30.398, 45.454")) pf = "pass"; System.out.println("    " + pf + " - put(10.046), size() = " + st.size() + ": " + retStr);
            pf = "fail"; ret = st.height(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; st.put(15.983); retStr = st.toString(); if(retStr.equals("10.046, 15.983, 30.398, 45.454")) pf = "pass"; System.out.println("    " + pf + " - put(15.983), size() = " + st.size() + ": " + retStr);
            pf = "fail"; ret = st.height(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; st.put(25.501); retStr = st.toString(); if(retStr.equals("10.046, 15.983, 25.501, 30.398, 45.454")) pf = "pass"; System.out.println("    " + pf + " - put(25.501), size() = " + st.size() + ": " + retStr);
            pf = "fail"; ret = st.height(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; st.put(35.556); retStr = st.toString(); if(retStr.equals("10.046, 15.983, 25.501, 30.398, 35.556, 45.454")) pf = "pass"; System.out.println("    " + pf + " - put(35.556), size() = " + st.size() + ": " + retStr);
            pf = "fail"; ret = st.height(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; st.put(60.333); retStr = st.toString(); if(retStr.equals("10.046, 15.983, 25.501, 30.398, 35.556, 45.454, 60.333")) pf = "pass"; System.out.println("    " + pf + " - put(60.333), size() = " + st.size() + ": " + retStr);
            pf = "fail"; ret = st.height(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; st.put(5.192); retStr = st.toString(); if(retStr.equals("5.192, 10.046, 15.983, 25.501, 30.398, 35.556, 45.454, 60.333")) pf = "pass"; System.out.println("    " + pf + " - put(5.192), size() = " + st.size() + ": " + retStr);
            pf = "fail"; ret = st.height(); if(ret == 4) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 4) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; st.put(13.267); retStr = st.toString(); if(retStr.equals("5.192, 10.046, 13.267, 15.983, 25.501, 30.398, 35.556, 45.454, 60.333")) pf = "pass"; System.out.println("    " + pf + " - put(13.267), size() = " + st.size() + ": " + retStr);
            pf = "fail"; ret = st.height(); if(ret == 4) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 4) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; st.put(3.002); retStr = st.toString(); if(retStr.equals("3.002, 5.192, 10.046, 13.267, 15.983, 25.501, 30.398, 35.556, 45.454, 60.333")) pf = "pass"; System.out.println("    " + pf + " - put(3.002), size() = " + st.size() + ": " + retStr);
            pf = "fail"; ret = st.height(); if(ret == 5) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 5) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; st.put(11.742); retStr = st.toString(); if(retStr.equals("3.002, 5.192, 10.046, 11.742, 13.267, 15.983, 25.501, 30.398, 35.556, 45.454, 60.333")) pf = "pass"; System.out.println("    " + pf + " - put(11.742), size() = " + st.size() + ": " + retStr);
            pf = "fail"; ret = st.height(); if(ret == 5) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 5) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; st.put(20.773); retStr = st.toString(); if(retStr.equals("3.002, 5.192, 10.046, 11.742, 13.267, 15.983, 20.773, 25.501, 30.398, 35.556, 45.454, 60.333")) pf = "pass"; System.out.println("    " + pf + " - put(20.773), size() = " + st.size() + ": " + retStr);
            pf = "fail"; ret = st.height(); if(ret == 5) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 5) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; st.put(27.277); retStr = st.toString(); if(retStr.equals("3.002, 5.192, 10.046, 11.742, 13.267, 15.983, 20.773, 25.501, 27.277, 30.398, 35.556, 45.454, 60.333")) pf = "pass"; System.out.println("    " + pf + " - put(27.277), size() = " + st.size() + ": " + retStr);
            pf = "fail"; ret = st.height(); if(ret == 5) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 5) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; st.put(31.111); retStr = st.toString(); if(retStr.equals("3.002, 5.192, 10.046, 11.742, 13.267, 15.983, 20.773, 25.501, 27.277, 30.398, 31.111, 35.556, 45.454, 60.333")) pf = "pass"; System.out.println("    " + pf + " - put(31.111), size() = " + st.size() + ": " + retStr);
            pf = "fail"; ret = st.height(); if(ret == 5) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 5) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            System.out.println("");

            System.out.println("Testing iterators:");
            System.out.println("Contents: " + st.toString());
            pf = "fail"; retStr = st.toStringIterator(); if(retStr.equals("3.002, 5.192, 10.046, 11.742, 13.267, 15.983, 20.773, 25.501, 27.277, 30.398, 31.111, 35.556, 45.454, 60.333")) pf = "pass"; System.out.println("    " + pf + " - keys(): " + retStr);
            pf = "fail"; retStr = st.toStringIterator(10.046, 30.398); if(retStr.equals("10.046, 11.742, 13.267, 15.983, 20.773, 25.501, 27.277, 30.398")) pf = "pass"; System.out.println("    " + pf + " - keys(10.046, 30.398): " + retStr);
            pf = "fail"; retStr = st.toStringIterator(6, 30.398); if(retStr.equals("10.046, 11.742, 13.267, 15.983, 20.773, 25.501, 27.277, 30.398")) pf = "pass"; System.out.println("    " + pf + " - keys(6, 30.398): " + retStr);
            pf = "fail"; retStr = st.toStringIterator(10.046, 32); if(retStr.equals("10.046, 11.742, 13.267, 15.983, 20.773, 25.501, 27.277, 30.398, 31.111")) pf = "pass"; System.out.println("    " + pf + " - keys(10.046, 32): " + retStr);
            pf = "fail"; retStr = st.toStringIterator(6, 32); if(retStr.equals("10.046, 11.742, 13.267, 15.983, 20.773, 25.501, 27.277, 30.398, 31.111")) pf = "pass"; System.out.println("    " + pf + " - keys(6, 32): " + retStr);
            System.out.println("");

            System.out.println("Testing with multiple elements:");
            System.out.println("Contents: " + st.toString());
            Setdouble orig = st.clone();
            pf = "fail"; retBool = orig.equals(st); if(retBool) pf = "pass"; System.out.println("    " + pf + " - equals(st): " + retBool);
            pf = "fail"; retBool = st.isEmpty(); if(!retBool) pf = "pass"; System.out.println("    " + pf + " - isEmpty(): " + retBool);
            pf = "fail"; ret = st.size(); if(ret == 14) pf = "pass"; System.out.println("    " + pf + " - size(): " + ret);
            pf = "fail"; ret = st.size(10.046, 30.398); if(ret == 8) pf = "pass"; System.out.println("    " + pf + " - size(10.046, 30.398): " + ret);
            pf = "fail"; ret = st.size(6, 30.398); if(ret == 8) pf = "pass"; System.out.println("    " + pf + " - size(6, 30.398): " + ret);
            pf = "fail"; ret = st.size(10.046, 32); if(ret == 9) pf = "pass"; System.out.println("    " + pf + " - size(10.046, 32): " + ret);
            pf = "fail"; ret = st.size(6, 32); if(ret == 9) pf = "pass"; System.out.println("    " + pf + " - size(6, 32): " + ret);
            pf = "fail"; retBool = st.contains(30.398); if(retBool) pf = "pass"; System.out.println("    " + pf + " - contains(30.398): " + retBool);
            pf = "fail"; retBool = st.contains(32); if(!retBool) pf = "pass"; System.out.println("    " + pf + " - contains(32): " + retBool);
            pf = "fail"; retDbl = st.min(); if(retDbl == 3.002) pf = "pass"; System.out.println("    " + pf + " - min(): " + retDbl);
            pf = "fail"; retDbl = st.max(); if(retDbl == 60.333) pf = "pass"; System.out.println("    " + pf + " - max(): " + retDbl);
            pf = "fail"; retDbl = st.floor(30.398); if (retDbl == 27.277) pf = "pass"; System.out.println("    " + pf + " - floor(30.398): " + retDbl);
            pf = "fail"; retDbl = st.floor(10.046); if (retDbl == 5.192) pf = "pass"; System.out.println("    " + pf + " - floor(10.046): " + retDbl);
            pf = "fail"; retDbl = st.floor(45.454); if (retDbl == 35.556) pf = "pass"; System.out.println("    " + pf + " - floor(45.454): " + retDbl);
            pf = "fail"; retDbl = st.floor(3); if (retDbl == null) pf = "pass"; System.out.println("    " + pf + " - floor(3): " + retDbl);
            pf = "fail"; retDbl = st.floor(60.333); if (retDbl == 45.454) pf = "pass"; System.out.println("    " + pf + " - floor(60.333): " + retDbl);
            pf = "fail"; retDbl = st.ceiling(30.398); if (retDbl == 31.111) pf = "pass"; System.out.println("    " + pf + " - ceiling(30.398): " + retDbl);
            pf = "fail"; retDbl = st.ceiling(10.046); if (retDbl == 11.742) pf = "pass"; System.out.println("    " + pf + " - ceiling(10.046): " + retDbl);
            pf = "fail"; retDbl = st.ceiling(45.454); if (retDbl == 60.333) pf = "pass"; System.out.println("    " + pf + " - ceiling(45.454): " + retDbl);
            pf = "fail"; retDbl = st.ceiling(3); if (retDbl == 3.002) pf = "pass"; System.out.println("    " + pf + " - ceiling(3): " + retDbl);
            pf = "fail"; retDbl = st.ceiling(60.333); if (retDbl == null) pf = "pass"; System.out.println("    " + pf + " - ceiling(60.333): " + retDbl);
            pf = "fail"; ret = st.rank(3); if(ret == 0) pf = "pass"; System.out.println("    " + pf + " - rank(3): " + ret);
            pf = "fail"; ret = st.rank(10.046); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - rank(10.046): " + ret);
            pf = "fail"; ret = st.rank(15.983); if(ret == 5) pf = "pass"; System.out.println("    " + pf + " - rank(15.983): " + ret);
            pf = "fail"; ret = st.rank(27); if(ret == 8) pf = "pass"; System.out.println("    " + pf + " - rank(27): " + ret);
            pf = "fail"; ret = st.rank(30.398); if(ret == 9) pf = "pass"; System.out.println("    " + pf + " - rank(30.398): " + ret);
            pf = "fail"; ret = st.rank(31); if(ret == 10) pf = "pass"; System.out.println("    " + pf + " - rank(31): " + ret);
            pf = "fail"; ret = st.rank(45.454); if(ret == 12) pf = "pass"; System.out.println("    " + pf + " - rank(45.454): " + ret);
            pf = "fail"; ret = st.rank(60.333); if(ret == 13) pf = "pass"; System.out.println("    " + pf + " - rank(60.333): " + ret);
            pf = "fail"; retDbl = st.select(0); if(retDbl == 3.002) pf = "pass"; System.out.println("    " + pf + " - select(0): " + retDbl);
            pf = "fail"; retDbl = st.select(4); if(retDbl == 13.267) pf = "pass"; System.out.println("    " + pf + " - select(4): " + retDbl);
            pf = "fail"; retDbl = st.select(5); if(retDbl == 15.983) pf = "pass"; System.out.println("    " + pf + " - select(5): " + retDbl);
            pf = "fail"; retDbl = st.select(8); if(retDbl == 27.277) pf = "pass"; System.out.println("    " + pf + " - select(8): " + retDbl);
            pf = "fail"; retDbl = st.select(9); if(retDbl == 30.398) pf = "pass"; System.out.println("    " + pf + " - select(9): " + retDbl);
            pf = "fail"; retDbl = st.select(9); if(retDbl == 30.398) pf = "pass"; System.out.println("    " + pf + " - select(9): " + retDbl);
            pf = "fail"; retDbl = st.select(10); if(retDbl == 31.111) pf = "pass"; System.out.println("    " + pf + " - select(10.046): " + retDbl);
            pf = "fail"; retDbl = st.select(13); if(retDbl == 60.333) pf = "pass"; System.out.println("    " + pf + " - select(13): " + retDbl);
            pf = "fail"; retDbl = st.select(-1); if(retDbl == null) pf = "pass"; System.out.println("    " + pf + " - select(-1): " + retDbl);
            pf = "fail"; sz = st.size(); retDbl = st.min(); st.deleteMin(); retBool = st.contains(ret); if(!retBool && st.size() + 1 == sz) pf = "pass"; System.out.println("    " + pf + " - deleteMin(): " + st.toString());
            pf = "fail"; ret = st.height(); if(ret == 5) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 5) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; sz = st.size(); retDbl = st.max(); st.deleteMax(); retBool = st.contains(ret); if(!retBool && st.size() + 1 == sz) pf = "pass"; System.out.println("    " + pf + " - deleteMax(): " + st.toString());
            pf = "fail"; ret = st.height(); if(ret == 5) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 5) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; sz = st.size(); retDbl = 35.556; st.delete(retDbl); retBool = st.contains(ret); if(!retBool && st.size() + 1 == sz) pf = "pass"; System.out.println("    " + pf + " - delete(" + ret + "): " + st.toString());
            pf = "fail"; ret = st.height(); if(ret == 4) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 4) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; sz = st.size(); retDbl = 10.046; st.delete(retDbl); retBool = st.contains(ret); if(!retBool && st.size() + 1 == sz) pf = "pass"; System.out.println("    " + pf + " - delete(" + ret + "): " + st.toString());
            pf = "fail"; ret = st.height(); if(ret == 4) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 4) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; sz = st.size(); retDbl = 20.773; st.delete(retDbl); retBool = st.contains(ret); if(!retBool && st.size() + 1 == sz) pf = "pass"; System.out.println("    " + pf + " - delete(" + ret + "): " + st.toString());
            pf = "fail"; ret = st.height(); if(ret == 4) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 4) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; sz = st.size(); retDbl = 5.192; st.delete(retDbl); retBool = st.contains(ret); if(!retBool && st.size() + 1 == sz) pf = "pass"; System.out.println("    " + pf + " - delete(" + ret + "): " + st.toString());
            pf = "fail"; ret = st.height(); if(ret == 4) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 4) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; sz = st.size(); retDbl = 15.983; st.delete(retDbl); retBool = st.contains(ret); if(!retBool && st.size() + 1 == sz) pf = "pass"; System.out.println("    " + pf + " - delete(" + ret + "): " + st.toString());
            pf = "fail"; ret = st.height(); if(ret == 4) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 4) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; sz = st.size(); retDbl = 31.111; st.delete(retDbl); retBool = st.contains(ret); if(!retBool && st.size() + 1 == sz) pf = "pass"; System.out.println("    " + pf + " - delete(" + ret + "): " + st.toString());
            pf = "fail"; ret = st.height(); if(ret == 4) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 4) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; sz = st.size(); retDbl = 25.501; st.delete(retDbl); retBool = st.contains(retDbl); if(!retBool && st.size() + 1 == sz) pf = "pass"; System.out.println("    " + pf + " - delete(" + ret + "): " + st.toString());
            pf = "fail"; ret = st.height(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; sz = st.size(); retDbl = 30.398; st.delete(retDbl); retBool = st.contains(retDbl); if(!retBool && st.size() + 1 == sz) pf = "pass"; System.out.println("    " + pf + " - delete(" + ret + "): " + st.toString());
            pf = "fail"; ret = st.height(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; sz = st.size(); retDbl = 11.742; st.delete(retDbl); retBool = st.contains(ret); if(!retBool && st.size() + 1 == sz) pf = "pass"; System.out.println("    " + pf + " - delete(" + ret + "): " + st.toString());
            pf = "fail"; ret = st.height(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
        }
        else
        {
            Setdouble st = new Setdouble();
            System.out.println("Symbol table empty? " + st.isEmpty());
            System.out.println("Testing put() operation:");
            Scanner sc = new Scanner(new File(args[0]));
            while(sc.hasNext())
            {
                st.put(sc.nextDouble());
            }

            //System.out.println("    Contents: " + st.toString());
            System.out.println("Symbol table empty? " + st.isEmpty());
            System.out.println("");

            System.out.println("Testing get() operation:");
            System.out.println("");

            System.out.println("Testing delete() operation");
            int sz = st.size();
            st.delete(123);
            st.delete(60);
            //System.out.println("    Contents: " + st.toString());
            System.out.println("    Number of elements decreased by: " + (sz - st.size()));
            System.out.println("");

            System.out.println("Testing contains() operation");
            System.out.println("    Contains 123? " + st.contains(123));
            System.out.println("    Contains 60? " + st.contains(60));
            System.out.println("");

            System.out.println("Testing keys iterator:");
            int cnt = 0;
            for(double key : st.keys())
            {
                System.out.println("    " + key);
                cnt++;
                if(cnt > 4) break;
            }
            System.out.println("");
        }
    }

}