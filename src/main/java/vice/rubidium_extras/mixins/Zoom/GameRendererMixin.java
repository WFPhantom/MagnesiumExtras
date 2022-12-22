package vice.rubidium_extras.mixins.Zoom;

import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import vice.rubidium_extras.WiZoom;
import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(
            at = @At("RETURN"),
            method = "getFov(Lnet/minecraft/client/Camera;FZ)D",
            cancellable = true
    )
    private void onGetFov(Camera camera, float tickDelta, boolean changingFov,
                          CallbackInfoReturnable<Double> cir)
    {
        cir.setReturnValue(
                WiZoom.INSTANCE.changeFovBasedOnZoom(cir.getReturnValueD()));
    }
    @Shadow
    public void close() throws Exception
    {

    }
}