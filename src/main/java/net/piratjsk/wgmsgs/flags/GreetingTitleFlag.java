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

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public class GreetingTitleFlag extends Handler {

    public static final Factory FACTORY = new Factory();
    public static class Factory extends Handler.Factory<GreetingTitleFlag> {
        @Override
        public GreetingTitleFlag create(final Session session) {
            return new GreetingTitleFlag(session);
        }
    }

    private Set<String> lastMessageStack = Collections.emptySet();

    public GreetingTitleFlag(final Session session) {
        super(session);
    }

    @Override
    public boolean onCrossBoundary(final Player player, final Location from, final Location to, final ApplicableRegionSet toSet, final Set<ProtectedRegion> entered, final Set<ProtectedRegion> exited, final MoveType moveType) {
        final Collection<String> messages = toSet.queryAllValues(getPlugin().wrapPlayer(player), Wgmsgs.GREETING_TITLE_FLAG);

        for (final String message : messages) {
            if (!lastMessageStack.contains(message)) {
                String effective = CommandUtils.replaceColorMacros(message);
                effective = getPlugin().replaceMacros(player, effective);
                if (effective.contains("||")) {
                    final String[] title = effective.split("\\|\\|");
                    player.sendTitle(title[0],title[1],10,70,20);
                } else {
                    player.sendTitle(effective, null,10,70,20);
                }
                break;
            }
        }

        lastMessageStack = Sets.newHashSet(messages);

        if (!lastMessageStack.isEmpty()) {
            // Due to flag priorities, we have to collect the lower
            // priority flag values separately
            for (final ProtectedRegion region : toSet) {
                final String message = region.getFlag(Wgmsgs.GREETING_TITLE_FLAG);
                if (message != null) {
                    lastMessageStack.add(message);
                }
            }
        }

        return true;
    }

}
