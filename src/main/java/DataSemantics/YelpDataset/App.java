package DataSemantics.YelpDataset;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

//import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.ObjectMapper;

//Jena libraries
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.query.* ;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;

//import org.codehaus.jackson.annotate.JsonMethod;
//import org.json.simple.JSONObject;






import DataSemantics.YelpDataset.business;
import DataSemantics.YelpDataset.reviews;
import DataSemantics.YelpDataset.Item_businesses;

/**
 * Hello world!
 *
 */
@Controller
@EnableAutoConfiguration
public class App extends Object 
{
	ObjectMapper om = new ObjectMapper();
    @RequestMapping("/home/")
    @ResponseBody
    String home() {
        return "Hello World!";
    }

    @RequestMapping(value = "/get_businesses", params = {"category", "city"})
    @ResponseBody
    Item_businesses get_businesses(@RequestParam(value = "category") String category, 
    		@RequestParam(value = "city") String city ) throws UnsupportedEncodingException 
    {
    	Item_businesses item_results = new Item_businesses();
    	ArrayList<business> final_result = new ArrayList<business>();
    	String inputFileName = "yelp_academic_dataset_business.rdf";
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
			"		?x business:city \"" + java.net.URLDecoder.decode(city, "UTF-8") + "\" . " +
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
			"		FILTER regex(?categories,\""+ java.net.URLDecoder.decode(category, "UTF-8")+ "\",\"i\") . " +
			"      } LIMIT 50";
		Query query = QueryFactory.create(queryString);
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();
		QuerySolution next = null;
		business temp = null;
		while(results.hasNext())
		{
			next = results.next();
			temp = new business();
			temp.setStars(next.getLiteral("stars").getString());
			temp.setReview_count(next.getLiteral("review_count").getString());
			temp.setCity(next.getLiteral("city").getString());
			temp.setId(next.getLiteral("id").getString());
			temp.setName(next.getLiteral("name").getString());
			temp.setLanglat(next.getLiteral("longitude").getString()+","+next.getLiteral("latitude").getString());			
			String cat = next.getLiteral("categories").getString();
			ArrayList<String> t = new ArrayList<String>();
			if(cat.length() > 3)
			{
				cat = cat.substring(0, cat.length()-2);
				if(cat.contains(","))
				{
					String temp_cat[] = cat.split(",");
					for(String each: temp_cat)
					{
						t.add(each);
					}
				}
				else
				{
					t.add(cat);
				}
			}			
			temp.setCategories(t);
			temp.setAddress(next.getLiteral("address").getString());
			temp.setAttributes(next.getLiteral("attributes").getString());			
			if(next.getLiteral("monday") != null && next.getLiteral("monday").getString() != "")
			{
				if(next.getLiteral("monday") != null)
				temp.setMonday(next.getLiteral("monday").getString());
				if(next.getLiteral("tuesday") != null)
				temp.setTuesday(next.getLiteral("tuesday").getString());
				if(next.getLiteral("wednesday") != null)
				temp.setWednesday(next.getLiteral("wednesday").getString());
				if(next.getLiteral("thursday") != null)
				temp.setThursday(next.getLiteral("thursday").getString());
				if(next.getLiteral("friday") != null)
				temp.setFriday(next.getLiteral("friday").getString());
				if(next.getLiteral("saturday") != null)
				temp.setSaturday(next.getLiteral("saturday").getString());
				if(next.getLiteral("sunday") != null)
				temp.setSunday(next.getLiteral("sunday").getString());
			} 
			final_result.add(temp);			
		}		
		qe.close();
        item_results.setItems(final_result);
        return item_results;
    }

