package com.github.moonshinecat.brightsigns.mixin;

import com.github.moonshinecat.brightsigns.BrightSigns;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySignRenderer;
import net.minecraft.tileentity.TileEntitySign;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntitySignRenderer.class)
public class MixinTileEntitySignRenderer {
    @Inject(
            method = "renderTileEntityAt(Lnet/minecraft/tileentity/TileEntitySign;DDDFI)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;depthMask(Z)V", shift = At.Shift.AFTER, ordinal = 0))
    public void renderTileEntityAt_depthMaskFalse(TileEntitySign te, double x, double y, double z, float partialTicks, int destroyStage, CallbackInfo ci){
        if(BrightSigns.getConfig().state){
            GlStateManager.disableLighting();
        }
    }

    @Inject(
            method = "renderTileEntityAt(Lnet/minecraft/tileentity/TileEntitySign;DDDFI)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;depthMask(Z)V", ordinal = 1))
    public void renderTileEntityAt_depthMaskTrue(TileEntitySign te, double x, double y, double z, float partialTicks, int destroyStage, CallbackInfo ci){
        if(BrightSigns.getConfig().state){
            GlStateManager.enableLighting();
        }
    }
}
