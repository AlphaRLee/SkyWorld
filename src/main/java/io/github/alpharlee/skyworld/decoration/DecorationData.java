package io.github.alpharlee.skyworld.decoration;

import nl.rutgerkok.worldgeneratorapi.WorldRef;

public class DecorationData {
	public WorldRef worldRef;
	public String schematicName;
	public int x;
	public int y;
	public int z;

	public DecorationData(WorldRef worldRef, String schematicName, int x, int y, int z) {
		this.worldRef = worldRef;
		this.schematicName = schematicName;
		this.x = x;
		this.y = y;
		this.z = z;
	}
}
