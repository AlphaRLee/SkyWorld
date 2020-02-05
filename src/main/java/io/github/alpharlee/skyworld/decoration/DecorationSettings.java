package io.github.alpharlee.skyworld.decoration;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class DecorationSettings implements ConfigurationSerializable {
	public String name;
	public PlacementType placementType;
	public String schematicName;
	public double spawnChance;
	public int spawnAttempts;

	public DecorationSettings(String name, PlacementType placementType, String schematicName, double spawnChance, int spawnAttempts) {
		this.name = name;
		this.placementType = placementType;
		this.schematicName = schematicName;
		this.spawnChance = spawnChance;
		this.spawnAttempts = spawnAttempts;
	}

	public DecorationSettings(Map<String, Object> data) {
		// TODO throw error message if any of these fields are null

		name = (String) data.get("name");
		placementType = PlacementType.get((String) data.get("type"));
		schematicName = (String) data.get("schematic");
		spawnChance = (double) data.get("spawnChance");
		spawnAttempts = (int) data.get("spawnAttempts");
	}

	/**
	 * Creates a Map representation of this class.
	 * <p>
	 * This class must provide a method to restore this class, as defined in
	 * the {@link ConfigurationSerializable} interface javadocs.
	 *
	 * @return Map containing the current state of this class
	 */
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> output = new LinkedHashMap<>();
		output.put("name", name);
		output.put("placementType", placementType.getName());
		output.put("schematicName", schematicName);
		output.put("spawnChance", spawnChance);
		output.put("spawnAttempts", spawnAttempts);

		return output;
	}
}
