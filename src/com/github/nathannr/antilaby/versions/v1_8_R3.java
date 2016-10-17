package com.github.nathannr.antilaby.versions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.github.nathannr.antilaby.main.AntiLaby.EnumLabyModFeature;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutCustomPayload;

public class v1_8_R3 {

	public static void setLabyModFeature(Player p, HashMap<EnumLabyModFeature, Boolean> list) {
		try {
			HashMap<String, Boolean> nList = new HashMap<String, Boolean>();
			for(EnumLabyModFeature f : list.keySet()) {
				nList.put(f.name(), list.get(f));
			}
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(byteOut);
			out.writeObject(nList);
			ByteBuf a = Unpooled.copiedBuffer(byteOut.toByteArray());
			PacketDataSerializer b = new PacketDataSerializer(a);
			PacketPlayOutCustomPayload packet = new PacketPlayOutCustomPayload("LABYMOD", b);
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
			System.out.print("[AntiLaby/INFO] Disable some LabyMod functions for " + p.getName() + " (" + p.getUniqueId() + ")");
		} catch (IOException e1) {
			e1.printStackTrace();
			System.err.println("[AntiLaby/ERROR] An unknown error has occurred: can't send AntiLaby packages to " + p.getName() + " (" + p.getUniqueId() + ")!");
		}
	}
	
}