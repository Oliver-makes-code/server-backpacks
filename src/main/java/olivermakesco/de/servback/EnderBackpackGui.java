package olivermakesco.de.servback;

import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class EnderBackpackGui extends SimpleGui {
    public ItemStack stack;
    public EnderBackpackGui(ServerPlayerEntity player, ItemStack stack) {
        super(
                ScreenHandlerType.GENERIC_9X3,
                player,
                false
        );
        this.stack = stack;
        fillChest();
        setTitle(stack.getName());
        open();

    }
    public void fillChest() {
        EnderChestInventory inventory = player.getEnderChestInventory();
        for (int i = 0; i < 27; i++)
            setSlotRedirect(i,new Slot(inventory,i,i,0));
    }
}
