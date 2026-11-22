/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.Bukkit
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.World
 *  org.bukkit.WorldBorder
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Vehicle
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryHolder
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.util.NumberConversions
 *  org.bukkit.util.Vector
 */
package ai.idealistic.spartan.abstraction.protocol;

import ai.idealistic.spartan.abstraction.check.Check;
import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.check.CheckRunner;
import ai.idealistic.spartan.abstraction.data.EnvironmentData;
import ai.idealistic.spartan.abstraction.data.PacketWorld;
import ai.idealistic.spartan.abstraction.data.OtherTimerBalancer;
import ai.idealistic.spartan.abstraction.data.component.ComponentXZ;
import ai.idealistic.spartan.abstraction.data.component.ComponentY;
import ai.idealistic.spartan.abstraction.event.CPlayerVelocityEvent;
import ai.idealistic.spartan.abstraction.event.PlayerTickEvent;
import ai.idealistic.spartan.abstraction.profiling.PlayerProfile;
import ai.idealistic.spartan.abstraction.protocol.CPlayerInventory;
import ai.idealistic.spartan.abstraction.protocol.ExtendedPotionEffect;
import ai.idealistic.spartan.abstraction.protocol.PlayerPunishments;
import ai.idealistic.spartan.abstraction.world.ServerLocation;
import ai.idealistic.spartan.compatibility.Compatibility;
import ai.idealistic.spartan.compatibility.necessary.BedrockCompatibility;
import ai.idealistic.spartan.compatibility.necessary.protocollib.ProtocolLib;
import ai.idealistic.spartan.functionality.concurrent.CheckThread;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.MultiVersion;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.functionality.server.ServerSchedulers;
import ai.idealistic.spartan.functionality.tracking.ResearchEngine;
import ai.idealistic.spartan.utils.java.ConcurrentList;
import ai.idealistic.spartan.utils.java.StringUtils;
import ai.idealistic.spartan.utils.math.Distance;
import ai.idealistic.spartan.utils.math.MathHelper;
import ai.idealistic.spartan.utils.minecraft.entity.AxisMatrix;
import ai.idealistic.spartan.utils.minecraft.entity.PlayerUtils;
import ai.idealistic.spartan.utils.minecraft.entity.PotionEffectUtils;
import ai.idealistic.spartan.utils.minecraft.protocol.ProtocolTools;
import ai.idealistic.spartan.utils.minecraft.server.PluginUtils;
import ai.idealistic.spartan.utils.minecraft.world.BlockUtils;
import ai.idealistic.spartan.utils.minecraft.world.GroundUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import kireiko.dev.millennium.math.CinematicComponent;
import kireiko.dev.millennium.math.OtherSensitivityProcessor;
import kireiko.dev.millennium.math.MathCollection;
import kireiko.dev.millennium.types.Full;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

public class PlayerProtocol {
    public long activeCreationTime;
    private Player bukkit;
    private EnvironmentData environment;
    public final PlayerPunishments punishments;
    public int slime0Tick;
    public int rightClickCounter;
    public int transactionVl;
    public int flyingTicks;
    public int predictedSlimeTicks;
    public final boolean npc;
    public boolean onGround;
    public boolean onGroundFrom;
    public boolean sprinting;
    public boolean sneaking;
    public boolean pistonTick;
    public boolean transactionBoot;
    public boolean clickBlocker;
    public boolean transactionSentKeep;
    public boolean useItemPacket;
    public boolean useItemPacketReset;
    private Location teleport;
    private Location location;
    private Location from;
    private Location schedulerFrom;
    public String fromWorld;
    private final Map<Long, Location> positionHistory;
    public CPlayerVelocityEvent claimedVelocity;
    public final List<CPlayerVelocityEvent> claimedVeloGravity;
    public final List<CPlayerVelocityEvent> claimedVeloSpeed;
    public long tickTime;
    public final MultiVersion.MCVersion version;
    public final OtherTimerBalancer timerBalancer;
    public final Check.DetectionType detectionType;
    private Check.DataType dataType;
    private Set<AxisMatrix> axisMatrixCache;
    public final PacketWorld packetWorld;
    public PlayerTickEvent lastTickEvent;
    public short transactionId;
    public long transactionTime;
    public long transactionLastTime;
    public long transactionPing;
    public long lagTick;
    public long oldClickTime;
    public long soulSandWater;
    public long magmaCubeWater;
    public long lastVelocity;
    private long lastInteraction;
    private long lastFlight;
    private long lastGlide;
    public boolean entityHandle;
    private ComponentY componentY;
    private ComponentXZ componentXZ;
    private Vector clampVector;
    private final Map<PotionEffectType, ExtendedPotionEffect> potionEffects;
    private boolean afk;
    private boolean cinematic;
    private boolean teleportTick;
    private final List<Integer> sensitivity = new Full<Integer>(14);
    private final OtherSensitivityProcessor sensitivityProcessor = new OtherSensitivityProcessor(this);
    private final CinematicComponent cinematicComponent = new CinematicComponent(this);
    private Entity[] nearbyEntities;
    private final double[] maxNearbyEntitiesCoordinate;
    private final CheckRunner[] runners;

