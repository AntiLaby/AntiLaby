package de.heisluft.antilaby.log;

import org.apache.logging.log4j.Level;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

import de.heisluft.antilaby.nms.NmsTools;

public class Logger {
	
	private static ConsoleCommandSender c;
	private static boolean init;
	
	private static void init() {
		if (init) return;
		try {
			c = (ConsoleCommandSender) Class
					.forName("org.bukkit.craftbukkit." + NmsTools.getVersion() + "command.ColouredConsoleSender")
					.getMethod("getInstance").invoke(null);
		} catch (ReflectiveOperationException | IllegalArgumentException | SecurityException e) {
			System.out.println("fuck");
			c = Bukkit.getServer().getConsoleSender();
		}
		init = true;
	}
	
	private final String name;

	public Logger() {
		this(Thread.currentThread().getStackTrace()[0].getClassName());
	}
	
	public Logger(Class<?> src) {
		this(src.getName());
	}
	
	public Logger(String name) {
		init();
		this.name = name;
	}

	public void log(Level level, String log) {
		c.sendMessage("[AntiLaby/" + name + "] [" + level.name() + "]: " + log);
	}
	
}
