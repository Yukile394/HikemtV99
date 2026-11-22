/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 */
package ai.idealistic.spartan.abstraction.check.implementation.combat.killaura;

import ai.idealistic.spartan.abstraction.check.CheckDetection;
import ai.idealistic.spartan.abstraction.check.CheckRunner;
import ai.idealistic.spartan.abstraction.event.PlayerAttackEvent;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.server.MultiVersion;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.utils.java.ConcurrentList;
import ai.idealistic.spartan.utils.math.RayUtils;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class Track
extends CheckDetection {
    private List<Integer> an = null;

    Track(CheckRunner executor) {
        super(executor, null, null, "back_track", true);
    }

    void handleCheck(PlayerAttackEvent event) {
        this.call(() -> {
            if (!(event.target instanceof Player)) {
                return;
            }
            if (this.an == null) {
                this.an = new ConcurrentList<Integer>();
            }
            PlayerProtocol target = PluginBase.getProtocol((Player)event.target);
            List<Location> h = target.getPositionHistory();
            boolean is1_8 = this.protocol.isUsingVersion(MultiVersion.MCVersion.V1_8);
            float hitbox = is1_8 ? 0.4f : 0.3f;
            int i = 0;
            for (Location l : h) {
                if (RayUtils.inHitbox(this.protocol, l, (Entity)event.target, hitbox, 3.0f)) break;
                ++i;
            }
            this.an.add(i);
            if (this.an.size() >= 15) {
                this.updateBackTrack();
            }
        });
    }

    private void updateBackTrack() {
        int o = 0;
        int f = 0;
        int c = 0;
        int a = 0;
        for (int i : this.an) {
            a += i;
            if (i > 0 && o == 0) {
                ++f;
            }
            if (i == 0) {
                ++c;
            }
            o = i;
        }
        a /= this.an.size();
        a *= 50;
        if (c > 5 && f > 2) {
            this.cancel("BackTrack [or past locations abuse] (approximately " + a + "ms) [f: " + f + ", c: " + c + "]");
        }
        this.an.clear();
    }
}

