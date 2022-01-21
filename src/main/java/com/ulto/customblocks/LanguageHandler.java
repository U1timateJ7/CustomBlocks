package com.ulto.customblocks;

import com.google.gson.*;

import java.io.*;
import java.util.HashMap;
import java.util.Locale;
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
		for (File value : GenerateCustomElements.entities) {
			try {
				BufferedReader blockReader = new BufferedReader(new FileReader(value));
				StringBuilder json = new StringBuilder();
				String line;
				while ((line = blockReader.readLine()) != null) {
					json.append(line);
				}
				JsonObject entity = new Gson().fromJson(json.toString(), JsonObject.class);
				if (entity.has("languages")) {
					for (Map.Entry<String, JsonElement> lang : entity.getAsJsonObject("languages").entrySet()) {
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

	public static boolean addSaplingKey(JsonObject sapling) {
		if (sapling.has("namespace") && sapling.has("id")) {
			String namespace = sapling.get("namespace").getAsString();
			String id = (new Object() {
				String getSaplingId(String id) {
					String saplingId = id.replace("_tree", "_sapling");
					if (!saplingId.contains("_sapling")) saplingId += "_sapling";
					return saplingId;
				}
			}).getSaplingId(sapling.get("id").getAsString());
			StringBuilder text = new StringBuilder();
			String[] words = id.split("_");
			for (int i = 0; i < words.length; i++) {
				String word = words[i];
				String firstChar = String.valueOf(word.charAt(0)).toUpperCase(Locale.ROOT);
				StringBuilder otherChars = new StringBuilder();
				for (int j = 1; j < word.length(); j++) {
					otherChars.append(word.charAt(j));
				}
				if (i > 0) text.append(" ");
				text.append(firstChar).append(otherChars);
			}
			languageObject.addProperty("block." + namespace + "." + id, text.toString());
			if (sapling.has("languages")) {
				for (Map.Entry<String, JsonElement> lang : sapling.getAsJsonObject("languages").entrySet()) {
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

	public static boolean addFluidKeys(JsonObject fluid) {
		if (fluid.has("namespace") && fluid.has("id") && fluid.has("display_name")) {
			String namespace = fluid.get("namespace").getAsString();
			String id = fluid.get("id").getAsString();
			String text = fluid.get("display_name").getAsString();
			languageObject.addProperty("block." + namespace + "." + id, text);
			if (fluid.has("languages")) {
				JsonObject languages = fluid.getAsJsonObject("languages");
				if (languages.has("fluid")) {
					for (Map.Entry<String, JsonElement> lang : languages.getAsJsonObject("fluid").entrySet()) {
						languageObjects.get(lang.getKey()).addProperty("item." + namespace + "." + id, lang.getValue().getAsString());
					}
				}
			}
			String bucketNamespace = fluid.get("namespace").getAsString();
			String bucketId = fluid.get("id").getAsString() + "_bucket";
			String bucketText = fluid.get("display_name").getAsString() + " Bucket";
			languageObject.addProperty("item." + bucketNamespace + "." + bucketId, bucketText);
			if (fluid.has("languages")) {
				JsonObject languages = fluid.getAsJsonObject("languages");
				if (languages.has("bucket")) {
					for (Map.Entry<String, JsonElement> lang : languages.getAsJsonObject("bucket").entrySet()) {
						languageObjects.get(lang.getKey()).addProperty("item." + namespace + "." + id, lang.getValue().getAsString());
					}
				}
			}
			return true;
		}
		return false;
	}

	public static boolean addEntityKey(JsonObject entity) {
		if (entity.has("namespace") && entity.has("id") && entity.has("display_name")) {
			String namespace = entity.get("namespace").getAsString();
			String id = entity.get("id").getAsString();
			String text = entity.get("display_name").getAsString();
			languageObject.addProperty("entity." + namespace + "." + id, text);
			if (entity.has("languages")) {
				JsonObject languages = entity.getAsJsonObject("languages");
				for (Map.Entry<String, JsonElement> lang : languages.getAsJsonObject("name").entrySet()) {
					languageObjects.get(lang.getKey()).addProperty("entity." + namespace + "." + id, lang.getValue().getAsString());
				}
			}
			if (entity.has("has_spawn_egg") && entity.get("has_spawn_egg").getAsBoolean()) {
				languageObject.addProperty("item." + namespace + "." + id + "_spawn_egg", text + " Spawn Egg");
				if (entity.has("languages")) {
					JsonObject languages = entity.getAsJsonObject("languages");
					for (Map.Entry<String, JsonElement> lang : languages.getAsJsonObject("spawn_egg").entrySet()) {
						languageObjects.get(lang.getKey()).addProperty("entity." + namespace + "." + id + "_spawn_egg", lang.getValue().getAsString());
					}
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
