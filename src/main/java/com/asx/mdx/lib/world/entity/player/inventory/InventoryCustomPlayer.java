package com.asx.mdx.lib.world.entity.player.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class InventoryCustomPlayer implements IInventory
{
    private final String NAME = "Inventory";
    private final String TAG_NAME = "InventoryNBTTagName";
    public static final int INV_SIZE = 2;
    private ItemStack[] inventory = new ItemStack[INV_SIZE];

    public InventoryCustomPlayer()
    {
        ;
    }

    @Override
    public int getSizeInventory()
    {
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
        return inventory[slot];
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount)
    {
        ItemStack stack = getStackInSlot(slot);
        if (stack != null)
        {
            if (stack.getCount() > amount)
            {
                stack = stack.splitStack(amount);
                this.onInventoryChanged();
            }
            else
            {
                setInventorySlotContents(slot, null);
            }
        }
        return stack;
    }

    @Override
    public ItemStack removeStackFromSlot(int slot)
    {
        ItemStack stack = getStackInSlot(slot);
        setInventorySlotContents(slot, null);
        return stack;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemstack)
    {
        this.inventory[slot] = itemstack;

        if (itemstack != null && itemstack.getCount() > this.getInventoryStackLimit())
        {
            itemstack.setCount(this.getInventoryStackLimit());
        }

        this.onInventoryChanged();
    }
    
    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }

    public void onInventoryChanged()
    {
        for (int i = 0; i < this.getSizeInventory(); ++i)
        {
            if (this.getStackInSlot(i) != null && this.getStackInSlot(i).getCount() == 0)
                this.setInventorySlotContents(i, null);
        }
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player)
    {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player)
    {
        ;
    }

    @Override
    public void closeInventory(EntityPlayer player)
    {
        ;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemstack)
    {
        return itemstack.getItem() instanceof Item;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        NBTTagList items = new NBTTagList();

        for (int i = 0; i < getSizeInventory(); ++i)
        {
            if (getStackInSlot(i) != null)
            {
                NBTTagCompound item = new NBTTagCompound();
                item.setByte("Slot", (byte) i);
                getStackInSlot(i).writeToNBT(item);
                items.appendTag(item);
            }
        }

        nbt.setTag(TAG_NAME, items);
        
        return nbt;
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        NBTTagList items = compound.getTagList(TAG_NAME, 0);

        for (int i = 0; i < items.tagCount(); ++i)
        {
            NBTTagCompound item = (NBTTagCompound) items.getCompoundTagAt(i);
            byte slot = item.getByte("Slot");

            if (slot >= 0 && slot < getSizeInventory())
            {
                setInventorySlotContents(slot, new ItemStack(item));
            }
        }
    }

    @Override
    public String getName()
    {
        return this.NAME;
    }

    @Override
    public boolean hasCustomName()
    {
        return false;
    }

    @Override
    public void markDirty()
    {
        ;
    }
    
    @Override
    public ITextComponent getDisplayName()
    {
        return new TextComponentString(this.NAME);
    }
    
    //TODO: Implement Unused Features
    @Override
    public int getField(int id)
    {
        return 0;
    }

    @Override
    public void setField(int id, int value)
    {
        ;
    }

    @Override
    public int getFieldCount()
    {
        return 0;
    }

    @Override
    public void clear()
    {
        ;
    }

    @Override
    public boolean isEmpty()
    {
        return false;
    }
}