    public PlayerProtocol(Player player) {
        long time = System.currentTimeMillis();
        this.bukkit = player;
        this.activeCreationTime = time;
        this.version = MultiVersion.get(player);
        this.packetWorld = new PacketWorld(player);
        this.npc = player.getAddress() == null;
        this.punishments = new PlayerPunishments(this);
        this.dataType = BedrockCompatibility.isPlayer(player) ? Check.DataType.BEDROCK : Check.DataType.JAVA;
        this.detectionType = this.packetsEnabled() ? Check.DetectionType.PACKETS : Check.DetectionType.BUKKIT;
        this.onGround = false;
        this.onGroundFrom = false;
        this.sprinting = false;
        this.sneaking = false;
        this.location = ProtocolTools.getLoadLocation(player);
        this.from = null;
        this.fromWorld = "";
        this.teleport = null;
        this.transactionVl = 0;
        this.timerBalancer = new OtherTimerBalancer();
        this.flyingTicks = 0;
        this.oldClickTime = System.currentTimeMillis();
        this.clickBlocker = false;
        this.tickTime = time;
        this.environment = new EnvironmentData();
        this.rightClickCounter = 0;
        this.positionHistory = Collections.synchronizedMap(new LinkedHashMap());
        this.claimedVelocity = null;
        this.claimedVeloGravity = new ConcurrentList<CPlayerVelocityEvent>();
        this.claimedVeloSpeed = new ConcurrentList<CPlayerVelocityEvent>();
        this.entityHandle = false;
        this.axisMatrixCache = new HashSet<AxisMatrix>();
        this.pistonTick = false;
        this.lastTickEvent = null;
        this.transactionId = -1939;
        this.transactionTime = System.currentTimeMillis();
        this.transactionLastTime = System.currentTimeMillis();
        this.transactionPing = 0L;
        this.transactionBoot = false;
        this.transactionSentKeep = false;
        this.lagTick = 0L;
        this.predictedSlimeTicks = 0;
        this.componentY = new ComponentY();
        this.componentXZ = new ComponentXZ();
        this.useItemPacket = false;
        this.useItemPacketReset = false;
        this.slime0Tick = 0;
        this.schedulerFrom = this.location;
        this.clampVector = new Vector();
        this.potionEffects = new ConcurrentHashMap<PotionEffectType, ExtendedPotionEffect>(2);
        this.setStoredPotionEffects();
        this.lastInteraction = System.currentTimeMillis();
        this.afk = false;
        this.nearbyEntities = new Entity[0];
        this.maxNearbyEntitiesCoordinate = new double[3];
        this.runners = new CheckRunner[CheckEnums.HackType.values().length];
        this.teleportTick = true;
        this.cinematic = false;
        for (CheckEnums.HackType hackType : CheckEnums.HackType.values()) {
            try {
                this.runners[hackType.ordinal()] = (CheckRunner)hackType.executor.getConstructor((hackType).getClass(), PlayerProtocol.class).newInstance(new Object[]{hackType, this});
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        this.profile().update(this);
    }

    public final Player bukkit() {
        return this.bukkit;
    }

    public void updateBukkit(Player player) {
        this.bukkit = player;
    }

    public CheckRunner getRunner(CheckEnums.HackType hackType) {
        return this.runners[hackType.ordinal()];
    }

    public void executeRunners(Object cancelled, Object object) {
        PluginBase.runTask(this, () -> {
            for (CheckRunner runner : this.getRunners()) {
                runner.handle(cancelled, object);
            }
        });
    }

    private void resetActiveCreationTime() {
        this.activeCreationTime = System.currentTimeMillis();
    }

    public long getTimePassed(long time) {
        return System.currentTimeMillis() - time;
    }

    public long getActiveTimePlayed() {
        return System.currentTimeMillis() - this.activeCreationTime;
    }

    public boolean isUsingVersion(MultiVersion.MCVersion trialVersion) {
        return this.version.ordinal() == trialVersion.ordinal();
    }

    public boolean isDesync() {
        return this.transactionSentKeep && System.currentTimeMillis() - this.transactionTime > 55L;
    }

    public boolean isBlatantDesync() {
        return this.transactionSentKeep && System.currentTimeMillis() - this.transactionTime > 150L;
    }

    public boolean isSDesync() {
        return this.transactionSentKeep && System.currentTimeMillis() - this.transactionTime > 400L;
    }

    public boolean isUsingVersionOrGreater(MultiVersion.MCVersion trialVersion) {
        return this.version.ordinal() >= trialVersion.ordinal();
    }

    public boolean isOnGround() {
        if (this.packetsEnabled()) {
            return this.onGround;
        }
        Entity vehicle = this.getVehicle();
        if (vehicle != null) {
            return vehicle.isOnGround();
        }
        return this.bukkit().isOnGround();
    }

    public boolean isOnGroundFrom() {
        return this.packetsEnabled() ? this.onGroundFrom : this.isOnGround();
    }

    public boolean isSprinting() {
        return this.packetsEnabled() ? this.sprinting : this.bukkit().isSprinting();
    }

    public boolean isSneaking() {
        return this.packetsEnabled() ? this.sneaking : this.bukkit().isSneaking();
    }

    public Location getLocation() {
        Location loc = this.packetsEnabled() ? this.location : ProtocolLib.getLocationOrNull(this.bukkit());
        return loc != null ? loc : ServerLocation.bukkitDefault.clone();
    }

    public Location getFromLocation() {
        return this.from != null ? this.from : this.getLocation();
    }

    public Location getVehicleLocation() {
        Entity vehicle = this.getVehicle();
        if (vehicle instanceof LivingEntity || vehicle instanceof Vehicle) {
            return ProtocolLib.getLocationOrNull(vehicle);
        }
        return null;
    }

    public Location getLocationOrVehicle() {
        Location vehicleLocation = this.getVehicleLocation();
        if (vehicleLocation == null) {
            return this.getLocation();
        }
        return vehicleLocation;
    }

    public List<Map.Entry<Long, Location>> getPositionHistoryEntries() {
        Map<Long, Location> map = this.positionHistory;
        synchronized (map) {
            return new ArrayList<Map.Entry<Long, Location>>(this.positionHistory.entrySet());
        }
    }

    public List<Location> getPositionHistory() {
        Map<Long, Location> map = this.positionHistory;
        synchronized (map) {
            return new ArrayList<Location>(this.positionHistory.values());
        }
    }

    public ServerLocation getPastTickRotation() {
        Location l = this.getLocation().clone();
        l.setYaw(this.from.getYaw());
        l.setPitch(this.from.getPitch());
        return new ServerLocation(l);
    }

    public boolean teleport(Location location) {
        if (location.getX() == ServerLocation.bukkitDefault.getX() && location.getY() == ServerLocation.bukkitDefault.getY() && location.getZ() == ServerLocation.bukkitDefault.getZ()) {
            return false;
        }
        boolean sync = PluginBase.isSynchronised();
        if (MultiVersion.folia) {
            if (sync) {
                this.bukkit().leaveVehicle();
            }
            this.bukkit().teleportAsync(location);
        } else if (sync) {
            this.bukkit().leaveVehicle();
            this.bukkit().teleport(location);
        } else {
            PluginBase.transferTask(this, () -> {
                Player bukkit = this.bukkit();
                if (bukkit.isOnline()) {
                    bukkit.teleport(location);
                }
            });
        }
        return true;
    }

    public int getPing() {
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17)) {
            return this.bukkit().isOnline() ? this.bukkit().getPing() : 0;
        }
        try {
            Object obj = PluginBase.getCraftPlayerMethod(this.bukkit(), "ping");
            return obj instanceof Integer ? Math.max((Integer)obj, 0) : 0;
        }
        catch (Exception ignored) {
            return 0;
        }
    }

    public UUID getUUID() {
        return ProtocolLib.isTemporary((OfflinePlayer)this.bukkit()) ? UUID.randomUUID() : this.bukkit().getUniqueId();
    }

    public PlayerProfile profile() {
        return ResearchEngine.getPlayerProfile(this);
    }

    public void setOnGround(boolean isOnGround) {
        this.onGroundFrom = this.onGround;
        this.onGround = isOnGround;
    }

    public void addRawLocation(Location location) {
        Map<Long, Location> map = this.positionHistory;
        synchronized (map) {
            if (this.positionHistory.size() == 20) {
                Iterator<Long> iterator = this.positionHistory.keySet().iterator();
                iterator.next();
                iterator.remove();
            }
            this.positionHistory.put(System.currentTimeMillis(), location.clone());
        }
    }

    public World getWorld() {
        return this.getLocation().getWorld();
    }

    public boolean packetsEnabled() {
        return PluginBase.packetsEnabled() && (Config.settings.getBoolean("Important.bedrock_on_protocollib") || !this.isBedrockPlayer());
    }

    public boolean isBedrockPlayer() {
        return this.dataType == Check.DataType.BEDROCK;
    }

    public boolean isLowEyeHeight() {
        return this.getEyeHeight() < 1.0;
    }

    public boolean isJumping(double d) {
        return PlayerUtils.isJumping(d, PlayerUtils.getPotionLevel(this, PotionEffectUtils.JUMP) + 1, GroundUtils.maxHeightLengthRatio);
    }

