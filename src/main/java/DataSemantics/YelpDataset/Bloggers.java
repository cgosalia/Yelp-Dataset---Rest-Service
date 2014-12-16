package DataSemantics.YelpDataset;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.query.* ;
import java.io.*;

public class Bloggers extends Object {
	
		//static final String inputFileName = "bloggers.rdf";
		static final String inputFileName = "reviews.rdf";
        
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
				"PREFIX user:<http://indiana.edu/svellank/users/> "+
				"PREFIX review:<http://indiana.edu/svellank/reviews/> "+
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
				"SELECT ?x ?stars ?review_text ?review_id ?review_date " + //?user_id ?business_id ?review_type ?votes_cool ?votes_useful ?votes_funny " + 
				"WHERE {" +
				"		?x review:stars ?stars . " +
				"		?x review:review_text ?review_text . " +
				"		?x review:review_id ?review_id . "  +
				"		?x review:review_date ?review_date . " +
				"		?x user:user_id ?user_id . " +
			/*	"		?x business:id \"dnCqXvdWhaOZNa0gz\" . " +*/
				"		?x review:type ?review_type . " +
				"		?x review:votes ?votes . " +
				/*"		OPTIONAL { ?votes review:cool ?votes_cool } . " +
				"		OPTIONAL { ?votes review:funny ?votes_funny } . " +
				"		OPTIONAL { ?votes review:useful ?votes_useful } . " +*/
				"      } LIMIT 10";
	
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