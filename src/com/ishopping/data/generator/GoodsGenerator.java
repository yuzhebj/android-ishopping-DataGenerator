package com.ishopping.data.generator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class GoodsGenerator extends DataGenerator {

	private int onSaleCount = 4;

	public GoodsGenerator(String sourceFileName, String targetFileName, JSONObject mapping) {
		super(sourceFileName, targetFileName, mapping);
	}

	// override specificAction to set values to some specific properties that
	// cannot be set directly by the value in source file.
	@Override
	public void specificAction(JSONObject targetJSON, JSONObject sourceJSON) {
		String currentTime = getCurrentTime();
		targetJSON.put("desc", "");
		targetJSON.put("registerTime", currentTime);

		boolean isNew = false;
		boolean onSale = false;
		double randomValue = Math.random();
		if (randomValue < 0.2) {
			isNew = true;
		}
		if (randomValue > 0.1 && randomValue < 0.6 && onSaleCount > 0) {
			onSale = true;
			// add a promotion image for home page
			targetJSON.put("promotionPhoto",
					"http://img01.bqstatic.com/upload/goods/000/001/5893/0000015893_04730.jpg@200w_200h_90Q.jpg");
			onSaleCount--;
		}
		targetJSON.put("isNew", isNew);
		targetJSON.put("onSale", onSale);

		// parse String to float/int
		targetJSON.put("currPrice", parseToFloat(targetJSON.get("currPrice").toString()));
		targetJSON.put("price", parseToFloat(targetJSON.get("price").toString()));
		targetJSON.put("stock", Integer.parseInt(targetJSON.get("stock").toString()));
		targetJSON.put("shelfLife", Integer.parseInt(targetJSON.get("shelfLife").toString()));
	}

	/**
	 * Generally 3 steps to configure a new generator.
	 * 
	 * Step 1 : set target and source file name respectively.
	 * 
	 * Step 2 : set key mapping relations between target and source json files -
	 * {target : source}
	 *
	 * If one property value cannot be set directly by the value in source file
	 * (need extra processes), then, do not set the mapping, or set the value to
	 * null (these property values need to be set in specificAction function)
	 *
	 * Step 3 : extract json array from the root JSONObject as input.
	 */
	public static void main(String[] args) {

		// Step 1
		String targetFileName = "goods.json";
		String sourceFileName = "supermarket.json";

		// Step 2 : {target : source}
		JSONObject keyMapping = new JSONObject();
		keyMapping.put("categoryId", "category_id");
		keyMapping.put("goodsId", "id");
		keyMapping.put("name", "name");
		keyMapping.put("photo", "img");
		keyMapping.put("brandName", "brand_name");
		keyMapping.put("specification", "specifics");
		keyMapping.put("tags", "tag_ids");

		keyMapping.put("currPrice", "price");
		keyMapping.put("price", "market_price");
		keyMapping.put("stock", "store_nums");
		keyMapping.put("shelfLife", "safe_day");

		// keyMapping.put("desc", null);
		// keyMapping.put("registerTime", null);
		// keyMapping.put("isNew", null);
		// keyMapping.put("onSale", null);

		// Using the constructor and the settings to create a new generator.
		DataGenerator generator = new GoodsGenerator(sourceFileName, targetFileName, keyMapping);

		// Using getJson() function to get root JSONObject from the source file.
		// ** the root element in source file SHOULD be a JSONObject **
		JSONObject rootJSON = (JSONObject) generator.getJson();

		// Step 3
		JSONArray inputArray = new JSONArray();
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
