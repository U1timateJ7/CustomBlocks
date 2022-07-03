package com.ulto.customblocks.client.renderer.entity.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.ulto.customblocks.util.JsonUtils;
import com.ulto.customblocks.util.Vec2i;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class CustomEntityModel<E extends PathfinderMob> extends EntityModel<E> {
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

    public static LayerDefinition getTexturedModelData(JsonObject model) {
        JsonArray bones = model.getAsJsonArray("parts");
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        for (JsonObject part : JsonUtils.jsonArrayToJsonObjectList(bones)) {
            CubeListBuilder builder = CubeListBuilder.create();
            Vector3f pivot = new Vector3f(part.getAsJsonArray("pivot").get(0).getAsFloat(), part.getAsJsonArray("pivot").get(1).getAsFloat(), part.getAsJsonArray("pivot").get(2).getAsFloat());
            Vector3f rotation = part.has("rotation") ? new Vector3f((float) -Math.toRadians(part.getAsJsonArray("rotation").get(0).getAsFloat()), (float) -Math.toRadians(part.getAsJsonArray("rotation").get(1).getAsFloat()), (float) -Math.toRadians(part.getAsJsonArray("rotation").get(2).getAsFloat())) : Vector3f.ZERO;
            for (JsonObject cube : JsonUtils.jsonArrayToJsonObjectList(part.getAsJsonArray("cubes"))) {
                Vector3f size = new Vector3f(cube.getAsJsonArray("size").get(0).getAsFloat(), cube.getAsJsonArray("size").get(1).getAsFloat(), cube.getAsJsonArray("size").get(2).getAsFloat());
                Vector3f origin = new Vector3f(cube.getAsJsonArray("origin").get(0).getAsFloat(), cube.getAsJsonArray("origin").get(1).getAsFloat(), cube.getAsJsonArray("origin").get(2).getAsFloat());
                Vec2i uv = new Vec2i(cube.getAsJsonArray("uv").get(0).getAsInt(), cube.getAsJsonArray("uv").get(1).getAsInt());
                float inflate = cube.has("inflate") ? cube.get("inflate").getAsFloat() : 0;
                builder.texOffs(uv.x(), uv.y()).addBox(origin.x(), origin.y(), origin.z(), size.x(), size.y(), size.z(), new CubeDeformation(inflate));
            }
            if (part.has("parent")) {
                modelPartData.getChild(part.get("parent").getAsString()).addOrReplaceChild(part.get("name").getAsString(), builder.mirror(part.has("mirror") && part.get("mirror").getAsBoolean()), PartPose.offsetAndRotation(pivot.x(), pivot.y(), pivot.z(), rotation.x(), rotation.y(), rotation.z()));
            }
            else modelPartData.addOrReplaceChild(part.get("name").getAsString(), builder.mirror(part.has("mirror") && part.get("mirror").getAsBoolean()), PartPose.offsetAndRotation(pivot.x(), pivot.y(), pivot.z(), rotation.x(), rotation.y(), rotation.z()));
        }
        return LayerDefinition.create(modelData, model.get("texture_width").getAsInt(), model.get("texture_height").getAsInt());
    }

    @Override
    public void setupAnim(E entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        if (this.entity.has("model_animations")) {
            JsonArray bones = model.getAsJsonArray("parts");
            for (Map.Entry<String, JsonElement> entry : this.entity.getAsJsonObject("model_animations").entrySet()) {
                for (JsonObject part : JsonUtils.jsonArrayToJsonObjectList(bones)) {
                    if (entry.getKey().equals(part.get("name").getAsString())) {
                        switch (entry.getValue().getAsString()) {
                            case "head_movement" -> {
                                parts.get(part.get("name").getAsString()).yRot = headYaw / (180F / (float)Math.PI);
                                parts.get(part.get("name").getAsString()).xRot = headPitch / (180F / (float)Math.PI);
                            }
                            case "left_arm_swing" -> parts.get(part.get("name").getAsString()).xRot = Mth.cos(limbAngle * 0.6662F) * limbDistance;
                            case "right_arm_swing" -> parts.get(part.get("name").getAsString()).xRot = Mth.cos(limbAngle * 0.6662F + (float)Math.PI) * limbDistance;
                            case "left_leg_swing" -> parts.get(part.get("name").getAsString()).xRot = Mth.cos(limbAngle) * 1.0F * limbDistance;
                            case "right_leg_swing" -> parts.get(part.get("name").getAsString()).xRot = Mth.cos(limbAngle) * -1.0F * limbDistance;
                            case "constant_rotation_x" -> parts.get(part.get("name").getAsString()).xRot = animationProgress;
                            case "constant_rotation_y" -> parts.get(part.get("name").getAsString()).yRot = animationProgress;
                            case "constant_rotation_z" -> parts.get(part.get("name").getAsString()).zRot = animationProgress;
                            case "constant_slow_rotation_x" -> parts.get(part.get("name").getAsString()).xRot = animationProgress / 20;
                            case "constant_slow_rotation_y" -> parts.get(part.get("name").getAsString()).yRot = animationProgress / 20;
                            case "constant_slow_rotation_z" -> parts.get(part.get("name").getAsString()).zRot = animationProgress / 20;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        parts.forEach((name, modelRenderer) -> modelRenderer.render(matrices, vertices, light, overlay, red, green, blue, alpha));
    }
}
