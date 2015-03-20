package com.enjoy.nerd.remoterequest;

import android.os.Parcel;
import android.os.Parcelable;

public class FeedTag implements Parcelable{
	private String _id;
	private String parent;
	private String name;
	private String target;
	
	public FeedTag(String tagId, String parentId, String name, FeedType type) {
		this._id = tagId;
		this.parent = parentId;
		this.name= name;
		setTargetType(type);
	}

	public String getId() {
		return _id;
	}

	public String getParentId() {
		return parent;
	}



	public String getName() {
		return name;
	}
	
	public void setTargetType(FeedType type){
		target= type.description;
	}

	public FeedType getTargetType(){
		return FeedType.translateFrom(target);
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(_id);
		dest.writeString(parent);
		dest.writeString(name);
		dest.writeString(target);
	}
	
	private FeedTag(Parcel in){
		_id = in.readString();
		parent = in.readString();
		name = in.readString();
		target=in.readString();
	}
	
     public static final Parcelable.Creator<FeedTag> CREATOR= new Parcelable.Creator<FeedTag>() {
	     public FeedTag createFromParcel(Parcel in) {
	         return new FeedTag(in);
	     }
	
	     public FeedTag[] newArray(int size) {
	         return new FeedTag[size];
	     }
	};
	
	static public  enum FeedType{
		DISTRACTION("distraction"),
		SCENIC("scenic"),
		TOPIC("topic"),
		USER("user");
		
		public final String description;
		
		private FeedType(String description){
			this.description = description;
		}
		
		public static FeedType translateFrom(String description){
			for(FeedType type : FeedType.values()){
				if(type.description.equalsIgnoreCase(description)){
					return type;
				}
			}
			throw new IllegalArgumentException("no such feedtype:" + description);
		}
		
		
	}
}
