package DataSemantics.YelpDataset;

import java.util.*;
public class users {

	private String user_id;
	private String yelping_since;
	private Hashtable<String, String> compliements = new Hashtable<String, String>();;
	private Hashtable<String, String> friends = new Hashtable<String, String>();;
	private String average_stars;
	private String review_count;
	private String votes_funny;
	private String votes_cool;
	private String votes_useful;
	private String user_fans;
	private String user_name;
	private String user_type;
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getYelping_since() {
		return yelping_since;
	}
	public void setYelping_since(String yelping_since) {
		this.yelping_since = yelping_since;
	}
	
	public Hashtable<String, String> getCompliements() {
		return compliements;
	}
	public void setCompliements(Hashtable<String, String> compliements) {
		this.compliements = compliements;
	}
	public Hashtable<String, String> getFriends() {
		return friends;
	}
	public void setFriends(Hashtable<String, String> friends) {
		this.friends = friends;
	}
	public String getAverage_stars() {
		return average_stars;
	}
	public void setAverage_stars(String average_stars) {
		this.average_stars = average_stars;
	}
	public String getReview_count() {
		return review_count;
	}
	public void setReview_count(String review_count) {
		this.review_count = review_count;
	}
	public String getVotes_funny() {
		return votes_funny;
	}
	public void setVotes_funny(String votes_funny) {
		this.votes_funny = votes_funny;
	}
	public String getVotes_cool() {
		return votes_cool;
	}
	public void setVotes_cool(String votes_cool) {
		this.votes_cool = votes_cool;
	}
	public String getVotes_useful() {
		return votes_useful;
	}
	public void setVotes_useful(String votes_useful) {
		this.votes_useful = votes_useful;
	}
	public String getUser_fans() {
		return user_fans;
	}
	public void setUser_fans(String user_fans) {
		this.user_fans = user_fans;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_type() {
		return user_type;
	}
	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}
}
