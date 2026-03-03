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
		String label = "[C2S] " + packet.type().id().getPath();
		Minecraft mc = Minecraft.getInstance();
		mc.execute(() -> {
			if (mc.player != null) {
				mc.player.displayClientMessage(Component.literal(label), false);
			}
		});
	}

	@Inject(method = "channelRead0", at = @At("HEAD"))
	private void onChannelRead0(ChannelHandlerContext ctx, Packet<?> packet, CallbackInfo ci) {
		if (!Claude12111TestClient.packetLoggerEnabled) return;
		String label = "[S2C] " + packet.type().id().getPath();
		Minecraft mc = Minecraft.getInstance();
		mc.execute(() -> {
			if (mc.player != null) {
				mc.player.displayClientMessage(Component.literal(label), false);
			}
		});
	}
}
