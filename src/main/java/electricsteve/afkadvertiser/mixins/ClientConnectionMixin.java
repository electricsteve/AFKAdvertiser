package electricsteve.afkadvertiser.mixins;

import net.minecraft.network.ClientConnection;
import net.minecraft.text.Text;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import electricsteve.afkadvertiser.client.AFKAdvertiserClient;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @Inject(method = "disconnect", at = @At("HEAD"))
    public void disconnect(Text disconnectReason, CallbackInfo ci) {
        AFKAdvertiserClient.adManager.setEnabled(false);
    }
}
