package com.example;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Claude12111TestClient implements ClientModInitializer {

	public static volatile boolean packetLoggerEnabled = false;
	public static final Set<String> knownPackets = ConcurrentHashMap.newKeySet();
	public static final Set<String> excludedPackets = new HashSet<>();

	private static KeyMapping helloWorldKey;
	private static KeyMapping packetLoggerKey;
	private static KeyMapping packetFilterKey;

	@Override
	public void onInitializeClient() {
		helloWorldKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
			"key.claude-12111-test.hello_world",
			InputConstants.Type.KEYSYM,
			GLFW.GLFW_KEY_H,
			KeyMapping.Category.MISC
		));

		packetLoggerKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
			"key.claude-12111-test.packet_logger",
			InputConstants.Type.KEYSYM,
			GLFW.GLFW_KEY_K,
			KeyMapping.Category.MISC
		));

		packetFilterKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
			"key.claude-12111-test.packet_filter",
			InputConstants.Type.KEYSYM,
			GLFW.GLFW_KEY_J,
			KeyMapping.Category.MISC
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (helloWorldKey.consumeClick()) {
				if (client.player != null) {
					client.player.displayClientMessage(Component.literal("Hello, World!"), false);
				}
			}

			while (packetLoggerKey.consumeClick()) {
				packetLoggerEnabled = !packetLoggerEnabled;
				if (client.player != null) {
					String state = packetLoggerEnabled ? "§aenabled" : "§cdisabled";
					client.player.displayClientMessage(Component.literal("Packet Logger " + state), false);
				}
			}

			while (packetFilterKey.consumeClick()) {
				client.setScreen(new PacketLoggerScreen());
			}
		});
	}
}