    @RequestMapping(value = "/get_business", params = {"id"})
    @ResponseBody
    business get_business(@RequestParam(value = "id") String id) throws UnsupportedEncodingException 
    {
    	business final_result = null;
    	String inputFileName = "yelp_academic_dataset_business.rdf";
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
			"		?x business:id \"" + java.net.URLDecoder.decode(id, "UTF-8") +"\" . " +
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
			"      }";
		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();
		QuerySolution next = null;		
		if(results.hasNext())
		{
			next = results.next();
			try
			{
				final_result = new business();
				System.out.println("we have rsi;ts");
				final_result.setStars(next.getLiteral("stars").getString());
				final_result.setReview_count(next.getLiteral("review_count").getString());
				final_result.setCity(next.getLiteral("city").getString());
				final_result.setId(id);
				final_result.setName(next.getLiteral("name").getString());
				final_result.setLanglat(next.getLiteral("longitude").getString()+","+next.getLiteral("latitude").getString());
				String cat = next.getLiteral("categories").getString();
				ArrayList<String> t = new ArrayList<String>();
				if(cat.length() > 3)
				{
					cat = cat.substring(0, cat.length()-2);
					if(cat.contains(","))
					{
						String temp_cat[] = cat.split(",");
						for(String each: temp_cat)
						{
							t.add(each);
						}
					}
					else
					{
						t.add(cat);
					}
				}				
				final_result.setCategories(t);
				final_result.setAddress(next.getLiteral("address").getString());
				final_result.setAttributes(next.getLiteral("attributes").getString());
				if(next.getLiteral("monday").getString() != null || next.getLiteral("monday").getString() != "")
				{
					if(next.getLiteral("monday") != null)
						final_result.setMonday(next.getLiteral("monday").getString());
						if(next.getLiteral("tuesday") != null)
							final_result.setTuesday(next.getLiteral("tuesday").getString());
						if(next.getLiteral("wednesday") != null)
							final_result.setWednesday(next.getLiteral("wednesday").getString());
						if(next.getLiteral("thursday") != null)
							final_result.setThursday(next.getLiteral("thursday").getString());
						if(next.getLiteral("friday") != null)
							final_result.setFriday(next.getLiteral("friday").getString());
						if(next.getLiteral("saturday") != null)
							final_result.setSaturday(next.getLiteral("saturday").getString());
						if(next.getLiteral("sunday") != null)
							final_result.setSunday(next.getLiteral("sunday").getString());
				}
			}
			catch(Exception ex)
			{
				
			}
		}
		qe.close();
        return final_result;
    }
    
    
    @RequestMapping(value = "/get_reviews_by_business_id", params = {"business_id"})
    @ResponseBody
    ArrayList<reviews> reviews_by_business_id(@RequestParam(value = "business_id") String business_id) throws UnsupportedEncodingException {
    	ArrayList<reviews> final_result = new ArrayList<reviews>();
    	String inputFileName = "reviews.rdf";
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
				"SELECT ?x ?stars ?review_text ?review_id ?review_date ?user_id ?business_id ?review_type ?votes_cool ?votes_useful ?votes_funny " + 
				"WHERE {" +
				"		?x review:stars ?stars . " +
				"		?x review:review_text ?review_text . " +
				"		?x review:review_id ?review_id . "  +
				"		?x review:review_date ?review_date . " +
				"		?x user:user_id ?user_id . " +
				"		?x business:id \"" + java.net.URLDecoder.decode(business_id, "UTF-8") +"\" . " +
				"		?x review:type ?review_type . " +
				"		?x review:votes ?votes . " +
				"		OPTIONAL { ?votes review:cool ?votes_cool } . " +
				"		OPTIONAL { ?votes review:funny ?votes_funny } . " +
				"		OPTIONAL { ?votes review:useful ?votes_useful } . " +
				"      } LIMIT 10";

		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();
	
		
		QuerySolution next = null;
		reviews temp = null;
		while(results.hasNext())
		{
			next = results.next();
			temp = new reviews();
			temp.setStars(next.getLiteral("stars").getString());
			temp.setReview_text(next.getLiteral("review_text").getString());
			temp.setReview_id(next.getLiteral("review_id").getString());
			temp.setBusiness_id(business_id);
			temp.setUser_id(next.getLiteral("user_id").getString());
			temp.setReview_date(next.getLiteral("review_date").getString());
			temp.setReview_type(next.getLiteral("review_type").getString());
			temp.setVotes_cool(next.getLiteral("votes_cool").getString());
			temp.setVotes_funny(next.getLiteral("votes_funny").getString());
			temp.setVotes_useful(next.getLiteral("votes_useful").getString());
			//getting the user name
			String inputFileName_users = "users.rdf";
			// Create an empty in-memory model 
			Model model1 = ModelFactory.createDefaultModel();
	        InputStream in1 = FileManager.get().open(inputFileName_users);
	        if (in1 == null) {
	            throw new IllegalArgumentException( "File: " + inputFileName_users + " not found");
	        }
	        model1.read( in1, "" );
	        System.out.println("User Id: " + temp.getUser_id());
			String queryString1 = 
					"PREFIX Compliments:<http://indiana.edu/svellank/Users/Compliments#> " +
					"PREFIX user:<http://indiana.edu/svellank/Users#> "+
					"PREFIX Votes:<http://indiana.edu/svellank/Users/Votes#> "+
					"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
					"SELECT ?x ?user_name " + 
					"WHERE {" +
					"		?x user:user_id \"" + temp.getUser_id() + "\" . " +
					"		?x user:name ?user_name . " +
					"      } ";

			Query query1 = QueryFactory.create(queryString1);
			// Execute the query and obtain results
			QueryExecution qe1 = QueryExecutionFactory.create(query1, model1);
			ResultSet results1 = qe1.execSelect();
			QuerySolution next1 = null;
			while(results1.hasNext())
			{
				next1 = results1.next();
				temp.setUser_name(next1.getLiteral("user_name").getString());			
			}
			qe1.close();
			//getting the business name
	    	String inputFileName2 = "bussiness.rdf"; 
			Model model2 = ModelFactory.createDefaultModel();
	        InputStream in2 = FileManager.get().open(inputFileName2);
	        if (in2 == null) {
	            throw new IllegalArgumentException( "File: " + inputFileName2 + " not found");
	        }
	        model2.read( in2, "" );      
			String queryString2 = 
				"PREFIX business:<http://indiana.edu/svellank#> " +
				"PREFIX business_hours:<http://indiana.edu/svellank/hoursOfOperation#> "+
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
			    "PREFIX location:<http://indiana.edu/svellank/location#> " +
				"SELECT ?x ?name " + 
				"WHERE {" +
				"		?x business:name ?name . " +
				"		?x business:id \"" + temp.getBusiness_id() +"\" . " +
				"      }";

			Query query2 = QueryFactory.create(queryString2);
			QueryExecution qe2 = QueryExecutionFactory.create(query2, model2);
			ResultSet results2 = qe2.execSelect();
			QuerySolution next2 = null;
			if(results2.hasNext())
			{
				next2 = results2.next();
				temp.setBusiness_name(next2.getLiteral("name").getString());
			}
			else
			{
			}
			qe2.close();	
			final_result.add(temp);			
		}
		qe.close();
        return final_result;
    }
    
    
    
