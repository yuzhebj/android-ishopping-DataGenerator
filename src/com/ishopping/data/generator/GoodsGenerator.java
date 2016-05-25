package com.ishopping.data.generator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class GoodsGenerator extends DataGenerator {

	public GoodsGenerator(String sourceFileName, String targetFileName, JSONObject mapping) {
		super(sourceFileName, targetFileName, mapping);
	}

	// Step 3 : override specificAction to set values to some specific
	// properties that cannot be set directly by the value in source file.
	@Override
	public void specificAction(JSONObject targetJSON, JSONObject sourceJSON) {
		String currentTime = getCurrentTime();
		targetJSON.put("desc", "");
		targetJSON.put("registerTime", currentTime);
		targetJSON.put("isNew", false);
		targetJSON.put("onSale", false);
	}

	/**
	 * Generally 4 steps to configure a new generator.
	 */
	public static void main(String[] args) {

		// Step 1 : set target and source file name respectively.
		String targetFileName = "goods.json";
		String sourceFileName = "supermarket.json";

		// Step 2 : set key mapping relations between target and source json
		// files - {target : source}
		//
		// If one property value cannot be set directly by the value in source
		// file (need extra processes),
		// then, do not set the mapping, or set the value to null (these
		// property values need to be set in specificAction function)
		JSONObject keyMapping = new JSONObject();
		keyMapping.put("categoryId", "category_id");
		keyMapping.put("currPrice", "price");
		keyMapping.put("price", "market_price");
		keyMapping.put("goodsId", "id");
		keyMapping.put("name", "name");
		keyMapping.put("photo", "img");
		keyMapping.put("stock", "store_nums");
		keyMapping.put("brandName", "brand_name");
		keyMapping.put("specification", "specifics");
		keyMapping.put("shelfLife", "safe_day");
		keyMapping.put("tags", "tag_ids");
		// keyMapping.put("desc", null);
		// keyMapping.put("registerTime", null);
		// keyMapping.put("isNew", null);
		// keyMapping.put("onSale", null);

		// Using the constructor and the settings to create a new generator.
		DataGenerator generator = new GoodsGenerator(sourceFileName, targetFileName, keyMapping);

		// Using getJson() function to get root JSONObject from the source file.
		// ** the root element in source file SHOULD be a JSONObject **
		JSONObject rootJSON = (JSONObject) generator.getJson();
		JSONArray inputArray = new JSONArray();

		// Step 4 : extract json array from the root JSONObject as input.
		JSONObject products = rootJSON.getJSONObject("data").getJSONObject("products");
		for (Object o : products.keySet()) {
			System.out.println("category : " + o.toString());
			JSONArray array = products.getJSONArray(o.toString());
			inputArray.addAll(array);
		}

		// Using generateData() function to generate target json file as output.
		generator.generateData(inputArray);
	}
}
