package com.ishopping.data.generator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class BBSGenerator extends DataGenerator {

	public BBSGenerator(String sourceFileName, String targetFileName, JSONObject mapping) {
		super(sourceFileName, targetFileName, mapping);
	}

	@Override
	public void specificAction(JSONObject targetJSON, JSONObject sourceJSON) {
		String currentTime = getCurrentTime();
		targetJSON.put("publishTime", currentTime);
	}

	/**
	 * Generally 4 steps to configure a new generator.
	 */
	public static void main(String[] args) {

		// Step 1 : set target and source file name respectively.
		String targetFileName = "bbs.json";
		String sourceFileName = "bbs.json";

		// Step 2 : set key mapping relations between target and source json
		// files - {target : source}
		//
		// If one property value cannot be set directly by the value in source
		// file (need extra processes),
		// then, do not set the mapping, or set the value to null (these
		// property values need to be set in specificAction function)
		JSONObject keyMapping = new JSONObject();
		keyMapping.put("bbsId", "id");
		keyMapping.put("content", "topimg");
		keyMapping.put("title", "name");
		
		// Using the constructor and the settings to create a new generator.
		DataGenerator generator = new BBSGenerator(sourceFileName, targetFileName, keyMapping);

		// Using getJson() function to get root JSONObject from the source file.
		// ** the root element in source file SHOULD be a JSONObject **
		JSONObject rootJSON = (JSONObject) generator.getJson();

		// Using generateData() function to generate target json file as output.
		generator.generateData(rootJSON.getJSONArray("bbs"));
	}
}
