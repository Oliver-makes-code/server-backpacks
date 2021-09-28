package olivermakesco.de.servback;

import eu.pb4.polymer.item.VirtualItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.apache.logging.log4j.core.jmx.Server;
import org.jetbrains.annotations.Nullable;

public class BackpackItem extends Item implements VirtualItem {
    int slots;
    String name;

    public BackpackItem(Settings settings, int slots) {
        super(settings.maxCount(1));
        this.slots = slots;
        if (slots == 9)
            name = "Small";
        if (slots == 18)
            name = "Medium";
        if (slots == 27)
            name = "Large";
    }

    @Override
    public Text getName(ItemStack stack) {
        return Text.of(name + " Backpack");
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (!(context.getPlayer() instanceof ServerPlayerEntity)) return ActionResult.PASS;
        ServerPlayerEntity player = (ServerPlayerEntity) context.getPlayer();
        if (player == null) return ActionResult.PASS;
        if (player.isSneaking()) return ActionResult.PASS;
        new BackpackGui(player,slots,context.getStack());
        return ActionResult.PASS;
    }

    @Override
    public Item getVirtualItem() {
        return Items.CHEST;
    }

    @Override
    public Item getVirtualItem(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        return getVirtualItem();
    }

    @Override
    public ItemStack getVirtualItemStack(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        return getVirtualItem().getDefaultStack().setCustomName(itemStack.getName());
    }
}
