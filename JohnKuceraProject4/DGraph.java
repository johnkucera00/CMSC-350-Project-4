/*
* File: DGraph.java
* Author: John Kucera
* Date: March 3, 2020
* Purpose: This java program is meant to accompany P4GUI.java and is responsible
* for creating a Directed Graph using the class dependency information read from 
* the input text file. It also holds methods to perform a depth first search 
* algorithm on the directed graph and to return a string of the topological 
* order of a class recompilation.
*/

// import of necessary java classes
import java.util.*;
import java.io.*;

// DGraph generic class
public class DGraph<T> {
    
    // Instance Variables
    private ArrayList<LinkedList<Integer>> adjacencyList;
    private List<Integer> neighborsList;
    private Map<T, Integer> mapTtoInteger;
    private StringBuilder recompOrder;
    private int numVertices = 0;
    
    // Default Constructor
    public DGraph() {
        adjacencyList = new ArrayList<>();
        mapTtoInteger = new HashMap<>();
    }

    // Method to convert the contents of the file into tokens
    public ArrayList<String[]> tokenizeFile(String file) throws IOException {
        // Variables
        int i = 0;
        String currentLine;
        
        // Reading file
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        ArrayList<String[]> fileToArr = new ArrayList<>();
        
        // Convert classes in file into tokens, puts them into arraylist
        while ((currentLine = bufferedReader.readLine()) != null) {
            String[] lineArray = currentLine.split("\\s");
            fileToArr.add(i, lineArray);
            i++;
        }
        return fileToArr;
    } // end of method

    // Method to build Directed Graph from given file (after being tokenized)
    public void buildDGraphFromFile(ArrayList<T[]> tokenArrList) {
        for (T[] line : tokenArrList) {
            for (int i = 0; i < line.length; i++) {
                // Create vertex for each token (class)
                addVertex(line[i]);
                // Connects dependent class with edges
                if (i == 0) {
                    continue;
                }
                else {
                    addEdge(line[0], line[i]);
                } // end of else
            } // end of inner for
        } // end of outer for
    } // end of method
    
    // Method to add a vertex to graph
    private void addVertex(T vertex) {
        // map vertex to vertex number, do nothing if class is already in hashmap
        if (!mapTtoInteger.containsKey(vertex)) {
            mapTtoInteger.put(vertex, numVertices);
            // List for holding edges of vertex
            LinkedList<Integer> edgeHolder = new LinkedList<>();
            adjacencyList.add(numVertices, edgeHolder);
            numVertices++;
        } // end of if
    } // end of method
    
    // Method to connect two vertices with an edge
    private void addEdge(T vertexFrom, T vertexTo) {
        adjacencyList.get(mapTtoInteger.get(vertexFrom)).add(mapTtoInteger.get(vertexTo));
    } // end of method
    
    // Method which uses hashmap to return string of class given a vertex index
    private String getClass(int value) {
        for(T key : mapTtoInteger.keySet()) {
            if (mapTtoInteger.get(key).equals(value)) {
                String classString = key.toString();
                return classString;
            }
        }
        return "";
    } // end of method

    // Method which performs a depth-first search given a starting vertex index
    private void depthFirstSearch(int index) throws GraphCycleException {
        // add classStrings onto StringBuilder
        recompOrder.append(getClass(index)).append(" ");
        for (Integer i : adjacencyList.get(index)) {
            // if current is already in list of neighbors, there is a cycle
            if (neighborsList.contains(i)) {
                throw new GraphCycleException();
            }
            else {
                // Recursion to continue searching
                neighborsList.add(i);
                depthFirstSearch(i);
            } // end of else
        } // end of for
    } // end of method

    // Method to generate String of topological order of class recompilation
    public String topOrdGeneration(T startVertex) throws InvalidClassNameException, 
            GraphCycleException {
        // Variables
        recompOrder = new StringBuilder();
        neighborsList = new ArrayList<>();
        
        // If class name is not in hashmap, exception is thrown
        if (mapTtoInteger.get(startVertex) == null) {
            throw new InvalidClassNameException();
        }
        else {
            // Initiate depth-first search, return string of recompilation order
            depthFirstSearch(mapTtoInteger.get(startVertex));
            String recompOrderString = recompOrder.toString();
            return recompOrderString;
        } // end of else
    } // end of method
} // end of class
