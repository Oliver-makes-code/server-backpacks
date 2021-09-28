package olivermakesco.de.servback;

import eu.pb4.sgui.api.gui.SimpleGui;
import eu.pb4.sgui.virtual.inventory.VirtualSlot;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;

public class BackpackGui extends SimpleGui {
    public final int slots;
    public ItemStack stack;
    public Inventory inventory;

    public BackpackGui(ServerPlayerEntity player, int slots, ItemStack stack) {
        super(
                slots == 27? ScreenHandlerType.GENERIC_9X3: slots == 18? ScreenHandlerType.GENERIC_9X2 : ScreenHandlerType.GENERIC_9X1,
                player, false
        );
        open();
        this.slots = slots;
        this.stack = stack;
        setTitle(stack.getName());
        NbtCompound nbt = stack.getOrCreateSubNbt("inventory");
        DefaultedList<ItemStack> list = DefaultedList.ofSize(slots+1, ItemStack.EMPTY);
        list.set(slots,stack);
        Inventories.readNbt(nbt,list);
        inventory = new SimpleInventory(list.toArray(ItemStack[]::new));
        fillChest();
        for (int i = 0; i < 9; i++) {
            ItemStack invStack = player.getInventory().getStack(i);
            if (invStack.equals(stack)) {
                screenHandler.setSlot(slots+27+i, new NopSlot(inventory,slots,slots,0));
            }
        }
    }

    public void fillChest() {
        for (int i = 0; i < slots; i++) {
            setSlotRedirect(i, new BackpackSlot(inventory,i,i,0));
        }
    }

    @Override
    public void onClose() {
        DefaultedList<ItemStack> inv = DefaultedList.ofSize(slots,ItemStack.EMPTY);
        for (int i = 0; i < slots; i++) {
            ItemStack stack = getSlotRedirect(i).getStack();
            inv.set(i,stack);
        }
        NbtCompound invNbt = Inventories.writeNbt(new NbtCompound(), inv);
        NbtCompound root = stack.getNbt();
        root.put("inventory", invNbt);
        stack.setNbt(root);
    }
}
