package com.nine.surfaceparticles.ui.component;

import com.nine.surfaceparticles.config.ConfigDraft;
import com.nine.surfaceparticles.config.ConfigValue;
import com.nine.surfaceparticles.ui.screen.SPConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;

import java.util.List;

public class SPConfigList extends ContainerObjectSelectionList<SPConfigList.Entry> {

    private static final int ITEM_HEIGHT = 25;
    private static final int ROW_WIDTH = 310;
    private static final int WIDGET_HEIGHT = 20;

    public SPConfigList(Minecraft minecraft, int width, SPConfigScreen screen) {
        super(minecraft, width, screen.height,
                screen.getHeaderHeight(), screen.getFooterTop(), ITEM_HEIGHT);
    }

    @Override
    public int getRowWidth() {
        return ROW_WIDTH;
    }

    public void addToggle(Component label, Component tooltip, ConfigDraft draft, ConfigValue<Boolean> value) {
        CycleButton<Boolean> button = CycleButton.onOffBuilder(draft.get(value))
                .withTooltip(ignoredValue -> Tooltip.create(tooltip))
                .create(0, 0, ROW_WIDTH, WIDGET_HEIGHT, label,
                        (cycleButton, newValue) -> draft.set(value, newValue));
        this.addEntry(new Entry(button));
    }

    public static class Entry extends ContainerObjectSelectionList.Entry<Entry> {

        private final AbstractWidget widget;

        private Entry(AbstractWidget widget) {
            this.widget = widget;
        }

        @Override
        public void render(GuiGraphics graphics, int index, int top, int left, int width, int height,
                           int mouseX, int mouseY, boolean hovered, float partialTick) {
            this.widget.setPosition(left, top + (height - this.widget.getHeight()) / 2);
            this.widget.render(graphics, mouseX, mouseY, partialTick);
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return List.of(this.widget);
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return List.of(this.widget);
        }
    }
}
