package io.github.alpharlee.skyworld.decoration;

import io.github.alpharlee.skyworld.SkyWorldConfig;
import nl.rutgerkok.worldgeneratorapi.WorldRef;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;
import org.bukkit.World;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.util.Random;

public abstract class DynamicDecoration extends SkyWorldDecoration {
	private final String name;
	private final String schematicName;
	protected final SkyWorldConfig skyWorldConfig;
	protected final SimplexOctaveGenerator terrainTester;
	protected final WorldRef worldRef;

	public DynamicDecoration(String name, String schematicName, World world, SkyWorldConfig skyWorldConfig) {
		this.name = name;
		this.schematicName = schematicName;
		this.skyWorldConfig = skyWorldConfig;

		this.worldRef = WorldRef.of(world);

		int landOctaves = (int) skyWorldConfig.getLandOctaves().get(worldRef);
		terrainTester = new SimplexOctaveGenerator(new Random(world.getSeed()), landOctaves);
	}

	public String getName() { return name; }

	@Override
	public String getSchematicName() {
		return schematicName;
	}
}
