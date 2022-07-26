package com.ulto.customblocks;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ulto.customblocks.entity.*;
import com.ulto.customblocks.util.JsonUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityGenerator {
    public static Map<ResourceLocation, EntityType<? extends PathfinderMob>> entities = new HashMap<>();
    public static Map<String, JsonObject> entityModels = new HashMap<>();

    private static final Map<ResourceLocation, Map<ResourceLocation, Double>> attributeSuppliers = new HashMap<>();

    public static boolean add(JsonObject entity) {
        if (entity.has("namespace") && entity.has("id")) {
            ResourceLocation id = new ResourceLocation(entity.get("namespace").getAsString(), entity.get("id").getAsString());
            String base = "path_aware";
            if (entity.has("base")) base = entity.get("base").getAsString();
            MobCategory spawnGroup = MobCategory.CREATURE;
            if (entity.has("spawn_group")) spawnGroup = MobCategory.byName(entity.get("spawn_group").getAsString());
            float width = 0.6f;
            if (entity.has("width")) width = entity.get("width").getAsFloat();
            float height = 1.8f;
            if (entity.has("height")) height = entity.get("height").getAsFloat();
            float health = 10f;
            if (entity.has("health")) health = entity.get("health").getAsFloat();
            float movementSpeed = 0.3f;
            if (entity.has("movement_speed")) movementSpeed = entity.get("movement_speed").getAsFloat();
            float attackDamage = 1f;
            if (entity.has("attack_damage")) attackDamage = entity.get("attack_damage").getAsFloat();
            boolean hasSpawnEgg = false;
            if (entity.has("has_spawn_egg")) hasSpawnEgg = entity.get("has_spawn_egg").getAsBoolean();
            int baseColor = 0xFFFFFF;
            if (entity.has("egg_base_color")) baseColor = entity.get("egg_base_color").getAsInt();
            int dotColor = 0xFFFFFF;
            if (entity.has("egg_dot_color")) dotColor = entity.get("egg_dot_color").getAsInt();

            attributeSuppliers.put(id, of(new ResourceLocation("generic.knockback_resistance"), Attributes.KNOCKBACK_RESISTANCE.getDefaultValue(), new ResourceLocation("generic.armor"), Attributes.ARMOR.getDefaultValue(), new ResourceLocation("generic.armor_toughness"), Attributes.ARMOR_TOUGHNESS.getDefaultValue(), new ResourceLocation("forge:swim_speed"), 1d, new ResourceLocation("forge:nametag_distance"), 64d, new ResourceLocation("forge:entity_gravity"), 0.08d, new ResourceLocation("generic.follow_range"), Attributes.FOLLOW_RANGE.getDefaultValue(), new ResourceLocation("generic.attack_knockback"), Attributes.ATTACK_KNOCKBACK.getDefaultValue(), new ResourceLocation("generic.max_health"),(double) health, new ResourceLocation("generic.movement_speed"),(double) movementSpeed, new ResourceLocation("generic.attack_damage"), (double) attackDamage));

            entities.put(id, switch (base) {
                case "passive" -> EntityType.Builder.<AgeableMob>of((t, w) -> new CustomPassiveEntity(t, w, entity), spawnGroup).sized(width, height).build(id.getPath());
                case "hostile" -> EntityType.Builder.<Monster>of((t, w) -> new CustomHostileEntity(t, w, entity), spawnGroup).sized(width, height).build(id.getPath());
                case "illager" -> EntityType.Builder.<AbstractIllager>of((t, w) -> new CustomIllagerEntity(t, w, entity), spawnGroup).sized(width, height).build(id.getPath());
                case "water" -> EntityType.Builder.<WaterAnimal>of((t, w) -> new CustomWaterEntity(t, w, entity), spawnGroup).sized(width, height).build(id.getPath());
                default -> EntityType.Builder.<PathfinderMob>of((t, w) -> new CustomEntity(t, w, entity), spawnGroup).sized(width, height).build(id.getPath());
            });

            assert entities.get(id) != null;
            ForgeRegistries.ENTITIES.register(id, entities.get(id));

            if (hasSpawnEgg) ForgeRegistries.ITEMS.register(getEggId(id), new ForgeSpawnEggItem(() -> entities.get(id), baseColor, dotColor, new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
            return true;
        }
        return false;
    }

    static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10, K k11, V v11) {
        Map<K, V> map = new HashMap<>();
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        map.put(k8, v8);
        map.put(k9, v9);
        map.put(k10, v10);
        map.put(k11, v11);
        return map;
    }

    @SubscribeEvent
    public static void createAttributes(EntityAttributeCreationEvent event) {
        for (Map.Entry<ResourceLocation, Map<ResourceLocation, Double>> attributes : attributeSuppliers.entrySet()) {
            AttributeSupplier.Builder attributeSupplier = AttributeSupplier.builder();
            for (Map.Entry<ResourceLocation, Double> attribute : attributes.getValue().entrySet()) attributeSupplier.add(ForgeRegistries.ATTRIBUTES.getValue(attribute.getKey()), attribute.getValue());
            event.put(entities.get(attributes.getKey()), attributeSupplier.build());
            CustomBlocksMod.LOGGER.info(ForgeRegistries.ENTITIES.getKey(entities.get(attributes.getKey())));
        }
        CustomBlocksMod.LOGGER.info("Created attributes for {} entities...", attributeSuppliers.size());
    }

    private static ResourceLocation getEggId(ResourceLocation id) {
        return new ResourceLocation(id.getNamespace(), id.getPath() + "_spawn_egg");
    }

    public static <T extends PathfinderMob> void addGoals(GoalSelector goalSelector, GoalSelector targetSelector, JsonObject entity, T pathAwareEntity) {
        if (entity.has("ai_tasks")) {
            JsonArray aiTasks = entity.getAsJsonArray("ai_tasks");
            for (int i = 0; i < aiTasks.size(); i++) {
                if (aiTasks.get(i).isJsonPrimitive()) {
                    if (aiTasks.get(i).getAsJsonPrimitive().isString()) {
                        switch (aiTasks.get(i).getAsString()) {
                            case "break_door" -> goalSelector.addGoal(i, new BreakDoorGoal(pathAwareEntity, (difficulty) -> difficulty.getId() > 0));
                            case "use_door" -> goalSelector.addGoal(i, new OpenDoorGoal(pathAwareEntity, true));
                            case "wander" -> goalSelector.addGoal(i, new WaterAvoidingRandomStrollGoal(pathAwareEntity, 1));
                            case "look_around" -> goalSelector.addGoal(i, new RandomLookAroundGoal(pathAwareEntity));
                            case "float" -> goalSelector.addGoal(i, new FloatGoal(pathAwareEntity));
                        }
                    }
                } else if (aiTasks.get(i).isJsonObject()) {
                    JsonObject aiTask = aiTasks.get(i).getAsJsonObject();
                    if (aiTask.has("id")) {
                        switch (aiTask.get("id").getAsString()) {
                            case "attack" -> {
                                goalSelector.addGoal(i, new MeleeAttackGoal(pathAwareEntity, 1, true));
                                targetSelector.addGoal(i, new HurtByTargetGoal(pathAwareEntity));
                                if (aiTask.has("targets")) {
                                    for (String target : JsonUtils.jsonArrayToStringList(aiTask.getAsJsonArray("targets"))) {
                                        Class<? extends LivingEntity> targetEntity = (Class<? extends LivingEntity>) ForgeRegistries.ENTITIES.getValue(new ResourceLocation(target)).getBaseClass();
                                        targetSelector.addGoal(i, new NearestAttackableTargetGoal<>(pathAwareEntity, targetEntity, true));
                                    }
                                }
                            }
                            case "look_at_entity" -> {
                                if (aiTask.has("target")) {
                                    Class<? extends LivingEntity> targetEntity = (Class<? extends LivingEntity>) ForgeRegistries.ENTITIES.getValue(new ResourceLocation(aiTask.get("target").getAsString())).getBaseClass();
                                    goalSelector.addGoal(i, new LookAtPlayerGoal(pathAwareEntity, targetEntity, 5));
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
