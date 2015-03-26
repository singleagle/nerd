package com.enjoy.nerd.remoterequest;

import android.os.Parcel;
import android.os.Parcelable;

public class FeedTag implements Parcelable{
	private String _id;
	private String parent;
	private String name;
	private String subject;
	
	public FeedTag(String tagId, String parentId, String name, FeedSubject subject) {
		this._id = tagId;
		this.parent = parentId;
		this.name= name;
		setSubject(subject);
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
	
	public void setSubject(FeedSubject subject){
		this.subject= subject.getDescription();
	}

	public FeedSubject getSubject(){
		return FeedSubject.translateFrom(subject);
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
		dest.writeString(subject);
	}
	
	private FeedTag(Parcel in){
		_id = in.readString();
		parent = in.readString();
		name = in.readString();
		subject =in.readString();
	}
	
     public static final Parcelable.Creator<FeedTag> CREATOR= new Parcelable.Creator<FeedTag>() {
	     public FeedTag createFromParcel(Parcel in) {
	         return new FeedTag(in);
	     }
	
	     public FeedTag[] newArray(int size) {
	         return new FeedTag[size];
	     }
	};

}
