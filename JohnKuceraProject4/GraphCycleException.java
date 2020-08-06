/*
* File: GraphCycleException.java
* Author: John Kucera
* Date: March 3, 2020
* Purpose: This java program is meant to accompany P4GUI.java. It is a user
* defined checked exception that handles situations where there is a cycle 
* detected in a directed graph created from the input text file.
*/

// Constructor
public class GraphCycleException extends Exception {
    public GraphCycleException() {
        super();
    }
}
