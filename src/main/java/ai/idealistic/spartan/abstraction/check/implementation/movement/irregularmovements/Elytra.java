/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 */
package ai.idealistic.spartan.abstraction.check.implementation.movement.irregularmovements;

import ai.idealistic.spartan.abstraction.check.Check;
import ai.idealistic.spartan.abstraction.check.CheckDetection;
import ai.idealistic.spartan.abstraction.check.CheckRunner;
import ai.idealistic.spartan.abstraction.data.Buffer;
import ai.idealistic.spartan.abstraction.data.EnvironmentData;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Elytra
extends CheckDetection {
    private double cY = 0.0;
    private double eb = 0.0;
    private double ec = 0.0;
    private double ed = 0.0;
    private int cD = 0;
    private int cC = 0;
    private int ee = 0;
    private int ef = 0;
    private Location from = null;
    private final Buffer.Storage aB = new Buffer.Storage();

    public Elytra(CheckRunner executor) {
        super(executor, null, Check.DetectionType.PACKETS, "elytra", false);
    }

    void as() {
        this.call(() -> {
            if (this.protocol.getVehicle() == null) {
                PlayerInventory inventory = this.protocol.getInventory();
                ItemStack chestplate =  inventory.getChestplate();
                if (i != null) {
                    if (i.getType() == Material.ELYTRA) {
                        if (i.getDurability() < 432) {
                            double threshold;
                            double buffer = this.aB.count(1.0, 20);
                            if (buffer >= (threshold = 15.0)) {
                                this.cancel("elytra(glide-spam)", this.protocol.getFromLocation(), 0, true);
                                if (this.prevent()) {
                                    this.protocol.bukkit().setGliding(false);
                                }
                            }
                        } else {
                            this.cancel("elytra(no-durability-glide)", this.protocol.getFromLocation(), 0, true);
                            if (this.prevent()) {
                                this.protocol.bukkit().setGliding(false);
                            }
                        }
                    } else {
                        this.cancel("elytra(no-elytra-glide)", this.protocol.getFromLocation(), 0, true);
                        if (this.prevent()) {
                            this.protocol.bukkit().setGliding(false);
                        }
                    }
                } else {
                    this.cancel("elytra(no-chestplate-item-glide)", this.protocol.getFromLocation(), 0, true);
                    if (this.prevent()) {
                        this.protocol.bukkit().setGliding(false);
                    }
                }
            } else {
                this.cancel("elytra(vehicle-glide)", this.protocol.getFromLocation(), 0, true);
                if (this.prevent()) {
                    this.protocol.bukkit().setGliding(false);
                }
            }
        });
    }

    void at() {
        this.call(() -> {
            PlayerProtocol p = this.protocol;
            if (!p.isGliding()) {
                this.ee = 0;
                this.cC = 0;
                this.cD = 0;
                this.cY = 0.0;
                this.ef = 0;
                this.from = null;
                this.eb = 0.0;
                this.ec = 0.0;
                this.ed = 0.0;
                return;
            }
            ++this.ee;
            boolean isOnGround = p.isOnGround();
            this.isCheckCancellation(isOnGround);
            Location location =  p.getLocation();
            if (this.from == null) {
                this.from = to.clone();
            }
            double dx = to.getX() - this.from.getX();
            double dz = to.getZ() - this.from.getZ();
            double speed = Math.sqrt(dx * dx + dz * dz);
            double accel = Elytra.handleDataType(speed, this.0.91);
            double diffAccel = Elytra.handleDataType(accel, this.ed);
            double y = to.getY() - this.from.getY();
            EnvironmentData environment =  this.protocol.getEnvironment();
            double pr = Math.abs(Math.abs(y) - Math.abs(this.eb));
            if (this.ee > 1) {
                if (speed > 0.05 && ec.isAllFalse() && diffAccel < 1.0E-8) {
                    this.ef += diffAccel == 0.0 ? 3 : 1;
                    if (this.ef > 12) {
                        this.cancel("invalid elytra speed: " + diffAccel, this.protocol.getFromLocation(), 0, true);
                        this.ef = 8;
                    }
                } else if (this.ef > 0) {
                    this.ef -= 2;
                }
                if (pr == 0.0 && ec.isAllFalse()) {
                    this.cY += 2.0;
                    if (this.cY >= 5.0) {
                        this.cancel("invalid elytra gravity: " + pr, this.protocol.getFromLocation(), 0, true);
                        this.cY /= 1.3;
                    }
                }
                if (speed > 2.4 || y > 1.7) {
                    this.cancel("elytra limit", this.protocol.getFromLocation(), 0, true);
                }
                this.cY /= 1.15;
            }
            this.from = to.clone();
            this.eb = y;
            this.ec = speed;
            this.ed = accel;
        });
    }

    private static double handleDataType(double x, double y) {
        return Math.abs(Math.abs(a) - Math.abs(b));
    }

    void S() {
        this.call(() -> {
            this.cC = 0;
            this.ee = 0;
        });
    }

    private void isCheckCancellation(boolean isOnGround) {
        this.call(() -> {
            this.cC = isOnGround ? 0 : this.cC + 1;
            this.cD = isOnGround ? this.cD + 1 : 0;
        });
    }
}

