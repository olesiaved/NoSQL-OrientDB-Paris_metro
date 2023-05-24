package src.orientdbbase.paris.metro;
import java.util.HashMap;
import java.util.Map;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.db.object.ODatabaseObject;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.orientechnologies.orient.object.db.OrientDBObject;
import com.tinkerpop.blueprints.Vertex;

public class paris_metro {
	public static void main(String[] args){
		OrientGraph graph = new OrientGraph("remote:localhost/Paris_Metro_Small", 
		        "root", "root");
		Map<String, Vertex> vert = new HashMap<String, Vertex>();
		String name;

	    for (Vertex v : (Iterable<Vertex>) graph.command(new OCommandSQL("SELECT FROM Station")).execute()) {
	    	name=v.getProperty("nom");
	    	if (!vert.containsKey(name)) {
	    		vert.put(name, v);
	    	}else {
	    		System.out.println("removed");
	    		v.remove();
	    	}
	    }

	    graph.shutdown();
	}
}
