package src.orientdbbase.paris.metro;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

public class subgraph {
	public static void FillingVertex() {

		OrientGraph graph1 = new OrientGraph("remote:localhost/Paris_Metro", "root", "root");
		OrientGraph graph2 = new OrientGraph("remote:localhost/Paris_Metro_Small", "root", "root");
		List<Vertex> subgraph = new ArrayList<Vertex>();
		for (int i = 1; i < 8; i++) {
			for (Vertex v : (Iterable<Vertex>) graph1
					.command(new OCommandSQL("SELECT FROM Station where Lines like '%." + i + ".%'")).execute()) {
				subgraph.add(v);
			}
		}
		for (int v = 0; v < subgraph.size(); v++) {
			Vertex u = subgraph.get(v);
			String nom = u.getProperty("nom");
			Double X = u.getProperty("X");
			Double Y = u.getProperty("Y");
			String lines = u.getProperty("Lines");
			lines = lines.replace(".7bis.", "");
			lines = lines.replace(".8.", "");
			lines = lines.replace(".9.", "");
			lines = lines.replace(".10.", "");
			lines = lines.replace(".11.", "");
			lines = lines.replace(".12.", "");
			lines = lines.replace(".13.", "");
			lines = lines.replace(".14.", "");
			lines = lines.replace(".3bis.", "");
			graph2.command(new OCommandSQL(
					"CREATE Vertex Station set nom='" + nom + "', X=" + X + ", Y=" + Y + ", Lines='" + lines + "'"))
					.execute();
			System.out.println(nom);

		}
		graph1.shutdown();
		graph2.shutdown();
	}

	public static String number(Vertex v) {
		String num = v.toString();
		String n = num.substring(num.indexOf("#"));
		n.trim();
		n = n.substring(0, n.length() - 1);
		return n;
	}

	public static void main(String[] args) {
		OrientGraph g = new OrientGraph("remote:localhost/Paris_Metro_Small", "root", "root");
		Iterable<Vertex> result = g.command(new OSQLSynchQuery<Vertex>("SELECT FROM Station")).execute();
		List<Vertex> listaVertex = new ArrayList<Vertex>();
		CollectionUtils.addAll(listaVertex, result.iterator());
		OrientGraph graph = new OrientGraph("remote:localhost/Paris_Metro", "root", "root");
		for (Vertex v1 : (Iterable<Vertex>) graph.command(new OCommandSQL(
				"SELECT FROM Station where (Lines LIKE '%.1.%' OR Lines LIKE '%.2.%' OR Lines LIKE '%.3.%' OR Lines LIKE '%.4.%' OR Lines LIKE '%.6.%' OR Lines LIKE '%.5.%' OR Lines LIKE '%.7.%')"))
				.execute()) {
			for (Vertex v2 : (Iterable<Vertex>) graph.command(new OCommandSQL(
					"SELECT FROM Station where (Lines LIKE '%.1.%' OR Lines LIKE '%.2.%' OR Lines LIKE '%.3.%' OR Lines LIKE '%.4.%' OR Lines LIKE '%.5.%' OR Lines LIKE '%.6.%' OR Lines LIKE '%.7.%')"))
					.execute()) {
				String name1 = v1.getProperty("nom");
				String name2 = v2.getProperty("nom");
				OrientVertex vOrient1 = graph.getVertex(number(v1));
				OrientVertex vOrient2 = graph.getVertex(number(v2));
				Iterable<Edge> edgebetween = vOrient2.getEdges(vOrient1, Direction.IN);
				List<Edge> edgeList = new ArrayList<Edge>();
				CollectionUtils.addAll(edgeList, edgebetween.iterator());
				boolean test = false;
				if (name2.equals("STALINGRAD") && name1.equals("LOUISBLANC")) {
					System.out.println("here");
					test = true;
				}
				if (edgebetween != null) {
					if (test) {
						System.out.println("here1");
					}
					for (Vertex v1Small : (Iterable<Vertex>) g
							.command(new OCommandSQL("SELECT FROM Station where nom='" + name1 + "'")).execute()) {
						if (test) {
							System.out.println("here2");
						}
						for (Vertex v2Small : (Iterable<Vertex>) g
								.command(new OCommandSQL("SELECT FROM Station where nom='" + name2 + "'")).execute()) {
							if (test) {
								System.out.println("here3");
							}
							for (Edge e : edgeList) {
								if (test) {
									System.out.println("here4");
								}
								String ligne = e.getProperty("ligne");
								if (name2.equals("STALINGRAD") && name1.equals("LOUISBLANC")) {
									System.out.println("here5");
									System.out.println(ligne);
								}
								if ((ligne.equals("1")) || (ligne.equals("2")) || (ligne.equals("3"))
										|| (ligne.equals("4")) || (ligne.equals("5")) || (ligne.equals("6"))
										|| (ligne.equals("7"))) {
									g.command(new OCommandSQL("CREATE EDGE Liaison from" + number(v1Small) + " to "
											+ number(v2Small) + " SET ligne='" + ligne + "'")).execute();
								}
							}
						}
					}
				}
			}
		}
		graph.shutdown();
		g.shutdown();
	}
}
