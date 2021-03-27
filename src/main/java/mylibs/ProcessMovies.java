package mylibs;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.io.File;
import java.util.LinkedList;
import java.io.FileWriter;

import mylibs.RedBlackBST;
import mylibs.Bag;

// This class' primary function is to generate a file that contains a list of
// edges that contains the same information as movies.txt.

public class ProcessMovies
{
    public static void main(String[] args) throws FileNotFoundException, IOException
    {
        String filename = args[0];
        String delim = args[1];
        Scanner sc = new Scanner(new File(filename), "utf-8");
        FileWriter writer = new FileWriter(new File(".\\src\\main\\resources\\moviesStringEdges.txt"));
        RedBlackBST<String, Integer> index = new RedBlackBST<String, Integer>();
        Bag<String[]> lines = new Bag<String[]>();

        // iterate all through elements, get unique elements, store in a bag,
        // write edges to file
        int idx = 0;
        while(sc.hasNextLine())
        {
            String[] line = sc.nextLine().split(delim);
            lines.add(line);
            String movie = line[0];
            index.put(movie, idx++);
            for(int i = 1; i < line.length; i++)
            {
                writer.write(movie + delim + line[i] + "\n");
                index.put(line[i], idx++);
            }
        }
        sc.close();
        writer.close();

        // iterate through all lines again and write edges in integer form to file
        writer = new FileWriter(new File(".\\src\\main\\resources\\moviesIntEdges.txt"));
        for(String[] line : lines)
        {
            int idxMovie = index.get(line[0]);
            for(int i = 0; i < line.length; i++)
                writer.write(idxMovie + " " + index.get(line[i]) + "\n");
        }
        writer.close();
    }
}
