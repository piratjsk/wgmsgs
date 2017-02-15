package net.piratjsk.wgmsgs.flags;

import com.google.common.collect.Sets;
import com.sk89q.worldguard.bukkit.commands.CommandUtils;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.session.MoveType;
import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.handler.Handler;
import net.piratjsk.wgmsgs.Wgmsgs;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Set;

public final class FarewellTitleFlag extends Handler {

    public static final Factory FACTORY = new Factory();
    public static class Factory extends Handler.Factory<FarewellTitleFlag> {
        @Override
        public FarewellTitleFlag create(final Session session) {
            return new FarewellTitleFlag(session);
        }
    }

    private Set<String> lastMessageStack = Collections.emptySet();

    FarewellTitleFlag(final Session session) {
        super(session);
    }

    private Set<String> getMessages(final Player player, final ApplicableRegionSet set) {
        return Sets.newLinkedHashSet(set.queryAllValues(getPlugin().wrapPlayer(player), Wgmsgs.FAREWELL_TITLE_FLAG));
    }

    @Override
    public boolean onCrossBoundary(final Player player, final Location from, final Location to, final ApplicableRegionSet toSet, final Set<ProtectedRegion> entered, final Set<ProtectedRegion> exited, final MoveType moveType) {
        final Set<String> messages = this.getMessages(player, toSet);

        if (!messages.isEmpty()) {
            for (final ProtectedRegion region : toSet) {
                final String message = region.getFlag(Wgmsgs.FAREWELL_TITLE_FLAG);
                if (message != null) {
                    messages.add(message);
                }
            }
        }

        for (final String message : lastMessageStack) {
            if (!messages.contains(message)) {
                String effective = CommandUtils.replaceColorMacros(message);
                effective = this.getPlugin().replaceMacros(player, effective);
                if (effective.contains("||")) {
                    final String[] title = effective.split("\\|\\|");
                    player.sendTitle(title[0],title[1],10,70,20);
                } else {
                    player.sendTitle(effective, null,10,70,20);
                }
                break;
            }
        }

        lastMessageStack = messages;
        return true;
    }

}
