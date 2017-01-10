package a4q2;

//Yingnan Zhao 260563769
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.Set;

public class ShapeGraph extends Graph<Shape> {

	public ShapeGraph() {
	}

	public void resetVisited() {
		Set<String> vertexKeySet = vertexMap.keySet();
		for (String key : vertexKeySet) {
			vertexMap.get(key).visited = false;
		}
	}

	/**
	 * Returns a list of lists, each inner list is a path to a node that can be
	 * reached from a given node if the total area along the path to that node
	 * is greater than the threshold. Your solution must be a recursive, depth
	 * first implementation for a graph traversal. The Strings in the returned
	 * list of lists should be the vertex labels (keys).
	 */
	/*
	 * created a slave list to store every output strings every time the
	 * tranverseFrom method is called it clears the slave and master list and
	 * set the area to 0, then call the helper method to do the recursion
	 */
	LinkedList<String> slaveList = new LinkedList<>();
	LinkedList<LinkedList<String>> masterList_temp = new LinkedList<>();
	float area = 0;

	public LinkedList<LinkedList<String>> traverseFrom(String key, float threshold) {
		LinkedList<LinkedList<String>> masterList = new LinkedList<>();
		// ADD YOUR CODE HERE. (IF YOU WISH TO ADD A HELPER METHOD, THEN ADD IT
		// AFTER THIS METHOD.)
		
		slaveList.clear();
		masterList_temp.clear();
		area = 0;
		resetVisited();
		masterList=traverse(key, threshold);
		return masterList;

	}

	/*
	 * helper method i used a recursion to iterate through the graph(depth
	 * first), every iteration I first add the key into the slaveList (add last)
	 * that i created to store the path, and increase the area, the method will
	 * visit the element's adjList, and the vetrex's adjlist, etc. and check if
	 * the total area is greater than the threshold, then the vertex I am
	 * visiting has no adjacent list I pop it out and decrease the area, if the
	 * vertex's adjacent list has all been visited, I pop it out from the
	 * slavelist and decrease the area the temp list was created to create space
	 * for adding element the element in the slave list
	 */
	public LinkedList<LinkedList<String>> traverse(String key, float threshold) {
		LinkedList<String> temp = new LinkedList<>();
		Shape a;
		float b = 0;
		slaveList.addLast(key);
		vertexMap.get(key).visited = true;
		a = (Shape) vertexMap.get(key).element;
		b = a.getArea();
		area = area + b;
		if (area > threshold) {

			masterList_temp.addLast(temp);
			for (String i : slaveList) {
				masterList_temp.getLast().addLast(i);
			}
		}

		if (!vertexMap.get(key).adjList.isEmpty()) {
			for (Edge i : vertexMap.get(key).adjList) {

				if (i.getEndVertex().visited == false) {

					traverse(i.getEndVertex().key, threshold);
				}

			}

			slaveList.removeLast();
			area = area - b;
		} else {
			slaveList.removeLast();
			area = area - b;
		}

		return masterList_temp;
	}
}
