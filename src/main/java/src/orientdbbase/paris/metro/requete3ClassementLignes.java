package src.orientdbbase.paris.metro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientDynaElementIterable;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

public class requete3ClassementLignes {
	public static Map<String, Integer> ClassementLignes(OrientGraph graph) {
		long startTime = System.nanoTime();
		String s;
		Map<String, Integer> classement = new HashMap<String, Integer>();
		for (Vertex v : (Iterable<Vertex>) graph.command(new OCommandSQL("SELECT FROM Station ")).execute()) {
			s =  v.getProperty("Lines");
			String[] lines = s.split("\\.");
			List<String> stringList = new ArrayList<String>(Arrays.asList(lines)); 
			for (String i : stringList) {
				int count = classement.containsKey(i) ? classement.get(i) : 0;
				classement.put(i, count + 1);
			}
		}
		graph.shutdown();
		long elapsedTime = System.nanoTime() - startTime;
		System.out.println("Total execution time to create 1000K objects in Java in millis: " + elapsedTime / 1000000);
		return classement;
	}

	public static void main(String[] args) {
		OrientGraph graph = new OrientGraph("remote:localhost/Paris_Metro", "root", "root");
		ClassementLignes(graph).entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach(System.out::println);
	}
}