    public boolean justJumped(double d) {
        return PlayerUtils.justJumped(d, PlayerUtils.getPotionLevel(this, PotionEffectUtils.JUMP) + 1, GroundUtils.maxHeightLengthRatio);
    }

    public boolean processLastMoveEvent(Location originalTo, Location vehicle, ServerLocation to, Location from, boolean packets) {
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17) || this.isUsingVersionOrGreater(MultiVersion.MCVersion.V1_17)) {
            double x = this.clampMin(to.getX(), -3.0E7, 3.0E7);
            double y = this.clampMin(to.getY(), -2.0E7, 2.0E7);
            double z = this.clampMin(to.getZ(), -3.0E7, 3.0E7);
            Vector vector = new Vector(x, y, z);
            double d = this.clampVector.distanceSquared(vector);
            this.clampVector = vector;
            if (d < 4.0E-8) {
                return false;
            }
        }
        if (!packets) {
            this.setFrom(from);
        }
        return true;
    }

    private double clampMin(double d, double d2, double d3) {
        return d < d2 ? d2 : Math.min(d, d3);
    }

    public void setLastInteraction() {
        this.lastInteraction = System.currentTimeMillis();
    }

    private void checkForAFK() {
        long lastInteraction = System.currentTimeMillis() - this.lastInteraction;
        if (lastInteraction >= 20000L) {
            if (!this.afk) {
                this.profile().getContinuity().setActiveTime(System.currentTimeMillis(), this.getActiveTimePlayed() - lastInteraction, true);
                this.afk = true;
            }
        } else if (this.afk) {
            this.resetActiveCreationTime();
            this.afk = false;
        }
    }

    public boolean isAFK() {
        return this.afk || this.npc;
    }

    public String toString() {
        return this.bukkit().getName();
    }

    public boolean debug(boolean function, boolean broadcast, boolean cutDecimals, Object ... message) {
        if (function && this.bukkit().isWhitelisted()) {
            String string = "\u00a7f" + this.bukkit().getName() + " ";
            if (message == null || message.length == 0) {
                string = string + new Random().nextInt();
            } else {
                if (cutDecimals) {
                    for (Object object : message) {
                        message[i] = object instanceof Double ? ((i + 1) % 2 == 0 ? "\u00a7c" : "\u00a77") + Distance.computeAlgebra((Double)object, (double)GroundUtils.maxHeightLength) : ((i + 1) % 2 == 0 ? "\u00a7c" : "\u00a77") + object;
                        ++i;
                    }
                } else {
                    for (Object object : message) {
                        message[i] = ((i + 1) % 2 == 0 ? "\u00a7c" : "\u00a77") + object;
                        ++i;
                    }
                }
                string = string + StringUtils.toString(message, " ");
            }
            Collection<PlayerProtocol> protocols = PluginBase.getProtocols();
            if (!protocols.isEmpty()) {
                if (broadcast) {
                    Bukkit.broadcastMessage((String)string);
                } else {
                    for (PlayerProtocol protocol : protocols) {
                        if (!protocol.bukkit().isOp()) continue;
                        protocol.bukkit().sendMessage(string);
                    }
                }
            }
            Bukkit.getConsoleSender().sendMessage(string);
            return true;
        }
        return false;
    }

    public int getMaxChatLength() {
        return this.isBedrockPlayer() ? 512 : (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_11) || this.isUsingVersionOrGreater(MultiVersion.MCVersion.V1_11) || PluginUtils.exists("viaversion") || Compatibility.CompatibilityType.PROTOCOL_SUPPORT.isFunctional() ? 256 : 100);
    }

    public void sendImportantMessage(String message) {
        this.bukkit().sendMessage("");
        this.bukkit().sendMessage(message);
        this.bukkit().sendMessage("");
    }

    public Inventory createInventory(int size, String title) {
        return Bukkit.createInventory((InventoryHolder)this.bukkit(), (int)size, (String)title);
    }

    public PlayerInventory getInventory() {
        return new CPlayerInventory(ProtocolLib.isTemporary((OfflinePlayer)this.bukkit()) ? null : this.bukkit().getInventory());
    }

    public ItemStack getItemInHand() {
        return this.getInventory().getItemInHand();
    }

    public ServerLocation getTargetBlock(double distance, double limit) {
        for (int i = 0; i < NumberConversions.ceil((double)Math.min(distance, limit)); ++i) {
            ServerLocation location = new ServerLocation(this.getLocation().clone().add(0.0, this.getEyeHeight(), 0.0).add(this.getLocation().getDirection().multiply(i)));
            if (!BlockUtils.isFullSolid(location.getBlock(this).getType())) continue;
            return location.getBlockLocation();
        }
        return null;
    }

    public boolean isFrozen() {
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17)) {
            return this.bukkit().isFrozen();
        }
        return false;
    }

    public float getAttackCooldown() {
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17)) {
            return this.bukkit().getAttackCooldown();
        }
        return 1.0f;
    }

    public Entity getVehicle() {
        return ProtocolLib.getVehicle((Entity)this.bukkit());
    }

    public double getHealth() {
        return ProtocolLib.isTemporary((OfflinePlayer)this.bukkit()) ? 0.0 : this.bukkit().getHealth();
    }

    public int getEntityId() {
        return ProtocolLib.isTemporary((OfflinePlayer)this.bukkit()) ? new Random().nextInt() : this.bukkit().getEntityId();
    }

    private void setStoredPotionEffects() {
        if (!ProtocolLib.isTemporary((OfflinePlayer)this.bukkit())) {
            PluginBase.transferTask(this, () -> {
                for (PotionEffect effect : this.bukkit().getActivePotionEffects()) {
                    this.potionEffects.put(effect.getType(), new ExtendedPotionEffect(effect));
                }
            });
        }
    }

    public Collection<PotionEffect> getActivePotionEffects() {
        return ProtocolLib.isTemporary((OfflinePlayer)this.bukkit()) ? new ArrayList(0) : this.bukkit().getActivePotionEffects();
    }

    public ExtendedPotionEffect getPotionEffect(PotionEffectType type, long lastActive) {
        this.setStoredPotionEffects();
        ExtendedPotionEffect potionEffect = this.potionEffects.get(type);
        if (potionEffect != null && potionEffect.timePassed() <= lastActive && potionEffect.bukkitEffect.getType().equals(type)) {
            return potionEffect;
        }
        return null;
    }

    public boolean hasPotionEffect(PotionEffectType type, long lastActive) {
        this.setStoredPotionEffects();
        ExtendedPotionEffect potionEffect = this.potionEffects.get(type);
        return potionEffect != null && potionEffect.timePassed() <= lastActive && potionEffect.bukkitEffect.getType().equals(type);
    }

    public GameMode getGameMode() {
        return ProtocolLib.isTemporary((OfflinePlayer)this.bukkit()) ? GameMode.SURVIVAL : this.bukkit.getGameMode();
    }

    public List<Entity> getNearbyEntities(double radius) {
        if (MultiVersion.folia) {
            return new ArrayList<Entity>(0);
        }
        this.maxNearbyEntitiesCoordinate[0] = Math.max(radius, this.maxNearbyEntitiesCoordinate[0]);
        this.maxNearbyEntitiesCoordinate[1] = Math.max(radius, this.maxNearbyEntitiesCoordinate[1]);
        this.maxNearbyEntitiesCoordinate[2] = Math.max(radius, this.maxNearbyEntitiesCoordinate[2]);
        if (PluginBase.isSynchronised()) {
            List list = this.bukkit().getNearbyEntities(radius, radius, radius);
            this.nearbyEntities = list.toArray(new Entity[0]);
            return list;
        }
        if (this.nearbyEntities.length == 0) {
            return new ArrayList<Entity>(0);
        }
        ArrayList<Entity> entities = new ArrayList<Entity>();
        Location location = this.getLocation();
        int smallX = MathHelper.floor_double((location.getX() - radius) / 16.0);
        int bigX = MathHelper.floor_double((location.getX() + radius) / 16.0);
        int smallZ = MathHelper.floor_double((location.getZ() - radius) / 16.0);
        int bigZ = MathHelper.floor_double((location.getZ() + radius) / 16.0);
        double radiusSquared = radius * radius;
        for (int xx = smallX; xx <= bigX; ++xx) {
            for (int zz = smallZ; zz <= bigZ; ++zz) {
                for (Entity entity : this.nearbyEntities) {
                    Location eLoc = ProtocolLib.getLocationOrNull(entity);
                    if (eLoc == null || !(ServerLocation.distanceSquared(eLoc, location) <= radiusSquared)) continue;
                    entities.add(entity);
                }
            }
        }
        entities.remove(this.bukkit());
        return entities;
    }

    public List<Entity> getNearbyEntities(double x, double x, double y) {
        if (MultiVersion.folia) {
            this.maxNearbyEntitiesCoordinate[0] = Math.max(x, this.maxNearbyEntitiesCoordinate[0]);
            this.maxNearbyEntitiesCoordinate[1] = Math.max(y, this.maxNearbyEntitiesCoordinate[1]);
            this.maxNearbyEntitiesCoordinate[2] = Math.max(z, this.maxNearbyEntitiesCoordinate[2]);
        }
        if (PluginBase.isSynchronised()) {
            List list = this.bukkit().getNearbyEntities(x, y, z);
            this.nearbyEntities = list.toArray(new Entity[0]);
            return list;
        }
        if (this.nearbyEntities.length == 0) {
            return new ArrayList<Entity>(0);
        }
        ArrayList<Entity> entities = new ArrayList<Entity>();
        Location location = this.getLocation();
        int smallX = MathHelper.floor_double((location.getX() - x) / 16.0);
        int bigX = MathHelper.floor_double((location.getX() + x) / 16.0);
        int smallZ = MathHelper.floor_double((location.getZ() - z) / 16.0);
        int bigZ = MathHelper.floor_double((location.getZ() + z) / 16.0);
        for (int xx = smallX; xx <= bigX; ++xx) {
            for (int zz = smallZ; zz <= bigZ; ++zz) {
                for (Entity entity : this.nearbyEntities) {
                    Location eLoc = ProtocolLib.getLocationOrNull(entity);
                    if (eLoc == null || !(Math.abs(eLoc.getX() - location.getX()) <= x) || !(Math.abs(eLoc.getY() - location.getY()) <= y) || !(Math.abs(eLoc.getZ() - location.getZ()) <= z)) continue;
                    entities.add(entity);
                }
            }
        }
        entities.remove(this.bukkit());
        return entities;
    }

    public boolean groundTeleport() {
        Location location = this.getLocation();
        ServerLocation locationP1 = new ServerLocation(location.clone().add(0.0, 1.0, 0.0));
        if (BlockUtils.isSolid(locationP1.getBlock(this).getType()) && !BlockUtils.areWalls(locationP1.getBlock(this).getType()) && !BlockUtils.canClimb(locationP1.getBlock(this).getType(), false)) {
            return false;
        }
        World world = this.getWorld();
        double startY = Math.min((double)BlockUtils.getMaxHeight(world), location.getY());
        int iterations = 0;
        double progressiveY = startY;
        while (startY > (double)BlockUtils.getMinHeight(world)) {
            ServerLocation loopLocation = new ServerLocation(location.clone().add(0.0, -(startY - progressiveY), 0.0));
            Material material = loopLocation.getBlock(this).getType();
            if ((double)iterations != 16.0 && (BlockUtils.canClimb(material, false) || BlockUtils.areWalls(material) || !BlockUtils.isSolid(material))) {
                ++iterations;
            } else {
                loopLocation.setY(Math.floor(progressiveY) + 1.0);
                if (!this.packetsEnabled()) break;
                this.teleport(loopLocation.bukkit());
                break;
            }
            progressiveY -= 1.0;
        }
        if (iterations > 0) {
            this.bukkit().setFallDistance(0.0f);
            if (Config.settings.getBoolean("Detections.fall_damage_on_teleport") && iterations > 3) {
                this.bukkit().damage((double)Math.max(this.bukkit().getFallDistance(), (float)iterations));
            }
        }
        return true;
    }

    public boolean isOutsideOfTheBorder(double deviation) {
        Location loc = this.getLocation();
        WorldBorder border = this.getWorld().getWorldBorder();
        double size = border.getSize() / 2.deviation;
        Location center = border.getCenter();
        return Math.abs(loc.getX() - center.getX()) > size || Math.abs(loc.getZ() - center.getZ()) > size;
    }

    public double getEyeHeight() {
        return ProtocolLib.getEyeHeight((Entity)this.bukkit());
    }

    public boolean isGliding() {
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_9)) {
            if (this.bukkit().isGliding()) {
                this.lastGlide = System.currentTimeMillis();
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean isSwimming() {
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)) {
            return this.bukkit().isSwimming();
        }
        return false;
    }

    public boolean wasGliding() {
        return this.isGliding() || (double)(System.currentTimeMillis() - this.lastGlide) <= 1000.0;
    }

    public boolean isFlying() {
        Entity vehicle = this.getVehicle();
        boolean flying = vehicle != null ? vehicle instanceof Player && (Player)vehicle.isFlying() : this.bukkit().isFlying();
        if (flying) {
            this.lastFlight = System.currentTimeMillis();
        }
        return flying;
    }

    public boolean wasFlying() {
        return this.isFlying() || (double)(System.currentTimeMillis() - this.lastFlight) <= 1000.0;
    }

    public int calculateSensitivity() {
        if (MathCollection.getDistinct(this.getSensitivity()) != this.getSensitivity().size()) {
            HashSet<Integer> prev = new HashSet<Integer>();
            for (int i : this.getSensitivity()) {
                if (prev.contains(i / 5)) {
                    return i;
                }
                prev.add(i / 5);
            }
        }
        return -1;
    }

    @Generated
    public long getActiveCreationTime() {
        return this.activeCreationTime;
    }

    @Generated
    public Player getBukkit() {
        return this.bukkit;
    }

    @Generated
    public PlayerPunishments getPunishments() {
        return this.punishments;
    }

    @Generated
    public int getSlime0Tick() {
        return this.slime0Tick;
    }

    @Generated
    public int getRightClickCounter() {
        return this.rightClickCounter;
    }

    @Generated
    public int getTransactionVl() {
        return this.transactionVl;
    }

    @Generated
    public int getFlyingTicks() {
        return this.flyingTicks;
    }

    @Generated
    public int getPredictedSlimeTicks() {
        return this.predictedSlimeTicks;
    }

    @Generated
    public boolean isNpc() {
        return this.npc;
    }

    @Generated
    public boolean isPistonTick() {
        return this.pistonTick;
    }

    @Generated
    public boolean isTransactionBoot() {
        return this.transactionBoot;
    }

    @Generated
    public boolean isClickBlocker() {
        return this.clickBlocker;
    }

    @Generated
    public boolean isTransactionSentKeep() {
        return this.transactionSentKeep;
    }

    @Generated
    public boolean isUseItemPacket() {
        return this.useItemPacket;
    }

    @Generated
    public boolean isUseItemPacketReset() {
        return this.useItemPacketReset;
    }

    @Generated
    public Location getFrom() {
        return this.from;
    }

    @Generated
    public Location getSchedulerFrom() {
        return this.schedulerFrom;
    }

    @Generated
    public String getFromWorld() {
        return this.fromWorld;
    }

    @Generated
    public CPlayerVelocityEvent getClaimedVelocity() {
        return this.claimedVelocity;
    }

    @Generated
    public List<CPlayerVelocityEvent> getClaimedVeloGravity() {
        return this.claimedVeloGravity;
    }

    @Generated
    public List<CPlayerVelocityEvent> getClaimedVeloSpeed() {
        return this.claimedVeloSpeed;
    }

    @Generated
    public long getTickTime() {
        return this.tickTime;
    }

    @Generated
    public MultiVersion.MCVersion getVersion() {
        return this.version;
    }

    @Generated
    public OtherTimerBalancer getTimerBalancer() {
        return this.timerBalancer;
    }

    @Generated
    public Check.DetectionType getDetectionType() {
        return this.detectionType;
    }

    @Generated
    public Set<AxisMatrix> getAxisMatrixCache() {
        return this.axisMatrixCache;
    }

    @Generated
    public PacketWorld getPacketWorld() {
        return this.packetWorld;
    }

    @Generated
    public PlayerTickEvent getLastTickEvent() {
        return this.lastTickEvent;
    }

    @Generated
    public short getTransactionId() {
        return this.transactionId;
    }

    @Generated
    public long getTransactionTime() {
        return this.transactionTime;
    }

    @Generated
    public long getTransactionLastTime() {
        return this.transactionLastTime;
    }

    @Generated
    public long getTransactionPing() {
        return this.transactionPing;
    }

    @Generated
    public long getLagTick() {
        return this.lagTick;
    }

    @Generated
    public long getOldClickTime() {
        return this.oldClickTime;
    }

    @Generated
    public long getSoulSandWater() {
        return this.soulSandWater;
    }

    @Generated
    public long getMagmaCubeWater() {
        return this.magmaCubeWater;
    }

    @Generated
    public long getLastVelocity() {
        return this.lastVelocity;
    }

    @Generated
    public long getLastInteraction() {
        return this.lastInteraction;
    }

    @Generated
    public long getLastFlight() {
        return this.lastFlight;
    }

    @Generated
    public long getLastGlide() {
        return this.lastGlide;
    }

    @Generated
    public boolean isEntityHandle() {
        return this.entityHandle;
    }

    @Generated
    public ComponentY getComponentY() {
        return this.componentY;
    }

    @Generated
    public ComponentXZ getComponentXZ() {
        return this.componentXZ;
    }

    @Generated
    public Vector getClampVector() {
        return this.clampVector;
    }

    @Generated
    public Map<PotionEffectType, ExtendedPotionEffect> getPotionEffects() {
        return this.potionEffects;
    }

    @Generated
    public boolean isCinematic() {
        return this.cinematic;
    }

    @Generated
    public boolean isTeleportTick() {
        return this.teleportTick;
    }

    @Generated
    public List<Integer> getSensitivity() {
        return this.sensitivity;
    }

    @Generated
    public OtherSensitivityProcessor getSensitivityProcessor() {
        return this.sensitivityProcessor;
    }

    @Generated
    public CinematicComponent getCinematicComponent() {
        return this.cinematicComponent;
    }

    @Generated
    public Entity[] getNearbyEntities() {
        return this.nearbyEntities;
    }

    @Generated
    public double[] getMaxNearbyEntitiesCoordinate() {
        return this.maxNearbyEntitiesCoordinate;
    }

    @Generated
    public void setActiveCreationTime(long activeCreationTime) {
    }

    @Generated
    public void setBukkit(Player bukkit) {
    }

    @Generated
    public void setEnvironment(EnvironmentData environment) {
    }

    @Generated
    public void setSlime0Tick(int slime0Tick) {
    }

    @Generated
    public void setRightClickCounter(int rightClickCounter) {
    }

    @Generated
    public void setTransactionVl(int transactionVl) {
    }

    @Generated
    public void setFlyingTicks(int flyingTicks) {
    }

    @Generated
    public void setPredictedSlimeTicks(int predictedSlimeTicks) {
    }

    @Generated
    public void setOnGroundFrom(boolean onGroundFrom) {
    }

    @Generated
    public void setSprinting(boolean sprinting) {
    }

    @Generated
    public void setSneaking(boolean sneaking) {
    }

    @Generated
    public void setPistonTick(boolean pistonTick) {
    }

    @Generated
    public void setTransactionBoot(boolean transactionBoot) {
    }

    @Generated
    public void setClickBlocker(boolean clickBlocker) {
    }

    @Generated
    public void setTransactionSentKeep(boolean transactionSentKeep) {
    }

    @Generated
    public void setUseItemPacket(boolean useItemPacket) {
    }

    @Generated
    public void setUseItemPacketReset(boolean useItemPacketReset) {
    }

    @Generated
    public void setFromWorld(String fromWorld) {
    }

    @Generated
    public void setClaimedVelocity(CPlayerVelocityEvent claimedVelocity) {
    }

    @Generated
    public void setTickTime(long tickTime) {
    }

    @Generated
    public void setDataType(Check.DataType dataType) {
    }

    @Generated
    public void setAxisMatrixCache(Set<AxisMatrix> axisMatrixCache) {
    }

    @Generated
    public void setLastTickEvent(PlayerTickEvent lastTickEvent) {
    }

    @Generated
    public void setTransactionId(short transactionId) {
    }

    @Generated
    public void setTransactionTime(long transactionTime) {
    }

    @Generated
    public void setTransactionLastTime(long transactionLastTime) {
    }

    @Generated
    public void setTransactionPing(long transactionPing) {
    }

    @Generated
    public void setLagTick(long lagTick) {
    }

    @Generated
    public void setOldClickTime(long oldClickTime) {
    }

    @Generated
    public void setSoulSandWater(long soulSandWater) {
    }

    @Generated
    public void setMagmaCubeWater(long magmaCubeWater) {
    }

    @Generated
    public void setLastVelocity(long lastVelocity) {
    }

    @Generated
    public void setLastInteraction(long lastInteraction) {
    }

    @Generated
    public void setLastFlight(long lastFlight) {
    }

    @Generated
    public void setLastGlide(long lastGlide) {
    }

    @Generated
    public void setEntityHandle(boolean entityHandle) {
    }

    @Generated
    public void setComponentY(ComponentY componentY) {
    }

    @Generated
    public void setComponentXZ(ComponentXZ componentXZ) {
    }

    @Generated
    public void setClampVector(Vector clampVector) {
    }

    @Generated
    public void setAfk(boolean afk) {
    }

    @Generated
    public void setCinematic(boolean cinematic) {
    }

    @Generated
    public void setTeleportTick(boolean teleportTick) {
    }

    @Generated
    public void setNearbyEntities(Entity[] nearbyEntities) {
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PlayerProtocol)) {
            return false;
        }
        PlayerProtocol other = (PlayerProtocol)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getActiveCreationTime() != other.getActiveCreationTime()) {
            return false;
        }
        if (this.getSlime0Tick() != other.getSlime0Tick()) {
            return false;
        }
        if (this.getRightClickCounter() != other.getRightClickCounter()) {
            return false;
        }
        if (this.getTransactionVl() != other.getTransactionVl()) {
            return false;
        }
        if (this.getFlyingTicks() != other.getFlyingTicks()) {
            return false;
        }
        if (this.getPredictedSlimeTicks() != other.getPredictedSlimeTicks()) {
            return false;
        }
        if (this.isNpc() != other.isNpc()) {
            return false;
        }
        if (this.isOnGround() != other.isOnGround()) {
            return false;
        }
        if (this.isOnGroundFrom() != other.isOnGroundFrom()) {
            return false;
        }
        if (this.isSprinting() != other.isSprinting()) {
            return false;
        }
        if (this.isSneaking() != other.isSneaking()) {
            return false;
        }
        if (this.isPistonTick() != other.isPistonTick()) {
            return false;
        }
        if (this.isTransactionBoot() != other.isTransactionBoot()) {
            return false;
        }
        if (this.isClickBlocker() != other.isClickBlocker()) {
            return false;
        }
        if (this.isTransactionSentKeep() != other.isTransactionSentKeep()) {
            return false;
        }
        if (this.isUseItemPacket() != other.isUseItemPacket()) {
            return false;
        }
        if (this.isUseItemPacketReset() != other.isUseItemPacketReset()) {
            return false;
        }
        if (this.getTickTime() != other.getTickTime()) {
            return false;
        }
        if (this.getTransactionId() != other.getTransactionId()) {
            return false;
        }
        if (this.getTransactionTime() != other.getTransactionTime()) {
            return false;
        }
        if (this.getTransactionLastTime() != other.getTransactionLastTime()) {
            return false;
        }
        if (this.getTransactionPing() != other.getTransactionPing()) {
            return false;
        }
        if (this.getLagTick() != other.getLagTick()) {
            return false;
        }
        if (this.getOldClickTime() != other.getOldClickTime()) {
            return false;
        }
        if (this.getSoulSandWater() != other.getSoulSandWater()) {
            return false;
        }
        if (this.getMagmaCubeWater() != other.getMagmaCubeWater()) {
            return false;
        }
        if (this.getLastVelocity() != other.getLastVelocity()) {
            return false;
        }
        if (this.getLastInteraction() != other.getLastInteraction()) {
            return false;
        }
        if (this.getLastFlight() != other.getLastFlight()) {
            return false;
        }
        if (this.getLastGlide() != other.getLastGlide()) {
            return false;
        }
        if (this.isEntityHandle() != other.isEntityHandle()) {
            return false;
        }
        if (this.isAFK() != other.isAFK()) {
            return false;
        }
        if (this.isCinematic() != other.isCinematic()) {
            return false;
        }
        if (this.isTeleportTick() != other.isTeleportTick()) {
            return false;
        }
        Player this$bukkit = this.getBukkit();
        Player other$bukkit = other.getBukkit();
        if (this$bukkit == null ? other$bukkit != null : !this$bukkit.equals(other$bukkit)) {
            return false;
        }
        EnvironmentData this$environment = this.getEnvironment();
        EnvironmentData other$environment = other.getEnvironment();
        if (this$environment == null ? other$environment != null : !((Object)this$environment).equals(other$environment)) {
            return false;
        }
        PlayerPunishments this$punishments = this.getPunishments();
        PlayerPunishments other$punishments = other.getPunishments();
        if (this$punishments == null ? other$punishments != null : !this$punishments.equals(other$punishments)) {
            return false;
        }
        Location this$teleport = this.getTeleport();
        Location other$teleport = other.getTeleport();
        if (this$teleport == null ? other$teleport != null : !this$teleport.equals(other$teleport)) {
            return false;
        }
        Location this$location = this.getLocation();
        Location other$location = other.getLocation();
        if (this$location == null ? other$location != null : !this$location.equals(other$location)) {
            return false;
        }
        Location this$from = this.getFrom();
        Location other$from = other.getFrom();
        if (this$from == null ? other$from != null : !this$from.equals(other$from)) {
            return false;
        }
        Location this$schedulerFrom = this.getSchedulerFrom();
        Location other$schedulerFrom = other.getSchedulerFrom();
        if (this$schedulerFrom == null ? other$schedulerFrom != null : !this$schedulerFrom.equals(other$schedulerFrom)) {
            return false;
        }
        String this$fromWorld = this.getFromWorld();
        String other$fromWorld = other.getFromWorld();
        if (this$fromWorld == null ? other$fromWorld != null : !this$fromWorld.equals(other$fromWorld)) {
            return false;
        }
        List<Location> this$positionHistory = this.getPositionHistory();
        List<Location> other$positionHistory = other.getPositionHistory();
        if (this$positionHistory == null ? other$positionHistory != null : !((Object)this$positionHistory).equals(other$positionHistory)) {
            return false;
        }
        CPlayerVelocityEvent this$claimedVelocity = this.getClaimedVelocity();
        CPlayerVelocityEvent other$claimedVelocity = other.getClaimedVelocity();
        if (this$claimedVelocity == null ? other$claimedVelocity != null : !((Object)this$claimedVelocity).equals((Object)other$claimedVelocity)) {
            return false;
        }
        List<CPlayerVelocityEvent> this$claimedVeloGravity = this.getClaimedVeloGravity();
        List<CPlayerVelocityEvent> other$claimedVeloGravity = other.getClaimedVeloGravity();
        if (this$claimedVeloGravity == null ? other$claimedVeloGravity != null : !((Object)this$claimedVeloGravity).equals(other$claimedVeloGravity)) {
            return false;
        }
        List<CPlayerVelocityEvent> this$claimedVeloSpeed = this.getClaimedVeloSpeed();
        List<CPlayerVelocityEvent> other$claimedVeloSpeed = other.getClaimedVeloSpeed();
        if (this$claimedVeloSpeed == null ? other$claimedVeloSpeed != null : !((Object)this$claimedVeloSpeed).equals(other$claimedVeloSpeed)) {
            return false;
        }
        MultiVersion.MCVersion this$version = this.getVersion();
        MultiVersion.MCVersion other$version = other.getVersion();
        if (this$version == null ? other$version != null : !((Object)this$version).equals((Object)other$version)) {
            return false;
        }
        OtherTimerBalancer this$timerBalancer = this.getTimerBalancer();
        OtherTimerBalancer other$timerBalancer = other.getTimerBalancer();
        if (this$timerBalancer == null ? other$timerBalancer != null : !this$timerBalancer.equals(other$timerBalancer)) {
            return false;
        }
        Check.DetectionType this$detectionType = this.getDetectionType();
        Check.DetectionType other$detectionType = other.getDetectionType();
        if (this$detectionType == null ? other$detectionType != null : !((Object)this$detectionType).equals((Object)other$detectionType)) {
            return false;
        }
        Check.DataType this$dataType = this.getDataType();
        Check.DataType other$dataType = other.getDataType();
        if (this$dataType == null ? other$dataType != null : !((Object)this$dataType).equals((Object)other$dataType)) {
            return false;
        }
        Set<AxisMatrix> this$axisMatrixCache = this.getAxisMatrixCache();
        Set<AxisMatrix> other$axisMatrixCache = other.getAxisMatrixCache();
        if (this$axisMatrixCache == null ? other$axisMatrixCache != null : !((Object)this$axisMatrixCache).equals(other$axisMatrixCache)) {
            return false;
        }
        PacketWorld this$packetWorld = this.getPacketWorld();
        PacketWorld other$packetWorld = other.getPacketWorld();
        if (this$packetWorld == null ? other$packetWorld != null : !this$packetWorld.equals(other$packetWorld)) {
            return false;
        }
        PlayerTickEvent this$lastTickEvent = this.getLastTickEvent();
        PlayerTickEvent other$lastTickEvent = other.getLastTickEvent();
        if (this$lastTickEvent == null ? other$lastTickEvent != null : !this$lastTickEvent.equals(other$lastTickEvent)) {
            return false;
        }
        ComponentY this$componentY = this.getComponentY();
        ComponentY other$componentY = other.getComponentY();
        if (this$componentY == null ? other$componentY != null : !this$componentY.equals(other$componentY)) {
            return false;
        }
        ComponentXZ this$componentXZ = this.getComponentXZ();
        ComponentXZ other$componentXZ = other.getComponentXZ();
        if (this$componentXZ == null ? other$componentXZ != null : !this$componentXZ.equals(other$componentXZ)) {
            return false;
        }
        Vector this$clampVector = this.getClampVector();
        Vector other$clampVector = other.getClampVector();
        if (this$clampVector == null ? other$clampVector != null : !this$clampVector.equals(other$clampVector)) {
            return false;
        }
        Map<PotionEffectType, ExtendedPotionEffect> this$potionEffects = this.getPotionEffects();
        Map<PotionEffectType, ExtendedPotionEffect> other$potionEffects = other.getPotionEffects();
        if (this$potionEffects == null ? other$potionEffects != null : !((Object)this$potionEffects).equals(other$potionEffects)) {
            return false;
        }
        List<Integer> this$sensitivity = this.getSensitivity();
        List<Integer> other$sensitivity = other.getSensitivity();
        if (this$sensitivity == null ? other$sensitivity != null : !((Object)this$sensitivity).equals(other$sensitivity)) {
            return false;
        }
        OtherSensitivityProcessor this$sensitivityProcessor = this.getSensitivityProcessor();
        OtherSensitivityProcessor other$sensitivityProcessor = other.getSensitivityProcessor();
        if (this$sensitivityProcessor == null ? other$sensitivityProcessor != null : !this$sensitivityProcessor.equals(other$sensitivityProcessor)) {
            return false;
        }
        CinematicComponent this$cinematicComponent = this.getCinematicComponent();
        CinematicComponent other$cinematicComponent = other.getCinematicComponent();
        if (this$cinematicComponent == null ? other$cinematicComponent != null : !this$cinematicComponent.equals(other$cinematicComponent)) {
            return false;
        }
        if (!Arrays.deepEquals(this.getNearbyEntities(), other.getNearbyEntities())) {
            return false;
        }
        if (!Arrays.equals(this.getMaxNearbyEntitiesCoordinate(), other.getMaxNearbyEntitiesCoordinate())) {
            return false;
        }
        return Arrays.deepEquals(this.getRunners(), other.getRunners());
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof PlayerProtocol;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        long $activeCreationTime = this.getActiveCreationTime();
        result = result * 59 + (int)($activeCreationTime >>> 32 ^ $activeCreationTime);
        result = result * 59 + this.getSlime0Tick();
        result = result * 59 + this.getRightClickCounter();
        result = result * 59 + this.getTransactionVl();
        result = result * 59 + this.getFlyingTicks();
        result = result * 59 + this.getPredictedSlimeTicks();
        result = result * 59 + (this.isNpc() ? 79 : 97);
        result = result * 59 + (this.isOnGround() ? 79 : 97);
        result = result * 59 + (this.isOnGroundFrom() ? 79 : 97);
        result = result * 59 + (this.isSprinting() ? 79 : 97);
        result = result * 59 + (this.isSneaking() ? 79 : 97);
        result = result * 59 + (this.isPistonTick() ? 79 : 97);
        result = result * 59 + (this.isTransactionBoot() ? 79 : 97);
        result = result * 59 + (this.isClickBlocker() ? 79 : 97);
        result = result * 59 + (this.isTransactionSentKeep() ? 79 : 97);
        result = result * 59 + (this.isUseItemPacket() ? 79 : 97);
        result = result * 59 + (this.isUseItemPacketReset() ? 79 : 97);
        long $tickTime = this.getTickTime();
        result = result * 59 + (int)($tickTime >>> 32 ^ $tickTime);
        result = result * 59 + this.getTransactionId();
        long $transactionTime = this.getTransactionTime();
        result = result * 59 + (int)($transactionTime >>> 32 ^ $transactionTime);
        long $transactionLastTime = this.getTransactionLastTime();
        result = result * 59 + (int)($transactionLastTime >>> 32 ^ $transactionLastTime);
        long $transactionPing = this.getTransactionPing();
        result = result * 59 + (int)($transactionPing >>> 32 ^ $transactionPing);
        long $lagTick = this.getLagTick();
        result = result * 59 + (int)($lagTick >>> 32 ^ $lagTick);
        long $oldClickTime = this.getOldClickTime();
        result = result * 59 + (int)($oldClickTime >>> 32 ^ $oldClickTime);
        long $soulSandWater = this.getSoulSandWater();
        result = result * 59 + (int)($soulSandWater >>> 32 ^ $soulSandWater);
        long $magmaCubeWater = this.getMagmaCubeWater();
        result = result * 59 + (int)($magmaCubeWater >>> 32 ^ $magmaCubeWater);
        long $lastVelocity = this.getLastVelocity();
        result = result * 59 + (int)($lastVelocity >>> 32 ^ $lastVelocity);
        long $lastInteraction = this.getLastInteraction();
        result = result * 59 + (int)($lastInteraction >>> 32 ^ $lastInteraction);
        long $lastFlight = this.getLastFlight();
        result = result * 59 + (int)($lastFlight >>> 32 ^ $lastFlight);
        long $lastGlide = this.getLastGlide();
        result = result * 59 + (int)($lastGlide >>> 32 ^ $lastGlide);
        result = result * 59 + (this.isEntityHandle() ? 79 : 97);
        result = result * 59 + (this.isAFK() ? 79 : 97);
        result = result * 59 + (this.isCinematic() ? 79 : 97);
        result = result * 59 + (this.isTeleportTick() ? 79 : 97);
        Player $bukkit = this.getBukkit();
        result = result * 59 + ($bukkit == null ? 43 : $bukkit.hashCode());
        EnvironmentData $environment = this.getEnvironment();
        result = result * 59 + ($environment == null ? 43 : (Object$environment).hashCode());
        PlayerPunishments $punishments = this.getPunishments();
        result = result * 59 + ($punishments == null ? 43 : $punishments.hashCode());
        Location $teleport = this.getTeleport();
        result = result * 59 + ($teleport == null ? 43 : $teleport.hashCode());
        Location $location = this.getLocation();
        result = result * 59 + ($location == null ? 43 : $location.hashCode());
        Location $from = this.getFrom();
        result = result * 59 + ($from == null ? 43 : $from.hashCode());
        Location $schedulerFrom = this.getSchedulerFrom();
        result = result * 59 + ($schedulerFrom == null ? 43 : $schedulerFrom.hashCode());
        String $fromWorld = this.getFromWorld();
        result = result * 59 + ($fromWorld == null ? 43 : $fromWorld.hashCode());
        List<Location> $positionHistory = this.getPositionHistory();
        result = result * 59 + ($positionHistory == null ? 43 : (Object$positionHistory).hashCode());
        CPlayerVelocityEvent $claimedVelocity = this.getClaimedVelocity();
        result = result * 59 + ($claimedVelocity == null ? 43 : (Object$claimedVelocity).hashCode());
        List<CPlayerVelocityEvent> $claimedVeloGravity = this.getClaimedVeloGravity();
        result = result * 59 + ($claimedVeloGravity == null ? 43 : (Object$claimedVeloGravity).hashCode());
        List<CPlayerVelocityEvent> $claimedVeloSpeed = this.getClaimedVeloSpeed();
        result = result * 59 + ($claimedVeloSpeed == null ? 43 : (Object$claimedVeloSpeed).hashCode());
        MultiVersion.MCVersion $version = this.getVersion();
        result = result * 59 + ($version == null ? 43 : (Object$version).hashCode());
        OtherTimerBalancer $timerBalancer = this.getTimerBalancer();
        result = result * 59 + ($timerBalancer == null ? 43 : $timerBalancer.hashCode());
        Check.DetectionType $detectionType = this.getDetectionType();
        result = result * 59 + ($detectionType == null ? 43 : (Object$detectionType).hashCode());
        Check.DataType $dataType = this.getDataType();
        result = result * 59 + ($dataType == null ? 43 : (Object$dataType).hashCode());
        Set<AxisMatrix> $axisMatrixCache = this.getAxisMatrixCache();
        result = result * 59 + ($axisMatrixCache == null ? 43 : (Object$axisMatrixCache).hashCode());
        PacketWorld $packetWorld = this.getPacketWorld();
        result = result * 59 + ($packetWorld == null ? 43 : $packetWorld.hashCode());
        PlayerTickEvent $lastTickEvent = this.getLastTickEvent();
        result = result * 59 + ($lastTickEvent == null ? 43 : $lastTickEvent.hashCode());
        ComponentY $componentY = this.getComponentY();
        result = result * 59 + ($componentY == null ? 43 : $componentY.hashCode());
        ComponentXZ $componentXZ = this.getComponentXZ();
        result = result * 59 + ($componentXZ == null ? 43 : $componentXZ.hashCode());
        Vector $clampVector = this.getClampVector();
        result = result * 59 + ($clampVector == null ? 43 : $clampVector.hashCode());
        Map<PotionEffectType, ExtendedPotionEffect> $potionEffects = this.getPotionEffects();
        result = result * 59 + ($potionEffects == null ? 43 : (Object$potionEffects).hashCode());
        List<Integer> $sensitivity = this.getSensitivity();
        result = result * 59 + ($sensitivity == null ? 43 : (Object$sensitivity).hashCode());
        OtherSensitivityProcessor $sensitivityProcessor = this.getSensitivityProcessor();
        result = result * 59 + ($sensitivityProcessor == null ? 43 : $sensitivityProcessor.hashCode());
        CinematicComponent $cinematicComponent = this.getCinematicComponent();
        result = result * 59 + ($cinematicComponent == null ? 43 : $cinematicComponent.hashCode());
        result = result * 59 + Arrays.deepHashCode(this.getNearbyEntities());
        result = result * 59 + Arrays.hashCode(this.getMaxNearbyEntitiesCoordinate());
        result = result * 59 + Arrays.deepHashCode(this.getRunners());
        return result;
    }

    @Generated
    public EnvironmentData getEnvironment() {
        return this.environment;
    }

    @Generated
    public void setTeleport(Location teleport) {
    }

    @Generated
    public Location getTeleport() {
        return this.teleport;
    }

    @Generated
    public void setLocation(Location location) {
    }

    @Generated
    public void setFrom(Location from) {
    }

    @Generated
    public void setSchedulerFrom(Location schedulerFrom) {
    }

    @Generated
    public Check.DataType getDataType() {
        return this.dataType;
    }

    @Generated
    public CheckRunner[] getRunners() {
        return this.runners;
    }

    static {
        PluginBase.runRepeatingTask(() -> {
            Collection<PlayerProtocol> protocols = PluginBase.getProtocols();
            if (!protocols.isEmpty()) {
                for (PlayerProtocol protocol : protocols) {
                    Check.DataType previousDataType = protocol.dataType;
                    Check.DataType dataType = protocol.dataType = BedrockCompatibility.isPlayer(protocol.bukkit()) ? Check.DataType.BEDROCK : Check.DataType.JAVA;
                    if (protocol.dataType != previousDataType) {
                        protocol.profile().update(protocol);
                    }
                    if (!MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)) {
                        protocol.setStoredPotionEffects();
                    }
                    if (!MultiVersion.folia && (protocol.maxNearbyEntitiesCoordinate[0] > 0.0 || protocol.maxNearbyEntitiesCoordinate[1] > 0.0 || protocol.maxNearbyEntitiesCoordinate[2] > 0.0)) {
                        ServerSchedulers.transfer(() -> {
                            protocol.nearbyEntities = protocol.bukkit().getNearbyEntities(protocol.maxNearbyEntitiesCoordinate[0], protocol.maxNearbyEntitiesCoordinate[1], protocol.maxNearbyEntitiesCoordinate[2]).toArray(new Entity[0]);
                        });
                    }
                    CheckThread.run(() -> {
                        protocol.isFlying();
                        protocol.isGliding();
                        protocol.checkForAFK();
                        protocol.executeRunners(false, null);
                        protocol.schedulerFrom = protocol.getLocation();
                    });
                }
            }
        }, 1L, 1L);
    }
}

