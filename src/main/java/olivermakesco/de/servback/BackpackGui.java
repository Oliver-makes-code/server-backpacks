package olivermakesco.de.servback;

import eu.pb4.sgui.api.gui.SimpleGui;
import eu.pb4.sgui.virtual.inventory.VirtualSlot;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;

public class BackpackGui extends SimpleGui {
    public final int slots;
    public ItemStack stack;
    public Inventory inventory;

    public BackpackGui(ServerPlayerEntity player, int slots, ItemStack stack) {
        super(
                getHandler(slots),
                player, false
        );
        open();
        this.slots = slots;
        this.stack = stack;
        setTitle(stack.getName());
        NbtCompound nbt = stack.getOrCreateNbt();
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

    public static ScreenHandlerType<?> getHandler(int slots) {
        return switch (slots/9) {
            case 1 -> ScreenHandlerType.GENERIC_9X1;
            case 2 -> ScreenHandlerType.GENERIC_9X2;
            case 3 -> ScreenHandlerType.GENERIC_9X3;
            default -> null;
        };
    }

    public void fillChest() {
        for (int i = 0; i < slots; i++)
            setSlotRedirect(i, new BackpackSlot(inventory,i,i,0));
    }

    public void saveMain() {
        DefaultedList<ItemStack> inv = DefaultedList.ofSize(slots,ItemStack.EMPTY);
        for (int i = 0; i < slots; i++) {
            ItemStack stack = getSlotRedirect(i).getStack();
            inv.set(i,stack);
        }
        NbtCompound root = stack.getNbt();
        NbtCompound invNbt = Inventories.writeNbt(root, inv);
        stack.setNbt(invNbt);
    }

    @Override
    public void onClose() {
        saveMain();
    }
}
