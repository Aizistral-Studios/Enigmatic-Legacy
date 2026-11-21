package com.aizistral.enigmaticlegacy.packets.clients;

import com.aizistral.enigmaticlegacy.entities.PermanentItemEntity;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
    private final ItemStack fakeDrop;

    public PacketSpawnFakeCorpse(Player player, ItemStack fakeDrop) {
        this.entityID = player.getId();
        this.x = player.getX();
        this.y = player.getY();
        this.z = player.getZ();
        this.yRot = player.getYRot();
        this.fakeDrop = fakeDrop.copy();
    }

    public PacketSpawnFakeCorpse(FriendlyByteBuf buf) {
        this.entityID = buf.readInt();
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.yRot = buf.readFloat();
        this.fakeDrop = buf.readItem();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(entityID);
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeFloat(yRot);
        buf.writeItem(fakeDrop);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            ClientLevel level = mc.level;

            if (level == null) return;

            Entity entity = level.getEntity(entityID);
            if (!(entity instanceof Player player)) return;

            spawnCorpse(player, level);
            spawnFakeDrop(player, level);
        });

        ctx.get().setPacketHandled(true);
        return true;
    }

    private void spawnCorpse(Player player, ClientLevel level) {
        Corpse corpse = new Corpse(level, player.getGameProfile());
        corpse.setUUID(UUID.randomUUID());

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            corpse.setItemSlot(slot, player.getItemBySlot(slot).copy());
        }

        corpse.setPos(x, y, z);
        corpse.setYRot(yRot);

        level.addPlayer(corpse.getId(), corpse);
    }

    private void spawnFakeDrop(Player player, ClientLevel level) {
        if (fakeDrop.isEmpty()) return;

        PermanentItemEntity fakeItem = new PermanentItemEntity(level, x, y + (player.getBbHeight() / 2), z, fakeDrop) {
            private int lifeTime = 30 * 20;

            @Override
            public void tick() {
                super.tick();

                this.lifeTime--;

                if (lifeTime <= 0) {
                    onDisappear();
                }
            }

            private void onDisappear() {
                this.discard();

                // wololo патужна сикс найн чи сикс севен?
                SoundEvent sound = this.random.nextDouble() >= 0.069 ? SoundEvents.PHANTOM_BITE : SoundEvents.EVOKER_PREPARE_WOLOLO;

                level.playLocalSound(
                        this.getX(),
                        this.getY(),
                        this.getZ(),
                        sound,
                        SoundSource.PLAYERS,
                        1.0F,
                        1.2F,
                        false
                );
            }
        };
        fakeItem.makeFakeItem();

        level.putNonPlayerEntity(fakeItem.getId(), fakeItem);
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
