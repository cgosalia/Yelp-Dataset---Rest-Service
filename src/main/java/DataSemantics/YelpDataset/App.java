package DataSemantics.YelpDataset;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

//Jena libraries
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.query.* ;

import java.io.*;
import java.util.ArrayList;

import DataSemantics.YelpDataset.business;

/**
 * Hello world!
 *
 */
@Controller
@EnableAutoConfiguration
public class App extends Object {

    @RequestMapping("/home/")
    @ResponseBody
    String home() {
        return "Hello World!";
    }

    @RequestMapping(value = "/get_businesses", params = {"category", "city"})
    @ResponseBody
    ArrayList<business> get_businesses(@RequestParam(value = "category") String category, 
    		@RequestParam(value = "city") String city ) {
    	ArrayList<business> final_result = new ArrayList<business>();
    	String inputFileName = "bussiness.rdf";
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
			"SELECT ?x ?stars ?categories ?id ?type ?name ?review_count ?city ?address ?longitude ?latitude ?monday ?tuesday ?wednesday ?thursday ?friday ?saturday ?sunday " + 
			"WHERE {" +
			"		?x business:name ?name . " +
			"		?x business:type ?type . " +
			"		?x business:stars ?stars . " +
			"		?x business:reviewcount	 ?review_count . " +
			"		?x business:id ?id . " +
			"		?x business:city \"" + city + "\" . " +
			"		?x business:city ?city . " +
			"		?x business:address ?address . " +
			"		?x location:coordinates ?location . " +
			"		?location location:longitude ?longitude . " +
			"		?location location:latitude ?latitude . " +
			"		?x business_hours:weekly ?time . " +
			"		?time business_hours:monday ?monday . " +
			"		?time business_hours:tuesday ?tuesday . " +
			"		?time business_hours:wednesday ?wednesday . " +
			"		?time business_hours:thursday ?thursday . " +
			"		?time business_hours:friday ?friday . " +
			"		?time business_hours:saturday ?saturday . " +
			"		?time business_hours:sunday ?sunday . " +
			"		?x business:categories ?categories . " +				
			"		FILTER regex(?categories,\""+ category+ "\",\"i\") . " +
			"      } LIMIT 10";

		Query query = QueryFactory.create(queryString);
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();
	
		
		QuerySolution next = null;
		business temp = null;
		while(results.hasNext())
		{
			next = results.next();
			temp = new business(next.getLiteral("stars").getString(), next.getLiteral("categories").getString(), next.getLiteral("id").getString(), 
					next.getLiteral("name").getString(), next.getLiteral("review_count").getString(), next.getLiteral("city").getString(),
					next.getLiteral("address").getString(), next.getLiteral("longitude").getString(), next.getLiteral("latitude").getString(),
					next.getLiteral("monday").getString(), next.getLiteral("tuesday").getString(), next.getLiteral("wednesday").getString(),
					next.getLiteral("thursday").getString(), next.getLiteral("friday").getString(), next.getLiteral("saturday").getString(), 
					next.getLiteral("sunday").getString()); 
			final_result.add(temp);			
		}
		
		qe.close();
        return final_result;
    }

    @RequestMapping(value = "/get_business", params = {"id"})
    @ResponseBody
    business get_business(@RequestParam(value = "id") String id) {
    	
    	business final_result = null;
    	String inputFileName = "bussiness.rdf";
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
			"SELECT ?x ?stars ?categories ?id ?type ?name ?review_count ?city ?address ?longitude ?latitude ?monday ?tuesday ?wednesday ?thursday ?friday ?saturday ?sunday " + 
			"WHERE {" +
			"		?x business:name ?name . " +
			"		?x business:type ?type . " +
			"		?x business:stars ?stars . " +
			"		?x business:reviewcount	 ?review_count . " +
			"		?x business:id \"" + id+"\" . " +
			"		?x business:city ?city . " +
			"		?x business:address ?address . " +
			"		?x location:coordinates ?location . " +
			"		?location location:longitude ?longitude . " +
			"		?location location:latitude ?latitude . " +	
			"		?x business_hours:weekly ?time . " +
			"		?time business_hours:monday ?monday . " +
			"		?time business_hours:tuesday ?tuesday . " +
			"		?time business_hours:wednesday ?wednesday . " +
			"		?time business_hours:thursday ?thursday . " +
			"		?time business_hours:friday ?friday . " +
			"		?time business_hours:saturday ?saturday . " +
			"		?time business_hours:sunday ?sunday . " +
			"		?x business:categories ?categories . " +				
			"      }";

		Query query = QueryFactory.create(queryString);

		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();

		QuerySolution next = null;
		
		if(results.hasNext())
		{
			next = results.next();
			final_result = new business(next.getLiteral("stars").getString(), 
					next.getLiteral("categories").getString(),
					id, 
					next.getLiteral("name").getString(),
					next.getLiteral("review_count").getString(),
					next.getLiteral("city").getString(),
					next.getLiteral("address").getString(),
					next.getLiteral("longitude").getString(),
					next.getLiteral("latitude").getString(),
					next.getLiteral("monday").getString(),
					next.getLiteral("tuesday").getString(), 
					next.getLiteral("wednesday").getString(),
					next.getLiteral("thursday").getString(), 
					next.getLiteral("friday").getString(), 
					next.getLiteral("saturday").getString(), 
					next.getLiteral("sunday").getString());			
		}
		
		qe.close();
        return final_result;
    }
    
    public static void main(String[] args) throws Exception
    {
        SpringApplication.run(App.class, args);
    }
    
}