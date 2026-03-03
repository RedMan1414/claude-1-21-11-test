package com.example;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public class Claude12111TestClient implements ClientModInitializer {

	private static KeyMapping helloWorldKey;

	@Override
	public void onInitializeClient() {
		helloWorldKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
			"key.claude-12111-test.hello_world",
			InputConstants.Type.KEYSYM,
			GLFW.GLFW_KEY_H,
			KeyMapping.Category.MISC
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (helloWorldKey.consumeClick()) {
				if (client.player != null) {
					client.player.displayClientMessage(Component.literal("Hello, World!"), false);
				}
			}
		});
	}
}
