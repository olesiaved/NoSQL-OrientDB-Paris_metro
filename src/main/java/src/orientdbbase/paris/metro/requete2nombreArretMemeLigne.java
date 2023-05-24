package src.orientdbbase.paris.metro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.db.object.ODatabaseObject;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.orientechnologies.orient.object.db.OrientDBObject;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.impls.orient.OrientDynaElementIterable;
import com.tinkerpop.blueprints.impls.orient.OrientEdge;

public class requete2nombreArretMemeLigne {
	public static void nbArret(String depart, String arrivee, OrientGraph graph) {
		long startTime = System.nanoTime();
		Iterable<Vertex> result = graph.command(new OSQLSynchQuery<Vertex>("SELECT FROM Station")).execute();
		List<Vertex> listaVertex = new ArrayList<Vertex>();
		CollectionUtils.addAll(listaVertex, result.iterator());
		String name = null;
		Vertex departVertex = null;
		Vertex arriveeVertex = null;
		int numberarr = -1;
		//Getting the vertex from the vertex list 
		for (int v = 0; v < listaVertex.size(); v++) {
			name = listaVertex.get(v).getProperty("nom");
			if (name.equals(depart)) {
				departVertex = listaVertex.get(v);
			}
			if (name.equals(arrivee)) {
				arriveeVertex = listaVertex.get(v);
			}
		}
		String s1 = arriveeVertex.getProperty("Lines");
		String[] lines1 = s1.split("\\.");
		String s2 = departVertex.getProperty("Lines");
		String[] lines2 = s2.split("\\.");
		String lineNumber = null;
		//finding the common line 
		for (int i = 0; i < lines2.length; i++) {
			if (lines2[i] != "") {
				boolean contains = Arrays.stream(lines1).anyMatch(lines2[i]::equals);
				if (contains) {
					lineNumber = lines2[i];
					break;
				}
			}
		}
		System.out.println("the line is " + lineNumber);
		boolean end = false;
		int count = 0;
		Vertex minus10 = arriveeVertex;
		Vertex minus11 = arriveeVertex;
		List<Vertex> firstNeighbors = findNextOnLine1(graph, arriveeVertex, lineNumber);
		//traversing the graph common line to find the arrive node
		if (!firstNeighbors.contains(departVertex)) {
			while (!end) {
				List<Vertex> firstWay = findNextOnLine2(graph, firstNeighbors.get(0), minus10, lineNumber);
				List<Vertex> secondWay = findNextOnLine2(graph, firstNeighbors.get(1), minus11, lineNumber);
				minus10 = firstNeighbors.get(0);
				minus11 = firstNeighbors.get(1);
				firstNeighbors.clear();
				firstNeighbors.add(firstWay.get(0));
				firstNeighbors.add(secondWay.get(0));
				if (firstNeighbors.contains(departVertex)) {
					end = true;
				}
				count++;
			}
		} else {
			System.out.println("the stations are neighbors");
		}
		System.out.println("the number of stations between is "+ count);
		long elapsedTime = System.nanoTime() - startTime;
		System.out.println("Total execution time in Java in millis: " + elapsedTime / 1000000);
		graph.shutdown();
	}
	public static ArrayList<Vertex> findNextOnLine1(OrientGraph graph, Vertex ver, String lineName) {
		String numArrivee = number(ver);
		Iterable<Vertex> result2 = graph.command(new OSQLSynchQuery<Vertex>("SELECT expand(in()) FROM " + numArrivee))
				.execute();
		List<Vertex> listaVertexResult = new ArrayList<Vertex>();
		CollectionUtils.addAll(listaVertexResult, result2.iterator());
		ArrayList<Vertex> sameLine = new ArrayList<Vertex>();
		for (int v = 0; v < listaVertexResult.size(); v++) {
			String lines = listaVertexResult.get(v).getProperty("Lines");
			String name = listaVertexResult.get(v).getProperty("nom");
			if (lines.contains(lineName)) {
				if (!sameLine.contains(listaVertexResult.get(v))) {
					sameLine.add(listaVertexResult.get(v));
					
				}
			}
		}
		return sameLine;

	}

	public static List<Vertex> findNextOnLine2(OrientGraph graph, Vertex ver, Vertex ver0, String lineName) {
		ArrayList<Vertex> sameline = findNextOnLine1(graph, ver, lineName);
		for (int v = 0; v < sameline.size(); v++) {
			if (sameline.get(v).equals(ver0)) {
				sameline.remove(v);
			}
		}
		return sameline;

	}
	public static String number(Vertex v) {
		String num = v.toString();
		String n = num.substring(num.indexOf("#"));
		n.trim();
		n = n.substring(0, n.length() - 1);
		return n;
	}

	public static void main(String[] args) {
		OrientGraph graph = new OrientGraph("remote:localhost/Paris_Metro", "root", "root");
		nbArret("PORTEDAUPHINE", "PIGALLE", graph);
	}
}