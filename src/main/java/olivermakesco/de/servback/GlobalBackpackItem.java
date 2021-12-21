package olivermakesco.de.servback;

import eu.pb4.polymer.item.VirtualItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class GlobalBackpackItem extends Item implements VirtualItem {
    public GlobalBackpackItem(Item.Settings settings) {
        super(settings.maxCount(1));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        var cast = user.raycast(5,0,false);
        if (cast.getType() == HitResult.Type.BLOCK) return TypedActionResult.pass(stack);
        if (!(user instanceof ServerPlayerEntity player)) return TypedActionResult.pass(stack);
        if (player.isSneaking()) return TypedActionResult.pass(stack);
        new GlobalBackpackGui(player);
        return TypedActionResult.success(stack);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (!(context.getPlayer() instanceof ServerPlayerEntity player)) return ActionResult.PASS;
        if (player.isSneaking()) return ActionResult.PASS;
        new GlobalBackpackGui(player);
        return ActionResult.PASS;
    }

    @Override
    public Item getVirtualItem() {
        return Items.CRAFTING_TABLE;
    }

    @Override
    public ItemStack getVirtualItemStack(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        ItemStack stack = VirtualItem.super.getVirtualItemStack(itemStack, player);
        stack.setCustomName(Text.of("Global Backpack"));
        return stack;
    }
}
