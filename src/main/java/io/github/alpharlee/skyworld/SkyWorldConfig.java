package io.github.alpharlee.skyworld;

import io.github.alpharlee.skyworld.decoration.DecorationData;
import io.github.alpharlee.skyworld.decoration.DecorationChunk;
import io.github.alpharlee.skyworld.decoration.DecorationSettings;
import nl.rutgerkok.worldgeneratorapi.WorldRef;
import nl.rutgerkok.worldgeneratorapi.property.FloatProperty;
import nl.rutgerkok.worldgeneratorapi.property.Property;
import nl.rutgerkok.worldgeneratorapi.property.PropertyRegistry;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	private final Property<Map<DecorationChunk, List<DecorationData>>> decorationTargets;

	private final Map<String, FloatProperty> dynamicDecorationProperties;

	public SkyWorldConfig(Plugin plugin, PropertyRegistry registry, List<DecorationSettings> decorationSettingsList) {
		landOctaves = registry.getFloat(new NamespacedKey(plugin, LAND_OCTAVES), 8);
		landScale = registry.getFloat(new NamespacedKey(plugin, LAND_SCALE), 0.004f);
		landYScale = registry.getFloat(new NamespacedKey(plugin, LAND_Y_SCALE), 0.004f);
		landFrequency = registry.getFloat(new NamespacedKey(plugin, LAND_FREQUENCY), 1.7f);
		landAmplitude = registry.getFloat(new NamespacedKey(plugin, LAND_AMPLITUDE), 0.7f);
		landThreshold = registry.getFloat(new NamespacedKey(plugin, LAND_THRESHOLD), 0.4f);

		decorationTargets = registry.getProperty(new NamespacedKey(plugin, "decorationTargets"), new HashMap<DecorationChunk, List<DecorationData>>());

		dynamicDecorationProperties = new HashMap<>();
		for (DecorationSettings decorationSettings : decorationSettingsList ) {
			String name = decorationSettings.name;
			setDynamicDecorationProperty(name + "." + "spawnChance", (float) decorationSettings.spawnChance, plugin, registry);
			setDynamicDecorationProperty(name + "." + "spawnAttempts", (float) decorationSettings.spawnAttempts, plugin, registry);
		}
	}

	private void setDynamicDecorationProperty(String key, float value, Plugin plugin, PropertyRegistry registry) {
		final FloatProperty valProperty = registry.getFloat(new NamespacedKey(plugin, key), value);
		dynamicDecorationProperties.put(key, valProperty);
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

	public final FloatProperty getDynamicDecorationProperty(String name, String property) {
		return dynamicDecorationProperties.get(name + "." + property);
	}

	public Property<Map<DecorationChunk, List<DecorationData>>> getDecorationTargets() {
		return decorationTargets;
	}
}

