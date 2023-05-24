package src.orientdbbase.paris.metro;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.opencsv.exceptions.CsvException;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class requete1StationsProches {
	public static List<Vertex> stationsProches(double d, double e, OrientGraph graph) {
		
		long startTime = System.nanoTime();
		Double xStation;
		Double yStation;
		Double dist;
		Map<Vertex, Double> distance = new HashMap<Vertex, Double>();
		for (Vertex v : (Iterable<Vertex>) graph.command(new OCommandSQL("SELECT FROM Station")).execute()) {
			xStation = v.getProperty("X");
			yStation = v.getProperty("Y");
			dist = Math.sqrt(Math.pow((d - xStation), 2) + Math.pow((e - yStation), 2));
			distance.put(v, dist);
		}
		List<Vertex> keys = distance.entrySet().stream().sorted(Map.Entry.<Vertex,Double>comparingByValue()).limit(3).map(Map.Entry::getKey).collect(Collectors.toList());
	
		graph.shutdown();
		long elapsedTime = System.nanoTime() - startTime;
		System.out.println("Total execution time to create 1000K objects in Java in millis: "
	                + elapsedTime/1000000);
		return keys;	
	}

	public static void main(String[] args) throws CsvException, FileNotFoundException, IOException {
		OrientGraph graph = new OrientGraph("remote:localhost/Paris_Metro", "root", "root");
		List<Vertex> keys=stationsProches(652109.5385, 6861853.2270, graph);
		for (Vertex i : keys) {
			String name = i.getProperty("nom");
            // Print all elements of ArrayList
            System.out.println(name);
        }
}}