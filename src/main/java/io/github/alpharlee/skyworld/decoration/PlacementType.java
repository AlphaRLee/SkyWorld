package io.github.alpharlee.skyworld.decoration;

import java.util.HashMap;
import java.util.Map;

public enum PlacementType {
	AIR("air"),
	FLOOR("floor"),
	CEILING("ceiling"),
	WALL("wall");

	private static Map<String, PlacementType> nameLookup = new HashMap<>();
	static {
		for (PlacementType type : PlacementType.values()) {
			nameLookup.put(type.name, type);
		}
	}

	private String name;
	PlacementType(String name) {
		this.name = name;
	}

	public static PlacementType get(String name) {
		return nameLookup.get(name);
	}

	public String getName() {
		return name;
	}
}
