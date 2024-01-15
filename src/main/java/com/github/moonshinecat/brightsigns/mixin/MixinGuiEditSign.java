package com.github.moonshinecat.brightsigns.mixin;

import com.github.moonshinecat.brightsigns.BrightSigns;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.renderer.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiEditSign.class)
public class MixinGuiEditSign {
    @Inject(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiScreen;drawScreen(IIF)V"))
    public void onDrawScreen_PreSuper(CallbackInfo ci) {
        if(BrightSigns.getConfig().state){
            GlStateManager.disableLighting();
        }
    }

    @Inject(method = "drawScreen", at = @At("RETURN"))
    public void onDrawScreen_End(CallbackInfo ci){
        if(BrightSigns.getConfig().state){
            GlStateManager.enableLighting();
        }
    }
}
