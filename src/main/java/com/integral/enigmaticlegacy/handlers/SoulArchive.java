package com.integral.enigmaticlegacy.handlers;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.entities.PermanentItemEntity;
import com.integral.enigmaticlegacy.objects.Vector3;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelResource;

public class SoulArchive {
	private static final Charset UTF8 = Charset.forName("UTF-8");
	private static SoulArchive instance;
	private final File saveFile;
	private final Multimap<ResourceKey<Level>, SoulData> data = HashMultimap.create();

	public SoulArchive(File saveFolder) {
		Preconditions.checkArgument(saveFolder.exists() && saveFolder.isDirectory(),
				"File " + saveFolder + " does not exist or is not a folder!");
		this.saveFile = new File(saveFolder, "soul_archive.json");
	}

	public Optional<BlockPos> findNearest(Level level, BlockPos pos) {
		var data =  this.data.get(level.dimension()).stream().filter(record -> record.type() == 0)
				.reduce((record1, record2) -> pos.distSqr(record1.pos()) > pos.distSqr(record2.pos()) ?
						record2 : record1).orElse(null);

		return data != null ? Optional.of(data.pos()) : Optional.empty();
	}

	public void save() {
		try {
			try (OutputStream out = FileUtils.openOutputStream(this.saveFile, false)) {
				IOUtils.write(this.saveToBytes(), out);
			}
		} catch (Exception ex) {
			EnigmaticLegacy.logger.fatal("FAILED TO SAVE FILE: " + this.saveFile);
			throw new RuntimeException(ex);
		}
	}

	public void load() {
		this.data.clear();

		try {
			if (!this.saveFile.exists() || !this.saveFile.isFile())
				return;

			try (InputStream in = FileUtils.openInputStream(this.saveFile)) {
				byte[] bytes = in.readAllBytes();
				this.loadFromBytes(bytes);
			}
		} catch (Exception ex) {
			EnigmaticLegacy.logger.fatal("FAILED TO LOAD FILE: " + this.saveFile);
			throw new RuntimeException(ex);
		}
	}

	private byte[] saveToBytes() {
		String text = new GsonBuilder().setPrettyPrinting().create().toJson(this.data.values());
		return text.getBytes(UTF8);
	}

	private void loadFromBytes(byte[] bytes) {
		this.data.clear();

		String text = new String(bytes, UTF8);
		List<SoulData> list =  new Gson().fromJson(text, List.class);

		list.forEach(record -> {
			ResourceKey<Level> key = ResourceKey.create(Registry.DIMENSION_REGISTRY, record.dimension);
			this.data.put(key, record);
		});
	}

	private void synchronize() {

	}

	public void addItem(PermanentItemEntity item) {
		SoulData record = new SoulData(item.level.dimension().location(), item.getUUID(), item.getOwnerId(),
				item.blockPosition(), item.containsSoul() ? 0 : 1);

		if (this.data.put(item.level.dimension(), record)) {
			this.save();
			this.synchronize();
		}
	}

	public void removeItem(PermanentItemEntity item) {
		if (this.data.values().removeIf(record -> record.isEqual(item))) {
			this.save();
			this.synchronize();
		}
	}

	public static SoulArchive getInstance() {
		return instance;
	}

	public static void initialize(MinecraftServer server) {
		instance = new SoulArchive(server.getWorldPath(LevelResource.ROOT).toFile());
		instance.load();
	}

	private static record SoulData(ResourceLocation dimension, UUID id, UUID ownerID, BlockPos pos, int type) {
		boolean isEqual(PermanentItemEntity item) {
			return Objects.equals(this.id, item.getUUID()) && Objects.equals(this.ownerID, item.getOwnerId());
		}
	}

}
