package io.github.alpharlee.skyworld;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.mask.BlockMask;
import com.sk89q.worldedit.function.mask.BlockTypeMask;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BlockType;
import org.bukkit.Material;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SchematicsManager {
	void pasteSchematic(World weWorld, String schematicName, int x, int y, int z) {
		File schemFile = new File(SkyWorld.getInstance().getDataFolder() + File.separator + schematicName);

		ClipboardFormat format = ClipboardFormats.findByFile(schemFile);
		Clipboard clipboard = null;

		try (ClipboardReader reader = format.getReader(new FileInputStream(schemFile))) {
			clipboard = reader.read();
		} catch (FileNotFoundException e) {
			SkyWorld.getInstance().getLogger().warning("Error, SkyWorld could not find " + schematicName + " inside schematics folder!");
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (clipboard == null) {
			return;
		}

		try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(weWorld, -1)) {
			Operation operation = new ClipboardHolder(clipboard)
					.createPaste(editSession)
					.to(BlockVector3.at(x, y, z))
					.ignoreAirBlocks(true)
					.copyEntities(true)
					.maskSource(new BlockTypeMask(clipboard, new BlockType(Material.STRUCTURE_VOID.toString())))
					.build();
		}
	}
}
