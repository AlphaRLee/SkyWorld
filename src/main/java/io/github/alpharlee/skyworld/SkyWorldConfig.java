package io.github.alpharlee.skyworld;

import io.github.alpharlee.skyworld.decoration.DecorationSettings;
import io.github.alpharlee.skyworld.decoration.PlacementType;
import nl.rutgerkok.worldgeneratorapi.WorldRef;
import nl.rutgerkok.worldgeneratorapi.property.FloatProperty;
import nl.rutgerkok.worldgeneratorapi.property.PropertyRegistry;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SkyWorldConfig {
	private Map<String, DecorationSettings> decorationSettingsMap;

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

	private final Map<String, FloatProperty> dynamicDecorationProperties;

	public SkyWorldConfig(Plugin plugin, PropertyRegistry registry) {
		decorationSettingsMap = new HashMap<>();

		// TODO Apparently the easiest way to read a list of objects from config is just to do unsafe casting. Any better ways?
		List<Map<String, Object>> rawDecorationSettings = readRawDecorationSettings();
		for (Map<String, Object> decorationMap : rawDecorationSettings) {
			DecorationSettings decorationSettings = new DecorationSettings(decorationMap);
			decorationSettingsMap.put(decorationSettings.name.toLowerCase(), decorationSettings);
		}

		landOctaves = registry.getFloat(new NamespacedKey(plugin, LAND_OCTAVES), 8);
		landScale = registry.getFloat(new NamespacedKey(plugin, LAND_SCALE), 0.004f);
		landYScale = registry.getFloat(new NamespacedKey(plugin, LAND_Y_SCALE), 0.004f);
		landFrequency = registry.getFloat(new NamespacedKey(plugin, LAND_FREQUENCY), 1.7f);
		landAmplitude = registry.getFloat(new NamespacedKey(plugin, LAND_AMPLITUDE), 0.7f);
		landThreshold = registry.getFloat(new NamespacedKey(plugin, LAND_THRESHOLD), 0.4f);

		dynamicDecorationProperties = new HashMap<>();
		for (DecorationSettings decorationSettings : decorationSettingsMap.values()) {
			String name = decorationSettings.name;
			setDynamicDecorationProperty(name + "." + "spawnChance", (float) decorationSettings.spawnChance, plugin, registry);
			setDynamicDecorationProperty(name + "." + "spawnAttempts", (float) decorationSettings.spawnAttempts, plugin, registry);
		}
	}

	private List<Map<String, Object>> readRawDecorationSettings() {
		return (List<Map<String, Object>>) SkyWorld.getInstance().getConfig().get("decorations");
	}

	private void writeRawDecorationSettings(List<Map<String, Object>> rawDecorationSettings) {
		SkyWorld.getInstance().getConfig().set("decorations", rawDecorationSettings);
		SkyWorld.getInstance().saveConfig();
	}

	private void setDynamicDecorationProperty(String key, float value, Plugin plugin, PropertyRegistry registry) {
		final FloatProperty valProperty = registry.getFloat(new NamespacedKey(plugin, key), value);
		dynamicDecorationProperties.put(key, valProperty);
	}

	public Map<String, DecorationSettings> getDecorationSettingsMap() {
		return decorationSettingsMap;
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

	public void addDefaultDecorationSetting(String name) {
		DecorationSettings settings = new DecorationSettings(name, PlacementType.AIR, "REPLACE_ME", 0.05, 5); // TODO validate these hardcoded default values
		decorationSettingsMap.put(name.toLowerCase(), settings);

		List<Map<String, Object>> rawDecorationSettings = readRawDecorationSettings();
		rawDecorationSettings.add(settings.serialize());
		writeRawDecorationSettings(rawDecorationSettings);

		SkyWorld.getInstance().saveConfig();
	}

	public void removeDecorationSettings(DecorationSettings settings) {
		decorationSettingsMap.remove(settings.name.toLowerCase());

		List<Map<String, Object>> rawDecorationSettings = readRawDecorationSettings();
		writeRawDecorationSettings(rawDecorationSettings.stream()
				.filter(e -> !e.get("name").equals(settings.name))
				.collect(Collectors.toList())
		);

		SkyWorld.getInstance().saveConfig();
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

	public final DecorationSettings getDynamicDecorationSettings(String name) {
		return decorationSettingsMap.get(name.toLowerCase());
	}

	public void setDynamicDecorationProperty(String name, String property, float value) {
		getDynamicDecorationProperty(name, property).setDefault(value);
		setDecorationConfigSetting(name, property, value);
	}

	public void setDecorationConfigSetting(String name, String property, Object value) {
		List<Map<String, Object>> rawDecorationSettings = readRawDecorationSettings();
		for (Map<String, Object> settings : rawDecorationSettings) {
			if (((String) settings.get("name")).equalsIgnoreCase(name)) {
				settings.put(property, value);
			}
		}

		writeRawDecorationSettings(rawDecorationSettings);
	}
}

