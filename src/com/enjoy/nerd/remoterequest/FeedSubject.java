package com.enjoy.nerd.remoterequest;

public  enum FeedSubject{
	DISTRACTION("distraction"),
	SCENIC("scenic"),
	TOPIC("topic"),
	USER("user");
	
	public final String description;
	
	private FeedSubject(String description){
		this.description = description;
	}
	
	public String getDescription(){
		return description;
	}
	
	public static FeedSubject translateFrom(String description){
		for(FeedSubject type : FeedSubject.values()){
			if(type.description.equalsIgnoreCase(description)){
				return type;
			}
		}
		throw new IllegalArgumentException("no such feedSubject:" + description);
	}
	
	
}

