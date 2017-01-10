package a4q2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MarkingTesterGraph {

	public static void main(String[] args){
		StringBuilder report = new StringBuilder();
		double finalGrade = 30;
		ShapeGraph graph = new ShapeGraph();

		ArrayList<Vertex<Shape>> listOfVertices = new ArrayList<Vertex<Shape>>();
		listOfVertices.add( new Vertex<Shape>("1",   new Square(1) ) );
		listOfVertices.add( new Vertex<Shape>("2", new Square(2) ));
		listOfVertices.add( new Vertex<Shape>("3", new Square(3)));
		listOfVertices.add( new Vertex<Shape>("4", new Square(4)));  // Triangle(10, 25)));
		listOfVertices.add( new Vertex<Shape>("5", new Square(5)));  // Circle(6)));
		listOfVertices.add( new Vertex<Shape>("6", new Square(6)));
		listOfVertices.add( new Vertex<Shape>("7", new Square(7)));
		listOfVertices.add( new Vertex<Shape>("8", new Square(8)));

		for (int i = 0; i < listOfVertices.size(); i++){
			graph.addVertex( listOfVertices.get(i).getKey(), listOfVertices.get(i) );
		}

		graph.addEdge("1", "2");
		graph.addEdge("1", "3");
		graph.addEdge("2", "4");
		graph.addEdge("2", "5");
		graph.addEdge("2", "6");
		graph.addEdge("5", "8");
		graph.addEdge("3", "7");

		//testing a tree, no area condition
		LinkedList<LinkedList<String>> result;
		result = graph.traverseFrom("1", 0);
		LinkedList<String[]> trueFromOne = new LinkedList<>();
		trueFromOne.add(new String[]{"1"});
		trueFromOne.add(new String[]{"1","2"});
		trueFromOne.add(new String[]{"1","2","4"});
		trueFromOne.add(new String[]{"1","2","5"});
		trueFromOne.add(new String[]{"1","2","5","8"});
		trueFromOne.add(new String[]{"1","2","6"});
		trueFromOne.add(new String[]{"1","3"});
		trueFromOne.add(new String[]{"1","3","7"});

		if(result.size() != trueFromOne.size()){
			if(result.size() < trueFromOne.size()){
				report.append("-7.5 Returned to few paths for a tree, with no area condition, i.e. all reachable vertex's\n");
				finalGrade -= 7.5;
			}
		}else{
			for(LinkedList<String> list : result){
				contains(trueFromOne, list);
			}
			if(!trueFromOne.isEmpty()){
				report.append("-7.5 Returned incorrect paths for a tree, with no area condition, i.e. all reachable vertex's\n");
				finalGrade -= 7.5;
			}
		}

		//Testing a tree with an area condition
		result = graph.traverseFrom("1", 31);
		trueFromOne = new LinkedList<>();
		trueFromOne.add(new String[]{"1","2","5","8"});
		trueFromOne.add(new String[]{"1","2","6"});
		trueFromOne.add(new String[]{"1","3","7"});
		if(result.size() != trueFromOne.size()){
			if(result.size() > trueFromOne.size()){
				report.append("-7.5 Returned to many paths for a tree with an area condition\n");
			}else{
				report.append("-7.5 Returned to few paths for a tree with an area condition\n");
			}
			finalGrade -= 7.5;
		}else{
			for(LinkedList<String> list : result){
				contains(trueFromOne, list);
			}
			if(!trueFromOne.isEmpty()){
				report.append("-7.5 Returned incorrect paths for a tree with an area condition\n");
				finalGrade -= 7.5;
			}
		}
		//Testing a graph with multiple paths, no loops
		graph = new ShapeGraph();
		for (int i = 0; i < listOfVertices.size(); i++){
			graph.addVertex( listOfVertices.get(i).getKey(), listOfVertices.get(i) );
		}

		graph.addEdge("1", "2");
		graph.addEdge("1", "3");
		graph.addEdge("2", "4");
		graph.addEdge("2", "5");
		graph.addEdge("2", "6");
		graph.addEdge("5", "8");
		graph.addEdge("3", "7");
		graph.addEdge("3", "6");
		graph.addEdge("7", "8");
		result = graph.traverseFrom("1", 0);
		trueFromOne = new LinkedList<>();
		trueFromOne.add(new String[]{"1"});
		trueFromOne.add(new String[]{"1","2"});
		trueFromOne.add(new String[]{"1","2","4"});
		trueFromOne.add(new String[]{"1","2","5"});
		trueFromOne.add(new String[]{"1","2","5","8"});
		trueFromOne.add(new String[]{"1","2","6"});
		trueFromOne.add(new String[]{"1","3"});
		trueFromOne.add(new String[]{"1","3","7"});
		if(result.size() != trueFromOne.size()){
			if(result.size() > trueFromOne.size()){
				report.append("-7.5 Returned to many paths for a graph with multiple paths\n");
				finalGrade -= 7.5;
			}else{
				report.append("-7.5 Returned to few paths for a graph with multiple paths\n");
				finalGrade -= 7.5;
			}
		}else{
			for(LinkedList<String> list : result){
				contains(trueFromOne, list);
			}
			if(!trueFromOne.isEmpty()){
				report.append("-7.5 Returned incorrect paths for a graph with multiple paths\n");
				finalGrade -= 7.5;
			}
		}

		//Check for loops
		graph = new ShapeGraph();
		trueFromOne = new LinkedList<>();
		trueFromOne.add(new String[]{"1"});
		trueFromOne.add(new String[]{"1", "2"});
		trueFromOne.add(new String[]{"1", "2", "3"});
		trueFromOne.add(new String[]{"1", "2", "3", "4"});
		graph.addVertex("1", new Vertex<Shape>("1",   new Square(1) ) );
		graph.addVertex("2", new Vertex<Shape>("2", new Square(2) ));
		graph.addVertex("3", new Vertex<Shape>("3", new Square(3)));
		graph.addVertex("4", new Vertex<Shape>("4", new Square(4)));
		graph.addEdge("1", "2");
		graph.addEdge("2", "3");
		graph.addEdge("3", "4");
		graph.addEdge("4", "1");
		boolean crashed = false;
		try{
			result = graph.traverseFrom("1", 0);
		}catch(StackOverflowError e){
			crashed = true;
		}catch (Exception e) {
			crashed = true;
		}
		if(!crashed){
			if(result.size() != trueFromOne.size()){
				if(result.size() > trueFromOne.size()){
					report.append("-7.5 Returned to many paths for a graph with a loop\n");
					finalGrade -= 7.5;
				}else{
					report.append("-7.5 Returned to few paths for a graph with a loop\n");
					finalGrade -= 7.5;
				}
			}else{
				for(LinkedList<String> list : result){
					contains(trueFromOne, list);
				}
				if(!trueFromOne.isEmpty()){
					report.append("-7.5 Returned incorrect paths for a graph with a loop\n");
					finalGrade -= 7.5;
				}
			}
		}else{
			report.append("-7.5 Crashed for a graph with a loop, possibly an infinite loop\n");
			finalGrade -= 7.5;
		}

		Field[] declaredFields = ShapeGraph.class.getDeclaredFields();
		for (Field field : declaredFields) {
			System.out.println(field.getName());
		    if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
		        report.append("-5 Used a static variable\n");
		        finalGrade -= 5;
		    }
		}

		if(report.length() == 0){
			report.append("All Tests Passed\n");
		}
		report.append("\nTotal Raw Grade: " + finalGrade + "/30");
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("Report.txt")));
			writer.write(report.toString());
			writer.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
//		System.out.println(report);
//		System.out.println("\nFinal Grade: " + finalGrade + "/30");
	}

	private static void contains(LinkedList<String[]> trueFromOne, LinkedList<String> list) {
		LinkedList<String[]> removeList = new LinkedList<>();
		for(String[] s : trueFromOne){
			if(matches(s, list)){
				removeList.add(s);
			}
		}
		trueFromOne.removeAll(removeList);
	}

	private static boolean matches(String[] string, LinkedList<String> list) {
		if(string.length != list.size()){
			return false;
		}
		for(int i = 0; i < string.length; i++){
			if(string[i] != list.get(i)){
				return false;
			}
		}
		return true;
	}
}
