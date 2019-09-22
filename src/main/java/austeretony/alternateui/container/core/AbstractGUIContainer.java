package austeretony.alternateui.container.core;

import java.io.IOException;
import java.util.Set;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

import austeretony.alternateui.container.framework.GUISlotsFramework;
import austeretony.alternateui.screen.browsing.GUIScroller.EnumScrollerType;
import austeretony.alternateui.screen.core.AbstractGUIScreen;
import austeretony.alternateui.screen.core.GUISimpleElement;
import austeretony.alternateui.util.EnumGUISlotsPosition;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;

public abstract class AbstractGUIContainer extends AbstractGUIScreen {

    public Container container;

    protected final Set<Slot> dragSplittingSlots = Sets.<Slot>newHashSet();

    private Slot hoveredSlot, clickedSlot, returningStackDestSlot, currentDragTargetSlot, lastClickSlot;

    private boolean isRightMouseClick, doubleClick, ignoreMouseUp;

    protected boolean dragSplitting;

    private ItemStack 
    draggedStack = ItemStack.EMPTY,
    returningStack = ItemStack.EMPTY,
    shiftClickedSlot = ItemStack.EMPTY;

    private int touchUpX, touchUpY, dragSplittingLimit, dragSplittingButton, dragSplittingRemnant, lastClickButton, yPrevMouse;

    private long returningStackTime, dragItemDropDelay, lastClickTime;

    public AbstractGUIContainer(Container container) {
        this.container = container;
        this.ignoreMouseUp = true;
    }

    public void initGui() {
        super.initGui();
        this.mc.player.openContainer = this.container;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        this.hoveredSlot = null;        
        super.drawScreen(mouseX, mouseY, partialTicks);
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) this.guiLeft, (float) this.guiTop, 0.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        this.drawGuiContainerForegroundLayer(mouseX, mouseY);
        InventoryPlayer inventoryplayer = this.mc.player.inventory;
        ItemStack itemstack = this.draggedStack.isEmpty() ? inventoryplayer.getItemStack() : this.draggedStack;

        RenderHelper.enableGUIStandardItemLighting();            
        GlStateManager.enableDepth();

        if (!itemstack.isEmpty()) {
            int j2 = 8;
            int k2 = this.draggedStack.isEmpty() ? 8 : 16;
            String s = null;

            if (!this.draggedStack.isEmpty() && this.isRightMouseClick) {
                itemstack = itemstack.copy();
                itemstack.setCount(MathHelper.ceil((float) itemstack.getCount() / 2.0F));
            } else if (this.dragSplitting && this.dragSplittingSlots.size() > 1) {
                itemstack = itemstack.copy();
                itemstack.setCount(this.dragSplittingRemnant);

                if (itemstack.isEmpty())
                    s = "" + TextFormatting.YELLOW + "0";
            }
            this.drawItemStack(itemstack, mouseX - this.guiLeft - 8, mouseY - this.guiTop - k2, s);
        }

        if (!this.returningStack.isEmpty()) {
            float f = (float) (Minecraft.getSystemTime() - this.returningStackTime) / 100.0F;
            if (f >= 1.0F) {
                f = 1.0F;
                this.returningStack = ItemStack.EMPTY;
            }
            int 
            l2 = this.returningStackDestSlot.xPos - this.touchUpX,
            i3 = this.returningStackDestSlot.yPos - this.touchUpY,
            l1 = this.touchUpX + (int) ((float) l2 * f),
            i2 = this.touchUpY + (int) ((float) i3 * f);
            this.drawItemStack(this.returningStack, l1, i2, (String) null);
        }

        GlStateManager.disableDepth();
        RenderHelper.disableStandardItemLighting();

        GlStateManager.popMatrix();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();

        this.renderHoveredToolTip(mouseX, mouseY);

