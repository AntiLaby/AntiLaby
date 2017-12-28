package com.github.antilaby.antilaby.api;

import com.github.antilaby.antilaby.main.AntiLaby;

import java.util.ArrayList;
import java.util.List;

public class LabyModJoinCommands {
	
	/**
	 * Get the LabyModJoinCommands from the configuration file without comments
	 *
	 * @return LabyModJoinCommands
	 *
	 * @author NathanNr
	 */
	public List<String> getLabyModJoinCommands() {
		return getLabyModJoinCommands(false);
	}
	
	/**
	 * Get the LabyModJoinCommands from the configuration file
	 *
	 * @param includeComments
	 * 		include comments or get only commands
	 *
	 * @return LabyModJoinCommands
	 *
	 * @author NathanNr
	 */
	public List<String> getLabyModJoinCommands(boolean includeComments) {
		if(includeComments)
			return AntiLaby.getInstance().getConfig().getStringList("AntiLaby.LabyModPlayerCommands");
		else {
			List<String> cmdComments = AntiLaby.getInstance().getConfig()
					                           .getStringList("AntiLaby.LabyModPlayerCommands");
			List<String> cmds = new ArrayList<>();
			for(final String command : cmdComments)
				if(!command.startsWith("#"))
					cmds.add(command);
			return cmds;
		}
	}
	
}
