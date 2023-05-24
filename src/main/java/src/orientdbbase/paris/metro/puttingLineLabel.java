package src.orientdbbase.paris.metro;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opencsv.exceptions.CsvException;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

public class puttingLineLabel {
	public static String number(Vertex v) {
	    String num=v.toString();
	    String n=num.substring(num.indexOf("#"));
	    n.trim();
	    n=n.substring(0, n.length() - 1);
	    return n;
	}
	public static void main(String[] args) throws CsvException, FileNotFoundException, IOException {

		OrientGraph graph = new OrientGraph("remote:localhost/Paris_Metro", "root", "root");
		for (Vertex v1 : (Iterable<Vertex>) graph.command(new OCommandSQL("SELECT FROM Station")).execute()) {
			for (Vertex v2 : (Iterable<Vertex>) graph.command(new OCommandSQL("SELECT FROM Station")).execute()) {
				OrientVertex vOrient1=graph.getVertex(number(v1));
				OrientVertex vOrient2=graph.getVertex(number(v2));
				Iterable<Edge> edgebetween = vOrient2.getEdges(vOrient1, Direction.BOTH);
				for (Edge e : edgebetween) {
					String ligne = e.getProperty("ligne");
					System.out.println(ligne);
					String v1Lines = vOrient1.getProperty("Lines");
					String v2Lines = vOrient2.getProperty("Lines");
					String name=vOrient1.getProperty("nom");
					System.out.println(name);
					if (v1Lines == null) {
						vOrient1.setProperty("Lines","." + ligne+".");
					}	
					if (v2Lines == null){
						vOrient2.setProperty("Lines","." + ligne+".");}
					if ((v1Lines != null)&&(!v1Lines.contains("." + ligne+"."))) {
						vOrient1.setProperty("Lines", v1Lines + "." + ligne+".");
					}
					if ((v2Lines != null)&&(!v2Lines.contains("." + ligne+".")) ){
						vOrient2.setProperty("Lines", v2Lines + "." + ligne+".");
					}
					String lines1=vOrient1.getProperty("Lines");
					System.out.println(lines1);
				}
			}
		}
		graph.shutdown();
	}}

