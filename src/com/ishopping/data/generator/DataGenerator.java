package com.ishopping.data.generator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class DataGenerator {

	/**
	 * Read source json file from the package of com.ishopping.data.json
	 * 
	 * @return
	 */
	public static JSONObject getJson(String sourceFileName) {

		String filePath = new File("").getAbsolutePath() + "/src/com/ishopping/data/generator/" + sourceFileName;

		BufferedReader reader = null;
		String jsonStr = "";
		try {
			FileInputStream fileInputStream = new FileInputStream(filePath);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
			reader = new BufferedReader(inputStreamReader);
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				jsonStr += tempString;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		JSONObject rootJson = (JSONObject) JSONObject.fromObject(jsonStr);
		return rootJson;
	}

	/**
	 * generate final json data according to the mappings
	 */
	public static void generateData(JSONArray sourceArrays, String fileName, String mapping) {

		JSONObject mappingJson = JSONObject.fromObject(mapping);
		FileWriter fileWritter = null;
		try {

			File file = new File(fileName);
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();

			fileWritter = new FileWriter(file.getName(), true);
			BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
			JSONArray targetArrays = new JSONArray();

			for (int i = 0; i < sourceArrays.size(); ++i) {
				JSONObject sourceJSON = sourceArrays.getJSONObject(i);
				JSONObject targetJSON = new JSONObject();

				for (Object targetKey : mappingJson.keySet()) {
					String sourceKey = mappingJson.get(targetKey).toString();
					targetJSON.put(targetKey, sourceJSON.get(sourceKey));
				}

				specificAction(targetJSON, sourceJSON);

				targetArrays.add(targetJSON);
			}
			bufferWritter.write(targetArrays.toString());
			bufferWritter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (fileWritter != null) {
			try {
				fileWritter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// set specific configurations and actions for categories
	private static void generateDataForCategory(JSONObject rootDataJSON) {
		String categoryFileName = "categories.json";
		String mappingForCategory = "{'categoryId': 'id', 'name': 'name'}";
		JSONArray categories = rootDataJSON.getJSONArray("categories");
		generateData(categories, categoryFileName, mappingForCategory);
	}

	private static void specificActionForCategory(JSONObject targetJSON, JSONObject sourceJSON) {
		targetJSON.put("categoryId", sourceJSON.get("id").toString().substring(1));
	}

	// set specific configurations and actions for goods
	private static void generateDataForGood(JSONObject rootDataJSON) {
		String goodFileName = "goods.json";
		String mappingForGood = "{'categoryId': 'id', 'currPrice': 'price', 'price': 'market_price', 'goodsId': 'id', 'name': 'name', 'photo': 'img',"
				+ "'stock': 'store_nums', 'desc': null, 'brandName': 'brand_name', 'specification': 'specifics', 'safeDay': 'safe_day', 'tags': 'tag_ids',"
				+ "'registerTime': null, 'isNew': null, 'onSale': null }";
		JSONArray products = rootDataJSON.getJSONObject("products").getJSONArray("a82");
		generateData(products, goodFileName, mappingForGood);
	}

	private static void specificActionForGood(JSONObject targetJSON, JSONObject sourceJSON) {
		String currentTime = Calendar.getInstance().getTime().toString();
		targetJSON.put("desc", "");
		targetJSON.put("registerTime", currentTime);
		targetJSON.put("isNew", false);
		targetJSON.put("onSale", false);
	}

	public static void specificAction(JSONObject targetJSON, JSONObject sourceJSON) {
		// for goods
		specificActionForGood(targetJSON, sourceJSON);
	}

	public static void main(String[] args) {

		String sourceFileName = "supermarket.json";
		JSONObject rootDataJSON = (JSONObject) getJson(sourceFileName).get("data");

		// for goods
		generateDataForGood(rootDataJSON);

	}
}
