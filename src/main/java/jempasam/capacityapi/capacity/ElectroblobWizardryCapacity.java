package jempasam.capacityapi.capacity;

import electroblob.wizardry.registry.Spells;
import electroblob.wizardry.spell.Mine;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import jempasam.data.loader.tags.Loadable;
import jempasam.data.loader.tags.LoadableParameter;
import net.minecraft.block.BlockStairs.EnumHalf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.DyeUtils;

@Loadable
public class ElectroblobWizardryCapacity implements Capacity{
	
	
	
	private Spell spell;
	
	
	
	
	@LoadableParameter(paramnames = "spell")
	public ElectroblobWizardryCapacity(String spellname) {
		super();
		this.spell = Spell.registry.getValue(new ResourceLocation(spellname));
	}
	
	
	
	@Override
	public int getColor(CapacityContext context) {
		String str=spell.getElement().getColour().getColor().toString();
		return Minecraft.getMinecraft().fontRenderer.getColorCode(str.charAt(str.length()-1));
	}
	
	@Override
	public int getMana(CapacityContext context) {
		return spell.getCost();
	}
	
	@Override
	public String getName(CapacityContext context) {
		return spell.getDisplayName();
	}
	
	@Override
	public boolean use(CapacityContext context) {
		SpellModifiers modif=new SpellModifiers();
		modif.amplified(SpellModifiers.CHARGEUP, context.getPower());
		if(context.getSender() instanceof EntityLiving && context.getTarget() instanceof EntityLivingBase)
			return spell.cast(context.getWorld(), (EntityLiving)context.getSender(), EnumHand.MAIN_HAND, 0, (EntityLivingBase)context.getTarget(), modif);
		else
			return false;
	}
}
