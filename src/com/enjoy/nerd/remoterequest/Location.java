package com.enjoy.nerd.remoterequest;

class Location{
	private String address;
	private int confidence;
	private double[] location;
	
	public String getAddress() {
		return address;
	}
	
	public int getConfidence() {
		return confidence;
	}
	
	public double[] getLocation() {
		return location.clone();
	}
	
	
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