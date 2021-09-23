package olivermakesco.de.servback;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;

public class Entrypoint implements ModInitializer {
	@Override
	public void onInitialize() {
		for (String s : backpacks.keySet()) {
			int i = backpacks.get(s);
			Identifier id = new Identifier("serverbackpacks",s);
			Item item = new BackpackItem(new FabricItemSettings(), i);
			Registry.register(Registry.ITEM,id,item);
		}
	}
	public static HashMap<String, Integer> backpacks;

	static {
		backpacks = new HashMap<>();
		backpacks.put("small" ,  9);
		backpacks.put("medium", 18);
		backpacks.put("large" , 27);
	}
}
