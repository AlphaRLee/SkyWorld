package io.github.alpharlee.skyworld;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.mask.*;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.session.ClipboardHolder;
//import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BlockTypes;
import org.bukkit.World;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class DecorationManager {
	public void pasteSchematic(World world, String schematicName, int x, int y, int z) {
		com.sk89q.worldedit.world.World weWorld = BukkitAdapter.adapt(world);;

		String schematicsDirName = SkyWorld.getInstance().getDataFolder() + File.separator + "schematics" + File.separator;
		File schemFile = new File(schematicsDirName + schematicName + ".schem");

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
			AffineTransform rotationTransform = new AffineTransform();
			rotationTransform = rotationTransform.rotateY(Math.floor(Math.random() * 4) * 90);
 			ClipboardHolder rotatedHolder = new ClipboardHolder(clipboard);
			rotatedHolder.setTransform(rotatedHolder.getTransform().combine(rotationTransform));
			Operation operation = rotatedHolder
					.createPaste(editSession)
					.to(BlockVector3.at(x, y, z))
					.copyEntities(true)
					.maskSource(Masks.negate(new BlockTypeMask(clipboard, BlockTypes.STRUCTURE_VOID)))
					.build();
			Operations.complete(operation);
		} catch (WorldEditException e) {
			SkyWorld.getInstance().getLogger().warning("Error, SkyWorld hit the following WorldEditException: ");
			e.printStackTrace();
		}
	}
}
