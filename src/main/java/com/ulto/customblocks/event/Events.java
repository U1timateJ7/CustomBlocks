package com.ulto.customblocks.event;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ulto.customblocks.GenerateCustomElements;
import com.ulto.customblocks.util.JsonUtils;
import net.minecraft.commands.CommandFunction;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

@SuppressWarnings("SimplifiableConditionalExpression")
public class Events {
    public static boolean parseIfCondition(Map<String, Object> dependencies, JsonObject condition) {
        if (condition.has("id")) {
            final boolean[] returnValue = {false};
            switch (condition.get("id").getAsString()) {
                case "true" -> returnValue[0] = true;
                case "check_game_rule" -> {
                    if (dependencies.containsKey("world") && condition.has("game_rule")) {
                        Level world = (Level) dependencies.get("world");
                        GameRules.visitGameRuleTypes(new GameRules.GameRuleTypeVisitor() {
                            @Override
                            public void visitBoolean(GameRules.Key<GameRules.BooleanValue> key, GameRules.Type<GameRules.BooleanValue> type) {
                                if (key.getId().equals(condition.get("game_rule").getAsString())) {
                                    returnValue[0] = world.getGameRules().getBoolean(key);
                                }
                            }

                            @Override
                            public void visitInteger(GameRules.Key<GameRules.IntegerValue> key, GameRules.Type<GameRules.IntegerValue> type) {
                                if (condition.has("value") && key.getId().equals(condition.get("game_rule").getAsString())) {
                                    returnValue[0] = world.getGameRules().getInt(key) == condition.get("value").getAsInt();
                                }
                            }
                        });
                    }
                }
                case "get_entity_attribute" -> {
                    if (dependencies.containsKey("entity") && condition.has("attribute") && condition.has("value")) {
                        Entity entity = (Entity) dependencies.get("entity");
                        double value = condition.get("value").getAsDouble();
                        if (entity instanceof LivingEntity livingEntity) {
                            returnValue[0] = value == livingEntity.getAttributes().getBaseValue(ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(condition.get("attribute").getAsString())));
                        }
                    }
                }
                case "get_block_at_pos" -> {
                    if (dependencies.containsKey("world") && condition.has("block")) {
                        Level world = (Level) dependencies.get("world");
                        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(condition.get("block").getAsString()));
                        int x = dependencies.containsKey("x") ? (int) dependencies.get("x") : condition.has("x") ? condition.get("x").getAsInt() : 0;
                        int y = dependencies.containsKey("y") ? (int) dependencies.get("y") : condition.has("y") ? condition.get("y").getAsInt() : 0;
                        int z = dependencies.containsKey("z") ? (int) dependencies.get("z") : condition.has("z") ? condition.get("z").getAsInt() : 0;
                        returnValue[0] = world.getBlockState(new BlockPos(x, y, z)).is(block);
                    }
                }
                case "is_block_at_pos_in_tag" -> {
                    if (dependencies.containsKey("world") && condition.has("tag")) {
                        Level world = (Level) dependencies.get("world");
                        TagKey<Block> tag = ForgeRegistries.BLOCKS.tags().createOptionalTagKey(new ResourceLocation(condition.get("tag").getAsString()), Set.of());
                        int x = dependencies.containsKey("x") ? (int) dependencies.get("x") : condition.has("x") ? condition.get("x").getAsInt() : 0;
                        int y = dependencies.containsKey("y") ? (int) dependencies.get("y") : condition.has("y") ? condition.get("y").getAsInt() : 0;
                        int z = dependencies.containsKey("z") ? (int) dependencies.get("z") : condition.has("z") ? condition.get("z").getAsInt() : 0;
                        returnValue[0] = world.getBlockState(new BlockPos(x, y, z)).is(tag);
                    }
                }
            }
            return returnValue[0];
        }
        return true;
    }

    public static void ifLoop(List<JsonObject> ifs, Map<String, Object> dependencies) {
        for (JsonObject _if : ifs) {
            if (_if.has("condition") && _if.has("if")) {
                if (parseIfCondition(dependencies, _if.getAsJsonObject("condition"))) {
                    playEvent(dependencies, _if.getAsJsonObject("if"));
                } else if (_if.has("else_if")) {
                    JsonArray elseIfs = _if.getAsJsonArray("else_if");
                    for (JsonObject elseIf : JsonUtils.jsonArrayToJsonObjectList(elseIfs)) {
                        if (!elseIf.has("condition") || !elseIf.has("if")) continue;
                        if (parseIfCondition(dependencies, elseIf.getAsJsonObject("condition"))) {
                            ifLoop(Collections.singletonList(elseIf), dependencies);
                            break;
                        }
                    }
                } else if (_if.has("else")) {
                    playEvent(dependencies, _if.getAsJsonObject("else"));
                }
            }
        }
    }

    public static InteractionResult playEvent(Map<String, Object> dependencies, JsonObject event) {
        InteractionResult result = InteractionResult.PASS;
        List<JsonObject> ifs = new ArrayList<>();
        for (Map.Entry<String, JsonElement> entry : event.entrySet()) if (entry.getKey().contains("if") && entry.getValue().isJsonObject()) ifs.add(entry.getValue().getAsJsonObject());
        ifLoop(ifs, dependencies);
        if (event.has("command")) {
            if (event.get("command").isJsonArray()) {
                for (JsonElement commandElement : event.getAsJsonArray("command")) {
                    if (commandElement.isJsonPrimitive() && dependencies.containsKey("world") && dependencies.containsKey("x") && dependencies.containsKey("y") && dependencies.containsKey("z")) {
                        String command = commandElement.getAsString();
                        Level world = (Level) dependencies.get("world");
                        double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
                        double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
                        double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
                        if(!world.isClientSide()) {
                            ((ServerLevel) world).getServer().getCommands().performCommandPrefixed(
                                    new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO,
                                            (ServerLevel) world, 4, "", Component.literal(""), ((ServerLevel) world).getServer(), null).withSuppressedOutput(), command);
                        }
                        result = InteractionResult.SUCCESS;
                    }
                }
            } else if (event.get("command").isJsonPrimitive() && dependencies.containsKey("world") && dependencies.containsKey("x") && dependencies.containsKey("y") && dependencies.containsKey("z")) {
                String command = event.get("command").getAsString();
                Level world = (Level) dependencies.get("world");
                double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
                double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
                double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
                if(!world.isClientSide()) {
                    ((ServerLevel) world).getServer().getCommands().performCommandPrefixed(
                            new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO,
                                    (ServerLevel) world, 4, "", Component.literal(""), ((ServerLevel) world).getServer(), null).withSuppressedOutput(), command);
                }
                result = InteractionResult.SUCCESS;
            }
        }
        if (event.has("function")) {
            if (event.get("function").isJsonArray()) {
                for (JsonElement functionElement : event.getAsJsonArray("function")) {
                    if (functionElement.isJsonPrimitive() && dependencies.containsKey("world") && dependencies.containsKey("x") && dependencies.containsKey("y") && dependencies.containsKey("z")) {
                        ResourceLocation function = new ResourceLocation(functionElement.getAsString());
                        Level world = (Level) dependencies.get("world");
                        double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
                        double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
                        double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
                        if(world instanceof ServerLevel) {
                            Optional<CommandFunction> _fopt = ((ServerLevel) world).getServer().getFunctions().get(function);
                            if (_fopt.isPresent()) {
                                CommandFunction _fobj = _fopt.get();
                                ((ServerLevel) world).getServer().getFunctions().execute(_fobj,
                                        new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO,
                                                (ServerLevel) world, 4, "", Component.literal(""), ((ServerLevel) world).getServer(), null));
                            }
                        }
                        result = InteractionResult.SUCCESS;
                    }
                }
            } else if (event.get("function").isJsonPrimitive() && dependencies.containsKey("world") && dependencies.containsKey("x") && dependencies.containsKey("y") && dependencies.containsKey("z")) {
                ResourceLocation function = new ResourceLocation(event.get("function").getAsString());
                Level world = (Level) dependencies.get("world");
                double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
                double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
                double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
                if(world instanceof ServerLevel) {
                    Optional<CommandFunction> _fopt = ((ServerLevel) world).getServer().getFunctions().get(function);
                    if (_fopt.isPresent()) {
                        CommandFunction _fobj = _fopt.get();
                        ((ServerLevel) world).getServer().getFunctions().execute(_fobj,
                                new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO,
                                        (ServerLevel) world, 4, "", Component.literal(""), ((ServerLevel) world).getServer(), null));
                    }
                }
                result = InteractionResult.SUCCESS;
            }
        }
        if (event.has("explode")) {
            if (event.get("explode").isJsonObject() && dependencies.containsKey("world") && dependencies.containsKey("x") && dependencies.containsKey("y") && dependencies.containsKey("z")) {
                JsonObject explode = event.getAsJsonObject("explode");
                float power = explode.has("power") ? explode.get("power").getAsFloat() : 4;
                boolean lightsFire = explode.has("lights_fire") ? explode.get("lights_fire").getAsBoolean() : false;
                Level world = (Level) dependencies.get("world");
                double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
                double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
                double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
                world.destroyBlock(new BlockPos(x, y, z), true, null);
                if (!world.isClientSide()) world.explode(null, x, y, z, power, lightsFire, Explosion.BlockInteraction.BREAK);
                result = InteractionResult.SUCCESS;
            }
        }
        if (event.has("give_item_to_entity")) {
            if (event.get("give_item_to_entity").isJsonArray()) {
                for (JsonElement element : event.getAsJsonArray("give_item_to_entity")) {
                    if (element.isJsonObject() && dependencies.containsKey("entity")) {
                        JsonObject giveItemToEntity = element.getAsJsonObject();
                        ItemStack item = JsonUtils.itemStackFromJsonObject(giveItemToEntity);
                        Entity entity = (Entity) dependencies.get("entity");
                        if (entity instanceof Player) {
                            ((Player) entity).addItem(item);
                        }
                        result = InteractionResult.SUCCESS;
                    }
                }
            } else if (event.get("give_item_to_entity").isJsonObject() && dependencies.containsKey("entity")) {
                JsonObject giveItemToEntity = event.getAsJsonObject("give_item_to_entity");
                ItemStack item = JsonUtils.itemStackFromJsonObject(giveItemToEntity);
                Entity entity = (Entity) dependencies.get("entity");
                if (entity instanceof Player) {
                    ((Player) entity).addItem(item);
                }
                result = InteractionResult.SUCCESS;
            }
        }
        if (event.has("play_sound")) {
            if (event.get("play_sound").isJsonObject() && dependencies.containsKey("world") && dependencies.containsKey("x") && dependencies.containsKey("y") && dependencies.containsKey("z")) {
                JsonObject playSound = event.getAsJsonObject("play_sound");
                ResourceLocation sound = playSound.has("sound") ? new ResourceLocation(playSound.get("sound").getAsString()) : new ResourceLocation("minecraft", "none");
                float volume = playSound.has("volume") ? playSound.get("volume").getAsFloat() : 1;
                float pitch = playSound.has("pitch") ? playSound.get("pitch").getAsFloat() : 1;
                Level world = (Level) dependencies.get("world");
                double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
                double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
                double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
                if(!world.isClientSide()) (world).playSound(null, x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(sound), SoundSource.MASTER, volume, pitch);
                result = InteractionResult.SUCCESS;
            }
        }
        if (event.has("add_effect_to_entity")) {
            if (event.get("add_effect_to_entity").isJsonArray()) {
                for (JsonElement element : event.getAsJsonArray("add_effect_to_entity")) {
                    if (element.isJsonObject() && dependencies.containsKey("entity")) {
                        JsonObject addEffectToEntity = element.getAsJsonObject();
                        MobEffectInstance effect = JsonUtils.mobEffectInstanceFromJsonObject(addEffectToEntity);
                        Entity entity = (Entity) dependencies.get("entity");
                        if(entity instanceof LivingEntity) ((LivingEntity) entity).addEffect(effect);
                        result = InteractionResult.SUCCESS;
                    }
                }
            } else if (event.get("add_effect_to_entity").isJsonObject() && dependencies.containsKey("entity")) {
                JsonObject addEffectToEntity = event.getAsJsonObject("add_effect_to_entity");
                MobEffectInstance effect = JsonUtils.mobEffectInstanceFromJsonObject(addEffectToEntity);
                Entity entity = (Entity) dependencies.get("entity");
                if(entity instanceof LivingEntity) ((LivingEntity) entity).addEffect(effect);
                result = InteractionResult.SUCCESS;
            }
        }
        if (event.has("spawn_entity")) {
            if (event.get("spawn_entity").isJsonArray()) {
                for (JsonElement commandElement : event.getAsJsonArray("spawn_entity")) {
                    if (commandElement.isJsonObject() && dependencies.containsKey("world") && dependencies.containsKey("x") && dependencies.containsKey("y") && dependencies.containsKey("z")) {
                        JsonObject command = commandElement.getAsJsonObject();
                        Level world = (Level) dependencies.get("world");
                        double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
                        double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
                        double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
                        ResourceLocation entity = new ResourceLocation(command.get("id").getAsString());
                        CompoundTag nbt = new CompoundTag();
                        boolean initialize = true;
                        if (command.has("nbt")) {
                            JsonUtils.jsonElementToCompoundTag(command.get("nbt"));
                            initialize = false;
                        }
                        nbt.putString("id", entity.toString());
                        if (!world.isClientSide()) {
                            Entity entity2 = EntityType.loadEntityRecursive(nbt, world, (entityx) -> {
                                entityx.moveTo(x, y, z, entityx.getYRot(), entityx.getXRot());
                                return entityx;
                            });
                            if (initialize && entity2 instanceof Mob) ((Mob) entity2).finalizeSpawn((ServerLevel) world, world.getCurrentDifficultyAt(entity2.getOnPos()), MobSpawnType.COMMAND, null, null);
                        }
                        result = InteractionResult.SUCCESS;
                    }
                }
            } else if (event.get("spawn_entity").isJsonObject() && dependencies.containsKey("world") && dependencies.containsKey("x") && dependencies.containsKey("y") && dependencies.containsKey("z")) {
                JsonObject command = event.get("spawn_entity").getAsJsonObject();
                Level world = (Level) dependencies.get("world");
                double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
                double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
                double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
                String entity = command.get("id").getAsString();
                CompoundTag nbt = new CompoundTag();
                boolean initialize = true;
                if (command.has("nbt")) {
                    JsonUtils.jsonElementToCompoundTag(command.get("nbt"));
                    initialize = false;
                }
                nbt.putString("id", entity.toString());
                if (!world.isClientSide()) {
                    Entity entity2 = EntityType.loadEntityRecursive(nbt, world, (entityx) -> {
                        entityx.moveTo(x, y, z, entityx.getYRot(), entityx.getXRot());
                        return entityx;
                    });
                    if (initialize && entity2 instanceof Mob) ((Mob) entity2).finalizeSpawn((ServerLevel) world, world.getCurrentDifficultyAt(entity2.getOnPos()), MobSpawnType.COMMAND, null, null);
                }
                result = InteractionResult.SUCCESS;
            }
        }
        if (event.has("emit_game_event")) {
            if (event.get("emit_game_event").isJsonPrimitive() && dependencies.containsKey("world") && dependencies.containsKey("x") && dependencies.containsKey("y") && dependencies.containsKey("z")) {
                Level world = (Level) dependencies.get("world");
                double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
                double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
                double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
                ResourceLocation gameEvent = new ResourceLocation(event.get("emit_game_event").getAsString());
                if (dependencies.containsKey("entity")) world.gameEvent((Entity) dependencies.get("entity"), Registry.GAME_EVENT.get(gameEvent), new BlockPos((int) x, (int) y, (int) z));
                else world.gameEvent(Registry.GAME_EVENT.get(gameEvent), new BlockPos((int) x, (int) y, (int) z), new GameEvent.Context(null, world.getBlockState(new BlockPos(x, y, z))));
                result = InteractionResult.SUCCESS;
            }
        }
        if (event.has("block_event")) {
            if (event.get("block_event").isJsonObject()) {
                JsonObject eventObject = event.getAsJsonObject("block_event");
                String blockId = eventObject.get("custom_block").getAsString();
                String eventName = eventObject.get("event_name").getAsString();
                for (File value : GenerateCustomElements.blocks) {
                    try {
                        BufferedReader blockReader = new BufferedReader(new FileReader(value));
                        StringBuilder json = new StringBuilder();
                        String line;
                        while ((line = blockReader.readLine()) != null) {
                            json.append(line);
                        }
                        JsonObject block = new Gson().fromJson(json.toString(), JsonObject.class);
                        if (blockId.equals(new ResourceLocation(block.get("namespace").getAsString(), block.get("id").getAsString()).toString())) if (block.has(eventName)) Events.playEvent(dependencies, block.getAsJsonObject(eventName));
                        blockReader.close();
                    } catch (Exception e) {
                    }
                }
                result = InteractionResult.SUCCESS;
            }
        }
        return result;
    }

    public static void playBlockEvent(BlockState state, BlockPos pos, Level world, @Nullable Map<String, Object> dependencies, JsonObject event) {
        Map<String, Object> deps = new HashMap<>(Map.of("blockstate", state, "x", pos.getX(), "y", pos.getY(), "z", pos.getZ(), "world", world));
        if (dependencies != null) {
            for (Map.Entry<String, Object> entry : dependencies.entrySet()) {
                deps.put(entry.getKey(), entry.getValue());
            }
        }
        playEvent(deps, event);
    }

    public static InteractionResult playBlockActionEvent(BlockState state, BlockPos pos, Level world, @Nullable Map<String, Object> dependencies, JsonObject event) {
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

    public static InteractionResult playItemActionEvent(ItemStack stack, @Nullable Map<String, Object> dependencies, JsonObject event) {
        Map<String, Object> deps = new HashMap<>(Map.of("itemstack", stack));
        if (dependencies != null) {
            for (Map.Entry<String, Object> entry : dependencies.entrySet()) {
                deps.put(entry.getKey(), entry.getValue());
            }
        }
        return playEvent(deps, event);
    }

    public static InteractionResultHolder<ItemStack> playItemTypedActionEvent(ItemStack stack, boolean swingHand, @Nullable Map<String, Object> dependencies, JsonObject event) {
        Map<String, Object> deps = new HashMap<>(Map.of("itemstack", stack));
        if (dependencies != null) {
            for (Map.Entry<String, Object> entry : dependencies.entrySet()) {
                deps.put(entry.getKey(), entry.getValue());
            }
        }
        return switch (playEvent(deps, event)) {
            case SUCCESS -> InteractionResultHolder.sidedSuccess(stack, swingHand);
            case CONSUME, CONSUME_PARTIAL -> InteractionResultHolder.consume(stack);
            case FAIL -> InteractionResultHolder.fail(stack);
            default -> InteractionResultHolder.pass(stack);
        };
    }

    public static InteractionResult playEntityEvent(Entity entity, @Nullable Map<String, Object> dependencies, JsonObject event) {
        Map<String, Object> deps = new HashMap<>(Map.of("entity", entity, "x", entity.getX(), "y", entity.getY(), "z", entity.getZ(), "world", entity.level));
        if (dependencies != null) {
            deps.putAll(dependencies);
        }
        return playEvent(deps, event);
    }
}
