package mod.tropidragon.packapunch.mixin.tacz;

import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.logging.LogUtils;
import com.tacz.guns.entity.EntityKineticBullet;
import com.tacz.guns.resource.pojo.data.gun.BulletData;
import com.tacz.guns.resource.pojo.data.gun.GunData;
import mod.tropidragon.packapunch.common.Pap;
import mod.tropidragon.packapunch.common.internal.IMixinEntityKineticBullet;

import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;

// 修改实例子弹的伤害
// modify bullet instance damage
@Mixin(EntityKineticBullet.class)
public class MixinEntityKineticBullet implements IMixinEntityKineticBullet {

    private float leveledDamageModifier;
    private static final Logger LOGGER = LogUtils.getLogger();

    // 创建子弹时，记录武器的伤害倍率
    // record weapon's damage multiplier when creating a bullet
    @Inject(method = "<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;ZLcom/tacz/guns/resource/pojo/data/gun/GunData;Lcom/tacz/guns/resource/pojo/data/gun/BulletData;)V", at = @At("TAIL"), remap = false)
    public void setLeveledDamageModifier(EntityType<? extends Projectile> type, Level worldIn, LivingEntity throwerIn,
            ItemStack gunItem,
            ResourceLocation ammoId, ResourceLocation gunId, ResourceLocation gunDisplayId, boolean isTracerAmmo,
            GunData gunData,
            BulletData bulletData, CallbackInfo ci) {

        this.leveledDamageModifier = Pap.getDamageModifier(gunItem);
        LOGGER.info("[TEST] leveledDamageModifier: {}", leveledDamageModifier);
    }

    // 结算伤害倍率
    // calculate damage multiplier
    @ModifyReturnValue(method = "getDamage", at = @At("RETURN"), remap = false)
    public float applyLeveledDamageModifier(float original) {
        LOGGER.info("[TEST] damage modified from {} to {} (multiplied by {})", original,
                original * leveledDamageModifier, leveledDamageModifier);
        return original * leveledDamageModifier;
    }
}
