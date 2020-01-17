package austeretony.oxygen_core.client.gui.privileges.players;

import java.util.UUID;

import austeretony.alternateui.util.EnumGUIAlignment;
import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.api.EnumBaseGUISetting;
import austeretony.oxygen_core.client.api.OxygenHelperClient;
import austeretony.oxygen_core.client.gui.OxygenGUITextures;
import austeretony.oxygen_core.client.gui.OxygenGUIUtils;
import austeretony.oxygen_core.client.gui.elements.OxygenIndexedPanelEntry;
import austeretony.oxygen_core.client.privilege.RoleDataClient;
import austeretony.oxygen_core.common.EnumActivityStatus;
import austeretony.oxygen_core.common.PlayerSharedData;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.util.OxygenUtils;
import net.minecraft.client.renderer.GlStateManager;

public class PlayerPanelEntry extends OxygenIndexedPanelEntry<UUID> {

    private String lastActivity, username, dimension;

    private int tooltipBackgroundColor, tooltipFrameColor, statusIconU, rolesAmount, rolesWidth;

    private boolean offline;

    private String[] roles = new String[OxygenMain.MAX_ROLES_PER_PLAYER];

    public PlayerPanelEntry(PlayerSharedData sharedData) {
        super(sharedData.getPlayerUUID());
        this.lastActivity = ClientReference.localize("oxygen_core.gui.lastActivityTime", OxygenUtils.getTimePassedLocalizedString(sharedData.getLastActivityTime()));

        EnumActivityStatus activityStatus = OxygenHelperClient.getPlayerActivityStatus(sharedData);
        this.statusIconU = activityStatus.ordinal() * 3;
        this.offline = activityStatus == EnumActivityStatus.OFFLINE;

        this.username = sharedData.getUsername();
        this.dimension = OxygenHelperClient.getDimensionName(OxygenHelperClient.getPlayerDimension(sharedData));      

        this.tooltipBackgroundColor = EnumBaseGUISetting.BACKGROUND_BASE_COLOR.get().asInt();
        this.tooltipFrameColor = EnumBaseGUISetting.BACKGROUND_ADDITIONAL_COLOR.get().asInt();
        this.setDynamicBackgroundColor(EnumBaseGUISetting.ELEMENT_ENABLED_COLOR.get().asInt(), EnumBaseGUISetting.ELEMENT_DISABLED_COLOR.get().asInt(), EnumBaseGUISetting.ELEMENT_HOVERED_COLOR.get().asInt());
        this.setTextDynamicColor(EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt(), EnumBaseGUISetting.TEXT_DISABLED_COLOR.get().asInt(), EnumBaseGUISetting.TEXT_HOVERED_COLOR.get().asInt());
        this.setTooltipScaleFactor(EnumBaseGUISetting.TEXT_TOOLTIP_SCALE.get().asFloat());

        int i, width, roleId;
        RoleDataClient roleData;
        for (i = 0; i < this.roles.length; i++) {
            roleId = sharedData.getByte(i + OxygenMain.ROLES_SHARED_DATA_STARTING_INDEX);
            if (roleId != OxygenMain.DEFAULT_ROLE_INDEX) {
                roleData = OxygenManagerClient.instance().getPrivilegesManager().getRoleData(roleId);
                this.roles[i] = roleData.nameColor + roleData.name;
                this.rolesAmount = i + 1;
                width = this.textWidth(roleData.name, this.getTooltipScaleFactor()) + 4;
                if (this.rolesWidth < width)
                    this.rolesWidth = width;
            }
        }
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (this.isVisible()) {  
            GlStateManager.pushMatrix();           
            GlStateManager.translate(this.getX(), this.getY(), 0.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);  

            int color, textColor, textY;                      
            if (!this.isEnabled()) {                 
                color = this.getDisabledBackgroundColor();
                textColor = this.getDisabledTextColor();           
            } else if (this.isHovered() || this.isToggled()) {                 
                color = this.getHoveredBackgroundColor();
                textColor = this.getHoveredTextColor();
            } else {                   
                color = this.getEnabledBackgroundColor(); 
                textColor = this.getEnabledTextColor();      
            }

            int third = this.getWidth() / 3;
            OxygenGUIUtils.drawGradientRect(0.0D, 0.0D, third, this.getHeight(), 0x00000000, color, EnumGUIAlignment.RIGHT);
            drawRect(third, 0, this.getWidth() - third, this.getHeight(), color);
            OxygenGUIUtils.drawGradientRect(this.getWidth() - third, 0.0D, this.getWidth(), this.getHeight(), 0x00000000, color, EnumGUIAlignment.LEFT);

            textY = (this.getHeight() - this.textHeight(this.getTextScale())) / 2 + 1;

            GlStateManager.pushMatrix();           
            GlStateManager.translate(18.0F, textY, 0.0F); 
            GlStateManager.scale(this.getTextScale(), this.getTextScale(), 0.0F); 
            this.mc.fontRenderer.drawString(this.username, 0, 0, textColor, this.isTextShadowEnabled());
            GlStateManager.popMatrix();

            GlStateManager.pushMatrix();    
            GlStateManager.translate(120.0F, textY, 0.0F); 
            GlStateManager.scale(this.getTextScale(), this.getTextScale(), 0.0F); 
            this.mc.fontRenderer.drawString(this.dimension, 0, 0, textColor, this.isTextShadowEnabled());
            GlStateManager.popMatrix();

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);  

            this.mc.getTextureManager().bindTexture(OxygenGUITextures.STATUS_ICONS); 
            drawCustomSizedTexturedRect(7, 4, this.statusIconU, 0, 3, 3, 12, 3);   

            GlStateManager.popMatrix();
        }
    }

    @Override
    public void drawTooltip(int mouseX, int mouseY) {
        if (this.offline && mouseX >= this.getX() + 7 && mouseY >= this.getY() + 4 && mouseX < this.getX() + 10 && mouseY < this.getY() + 7)
            this.drawLastActivityTimeTooltip(mouseX, mouseY, this.lastActivity);
        if (this.rolesAmount != 0 && mouseX >= this.getX() + 18 && mouseY >= this.getY() + 1 && mouseX < this.getX() + 60 && mouseY < this.getY() + 9)
            this.drawRoles(mouseX, mouseY);
    }

    private void drawLastActivityTimeTooltip(int mouseX, int mouseY, String text) {
        int 
        width = this.textWidth(text, this.getTooltipScaleFactor()) + 6,
        height = 9;
        GlStateManager.pushMatrix();           
        GlStateManager.translate(mouseX, mouseY - height - 2, 0.0F);            
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        //background
        drawRect(0, 0, width, height, this.tooltipBackgroundColor);

        //frame
        OxygenGUIUtils.drawRect(0.0D, 0.0D, 0.4D, (double) height, this.tooltipFrameColor);
        OxygenGUIUtils.drawRect((double) width - 0.4D, 0.0D, (double) width, (double) height, this.tooltipFrameColor);
        OxygenGUIUtils.drawRect(0.0D, 0.0D, (double) width, 0.4D, this.tooltipFrameColor);
        OxygenGUIUtils.drawRect(0.0D, (double) height - 0.4D, (double) width, (double) height, this.tooltipFrameColor);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        GlStateManager.pushMatrix();           
        GlStateManager.translate((width - this.textWidth(text, this.getTooltipScaleFactor())) / 2, (height - this.textHeight(this.getTooltipScaleFactor())) / 2 + 1.0F, 0.0F);            
        GlStateManager.scale(this.getTooltipScaleFactor(), this.getTooltipScaleFactor(), 0.0F);

        this.mc.fontRenderer.drawString(text, 0, 0, this.getEnabledTextColor(), false);

        GlStateManager.popMatrix();      

        GlStateManager.popMatrix(); 
    }

    private void drawRoles(int mouseX, int mouseY) {
        int height = 9;
        GlStateManager.pushMatrix();           
        GlStateManager.translate(mouseX, mouseY - height * this.rolesAmount, 0.0F);            
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        //background
        drawRect(0, 0, this.rolesWidth, height * this.rolesAmount, this.tooltipBackgroundColor);

        //frame
        OxygenGUIUtils.drawRect(0.0D, 0.0D, 0.4D, (double) height * this.rolesAmount, this.tooltipFrameColor);
        OxygenGUIUtils.drawRect((double) this.rolesWidth - 0.4D, 0.0D, (double) this.rolesWidth, (double) height * this.rolesAmount, this.tooltipFrameColor);
        OxygenGUIUtils.drawRect(0.0D, 0.0D, (double) this.rolesWidth, 0.4D, this.tooltipFrameColor);
        OxygenGUIUtils.drawRect(0.0D, (double) height * this.rolesAmount - 0.4D, (double) this.rolesWidth, (double) height * this.rolesAmount, this.tooltipFrameColor);

        for (int i = 0; i < this.rolesAmount; i++) {
            GlStateManager.pushMatrix();           
            GlStateManager.translate(2.0F, i * height + (height - this.textHeight(this.getTooltipScaleFactor())) / 2 + 1.0F, 0.0F);            
            GlStateManager.scale(this.getTooltipScaleFactor(), this.getTooltipScaleFactor(), 0.0F);

            this.mc.fontRenderer.drawString(this.roles[i], 0, 0, this.getEnabledTextColor(), false);

            GlStateManager.popMatrix();   
        }

        GlStateManager.popMatrix(); 
    }
}
