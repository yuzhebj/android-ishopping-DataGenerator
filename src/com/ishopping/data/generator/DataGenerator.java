package com.ishopping.data.generator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class DataGenerator {

	protected String sourceFileName;
	protected String targetFileName;
	protected String mapping;

	DataGenerator(String sourceFileName, String targetFileName, String mapping) {
		this.sourceFileName = sourceFileName;
		this.targetFileName = targetFileName;
		this.mapping = mapping;
	}

	/**
	 * Read source json file from the package of com.ishopping.data.json
	 * 
	 * @return
	 */
	public JSONObject getJson() {

		String filePath = new File("").getAbsolutePath() + "/src/com/ishopping/data/generator/" + sourceFileName;

		System.out.println("Read file: " + filePath);
		
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
		System.out.println("Get JSON from file successfully!");
		return rootJson;
	}

	/**
	 * generate final json data according to the mappings
	 */
	public void generateData(JSONArray sourceArrays) {
		
		System.out.println("Start to generate JSON data...");

		JSONObject mappingJson = JSONObject.fromObject(mapping);
		FileWriter fileWritter = null;
		try {

			File file = new File(targetFileName);
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
		
		System.out.println("Generate data successfully!");
	}

	public void specificAction(JSONObject targetJSON, JSONObject sourceJSON) {
	}

}
