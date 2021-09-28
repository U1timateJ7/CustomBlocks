package com.ulto.customblocks.event;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ulto.customblocks.util.JsonUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.function.CommandFunction;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Events {
    public static ActionResult playEvent(Map<String, Object> dependencies, JsonObject event) {
        ActionResult result = ActionResult.PASS;
        if (event.has("command")) {
            if (event.get("command").isJsonArray()) {
                for (JsonElement commandElement : event.getAsJsonArray("command")) {
                    if (commandElement.isJsonPrimitive() && dependencies.containsKey("world") && dependencies.containsKey("x") && dependencies.containsKey("y") && dependencies.containsKey("z")) {
                        String command = commandElement.getAsString();
                        World world = (World) dependencies.get("world");
                        double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
                        double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
                        double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
                        if(!world.isClient()) {
                            ((ServerWorld) world).getServer().getCommandManager().execute(
                                    new ServerCommandSource(CommandOutput.DUMMY, new Vec3d(x, y, z), Vec2f.ZERO,
                                            (ServerWorld) world, 4, "", new LiteralText(""), ((ServerWorld) world).getServer(), null).withSilent(), command);
                        }
                        result = ActionResult.SUCCESS;
                    }
                }
            } else if (event.get("command").isJsonPrimitive() && dependencies.containsKey("world") && dependencies.containsKey("x") && dependencies.containsKey("y") && dependencies.containsKey("z")) {
                String command = event.get("command").getAsString();
                World world = (World) dependencies.get("world");
                double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
                double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
                double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
                if(!world.isClient()) {
                    ((ServerWorld) world).getServer().getCommandManager().execute(
                            new ServerCommandSource(CommandOutput.DUMMY, new Vec3d(x, y, z), Vec2f.ZERO,
                                    (ServerWorld) world, 4, "", new LiteralText(""), ((ServerWorld) world).getServer(), null).withSilent(), command);
                }
                result = ActionResult.SUCCESS;
            }
        }
        if (event.has("function")) {
            if (event.get("function").isJsonArray()) {
                for (JsonElement functionElement : event.getAsJsonArray("function")) {
                    if (functionElement.isJsonPrimitive() && dependencies.containsKey("world") && dependencies.containsKey("x") && dependencies.containsKey("y") && dependencies.containsKey("z")) {
                        Identifier function = new Identifier(functionElement.getAsString());
                        World world = (World) dependencies.get("world");
                        double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
                        double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
                        double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
                        if(world instanceof ServerWorld) {
                            Optional<CommandFunction> _fopt = ((ServerWorld) world).getServer().getCommandFunctionManager().getFunction(function);
                            if (_fopt.isPresent()) {
                                CommandFunction _fobj = _fopt.get();
                                ((ServerWorld) world).getServer().getCommandFunctionManager().execute(_fobj,
                                        new ServerCommandSource(CommandOutput.DUMMY, new Vec3d(x, y, z), Vec2f.ZERO,
                                                (ServerWorld) world, 4, "", new LiteralText(""), ((ServerWorld) world).getServer(), null));
                            }
                        }
                        result = ActionResult.SUCCESS;
                    }
                }
            } else if (event.get("function").isJsonPrimitive() && dependencies.containsKey("world") && dependencies.containsKey("x") && dependencies.containsKey("y") && dependencies.containsKey("z")) {
                Identifier function = new Identifier(event.get("function").getAsString());
                World world = (World) dependencies.get("world");
                double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
                double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
                double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
                if(world instanceof ServerWorld) {
                    Optional<CommandFunction> _fopt = ((ServerWorld) world).getServer().getCommandFunctionManager().getFunction(function);
                    if (_fopt.isPresent()) {
                        CommandFunction _fobj = _fopt.get();
                        ((ServerWorld) world).getServer().getCommandFunctionManager().execute(_fobj,
                                new ServerCommandSource(CommandOutput.DUMMY, new Vec3d(x, y, z), Vec2f.ZERO,
                                        (ServerWorld) world, 4, "", new LiteralText(""), ((ServerWorld) world).getServer(), null));
                    }
                }
                result = ActionResult.SUCCESS;
            }
        }
        if (event.has("explode")) {
            if (event.get("explode").isJsonObject() && dependencies.containsKey("world") && dependencies.containsKey("x") && dependencies.containsKey("y") && dependencies.containsKey("z")) {
                JsonObject explode = event.getAsJsonObject("explode");
                float power = explode.has("power") ? explode.get("power").getAsFloat() : 4;
                boolean lightsFire = explode.has("lights_fire") && explode.get("lights_fire").getAsBoolean();
                World world = (World) dependencies.get("world");
                double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
                double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
                double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
                world.breakBlock(new BlockPos(x, y, z), true, null);
                world.createExplosion(null, x, y, z, power, lightsFire, Explosion.DestructionType.BREAK);
                result = ActionResult.SUCCESS;
            }
        }
        if (event.has("give_item_to_entity")) {
            if (event.get("give_item_to_entity").isJsonArray()) {
                for (JsonElement element : event.getAsJsonArray("give_item_to_entity")) {
                    if (element.isJsonObject() && dependencies.containsKey("entity")) {
                        JsonObject giveItemToEntity = element.getAsJsonObject();
                        ItemStack item = JsonUtils.itemStackFromJsonObject(giveItemToEntity);
                        Entity entity = (Entity) dependencies.get("entity");
                        if (entity instanceof PlayerEntity) {
                            ((PlayerEntity) entity).giveItemStack(item);
                        }
                        result = ActionResult.SUCCESS;
                    }
                }
            } else if (event.get("give_item_to_entity").isJsonObject() && dependencies.containsKey("entity")) {
                JsonObject giveItemToEntity = event.getAsJsonObject("give_item_to_entity");
                ItemStack item = JsonUtils.itemStackFromJsonObject(giveItemToEntity);
                Entity entity = (Entity) dependencies.get("entity");
                if (entity instanceof PlayerEntity) {
                    ((PlayerEntity) entity).giveItemStack(item);
                }
                result = ActionResult.SUCCESS;
            }
        }
        if (event.has("play_sound")) {
            if (event.get("play_sound").isJsonObject() && dependencies.containsKey("world") && dependencies.containsKey("x") && dependencies.containsKey("y") && dependencies.containsKey("z")) {
                JsonObject playSound = event.getAsJsonObject("play_sound");
                Identifier sound = playSound.has("sound") ? new Identifier(playSound.get("sound").getAsString()) : new Identifier("minecraft", "none");
                float volume = playSound.has("volume") ? playSound.get("volume").getAsFloat() : 1;
                float pitch = playSound.has("pitch") ? playSound.get("pitch").getAsFloat() : 1;
                World world = (World) dependencies.get("world");
                double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
                double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
                double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
                if(!world.isClient()) (world).playSound(null, x, y, z, Registry.SOUND_EVENT.get(sound), SoundCategory.MASTER, volume, pitch);
                result = ActionResult.SUCCESS;
            }
        }
        if (event.has("add_effect_to_entity")) {
            if (event.get("add_effect_to_entity").isJsonArray()) {
                for (JsonElement element : event.getAsJsonArray("add_effect_to_entity")) {
                    if (element.isJsonObject() && dependencies.containsKey("entity")) {
                        JsonObject addEffectToEntity = element.getAsJsonObject();
                        StatusEffectInstance effect = JsonUtils.statusEffectInstanceFromJsonObject(addEffectToEntity);
                        Entity entity = (Entity) dependencies.get("entity");
                        if(entity instanceof LivingEntity) ((LivingEntity) entity).addStatusEffect(effect);
                        result = ActionResult.SUCCESS;
                    }
                }
            } else if (event.get("add_effect_to_entity").isJsonObject() && dependencies.containsKey("entity")) {
                JsonObject addEffectToEntity = event.getAsJsonObject("add_effect_to_entity");
                StatusEffectInstance effect = JsonUtils.statusEffectInstanceFromJsonObject(addEffectToEntity);
                Entity entity = (Entity) dependencies.get("entity");
                if(entity instanceof LivingEntity) ((LivingEntity) entity).addStatusEffect(effect);
                result = ActionResult.SUCCESS;
            }
        }
        if (event.has("spawn_entity")) {
            if (event.get("spawn_entity").isJsonArray()) {
                for (JsonElement commandElement : event.getAsJsonArray("spawn_entity")) {
                    if (commandElement.isJsonObject() && dependencies.containsKey("world") && dependencies.containsKey("x") && dependencies.containsKey("y") && dependencies.containsKey("z")) {
                        JsonObject command = commandElement.getAsJsonObject();
                        World world = (World) dependencies.get("world");
                        double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
                        double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
                        double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
                        Identifier entity = new Identifier(command.get("id").getAsString());
                        NbtCompound nbt = new NbtCompound();
                        boolean initialize = true;
                        if (command.has("nbt")) {
                            JsonUtils.jsonElementToNbtCompound(command.get("nbt"));
                            initialize = false;
                        }
                        nbt.putString("id", entity.toString());
                        if (!world.isClient()) {
                            Entity entity2 = EntityType.loadEntityWithPassengers(nbt, world, (entityx) -> {
                                entityx.refreshPositionAndAngles(x, y, z, entityx.getYaw(), entityx.getPitch());
                                return entityx;
                            });
                            if (initialize && entity2 instanceof MobEntity) ((MobEntity) entity2).initialize((ServerWorld) world, world.getLocalDifficulty(entity2.getBlockPos()), SpawnReason.COMMAND, null, null);
                        }
                        result = ActionResult.SUCCESS;
                    }
                }
            } else if (event.get("spawn_entity").isJsonObject() && dependencies.containsKey("world") && dependencies.containsKey("x") && dependencies.containsKey("y") && dependencies.containsKey("z")) {
                JsonObject command = event.get("spawn_entity").getAsJsonObject();
                World world = (World) dependencies.get("world");
                double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
                double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
                double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
                Identifier entity = new Identifier(command.get("id").getAsString());
                NbtCompound nbt = new NbtCompound();
                boolean initialize = true;
                if (command.has("nbt")) {
                    JsonUtils.jsonElementToNbtCompound(command.get("nbt"));
                    initialize = false;
                }
                nbt.putString("id", entity.toString());
                if (!world.isClient()) {
                    Entity entity2 = EntityType.loadEntityWithPassengers(nbt, world, (entityx) -> {
                        entityx.refreshPositionAndAngles(x, y, z, entityx.getYaw(), entityx.getPitch());
                        return entityx;
                    });
                    if (initialize && entity2 instanceof MobEntity) ((MobEntity) entity2).initialize((ServerWorld) world, world.getLocalDifficulty(entity2.getBlockPos()), SpawnReason.COMMAND, null, null);
                }
                result = ActionResult.SUCCESS;
            }
        }
        return result;
    }

    public static void playBlockEvent(BlockState state, BlockPos pos, World world, @Nullable Map<String, Object> dependencies, JsonObject event) {
        Map<String, Object> deps = new HashMap<>(Map.of("blockstate", state, "x", pos.getX(), "y", pos.getY(), "z", pos.getZ(), "world", world));
        if (dependencies != null) {
            for (Map.Entry<String, Object> entry : dependencies.entrySet()) {
                deps.put(entry.getKey(), entry.getValue());
            }
        }
        playEvent(deps, event);
    }

    public static ActionResult playBlockActionEvent(BlockState state, BlockPos pos, World world, @Nullable Map<String, Object> dependencies, JsonObject event) {
        Map<String, Object> deps = new HashMap<>(Map.of("blockstate", state, "x", pos.getX(), "y", pos.getY(), "z", pos.getZ(), "world", world));
        if (dependencies != null) {
            for (Map.Entry<String, Object> entry : dependencies.entrySet()) {
                deps.put(entry.getKey(), entry.getValue());
            }
        }
        return playEvent(deps, event);
    }

    public static void playItemEvent(ItemStack stack, @Nullable Map<String, Object> dependencies, JsonObject event) {
        Map<String, Object> deps = new HashMap<>(Map.of("itemstack", stack));
        if (dependencies != null) {
            for (Map.Entry<String, Object> entry : dependencies.entrySet()) {
                deps.put(entry.getKey(), entry.getValue());
            }
        }
        playEvent(deps, event);
    }

    public static ActionResult playItemActionEvent(ItemStack stack, @Nullable Map<String, Object> dependencies, JsonObject event) {
        Map<String, Object> deps = new HashMap<>(Map.of("itemstack", stack));
        if (dependencies != null) {
            for (Map.Entry<String, Object> entry : dependencies.entrySet()) {
                deps.put(entry.getKey(), entry.getValue());
            }
        }
        return playEvent(deps, event);
    }

    public static TypedActionResult<ItemStack> playItemTypedActionEvent(ItemStack stack, boolean swingHand, @Nullable Map<String, Object> dependencies, JsonObject event) {
        Map<String, Object> deps = new HashMap<>(Map.of("itemstack", stack));
        if (dependencies != null) {
            for (Map.Entry<String, Object> entry : dependencies.entrySet()) {
                deps.put(entry.getKey(), entry.getValue());
            }
        }
        return switch (playEvent(deps, event)) {
            case SUCCESS -> TypedActionResult.success(stack, swingHand);
            case CONSUME, CONSUME_PARTIAL -> TypedActionResult.consume(stack);
            case FAIL -> TypedActionResult.fail(stack);
            default -> TypedActionResult.pass(stack);
        };
    }
}
