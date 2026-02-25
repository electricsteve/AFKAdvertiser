package electricsteve.afkadvertiser.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.Text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import electricsteve.afkadvertiser.AdvertiserManager;

public class AFKAdvertiserClient implements ClientModInitializer {
    public static final String MOD_ID = "AFKAdvertiser";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static AdvertiserManager adManager;
    @Override
    public void onInitializeClient() {
        adManager = new AdvertiserManager();
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal("afkadvertiser")
                .executes(context -> {
                    boolean new_value = adManager.toggleEnabled();
                    context.getSource().sendFeedback(Text.of("AFK Advertiser " + (new_value ? "enabled" : "disabled") + "."));
                    return 1;
                })));

    }
}