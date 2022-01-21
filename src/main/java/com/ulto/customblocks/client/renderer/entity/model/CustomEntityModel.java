package com.ulto.customblocks.client.renderer.entity.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ulto.customblocks.util.JsonUtils;
import com.ulto.customblocks.util.Vec2i;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class CustomEntityModel<E extends PathAwareEntity> extends EntityModel<E> {
    public Map<String, ModelPart> parts = new HashMap<>();
    private final JsonObject entity;
    private JsonObject model;

    public CustomEntityModel(JsonObject model, JsonObject entity) {
        JsonArray bones = model.getAsJsonArray("parts");
        this.entity = entity;
        this.model = model;

        textureWidth = model.get("texture_width").getAsInt();
        textureHeight = model.get("texture_height").getAsInt();

        for (JsonObject part : JsonUtils.jsonArrayToJsonObjectList(bones)) {
            Vec3f pivot = new Vec3f(part.getAsJsonArray("pivot").get(0).getAsFloat(), part.getAsJsonArray("pivot").get(1).getAsFloat(), part.getAsJsonArray("pivot").get(2).getAsFloat());
            Vec3f rotation = part.has("rotation") ? new Vec3f((float) -Math.toRadians(part.getAsJsonArray("rotation").get(0).getAsFloat()), (float) -Math.toRadians(part.getAsJsonArray("rotation").get(1).getAsFloat()), (float) -Math.toRadians(part.getAsJsonArray("rotation").get(2).getAsFloat())) : new Vec3f(0 ,0 ,0);

            ModelPart bone = new ModelPart(this);
            bone.setPivot(pivot.getX(), pivot.getY(), pivot.getZ());
            bone.mirror = part.has("mirror") && part.get("mirror").getAsBoolean();
            setRotationAngle(bone, rotation);
            for (JsonObject cube : JsonUtils.jsonArrayToJsonObjectList(part.getAsJsonArray("cubes"))) {
                Vec3f size = new Vec3f(cube.getAsJsonArray("size").get(0).getAsFloat(), cube.getAsJsonArray("size").get(1).getAsFloat(), cube.getAsJsonArray("size").get(2).getAsFloat());
                Vec3f origin = new Vec3f(cube.getAsJsonArray("origin").get(0).getAsFloat(), cube.getAsJsonArray("origin").get(1).getAsFloat(), cube.getAsJsonArray("origin").get(2).getAsFloat());
                Vec2i uv = new Vec2i(cube.getAsJsonArray("uv").get(0).getAsInt(), cube.getAsJsonArray("uv").get(1).getAsInt());
                float inflate = cube.has("inflate") ? cube.get("inflate").getAsFloat() : 0;
                bone.setTextureOffset(uv.x(), uv.y()).addCuboid(origin.getX(), origin.getY(), origin.getZ(), size.getX(), size.getY(), size.getZ(), inflate, false);
            }
            parts.put(part.get("name").getAsString(), bone);

            if (part.has("parent")) parts.get(part.get("parent").getAsString()).addChild(bone);
        }
    }

    @Override
    public void setAngles(E entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        if (this.entity.has("model_animations")) {
            JsonArray bones = model.getAsJsonArray("parts");
            for (Map.Entry<String, JsonElement> entry : this.entity.getAsJsonObject("model_animations").entrySet()) {
                for (JsonObject part : JsonUtils.jsonArrayToJsonObjectList(bones)) {
                    if (entry.getKey().equals(part.get("name").getAsString())) {
                        switch (entry.getValue().getAsString()) {
                            case "head_movement":
                                parts.get(part.get("name").getAsString()).yaw = headYaw / (180F / (float)Math.PI);
                                parts.get(part.get("name").getAsString()).pitch = headPitch / (180F / (float)Math.PI);
                                break;
                            case "left_arm_swing":
                                parts.get(part.get("name").getAsString()).pitch = MathHelper.cos(limbAngle * 0.6662F) * limbDistance;
                                break;
                            case "right_arm_swing":
                                parts.get(part.get("name").getAsString()).pitch = MathHelper.cos(limbAngle * 0.6662F + (float)Math.PI) * limbDistance;
                                break;
                            case "left_leg_swing":
                                parts.get(part.get("name").getAsString()).pitch = MathHelper.cos(limbAngle) * 1.0F * limbDistance;
                                break;
                            case "right_leg_swing":
                                parts.get(part.get("name").getAsString()).pitch = MathHelper.cos(limbAngle) * -1.0F * limbDistance;
                                break;
                            case "constant_rotation_x":
                                parts.get(part.get("name").getAsString()).pitch = animationProgress;
                                break;
                            case "constant_rotation_y":
                                parts.get(part.get("name").getAsString()).yaw = animationProgress;
                                break;
                            case "constant_rotation_z":
                                parts.get(part.get("name").getAsString()).roll = animationProgress;
                                break;
                            case "constant_slow_rotation_x":
                                parts.get(part.get("name").getAsString()).pitch = animationProgress / 20;
                                break;
                            case "constant_slow_rotation_y":
                                parts.get(part.get("name").getAsString()).yaw = animationProgress / 20;
                                break;
                            case "constant_slow_rotation_z":
                                parts.get(part.get("name").getAsString()).roll = animationProgress / 20;
                                break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        parts.forEach((name, modelRenderer) -> modelRenderer.render(matrices, vertices, light, overlay, red, green, blue, alpha));
    }

    public void setRotationAngle(ModelPart bone, Vec3f rotation) {
        bone.pitch = rotation.getX();
        bone.yaw = rotation.getY();
        bone.roll = rotation.getZ();
    }
}
