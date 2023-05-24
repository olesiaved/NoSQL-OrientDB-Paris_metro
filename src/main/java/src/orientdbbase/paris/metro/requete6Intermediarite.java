package src.orientdbbase.paris.metro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.tinkerpop.gremlin.jsr223.ConcurrentBindings;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;

import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.graph.gremlin.OCommandGremlin;
import com.orientechnologies.orient.graph.gremlin.OGremlinHelper;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class requete6Intermediarite {
	public static String number(Vertex v) {
		String num = v.toString();
		String n = num.substring(num.indexOf("#"));
		n.trim();
		n = n.substring(0, n.length() - 1);
		return n;
	}

	public static void mostImportantnode(OrientGraph g) {
		long startTime = System.nanoTime();
		Iterable<Vertex> result = g.command(new OSQLSynchQuery<Vertex>("SELECT FROM Station")).execute();
		List<Vertex> listaVertex = new ArrayList<Vertex>();
		CollectionUtils.addAll(listaVertex, result.iterator());
		String name;
		
		Map<String, Integer> shortest = new HashMap<String, Integer>();
		for (int v = 0; v < listaVertex.size(); v++) {
			for (int u = v; u < listaVertex.size(); u++) {
				String nameV = listaVertex.get(v).getProperty("nom");
				String nameU = listaVertex.get(v).getProperty("nom");
				String V1 = number(listaVertex.get(v)); // Vertex A
				String V2 = number(listaVertex.get(u)); // Vertex D
				String s = "select expand(shortestPath(" + V1 + ", " + V2 + "))";
				result = g.command(new OSQLSynchQuery<Vertex>(s)).execute();
				List<Vertex> listaVertexShortPath = new ArrayList<Vertex>();
				CollectionUtils.addAll(listaVertexShortPath, result.iterator());
	
				for (int k = 0; k < listaVertexShortPath.size(); k++) {
					int count = shortest.containsKey(listaVertexShortPath.get(k).getProperty("nom"))
							? shortest.get(listaVertexShortPath.get(k).getProperty("nom"))
							: 0;
					shortest.put(listaVertexShortPath.get(k).getProperty("nom"), count + 1);
				}

			}

		}

		shortest.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach(System.out::println);
		long elapsedTime = System.nanoTime() - startTime;
		System.out.println("Total execution time in Java in seconds: " + elapsedTime / (1000000*1000));
		g.shutdown();
	}

	public static void main(String[] args) {
		OrientGraph g = new OrientGraph("remote:localhost/Paris_Metro", "root", "root");
		mostImportantnode(g);
	}

}
