package com.ishopping.data.generator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class GoodsGenerator extends DataGenerator {

	public GoodsGenerator(String sourceFileName, String targetFileName, String mapping) {
		super(sourceFileName, targetFileName, mapping);
	}

	@Override
	public void specificAction(JSONObject targetJSON, JSONObject sourceJSON) {
		String currentTime = getCurrentTime();
		targetJSON.put("desc", "");
		targetJSON.put("registerTime", currentTime);
		targetJSON.put("isNew", false);
		targetJSON.put("onSale", false);
	}

	public static void main(String[] args) {

		// mapping: { keyNameInTargetFile : keyNameInSourceFile}
		//
		// if the property value cannot be set directly by the value in source
		// file, then set the value in the mapping (keyNameInSourceFile) to null

		String mapping = "{'categoryId': 'category_id', 'currPrice': 'price', 'price': 'market_price', 'goodsId': 'id', 'name': 'name', 'photo': 'img',"
				+ "'stock': 'store_nums', 'desc': null, 'brandName': 'brand_name', 'specification': 'specifics', 'shelfLife': 'safe_day', 'tags': 'tag_ids',"
				+ "'registerTime': null, 'isNew': null, 'onSale': null }";

		DataGenerator generator = new GoodsGenerator("supermarket.json", "goods.json", mapping);

		JSONObject rootDataJSON = (JSONObject) generator.getJson().get("data");
		JSONObject tmpProducts = rootDataJSON.getJSONObject("products");
		JSONArray products = new JSONArray();
		for (Object o : tmpProducts.keySet()) {
			System.out.println("category : " + o.toString());
			JSONArray array = tmpProducts.getJSONArray(o.toString());
			products.addAll(array);
		}

		generator.generateData(products);
	}
}
