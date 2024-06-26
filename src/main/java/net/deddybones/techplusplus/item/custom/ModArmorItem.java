package net.deddybones.techplusplus.item.custom;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.Level;

import java.util.Map;

public class ModArmorItem extends ArmorItem {
    private static final Map<Holder<ArmorMaterial>, MobEffectInstance> MATERIAL_TO_EFFECT_MAP =
            (new ImmutableMap.Builder<Holder<ArmorMaterial>, MobEffectInstance>())
                    .put(ArmorMaterials.DIAMOND, new MobEffectInstance(MobEffects.NIGHT_VISION,
                            // Duration, Amplifier, Ambient, Visible, ShowIcon
                            200, 1, false, false, true)).build();
    public ModArmorItem(Holder<ArmorMaterial> p_40386_, Type p_266831_, Properties p_40388_) {
        super(p_40386_, p_266831_, p_40388_);
    }

    @Override
    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
        super.onInventoryTick(stack, level, player, slotIndex, selectedIndex);
        if (!level.isClientSide()) {
            evaluateArmorEffects(player);
        }
    }

    private void evaluateArmorEffects(Player player) {
        Holder<ArmorMaterial> _material = hasFullSuitOfArmorOn(player);
        if (_material != null) {
            if (MATERIAL_TO_EFFECT_MAP.containsKey(_material)) {
                player.addEffect(new MobEffectInstance(MATERIAL_TO_EFFECT_MAP.get(_material)));
            }
        }
    }

    private Holder<ArmorMaterial> hasFullSuitOfArmorOn(Player player) {
        Holder<ArmorMaterial> _material = null;
        for (ItemStack i : player.getArmorSlots()) {
            if (i.isEmpty()) return null;
            Holder<ArmorMaterial> _thisMaterial = ((ArmorItem) i.getItem()).getMaterial();
            if (_material == null) _material = _thisMaterial;
            else if (_material != _thisMaterial) {
                _material = null;
                break;
            }
        }
        return _material;
    }
}