    @RequestMapping(value = "/get_reviews_by_user_id", params = {"user_id"})
    @ResponseBody
    ArrayList<reviews> reviews_by_user_id(@RequestParam(value = "user_id") String user_id) throws UnsupportedEncodingException {
    	ArrayList<reviews> final_result = new ArrayList<reviews>();
    	String inputFileName = "reviews.rdf";
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
				"SELECT ?x ?stars ?review_text ?review_id ?review_date ?user_id ?business_id ?review_type ?votes_cool ?votes_useful ?votes_funny " + 
				"WHERE {" +
				"		?x review:stars ?stars . " +
				"		?x review:review_text ?review_text . " +
				"		?x review:review_id ?review_id . "  +
				"		?x review:review_date ?review_date . " +
				"		?x user:user_id \"" + java.net.URLDecoder.decode(user_id, "UTF-8") + "\" . " +
				"		?x business:id ?business_id . " +
				"		?x review:type ?review_type . " +
				"		?x review:votes ?votes . " +
				"		OPTIONAL { ?votes review:cool ?votes_cool } . " +
				"		OPTIONAL { ?votes review:funny ?votes_funny } . " +
				"		OPTIONAL { ?votes review:useful ?votes_useful } . " +
				"      } LIMIT 10";

		Query query = QueryFactory.create(queryString);
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();
		QuerySolution next = null;
		reviews temp = null;
		while(results.hasNext())
		{
			next = results.next();
			temp = new reviews();
			temp.setStars(next.getLiteral("stars").getString());
			temp.setReview_text(next.getLiteral("review_text").getString());
			temp.setReview_id(next.getLiteral("review_id").getString());
			temp.setUser_id(user_id);
			temp.setBusiness_id(next.getLiteral("business_id").getString());
			temp.setReview_date(next.getLiteral("review_date").getString());
			temp.setReview_type(next.getLiteral("review_type").getString());
			temp.setVotes_cool(next.getLiteral("votes_cool").getString());
			temp.setVotes_funny(next.getLiteral("votes_funny").getString());
			temp.setVotes_useful(next.getLiteral("votes_useful").getString());
			//getting the user name
			String inputFileName_users = "users.rdf";
			// Create an empty in-memory model 
			Model model1 = ModelFactory.createDefaultModel();
	        InputStream in1 = FileManager.get().open(inputFileName_users);
	        if (in1 == null) {
	            throw new IllegalArgumentException( "File: " + inputFileName_users + " not found");
	        }
	        model1.read( in1, "" );
			String queryString1 = 
					"PREFIX Compliments:<http://indiana.edu/svellank/Users/Compliments#> " +
					"PREFIX user:<http://indiana.edu/svellank/Users#> "+
					"PREFIX Votes:<http://indiana.edu/svellank/Users/Votes#> "+
					"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
					"SELECT ?x ?user_name " + 
					"WHERE {" +
					"		?x user:user_id \"" + temp.getUser_id() + "\" . " +
					"		?x user:name ?user_name . " +
					"      } ";

			Query query1 = QueryFactory.create(queryString1);
			// Execute the query and obtain results
			QueryExecution qe1 = QueryExecutionFactory.create(query1, model1);
			ResultSet results1 = qe1.execSelect();
			QuerySolution next1 = null;
			while(results1.hasNext())
			{
				next1 = results1.next();
				temp.setUser_name(next1.getLiteral("user_name").getString());			
			}
			qe1.close();
			

			//getting the business name
	    	String inputFileName2 = "bussiness.rdf"; 
			Model model2 = ModelFactory.createDefaultModel();
	        InputStream in2 = FileManager.get().open(inputFileName2);
	        if (in2 == null) {
	            throw new IllegalArgumentException( "File: " + inputFileName2 + " not found");
	        }
	        model2.read( in2, "" );
			String queryString2 = 
				"PREFIX business:<http://indiana.edu/svellank#> " +
				"PREFIX business_hours:<http://indiana.edu/svellank/hoursOfOperation#> "+
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
			    "PREFIX location:<http://indiana.edu/svellank/location#> " +
				"SELECT ?x ?name " + 
				"WHERE {" +
				"		?x business:name ?name . " +
				"		?x business:id \"" + temp.getBusiness_id() +"\" . " +
				"      }";

			Query query2 = QueryFactory.create(queryString2);
			QueryExecution qe2 = QueryExecutionFactory.create(query2, model2);
			ResultSet results2 = qe2.execSelect();
			QuerySolution next2 = null;
			
			if(results2.hasNext())
			{
				next2 = results2.next();
				temp.setBusiness_name(next2.getLiteral("name").getString());
			}
			else
			{
			}
			qe2.close();	
			final_result.add(temp);			
		}		
		qe.close();
        return final_result;
    }

