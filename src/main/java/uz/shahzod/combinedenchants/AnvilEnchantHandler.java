package uz.shahzod.combinedenchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = CombinedEnchantsMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AnvilEnchantHandler {

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();

        if (left.isEmpty() || right.isEmpty()) return;

        // Right item must be an enchanted book or enchanted armor
        Map<Enchantment, Integer> rightEnchs = EnchantmentHelper.getEnchantments(right);
        if (rightEnchs.isEmpty()) return;

        // Check if left item is armor
        if (!isArmor(left)) return;

        // Get current enchantments on left item
        Map<Enchantment, Integer> leftEnchs = new HashMap<>(EnchantmentHelper.getEnchantments(left));

        boolean changed = false;
        int cost = 0;

        for (Map.Entry<Enchantment, Integer> entry : rightEnchs.entrySet()) {
            Enchantment ench = entry.getKey();
            int level = entry.getValue();

            // Only process protection enchantments
            if (!isProtectionEnchant(ench)) continue;

            // Can this enchantment go on this item normally (ignoring compatibility)?
            if (!ench.canEnchant(left)) continue;

            int existingLevel = leftEnchs.getOrDefault(ench, 0);
            int newLevel = existingLevel == level ? Math.min(level + 1, ench.getMaxLevel()) : Math.max(existingLevel, level);

            leftEnchs.put(ench, newLevel);
            cost += newLevel;
            changed = true;
        }

        if (!changed) return;

        // Build result
        ItemStack result = left.copy();
        EnchantmentHelper.setEnchantments(leftEnchs, result);

        String name = event.getName();
        if (name != null && !name.isEmpty()) {
            result.setHoverName(new net.minecraft.util.text.StringTextComponent(name));
        }

        event.setOutput(result);
        event.setCost(Math.max(1, cost));
        event.setMaterialCost(1);
    }

    private static boolean isArmor(ItemStack stack) {
        return stack.getItem() instanceof net.minecraft.item.ArmorItem;
    }

    private static boolean isProtectionEnchant(Enchantment ench) {
        return ench == Enchantments.ALL_DAMAGE_PROTECTION
            || ench == Enchantments.FIRE_PROTECTION
            || ench == Enchantments.BLAST_PROTECTION
            || ench == Enchantments.PROJECTILE_PROTECTION;
    }
}
