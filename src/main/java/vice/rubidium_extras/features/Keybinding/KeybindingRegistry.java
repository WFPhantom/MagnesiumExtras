package vice.rubidium_extras.features.Keybinding;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
/*
public class KeybindingRegistry {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> RegisterKeyMappingsEvent(KeyboardInput.zoomKey));
    }
}
*/
public class KeybindingRegistry {
    @SubscribeEvent
    public static void onKeyBindRegister(RegisterKeyMappingsEvent event){
        KeybindingRegistry.onKeyBindRegister(event);
        event.register(KeyboardInput.zoomKey);
    }
}
