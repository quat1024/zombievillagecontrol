package quaternary.zombievillagecontrol;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(
	modid = ZombieVillageControl.MODID,
	name = ZombieVillageControl.NAME,
	version = ZombieVillageControl.VERSION,
	acceptableRemoteVersions = "*",
	guiFactory = "quaternary.zombievillagecontrol.AAAAAAAAHHHH.AAAAAAAAAAAAAHHHHHHHHHHH"
)
@Mod.EventBusSubscriber
public class ZombieVillageControl {
	public static final String MODID = "zombievillagecontrol";
	public static final String NAME = "Zombie Village Control";
	public static final String VERSION = "GRADLE:VERSION";
	
	public static Configuration config;
	
	@Mod.EventHandler
	public static void preinit(FMLPreInitializationEvent e) {
		config = new Configuration(e.getSuggestedConfigurationFile());
		
		loadConfig();
	}
	
	public static void loadConfig() {
		Hooks.zombieVillageChance = config.getInt(
			"zombieVillageRarity",
			"general",
			50,
			1,
			Integer.MAX_VALUE,
			"The chance a village will be a zombie village. This is taken as a '1/n' chance, so a value of 50 means that 1 out of every 50 villages will be a zombie village. A value of 1 means every village will be a zombie village."
		);
		
		Hooks.flipChance = config.getBoolean(
			"flipChance",
			"general",
			false,
			"If this is 'true', the zombieVillageRarity option is treated instead a chance to *not* be a zombie village. For example, if this is 'true' and zombieVillageRarity is 50, 49 out of every 50 villages will be zombie villages.\n\nSetting this to 'true' and setting zombieVillageRarity to 1 will disable zombie villages."
		);
		
		Hooks.shouldBurnInDay = config.getBoolean(
			"shouldBurnInDay",
			"tweaks",
			true,
			"If this is 'false', zombie villagers will not burn in the daylight, fixing a Mojang oversight that makes them basically despawn instantly. Note that this (currently? send me a PR ;) ) affects *all* zombie villagers, since I can't tell if they were natural village spawns or not, which is why its effects are disabled by default."
		);
		
		config.addCustomCategoryComment("general", "Any changes made here only affect newly generated villages.");
		
		if(config.hasChanged()) config.save();
	}
	
	@SubscribeEvent
	public static void config(ConfigChangedEvent.OnConfigChangedEvent e) {
		if(e.getModID().equals(MODID)) {
			loadConfig();
		}
	}
}
