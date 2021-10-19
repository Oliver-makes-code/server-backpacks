package olivermakesco.de.servback.mixin;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ProgressListener;
import olivermakesco.de.servback.Entrypoint;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public class WorldSaveMixin {
    @Inject(at=@At("HEAD"),method="save(Lnet/minecraft/util/ProgressListener;ZZ)V")
    private void saveGlobal(ProgressListener progressListener, boolean flush, boolean bl, CallbackInfo ci) {
        Entrypoint.saveGlobal();
    }
}
