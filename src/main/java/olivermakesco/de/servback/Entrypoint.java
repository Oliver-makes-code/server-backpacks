package olivermakesco.de.servback;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;

import java.io.*;
import java.nio.file.Path;
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
		Registry.register(Registry.ITEM,new Identifier("serverbackpacks","ender"), new EnderBackpackItem(new FabricItemSettings()));
		Registry.register(Registry.ITEM,new Identifier("serverbackpacks","global"), new GlobalBackpackItem(new FabricItemSettings()));

		ServerLifecycleEvents.SERVER_STARTING.register((s) -> {
			server = s;
			loadGlobal();
		});
		ServerLifecycleEvents.SERVER_STOPPING.register((s) -> {
			saveGlobal();
		});
	}
	public static void loadGlobal() {
		try {
			Path savePath = server.getSavePath(WorldSavePath.ROOT);
			InputStream in = new FileInputStream(savePath.toString() + "/backpacks_global_inv.nbt");
			NbtCompound nbt = NbtIo.readCompressed(in);
			DefaultedList<ItemStack> inv = DefaultedList.ofSize(27,ItemStack.EMPTY);
			Inventories.readNbt(nbt,inv);
			globalInventory = inv.toArray(ItemStack[]::new);
		} catch (Exception ignored) {
			DefaultedList<ItemStack> inv = DefaultedList.ofSize(27,ItemStack.EMPTY);
			globalInventory = inv.toArray(ItemStack[]::new);
		}
	}
	public static void saveGlobal() {
		try {
			Path savePath = server.getSavePath(WorldSavePath.ROOT);
			OutputStream out = new FileOutputStream(savePath.toString() + "/backpacks_global_inv.nbt");
			DefaultedList<ItemStack> inv = DefaultedList.ofSize(27,ItemStack.EMPTY);
			for (int i = 0; i < 27; i++) {
				ItemStack stack = globalInventory[i];
				if (stack == null) stack = ItemStack.EMPTY;
				inv.set(i,stack);
			}
			NbtCompound invNbt = Inventories.writeNbt(new NbtCompound(), inv,true);
			NbtIo.writeCompressed(invNbt,out);
		} catch (Exception ignored) {}
	}

    public static Inventory getInventory() {
        return new SimpleInventory(globalInventory);
    }

	public static MinecraftServer server;
	public static ItemStack[] globalInventory = new ItemStack[27];

	public static HashMap<String, Integer> backpacks;
	static {
		backpacks = new HashMap<>();
		backpacks.put("small" ,  9);
		backpacks.put("medium", 18);
		backpacks.put("large" , 27);
	}
}
