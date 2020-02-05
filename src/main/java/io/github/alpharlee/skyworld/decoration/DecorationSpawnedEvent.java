package io.github.alpharlee.skyworld.decoration;

import nl.rutgerkok.worldgeneratorapi.WorldRef;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DecorationSpawnedEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	private final WorldRef worldRef;
	private final DecorationData decorationData;

	public DecorationSpawnedEvent(WorldRef worldRef, DecorationData decorationData) {
		super(true);
		this.worldRef = worldRef;
		this.decorationData = decorationData;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public WorldRef getWorldRef() {
		return worldRef;
	}

	public DecorationData getDecorationData() {
		return decorationData;
	}

}
