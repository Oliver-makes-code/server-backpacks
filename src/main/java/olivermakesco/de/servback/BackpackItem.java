package olivermakesco.de.servback;

import eu.pb4.polymer.item.VirtualItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BackpackItem extends Item implements VirtualItem {
    int slots;

    public BackpackItem(Settings settings, int slots) {
        super(settings.maxCount(1));
        this.slots = slots;
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack stack = new ItemStack(this);

        return stack;
    }

    public NbtCompound generateDefaultInventory() {
        NbtCompound nbt = new NbtCompound();
        nbt.putInt("color", 0);
        DefaultedList<ItemStack> list = DefaultedList.ofSize(slots, ItemStack.EMPTY);
        nbt.put("inventory", Inventories.writeNbt(new NbtCompound(), list));
        return nbt;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (user.isSneaking()) return TypedActionResult.pass(stack);
        if (user instanceof ServerPlayerEntity player) {
            BackpackGui gui = new BackpackGui(player, slots, stack);
            gui.open();
        }
        return TypedActionResult.success(stack);
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
