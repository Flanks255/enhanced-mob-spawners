package com.branders.spawnermod.networking;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * 	Network message to handle communication from Client GUI to logical server in order to write
 * 	new NBT values for MobSpawnerBaseLogic.
 * 
 * 	@author Anders <Branders> Blomqvist
 */
public class MessageSyncSpawner implements IMessage
{
	private BlockPos pos;
	private short delay;
	private short minSpawnDelay;
	private short maxSpawnDelay;
	private short spawnCount;
	private short maxNearbyEntities;
	private short requiredPlayerRange;
	
	public MessageSyncSpawner() {}
	
	public MessageSyncSpawner(TileEntityMobSpawner tile, short delay, short spawnCount, short requiredPlayerRange, short maxNearbyEntities, short minSpawnDelay, short maxSpawnDelay)
	{
		this.pos = tile.getPos();
		this.delay = delay;
		this.minSpawnDelay = minSpawnDelay;
		this.maxSpawnDelay = maxSpawnDelay;
		this.spawnCount = spawnCount;
		this.maxNearbyEntities = maxNearbyEntities;
		this.requiredPlayerRange = requiredPlayerRange;
	}	
	
	@Override
	public void fromBytes(ByteBuf buf) 
	{
		this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		
		this.delay = buf.readShort();
		this.maxNearbyEntities = buf.readShort();
		this.maxSpawnDelay = buf.readShort();
		this.minSpawnDelay = buf.readShort();
		this.requiredPlayerRange = buf.readShort();
		this.spawnCount = buf.readShort();
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		buf.writeInt(this.pos.getX());
		buf.writeInt(this.pos.getY());
		buf.writeInt(this.pos.getZ());
		
		buf.writeShort(this.delay);
		buf.writeShort(this.maxNearbyEntities);
		buf.writeShort(this.maxSpawnDelay);
		buf.writeShort(this.minSpawnDelay);
		buf.writeShort(this.requiredPlayerRange);
		buf.writeShort(this.spawnCount);
	}
	
	public static class Handler implements IMessageHandler<MessageSyncSpawner, IMessage>
    {
        @Override
        public IMessage onMessage(MessageSyncSpawner msg, MessageContext ctx)
        {
            // Get world
        	World world = ctx.getServerHandler().player.getServerWorld();
        	
            if (world != null)
            {
            	// Leave if blockpos not loaded, safety measures
	    		if(!world.isBlockLoaded(msg.pos))
	    			return null;
	    		
	    		TileEntityMobSpawner spawner = (TileEntityMobSpawner)world.getTileEntity(msg.pos);
	        	MobSpawnerBaseLogic logic = spawner.getSpawnerBaseLogic();
	        	IBlockState iblockstate = world.getBlockState(msg.pos);	
	        	
	        	NBTTagCompound nbt = new NBTTagCompound();
	        	nbt = logic.writeToNBT(nbt);
	        	
	        	// Change NBT values
	        	nbt.setShort("Delay", msg.delay);
	        	nbt.setShort("SpawnCount", msg.spawnCount);
	        	nbt.setShort("RequiredPlayerRange", msg.requiredPlayerRange);
	        	nbt.setShort("MaxNearbyEntities", msg.maxNearbyEntities);
	        	nbt.setShort("MinSpawnDelay", msg.minSpawnDelay);
	        	nbt.setShort("MaxSpawnDelay", msg.maxSpawnDelay);
	        	
	        	// Update block
	        	logic.readFromNBT(nbt);
	        	spawner.markDirty();
	        	world.notifyBlockUpdate(msg.pos, iblockstate, iblockstate, 3);
            }

            return null;
        }
    }
}