    @RequestMapping(value = "/get_reviews_by_business_user_id", params = { "business_id", "user_id"})
    @ResponseBody
    ArrayList<reviews> reviews_by_business_user_id(@RequestParam(value = "user_id") String user_id, @RequestParam(value = "business_id") String business_id) throws UnsupportedEncodingException {
    	ArrayList<reviews> final_result = new ArrayList<reviews>();
    	String inputFileName = "reviews.rdf";
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
				"SELECT ?x ?stars ?review_text ?review_id ?review_date ?user_id ?business_id ?review_type ?votes_cool ?votes_funny ?votes_useful " + 
				"WHERE {" +
				"		?x review:stars ?stars . " +
				"		?x review:review_text ?review_text . " +
				"		?x review:review_id ?review_id . "  +
				"		?x review:review_date ?review_date . " +
				"		?x user:user_id \"" + java.net.URLDecoder.decode(user_id, "UTF-8") + "\" . " +
				"		?x business:id \"" + java.net.URLDecoder.decode(business_id, "UTF-8") +"\" . " +
				"		?x review:type ?review_type . " +
				"		?x review:votes ?votes . " +
				"		OPTIONAL { ?votes review:cool ?votes_cool } . " +
				"		OPTIONAL { ?votes review:funny ?votes_funny } . " +
				"		OPTIONAL { ?votes review:useful ?votes_useful } . " +
				"      } LIMIT 10";

		Query query = QueryFactory.create(queryString);
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();
	
		
		QuerySolution next = null;
		reviews temp = null;
		while(results.hasNext())
		{
			next = results.next();
			temp = new reviews();
			temp.setStars(next.getLiteral("stars").getString());
			temp.setReview_text(next.getLiteral("review_text").getString());
			temp.setReview_id(next.getLiteral("review_id").getString());
			temp.setUser_id(user_id);
			temp.setBusiness_id(business_id);
			temp.setReview_date(next.getLiteral("review_date").getString());
			temp.setReview_type(next.getLiteral("review_type").getString());
			temp.setVotes_cool(next.getLiteral("votes_cool").getString());
			temp.setVotes_funny(next.getLiteral("votes_funny").getString());
			temp.setVotes_useful(next.getLiteral("votes_useful").getString());
			
			

			//getting the user name
			String inputFileName_users = "users.rdf";
			// Create an empty in-memory model 
			Model model1 = ModelFactory.createDefaultModel();
	        InputStream in1 = FileManager.get().open(inputFileName_users);
	        if (in1 == null) {
	            throw new IllegalArgumentException( "File: " + inputFileName_users + " not found");
	        }
	        model1.read( in1, "" );
	        System.out.println("User Id: " + temp.getUser_id());
			String queryString1 = 
					"PREFIX Compliments:<http://indiana.edu/svellank/Users/Compliments#> " +
					"PREFIX user:<http://indiana.edu/svellank/Users#> "+
					"PREFIX Votes:<http://indiana.edu/svellank/Users/Votes#> "+
					"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
					"SELECT ?x ?user_name " + 
					"WHERE {" +
					"		?x user:user_id \"" + temp.getUser_id() + "\" . " +
					"		?x user:name ?user_name . " +
					"      } ";

			Query query1 = QueryFactory.create(queryString1);
			// Execute the query and obtain results
			QueryExecution qe1 = QueryExecutionFactory.create(query1, model1);
			ResultSet results1 = qe1.execSelect();
			QuerySolution next1 = null;
			while(results1.hasNext())
			{
				next1 = results1.next();
				temp.setUser_name(next1.getLiteral("user_name").getString());			
			}
			qe1.close();
			

			//getting the business name
	    	String inputFileName2 = "bussiness.rdf"; 
			Model model2 = ModelFactory.createDefaultModel();
	        InputStream in2 = FileManager.get().open(inputFileName2);
	        if (in2 == null) {
	            throw new IllegalArgumentException( "File: " + inputFileName2 + " not found");
	        }
	        model2.read( in2, "" );
//	        System.out.println("business id: " + temp.getBusiness_id());
	        
			String queryString2 = 
				"PREFIX business:<http://indiana.edu/svellank#> " +
				"PREFIX business_hours:<http://indiana.edu/svellank/hoursOfOperation#> "+
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
			    "PREFIX location:<http://indiana.edu/svellank/location#> " +
				"SELECT ?x ?name " + 
				"WHERE {" +
				"		?x business:name ?name . " +
				"		?x business:id \"" + temp.getBusiness_id() +"\" . " +
				"      }";

			Query query2 = QueryFactory.create(queryString2);
			QueryExecution qe2 = QueryExecutionFactory.create(query2, model2);
			ResultSet results2 = qe2.execSelect();
			QuerySolution next2 = null;
			
			if(results2.hasNext())
			{
				next2 = results2.next();
				temp.setBusiness_name(next2.getLiteral("name").getString());
			}
			else
			{
//				System.out.println("No result");
			}
			qe2.close();	

			
			
			
			final_result.add(temp);			
		}
		
		qe.close();
        return final_result;
    }
    
