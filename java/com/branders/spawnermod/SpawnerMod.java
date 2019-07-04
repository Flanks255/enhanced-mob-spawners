package com.branders.spawnermod;

import com.branders.spawnermod.event.SpawnerEventHandler;
import com.branders.spawnermod.item.ItemsList;
import com.branders.spawnermod.item.SpawnerKeyItem;
import com.branders.spawnermod.networking.MessageSyncSpawner;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 	Small mod adding more functionality to Mob Spawners (Minecraft Forge 1.12.2)
 * 
 * 	@author Anders <Branders> Blomqvist
 *
 */
@Mod(modid = SpawnerMod.MODID, name = SpawnerMod.NAME, version = SpawnerMod.VERSION)
public class SpawnerMod
{
	/** Mod info */
    public static final String MODID = "spawnermod";
    public static final String NAME = "Enhanced Spawner Mod";
    public static final String VERSION = "1.0-1.12.2";
    
    public static final SimpleNetworkWrapper packetHandler = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
    
    /**
     * 	Register events
     */
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	// Register Spawner events
    	MinecraftForge.EVENT_BUS.register(new SpawnerEventHandler());
    	
    	// Set Mob Spawner block to a creative tab
    	Blocks.MOB_SPAWNER.setCreativeTab(CreativeTabs.DECORATIONS);
    }
    
    /**
     * 	Register network message
     */
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	int messageId = 0;
    	packetHandler.registerMessage(MessageSyncSpawner.Handler.class, MessageSyncSpawner.class, messageId++, Side.SERVER);
    }
    
    /**
     * 	Register Spawner Key item
     */
    @Mod.EventBusSubscriber(modid = MODID)
    public static class Registries
    {
    	@SubscribeEvent
    	public static void registerItems(RegistryEvent.Register<Item> event)
    	{
    		event.getRegistry().register(ItemsList.SPAWNER_KEY);
    	}
    	
    	@SideOnly(Side.CLIENT)
        @SubscribeEvent
        public static void registerModels(ModelRegistryEvent event) 
    	{
    		ModelLoader.setCustomModelResourceLocation(ItemsList.SPAWNER_KEY, 0, new ModelResourceLocation(ItemsList.SPAWNER_KEY.getRegistryName(), "inventory"));    
    	}
    }
}
