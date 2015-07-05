package com.gmail.zahusek.configurations;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Lists;

public class Config {

	private final static List<QConfig> CONFIGS = Lists.newArrayList();

	public static QConfig getConfig(String id) {
		return CONFIGS.stream().filter(config -> config.getId().equals(id))
				.findFirst().orElse(null);
	}

	public static void regitser(String id, String path, JavaPlugin plugin) {
		register(id, path, path, plugin);
	}

	public static void register(String id, String resource, String path,
			JavaPlugin plugin) {
		if (!resource.isEmpty() && !resource.endsWith(".yml"))
			resource = resource + ".yml";
		if (!path.endsWith(".yml"))
			path = path + ".yml";

		File file = new File(plugin.getDataFolder(), path);
		QConfig cfg = new QConfig(id, file);

		if (!file.exists() && !file.isFile()) {
			try {
				file.getParentFile().mkdirs();
				file.createNewFile();
			} catch (IOException e) {
				System.out.println("== An error when creating a new file " + id
						+ " ==");
				e.printStackTrace();
			}

			InputStream is = plugin.getResource(resource);
			if (is != null) {
				try {
					copyDefaults(is, file);
				} catch (Exception e) {
					System.out.println("== An error when loading copy to file "
							+ id + " ==");
					e.printStackTrace();
				}
			}
		}
		cfg.load();
		if(CONFIGS.stream().filter(config -> config.getId().equals(id)).findFirst().orElse(null) != null) return;
		CONFIGS.add(cfg);
	}

	public static void unregister(String id, Save type) {
		QConfig cfg = getConfig(id);
		if(cfg == null) return;
		if (type == Save.ALL)
			cfg.saveVariables();
		if (type == Save.ALL || type == Save.FILE)
			cfg.save();
		CONFIGS.remove(cfg);
	}

	public static void unregisters(Save type) {
		CONFIGS.forEach(cfg -> {
			if (type == Save.ALL)
				cfg.saveVariables();
			if (type == Save.ALL || type == Save.FILE)
				cfg.save();
		});
		CONFIGS.clear();
	}

	private static void copyDefaults(InputStream in, File file)
			throws IOException {
		OutputStream out = new FileOutputStream(file);
		try {

			byte[] b = new byte[1024];
			int l;
			while ((l = in.read(b)) > 0)
				out.write(b, 0, l);

		} finally {
			out.close();
			in.close();
		}
	}
}
