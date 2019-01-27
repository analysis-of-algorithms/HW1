/*
 * File: Match.java
 * Author: Daria Chaplin / dxc4643
 * Date: 1-20-19
 */

import java.util.*;

public class Match {
    public static void main(String[] args) {
	
	// Set up scanner and initialize preference matrices
	Scanner scan = new Scanner(System.in);
	int numElements = scan.nextInt();
	int[][] groupOne = new int[numElements][numElements];
	int[][] groupTwo = new int[numElements][numElements];
	String result = "NO";

	// Scan group one's preferences into matrix
	for(int i = 0; i < numElements; i++) {
	    for(int j = 0; j < numElements; j++) {
		groupOne[i][j] = scan.nextInt();
	    }
	}
	
	// Scan group two's preferences into matrix
	for(int i = 0; i < numElements; i++) {
	    for(int j = 0; j < numElements; j++) {
		groupTwo[i][j] = scan.nextInt();
	    }
	}

	// Check for different stable matching groups- swap optimal group
	int matchOne[] = galeShapley(numElements, groupOne, groupTwo);
	int matchTwo[] = galeShapley(numElements, groupTwo, groupOne);
	int tempIndex;

	// One set of returned pairs is inverted to adjust for the swap
	for(int i = 0; i < numElements; i++) {
	    tempIndex = matchOne[i];
	    if(matchTwo[tempIndex] != i) { result = "YES"; }
	}

	System.out.println(result + "\n");
    }

    private static int[] galeShapley
	    (int numElements, int[][] professors, int[][] students) {
	
	// Initialize arrays of matches where every person is free
	// Set up stack of free professors, count of their asks
	int[] partnerStudent = new int[numElements];
	int[] partnerProf = new int[numElements];	
	Stack<Integer> freeProfs = new Stack<Integer>();
	int[] askCount = new int[numElements];

	for(int i = 0; i < numElements; i++) {
	    partnerStudent[i] = -1;
	    partnerProf[i] = -1;
	    freeProfs.push((numElements - 1) - i);
	    askCount[i] = 0;
	}

	// Create inverse of students' initial preference list
	// For each student (outer loop),
	// Their preferences and rankings are swapped (inner loop)
	int[][] inverseRank = new int[numElements][numElements];
	for(int i = 0; i < numElements; i++) {
	    for (int j = 0; j < numElements; j++) {
		inverseRank[i][students[i][j]] = j;
	    }
	}

	// Complete each cycle of a free professor asking a student
	while(!freeProfs.empty()) {
	    // Access first free professor and their first unasked student
	    int prof = freeProfs.pop();
	    int student = professors[prof][askCount[prof]];
	    int profCurrent = partnerProf[student];

	    // Complete asking phase for given professor and student
	    if(partnerProf[student] == -1) {
		// Assign the free student to the professor
		partnerProf[student] = prof;
		partnerStudent[prof] = student;
	    } else if(inverseRank[student][prof] <
			    inverseRank[student][profCurrent]) {
		// Free their currently assigned professor
		partnerStudent[profCurrent] = -1;
		freeProfs.push(profCurrent);
		// Assign the newly matched prof/student pair
		partnerProf[student] = prof;
		partnerStudent[prof] = student;
	    } else {
		// Match failed, keep professor free
		freeProfs.push(prof);
	    }
	    
	    askCount[prof]++;
	}

	return partnerStudent;
    }
}
