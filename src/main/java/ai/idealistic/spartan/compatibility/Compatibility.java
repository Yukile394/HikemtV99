/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.configuration.file.YamlConfiguration
 */
package ai.idealistic.spartan.compatibility;

import ai.idealistic.spartan.Register;
import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.configuration.ConfigurationBuilder;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.compatibility.manual.abilities.AbilityCallListener;
import ai.idealistic.spartan.compatibility.manual.abilities.TerraformBlockBreakListener;
import ai.idealistic.spartan.compatibility.manual.abilities.GrapplingHook;
import ai.idealistic.spartan.compatibility.manual.abilities.ItemsAdder;
import ai.idealistic.spartan.compatibility.manual.abilities.SpellCastListener;
import ai.idealistic.spartan.compatibility.manual.abilities.MTPlayerInteractListener;
import ai.idealistic.spartan.compatibility.manual.abilities.ProjectKorra;
import ai.idealistic.spartan.compatibility.manual.abilities.crackshot.WeaponScopeListener;
import ai.idealistic.spartan.compatibility.manual.abilities.crackshot.WeaponSecondScopeListener;
import ai.idealistic.spartan.compatibility.manual.building.MineBomb;
import ai.idealistic.spartan.compatibility.manual.building.Stack;
import ai.idealistic.spartan.compatibility.manual.building.DrillBreakListener;
import ai.idealistic.spartan.compatibility.manual.building.PlayerVeinMineListener;
import ai.idealistic.spartan.compatibility.manual.building.WildTools;
import ai.idealistic.spartan.compatibility.manual.damage.PlayerOffhandAnimationListener;
import ai.idealistic.spartan.compatibility.manual.entity.CraftBook;
import ai.idealistic.spartan.compatibility.manual.entity.VehicleEnterListener;
import ai.idealistic.spartan.compatibility.necessary.Floodgate;
import ai.idealistic.spartan.compatibility.necessary.protocollib.ProtocolLib;
import ai.idealistic.spartan.functionality.moderation.AwarenessNotifications;
import ai.idealistic.spartan.functionality.server.MultiVersion;
import ai.idealistic.spartan.utils.java.ReflectionUtils;
import ai.idealistic.spartan.utils.minecraft.server.ConfigUtils;
import ai.idealistic.spartan.utils.minecraft.server.PluginUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.Generated;
import org.bukkit.configuration.file.YamlConfiguration;

public class Compatibility {
    private static final String fileName = "compatibility";
    private static final String staticDirectory = ConfigurationBuilder.getDirectory("compatibility");
    private static File file = new File(staticDirectory);
    private static final Map<String, Boolean> bool = new LinkedHashMap<String, Boolean>();

    public File getFile() {
        return file;
    }

