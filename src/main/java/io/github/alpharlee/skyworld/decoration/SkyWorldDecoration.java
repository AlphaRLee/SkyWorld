package io.github.alpharlee.skyworld.decoration;

import io.github.alpharlee.skyworld.SkyWorld;
import io.github.alpharlee.skyworld.SkyWorldConfig;
import nl.rutgerkok.worldgeneratorapi.WorldRef;
import nl.rutgerkok.worldgeneratorapi.decoration.Decoration;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.Random;

public abstract class SkyWorldDecoration implements Decoration{
	public static final int DATA_Y = 255;
	public static final Material DATA_BLOCK = Material.TRAPPED_CHEST;
	public static final Material DATA_ITEM = Material.POISONOUS_POTATO;

	public abstract String getSchematicName();

	private final WorldRef worldRef;
	private final SkyWorldConfig skyWorldConfig;

	public SkyWorldDecoration(WorldRef worldRef, SkyWorldConfig skyWorldConfig) {
		this.worldRef = worldRef;
		this.skyWorldConfig = skyWorldConfig;
	}

	protected void spawn(int x, int y, int z) {
		spawn(x, y, z, Math.floor(Math.random() * 4) * 90);
	}

	protected void spawn(int x, int y, int z, double angle) {
		DecorationData data = new DecorationData(worldRef, getSchematicName(), x, y, z);
		data.setAngle(angle);

		SpawnDecorationRunnable runnable = new SpawnDecorationRunnable(Bukkit.getWorld(worldRef.getName()), data);
		final long ONE_SECOND = 20l;
		runnable.runTaskTimer(SkyWorld.getInstance(), ONE_SECOND, ONE_SECOND);
	}

	/**
	 * Get a random int that is suitable for x coordinate OR z coordinate
	 * @return
	 */
	protected int getRandomX(DecorationArea area, Random random) {
		return random.nextInt(2 * DecorationArea.DECORATION_RADIUS) - DecorationArea.DECORATION_RADIUS + area.getCenterX();
	}

	protected int getRandomZ(DecorationArea area, Random random) {
		return random.nextInt(2 * DecorationArea.DECORATION_RADIUS) - DecorationArea.DECORATION_RADIUS + area.getCenterZ();
	}
}
