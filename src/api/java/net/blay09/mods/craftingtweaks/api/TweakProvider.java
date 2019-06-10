package net.blay09.mods.craftingtweaks.api;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent;

import javax.annotation.Nullable;

/**
 * An interface for tweak provider implementations. Needs to be registered using CraftingTweaksAPI.registerProvider().
 * Can make use of CraftingTweaksAPI.createDefaultProvider() for standard crafting grids.
 */
public interface TweakProvider<T extends Container> {

    /**
     * @return the mod id the provider is for, used to check availability and configuration
     */
    String getModId();

    /**
     * This is called upon registering the provider if the mod id returned in getModId() is loaded
     *
     * @return true if this provider was successfully loaded
     */
    boolean load();

    /**
     * Defaults to true.
     *
     * @return true if this provider is unable to run on client-only instances (i.e. uses phantom items in its grid)
     */
    default boolean requiresServerSide() {
        return true;
    }

    /**
     * Defaults to 1.
     *
     * @param entityPlayer the player who's looking at the grid
     * @param container    the container the grid is part of
     * @param id           the crafting grid ID this is checked for (usually 0 unless there's more grids in one GUI)
     * @return the slot number within the container's inventorySlots list that marks the beginning of the grid (for client-only instances)
     */
    default int getCraftingGridStart(PlayerEntity entityPlayer, T container, int id) {
        return 1;
    }

    /**
     * Defaults to 9.
     *
     * @param entityPlayer the player who's looking at the grid
     * @param container    the container the grid is part of
     * @param id           the crafting grid ID this is checked for (usually 0 unless there's more grids in one GUI)
     * @return the size of this crafting grid within the container's inventorySlots list (for client-only instances)
     */
    default int getCraftingGridSize(PlayerEntity entityPlayer, T container, int id) {
        return 9;
    }

    /**
     * Clears the grid, transferring items from it into the player inventory.
     *
     * @param entityPlayer the player who's clearing the grid
     * @param container    the container the grid is part of
     * @param forced       if true, drop items to the ground if necessary
     * @param id           the crafting grid ID that is being cleared (usually 0 unless there's more grids in one GUI)
     */
    void clearGrid(PlayerEntity entityPlayer, T container, int id, boolean forced);

    /**
     * Rotates the grid clockwise.
     *
     * @param entityPlayer     the player who's rotating the grid
     * @param container        the container the grid is part of
     * @param id               the crafting grid ID that is being rotated (usually 0 unless there's more grids in one GUI)
     * @param counterClockwise true if the rotation should happen counter clockwise
     */
    void rotateGrid(PlayerEntity entityPlayer, T container, int id, boolean counterClockwise);

    /**
     * Balances the grid.
     *
     * @param entityPlayer the player who's balancing the grid
     * @param container    the container the grid is part of
     * @param id           the crafting grid ID that is being balanced (usually 0 unless there's more grids in one GUI)
     */
    void balanceGrid(PlayerEntity entityPlayer, T container, int id);

    /**
     * Spreads the items in the grid out.
     *
     * @param entityPlayer the player who's spreading the grid
     * @param container    the container the grid is part of
     * @param id           the crafting grid ID that is being spread (usually 0 unless there's more grids in one GUI)
     */
    void spreadGrid(PlayerEntity entityPlayer, T container, int id);

    /**
     * Checks if the transfer-to-grid feature can be used from the sourceSlot.
     *
     * @param entityPlayer the player who's attempting to transfer items
     * @param container    the container the grid is part of
     * @param id           the crafting grid ID that is being transferred into (usually 0 unless there's more grids in one GUI)
     * @param sourceSlot   the slot items are being transferred from
     * @return true if transfer-to-grid is allowed from the sourceSlot, false otherwise
     */
    boolean canTransferFrom(PlayerEntity entityPlayer, T container, int id, Slot sourceSlot);

    /**
     * Transfers items from sourceSlot into the grid (similar to shift-clicking items into a chest).
     *
     * @param entityPlayer the player who's transferring items
     * @param container    the container the grid is part of
     * @param id           the crafting grid ID that is being transferred into (usually 0 unless there's more grids in one GUI)
     * @param sourceSlot   the slot items are being transferred from
     * @return true if items were fully transferred, false otherwise
     */
    boolean transferIntoGrid(PlayerEntity entityPlayer, T container, int id, Slot sourceSlot);

    /**
     * Puts an item into the grid.
     *
     * @param entityPlayer the player who's putting an item in
     * @param container    the container the grid is part of
     * @param id           the crafting grid ID that is being put into (usually 0 unless there's more grids in one GUI)
     * @param itemStack    the item stack that should be put into the grid
     * @param index        the slot index within the craft matrix the item should be put into
     * @return a rest stack or null if there is no rest
     */
    ItemStack putIntoGrid(PlayerEntity entityPlayer, T container, int id, ItemStack itemStack, int index);

    /**
     * @param entityPlayer the player that is accessing the container
     * @param container    the container the grid is part of
     * @param id           the crafting grid ID that is being accessed (usually 0 unless there's more grids in one GUI)
     * @return the craft matrix inventory
     */
    @Nullable
    IInventory getCraftMatrix(PlayerEntity entityPlayer, T container, int id);

    /**
     * Called to add buttons to the GUI. May not be called if buttons are disabled in the configuration.
     * Use CraftingTweaksAPI.create***Button() to create tweak buttons, then add them to the buttonList.
     *
     * @param guiContainer the gui container the buttons are being added to
     * @param event        the event triggering this gui initialization
     */
    @OnlyIn(Dist.CLIENT)
    void initGui(ContainerScreen<T> guiContainer, GuiScreenEvent.InitGuiEvent event);

    /**
     * @param container the container to test
     * @return true if the container contains a valid crafting grid
     */
    default boolean isValidContainer(Container container) {
        return true;
    }
}
