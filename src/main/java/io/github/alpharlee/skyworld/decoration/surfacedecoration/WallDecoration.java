package io.github.alpharlee.skyworld.decoration.surfacedecoration;

import io.github.alpharlee.skyworld.SkyWorldConfig;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.Random;

public class WallDecoration extends SurfaceDecoration {
	private enum Direction {
		EAST(new Vector(1, 0, 0), 0d),
		SOUTH(new Vector(0, 0, 1), 90d),
		WEST(new Vector(-1, 0, 0), 180d),
		NORTH(new Vector(0, 0, -1), 270d);

		private Vector direction;
		private double angle;
		Direction(Vector direction, double angle) {
			this.direction = direction;
			this.angle = angle;
		}

		public Vector getDirection() {
			return direction;
		}

		public double getAngle() {
			return angle;
		}
	}

	private Direction direction;

	public WallDecoration(String name, String schematicName, World world, SkyWorldConfig skyWorldConfig) {
		super(name, schematicName, world, skyWorldConfig);
	}

	@Override
	protected Vector getSearchDirection() {
		Direction[] directions = Direction.values();
		int rand = new Random().nextInt(directions.length);
		direction = directions[rand];
		return direction.getDirection();
	}

	@Override
	protected double getAngle() {
		return direction.getAngle();
	}
}