    private static void refresh(boolean create) {
        for (CompatibilityType compatibilityType : CompatibilityType.values()) {
            compatibilityType.refresh(create);
        }
        CompatibilityType.MC_MMO.setFunctional();
        CompatibilityType.TREE_FELLER.setFunctional(new String[]{CompatibilityType.TREE_FELLER.toString()}, new CompatibilityType[]{CompatibilityType.MC_MMO}, null, null);
        CompatibilityType.CRAFT_BOOK.setFunctional(CraftBook::resetBoatLimit, null);
        CompatibilityType.CRACK_SHOT.setFunctional(() -> Register.enable(new WeaponScopeListener()), null);
        CompatibilityType.CRACK_SHOT_PLUS.setFunctional(() -> Register.enable(new WeaponSecondScopeListener()), null);
        CompatibilityType.REAL_DUAL_WIELD.setFunctional(() -> Register.enable(new PlayerOffhandAnimationListener()), null);
        CompatibilityType.MAGIC_SPELLS.setFunctional(() -> Register.enable(new SpellCastListener()), null);
        CompatibilityType.ADVANCED_ABILITIES.setFunctional(() -> Register.enable(new AbilityCallListener()), null);
        CompatibilityType.VEIN_MINER.setFunctional(PlayerVeinMineListener::reload, null);
        CompatibilityType.PROJECT_KORRA.setFunctional(() -> Register.enable(new ProjectKorra()), null);
        CompatibilityType.GRAPPLING_HOOK.setFunctional(() -> Register.enable(new GrapplingHook()), null);
        CompatibilityType.MYTHIC_MOBS.setFunctional(Stack::reload, null);
        CompatibilityType.CUSTOM_ENCHANTS_PLUS.setFunctional();
        CompatibilityType.ECO_ENCHANTS.setFunctional(ReflectionUtils.classExists("com.willfp.ecoenchants.enchants.EcoEnchant"));
        CompatibilityType.ADVANCED_ENCHANTMENTS.setFunctional(new String[]{CompatibilityType.ADVANCED_ENCHANTMENTS.toString()}, null, null, null);
        CompatibilityType.VEHICLES.setFunctional(() -> Register.enable(new VehicleEnterListener()), null);
        CompatibilityType.MINE_TINKER.setFunctional(() -> Register.enable(new MTPlayerInteractListener()), null);
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_9)) {
            CompatibilityType.ITEM_ATTRIBUTES.setFunctional(new String[]{""}, new CompatibilityType[]{CompatibilityType.MINE_TINKER, CompatibilityType.MYTHIC_MOBS, CompatibilityType.PROJECT_KORRA}, null, null);
        }
        CompatibilityType.WILD_TOOLS.setFunctional(() -> Register.enable(new WildTools()), null);
        CompatibilityType.FLOODGATE.setFunctional(new String[]{"%" + (Object)CompatibilityType.FLOODGATE, "%geyser"}, null, Floodgate::reload, null);
        CompatibilityType.PROTOCOL_SUPPORT.setFunctional(new String[]{"protocolsupport.api.Connection+", "protocolsupport.api.ProtocolSupportAPI+", CompatibilityType.PROTOCOL_SUPPORT + "+"}, null, null, null);
        CompatibilityType.PROTOCOL_LIB.setFunctional(ProtocolLib::run, ProtocolLib::otherwise);
        CompatibilityType.RAMPEN_DRILLS.setFunctional(() -> Register.enable(new DrillBreakListener()), null);
        CompatibilityType.MINE_BOMB.setFunctional(MineBomb::reload, null);
        CompatibilityType.PRINTER_MODE.setFunctional();
        CompatibilityType.SUPER_PICKAXE.setFunctional(new String[]{CompatibilityType.SUPER_PICKAXE.toString(), CompatibilityType.SUPER_PICKAXE + "Reloaded"}, null, null, null);
        CompatibilityType.AURELIUM_SKILLS.setFunctional(() -> Register.enable(new TerraformBlockBreakListener()), null);
        CompatibilityType.ITEMS_ADDER.setFunctional(() -> Register.enable(new ItemsAdder()), null);
    }

    private static boolean getBoolean(String path, boolean create) {
        Boolean data = bool.get(path);
        if (data != null) {
            return data;
        }
        if (!file.exists()) {
            if (!create) {
                return false;
            }
            Compatibility.create();
        }
        boolean value = YamlConfiguration.loadConfiguration((File)file).getBoolean(path);
        bool.put(path, value);
        return value;
    }

    public void clearCache() {
        bool.clear();
    }

    public void fastRefresh() {
        Compatibility.refresh(false);
    }

    public static void create() {
        file = new File(staticDirectory);
        bool.clear();
        Compatibility.refresh(true);
    }

    public List<CompatibilityType> getActiveCompatibilities() {
        CompatibilityType[] compatibilities = CompatibilityType.values();
        ArrayList<CompatibilityType> active = new ArrayList<CompatibilityType>(compatibilities.length);
        for (CompatibilityType compatibility : compatibilities) {
            if (!compatibility.isFunctional()) continue;
            active.add(compatibility);
        }
        return active;
    }

    public List<CompatibilityType> getTotalCompatibilities() {
        CompatibilityType[] compatibilities = CompatibilityType.values();
        ArrayList<CompatibilityType> active = new ArrayList<CompatibilityType>(compatibilities.length);
        for (CompatibilityType compatibility : CompatibilityType.values()) {
            if (!compatibility.isEnabled()) continue;
            active.add(compatibility);
        }
        return active;
    }

    public void evadeFalsePositives(PlayerProtocol protocol, CompatibilityType compatibilityType, CheckEnums.HackType[] hackTypes, int ticks) {
        for (CheckEnums.HackType hackType : hackTypes) {
            protocol.getRunner(hackType).addDisableCause(compatibilityType.toString(), null, ticks);
        }
    }

    public void evadeFalsePositives(PlayerProtocol protocol, CompatibilityType compatibilityType, CheckEnums.HackType hackType, int ticks) {
        protocol.getRunner(hackType).addDisableCause(compatibilityType.toString(), null, ticks);
    }

    public void evadeFalsePositives(PlayerProtocol protocol, CompatibilityType compatibilityType, CheckEnums.HackCategoryType[] types, int ticks) {
for (CheckEnums.HackType hackType : CheckEnums.HackType.values()) {
            for (CheckEnums.HackCategoryType type : types) {
                if (hackType.category != type) continue;
                protocol.getRunner(hackType).addDisableCause(compatibilityType.toString(), null, ticks);
                continue block0;
            }
        }
    }

    public void evadeFalsePositives(PlayerProtocol protocol, CompatibilityType compatibilityType, CheckEnums.HackCategoryType type, int ticks) {
        for (CheckEnums.HackType hackType : CheckEnums.HackType.values()) {
            if (hackType.category != type) continue;
            protocol.getRunner(hackType).addDisableCause(compatibilityType.toString(), null, ticks);
        }
    }

    public static enum CompatibilityType {
        ADVANCED_ABILITIES("AdvancedAbilities"),
        CRACK_SHOT("CrackShot"),
        CRACK_SHOT_PLUS("CrackShotPlus"),
        CRAFT_BOOK("CraftBook"),
        MAGIC_SPELLS("MagicSpells"),
        PROTOCOL_LIB("ProtocolLib"),
        MC_MMO("mcMMO"),
        PROTOCOL_SUPPORT("ProtocolSupport"),
        TREE_FELLER("TreeFeller"),
        VEIN_MINER("VeinMiner"),
        GRAPPLING_HOOK("GrapplingHook"),
        MINE_BOMB("MineBomb"),
        SUPER_PICKAXE("SuperPickaxe"),
        REAL_DUAL_WIELD("RealDualWield"),
        MYTHIC_MOBS("MythicMobs"),
        ITEM_ATTRIBUTES("ItemAttributes"),
        PRINTER_MODE("PrinterMode"),
        VEHICLES("Vehicles"),
        MINE_TINKER("MineTinker"),
        WILD_TOOLS("WildTools"),
        AURELIUM_SKILLS("AureliumSkills"),
        CUSTOM_ENCHANTS_PLUS("CustomEncahntsPlus"),
        ECO_ENCHANTS("EcoEnchants"),
        ITEMS_ADDER("ItemsAdder"),
        RAMPEN_DRILLS("RampenDrills"),
        PROJECT_KORRA("ProjectKorra"),
        FLOODGATE("Floodgate"),
        ADVANCED_ENCHANTMENTS("AdvancedEnchantments");

        private boolean enabled;
        private boolean forced;
        private boolean functional;
        private boolean elseRunnable;
        private final String name;

        private CompatibilityType(String name) {
            this.enabled = false;
            this.forced = false;
            this.functional = false;
            this.elseRunnable = false;
        }

        public String toString() {
            return this.name;
        }

        public void refresh(boolean create) {
            String name;
            boolean contains;
            boolean hardcoded;
            switch (this.ordinal()) {
                case 7:
                case 26: {
                    hardcoded = true;
                    contains = false;
                    name = this.toString().toLowerCase();
                    break;
                }
                default: {
                    hardcoded = false;
                    contains = false;
                    name = null;
                }
            }
            if (hardcoded) {
                this.enabled = true;
                this.forced = false;
                this.functional = contains ? PluginUtils.contains(name) : PluginUtils.exists(name);
            } else {
                file = new File(staticDirectory);
                String compatibility = this.toString();
                if (create) {
                    ConfigUtils.add(file, compatibility + ".enabled", !this.equals(WILD_TOOLS));
                    ConfigUtils.add(file, compatibility + ".force", false);
                }
                this.enabled = Compatibility.getBoolean(compatibility + ".enabled", create);
                this.forced = Compatibility.getBoolean(compatibility + ".force", create);
            }
        }

        public boolean isFunctional() {
            return this.enabled && this.functional;
        }

        public void setFunctional() {
            this.setFunctional(new String[]{this.toString()}, null, null, null);
        }

        public void setFunctional(Runnable runnable, Runnable elseRunnable) {
            this.setFunctional(new String[]{this.toString()}, null, runnable, elseRunnable);
        }

        public void setFunctional(String[] pluginsOrClasses, CompatibilityType[] compatibilities, Runnable runnable, Runnable elseRunnable) {
            if (this.isEnabled()) {
                if (this.functional) {
                    return;
                }
                boolean function = this.isForced();
                if (!function && pluginsOrClasses != null) {
                    int count = 0;
                    int desired = 0;
                    for (String pluginOrClass : pluginsOrClasses) {
                        if (pluginOrClass.isEmpty()) {
                            function = true;
                            break;
                        }
                        boolean partRequirement = pluginOrClass.endsWith("+");
                        if (partRequirement) {
                            ++desired;
                            pluginOrClass = pluginOrClass.substring(0, pluginOrClass.length() - 1);
                        }
                        if (!(pluginOrClass.contains(".") ? ReflectionUtils.classExists(pluginOrClass) : (pluginOrClass.startsWith("%") ? PluginUtils.contains(pluginOrClass.substring(1)) : PluginUtils.exists(pluginOrClass)))) continue;
                        if (partRequirement) {
                            ++count;
                            continue;
                        }
                        function = true;
                        break;
                    }
                    if (!function) {
                        function = desired > 0 && count == desired;
                    }
                }
                if (!function && compatibilities != null) {
                    for (CompatibilityType compatibilityType : compatibilities) {
                        if (!compatibilityType.isFunctional()) continue;
                        function = true;
                        break;
                    }
                }
                if (function) {
                    this.elseRunnable = false;
                    if (runnable != null) {
                        try {
                            runnable.run();
                            this.functional = true;
                        }
                        catch (Exception ex) {
                            this.functional = false;
                            AwarenessNotifications.forcefullySend("Compatibility '" + this.toString() + "' failed to load.");
                        }
                    } else {
                        this.functional = true;
                    }
                } else {
                    this.functional = false;
                    if (!this.elseRunnable && elseRunnable != null) {
                        this.elseRunnable = true;
                        elseRunnable.run();
                    }
                }
            } else if (!this.elseRunnable && elseRunnable != null) {
                this.elseRunnable = true;
                elseRunnable.run();
            }
        }

        @Generated
        public void setEnabled(boolean enabled) {
        }

        @Generated
        public void setForced(boolean forced) {
        }

        @Generated
        public void setFunctional(boolean functional) {
        }

        @Generated
        public void setElseRunnable(boolean elseRunnable) {
        }

        @Generated
        public boolean isEnabled() {
            return this.enabled;
        }

        @Generated
        public boolean isForced() {
            return this.forced;
        }

        @Generated
        public boolean isElseRunnable() {
            return this.elseRunnable;
        }
    }
}

