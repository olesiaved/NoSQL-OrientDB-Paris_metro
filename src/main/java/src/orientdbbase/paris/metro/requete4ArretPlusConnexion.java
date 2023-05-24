package src.orientdbbase.paris.metro;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class requete4ArretPlusConnexion {
	public static List<Vertex> stationPlusConnexion(OrientGraph graph) {
		long startTime = System.nanoTime();
		List<Vertex> maxConnection = new ArrayList<Vertex>();
		int nbMaxConnection = 2;
		int connection = 0;
		String lines;
		
		for (Vertex v : (Iterable<Vertex>) graph.command(new OCommandSQL("SELECT FROM Station")).execute()) {
			lines = v.getProperty("Lines");
			connection = lines.split("\\.").length;
			if (nbMaxConnection < connection) {
				nbMaxConnection = connection;
				maxConnection.clear();
				maxConnection.add(v);
			} else {
				if (nbMaxConnection == connection) {
					maxConnection.add(v);
				}
			}
			for (Vertex v1 : maxConnection) {
			}
		}
		graph.shutdown();
		long elapsedTime = System.nanoTime() - startTime;
		System.out.println("Total execution time in Java in millis: " + elapsedTime / 1000000);
		return maxConnection;

	}

	public static void main(String[] args) {
		OrientGraph graph = new OrientGraph("remote:localhost/Paris_Metro", "root", "root");
		List<Vertex> result = stationPlusConnexion(graph);
		for (Vertex v : result) {
			String name = v.getProperty("nom");
			System.out.println(name);
		}
	}
}
