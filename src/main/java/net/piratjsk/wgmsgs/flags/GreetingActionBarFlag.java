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

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public class GreetingActionBarFlag extends Handler {

    public static final Factory FACTORY = new Factory();
    public static class Factory extends Handler.Factory<GreetingActionBarFlag> {
        @Override
        public GreetingActionBarFlag create(final Session session) {
            return new GreetingActionBarFlag(session);
        }
    }

    private Set<String> lastMessageStack = Collections.emptySet();

    GreetingActionBarFlag(final Session session) {
        super(session);
    }

    @Override
    public boolean onCrossBoundary(final Player player, final Location from, final Location to, final ApplicableRegionSet toSet, final Set<ProtectedRegion> entered, final Set<ProtectedRegion> exited, final MoveType moveType) {
        final Collection<String> messages = toSet.queryAllValues(this.getPlugin().wrapPlayer(player), Wgmsgs.GREETING_ACTIONBAR_FLAG);

        for (final String message : messages) {
            if (!lastMessageStack.contains(message)) {
                String effective = CommandUtils.replaceColorMacros(message);
                effective = this.getPlugin().replaceMacros(player, effective);
                ActionBar.send(player,effective);
                break;
            }
        }

        lastMessageStack = Sets.newHashSet(messages);

        if (!lastMessageStack.isEmpty()) {
            for (final ProtectedRegion region : toSet) {
                final String message = region.getFlag(Wgmsgs.GREETING_ACTIONBAR_FLAG);
                if (message != null) {
                    lastMessageStack.add(message);
                }
            }
        }

        return true;
    }

}
