package net.deddybones.techplusplus.block;

import net.deddybones.techplusplus.block.custom.*;
import net.deddybones.techplusplus.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import static net.deddybones.techplusplus.TechPlusPlus.MOD_ID;

@SuppressWarnings("SameParameterValue")
public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);

    public static final RegistryObject<Block> BAUXITE = registerBlock("bauxite",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GRANITE)));
    public static final RegistryObject<Block> POLISHED_BAUXITE = registerBlock("polished_bauxite",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_GRANITE)));

    public static final RegistryObject<Block> SAPPHIRE_BLOCK = registerBlock("sapphire_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_BLOCK)));

    public static final RegistryObject<Block> PLASTIMETAL_BLOCK = registerBlock("plastimetal_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));
    public static final RegistryObject<Block> RAW_PLASTIMETAL_BLOCK = registerBlock("raw_plastimetal_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.RAW_IRON_BLOCK)));

    public static final RegistryObject<Block> CRUSHER = registerBlock("crusher",
            () -> new CrusherBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)
                    .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.5F)));
    public static final RegistryObject<Block> KILN = registerBlock("kiln",
            () -> new KilnBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)
                    .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.5F)
                    .lightLevel(litBlockEmission(13))));
    public static final RegistryObject<Block> CLAY_MOLDER = registerBlock("clay_molder",
            () -> new ClayMolderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)
                    .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.5F)));
    public static final RegistryObject<Block> SMELTERY = registerBlock("smeltery",
            () -> new SmelteryBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)
                    .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.5F)
                    .lightLevel(smeltingBlockEmission(13))));

    public static final RegistryObject<Block> TIN_BLOCK = registerBlock("tin_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));
    public static final RegistryObject<Block> RAW_TIN_BLOCK = registerBlock("raw_tin_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.RAW_IRON_BLOCK)));

    public static final RegistryObject<Block> BRONZE_BLOCK = registerBlock("bronze_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));
    public static final RegistryObject<Block> RAW_BRONZE_BLOCK = registerBlock("raw_bronze_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.RAW_IRON_BLOCK)));

    public static final RegistryObject<Block> RAW_NETHERITE_BLOCK = registerBlock("raw_netherite_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.RAW_IRON_BLOCK)));

    public static final RegistryObject<Block> TIN_ORE = registerBlock("tin_ore",
            () -> new DropExperienceBlock(
                    ConstantInt.of(0),
                    BlockBehaviour.Properties.of().mapColor(MapColor.STONE)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(3.0F, 3.0F)));
    public static final RegistryObject<Block> DEEPSLATE_TIN_ORE = registerBlock("deepslate_tin_ore",
            () -> new DropExperienceBlock(
                    ConstantInt.of(0),
                    BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(4.5F, 3.0F)));

    public static final RegistryObject<Block> SAPPHIRE_ORE = registerBlock("sapphire_ore",
            () -> new DropExperienceBlock(
                    UniformInt.of(3, 7),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_ORE)));
    public static final RegistryObject<Block> DEEPSLATE_SAPPHIRE_ORE = registerBlock("deepslate_sapphire_ore",
            () -> new DropExperienceBlock(
                    UniformInt.of(3, 7),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_DIAMOND_ORE)));

    public static final RegistryObject<Block> RUINED_PLASTIMETAL = registerBlock("ruined_plastimetal",
            () -> new DropExperienceBlock(
                    UniformInt.of(0, 1),
                    BlockBehaviour.Properties.of().mapColor(MapColor.STONE)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(4.0F, 3.0F)));
    public static final RegistryObject<Block> DEEPSLATE_RUINED_PLASTIMETAL = registerBlock("deepslate_ruined_plastimetal",
            () -> new DropExperienceBlock(
                    UniformInt.of(0, 1),
                    BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(5.0F, 3.0F)));

    public static final RegistryObject<Block> TEST_BLOCK = registerBlock("test_block",
//            () -> new TransparentBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noOcclusion().isViewBlocking(ModBlocks::never)));
            () -> new FlowerBlock(MobEffects.SATURATION, 7, BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY)));


    public static final RegistryObject<Block> TINY_ROCK_BLOCK = registerBlock("tiny_rock_block",
            () -> new RockGoodieBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instabreak().pushReaction(PushReaction.DESTROY).sound(SoundType.STONE).noCollission().isViewBlocking(ModBlocks::never).offsetType(BlockBehaviour.OffsetType.XZ)));
    public static final RegistryObject<Block> TINY_LOG_BLOCK = registerBlock("tiny_log_block",
            () -> new LogGoodieBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instabreak().pushReaction(PushReaction.DESTROY).sound(SoundType.STONE).noCollission().isViewBlocking(ModBlocks::never).offsetType(BlockBehaviour.OffsetType.XZ)));

    public static final RegistryObject<Block> GLASS_STAIRS = registerBlock("glass_stairs",
            () -> new TransparentStairBlock(Blocks.GLASS.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)));
    public static final RegistryObject<Block> GLASS_SLAB = registerBlock("glass_slab",
            () -> new TransparentSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noOcclusion().isViewBlocking(ModBlocks::never)));
    public static final RegistryObject<Block> GLASS_BUTTON = registerBlock("glass_button",
            () -> new ButtonBlock(BlockSetType.OAK, 10, BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noOcclusion().isViewBlocking(ModBlocks::never)));
    public static final RegistryObject<Block> GLASS_PRESSURE_PLATE = registerBlock("glass_pressure_plate",
            () -> new PressurePlateBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noOcclusion().isViewBlocking(ModBlocks::never)));
    public static final RegistryObject<Block> GLASS_FENCE = registerBlock("glass_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noOcclusion().isViewBlocking(ModBlocks::never)));
    public static final RegistryObject<Block> GLASS_FENCE_GATE = registerBlock("glass_fence_gate",
            () -> new FenceGateBlock(WoodType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noOcclusion().isViewBlocking(ModBlocks::never), SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_DOOR_CLOSE));

    public static final RegistryObject<Block> GLASS_WALL = registerBlock("glass_wall",
            () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noOcclusion().isViewBlocking(ModBlocks::never)));
    public static final RegistryObject<Block> GLASS_DOOR = registerBlock("glass_door",
            () -> new DoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noOcclusion().isViewBlocking(ModBlocks::never)));
    public static final RegistryObject<Block> GLASS_TRAPDOOR = registerBlock("glass_trapdoor",
            () -> new TrapDoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noOcclusion().isViewBlocking(ModBlocks::never)));

    public static final RegistryObject<Block> PLASTIMETAL_DOOR = registerBlock("plastimetal_door",
            () -> new DoorBlock(BlockSetType.IRON, BlockBehaviour.Properties.ofFullCopy(ModBlocks.PLASTIMETAL_BLOCK.get()).noOcclusion()));
    public static final RegistryObject<Block> PLASTIMETAL_TRAPDOOR = registerBlock("plastimetal_trapdoor",
            () -> new TrapDoorBlock(BlockSetType.IRON, BlockBehaviour.Properties.ofFullCopy(ModBlocks.PLASTIMETAL_BLOCK.get()).noOcclusion()));
    public static final RegistryObject<Block> PLASTIMETAL_BARS = registerBlock("plastimetal_bars",
            () -> new IronBarsBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F).sound(SoundType.METAL).noOcclusion()));

    public static final RegistryObject<Block> GOLD_DOOR = registerBlock("gold_door",
            () -> new DoorBlock(BlockSetType.GOLD, BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK).noOcclusion()));
    public static final RegistryObject<Block> GOLD_TRAPDOOR = registerBlock("gold_trapdoor",
            () -> new TrapDoorBlock(BlockSetType.GOLD, BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK).noOcclusion()));
    public static final RegistryObject<Block> GOLD_BARS = registerBlock("gold_bars",
            () -> new IronBarsBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F).sound(SoundType.METAL).noOcclusion()));

    public static final RegistryObject<Block> NETHERITE_DOOR = registerBlock("netherite_door",
            () -> new DoorBlock(BlockSetType.IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERITE_BLOCK).noOcclusion()));
    public static final RegistryObject<Block> NETHERITE_TRAPDOOR = registerBlock("netherite_trapdoor",
            () -> new TrapDoorBlock(BlockSetType.IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERITE_BLOCK).noOcclusion()));
    public static final RegistryObject<Block> NETHERITE_BARS = registerBlock("netherite_bars",
            () -> new IronBarsBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F).sound(SoundType.METAL).noOcclusion()));

    public static final RegistryObject<Block> COPPER_BARS = registerBlock("copper_bars",
            () -> new IronBarsBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F).sound(SoundType.METAL).noOcclusion()));

    public static final RegistryObject<Block> TIN_DOOR = registerBlock("tin_door",
            () -> new DoorBlock(BlockSetType.IRON, BlockBehaviour.Properties.ofFullCopy(ModBlocks.TIN_BLOCK.get()).noOcclusion()));
    public static final RegistryObject<Block> TIN_TRAPDOOR = registerBlock("tin_trapdoor",
            () -> new TrapDoorBlock(BlockSetType.IRON, BlockBehaviour.Properties.ofFullCopy(ModBlocks.TIN_BLOCK.get()).noOcclusion()));
    public static final RegistryObject<Block> TIN_BARS = registerBlock("tin_bars",
            () -> new IronBarsBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F).sound(SoundType.METAL).noOcclusion()));

    public static final RegistryObject<Block> BRONZE_DOOR = registerBlock("bronze_door",
            () -> new DoorBlock(BlockSetType.IRON, BlockBehaviour.Properties.ofFullCopy(ModBlocks.BRONZE_BLOCK.get()).noOcclusion()));
    public static final RegistryObject<Block> BRONZE_TRAPDOOR = registerBlock("bronze_trapdoor",
            () -> new TrapDoorBlock(BlockSetType.IRON, BlockBehaviour.Properties.ofFullCopy(ModBlocks.BRONZE_BLOCK.get()).noOcclusion()));
    public static final RegistryObject<Block> BRONZE_BARS = registerBlock("bronze_bars",
            () -> new IronBarsBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F).sound(SoundType.METAL).noOcclusion()));

    public static final RegistryObject<Block> COFFEE_CROP = BLOCKS.register("coffee_crop",
            () -> new CoffeePlantBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SWEET_BERRY_BUSH).noCollission()));
    public static final RegistryObject<Block> FIBROSIA_CROP = BLOCKS.register("fibrosia_crop",
            () -> new FibrosiaCropBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT).noCollission()));

    public static boolean never(BlockState p_50806_, BlockGetter p_50807_, BlockPos p_50808_) {
        return false;
    }

    private static ToIntFunction<BlockState> litBlockEmission(int lightLevel) {
        return (state) -> state.getValue(BlockStateProperties.LIT) ? lightLevel : 0;
    }

    private static ToIntFunction<BlockState> smeltingBlockEmission(int lightLevel) {
        return (state) -> state.getValue(SmelteryBlock.SMELTING) ? lightLevel : 0;
    }

    public static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = registerBlockOnly(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    public static <T extends Block>RegistryObject<T> registerBlockOnly(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    // register an item associated with a block
    @SuppressWarnings("UnusedReturnValue")
    public static <T extends Block>RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
