package net.piratjsk.wgmsgs.flags;

import com.google.common.collect.Sets;
import com.sk89q.worldguard.bukkit.commands.CommandUtils;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.session.MoveType;
import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.handler.Handler;
import net.piratjsk.wgmsgs.Wgmsgs;
import net.piratjsk.wgmsgs.utils.ActionBar;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Set;

public final class FarewellActionBarFlag extends Handler {

    public static final Factory FACTORY = new Factory();
    public static class Factory extends Handler.Factory<FarewellActionBarFlag> {
        @Override
        public FarewellActionBarFlag create(Session session) {
            return new FarewellActionBarFlag(session);
        }
    }

    private Set<String> lastMessageStack = Collections.emptySet();

    public FarewellActionBarFlag(final Session session) {
        super(session);
    }

    private Set<String> getMessages(final Player player, final ApplicableRegionSet set) {
        return Sets.newLinkedHashSet(set.queryAllValues(getPlugin().wrapPlayer(player), Wgmsgs.FAREWELL_ACTIONBAR_FLAG));
    }

    @Override
    public boolean onCrossBoundary(final Player player, final Location from, final Location to, final ApplicableRegionSet toSet, final Set<ProtectedRegion> entered, final Set<ProtectedRegion> exited, final MoveType moveType) {
        final Set<String> messages = getMessages(player, toSet);

        if (!messages.isEmpty()) {
            for (ProtectedRegion region : toSet) {
                String message = region.getFlag(Wgmsgs.FAREWELL_ACTIONBAR_FLAG);
                if (message != null) {
                    messages.add(message);
                }
            }
        }

        for (final String message : lastMessageStack) {
            if (!messages.contains(message)) {
                String effective = CommandUtils.replaceColorMacros(message);
                effective = getPlugin().replaceMacros(player, effective);
                ActionBar.send(player,effective);
                break;
            }
        }

        lastMessageStack = messages;
        return true;
    }

}
