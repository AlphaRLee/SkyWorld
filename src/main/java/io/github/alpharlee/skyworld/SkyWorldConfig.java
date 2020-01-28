package io.github.alpharlee.skyworld;

import nl.rutgerkok.worldgeneratorapi.WorldRef;
import nl.rutgerkok.worldgeneratorapi.property.FloatProperty;
import nl.rutgerkok.worldgeneratorapi.property.PropertyRegistry;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class SkyWorldConfig {
	private static final String LAND_OCTAVES = "land.octaves";
	private static final String LAND_SCALE = "land.scale";
	private static final String LAND_Y_SCALE = "land.yScale";
	private static final String LAND_FREQUENCY = "land.frequency";
	private static final String LAND_AMPLITUDE = "land.amplitude";
	private static final String LAND_THRESHOLD = "land.threshold";

	private final FloatProperty landOctaves;
	private final FloatProperty landScale;
	private final FloatProperty landYScale;
	private final FloatProperty landFrequency;
	private final FloatProperty landAmplitude;
	private final FloatProperty landThreshold;

	public SkyWorldConfig(Plugin plugin, PropertyRegistry registry) {
		landOctaves = registry.getFloat(new NamespacedKey(plugin, LAND_OCTAVES), 8);
		landScale = registry.getFloat(new NamespacedKey(plugin, LAND_SCALE), 0.004f);
		landYScale = registry.getFloat(new NamespacedKey(plugin, LAND_Y_SCALE), 0.004f);
		landFrequency = registry.getFloat(new NamespacedKey(plugin, LAND_FREQUENCY), 1.7f);
		landAmplitude = registry.getFloat(new NamespacedKey(plugin, LAND_AMPLITUDE), 0.7f);
		landThreshold = registry.getFloat(new NamespacedKey(plugin, LAND_THRESHOLD), 0.4f);
	}



	public void readConfig(WorldRef worldRef, FileConfiguration fileConfiguration) {
		landOctaves.setWorldDefault(worldRef, fileConfiguration.getInt(LAND_OCTAVES, (int) landOctaves.get(worldRef)));
		landScale.setWorldDefault(worldRef,(float) fileConfiguration.getDouble(LAND_SCALE, landScale.get(worldRef)));
		landYScale.setWorldDefault(worldRef,(float) fileConfiguration.getDouble(LAND_Y_SCALE, landYScale.get(worldRef)));
		landFrequency.setWorldDefault(worldRef,(float) fileConfiguration.getDouble(LAND_FREQUENCY, landFrequency.get(worldRef)));
		landAmplitude.setWorldDefault(worldRef,(float) fileConfiguration.getDouble(LAND_AMPLITUDE, landAmplitude.get(worldRef)));
		landThreshold.setWorldDefault(worldRef,(float) fileConfiguration.getDouble(LAND_THRESHOLD, landThreshold.get(worldRef)));
	}

	public void writeConfig(WorldRef worldRef, FileConfiguration config) {
		config.set(LAND_OCTAVES, (int) landOctaves.get(worldRef));
		config.set(LAND_SCALE, landScale.get(worldRef));
		config.set(LAND_Y_SCALE, landYScale.get(worldRef));
		config.set(LAND_FREQUENCY, landFrequency.get(worldRef));
		config.set(LAND_AMPLITUDE, landAmplitude.get(worldRef));
		config.set(LAND_THRESHOLD, landThreshold.get(worldRef));
	}

	public final FloatProperty getLandOctaves() { return landOctaves; }
	public final FloatProperty getLandScale() { return landScale; }
	public final FloatProperty getLandYScale() { return landYScale; }
	public final FloatProperty getLandFrequency() { return landFrequency; }
	public final FloatProperty getLandAmplitude() { return landAmplitude; }
	public final FloatProperty getLandThreshold() { return landThreshold; }
}

