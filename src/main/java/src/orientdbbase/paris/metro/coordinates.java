package src.orientdbbase.paris.metro;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class coordinates {
	public static void main(String[] args) throws CsvException, FileNotFoundException, IOException {
		OrientGraph graph = new OrientGraph("remote:localhost/Paris_Metro", "root", "root");
		Map<String, Vertex> vert = new HashMap<String, Vertex>();
		String name;

		try (CSVReader reader = new CSVReader(
				new FileReader("/Users/lesya/Documents/Etudes/IASD/SGBD/Neo4j_Paris_metro-main/stations.csv"))) {
			List<String[]> r;

			try {
				r = reader.readAll();
				for (Iterator<String[]> iter = r.iterator(); iter.hasNext();) {
					String[] element = iter.next();
					String nomStation = element[0];
					if (!element[2].equals("x")) {
						Double x = Double.valueOf(element[2]);
						Double y = Double.valueOf(element[3]);
						for (Vertex v : (Iterable<Vertex>) graph.command(new OCommandSQL("SELECT FROM Station"))
								.execute()) {
							name = v.getProperty("nom");
							if (name.equals(nomStation)) {
								System.out.println(name + "  " + x + "  " + y);
								v.setProperty("X", x);
								v.setProperty("Y", y);
							}
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			graph.shutdown();
		}
	}
}
