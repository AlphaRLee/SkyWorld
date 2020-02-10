package io.github.alpharlee.skyworld.decoration.surfacedecoration;

import io.github.alpharlee.skyworld.SkyWorldConfig;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class FloorDecoration extends SurfaceDecoration {
	public FloorDecoration(String name, String schematicName, World world, SkyWorldConfig skyWorldConfig) {
		super(name, schematicName, world, skyWorldConfig);
	}

	@Override
	protected Vector getSearchDirection() {
		return new Vector(0, -1, 0);
	}
}
