package com.ishopping.data.generator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class CategoriesGenerator extends DataGenerator {

	public CategoriesGenerator(String sourceFileName, String targetFileName, String mapping) {
		super(sourceFileName, targetFileName, mapping);
	}

	@Override
	public void specificAction(JSONObject targetJSON, JSONObject sourceJSON) {
		targetJSON.put("categoryId", sourceJSON.get("id").toString().substring(1));
	}

	public static void main(String[] args) {

		String mapping = "{'categoryId': null, 'name': 'name'}";

		DataGenerator generator = new CategoriesGenerator("supermarket.json", "categories.json", mapping);

		JSONObject rootDataJSON = (JSONObject) generator.getJson().get("data");
		JSONArray categories = rootDataJSON.getJSONArray("categories");

		generator.generateData(categories);
	}
}
