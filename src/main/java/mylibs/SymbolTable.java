// Interface for all symbol tables
package mylibs;

public interface SymbolTable<Key extends Comparable<Key>, Value>
{
    // operations
    public void put(Key key, Value val);            // insert operation
    public Value get(Key key);                      // search and return value
    public void delete(Key key);                    // delete operation
    public boolean contains(Key key);               // search
    public Key min();                               // return least entry
    public Key max();                               // return greatest entry
    public Key floor(Key key);                      // return greatest key that is less than key passed in
    public Key ceiling(Key key);                    // return smallest key that is greater than key passed in
    public int rank(Key key);                       // return number of keys less than key passed in
    public Key select(int k);                       // return the key that has k keys less than it
    public void deleteMin();                        // remove the smallest key
    public void deleteMax();                        // remove the largest key
    public int size(Key from, Key to);              // return number of keys between from and to, inclusive
    public int heightCompute();                     // return height of tree by examining every element

    // convenience
    public boolean isEmpty();                       // check if count of entries is 0
    public int size();                              // return number of entries
    public int height();                            // return height of tree
    public String toString();                       // return (key, value) of all entries as a string
    public Iterable<Key> keys();                    // return an Iterable<Key> type
    public Iterable<Key> keys(Key from, Key to);    // return an Iterable<Key> type of all entries between from and to, inclusive
}
