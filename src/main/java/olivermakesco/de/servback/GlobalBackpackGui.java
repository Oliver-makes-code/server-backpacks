package olivermakesco.de.servback;

import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class GlobalBackpackGui extends SimpleGui {
    public ItemStack stack;
    public GlobalBackpackGui(ServerPlayerEntity player, ItemStack stack) {
        super(ScreenHandlerType.GENERIC_9X3, player, false);
        this.stack = stack;
        setTitle(stack.getName());
        fillChest();
        open();
    }

    public void fillChest() {
        Inventory inventory = Entrypoint.getInventory();
        for (int i = 0; i < 27; i++)
            setSlotRedirect(i, new Slot(inventory, i, i, 0));
    }
}