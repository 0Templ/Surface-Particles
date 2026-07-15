package com.nine.surfaceparticles.ui.screen;

import com.nine.surfaceparticles.config.ConfigDraft;
import com.nine.surfaceparticles.config.SPConfig;
import com.nine.surfaceparticles.ui.component.SPConfigList;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class SPConfigScreen extends Screen {

    public final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this, 33, 51);

    private final Screen parent;
    private final ConfigDraft draft = ConfigDraft.create();

    private SPConfigList list;

    public SPConfigScreen(Screen parent) {
        super(Component.translatable("screen.surfaceparticles.config.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.layout.addTitleHeader(this.title, this.font);

        this.list = this.layout.addToContents(new SPConfigList(this.minecraft, this.width, this));
        this.addOptions();

        LinearLayout footer = this.layout.addToFooter(LinearLayout.vertical().spacing(4));
        footer.defaultCellSetting().alignHorizontallyCenter();

        LinearLayout buttons = footer.addChild(LinearLayout.horizontal().spacing(8));
        buttons.addChild(Button.builder(CommonComponents.GUI_DONE, button -> this.saveAndClose()).build());


        this.layout.visitWidgets(this::addRenderableWidget);
        this.repositionElements();
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
    protected void repositionElements() {
        this.layout.arrangeElements();
        if (this.list != null) {
            this.list.updateSize(this.width, this.layout);
        }
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
