package io.github.alpharlee.skyworld.decoration;

import nl.rutgerkok.worldgeneratorapi.WorldRef;

public class DecorationData {
	public WorldRef worldRef;
	public String schematicName;
	public int x;
	public int y;
	public int z;
	public double angle = 0;

	public DecorationData(WorldRef worldRef, String schematicName, int x, int y, int z) {
		this.worldRef = worldRef;
		this.schematicName = schematicName;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}
}
