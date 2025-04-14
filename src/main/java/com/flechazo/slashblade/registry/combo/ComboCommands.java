package com.flechazo.slashblade.registry.combo;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.flechazo.slashblade.capability.inputstate.InputStateComponentRegistry;
import com.flechazo.slashblade.registry.ComboStateRegistry;
import com.flechazo.slashblade.util.InputCommand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class ComboCommands {
    public static final EnumSet<InputCommand> COMBO_B1_ALT = EnumSet.of(InputCommand.BACK, InputCommand.R_DOWN);
    private static final Map<EnumSet<InputCommand>, ResourceLocation> DEAFULT_STANDBY = new HashMap<>();

    public static ResourceLocation initStandByCommand(LivingEntity a) {
        return initStandByCommand(a, DEAFULT_STANDBY);
    }

    public static ResourceLocation initStandByCommand(LivingEntity a,
            Map<EnumSet<InputCommand>, ResourceLocation> map) {
        EnumSet<InputCommand> commands = a.getCapability(InputStateComponentRegistry.INPUT_STATE)
                .map((state) -> state.getCommands(a)).orElseGet(() -> EnumSet.noneOf(InputCommand.class));

        return map.entrySet().stream().filter((entry) -> commands.containsAll(entry.getKey()))
                // .findFirst()
                .min(Comparator.comparingInt(
                        (entry) -> ComboStateRegistry.REGISTRY.get().getValue(entry.getValue()).getPriority()))
                .map((entry) -> entry.getValue()).orElseGet(ComboStateRegistry.NONE::getId);
    }

    public static void initDefaultStandByCommands() {
        DEAFULT_STANDBY.put(
                EnumSet.of(InputCommand.ON_GROUND, InputCommand.SNEAK, InputCommand.FORWARD, InputCommand.R_CLICK),
                ComboStateRegistry.RAPID_SLASH.getId());
        DEAFULT_STANDBY.put(EnumSet.of(InputCommand.ON_GROUND, InputCommand.L_CLICK),
                ComboStateRegistry.COMBO_A1.getId());
        DEAFULT_STANDBY.put(
                EnumSet.of(InputCommand.ON_GROUND, InputCommand.BACK, InputCommand.SNEAK, InputCommand.R_CLICK),
                ComboStateRegistry.UPPERSLASH.getId());

        DEAFULT_STANDBY.put(EnumSet.of(InputCommand.ON_GROUND, InputCommand.R_CLICK),
                ComboStateRegistry.COMBO_A1.getId());

        DEAFULT_STANDBY.put(
                EnumSet.of(InputCommand.ON_AIR, InputCommand.SNEAK, InputCommand.BACK, InputCommand.R_CLICK),
                ComboStateRegistry.AERIAL_CLEAVE.getId());
        DEAFULT_STANDBY.put(EnumSet.of(InputCommand.ON_AIR), ComboStateRegistry.AERIAL_RAVE_A1.getId());
    }
}
