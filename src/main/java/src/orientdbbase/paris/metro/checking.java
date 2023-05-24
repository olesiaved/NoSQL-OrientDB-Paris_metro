package src.orientdbbase.paris.metro;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class checking {
	public static void main(String[] args) throws CsvException, FileNotFoundException, IOException {
		OrientGraph graph = new OrientGraph("remote:localhost/Paris_Metro_Small", "root", "root");
		String name;

		for (Vertex v : (Iterable<Vertex>) graph.command(new OCommandSQL("SELECT FROM Station")).execute()) {
			name = v.getProperty("nom");
			//v.setProperty("Lines","");
			System.out.println(name + "  ");
		}

		graph.shutdown();
	}
}
