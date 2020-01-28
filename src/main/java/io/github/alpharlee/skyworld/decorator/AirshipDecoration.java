package io.github.alpharlee.skyworld.decorator;

import nl.rutgerkok.worldgeneratorapi.decoration.Decoration;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;
import org.bukkit.World;

import java.util.Random;

public class AirshipDecoration implements Decoration {
	private final World world;

	public AirshipDecoration(World world) {
		this.world = world;
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

	}
}