    @RequestMapping(value = "/get_reviews_by_review_id", params = { "review_id"})
    @ResponseBody
    ArrayList<reviews> reviews_by_review_id(@RequestParam(value = "review_id") String review_id) throws UnsupportedEncodingException {
    	ArrayList<reviews> final_result = new ArrayList<reviews>();
    	String inputFileName = "reviews.rdf";
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
				"SELECT ?x ?stars ?review_text ?review_id ?review_date ?user_id ?business_id ?review_type ?votes_cool ?votes_funny ?votes_useful " + 
				"WHERE {" +
				"		?x review:stars ?stars . " +
				"		?x review:review_text ?review_text . " +
				"		?x review:review_id \"" + java.net.URLDecoder.decode(review_id, "UTF-8") + "\" . "  +
				"		?x review:review_date ?review_date . " +
				"		?x user:user_id ?user_id . " +
				"		?x business:id ?business_id . " +
				"		?x review:type ?review_type . " +
				"		?x review:votes ?votes . " +
				"		OPTIONAL { ?votes review:cool ?votes_cool } . " +
				"		OPTIONAL { ?votes review:funny ?votes_funny } . " +
				"		OPTIONAL { ?votes review:useful ?votes_useful } . " +
				"      } LIMIT 10";

		Query query = QueryFactory.create(queryString);
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();
	
		
		QuerySolution next = null;
		reviews temp = null;
		while(results.hasNext())
		{
			next = results.next();
			temp = new reviews();
			temp.setStars(next.getLiteral("stars").getString());
			temp.setReview_text(next.getLiteral("review_text").getString());
			temp.setReview_id(review_id);
			temp.setUser_id(next.getLiteral("user_id").getString());
			temp.setBusiness_id(next.getLiteral("business_id").getString());
			temp.setReview_date(next.getLiteral("review_date").getString());
			temp.setReview_type(next.getLiteral("review_type").getString());
			temp.setVotes_cool(next.getLiteral("votes_cool").getString());
			temp.setVotes_funny(next.getLiteral("votes_funny").getString());
			temp.setVotes_useful(next.getLiteral("votes_useful").getString());
			
			
			//getting the user name
			String inputFileName_users = "users.rdf";
			// Create an empty in-memory model 
			Model model1 = ModelFactory.createDefaultModel();
	        InputStream in1 = FileManager.get().open(inputFileName_users);
	        if (in1 == null) {
	            throw new IllegalArgumentException( "File: " + inputFileName_users + " not found");
	        }
	        model1.read( in1, "" );
//	        System.out.println("User Id: " + temp.getUser_id());
			String queryString1 = 
					"PREFIX Compliments:<http://indiana.edu/svellank/Users/Compliments#> " +
					"PREFIX user:<http://indiana.edu/svellank/Users#> "+
					"PREFIX Votes:<http://indiana.edu/svellank/Users/Votes#> "+
					"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
					"SELECT ?x ?user_name " + 
					"WHERE {" +
					"		?x user:user_id \"" + temp.getUser_id() + "\" . " +
					"		?x user:name ?user_name . " +
					"      } ";

			Query query1 = QueryFactory.create(queryString1);
			// Execute the query and obtain results
			QueryExecution qe1 = QueryExecutionFactory.create(query1, model1);
			ResultSet results1 = qe1.execSelect();
			QuerySolution next1 = null;
			while(results1.hasNext())
			{
				next1 = results1.next();
				temp.setUser_name(next1.getLiteral("user_name").getString());			
			}
			qe1.close();
			

			//getting the business name
	    	String inputFileName2 = "bussiness.rdf"; 
			Model model2 = ModelFactory.createDefaultModel();
	        InputStream in2 = FileManager.get().open(inputFileName2);
	        if (in2 == null) {
	            throw new IllegalArgumentException( "File: " + inputFileName2 + " not found");
	        }
	        model2.read( in2, "" );
//	        System.out.println("business id: " + temp.getBusiness_id());
	        
			String queryString2 = 
				"PREFIX business:<http://indiana.edu/svellank#> " +
				"PREFIX business_hours:<http://indiana.edu/svellank/hoursOfOperation#> "+
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
			    "PREFIX location:<http://indiana.edu/svellank/location#> " +
				"SELECT ?x ?name " + 
				"WHERE {" +
				"		?x business:name ?name . " +
				"		?x business:id \"" + temp.getBusiness_id() +"\" . " +
				"      }";

			Query query2 = QueryFactory.create(queryString2);
			QueryExecution qe2 = QueryExecutionFactory.create(query2, model2);
			ResultSet results2 = qe2.execSelect();
			QuerySolution next2 = null;
			
			if(results2.hasNext())
			{
				next2 = results2.next();
				temp.setBusiness_name(next2.getLiteral("name").getString());
			}
			else
			{
//				System.out.println("No result");
			}
			qe2.close();	
			
			
			
			
			final_result.add(temp);			
		}
		
		qe.close();
        return final_result;
    }

    
    
