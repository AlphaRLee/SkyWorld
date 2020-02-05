package io.github.alpharlee.skyworld.decoration;

import io.github.alpharlee.skyworld.SkyTerrainGenerator;
import io.github.alpharlee.skyworld.SkyWorld;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class SpawnDecorationRunnable extends BukkitRunnable {
	private final World world;
	private final DecorationData decorationData;

	public SpawnDecorationRunnable(World world, DecorationData decorationData) {
		this.world = world;
		this.decorationData = decorationData;
	}

	/**
	 * When an object implementing interface <code>Runnable</code> is used
	 * to create a thread, starting the thread causes the object's
	 * <code>run</code> method to be called in that separately executing
	 * thread.
	 * <p>
	 * The general contract of the method <code>run</code> is that it may
	 * take any action whatsoever.
	 *
	 * @see Thread#run()
	 */
	@Override
	public void run() {
		Chunk chunk = world.getChunkAt(decorationData.x / SkyTerrainGenerator.CHUNK_SIZE, decorationData.z / SkyTerrainGenerator.CHUNK_SIZE);
		if (!chunk.isLoaded()) {
			return;
		}

		SkyWorld.getInstance().getDecorationManager().pasteSchematic(decorationData.schematicName, world, decorationData.x, decorationData.y, decorationData.z);

		this.cancel();
	}
}
