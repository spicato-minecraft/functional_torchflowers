package functional_torchflowers;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FunctionalTorchflowers implements ModInitializer {
	public static final String MOD_ID = "functional_torchflowers";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ServerChunkEvents.CHUNK_LOAD.register((level, chunk, generated) -> TorchflowerLight.relight(level, chunk));
	}
}
