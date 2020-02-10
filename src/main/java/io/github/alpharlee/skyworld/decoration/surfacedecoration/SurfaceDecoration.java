package io.github.alpharlee.skyworld.decoration.surfacedecoration;

import io.github.alpharlee.skyworld.SkyWorldConfig;
import io.github.alpharlee.skyworld.decoration.DynamicDecoration;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.Random;

public abstract class SurfaceDecoration extends DynamicDecoration {
	public SurfaceDecoration(String name, String schematicName, World world, SkyWorldConfig skyWorldConfig) {
		super(name, schematicName, world, skyWorldConfig);
	}

	protected abstract Vector getSearchDirection();

	/**
	 * Decorates a 16x16 area. See the documentation of {@link DecorationArea} for
	 * where exactly you should place objects.
	 * <p>
	 * <p>
	 * Note: <strong>this method can be called on any thread</strong>, including the
	 * main server thread. As long as you only use the methods contained in the
	 * decoration area and in the property registry,
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
		final int maxSearchDistance = 10;
		final int maxTestCount = (int) Math.floor(skyWorldConfig.getDynamicDecorationProperty(getName(), "spawnAttempts").get(worldRef));
		final double probability = (double) skyWorldConfig.getDynamicDecorationProperty(getName(), "spawnChance").get(worldRef);

		for (int i = 0; i < maxTestCount; i++) {
			Vector position = new Vector(getRandomX(area, random), random.nextInt(255), getRandomZ(area, random));
			Vector searchDirection = getSearchDirection();
			int searchDistance = 0;
			boolean isValid = false;
			boolean lastBlockAir = false;
			while (position.getBlockY() > 0 && searchDistance < maxSearchDistance && !isValid) {
				// Hit land
				if (area.getBlock(position.getBlockX(), position.getBlockY(), position.getBlockZ()) != Material.AIR) {
					// Hit land or started in land?
					if (lastBlockAir) {
						isValid = true;
					}
					break;
				}

				lastBlockAir = true;
				searchDistance++;
				position.add(searchDirection);
			}

			if (isValid && random.nextDouble() <= probability) {
				spawn(area, position.getBlockX(), position.getBlockY(), position.getBlockZ());
			}
		}
	}
}
