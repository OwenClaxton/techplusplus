package net.deddybones.techplusplus.recipes;

import net.deddybones.techplusplus.block.entity.SmelteryBlockEntity;
import net.minecraft.world.item.crafting.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static net.deddybones.techplusplus.TechPlusPlus.MOD_ID;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MOD_ID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, MOD_ID);

    public static final RegistryObject<RecipeSerializer<EmptyRecipe>> EMPTY_SERIALIZER
            = RECIPE_SERIALIZERS.register("empty",
            () -> new EmptyRecipe.Serializer<>(EmptyRecipe::new));
    public static final RegistryObject<RecipeSerializer<CrusherRecipe>> CRUSHING_SERIALIZER
            = RECIPE_SERIALIZERS.register("crushing",
            () -> new ModSingleItemRecipe.Serializer<>(CrusherRecipe::new));
    public static final RegistryObject<RecipeSerializer<KilnRecipe>> KILN_SERIALIZER
            = RECIPE_SERIALIZERS.register("kiln",
            () -> new SimpleCookingSerializer<>(KilnRecipe::new, 400));
    public static final RegistryObject<RecipeSerializer<ClayMolderRecipe>> MOLD_SERIALIZER
            = RECIPE_SERIALIZERS.register("mold",
            () -> new ModSingleItemRecipe.Serializer<>(ClayMolderRecipe::new));
    public static final RegistryObject<SmelteryRecipe.SmelterySerializer<SmelteryRecipe>> SMELTERY_SERIALIZER
            = RECIPE_SERIALIZERS.register("smeltery",
            () -> new SmelteryRecipe.SmelterySerializer<>(SmelteryRecipe::new, SmelteryBlockEntity.SMELT_TIME_DEFAULT));
    public static final RegistryObject<RecipeSerializer<ModFireworkStarRecipe>> FIREWORK_STAR_SERIALIZER
            = RECIPE_SERIALIZERS.register("crafting_special_firework_star", () -> new SimpleCraftingRecipeSerializer<>(ModFireworkStarRecipe::new));

    public static final RegistryObject<RecipeType<EmptyRecipe>> EMPTY_TYPE          = registerType("empty");
    public static final RegistryObject<RecipeType<CrusherRecipe>> CRUSHING_TYPE     = registerType("crushing");
    public static final RegistryObject<RecipeType<KilnRecipe>> KILN_TYPE            = registerType("kiln");
    public static final RegistryObject<RecipeType<ClayMolderRecipe>> MOLD_TYPE      = registerType("mold");
    public static final RegistryObject<RecipeType<SmelteryRecipe>> SMELTERY_TYPE    = registerType("smeltery");

    static <T extends Recipe<?>> RegistryObject<RecipeType<T>> registerType(final String pId) {
        return RECIPE_TYPES.register(pId, () -> new RecipeType<>() {
            public String toString() {
                return pId;
            }
        });
    }

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
        RECIPE_TYPES.register(eventBus);
    }
}
