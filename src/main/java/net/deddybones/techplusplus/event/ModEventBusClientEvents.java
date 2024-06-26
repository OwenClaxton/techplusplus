package net.deddybones.techplusplus.event;

import com.google.common.collect.ImmutableList;
import net.deddybones.techplusplus.TechPlusPlus;
import net.deddybones.techplusplus.entity.ModEntities;
import net.deddybones.techplusplus.entity.client.ModModelLayers;
import net.deddybones.techplusplus.entity.client.ThrownWoodenSpearModel;
import net.deddybones.techplusplus.entity.client.ThrownWoodenSpearRenderer;
import net.deddybones.techplusplus.gui.screen.SmelteryScreen;
import net.deddybones.techplusplus.recipes.ModRecipes;
import net.deddybones.techplusplus.gui.screen.ClayMolderScreen;
import net.deddybones.techplusplus.gui.screen.CrusherScreen;
import net.deddybones.techplusplus.gui.screen.KilnScreen;
import net.deddybones.techplusplus.gui.ModMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterRecipeBookCategoriesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static net.deddybones.techplusplus.TechPlusPlus.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(ModEntities.THROWN_WOODEN_SPEAR_ENTITY_TYPE.get(), ThrownWoodenSpearRenderer::new);

        MenuScreens.register(ModMenuTypes.CRUSHER.get(), CrusherScreen::new);
        MenuScreens.register(ModMenuTypes.KILN.get(), KilnScreen::new);
        MenuScreens.register(ModMenuTypes.CLAY_MOLDER.get(), ClayMolderScreen::new);
        MenuScreens.register(ModMenuTypes.SMELTERY.get(), SmelteryScreen::new);
    }

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.THROWN_WOODEN_SPEAR_LAYER, ThrownWoodenSpearModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void onRegisterRecipeBookCategoriesEvent(RegisterRecipeBookCategoriesEvent event) {
        // Kiln:
        event.registerBookCategories(TechPlusPlus.KILN_RECIPE_BOOK_TYPE, TechPlusPlus.KILN_CATEGORIES);
        event.registerAggregateCategory(TechPlusPlus.KILN_SEARCH, ImmutableList.of(TechPlusPlus.KILN_SEARCH, TechPlusPlus.KILN_FOOD, TechPlusPlus.KILN_MISC));
        event.registerRecipeCategoryFinder(ModRecipes.KILN_TYPE.get(), recipe -> {
            AbstractCookingRecipe abstractCookingRecipe = (AbstractCookingRecipe) recipe;
            CookingBookCategory cookingBookCategory = abstractCookingRecipe.category();
            return cookingBookCategory == CookingBookCategory.MISC ? TechPlusPlus.KILN_MISC : TechPlusPlus.KILN_FOOD;
        });
        // Crusher:
        event.registerBookCategories(TechPlusPlus.CRUSHER_RECIPE_BOOK_TYPE, TechPlusPlus.CRUSHER_CATEGORIES);
        event.registerAggregateCategory(TechPlusPlus.CRUSHER_SEARCH, ImmutableList.of(TechPlusPlus.CRUSHER_SEARCH, TechPlusPlus.CRUSHER_MISC));
        event.registerRecipeCategoryFinder(ModRecipes.CRUSHING_TYPE.get(), recipe -> TechPlusPlus.CRUSHER_MISC);
        // Smeltery:
        event.registerBookCategories(TechPlusPlus.SMELTERY_RECIPE_BOOK_TYPE, TechPlusPlus.SMELTERY_CATEGORIES);
        event.registerAggregateCategory(TechPlusPlus.SMELTERY_SEARCH, ImmutableList.of(TechPlusPlus.SMELTERY_SEARCH, TechPlusPlus.SMELTERY_MISC));
        event.registerRecipeCategoryFinder(ModRecipes.SMELTERY_TYPE.get(), recipe -> TechPlusPlus.SMELTERY_MISC);
    }

}
