package io.github.alpharlee.skyworld.decoration;

import io.github.alpharlee.skyworld.SkyWorld;
import io.github.alpharlee.skyworld.SkyWorldConfig;
import nl.rutgerkok.worldgeneratorapi.WorldRef;
import nl.rutgerkok.worldgeneratorapi.decoration.Decoration;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.util.Random;

public class AirshipDecoration implements Decoration {
	private final World world;
	private SimplexOctaveGenerator terrainTester;
	private SkyWorldConfig skyWorldConfig;

	public AirshipDecoration(World world, SkyWorldConfig skyWorldConfig) {
		this.world = world;
		this.skyWorldConfig = skyWorldConfig;

		int landOctaves = (int) skyWorldConfig.getLandOctaves().get(WorldRef.of(world));
		terrainTester = new SimplexOctaveGenerator(new Random(world.getSeed()), landOctaves);
	}

	/**
	 * Decorates a 16x16 area. See the documentation of {@link DecorationArea} for
	 * where exactly you should place objects.
	 * <p>
	 * <p>
	 * Note: <strong>this method can be called on any thread</strong>, including the
	 * main server thread. As long as you only use the methods contained in the
	 * decoration area and in the {@link PropertyRegistry property registry},
	 * there's no need to worry about this. However, if you use/call code from other
	 * areas (like the rest of the world or an ordinary hash map from your plugin)
	 * you will get into trouble. Exceptions may be thrown, or worse: your world may
	 * be corrupted silently.
	 *
	 * @param area   The decoration area.
	 * @param random
	 */
	@Override
	public void decorate(DecorationArea area, Random random) {

		int minSpawnHeight = 16;
		int maxSpawnHeight = 180;

		int maxTestCount = 10;
		for (int i = 0; i < maxTestCount; i++) {
			int x = random.nextInt(2 * DecorationArea.DECORATION_RADIUS) - DecorationArea.DECORATION_RADIUS + area.getCenterX();
			int z = random.nextInt(2 * DecorationArea.DECORATION_RADIUS) - DecorationArea.DECORATION_RADIUS + area.getCenterZ();
			int y = random.nextInt(maxSpawnHeight - minSpawnHeight) + maxSpawnHeight;

			if (shouldSpawn(x, y, z, random)) {
				spawn(area, x, y, z);
			}
		}
	}

	private boolean shouldSpawn(int x, int y, int z, Random random) {
		double landFrequency = (double) skyWorldConfig.getLandFrequency().get(worldRef);
		double landAmplitude = (double) skyWorldConfig.getLandAmplitude().get(worldRef);

		double spawnThreshold = -0.8;
		double probability = 1;

		return random.nextDouble() <= probability && terrainTester.noise(x, y, z, landFrequency, landAmplitude, true) <= spawnThreshold;
	}

	private void spawn(DecorationArea area, int x, int y, int z) {
		area.setBlock(x, y, z, Material.BEDROCK);
	}
}
