/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.GameMode
 */
package ai.idealistic.spartan.abstraction.check.implementation.movement.irregularmovements;

import ai.idealistic.spartan.abstraction.check.Check;
import ai.idealistic.spartan.abstraction.check.CheckDetection;
import ai.idealistic.spartan.abstraction.check.CheckRunner;
import ai.idealistic.spartan.abstraction.data.Buffer;
import org.bukkit.GameMode;

public class IMFoodSprint
extends CheckDetection {
    private final Buffer.Storage aB = new Buffer.Storage();

    IMFoodSprint(CheckRunner executor) {
        super(executor, Check.DataType.JAVA, null, "food_sprint", false);
    }

    void run() {
        this.call(() -> {
            if (this.protocol.isSprinting() && !this.protocol.wasGliding()) {
                double ratio;
                int threshold;
                GameMode gameMode = this.protocol.getGameMode();
                int food = this.protocol.bukkit().getFoodLevel();
                if (food <= (threshold = 6) && (gameMode == GameMode.SURVIVAL || gameMode == GameMode.ADVENTURE) && (ratio = this.aB.count(1.0, 10) / 10.0) >= 0.4) {
                    this.cancel("food-sprint, game-mode: " + gameMode.toString().toLowerCase().replace("_", "-") + ", food: " + food, this.protocol.getFromLocation(), 0, true);
                }
            }
        });
    }
}