        this.yPrevMouse = mouseY;
    }

    protected void drawSlots(int mouseX, int mouseY) {
        int i;
        Slot slot;
        for (GUISlotsFramework framework : this.getWorkspace().getCurrentSection().getSlotsFrameworks()) {
            if (framework.isVisible()) {
                for (i = 0; i < framework.visibleSlots; i++) {          
                    if (i < framework.slots.visibleSlots.size()) {
                        slot = framework.slots.visibleSlots.get(i);
                        if (framework.isSlotBottomLayerEnabled()) {
                            RenderHelper.disableStandardItemLighting();
                            this.drawSlotBottomLayer(slot, framework); 
                            RenderHelper.enableStandardItemLighting();                                              
                        }
                        RenderHelper.enableGUIStandardItemLighting();
                        this.drawSlot(slot, framework);
                        if (!this.getWorkspace().getCurrentSection().hasCurrentCallback()) {
                            if (this.isMouseOverSlot(slot, mouseX, mouseY) 
                                    && slot.isEnabled())
                                this.drawSlotHighlighting(slot, framework);
                        }
                    }
                }
            }
        }
    }

    protected void drawSlotBottomLayer(Slot slot, GUISlotsFramework framework) {
        drawRect(slot.xPos, slot.yPos, slot.xPos + framework.getSlotWidth(), slot.yPos + framework.getSlotHeight(), framework.getSlotBottomLayerColor());
    }

    protected void drawSlotHighlighting(Slot slot, GUISlotsFramework framework) {
        this.hoveredSlot = slot;
        int j, k;
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        j = slot.xPos;
        k = slot.yPos;
        GlStateManager.colorMask(true, true, true, false);
        GUISimpleElement.drawRect(j, k, j + 16, k + 16, framework.getSlotHighlightingColor());
        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
    }

    /**
     * Draws the given slot: any item in it, the slot's background, the hovered highlight, etc.
     */
    private void drawSlot(Slot slot, GUISlotsFramework framework) {
        int 
        i = slot.xPos,
        j = slot.yPos;
        ItemStack itemstack = slot.getStack();
        boolean 
        flag = false,
        flag1 = slot == this.clickedSlot && !this.draggedStack.isEmpty() && !this.isRightMouseClick;
        ItemStack itemstack1 = this.mc.player.inventory.getItemStack();
        String s = null;
        if (slot == this.clickedSlot && !this.draggedStack.isEmpty() && this.isRightMouseClick && !itemstack.isEmpty()) {
            itemstack = itemstack.copy();
            itemstack.setCount(itemstack.getCount() / 2);
        } else if (this.dragSplitting && this.dragSplittingSlots.contains(slot) && !itemstack1.isEmpty()) {
            if (this.dragSplittingSlots.size() == 1)
                return;
            if (Container.canAddItemToSlot(slot, itemstack1, true) && this.container.canDragIntoSlot(slot)) {
                itemstack = itemstack1.copy();
                flag = true;
                Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, itemstack, slot.getStack().isEmpty() ? 0 : slot.getStack().getCount());
                int k = Math.min(itemstack.getMaxStackSize(), slot.getItemStackLimit(itemstack));
                if (itemstack.getCount() > k) {
                    s = TextFormatting.YELLOW.toString() + k;
                    itemstack.setCount(k);
                }
            } else {
                this.dragSplittingSlots.remove(slot);
                this.updateDragSplitting();
            }
        }
        this.zLevel = 100.0F;
        this.itemRender.zLevel = 100.0F;
        if (itemstack.isEmpty() && slot.isEnabled()) {
            TextureAtlasSprite textureatlassprite = slot.getBackgroundSprite();
            if (textureatlassprite != null) {
                GlStateManager.disableLighting();
                this.mc.getTextureManager().bindTexture(slot.getBackgroundLocation());
                this.drawTexturedModalRect(i, j, textureatlassprite, 16, 16);
                GlStateManager.enableLighting();
                flag1 = true;
            }
        }
        if (!flag1) {
            if (flag)
                drawRect(i, j, i + 16, j + 16, -2130706433);
            GlStateManager.enableDepth();
            this.itemRender.renderItemAndEffectIntoGUI(this.mc.player, itemstack, i, j);
            this.itemRender.renderItemOverlayIntoGUI(this.fontRenderer, itemstack, i, j, s);
        }
        this.itemRender.zLevel = 0.0F;
        this.zLevel = 0.0F;
    }

    public void handleFrameworkSlidebar(GUISlotsFramework framework, int mouseY) {
        int slidebarOffset;
        float sliderActiveHeight, currentPosition;
        if (framework.slots.getScroller().getSlider().isDragged()) {                            
            slidebarOffset = mouseY - framework.slots.getScroller().getSlider().getSlidebarY();
            sliderActiveHeight = framework.slots.getScroller().getSlider().getHeight() - framework.slots.getScroller().getSlider().getSlidebarHeight();
            currentPosition = mouseY - slidebarOffset - this.yPrevMouse + mouseY + this.guiTop;
            framework.slots.getScroller().setPosition((int) ((float) framework.slots.getScroller().getMaxPosition() * ((currentPosition - framework.slots.getScroller().getSlider().getY()) / sliderActiveHeight)));                                
            framework.slots.getScroller().getSlider().handleSlidebarViaCursor((int) (sliderActiveHeight * (currentPosition / sliderActiveHeight)));
            this.scrollSlots(framework);                                    
        }
    }

    //TODO scrollSlots()
    protected void scrollSlots(GUISlotsFramework framework) {
        int i = 0, size, k;
        boolean scrollingSearch = !framework.slots.searchSlots.isEmpty();
        Slot slot, slotCopy;
        framework.slots.visibleSlots.clear();
        framework.slots.visibleSlotsIndexes.clear();
        if (framework.slots.getScroller().scrollerType == EnumScrollerType.STANDARD) {
            for (i = framework.slots.getScroller().getPosition() * framework.columns; i < framework.slots.getScroller().getPosition() * framework.columns + framework.visibleSlots; i++) {
                if ((!scrollingSearch && i < framework.slots.slotsBuffer.size()) || (scrollingSearch && i < framework.slots.searchSlots.size())) {
                    slot = scrollingSearch ? framework.slots.searchSlots.get(i) : framework.slots.slotsBuffer.get(i);  
                    slotCopy = this.copySlot(slot); 
                    size = framework.slots.visibleSlots.size();
                    if (framework.slotsPosition == EnumGUISlotsPosition.CUSTOM) {
                        k = size / framework.columns;
                        slotCopy.xPos = framework.getX() + size * (framework.getSlotWidth() + framework.getSlotDistanceHorizontal()) - k * ((framework.getSlotWidth() + framework.getSlotDistanceHorizontal()) * framework.columns);
                        slotCopy.yPos = framework.getY() + k * (framework.getSlotHeight() + framework.getSlotDistanceVertical()) - (size / framework.visibleSlots) * (framework.rows * (framework.getSlotHeight() + framework.getSlotDistanceVertical()));
                    }
                    framework.slots.visibleSlots.add(slotCopy);
                    framework.slots.visibleSlotsIndexes.add(scrollingSearch ? framework.slots.searchIndexes.get(i) : framework.slots.indexesBuffer.get(i));
                }
            }
        }
    }

    protected final Slot copySlot(Slot slot) {
        return new Slot(slot.inventory, slot.getSlotIndex(), slot.xPos, slot.yPos);     
    }

    protected void renderHoveredToolTip(int p_191948_1_, int p_191948_2_) {
        if (this.mc.player.inventory.getItemStack().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.getHasStack())
            this.renderToolTip(this.hoveredSlot.getStack(), p_191948_1_, p_191948_2_);
    }

    /**
     * Draws an ItemStack.
     *  
     * The z index is increased by 32 (and not decreased afterwards), and the item is then rendered at z=200.
     */
    private void drawItemStack(ItemStack stack, int x, int y, String altText) {
        GlStateManager.translate(0.0F, 0.0F, 32.0F);
        this.zLevel = 200.0F;
        this.itemRender.zLevel = 200.0F;
        FontRenderer font = stack.getItem().getFontRenderer(stack);
        if (font == null) font = fontRenderer;
        this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
        this.itemRender.renderItemOverlayIntoGUI(font, stack, x, y - (this.draggedStack.isEmpty() ? 0 : 8), altText);
        this.zLevel = 0.0F;
        this.itemRender.zLevel = 0.0F;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {}

    /**
     * Draws the background layer of this container (behind the items).
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {}

    private void updateDragSplitting() {
        ItemStack itemstack = this.mc.player.inventory.getItemStack();
        if (!itemstack.isEmpty() && this.dragSplitting) {
            if (this.dragSplittingLimit == 2) {
                this.dragSplittingRemnant = itemstack.getMaxStackSize();
            } else {
                this.dragSplittingRemnant = itemstack.getCount();
                for (Slot slot : this.dragSplittingSlots) {
                    ItemStack itemstack1 = itemstack.copy();
                    ItemStack itemstack2 = slot.getStack();
                    int i = itemstack2.isEmpty() ? 0 : itemstack2.getCount();
                    Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, itemstack1, i);
                    int j = Math.min(itemstack1.getMaxStackSize(), slot.getItemStackLimit(itemstack1));
                    if (itemstack1.getCount() > j)
                        itemstack1.setCount(j);
                    this.dragSplittingRemnant -= itemstack1.getCount() - i;
                }
            }
        }
    }

    /**
     * Returns the slot at the given coordinates or null if there is none.
     */
    private Slot getSlotAtPosition(int x, int y) {
        int i;
        Slot slot;      
        for (GUISlotsFramework framework : this.getWorkspace().getCurrentSection().getSlotsFrameworks()) {
            if (framework.isEnabled()) {
                for (i = 0; i < framework.visibleSlots; i++) {          
                    if (i < framework.slots.visibleSlots.size()) {
                        slot = framework.slots.visibleSlots.get(i);    
                        if (!this.getWorkspace().getCurrentSection().hasCurrentCallback() 
                                && this.isMouseOverSlot(slot, x, y))
                            return (Slot) this.container.inventorySlots.get(framework.slots.visibleSlotsIndexes.get(i));
                    }
                }
            }
        }
        return null;
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (!this.getWorkspace().getCurrentSection().hasCurrentCallback()) {
            boolean flag = this.mc.gameSettings.keyBindPickBlock.isActiveAndMatches(mouseButton - 100);
            Slot slot = this.getSlotAtPosition(mouseX, mouseY);
            long i = Minecraft.getSystemTime();
            this.doubleClick = this.lastClickSlot == slot && i - this.lastClickTime < 250L && this.lastClickButton == mouseButton;
            this.ignoreMouseUp = false;
            if (mouseButton == 0 || mouseButton == 1 || flag) {
                int j = this.guiLeft;
                int k = this.guiTop;
                boolean flag1 = this.hasClickedOutside(mouseX, mouseY, j, k);
                if (slot != null) flag1 = false; // Forge, prevent dropping of items through slots outside of GUI boundaries
                int l = -1;
                if (slot != null)
                    l = slot.slotNumber;
                if (flag1)
                    l = -999;
                if (this.mc.gameSettings.touchscreen && flag1 && this.mc.player.inventory.getItemStack().isEmpty()) {
                    this.mc.displayGuiScreen((GuiScreen)null);
                    return;
                }
                if (l != -1) {
                    if (this.mc.gameSettings.touchscreen) {
                        if (slot != null && slot.getHasStack()) {
                            this.clickedSlot = slot;
                            this.draggedStack = ItemStack.EMPTY;
                            this.isRightMouseClick = mouseButton == 1;
                        } else
                            this.clickedSlot = null;
                    } else if (!this.dragSplitting) {
                        if (this.mc.player.inventory.getItemStack().isEmpty()) {
                            if (this.mc.gameSettings.keyBindPickBlock.isActiveAndMatches(mouseButton - 100))
                                this.handleMouseClick(slot, l, mouseButton, ClickType.CLONE);
                            else {
                                boolean flag2 = l != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
                                ClickType clicktype = ClickType.PICKUP;

                                if (flag2) {
                                    this.shiftClickedSlot = slot != null && slot.getHasStack() ? slot.getStack().copy() : ItemStack.EMPTY;
                                    clicktype = ClickType.QUICK_MOVE;
                                } else if (l == -999)
                                    clicktype = ClickType.THROW;
                                this.handleMouseClick(slot, l, mouseButton, clicktype);
                            }
                            this.ignoreMouseUp = true;
                        } else {
                            this.dragSplitting = true;
                            this.dragSplittingButton = mouseButton;
                            this.dragSplittingSlots.clear();
                            if (mouseButton == 0)
                                this.dragSplittingLimit = 0;
                            else if (mouseButton == 1)
                                this.dragSplittingLimit = 1;
                            else if (this.mc.gameSettings.keyBindPickBlock.isActiveAndMatches(mouseButton - 100))
                                this.dragSplittingLimit = 2;
                        }
                    }
                }
            }
            this.lastClickSlot = slot;
            this.lastClickTime = i;
            this.lastClickButton = mouseButton;
        }
        for (GUISlotsFramework framework : this.getWorkspace().getCurrentSection().getSlotsFrameworks())
            framework.mouseClicked(mouseX, mouseY, mouseButton);
        this.getWorkspace().getCurrentSection().mouseClicked(mouseX, mouseY, mouseButton);
    }

    protected boolean hasClickedOutside(int p_193983_1_, int p_193983_2_, int p_193983_3_, int p_193983_4_) {
        return p_193983_1_ < p_193983_3_ || p_193983_2_ < p_193983_4_ || p_193983_1_ >= p_193983_3_ + this.xSize || p_193983_2_ >= p_193983_4_ + this.ySize;
    }

    /**
     * Called when a mouse button is pressed and the mouse is moved around. Parameters are : mouseX, mouseY,
     * lastButtonClicked & timeSinceMouseClick.
     */
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        Slot slot = this.getSlotAtPosition(mouseX, mouseY);
        ItemStack itemstack = this.mc.player.inventory.getItemStack();
        if (this.clickedSlot != null && this.mc.gameSettings.touchscreen) {
            if (clickedMouseButton == 0 || clickedMouseButton == 1)  {
                if (this.draggedStack.isEmpty()) {
                    if (slot != this.clickedSlot && !this.clickedSlot.getStack().isEmpty())
                        this.draggedStack = this.clickedSlot.getStack().copy();
                } else if (this.draggedStack.getCount() > 1 && slot != null && Container.canAddItemToSlot(slot, this.draggedStack, false)) {
                    long i = Minecraft.getSystemTime();
                    if (this.currentDragTargetSlot == slot) {
                        if (i - this.dragItemDropDelay > 500L) {
                            this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, 0, ClickType.PICKUP);
                            this.handleMouseClick(slot, slot.slotNumber, 1, ClickType.PICKUP);
                            this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, 0, ClickType.PICKUP);
                            this.dragItemDropDelay = i + 750L;
                            this.draggedStack.shrink(1);
                        }
                    } else {
                        this.currentDragTargetSlot = slot;
                        this.dragItemDropDelay = i;
                    }
                }
            }
        } else if (this.dragSplitting && slot != null && !itemstack.isEmpty() && (itemstack.getCount() > this.dragSplittingSlots.size() || this.dragSplittingLimit == 2) && Container.canAddItemToSlot(slot, itemstack, true) && slot.isItemValid(itemstack) && this.container.canDragIntoSlot(slot)) {
            this.dragSplittingSlots.add(slot);
            this.updateDragSplitting();
        }
    }

    /**
     * Called when a mouse button is released.
     */
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state); //Forge, Call parent to release buttons
        Slot slot = this.getSlotAtPosition(mouseX, mouseY);
        int i = this.guiLeft;
        int j = this.guiTop;
        boolean flag = this.hasClickedOutside(mouseX, mouseY, i, j);
        if (slot != null) flag = false; // Forge, prevent dropping of items through slots outside of GUI boundaries
        int k = -1;
        if (slot != null)
            k = slot.slotNumber;
        if (flag)
            k = -999;
        if (this.doubleClick && slot != null && state == 0 && this.container.canMergeSlot(ItemStack.EMPTY, slot)) {
            if (isShiftKeyDown()) {
                if (!this.shiftClickedSlot.isEmpty()) {
                    for (Slot slot2 : this.container.inventorySlots) {
                        if (slot2 != null && slot2.canTakeStack(this.mc.player) && slot2.getHasStack() && slot2.isSameInventory(slot) && Container.canAddItemToSlot(slot2, this.shiftClickedSlot, true))
                            this.handleMouseClick(slot2, slot2.slotNumber, state, ClickType.QUICK_MOVE);
                    }
                }
            } else
                this.handleMouseClick(slot, k, state, ClickType.PICKUP_ALL);
            this.doubleClick = false;
            this.lastClickTime = 0L;
        } else {
            if (this.dragSplitting && this.dragSplittingButton != state) {
                this.dragSplitting = false;
                this.dragSplittingSlots.clear();
                this.ignoreMouseUp = true;
                return;
            }
            if (this.ignoreMouseUp) {
                this.ignoreMouseUp = false;
                return;
            }
            if (this.clickedSlot != null && this.mc.gameSettings.touchscreen) {
                if (state == 0 || state == 1) {
                    if (this.draggedStack.isEmpty() && slot != this.clickedSlot)
                        this.draggedStack = this.clickedSlot.getStack();
                    boolean flag2 = Container.canAddItemToSlot(slot, this.draggedStack, false);
                    if (k != -1 && !this.draggedStack.isEmpty() && flag2) {
                        this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, state, ClickType.PICKUP);
                        this.handleMouseClick(slot, k, 0, ClickType.PICKUP);
                        if (this.mc.player.inventory.getItemStack().isEmpty())
                            this.returningStack = ItemStack.EMPTY;
                        else {
                            this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, state, ClickType.PICKUP);
                            this.touchUpX = mouseX - i;
                            this.touchUpY = mouseY - j;
                            this.returningStackDestSlot = this.clickedSlot;
                            this.returningStack = this.draggedStack;
                            this.returningStackTime = Minecraft.getSystemTime();
                        }
                    } else if (!this.draggedStack.isEmpty()) {
                        this.touchUpX = mouseX - i;
                        this.touchUpY = mouseY - j;
                        this.returningStackDestSlot = this.clickedSlot;
                        this.returningStack = this.draggedStack;
                        this.returningStackTime = Minecraft.getSystemTime();
                    }
                    this.draggedStack = ItemStack.EMPTY;
                    this.clickedSlot = null;
                }
            } else if (this.dragSplitting && !this.dragSplittingSlots.isEmpty()) {
                this.handleMouseClick((Slot)null, -999, Container.getQuickcraftMask(0, this.dragSplittingLimit), ClickType.QUICK_CRAFT);
                for (Slot slot1 : this.dragSplittingSlots)
                    this.handleMouseClick(slot1, slot1.slotNumber, Container.getQuickcraftMask(1, this.dragSplittingLimit), ClickType.QUICK_CRAFT);
                this.handleMouseClick((Slot)null, -999, Container.getQuickcraftMask(2, this.dragSplittingLimit), ClickType.QUICK_CRAFT);
            } else if (!this.mc.player.inventory.getItemStack().isEmpty()) {
                if (this.mc.gameSettings.keyBindPickBlock.isActiveAndMatches(state - 100))
                    this.handleMouseClick(slot, k, state, ClickType.CLONE);
                else {
                    boolean flag1 = k != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
                    if (flag1)
                        this.shiftClickedSlot = slot != null && slot.getHasStack() ? slot.getStack().copy() : ItemStack.EMPTY;
                        this.handleMouseClick(slot, k, state, flag1 ? ClickType.QUICK_MOVE : ClickType.PICKUP);
                }
            }
        }
        if (this.mc.player.inventory.getItemStack().isEmpty())
            this.lastClickTime = 0L;
        this.dragSplitting = false;
    }

    /**
     * Returns whether the mouse is over the given slot.
     */
    private boolean isMouseOverSlot(Slot slotIn, int mouseX, int mouseY) {
        return this.isPointInRegion(slotIn.xPos, slotIn.yPos, 16, 16, mouseX, mouseY);
    }

    /**
     * Test if the 2D point is in a rectangle (relative to the GUI). Args : rectX, rectY, rectWidth, rectHeight, pointX,
     * pointY
     */
    protected boolean isPointInRegion(int rectX, int rectY, int rectWidth, int rectHeight, int pointX, int pointY) {
        int i = this.guiLeft;
        int j = this.guiTop;
        pointX = pointX - i;
        pointY = pointY - j;
        return pointX >= rectX - 1 && pointX < rectX + rectWidth + 1 && pointY >= rectY - 1 && pointY < rectY + rectHeight + 1;
    }

    /**
     * Called when the mouse is clicked over a slot or outside the gui.
     */
    protected void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type) {
        if (slotIn != null)
            slotId = slotIn.slotNumber;
        this.mc.playerController.windowClick(this.container.windowId, slotId, mouseButton, type, this.mc.player);
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (keyCode == 1)
            this.mc.player.closeScreen();
        this.checkHotbarKeys(keyCode);
        if (this.hoveredSlot != null && this.hoveredSlot.getHasStack()) {
            if (this.mc.gameSettings.keyBindPickBlock.isActiveAndMatches(keyCode))
                this.handleMouseClick(this.hoveredSlot, this.hoveredSlot.slotNumber, 0, ClickType.CLONE);
            else if (this.mc.gameSettings.keyBindDrop.isActiveAndMatches(keyCode))
                this.handleMouseClick(this.hoveredSlot, this.hoveredSlot.slotNumber, isCtrlKeyDown() ? 1 : 0, ClickType.THROW);
        }
        for (GUISlotsFramework framework : this.getWorkspace().getCurrentSection().getSlotsFrameworks())
            framework.keyTyped(typedChar, keyCode);
    }

    /**
     * Checks whether a hotbar key (to swap the hovered item with an item in the hotbar) has been pressed. If so, it
     * swaps the given items.
     * Returns true if a hotbar key was pressed.
     */
    protected boolean checkHotbarKeys(int keyCode) {
        if (this.mc.player.inventory.getItemStack().isEmpty() && this.hoveredSlot != null) {
            for (int i = 0; i < 9; ++i) {
                if (this.mc.gameSettings.keyBindsHotbar[i].isActiveAndMatches(keyCode)) {
                    this.handleMouseClick(this.hoveredSlot, this.hoveredSlot.slotNumber, i, ClickType.SWAP);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed() {
        super.onGuiClosed();
        if (this.mc.player != null)
            this.container.onContainerClosed(this.mc.player);
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame() {
        return false;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen() {
        super.updateScreen();
        if (!this.mc.player.isEntityAlive() || this.mc.player.isDead)
            this.mc.player.closeScreen();
        if (this.isWorkspaceCreated())
            for (GUISlotsFramework framework : this.getWorkspace().getCurrentSection().getSlotsFrameworks())
                framework.update();
    }

    @Nullable
    public Slot getSlotUnderMouse() { 
        return this.hoveredSlot; 
    }

    public int getGuiLeft() { 
        return guiLeft; 
    }

    public int getGuiTop() { 
        return guiTop; 
    }

    public int getXSize() { 
        return xSize; 
    }

    public int getYSize() { 
        return ySize; 
    }
}
