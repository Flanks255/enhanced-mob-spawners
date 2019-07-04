package com.branders.spawnermod.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SpawnerKeyItem extends Item
{
	public SpawnerKeyItem(String name)
	{
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(CreativeTabs.TOOLS);
	}
	
	@Override
	public boolean hasEffect(ItemStack stack) 
	{
		return true;
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack) 
	{
		return EnumRarity.RARE;
	}
	
	@Override
	public int getItemStackLimit() 
	{
		return 1;
	}
}
