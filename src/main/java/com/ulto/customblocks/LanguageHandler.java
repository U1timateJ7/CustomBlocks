package com.ulto.customblocks;

import com.google.gson.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class LanguageHandler {
	static Gson gson = new GsonBuilder().setPrettyPrinting().create();
	static JsonObject languageObject = new JsonObject();
	static File langNamespace = new File(CustomResourceCreator.assets, File.separator + "lang");
	static File lang = new File(langNamespace, File.separator + "lang");
	static File en_us = new File(lang, File.separator + "en_us.json");
	static Map<String, File> languages = new HashMap<>();
	static Map<String, JsonObject> languageObjects = new HashMap<>();

	public static void setupLanguage() {
		langNamespace.mkdirs();
		lang.mkdirs();

		if (!en_us.exists()) {
			try {
				en_us.createNewFile();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
		for (File value : GenerateCustomElements.blocks) {
			try {
				BufferedReader blockReader = new BufferedReader(new FileReader(value));
				StringBuilder json = new StringBuilder();
				String line;
				while ((line = blockReader.readLine()) != null) {
					json.append(line);
				}
				JsonObject block = new Gson().fromJson(json.toString(), JsonObject.class);
				if (block.has("languages")) {
					for (Map.Entry<String, JsonElement> lang : block.getAsJsonObject("languages").entrySet()) {
						if (!languages.containsKey(lang.getKey())) {
							File language = languages.put(lang.getKey(), new File(LanguageHandler.lang, File.separator + lang.getKey() + ".json"));
							if (!language.exists()) {
								try {
									language.createNewFile();
								} catch (IOException exception) {
									exception.printStackTrace();
								}
							}
						}
					}
				}
				blockReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JsonSyntaxException e) {
			}
		}
		for (File value : GenerateCustomElements.items) {
			try {
				BufferedReader blockReader = new BufferedReader(new FileReader(value));
				StringBuilder json = new StringBuilder();
				String line;
				while ((line = blockReader.readLine()) != null) {
					json.append(line);
				}
				JsonObject item = new Gson().fromJson(json.toString(), JsonObject.class);
				if (item.has("languages")) {
					for (Map.Entry<String, JsonElement> lang : item.getAsJsonObject("languages").entrySet()) {
						if (!languages.containsKey(lang.getKey())) {
							File language = languages.put(lang.getKey(), new File(LanguageHandler.lang, File.separator + lang.getKey() + ".json"));
							if (!language.exists()) {
								try {
									language.createNewFile();
								} catch (IOException exception) {
									exception.printStackTrace();
								}
							}
						}
					}
				}
				blockReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JsonSyntaxException e) {
			}
		}
		for (File value : GenerateCustomElements.itemGroups) {
			try {
				BufferedReader blockReader = new BufferedReader(new FileReader(value));
				StringBuilder json = new StringBuilder();
				String line;
				while ((line = blockReader.readLine()) != null) {
					json.append(line);
				}
				JsonObject itemGroup = new Gson().fromJson(json.toString(), JsonObject.class);
				if (itemGroup.has("languages")) {
					for (Map.Entry<String, JsonElement> lang : itemGroup.getAsJsonObject("languages").entrySet()) {
						if (!languages.containsKey(lang.getKey())) {
							File language = languages.put(lang.getKey(), new File(LanguageHandler.lang, File.separator + lang.getKey() + ".json"));
							if (!language.exists()) {
								try {
									language.createNewFile();
								} catch (IOException exception) {
									exception.printStackTrace();
								}
							}
						}
					}
				}
				blockReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JsonSyntaxException e) {
			}
		}
	}

	public static boolean addBlockKey(JsonObject block) {
		if (block.has("namespace") && block.has("id") && block.has("display_name")) {
			String namespace = block.get("namespace").getAsString();
			String id = block.get("id").getAsString();
			String text = block.get("display_name").getAsString();
			languageObject.addProperty("block." + namespace + "." + id, text);
			if (block.has("languages")) {
				for (Map.Entry<String, JsonElement> lang : block.getAsJsonObject("languages").entrySet()) {
					languageObjects.get(lang.getKey()).addProperty("block." + namespace + "." + id, lang.getValue().getAsString());
				}
			}
			return true;
		}
		return false;
	}

	public static boolean addItemKey(JsonObject item) {
		if (item.has("namespace") && item.has("id") && item.has("display_name")) {
			String namespace = item.get("namespace").getAsString();
			String id = item.get("id").getAsString();
			String text = item.get("display_name").getAsString();
			languageObject.addProperty("item." + namespace + "." + id, text);
			if (item.has("languages")) {
				for (Map.Entry<String, JsonElement> lang : item.getAsJsonObject("languages").entrySet()) {
					languageObjects.get(lang.getKey()).addProperty("item." + namespace + "." + id, lang.getValue().getAsString());
				}
			}
			return true;
		}
		return false;
	}

	public static boolean addItemGroupKey(JsonObject itemGroup) {
		if (itemGroup.has("namespace") && itemGroup.has("id") && itemGroup.has("name")) {
			String namespace = itemGroup.get("namespace").getAsString();
			String id = itemGroup.get("id").getAsString();
			String text = itemGroup.get("name").getAsString();
			languageObject.addProperty("itemGroup." + namespace + "." + id, text);
			if (itemGroup.has("languages")) {
				for (Map.Entry<String, JsonElement> lang : itemGroup.getAsJsonObject("languages").entrySet()) {
					languageObjects.get(lang.getKey()).addProperty("item." + namespace + "." + id, lang.getValue().getAsString());
				}
			}
			return true;
		}
		return false;
	}

	public static void saveLang() {
		try {
			FileWriter en_usfw = new FileWriter(en_us);
			en_usfw.write(gson.toJson(languageObject));
			en_usfw.close();
			for (Map.Entry<String, File> language : languages.entrySet()) {
				FileWriter languagefw = new FileWriter(language.getValue());
				languagefw.write(gson.toJson(languageObjects.get(language.getKey())));
				languagefw.close();
			}
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
}
