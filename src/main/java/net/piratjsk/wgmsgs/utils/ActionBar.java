package net.piratjsk.wgmsgs.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class ActionBar {

    public static void send(final Player player, final String message) {
        try {
            final Constructor<?> constructor = getNMSClass("PacketPlayOutChat").getConstructor(getNMSClass("IChatBaseComponent"), byte.class);
            final Object icbc = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + message + "\"}");
            final Object packet = constructor.newInstance(icbc, (byte) 2);
            final Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            final Object playerConnection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        } catch (final NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    private static Class<?> getNMSClass(final String name) {
        try {
            return Class.forName("net.minecraft.server." + getVersion() + "." + name);
        } catch (final ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }

}
