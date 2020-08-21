package entries.FlameMCExampleMod;

import com.tfc.API.flamemc.Registry;
import com.tfc.API.flamemc.blocks.BlockInstancer;
import com.tfc.API.flamemc.blocks.BlockPropeteries;
import com.tfc.flame.IFlameMod;

public class Main implements IFlameMod {
	@Override
	public void preinit(String[] args) {
	}
	
	@Override
	public void postinit(String[] args) {
	}
	
	@Override
	public void init(String[] args) {
		//The name of the block
		Registry.ResourceLocation location = new Registry.ResourceLocation("flame_example_mod:test");
		//Register a generic block
		Registry.register(location, Registry.RegistryType.BLOCK, BlockInstancer.instanceBlock(new BlockPropeteries(location, Registry.get(Registry.RegistryType.BLOCK, new Registry.ResourceLocation("minecraft:stone")))));
	}
}
