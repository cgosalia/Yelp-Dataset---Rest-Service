package DataSemantics.YelpDataset;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

//import org.json.JSONObject;

//import java.util.List;

public class business
{
	//?stars ?g ?id ?type ?name ?review_count ?city ?address ?longitude 
	//?latitude ?monday ?tuesday ?wednesday ?thursday ?friday ?saturday ?sunday
	private String stars;
	private ArrayList<String> categories = new ArrayList<String>();
	public void setCategories(ArrayList<String> categories) {
		this.categories = categories;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	private String id;
	public ArrayList<String> getCategories() {
		return categories;
	}
	private String label;
	private String review_count;
	private String city;
	private String address;
	private String langlat;
//	private String latitude;
	private String monday;
	private String tuesday;
	private String wednesday; 
	private String thursday;
	private String friday;
	private String saturday; 
	private String sunday;
	private String attributes;
	
	
//	private JSONObject attibute_categories;

	
	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}
	public String getAttributes() {
		return attributes;
	}
	public String getStars() {
		return stars;
	}
	public void setStars(String stars) {
		this.stars = stars;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getReview_count() {
		return review_count;
	}
	public void setReview_count(String review_count) {
		this.review_count = review_count;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getLanglat() {
		return langlat;
	}
	public void setLanglat(String langlat) {
		this.langlat = langlat;
	}
	public String getMonday() {
		return monday;
	}
	public void setMonday(String monday) {
		this.monday = monday;
	}
	public String getTuesday() {
		return tuesday;
	}
	public void setTuesday(String tuesday) {
		this.tuesday = tuesday;
	}
	public String getWednesday() {
		return wednesday;
	}
	public void setWednesday(String wednesday) {
		this.wednesday = wednesday;
	}
	public String getThursday() {
		return thursday;
	}
	public void setThursday(String thursday) {
		this.thursday = thursday;
	}
	public String getFriday() {
		return friday;
	}
	public void setFriday(String friday) {
		this.friday = friday;
	}
	public String getSaturday() {
		return saturday;
	}
	public void setSaturday(String saturday) {
		this.saturday = saturday;
	}
	public String getSunday() {
		return sunday;
	}
	public void setSunday(String sunday) {
		this.sunday = sunday;
	}

}
