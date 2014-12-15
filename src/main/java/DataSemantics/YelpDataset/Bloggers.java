package DataSemantics.YelpDataset;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.query.* ;
import java.io.*;

public class Bloggers extends Object {
	
		//static final String inputFileName = "bloggers.rdf";
		static final String inputFileName = "yelp_academic_dataset_business.rdf";
        
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
						"SELECT ?x ?stars ?categories ?id ?type ?name ?review_count ?city ?address ?longitude ?latitude ?attributes ?monday ?tuesday ?wednesday ?thursday ?friday ?saturday ?sunday " + 
						"WHERE {" +
						"		?x business:name ?name . " +
						"		?x business:type ?type . " +
						"		?x business:stars ?stars . " +
						"		?x business:reviewcount	 ?review_count . " +
						"		?x business:id ?id . " +
						"		?x business:city \"Phoenix\" . " +
						"		?x business:city ?city . " +
						"		?x business:address ?address . " +
						"		?x location:coordinates ?location . " +
						"		?location location:longitude ?longitude . " +
						"		?location location:latitude ?latitude . " +
						"		?x business:attributes ?attributes . " +
						"		OPTIONAL { ?x business_hours:weekly ?time } . " +
						"		OPTIONAL { ?time business_hours:monday ?monday } . " +
						"		OPTIONAL { ?time business_hours:tuesday ?tuesday } . " +
						"		OPTIONAL { ?time business_hours:wednesday ?wednesday } . " +
						"		OPTIONAL { ?time business_hours:thursday ?thursday } . " +
						"		OPTIONAL { ?time business_hours:friday ?friday } . " +
						"		OPTIONAL { ?time business_hours:saturday ?saturday } . " +
						"		OPTIONAL { ?time business_hours:sunday ?sunday } . " +
						"		?x business:categories ?categories . " +				
						"		FILTER regex(?categories,\"Mexican\",\"i\") . " +
						"      } LIMIT 50";
	
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