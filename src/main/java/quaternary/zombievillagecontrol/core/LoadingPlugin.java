package quaternary.zombievillagecontrol.core;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import javax.annotation.Nullable;
import java.util.Map;

@IFMLLoadingPlugin.Name("ZombieVillageTweakCore")
@IFMLLoadingPlugin.TransformerExclusions("quaternary.zombievillagecontrol")
@IFMLLoadingPlugin.SortingIndex(1337)
@IFMLLoadingPlugin.MCVersion("1.12.2")
public class LoadingPlugin implements IFMLLoadingPlugin {
	@Override
	public String[] getASMTransformerClass() {
		return new String[] {
			"quaternary.zombievillagecontrol.core.ClassTransformer"
		};
	}
	
	@Override
	public String getModContainerClass() {
		return null;
	}
	
	@Nullable
	@Override
	public String getSetupClass() {
		return null;
	}
	
	@Override
	public void injectData(Map<String, Object> data) {
		
	}
	
	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}
