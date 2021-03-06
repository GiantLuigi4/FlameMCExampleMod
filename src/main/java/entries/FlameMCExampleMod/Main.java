package entries.FlameMCExampleMod;

import com.tfc.API.flame.FlameAPI;
import com.tfc.API.flamemc.blocks.BlockProperties;
import com.tfc.API.flamemc.event.init_steps.RegistryStep;
import com.tfc.API.flamemc.Registry;
import com.tfc.API.flamemc.items.Item;
import com.tfc.API.flamemc.items.ItemProperties;
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
		BlockProperties properties = new BlockProperties(
				new Registry.ResourceLocation("flameapi:test"),
				Registry.get(Registry.RegistryType.BLOCK, new Registry.ResourceLocation("minecraft:ice"))
		);
		Registry.RegistryObject<?> block = Registry.register(location, Registry.RegistryType.BLOCK, new ExampleBlock(location, properties).toRegisterable());
		//Register an item for the generic block
		Registry.registerBlockItem(location,block);
		//The name of the item
		location = new Registry.ResourceLocation("flame_example_mod:test_item");
		//Register a generic item
		Registry.register(location, Registry.RegistryType.ITEM, Item.instance(new ItemProperties(location,Registry.get(Registry.RegistryType.ITEM,new Registry.ResourceLocation("minecraft:diamond_sword")))));
	}
}
