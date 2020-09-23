package entries.FlameMCExampleMod;

import com.tfc.API.flame.utils.logging.Logger;
import com.tfc.API.flame.utils.reflection.Fields;
import com.tfc.API.flamemc.Registry;
import com.tfc.API.flamemc.abstraction.CallInfo;
import com.tfc.API.flamemc.blocks.Block;
import com.tfc.API.flamemc.blocks.BlockProperties;
import com.tfc.API.flamemc.world.BlockPos;
import com.tfc.API.flamemc.world.World;
import entries.FlameAPI.Main;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class ExampleBlock extends Block {
	private final Registry.ResourceLocation location;
	
	public ExampleBlock(Registry.ResourceLocation location, BlockProperties properties) {
		super(location, properties);
		this.location = location;
	}
	
	public ExampleBlock(Registry.ResourceLocation thisBlock) {
		super(thisBlock);
		this.location = thisBlock;
	}
	
	@Override
	public void onRemoved(CallInfo arguments) {
		try {
			Logger.logLine(this.toString() + " REMOVED.");
		} catch (Throwable err) {
			try {
				File f = new File(Main.getExecDir() + "\\errors\\" + this.getClass().getName() + "\\err.txt");
				f.getParentFile().mkdirs();
				f.createNewFile();
				FileOutputStream stream = new FileOutputStream(f);
				StringBuilder s = new StringBuilder();
				Logger.logLine("\n\n");
				s.append("Flame encountered an error:\n");
				s.append(err.getClass().getName()).append(": ").append(err.getLocalizedMessage()).append("\n");
				for (StackTraceElement element : err.getStackTrace())
					s.append(element.toString()).append("\n");
				stream.write(s.toString().getBytes());
				stream.close();
			} catch (Throwable ignored) {
			}
		}
		super.onRemoved(arguments);
	}
	
	@Override
	public void onPlaced(CallInfo arguments) {
		try {
			if (!isCalled) {
				World world = arguments.get("world", World.class);
				BlockPos pos = arguments.get("pos", BlockPos.class);
				reverse(world, pos);
				isCalled = false;
			}
		} catch (Throwable err) {
			Logger.logErrFull(err);
			try {
				Fields.forEach(Class.forName("gg"), (m) -> {
					Logger.logLine(m.toString());
				});
			} catch (Throwable ignored) {
			}
			isCalled = false;
		}
	}
	
	@Override
	public void onUpdated(CallInfo arguments) {
		this.onPlaced(arguments);
	}
	
	private static boolean isCalled = false;
	
	public static void reverse(World world, BlockPos pos) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		try {
			if (!isCalled) {
				isCalled = true;
				ArrayList<BlockPos> posesDone = new ArrayList<>();
				for (int xO = -1; xO <= 1; xO++) {
					for (int yO = -1; yO <= 1; yO++) {
						if (!posesDone.contains(new BlockPos(Math.abs(xO), Math.abs(yO), Math.abs(0)))) {
							Object o1 = world.getBlockState(pos.offset(xO, yO, 0));
							Object o2 = world.getBlockState(pos.offset(-xO, -yO, -0));
							world.setBlockState(pos.offset(-xO, -yO, -0), o1);
							world.setBlockState(pos.offset(xO, yO, 0), o2);
							posesDone.add(new BlockPos(-xO, -yO, -0));
						}
					}
				}
				int xO = 1;
				int yO = -1;
				Object o1 = world.getBlockState(pos.offset(xO, yO, 0));
				Object o2 = world.getBlockState(pos.offset(-xO, -yO, -0));
				world.setBlockState(pos.offset(-xO, -yO, -0), o1);
				world.setBlockState(pos.offset(xO, yO, 0), o2);
			}
		} catch (Throwable ignored) {
			isCalled = false;
		}
	}
	
	@Override
	public String toString() {
		return "ExampleBlock{" + location.getNamespace() + "}";
	}
}
