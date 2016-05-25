package com.ishopping.data.generator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class CategoriesGenerator extends DataGenerator {

	public CategoriesGenerator(String sourceFileName, String targetFileName, JSONObject mapping) {
		super(sourceFileName, targetFileName, mapping);
	}

	// Step 3
	@Override
	public void specificAction(JSONObject targetJSON, JSONObject sourceJSON) {
		targetJSON.put("categoryId", sourceJSON.get("id").toString().substring(1));
	}

	public static void main(String[] args) {

		// Step 1
		String targetFileName = "categories.json";
		String sourceFileName = "supermarket.json";

		// Step 2 - {target : source}
		JSONObject keyMapping = new JSONObject();
		keyMapping.put("name", "name");
		keyMapping.put("categoryId", null);

		DataGenerator generator = new CategoriesGenerator(sourceFileName, targetFileName, keyMapping);

		JSONObject rootJSON = (JSONObject) generator.getJson();
		JSONArray inputArray = new JSONArray();

		// Step 4
		inputArray = rootJSON.getJSONObject("data").getJSONArray("categories");

		generator.generateData(inputArray);
	}
}