    @RequestMapping(value = "/get_user_by_user_id", params = { "user_id"})
    @ResponseBody
    ArrayList<users> user_by_user_id(@RequestParam(value = "user_id") String user_id) throws UnsupportedEncodingException {
    	ArrayList<users> final_result = new ArrayList<users>();
    	String inputFileName = "users.rdf";
		// Create an empty in-memory model 
		Model model = ModelFactory.createDefaultModel();
        InputStream in = FileManager.get().open(inputFileName);
        if (in == null) {
            throw new IllegalArgumentException( "File: " + inputFileName + " not found");
        }
        model.read( in, "" );
		String queryString = 
				"PREFIX Compliments:<http://indiana.edu/svellank/Users/Compliments#> " +
				"PREFIX user:<http://indiana.edu/svellank/Users#> "+
				"PREFIX Votes:<http://indiana.edu/svellank/Users/Votes#> "+
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
				"SELECT ?x ?user_id ?yelping_since ?compliments ?friends ?average_stars ?review_count ?votes_funny ?votes_cool ?votes_useful ?user_fans ?user_name ?user_type " + 
				"WHERE {" +
				"		?x user:user_id \"" + java.net.URLDecoder.decode(user_id, "UTF-8") + "\" . " +
				"		?x user:yelping_since ?yelping_since . " +
				"		?x user:compliments ?compliments . "  +
				"		?x user:friends ?friends . " +
				"		?x user:average_stars ?average_stars . " +
				"		?x user:review_count ?review_count . " +
				"		?x user:fans ?user_fans . " +
				"		?x user:name ?user_name . " +
				"		?x user:type ?user_type . " +
				"		?x user:votes ?votes . " +
				"		OPTIONAL { ?votes Votes:cool ?votes_cool } . " +
				"		OPTIONAL { ?votes Votes:funny ?votes_funny } . " +
				"		OPTIONAL { ?votes Votes:useful ?votes_useful } . " +
				"      } LIMIT 10";

		Query query = QueryFactory.create(queryString);
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();
	
		
		QuerySolution next = null;
		users temp = null;
		while(results.hasNext())
		{
			next = results.next();
			temp = new users();
			temp.setUser_id(user_id);
			temp.setYelping_since(next.getLiteral("yelping_since").getString());		
			temp.setCompliements(get_values(next.getLiteral("compliments").getString()));
			temp.setFriends(get_friends(next.getLiteral("friends").getString()));
			temp.setAverage_stars(next.getLiteral("average_stars").getString());
			temp.setReview_count(next.getLiteral("review_count").getString());
			temp.setUser_fans(next.getLiteral("user_fans").getString());
			temp.setUser_name(next.getLiteral("user_name").getString());
			temp.setVotes_cool(next.getLiteral("votes_cool").getString());
			temp.setVotes_funny(next.getLiteral("votes_funny").getString());
			temp.setVotes_useful(next.getLiteral("votes_useful").getString());
			temp.setUser_type(next.getLiteral("user_type").getString());
			final_result.add(temp);			
		}
		
		qe.close();
        return final_result;
    }
    
    
 Hashtable<String, String> get_values(String value) throws UnsupportedEncodingException {
    	Hashtable<String, String> final_result = new Hashtable<String, String>();
    	
    	value = value.substring(1, value.length()-1);
    	if(value.contains(":"))
    	{
    		value.replace("\"","");
    		if(value.contains(","))
    		{
    			String each_pair[] =  value.split(",");
    			for(String temp : each_pair)
    			{
    				String[] pair = temp.split(":");
    				final_result.put(pair[0], pair[1]);
    			}
    			
    		}    		
    		else
    		{String[] temp_results = value.split(":");
    		final_result.put(temp_results[0], temp_results[1]);}
    	}
    	return final_result;
    }
 
 
    
 
 Hashtable<String, String> get_friends(String value) throws UnsupportedEncodingException {
	 Hashtable<String, String> final_result = new Hashtable<String, String>();
	 if(value.length() < 3)
	 {
	 	 return final_result;
	 }
	 else if(value.contains(",") ==true)
	 {
//		 System.out.println("In loop 2: " + value);
		 value = value.substring(1, value.length()-1);
		 value.replace("\"", "");
		 String temp_each[] = value.split(",");

		 for(String each: temp_each)
		 {
			 String inputFileName = "users.rdf";
			// Create an empty in-memory model 
			Model model = ModelFactory.createDefaultModel();
		    InputStream in = FileManager.get().open(inputFileName);
		    if (in == null) {
		         throw new IllegalArgumentException( "File: " + inputFileName + " not found");
		    }
		    model.read( in, "" );
		    each = each.substring(1,each.length()-1);
//		    System.out.println("Each: " + each);
			String queryString = 
						"PREFIX Compliments:<http://indiana.edu/svellank/Users/Compliments#> " +
						"PREFIX user:<http://indiana.edu/svellank/Users#> "+
						"PREFIX Votes:<http://indiana.edu/svellank/Users/Votes#> "+
						"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
						"SELECT ?x ?user_id ?user_name " + 
						"WHERE {" +
						"		?x user:user_id \"" + each + "\" . " +
						"		?x user:name ?user_name . " +
						"      }";
	
			Query query = QueryFactory.create(queryString);
			// Execute the query and obtain results
			QueryExecution qe = QueryExecutionFactory.create(query, model);
			ResultSet results = qe.execSelect();	
			QuerySolution next = null;
//			users temp = null;
			while(results.hasNext())
			{
				next = results.next();
				final_result.put(each, next.getLiteral("user_name").getString());
			}
			qe.close();
		 
		 }
	 }
	 else
	 {
		 value = value.substring(1, value.length()-1);
//		 System.out.println("In loop 3: " + value);
		 value.replace("\"", "");
		 String inputFileName = "users.rdf";
		// Create an empty in-memory model 
		Model model = ModelFactory.createDefaultModel();
	    InputStream in = FileManager.get().open(inputFileName);
	    if (in == null) {
	         throw new IllegalArgumentException( "File: " + inputFileName + " not found");
	    }
	    model.read( in, "" );
		String queryString = 
					"PREFIX Compliments:<http://indiana.edu/svellank/Users/Compliments#> " +
					"PREFIX user:<http://indiana.edu/svellank/Users#> "+
					"PREFIX Votes:<http://indiana.edu/svellank/Users/Votes#> "+
					"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
					"SELECT ?x ?user_id ?user_name " + 
					"WHERE {" +
					"		?x user:user_id \"" +  value + "\" . " +
					"		?x user:name ?user_name . " +
					"      } LIMIT 10";

		Query query = QueryFactory.create(queryString);
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();	
		QuerySolution next = null;
//		users temp = null;
		while(results.hasNext())
		{
			next = results.next();
			final_result.put(value, next.getLiteral("user_name").getString());
		}
		qe.close();

		 
	 }
 	return final_result;
 }

 
 
 
 
