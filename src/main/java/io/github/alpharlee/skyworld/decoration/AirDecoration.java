package io.github.alpharlee.skyworld.decoration;

import io.github.alpharlee.skyworld.SkyWorldConfig;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;
import org.bukkit.World;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.util.Random;

public class AirDecoration extends DynamicDecoration {
	public AirDecoration(String name, String schematicName, World world, SkyWorldConfig skyWorldConfig) {
		super(name, schematicName, world, skyWorldConfig);
	}

	/**
	 * Decorates a 16x16 area. See the documentation of {@link DecorationArea} for
	 * where exactly you should place objects.
	 * <p>
	 * <p>
	 * Note: <strong>this method can be called on any thread</strong>, including the
	 * main server thread. As long as you only use the methods contained in the
	 * decoration area and in the PropertyRegistry property registry,
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

		int maxTestCount = (int) Math.floor(skyWorldConfig.getDynamicDecorationProperty(getName(), "spawnAttempts").get(worldRef));
		for (int i = 0; i < maxTestCount; i++) {
			int x = random.nextInt(2 * DecorationArea.DECORATION_RADIUS) - DecorationArea.DECORATION_RADIUS + area.getCenterX();
			int z = random.nextInt(2 * DecorationArea.DECORATION_RADIUS) - DecorationArea.DECORATION_RADIUS + area.getCenterZ();
			int y = random.nextInt(maxSpawnHeight - minSpawnHeight) + minSpawnHeight;

			if (shouldSpawn(x, y, z, random)) {
				spawn(area, x, y, z);
			}
		}
	}

	private boolean shouldSpawn(int x, int y, int z, Random random) {
		double landFrequency = (double) skyWorldConfig.getLandFrequency().get(worldRef);
		double landAmplitude = (double) skyWorldConfig.getLandAmplitude().get(worldRef);

		double spawnThreshold = -0.5;
		double probability = (double) skyWorldConfig.getDynamicDecorationProperty(getName(), "spawnChance").get(worldRef);

		return random.nextDouble() <= probability && terrainTester.noise(x, y, z, landFrequency, landAmplitude, true) <= spawnThreshold;
	}
}
