package trufflez.justload.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import trufflez.justload.client.JustLoadClient;

@Mixin(Minecraft.class)
public class JustLoadEntrypoint {
    
    @Inject(at = @At("TAIL"), method = "<init>()V")
    public void init(CallbackInfo ci) {
        JustLoadClient.LOGGER.info("Mixin injected into Minecraft");
        JustLoadClient.init();
    }
}
