package com.github.nathannr.antilaby.nms;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.github.nathannr.antilaby.api.LabyModFeature;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_8_R2.PacketDataSerializer;
import net.minecraft.server.v1_8_R2.PacketPlayOutCustomPayload;

public class Nms_v1_8_R2 {

	public static boolean setLabyModFeature(Player p, HashMap<LabyModFeature, Boolean> list) throws IOException {
		HashMap<String, Boolean> nList = new HashMap<String, Boolean>();
		for (LabyModFeature f : list.keySet()) {
			nList.put(f.name(), list.get(f));
		}
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(byteOut);
		out.writeObject(nList);
		ByteBuf a = Unpooled.copiedBuffer(byteOut.toByteArray());
		PacketDataSerializer b = new PacketDataSerializer(a);
		PacketPlayOutCustomPayload packet = new PacketPlayOutCustomPayload("LABYMOD", b);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
		System.out.print("[AntiLaby/INFO] Disable some LabyMod functions for player " + p.getName() + " ("
				+ p.getUniqueId() + ")");
		return true;
	}

}
