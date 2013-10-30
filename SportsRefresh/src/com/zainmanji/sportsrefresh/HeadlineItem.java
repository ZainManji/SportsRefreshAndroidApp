package com.zainmanji.sportsrefresh;

import java.io.Serializable;
import java.util.ArrayList;

public class HeadlineItem implements Serializable {

	//Declare and initialize object's variables
	private static final long serialVersionUID = -2706960697138812041L;
	public String headline;
	public String lastModified;
	public String mobileLink;
	public String story; //for substring Search
	public String source;
	public String description;
	public ArrayList<Image> imagesList;
	
	//Return HeadlineItem's image for display
	public String retrieveThumbnail() {
		if (imagesList!=null && !imagesList.isEmpty()) {
			return imagesList.get(0).url; //return first image
		}
		return null;
	}
}