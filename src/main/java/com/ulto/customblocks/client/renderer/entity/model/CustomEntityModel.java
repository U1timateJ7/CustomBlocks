package com.ulto.customblocks.client.renderer.entity.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ulto.customblocks.util.JsonUtils;
import io.github.cottonmc.cotton.gui.widget.data.Vec2i;
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

    public CustomEntityModel(ModelPart modelPart, JsonObject model, JsonObject entity) {
        JsonArray bones = model.getAsJsonArray("parts");
        for (JsonObject part : JsonUtils.jsonArrayToJsonObjectList(bones)) {
            if (part.has("parent")) {
                parts.put(part.get("name").getAsString(), modelPart.getChild(part.get("parent").getAsString()).getChild(part.get("name").getAsString()));
            }
            else parts.put(part.get("name").getAsString(), modelPart.getChild(part.get("name").getAsString()));
        }
        this.entity = entity;
        this.model = model;
    }

    public static TexturedModelData getTexturedModelData(JsonObject model) {
        JsonArray bones = model.getAsJsonArray("parts");
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        for (JsonObject part : JsonUtils.jsonArrayToJsonObjectList(bones)) {
            ModelPartBuilder builder = ModelPartBuilder.create();
            Vec3f pivot = new Vec3f(part.getAsJsonArray("pivot").get(0).getAsFloat(), part.getAsJsonArray("pivot").get(1).getAsFloat(), part.getAsJsonArray("pivot").get(2).getAsFloat());
            Vec3f rotation = part.has("rotation") ? new Vec3f((float) -Math.toRadians(part.getAsJsonArray("rotation").get(0).getAsFloat()), (float) -Math.toRadians(part.getAsJsonArray("rotation").get(1).getAsFloat()), (float) -Math.toRadians(part.getAsJsonArray("rotation").get(2).getAsFloat())) : Vec3f.ZERO;
            for (JsonObject cube : JsonUtils.jsonArrayToJsonObjectList(part.getAsJsonArray("cubes"))) {
                Vec3f size = new Vec3f(cube.getAsJsonArray("size").get(0).getAsFloat(), cube.getAsJsonArray("size").get(1).getAsFloat(), cube.getAsJsonArray("size").get(2).getAsFloat());
                Vec3f origin = new Vec3f(cube.getAsJsonArray("origin").get(0).getAsFloat(), cube.getAsJsonArray("origin").get(1).getAsFloat(), cube.getAsJsonArray("origin").get(2).getAsFloat());
                Vec2i uv = new Vec2i(cube.getAsJsonArray("uv").get(0).getAsInt(), cube.getAsJsonArray("uv").get(1).getAsInt());
                float inflate = cube.has("inflate") ? cube.get("inflate").getAsFloat() : 0;
                builder.uv(uv.x(), uv.y()).cuboid(origin.getX(), origin.getY(), origin.getZ(), size.getX(), size.getY(), size.getZ(), new Dilation(inflate));
            }
            if (part.has("parent")) {
                modelPartData.getChild(part.get("parent").getAsString()).addChild(part.get("name").getAsString(), builder.mirrored(part.has("mirror") && part.get("mirror").getAsBoolean()), ModelTransform.of(pivot.getX(), pivot.getY(), pivot.getZ(), rotation.getX(), rotation.getY(), rotation.getZ()));
            }
            else modelPartData.addChild(part.get("name").getAsString(), builder.mirrored(part.has("mirror") && part.get("mirror").getAsBoolean()), ModelTransform.of(pivot.getX(), pivot.getY(), pivot.getZ(), rotation.getX(), rotation.getY(), rotation.getZ()));
        }
        return TexturedModelData.of(modelData, model.get("texture_width").getAsInt(), model.get("texture_height").getAsInt());
    }

    @Override
    public void setAngles(E entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        if (this.entity.has("model_animations")) {
            JsonArray bones = model.getAsJsonArray("parts");
            for (Map.Entry<String, JsonElement> entry : this.entity.getAsJsonObject("model_animations").entrySet()) {
                for (JsonObject part : JsonUtils.jsonArrayToJsonObjectList(bones)) {
                    if (entry.getKey().equals(part.get("name").getAsString())) {
                        switch (entry.getValue().getAsString()) {
                            case "head_movement" -> {
                                parts.get(part.get("name").getAsString()).yaw = headYaw / (180F / (float)Math.PI);
                                parts.get(part.get("name").getAsString()).pitch = headPitch / (180F / (float)Math.PI);
                            }
                            case "left_arm_swing" -> parts.get(part.get("name").getAsString()).pitch = MathHelper.cos(limbAngle * 0.6662F) * limbDistance;
                            case "right_arm_swing" -> parts.get(part.get("name").getAsString()).pitch = MathHelper.cos(limbAngle * 0.6662F + (float)Math.PI) * limbDistance;
                            case "left_leg_swing" -> parts.get(part.get("name").getAsString()).pitch = MathHelper.cos(limbAngle) * 1.0F * limbDistance;
                            case "right_leg_swing" -> parts.get(part.get("name").getAsString()).pitch = MathHelper.cos(limbAngle) * -1.0F * limbDistance;
                            case "constant_rotation_x" -> parts.get(part.get("name").getAsString()).pitch = animationProgress;
                            case "constant_rotation_y" -> parts.get(part.get("name").getAsString()).yaw = animationProgress;
                            case "constant_rotation_z" -> parts.get(part.get("name").getAsString()).roll = animationProgress;
                            case "constant_slow_rotation_x" -> parts.get(part.get("name").getAsString()).pitch = animationProgress / 20;
                            case "constant_slow_rotation_y" -> parts.get(part.get("name").getAsString()).yaw = animationProgress / 20;
                            case "constant_slow_rotation_z" -> parts.get(part.get("name").getAsString()).roll = animationProgress / 20;
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
}
