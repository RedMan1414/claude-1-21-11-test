package com.example;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PacketLoggerScreen extends Screen {

	private PacketList list;

	public PacketLoggerScreen() {
		super(Component.literal("Packet Logger - Filter"));
	}

	@Override
	protected void init() {
		list = new PacketList(minecraft, width, height - 64, 32, 20);
		addRenderableWidget(list);

		addRenderableWidget(Button.builder(Component.literal("Done"), btn -> onClose())
			.pos(width / 2 - 75, height - 28)
			.size(150, 20)
			.build());
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
		super.render(graphics, mouseX, mouseY, delta);
		graphics.drawCenteredString(font, title, width / 2, 10, 0xFFFFFF);
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	private static class PacketList extends ObjectSelectionList<PacketList.PacketEntry> {

		PacketList(Minecraft mc, int width, int height, int y, int itemHeight) {
			super(mc, width, height, y, itemHeight);
			List<String> sorted = new ArrayList<>(Claude12111TestClient.knownPackets);
			sorted.sort(String::compareTo);
			for (String label : sorted) {
				addEntry(new PacketEntry(label));
			}
		}

		static class PacketEntry extends ObjectSelectionList.Entry<PacketEntry> {

			private final String label;
			private final StringWidget labelWidget;
			private final Button toggleButton;

			PacketEntry(String label) {
				this.label = label;
				Minecraft mc = Minecraft.getInstance();
				this.labelWidget = new StringWidget(0, 0, 200, 9, Component.literal(label), mc.font);
				this.toggleButton = Button.builder(toggleText(label), btn -> {
					if (Claude12111TestClient.excludedPackets.contains(label)) {
						Claude12111TestClient.excludedPackets.remove(label);
					} else {
						Claude12111TestClient.excludedPackets.add(label);
					}
					btn.setMessage(toggleText(label));
				}).pos(0, 0).size(60, 16).build();
			}

			private static Component toggleText(String label) {
				return Component.literal(Claude12111TestClient.excludedPackets.contains(label) ? "§cHidden" : "§aShown");
			}

			@Override
			public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float delta) {
				int x = getContentX();
				int y = getContentY();
				int w = getContentWidth();
				int h = getContentHeight();

				labelWidget.setX(x + 4);
				labelWidget.setY(y + (h - 9) / 2);
				labelWidget.setWidth(w - 72);
				labelWidget.render(graphics, mouseX, mouseY, delta);

				toggleButton.setX(x + w - 64);
				toggleButton.setY(y + (h - 16) / 2);
				toggleButton.render(graphics, mouseX, mouseY, delta);
			}

			@Override
			public void visitWidgets(Consumer<AbstractWidget> consumer) {
				consumer.accept(labelWidget);
				consumer.accept(toggleButton);
			}

			@Override
			public Component getNarration() {
				return Component.literal(label);
			}
		}
	}
}
