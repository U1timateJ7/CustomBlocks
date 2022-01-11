package com.ulto.customblocks.entity;

import com.google.gson.JsonObject;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.world.World;

public class CustomEntity extends PathAwareEntity {
    private JsonObject entity;

    public CustomEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    public void setJsonObject(JsonObject entity) {
        this.entity = entity;
    }
}
