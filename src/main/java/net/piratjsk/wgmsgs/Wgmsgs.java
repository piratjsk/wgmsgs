package net.piratjsk.wgmsgs;

import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StringFlag;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public final class Wgmsgs extends JavaPlugin {

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.getCommand("wgmsg").setExecutor(new WgmsgCommand(this));
    }

    void setMsgs() {
        this.reloadConfig();
        final String denyMessage  = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("deny-message"));
        final String denyEntry = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("entry-deny-message"));
        final String denyExit  = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("exit-deny-message"));
        try {
            this.setFinalStatic(DefaultFlag.class.getDeclaredField("DENY_MESSAGE"), new StringFlag("deny-message", denyMessage));
            this.setFinalStatic(DefaultFlag.class.getField("ENTRY_DENY_MESSAGE"), new StringFlag("entry-deny-message", denyEntry));
            this.setFinalStatic(DefaultFlag.class.getField("EXIT_DENY_MESSAGE"), new StringFlag("exit-deny-message", denyExit));
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private void setFinalStatic(final Field field, final Object newValue) throws Exception {
        field.setAccessible(true);
        final Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(null, newValue);
    }

}
