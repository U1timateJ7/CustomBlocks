package com.ulto.customblocks;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ulto.customblocks.entity.*;
import com.ulto.customblocks.util.JsonUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.mixin.object.builder.SpawnRestrictionAccessor;
import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class EntityGenerator {
    public static Map<Identifier, EntityType<? extends PathAwareEntity>> entities = new HashMap<>();
    public static Map<Identifier, JsonObject> entityJSONs = new HashMap<>();
    @Environment(EnvType.CLIENT)
    public static Map<String, JsonObject> entityModels = new HashMap<>();

    public static boolean add(JsonObject entity) {
        if (entity.has("namespace") && entity.has("id")) {
            Identifier id = new Identifier(entity.get("namespace").getAsString(), entity.get("id").getAsString());
            String base = "path_aware";
            if (entity.has("base")) base = entity.get("base").getAsString();
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
            boolean hasSpawnEgg = false;
            if (entity.has("has_spawn_egg")) hasSpawnEgg = entity.get("has_spawn_egg").getAsBoolean();
            int baseColor = 0xFFFFFF;
            if (entity.has("egg_base_color")) baseColor = entity.get("egg_base_color").getAsInt();
            int dotColor = 0xFFFFFF;
            if (entity.has("egg_dot_color")) dotColor = entity.get("egg_dot_color").getAsInt();

            DefaultAttributeContainer.Builder attributeBuilder = MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, health).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, movementSpeed);
            EntityType.EntityFactory<? extends PathAwareEntity> factory = (t, w) -> new CustomEntity(t, w, entity);
            EntityType.EntityFactory<? extends PassiveEntity> passiveFactory = (t, w) -> new CustomPassiveEntity(t, w, entity);
            EntityType.EntityFactory<? extends HostileEntity> hostileFactory = (t, w) -> new CustomHostileEntity(t, w, entity);
            EntityType.EntityFactory<? extends IllagerEntity> illagerFactory = (t, w) -> new CustomIllagerEntity(t, w, entity);
            EntityType.EntityFactory<? extends WaterCreatureEntity> waterFactory = (t, w) -> new CustomWaterEntity(t, w, entity);

            EntityType<? extends PathAwareEntity> ENTITY_TYPE;
            switch (base) {
                case "passive":
                    ENTITY_TYPE = Registry.register(
                            Registry.ENTITY_TYPE,
                            id,
                            PathAware.createPathAware(spawnGroup, passiveFactory).defaultAttributes(() -> attributeBuilder).dimensions(EntityDimensions.fixed(width, height)).build()
                    );
                    break;
                case "hostile":
                    ENTITY_TYPE = Registry.register(
                        Registry.ENTITY_TYPE,
                        id,
                        PathAware.createPathAware(spawnGroup, hostileFactory).defaultAttributes(() -> attributeBuilder).dimensions(EntityDimensions.fixed(width, height)).build()
                    );
                    break;
                case "illager":
                    ENTITY_TYPE = Registry.register(
                        Registry.ENTITY_TYPE,
                        id,
                        PathAware.createPathAware(spawnGroup, illagerFactory).defaultAttributes(() -> attributeBuilder).dimensions(EntityDimensions.fixed(width, height)).build()
                    );
                    break;
                case "water":
                    ENTITY_TYPE = Registry.register(
                        Registry.ENTITY_TYPE,
                        id,
                        PathAware.createPathAware(spawnGroup, waterFactory).defaultAttributes(() -> attributeBuilder).dimensions(EntityDimensions.fixed(width, height)).build()
                    );
                    break;
                default:
                    ENTITY_TYPE = Registry.register(
                        Registry.ENTITY_TYPE,
                        id,
                        PathAware.createPathAware(spawnGroup, factory).defaultAttributes(() -> attributeBuilder).dimensions(EntityDimensions.fixed(width, height)).build()
                    );
                    break;
            }
            entities.put(id, ENTITY_TYPE);

            if (hasSpawnEgg) Registry.register(Registry.ITEM, getEggId(id), new SpawnEggItem(ENTITY_TYPE, baseColor, dotColor, new Item.Settings().group(ItemGroup.MISC)));
            return true;
        }
        return false;
    }

    private static Identifier getEggId(Identifier id) {
        return new Identifier(id.getNamespace(), id.getPath() + "_spawn_egg");
    }

    public static <T extends PathAwareEntity> void addGoals(GoalSelector goalSelector, GoalSelector targetSelector, JsonObject entity, T pathAwareEntity) {
        if (entity.has("ai_tasks")) {
            JsonArray aiTasks = entity.getAsJsonArray("ai_tasks");
            for (int i = 0; i < aiTasks.size(); i++) {
                if (aiTasks.get(i).isJsonPrimitive()) {
                    if (aiTasks.get(i).getAsJsonPrimitive().isString()) {
                        switch (aiTasks.get(i).getAsString()) {
                            case "break_door":
                                goalSelector.add(i, new BreakDoorGoal(pathAwareEntity, (difficulty) -> difficulty.getId() > 0));
                                break;
                            case "use_door":
                                goalSelector.add(i, new DoorInteractGoal(pathAwareEntity) {});
                                break;
                            case "wander":
                                goalSelector.add(i, new WanderAroundGoal(pathAwareEntity, 1));
                                break;
                            case "look_around":
                                goalSelector.add(i, new LookAroundGoal(pathAwareEntity));
                                break;
                            case "float":
                                goalSelector.add(i, new SwimGoal(pathAwareEntity));
                                break;
                        }
                    }
                } else if (aiTasks.get(i).isJsonObject()) {
                    JsonObject aiTask = aiTasks.get(i).getAsJsonObject();
                    if (aiTask.has("id")) {
                        switch (aiTask.get("id").getAsString()) {
                            case "attack":
                                goalSelector.add(i, new MeleeAttackGoal(pathAwareEntity, 1, true));
                                targetSelector.add(i, new RevengeGoal(pathAwareEntity));
                                if (aiTask.has("targets")) {
                                    for (String target : JsonUtils.jsonArrayToStringList(aiTask.getAsJsonArray("targets"))) {
                                        Class<? extends LivingEntity> targetEntity = (Class<? extends LivingEntity>) Registry.ENTITY_TYPE.get(new Identifier(target)).create(pathAwareEntity.world).getClass();
                                        targetSelector.add(i, new FollowTargetGoal<>(pathAwareEntity, targetEntity, true));
                                    }
                                }
                                break;
                            case "look_at_entity":
                                if (aiTask.has("target")) {
                                    Class<? extends LivingEntity> targetEntity = (Class<? extends LivingEntity>) Registry.ENTITY_TYPE.get(new Identifier(aiTask.get("target").getAsString())).create(pathAwareEntity.world).getClass();
                                    goalSelector.add(i, new LookAtEntityGoal(pathAwareEntity, targetEntity, 5));
                                }
                                break;
                        }
                    }
                }
            }
        }
    }

    public static class PathAware<T extends PathAwareEntity> extends FabricEntityTypeBuilder.Mob<T> {
        private SpawnRestriction.Location restrictionLocation;
        private Heightmap.Type restrictionHeightmap;
        private SpawnRestriction.SpawnPredicate<T> spawnPredicate;

        protected PathAware(SpawnGroup spawnGroup, EntityType.EntityFactory<T> function) {
            super(spawnGroup, function);
        }

        public static PathAware<? extends PathAwareEntity> createPathAware(SpawnGroup spawnGroup, EntityType.EntityFactory<? extends PathAwareEntity> function) {
            return new PathAware<>(spawnGroup, function);
        }

        @Override
        public PathAware<T> spawnGroup(SpawnGroup group) {
            super.spawnGroup(group);
            return this;
        }

        @Override
        public <N extends T> PathAware<N> entityFactory(EntityType.EntityFactory<N> factory) {
            super.entityFactory(factory);
            return (PathAware<N>) this;
        }

        @Override
        public PathAware<T> disableSummon() {
            super.disableSummon();
            return this;
        }

        @Override
        public PathAware<T> disableSaving() {
            super.disableSaving();
            return this;
        }

        @Override
        public PathAware<T> fireImmune() {
            super.fireImmune();
            return this;
        }

        @Override
        public PathAware<T> spawnableFarFromPlayer() {
            super.spawnableFarFromPlayer();
            return this;
        }

        @Override
        public PathAware<T> dimensions(EntityDimensions dimensions) {
            super.dimensions(dimensions);
            return this;
        }

        /**
         * @deprecated use {@link PathAware#trackRangeBlocks(int)}, {@link PathAware#trackedUpdateRate(int)} and {@link PathAware#forceTrackedVelocityUpdates(boolean)}
         */
        @Override
        @Deprecated
        public PathAware<T> trackable(int trackRangeBlocks, int trackedUpdateRate) {
            super.trackable(trackRangeBlocks, trackedUpdateRate);
            return this;
        }

        /**
         * @deprecated use {@link PathAware#trackRangeBlocks(int)}, {@link PathAware#trackedUpdateRate(int)} and {@link PathAware#forceTrackedVelocityUpdates(boolean)}
         */
        @Override
        @Deprecated
        public PathAware<T> trackable(int trackRangeBlocks, int trackedUpdateRate, boolean forceTrackedVelocityUpdates) {
            super.trackable(trackRangeBlocks, trackedUpdateRate, forceTrackedVelocityUpdates);
            return this;
        }

        @Override
        public PathAware<T> trackRangeChunks(int range) {
            super.trackRangeChunks(range);
            return this;
        }

        @Override
        public PathAware<T> trackRangeBlocks(int range) {
            super.trackRangeBlocks(range);
            return this;
        }

        @Override
        public PathAware<T> trackedUpdateRate(int rate) {
            super.trackedUpdateRate(rate);
            return this;
        }

        @Override
        public PathAware<T> forceTrackedVelocityUpdates(boolean forceTrackedVelocityUpdates) {
            super.forceTrackedVelocityUpdates(forceTrackedVelocityUpdates);
            return this;
        }

        @Override
        public PathAware<T> specificSpawnBlocks(Block... blocks) {
            super.specificSpawnBlocks(blocks);
            return this;
        }

        @Override
        public PathAware<T> defaultAttributes(Supplier<DefaultAttributeContainer.Builder> defaultAttributeBuilder) {
            super.defaultAttributes(defaultAttributeBuilder);
            return this;
        }

        /**
         * Registers a spawn restriction for this entity.
         *
         * <p>This is used by mobs to determine whether Minecraft should spawn an entity within a certain context.
         *
         * @return this builder for chaining.
         */
        public PathAware<T> spawnRestriction(SpawnRestriction.Location location, Heightmap.Type heightmap, SpawnRestriction.SpawnPredicate<T> spawnPredicate) {
            this.restrictionLocation = Objects.requireNonNull(location, "Location cannot be null.");
            this.restrictionHeightmap = Objects.requireNonNull(heightmap, "Heightmap type cannot be null.");
            this.spawnPredicate = Objects.requireNonNull(spawnPredicate, "Spawn predicate cannot be null.");
            return this;
        }

        @Override
        public EntityType<T> build() {
            EntityType<T> type = super.build();

            if (this.spawnPredicate != null) {
                SpawnRestrictionAccessor.callRegister(type, this.restrictionLocation, this.restrictionHeightmap, this.spawnPredicate);
            }

            return type;
        }
    }
}