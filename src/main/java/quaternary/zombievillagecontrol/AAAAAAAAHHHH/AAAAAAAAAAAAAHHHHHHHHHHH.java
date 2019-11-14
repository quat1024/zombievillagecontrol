package quaternary.zombievillagecontrol.AAAAAAAAHHHH;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import quaternary.zombievillagecontrol.ZombieVillageControl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

//I want to scream!!!!!!!!!!!!
public class AAAAAAAAAAAAAHHHHHHHHHHH implements IModGuiFactory {
	@Override
	public void initialize(Minecraft minecraftInstance) {
		
	}
	
	@Override
	public boolean hasConfigGui() {
		return true;
	}
	
	@Override
	public GuiScreen createConfigGui(GuiScreen parentScreen) {
		return new GuiConfig(parentScreen, getConfigElements(), ZombieVillageControl.MODID, false, false, ZombieVillageControl.NAME + " Config!");
	}
	
	//Adapted from Choonster's TestMod3. They say they adapted it from EnderIO "a while back".
	//http://www.minecraftforge.net/forum/topic/39880-110solved-make-config-options-show-up-in-gui/
	private static List<IConfigElement> getConfigElements() {
		Configuration c = ZombieVillageControl.config;
		//Don't look!
		return c.getCategoryNames().stream().filter(name -> !c.getCategory(name).isChild()).map(name -> new ConfigElement(c.getCategory(name).setLanguageKey(ZombieVillageControl.MODID + ".config." + name))).collect(Collectors.toList());
	}
	
	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}
}