 @RequestMapping(value = "/cities", params = { "term"})
 @ResponseBody
 ArrayList<String> autocheck_cities(@RequestParam(value = "term") String city) throws UnsupportedEncodingException {
 	ArrayList<String> final_result = new ArrayList<String>();
 	String inputFileName = "cities.txt";

 	
 	try 
 	{
		for (String line : Files.readAllLines(Paths.get(inputFileName), Charset.defaultCharset()))  
		{
			if(line.toLowerCase().contains(java.net.URLDecoder.decode(city.toLowerCase(), "UTF-8")))
				final_result.add(line);
		}
	} 
 	catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
     return final_result;
 }
 
 
 @RequestMapping(value = "/categories", params = { "term"})
 @ResponseBody
 ArrayList<String> autocheck_category(@RequestParam(value = "term") String category) throws UnsupportedEncodingException {
 	ArrayList<String> final_result = new ArrayList<String>();
 	String inputFileName = "categories.txt";

 	
 	try 
 	{
		for (String line : Files.readAllLines(Paths.get(inputFileName), Charset.defaultCharset())) 
		{
			if(line.toLowerCase().contains(java.net.URLDecoder.decode(category.toLowerCase(), "UTF-8")))
				final_result.add(line);
		}
	} 
 	catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
     return final_result;
 }
    
    public static void main(String[] args) throws Exception
    {
        SpringApplication.run(App.class, args);
    }
    
}