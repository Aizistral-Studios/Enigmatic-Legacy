package com.aizistral.enigmaticlegacy.packets.clients;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Supplier;

public class PacketSpawnFakeCorpse {
    private final int entityID;
    private final double x;
    private final double y;
    private final double z;
    private final float yRot;

    public PacketSpawnFakeCorpse(Player player) {
        this.entityID = player.getId();
        this.x = player.getX();
        this.y = player.getY();
        this.z = player.getZ();
        this.yRot = player.getYRot();
    }

    public PacketSpawnFakeCorpse(FriendlyByteBuf buf) {
        this.entityID = buf.readInt();
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.yRot = buf.readFloat();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(entityID);
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

            Entity entity = level.getEntity(entityID);
            if (!(entity instanceof Player player)) return;

            EnigmaticLegacy.LOGGER.info("send fake corpse");

            Corpse corpse = new Corpse(level, player.getGameProfile());

//            List<SynchedEntityData.DataValue<?>> playerData = player.getEntityData().getNonDefaultValues();
//            if (playerData != null) {
//                corpse.getEntityData().assignValues(playerData);
//            }

            corpse.setUUID(UUID.randomUUID());

            corpse.setPos(x, y, z);
            corpse.setYRot(yRot);

            level.addPlayer(corpse.getId(), corpse);
        });

        ctx.get().setPacketHandled(true);
        return true;
    }

    private static class Corpse extends RemotePlayer {

        private final GameProfile ownerProfile;

        public Corpse(ClientLevel pClientLevel, GameProfile pGameProfile) {
            super(pClientLevel, pGameProfile);
            this.ownerProfile = pGameProfile;
        }

        @Override
        protected @Nullable PlayerInfo getPlayerInfo() {
            if (ownerProfile != null) {
                ClientPacketListener connection = Minecraft.getInstance().getConnection();
                if (connection != null) return connection.getPlayerInfo(ownerProfile.getId());
            }

            return super.getPlayerInfo();
        }

        @Override
        public void tick() {
            super.tick();

            this.deathTime++;

            if (deathTime >= 20) {
                this.discard();
            }
        }
    }
}
