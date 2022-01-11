package com.ulto.customblocks;

import com.google.gson.JsonObject;
import com.ulto.customblocks.entity.CustomEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

public class EntityGenerator {
    public static Map<Identifier, EntityType<? extends PathAwareEntity>> entities = new HashMap<>();

    public static boolean add(JsonObject entity) {
        if (entity.has("namespace") && entity.has("id") && entity.has("base")) {
            Identifier id = new Identifier(entity.get("namespace").getAsString(), entity.get("id").getAsString());
            String base = entity.get("base").getAsString();
            SpawnGroup spawnGroup = SpawnGroup.CREATURE;
            if (entity.has("spawn_group")) spawnGroup = SpawnGroup.byName(entity.get("spawn_group").getAsString());
            float width = 0.6f;
            if (entity.has("width")) width = entity.get("width").getAsFloat();
            float height = 1.8f;
            if (entity.has("height")) height = entity.get("height").getAsFloat();
            float health = 10f;
            if (entity.has("health")) health = entity.get("health").getAsFloat();
            float movementSpeed = 0.3f;
            if (entity.has("movement_speed")) movementSpeed = entity.get("movement_speed").getAsFloat();

            DefaultAttributeContainer.Builder attributeBuilder = MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, health).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, movementSpeed);

            EntityType<? extends Entity> ENTITY_TYPE = entities.put(id, switch (base) {
                default -> Registry.register(
                        Registry.ENTITY_TYPE,
                        id,
                        FabricEntityTypeBuilder.createMob().spawnGroup(spawnGroup).entityFactory(CustomEntity::new).defaultAttributes(() -> attributeBuilder).dimensions(EntityDimensions.fixed(width, height)).build()
                );
            });
            return true;
        }
        return false;
    }

    @Environment(EnvType.CLIENT)
    public static boolean addClient(JsonObject entity) {
        return false;
    }
}
