package entries.FlameMCExampleMod;

import com.tfc.API.flame.FlameAPI;
import com.tfc.API.flamemc.event.init_steps.RegistryStep;
import com.tfc.API.flamemc.Registry;
import com.tfc.API.flamemc.blocks.BlockInstancer;
import com.tfc.API.flamemc.blocks.BlockPropeteries;
import com.tfc.flame.IFlameMod;

public class Main implements IFlameMod {
	@Override
	public void preinit(String[] args) {
		FlameAPI.instance.bus.register(RegistryStep.class, this::doRegistry);
	}
	
	@Override
	public void postinit(String[] args) {
	}
	
	@Override
	public void init(String[] args) {
	}
	
	public void doRegistry(RegistryStep event) {
		//The name of the block
		Registry.ResourceLocation location = new Registry.ResourceLocation("flame_example_mod:test");
		//Register a generic block
		Registry.RegistryObject<?> block = Registry.register(location, Registry.RegistryType.BLOCK, BlockInstancer.instanceBlock(new BlockPropeteries(location, Registry.get(Registry.RegistryType.BLOCK, new Registry.ResourceLocation("minecraft:stone")))));
		//Register an item for the generic block
		Registry.registerBlockItem(location,block);
	}
}
