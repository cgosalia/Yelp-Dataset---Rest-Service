package DataSemantics.YelpDataset;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.query.* ;
import java.io.*;

public class Bloggers extends Object {
	
		//static final String inputFileName = "bloggers.rdf";
		static final String inputFileName = "bussiness.rdf";
        
      	public static void main (String args[]) {

		// Create an empty in-memory model 
		Model model = ModelFactory.createDefaultModel();
        InputStream in = FileManager.get().open(inputFileName);
        if (in == null) {
            throw new IllegalArgumentException( "File: " + inputFileName + " not found");
        }
        model.read( in, "" );
		String queryString = 
				"PREFIX business:<http://indiana.edu/svellank#> " +
						"PREFIX business_hours:<http://indiana.edu/svellank/hoursOfOperation#> "+
						"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
					    "PREFIX location:<http://indiana.edu/svellank/location#> " +
						"SELECT ?x ?name " + 
						"WHERE {" +
						"		?x business:name ?name . " +
						"		?x business:id \"N9p41uAx9P7Hm2xQJ14j1g\" . " +
						"      }";
	
		Query query = QueryFactory.create(queryString);

		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();

		// Output query results	
		ResultSetFormatter.out(System.out, results, query);
		
			
		// Important - free up resources used running the query
		qe.close();
	}
	
}