/*
* File: InvalidClassNameException.java
* Author: John Kucera
* Date: March 3, 2020
* Purpose: This java program is meant to accompany P4GUI.java. It is a user
* defined checked exception that handles situations where class name input
* is invalid due to having white space or not matching a class name in the
* respective read text file.
*/

// Constructor
public class InvalidClassNameException extends Exception {
    public InvalidClassNameException() {
        super();
    }
}
