package com.aizistral.enigmaticlegacy.packets.clients;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class PacketSpawnFakeCorpse {
    private final int entityID;
    private final UUID playerID;
    private final String playerName;

    private final double x;
    private final double y;
    private final double z;
    private final float yRot;


    public PacketSpawnFakeCorpse(Player player) {
        this.entityID = player.getId();
        this.playerID = player.getUUID();
        this.playerName = player.getGameProfile().getName();
        this.x = player.getX();
        this.y = player.getY();
        this.z = player.getZ();
        this.yRot = player.getYRot();
    }

    public PacketSpawnFakeCorpse(FriendlyByteBuf buf) {
        this.entityID = buf.readInt();
        this.playerID = buf.readUUID();
        this.playerName = buf.readUtf();
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.yRot = buf.readFloat();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(entityID);
        buf.writeUUID(playerID);
        buf.writeUtf(playerName);
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeFloat(yRot);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            ClientLevel level = mc.level;

            if (level == null) return;

            GameProfile profile = new GameProfile(playerID, playerName);

            RemotePlayer fakeCorpse = new RemotePlayer(level, profile);
            fakeCorpse.setPos(x, y, z);
            fakeCorpse.setYRot(yRot);

            level.addPlayer(entityID + 1, fakeCorpse);
            // TODO: fix fake corpse not spawning
        });

        return true;
    }
}
