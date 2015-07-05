package com.gmail.zahusek.configurations;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.collect.Maps;

public class QConfig extends YamlConfiguration {

	private String id;
	private File file;
	private Map<String, Object> variables;

	public QConfig(String id, File file) {
		super();
		this.id = id.trim();
		this.file = file;
		this.variables = Maps.newHashMap();
	}

	public String getId() {
		return id;
	}

	public File getFile() {
		return file;
	}

	public Map<String, Object> getVariables() {
		return variables;
	}

	public void loadVariables() {
		for(String key : getKeys(true)) variables.put(key, get(key));
	}

	public void saveVariables() {
		for(String key : variables.keySet()) set(key, variables.get(key));
	}
	public void save() {
		try {
			save(file);
		} catch (IOException e) {
			System.out.println("== An error when saving a file " + id + " ==");
			e.printStackTrace();
		}
	}

	public void load() {
		try {
			load(file);
		} catch (IOException | InvalidConfigurationException e) {
			System.out.println("== An error when loading a file " + id + " ==");
			e.printStackTrace();
		}
		loadVariables();
	}

	public boolean equals(QConfig c) {
		return c.getId().equals(this.id);
	}

	@SuppressWarnings("unchecked")
	public <T> T getVariable(String path) {
		return (T) variables.get(path);
	}
	public void setVariable(String path, Object obj) {
		variables.put(path, obj);
	}
}
