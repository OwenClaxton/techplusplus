package net.deddybones.techplusplus.gui;

import net.deddybones.techplusplus.gui.menu.ClayMolderMenu;
import net.deddybones.techplusplus.gui.menu.CrusherMenu;
import net.deddybones.techplusplus.gui.menu.KilnMenu;
import net.deddybones.techplusplus.gui.menu.SmelteryMenu;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static net.deddybones.techplusplus.TechPlusPlus.MOD_ID;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, MOD_ID);

    public static final RegistryObject<MenuType<CrusherMenu>> CRUSHER =
            registerMenuType("crusher", CrusherMenu::new);

    public static final RegistryObject<MenuType<KilnMenu>> KILN =
            registerMenuType("kiln", KilnMenu::new);

    public static final RegistryObject<MenuType<ClayMolderMenu>> CLAY_MOLDER =
            registerMenuType("clay_molder", ClayMolderMenu::new);

    public static final RegistryObject<MenuType<SmelteryMenu>> SMELTERY =
            registerMenuType("smeltery", SmelteryMenu::new);

    @SuppressWarnings("SameParameterValue")
    private static <T extends AbstractContainerMenu>RegistryObject<MenuType<T>> registerMenuType(
            String name, MenuType.MenuSupplier<T> p_39990_) {
        return MENUS.register(name, () -> new MenuType<>(p_39990_, FeatureFlags.DEFAULT_FLAGS));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
