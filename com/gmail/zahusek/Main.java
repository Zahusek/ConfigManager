package com.gmail.zahusek;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.zahusek.configurations.Config;
import com.gmail.zahusek.configurations.QConfig;
import com.gmail.zahusek.configurations.Save;
import com.gmail.zahusek.utils.Util;

public class Main extends JavaPlugin implements Util {
	
	public static void main(String[] args) {
	}

	private static Main getJavaPlugin;
	public static Main getJavaPlugin() { return getJavaPlugin; }

	//private static PermissionsEx getPluginEx;
	//public static PermissionsEx getPluginEx() { return getPluginEx; }
	
//	protected SchedulerCore task = new SchedulerCore();
	
	@Override
	public void onDisable() {
		//task.cancel();
		//Arrays.asList(Bukkit.getOnlinePlayers()).stream().map((s) -> User.getUser(s.getUniqueId())).forEach(this::saveData);
		Config.unregister("Messages", Save.NOTHING);
		Config.unregisters(Save.ALL);
		super.onDisable();
	}
	@Override
	public void onEnable() {
//		if(!checkJava()) {
//			System.out.println(String.format("[%s] - Where are you Jv8 :c ???", getDescription().getName()));
//			getServer().getPluginManager().disablePlugin(this);
//			return;
//		}
//		if(!setupPermissions()) {
//			System.out.println(String.format("[%s] - Where are you PermissionsEx :c ???", getDescription().getName()));
//			getServer().getPluginManager().disablePlugin(this);
//			return;
//		}
		getJavaPlugin = this;
		setupConfigurations();
//		setupListeners();
//		setupCommands();
//		Arrays.asList(Bukkit.getOnlinePlayers()).stream().map((s) -> User.getUser(s.getUniqueId())).forEach(this::loadData);
//		task.runTaskTimerAsynchronously(this, 20L, 20L);
		super.onEnable();
	}
//	public void setupListeners(){
//		PluginManager e = Bukkit.getPluginManager();
//		e.registerEvents(new EventJoin(), this);
//		e.registerEvents(new EventQuit(), this);
		
//	}
//	public void setupCommands() {
//		CommandManager.registerCommand(new CmdCalendarRank(), this);
//		CommandManager.registerCommand(new SubCommandAdd(), this);
//		CommandManager.registerCommand(new SubCommandCheck(), this);
//		CommandManager.registerCommand(new SubCommandDel(), this);
//		CommandManager.registerCommand(new SubCommandHelp(), this);
//		CommandManager.registerCommand(new SubCommandReload(), this);
//		CommandManager.registerCommand(new SubCommandRem(), this);
//		CommandManager.registerCommand(new SubCommandSet(), this);
//	}
	/* Example */
	public static void setupConfigurations() {
		
		Config.register("Variables", "com/gmail/zahusek/files/Variables", "Variables", getJavaPlugin());
		Config.unregister("Variables", Save.NOTHING);
		
		Config.register("Messages", "com/gmail/zahusek/files/Messages",
				"Messages", getJavaPlugin);

		QConfig cfg = Config.getConfig("Messages");
		cfg.load();

		try {String.format((String) cfg.getVariable("Date.start"),Calendar.getInstance());
		} catch (Exception e) {cfg.setVariable("Date.start", new String(""));}
		
		try {String.format((String) cfg.getVariable("Date.end"),Calendar.getInstance());
		} catch (Exception e) {cfg.setVariable("Date.end", new String(""));}

		try {String.format((String) cfg.getVariable("Date.now"),Calendar.getInstance());
		} catch (Exception e) {cfg.setVariable("Date.now", new String(""));}
		
		List<String> list = cfg.getVariable("Date.format");
		if(list == null || list.size() != 7) return;
		
		String[] format = new String[19];
		
		int i = 0;
		for(String names : list){
			if(i == 0) {
				format[i++] = names.trim();
				continue;
			}
			String[] split = names.split(" \\^ ");
			for(String name : Arrays.asList(split)) {
				format[i++] = name;
			}
		}
		DATE.setFormat(format);
	}

//	private boolean setupPermissions() {
//		Plugin pex = Bukkit.getServer().getPluginManager().getPlugin("PermissionsEx");
//		getPluginEx = (PermissionsEx) pex;
//		return pex != null;
//	}
//	private boolean checkJava() {
//		return System.getProperty("java.version").startsWith("1.8");
//	}
}
