package io.github.alpharlee.skyworld.decoration;

import nl.rutgerkok.worldgeneratorapi.WorldRef;

import java.util.Objects;

public class DecorationChunk {
	private WorldRef worldRef;
	private int chunkX;
	private int chunkZ;
	public DecorationChunk(WorldRef worldRef, int chunkX, int chunkZ) {
		this.worldRef = worldRef;
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
	}

	@Override
	public int hashCode() {
		return Objects.hash(worldRef, chunkX, chunkZ);
	}

	public WorldRef getWorldRef() {
		return worldRef;
	}

	public int getChunkX() {
		return chunkX;
	}

	public int getChunkZ() {
		return chunkZ;
	}
}
