package com.enjoy.nerd.remoterequest;

import android.os.Parcel;
import android.os.Parcelable;

public class Location implements Parcelable{
	private String address;
	private int confidence;
	private double[] location = new double[2];  //location[0] = longitude, location[1] = latitude
	
	public Location(){
		confidence = 100;
	}
	
	public Location setAddress(String address){
		this.address = address;
		return this;
	}
	
	public Location setLatLng(double longitude, double latitude){
		location[0] = longitude;
		location[1] = latitude;
		return this;
	}
	
	public double getLongitude(){
		return location[0];
	}
	
	public double getLatitude(){
		return location[1];
	}
	
	public String getAddress() {
		return address;
	}
	
	public int getConfidence() {
		return confidence;
	}
	
	public double[] getLocation() {
		return location.clone();
	}

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	private Location(Parcel in){
		address = in.readString();
		confidence = in.readInt();
		in.readDoubleArray(location);
	}
	
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(address);
		dest.writeInt(confidence);
		dest.writeDoubleArray(location);
		
	}
	
   public static final Parcelable.Creator<Location> CREATOR= new Parcelable.Creator<Location>() {
	     public Location createFromParcel(Parcel in) {
	         return new Location(in);
	     }
	
	     public Location[] newArray(int size) {
	         return new Location[size];
	     }
	};
		
	
	
	/*
	static public class LocationSerializer implements JsonDeserializer<Location>, JsonSerializer<Location>{

		@Override
		public Location deserialize(JsonElement json, Type typeOfT,
				JsonDeserializationContext context)
				throws JsonParseException {
			JsonObject jobj = json.getAsJsonObject();
			Location location = new Location();
			
			location.address = jobj.getAsString();
			location.confidence = jobj.get("confidence").getAsInt();
			location.location[0] = jobj.get("location").getAsJsonArray().get(0).getAsDouble();
			location.location[1] = jobj.get("location").getAsJsonArray().get(0).getAsDouble();
			return location;
		}
		
		@Override
		public JsonElement serialize(Location src, Type typeOfSrc,
				JsonSerializationContext context) {
			JsonObject jobj = new JsonObject();
			jobj.addProperty("address", src.address);
			jobj.addProperty("confidence", src.confidence);
			JsonArray jArray = new JsonArray();
			jArray.add(new JsonPrimitive(src.location[0]));
			jArray.add(new JsonPrimitive(src.location[0]));
			jobj.add("location", jArray);
			return jobj;
		}	
		
	}*/
	
	
}