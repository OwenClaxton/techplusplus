package net.deddybones.techplusplus.gui.menu;

import com.google.common.collect.Lists;

import java.util.List;

import net.deddybones.techplusplus.recipes.ModSingleItemRecipe;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ModAbstractOneInputOneOutputContainerMenu<T extends ModSingleItemRecipe, S extends ModAbstractOneInputOneOutputContainerMenu<T, S>> extends AbstractContainerMenu {
    public static final int INPUT_SLOT = 0;
    public static final int RESULT_SLOT = 1;
    private static final int INV_SLOT_START = 2;
    private static final int INV_SLOT_END = 29;
    private static final int USE_ROW_SLOT_START = INV_SLOT_END;
    private static final int USE_ROW_SLOT_END = 38;
    public final int INPUT_SLOT_PX;
    public static final int INPUT_SLOT_PY = 33;
    public final int RESULT_SLOT_PX;
    public static final int RESULT_SLOT_PY = 33;
    private final ContainerLevelAccess access;
    private final Level level;
    private List<RecipeHolder<T>> recipes = Lists.newArrayList();
    private ItemStack input = ItemStack.EMPTY;
    long lastSoundTime;
    final Slot inputSlot;
    final Slot resultSlot;
    Runnable slotUpdateListener = () -> {};
    public final Container inputContainer = new SimpleContainer(1) {
        public void setChanged() {
            super.setChanged();
            ModAbstractOneInputOneOutputContainerMenu.this.slotsChanged(this);
            ModAbstractOneInputOneOutputContainerMenu.this.slotUpdateListener.run();
        }
    };
    final ResultContainer resultContainer = new ResultContainer();
    private final Block linkedBlock;
    private final RecipeType<T> linkedRecipeType;
    private final MenuType<S> linkedMenuType;
    private final boolean pickable;
    private final DataSlot selectedRecipeIndex = DataSlot.standalone();

    @SuppressWarnings("unused")
    public ModAbstractOneInputOneOutputContainerMenu(Block linkedBlock, RecipeType<T> linkedRecipeType,
                                                     MenuType<S> linkedMenuType, int pContainerId,
                                                     Inventory pInventory, boolean pPickable) {
        this(linkedBlock, linkedRecipeType, linkedMenuType, pContainerId, pInventory, ContainerLevelAccess.NULL, pPickable);
    }

    public SoundEvent getCollectSound() {
        return SoundEvents.UI_STONECUTTER_TAKE_RESULT;
    }

    public ModAbstractOneInputOneOutputContainerMenu(Block linkedBlock, RecipeType<T> linkedRecipeType,
                                                     MenuType<S> linkedMenuType, int pContainerId,
                                                     Inventory pInventory, final ContainerLevelAccess pAccess,
                                                     boolean pPickable) {
        super(linkedMenuType, pContainerId);
        this.pickable = pPickable;
        if (!this.pickable) {
            this.INPUT_SLOT_PX = 54;
            this.RESULT_SLOT_PX = 106;
        } else {
            this.INPUT_SLOT_PX = 20;
            this.RESULT_SLOT_PX = 143;
        }
        this.linkedBlock = linkedBlock;
        this.linkedRecipeType = linkedRecipeType;
        this.linkedMenuType = linkedMenuType;

        this.access = pAccess;
        this.level = pInventory.player.level();
        this.inputSlot = this.addSlot(new Slot(this.inputContainer, INPUT_SLOT, INPUT_SLOT_PX, INPUT_SLOT_PY));
        this.resultSlot = this.addSlot(new Slot(this.resultContainer, RESULT_SLOT, RESULT_SLOT_PX, RESULT_SLOT_PY) {
            public boolean mayPlace(@NotNull ItemStack pStack) {
                return false;
            }

            public void onTake(@NotNull Player pPlayer, @NotNull ItemStack pStack) {
                pStack.onCraftedBy(pPlayer.level(), pPlayer, pStack.getCount());
                ModAbstractOneInputOneOutputContainerMenu.this.resultContainer.awardUsedRecipes(pPlayer, this.getRelevantItems());
                ItemStack itemstack = ModAbstractOneInputOneOutputContainerMenu.this.inputSlot.remove(RESULT_SLOT);
                if (!itemstack.isEmpty()) {
                    ModAbstractOneInputOneOutputContainerMenu.this.setupResultSlot();
                }
                pAccess.execute((pLevel, pPos) -> {
                    long l = pLevel.getGameTime();
                    if (ModAbstractOneInputOneOutputContainerMenu.this.lastSoundTime != l) {
                        pLevel.playSound(null, pPos, getCollectSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
                        ModAbstractOneInputOneOutputContainerMenu.this.lastSoundTime = l;
                    }

                });
                super.onTake(pPlayer, pStack);
            }

            private List<ItemStack> getRelevantItems() {
                return List.of(ModAbstractOneInputOneOutputContainerMenu.this.inputSlot.getItem());
            }
        });

        addPlayerInventory(pInventory);
        addPlayerHotbar(pInventory);

        this.addDataSlot(this.selectedRecipeIndex);
    }

    public int getSelectedRecipeIndex() {
        if (this.pickable) return this.selectedRecipeIndex.get();
        return 0;
    }

    public List<RecipeHolder<T>> getRecipes() {
        return this.recipes;
    }

    public int getNumRecipes() {
        return this.recipes.size();
    }

    public boolean hasInputItem() {
        return this.inputSlot.hasItem() && !this.recipes.isEmpty();
    }

    public boolean stillValid(@NotNull Player pPlayer) {
        return stillValid(this.access, pPlayer, this.linkedBlock);
    }

    public boolean clickMenuButton(@Nullable Player pPlayer, int pSlotIndex) {
        if (this.pickable && this.isValidRecipeIndex(pSlotIndex)) {
            this.selectedRecipeIndex.set(pSlotIndex);
            this.setupResultSlot();
        }
        return true;
    }

    private boolean isValidRecipeIndex(int pSlotIndex) {
        if (this.pickable) return pSlotIndex >= 0 && pSlotIndex < this.recipes.size();
        return true;
    }

    public void slotsChanged(@NotNull Container pContainer) {
        ItemStack itemstack = this.inputSlot.getItem();
        if (!itemstack.is(this.input.getItem())) {
            this.input = itemstack.copy();
            this.setupRecipeList(pContainer, itemstack);
//            this.setupResultSlot();
        }

    }

    private void setupRecipeList(Container pContainer, ItemStack pStack) {
        this.recipes.clear();
        if (this.pickable) this.selectedRecipeIndex.set(-1);
        this.resultSlot.set(ItemStack.EMPTY);
        if (!pStack.isEmpty()) {
            this.recipes = this.level.getRecipeManager().getRecipesFor(this.linkedRecipeType,
                    new SingleRecipeInput(pContainer.getItem(INPUT_SLOT)), this.level);
        }
    }

    void setupResultSlot() {
        if (!this.recipes.isEmpty() && this.isValidRecipeIndex(this.selectedRecipeIndex.get())) {
            RecipeHolder<T> recipeHolder = this.recipes.get(this.selectedRecipeIndex.get());
            ItemStack itemstack = recipeHolder.value().assemble(
                    new SingleRecipeInput(this.inputContainer.getItem(INPUT_SLOT)), this.level.registryAccess());
            if (itemstack.isItemEnabled(this.level.enabledFeatures())) {
                this.resultContainer.setRecipeUsed(recipeHolder);
                this.resultSlot.set(itemstack);
            } else {
                this.resultSlot.set(ItemStack.EMPTY);
            }
        } else {
            this.resultSlot.set(ItemStack.EMPTY);
        }

        this.broadcastChanges();
    }

    public @NotNull MenuType<S> getType() {
        return this.linkedMenuType;
    }

    public void registerUpdateListener(Runnable pListener) {
        this.slotUpdateListener = pListener;
    }

    public boolean canTakeItemForPickAll(@NotNull ItemStack pStack, Slot pSlot) {
        return pSlot.container != this.resultContainer && super.canTakeItemForPickAll(pStack, pSlot);
    }

    public @NotNull ItemStack quickMoveStack(@NotNull Player pPlayer, int pSlotNum) {
        Slot slot = this.slots.get(pSlotNum);
        if (!slot.hasItem()) return ItemStack.EMPTY;
        ItemStack slotStack = slot.getItem();
        ItemStack slotStackOut = slotStack.copy();

        if (pSlotNum == RESULT_SLOT) {
            slotStack.getItem().onCraftedBy(slotStack, pPlayer.level(), pPlayer);
            if (!this.moveItemStackTo(slotStack, INV_SLOT_START, USE_ROW_SLOT_END, true)) {
                return ItemStack.EMPTY;
            }
            slot.onQuickCraft(slotStack, slotStackOut);

        } else if (pSlotNum == INPUT_SLOT) {
            if (!this.moveItemStackTo(slotStack, INV_SLOT_START, USE_ROW_SLOT_END, false)) {
                return ItemStack.EMPTY;
            }

        } else if (this.level.getRecipeManager().getRecipeFor(this.linkedRecipeType,
                new SingleRecipeInput(slotStack), this.level).isPresent()) {
            if (!this.moveItemStackTo(slotStack, INPUT_SLOT, RESULT_SLOT, false)) {
                return ItemStack.EMPTY;
            }
        } else if (pSlotNum >= INV_SLOT_START && pSlotNum < USE_ROW_SLOT_START) {
            if (!this.moveItemStackTo(slotStack, USE_ROW_SLOT_START, USE_ROW_SLOT_END, false)) {
                return ItemStack.EMPTY;
            }
        } else if (pSlotNum >= USE_ROW_SLOT_START && pSlotNum < USE_ROW_SLOT_END) {
            if (!this.moveItemStackTo(slotStack, INV_SLOT_START, USE_ROW_SLOT_START, false)) {
                return ItemStack.EMPTY;
            }
        }

        if (slotStack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        }

        slot.setChanged();
        if (slotStack.getCount() == slotStackOut.getCount()) {
            return ItemStack.EMPTY;
        }

        slot.onTake(pPlayer, slotStack);
        this.broadcastChanges();

        return slotStackOut;
    }

    public void removed(@NotNull Player pPlayer) {
        super.removed(pPlayer);
        this.resultContainer.removeItemNoUpdate(1);
        this.access.execute((pLevel, pPos) -> this.clearContainer(pPlayer, this.inputContainer));
    }

    private void addPlayerInventory(Inventory pInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(pInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory pInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(pInventory, i, 8 + i * 18, 142));
        }
    }
}