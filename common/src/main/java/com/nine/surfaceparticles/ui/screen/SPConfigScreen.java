package com.nine.surfaceparticles.ui.screen;

import com.nine.surfaceparticles.config.ConfigDraft;
import com.nine.surfaceparticles.config.SPConfig;
import com.nine.surfaceparticles.ui.component.SPConfigList;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class SPConfigScreen extends Screen {

    public static final int HEADER_HEIGHT = 33;
    public static final int FOOTER_HEIGHT = 51;

    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 20;

    private final Screen parent;
    private final ConfigDraft draft = ConfigDraft.create();

    private SPConfigList list;

    public SPConfigScreen(Screen parent) {
        super(Component.translatable("screen.surfaceparticles.config.title"));
        this.parent = parent;
    }

    public int getHeaderHeight() {
        return HEADER_HEIGHT;
    }

    public int getFooterTop() {
        return this.height - FOOTER_HEIGHT;
    }

    @Override
    protected void init() {
        this.list = new SPConfigList(this.minecraft, this.width, this);
        this.addOptions();
        this.addWidget(this.list);

        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> this.saveAndClose())
                .bounds((this.width - BUTTON_WIDTH) / 2,
                        this.height - FOOTER_HEIGHT + 15,
                        BUTTON_WIDTH,
                        BUTTON_HEIGHT)
                .build());
    }

    private void addOptions() {
        this.list.addToggle(
                Component.translatable("config.surfaceparticles.enable_sprint_particles"),
                Component.translatable("config.surfaceparticles.enable_sprint_particles.tooltip"),
                this.draft,
                SPConfig.General.ENABLE_SPRINT_PARTICLES
        );
        this.list.addToggle(
                Component.translatable("config.surfaceparticles.enable_hit_particles"),
                Component.translatable("config.surfaceparticles.enable_hit_particles.tooltip"),
                this.draft,
                SPConfig.General.ENABLE_HIT_PARTICLES
        );
        this.list.addToggle(
                Component.translatable("config.surfaceparticles.enable_destroy_particles"),
                Component.translatable("config.surfaceparticles.enable_destroy_particles.tooltip"),
                this.draft,
                SPConfig.General.ENABLE_DESTROY_PARTICLES
        );
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        this.list.render(graphics, mouseX, mouseY, partialTick);
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 12, 0xFFFFFF);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private void saveAndClose() {
        this.draft.commit();
        this.onClose();
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }
}
