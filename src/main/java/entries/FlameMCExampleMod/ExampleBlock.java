package entries.FlameMCExampleMod;

import com.tfc.API.flame.utils.logging.Logger;
import com.tfc.API.flame.utils.reflection.Fields;
import com.tfc.API.flame.utils.reflection.Methods;
import com.tfc.API.flamemc.Registry;
import com.tfc.API.flamemc.abstraction.CallInfo;
import com.tfc.API.flamemc.blocks.Block;
import com.tfc.API.flamemc.blocks.BlockProperties;
import com.tfc.API.flamemc.world.BlockPos;
import com.tfc.utils.ScanningUtils;
import entries.FlameAPI.Main;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

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
			Object world = arguments.get("world", Object.class);
			BlockPos pos = arguments.get("pos", BlockPos.class);
			reverse(world, pos);
		} catch (Throwable err) {
			Logger.logErrFull(err);
			try {
				Fields.forEach(Class.forName("gg"), (m) -> {
					Logger.logLine(m.toString());
				});
			} catch (Throwable ignored) {
			}
		}
	}
	
	public static void reverse(Object world, BlockPos pos) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Method getBlockState = world.getClass().getMethod("d_", Class.forName(ScanningUtils.toClassName(Main.getBlockPosClass())));
		Method setBlockState = world.getClass().getMethod("a",
				Class.forName(ScanningUtils.toClassName(Main.getBlockPosClass())),
				Class.forName(ScanningUtils.toClassName(Main.getBlockStateClass()))
		);
		ArrayList<BlockPos> posesDone = new ArrayList<>();
		for (int xO = -1; xO <= 1; xO++) {
			for (int yO = -1; yO <= 1; yO++) {
				if (!posesDone.contains(new BlockPos(Math.abs(xO), Math.abs(yO), Math.abs(0)))) {
					Object o1 = getBlockState.invoke(world, pos.offset(xO, yO, 0).unwrap());
					Object o2 = getBlockState.invoke(world, pos.offset(-xO, -yO, -0).unwrap());
					setBlockState.invoke(world, pos.offset(-xO, -yO, -0).unwrap(), o1);
					setBlockState.invoke(world, pos.offset(xO, yO, 0).unwrap(), o2);
					posesDone.add(new BlockPos(-xO, -yO, -0));
				}
			}
		}
		int xO = 1;
		int yO = -1;
		Object o1 = getBlockState.invoke(world, pos.offset(xO, yO, 0).unwrap());
		Object o2 = getBlockState.invoke(world, pos.offset(-xO, -yO, -0).unwrap());
		setBlockState.invoke(world, pos.offset(-xO, -yO, -0).unwrap(), o1);
		setBlockState.invoke(world, pos.offset(xO, yO, 0).unwrap(), o2);
	}
	
	@Override
	public String toString() {
		return "ExampleBlock{" + location.getNamespace() + "}";
	}
}
