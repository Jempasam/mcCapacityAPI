package jempasam.capacityapi.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.ai.EntityAITarget;

public class EntityAIOwnerProtection extends EntityAITarget {
    IEntityOwnable tameable;
    EntityLivingBase ennemy;
    private int timestamp;

    public EntityAIOwnerProtection(EntityCreature defending, IEntityOwnable theDefendingTameableIn)
    {
        super(defending, false);
        this.tameable = theDefendingTameableIn;
        this.setMutexBits(1);
    }

    public boolean shouldExecute()
    {
    	Entity owner=this.tameable.getOwner();
        if (owner==null || !(owner instanceof EntityLivingBase))  {
            return false;
        }
        else
        {
        	EntityLivingBase entitylivingbase = (EntityLivingBase)owner;
            int revengeTime = entitylivingbase.getRevengeTimer();
            int attackTime = entitylivingbase.getLastAttackedEntityTime();
            if(revengeTime>timestamp) {
            	this.ennemy = entitylivingbase.getRevengeTarget();
            	return this.isSuitableTarget(this.ennemy, false);
            }
            else if(attackTime>timestamp) {
            	this.ennemy = entitylivingbase.getLastAttackedEntity();
            	return this.isSuitableTarget(this.ennemy, false);
            }
            else return false;
        }
    }

    public void startExecuting()
    {
        this.taskOwner.setAttackTarget(this.ennemy);
        Entity entitylivingbase = this.tameable.getOwner();

        if (entitylivingbase != null && (entitylivingbase instanceof EntityLivingBase)){
            this.timestamp = Math.max(((EntityLivingBase)entitylivingbase).getRevengeTimer(), ((EntityLivingBase)entitylivingbase).getLastAttackedEntityTime());
        }

        super.startExecuting();
    }
}
