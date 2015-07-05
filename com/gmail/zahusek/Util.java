package com.gmail.zahusek.utils;

import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import com.gmail.zahusek.Main;
import com.gmail.zahusek.configurations.QConfig;
import com.gmail.zahusek.configurations.Config;
import com.gmail.zahusek.configurations.Save;
import com.gmail.zahusek.data.User;
import com.google.common.collect.Lists;

public interface Util {
	
	public static final DateUtil DATE = new DateUtil();
//	public static final String PREFIX = "&6&l|| &3&oCalendarRank &6&l||&r ";

//	public default String $(String... msgs) {
//		String result = "";
//		for(String msg : msgs) result += msg + " ";
//		return ChatColor.translateAlternateColorCodes('&', result);
//	}
	  / * Example */
    public default List<String> getMessage(String key, String... var){
    	List<String> keys = Config.getConfig("Messages").getVariable(key);
    	List<String> list = Lists.newArrayList();
    	
    	for(int i = 0; i < keys.size(); i++ ){
    		String result = ChatColor.translateAlternateColorCodes('&', keys.get(i));
    		for(int z = 0; z < var.length; z++)
    			result = result.replace("{" + z + "}", var[z]);
    		list.add(result);
    	}
    	
    	return list;
    }
    
    public default String[] variables(Player receiver, User user) {
    	QConfig cfg = Config.getConfig("Messages");
    	PermissionGroup cg = user.getCurrentGroup().iterator().next();
    	PermissionGroup lg = user.getLastGroup().iterator().next();
    	return new String[] {receiver.getName(), receiver.getDisplayName(),
				cg.getName(), cg.getPrefix(), cg.getSuffix(),
				lg.getName(), lg.getPrefix(), lg.getSuffix(),
				String.format(((String)cfg.getVariable("Date.start")).replace("$t", "%1$t"), user.getStart()),
				String.format(((String)cfg.getVariable("Date.end")).replace("$t", "%1$t"), user.getEnd()),
				String.format(((String) cfg.getVariable("Date.now")).replace("$t", "%1$t"), System.currentTimeMillis()),
				DATE.formatDateDiff(user.getEnd())};
    }
    
	public default void saveData(User player) {
		User user = player;
		UUID uuid = user.getUUID();
		String id = uuid.toString();
		long end = user.getEnd();
		long start = user.getStart();
		List<String> currentgroup = Lists.newArrayList();
		List<String> lastgroup = Lists.newArrayList();
		user.getCurrentGroup().forEach($group -> currentgroup.add($group.getName()));
		user.getLastGroup().forEach($group -> lastgroup.add($group.getName()));
		String lastname = user.getLastName();

		Config.register(id, "", "/User/" + id, Main.getJavaPlugin());
		QConfig cfg = Config.getConfig(id);
		
		cfg.setVariable("end", end);
		cfg.setVariable("start", start);
		cfg.setVariable("currentgroup", currentgroup);
		cfg.setVariable("lastgroup", lastgroup);
		cfg.setVariable("lastname", lastname);
		
		Config.unregister(id, Save.ALL);
	}
	@SuppressWarnings("unchecked")
	public default void loadData(User player) {
		User user = player;
		UUID uuid = user.getUUID();
		String id = uuid.toString();
		Config.register(id, "", "/User/" + id, Main.getJavaPlugin());

		QConfig cfg = Config.getConfig(id);
		if (cfg.getVariables().keySet().isEmpty()) return;
		
		long start = Long.parseLong(String.valueOf(cfg.getVariable("start")));
		long end = Long.parseLong(String.valueOf(cfg.getVariable("end")));
		List<PermissionGroup> currentgroup = Lists.newArrayList();
		List<PermissionGroup> lastgroup = Lists.newArrayList();
		((List<String>)cfg.getVariable("currentgroup")).forEach($group -> currentgroup.add(PermissionsEx.getPermissionManager().getGroup($group)));
		((List<String>)cfg.getVariable("lastgroup")).forEach($group -> lastgroup.add(PermissionsEx.getPermissionManager().getGroup($group)));
		String lastname = String.valueOf(cfg.getVariable("lastname"));
		
		user.setStart(start);
		user.setEnd(end);
		user.setCurrentGroup(currentgroup);
		user.setLastGroup(lastgroup);
		user.setLastName(lastname);
		
		Config.unregister(id, Save.ALL);
	}
}

