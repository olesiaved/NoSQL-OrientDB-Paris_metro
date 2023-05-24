package src.orientdbbase.paris.metro;

import java.io.IOException;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.orientechnologies.orient.client.remote.OServerAdmin;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

public class requete5DeuxAmisSeRejoinent {
	public static void showPath(String depart, String arrivee, OrientGraphNoTx g) {
		long startTime = System.nanoTime();
		Iterable<Vertex> result = g.command(new OSQLSynchQuery<Vertex>("SELECT FROM Station")).execute();
		List<Vertex> listaVertex = new ArrayList<Vertex>();
		CollectionUtils.addAll(listaVertex, result.iterator());
		String name = null;
		Vertex v1 = null;
		Vertex v2 = null;
		for (int v = 0; v < listaVertex.size(); v++) {
			name = listaVertex.get(v).getProperty("nom");
			if (name.equals(depart)) {
				v1 = listaVertex.get(v);
			}
			if (name.equals(arrivee)) {
				v2 = listaVertex.get(v);
			}
		}
		String V1 = number(v1); // Vertex A
		String V2 = number(v2); // Vertex D
		String s = "select expand(shortestPath(" + V1 + ", " + V2 + "))";
		result = g.command(new OSQLSynchQuery<Vertex>(s)).execute();
		listaVertex.clear();
		CollectionUtils.addAll(listaVertex, result.iterator());
		List<String> edgePath = new ArrayList<String>();
		String singleIn = "";
		String singleOut = "";

		System.out.println("SHORTEST PATH (vertex):");
		for (int v = 0; v < listaVertex.size(); v++) {
			System.out.print(listaVertex.get(v).getProperty("nom") + " ");
		}
		System.out.println("");
		int stationChoice=Integer.valueOf(Math.round(listaVertex.size()/2));
		System.out.println("The Station to meet is "+listaVertex.get(stationChoice).getProperty("nom"));
		long elapsedTime = System.nanoTime() - startTime;
		System.out.println("Total execution time to create 1000K objects in Java in millis: " + elapsedTime / 1000000);
		g.shutdown();
	}
	public static void main(String[] args) throws IOException {
		OrientGraphNoTx g = new OrientGraphFactory("remote:localhost/Paris_Metro_Small", "root", "root").getNoTx();
		showPath("CHATELET", "PORTEDAUPHINE", g);
	}

	public static String number(Vertex v) {
		String num = v.toString();
		String n = num.substring(num.indexOf("#"));
		n.trim();
		n = n.substring(0, n.length() - 1);
		return n;
	}
}
