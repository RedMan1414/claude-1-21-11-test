package com.example.mixin;

import com.example.Claude12111TestClient;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public class PacketLoggerMixin {

	@Inject(method = "sendPacket", at = @At("HEAD"))
	private void onSendPacket(Packet<?> packet, ChannelFutureListener listener, boolean flush, CallbackInfo ci) {
		if (!Claude12111TestClient.packetLoggerEnabled) return;
		String name = packet.getClass().getSimpleName();
		Minecraft mc = Minecraft.getInstance();
		mc.execute(() -> {
			if (mc.player != null) {
				mc.player.displayClientMessage(Component.literal("[C2S] " + name), false);
			}
		});
	}

	@Inject(method = "channelRead0", at = @At("HEAD"))
	private void onChannelRead0(ChannelHandlerContext ctx, Packet<?> packet, CallbackInfo ci) {
		if (!Claude12111TestClient.packetLoggerEnabled) return;
		String name = packet.getClass().getSimpleName();
		Minecraft mc = Minecraft.getInstance();
		mc.execute(() -> {
			if (mc.player != null) {
				mc.player.displayClientMessage(Component.literal("[S2C] " + name), false);
			}
		});
	}
}
