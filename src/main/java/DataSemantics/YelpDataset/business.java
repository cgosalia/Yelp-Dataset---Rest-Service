package DataSemantics.YelpDataset;

public class business{
	//?stars ?g ?id ?type ?name ?review_count ?city ?address ?longitude 
	//?latitude ?monday ?tuesday ?wednesday ?thursday ?friday ?saturday ?sunday
	private String stars;
	private String categories;
	private String id;
	private String name;
	private String review_count;
	private String city;
	private String address;
	private String longitude;
	private String latitude;
	private String monday;
	private String tuesday;
	private String wednesday; 
	private String thursday;
	private String friday;
	private String saturday; 
	private String sunday;
	
	
	
	
	
	public business(String stars, String categories, String id, String name, String review_count, String city,
			String address, String longitude, String latitude, String monday, String tuesday, String wednesday,
			String thursday, String friday, String saturday, String sunday) 
	{
		super();
		this.stars = stars;
		this.categories = categories;
		this.id = id;
		this.name = name;
		this.review_count = review_count;
		this.city = city;
		this.address = address;
		this.longitude = longitude;
		this.latitude = latitude;
		this.monday = monday;
		this.tuesday = tuesday;
		this.wednesday = wednesday;
		this.thursday = thursday;
		this.friday = friday;
		this.saturday = saturday;
		this.sunday = sunday;
	}
	
	public String getStars() {
		return stars;
	}
	public void setStars(String stars) {
		this.stars = stars;
	}
	public String getCategories() {
		return categories;
	}
	public void setCategories(String categories) {
		this.categories = categories;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
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
