package mod.tropidragon.packapunch.mixin.tacz;

import java.util.Optional;
import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;

import mod.tropidragon.packapunch.common.Pap;
import mod.tropidragon.packapunch.common.internal.IMixinEntityKineticBullet;
import mod.tropidragon.packapunch.common.internal.IMixinModernKineticGunItem;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

import com.tacz.guns.api.item.IGun;
import com.tacz.guns.item.ModernKineticGunItem;
import com.tacz.guns.item.ModernKineticGunScriptAPI;
import com.tacz.guns.resource.index.CommonGunIndex;
import com.tacz.guns.resource.pojo.data.gun.BulletData;
import com.tacz.guns.resource.pojo.data.gun.GunData;

@Mixin(ModernKineticGunItem.class)
public class MixinModernKineticGunItem implements IMixinModernKineticGunItem {

    final String GUN_RARITY_TAG = "GunRarityLevel";
    final String GUN_PAP_TAG = "GunPaPLevel";

    @Override
    public int getRarityLevel(ItemStack gunItem) {
        CompoundTag nbt = gunItem.getOrCreateTag();
        if (nbt.contains(GUN_RARITY_TAG, Tag.TAG_INT)) {
            return nbt.getInt(GUN_RARITY_TAG);
        }
        return 0;
    }

    @Override
    public int getPaPLevel(ItemStack gunItem) {
        CompoundTag nbt = gunItem.getOrCreateTag();
        if (nbt.contains(GUN_PAP_TAG, Tag.TAG_INT)) {
            return nbt.getInt(GUN_PAP_TAG);
        }
        return 0;
    }

    @Override
    public void setRarityLevel(ItemStack gunItem, int level) {
        CompoundTag nbt = gunItem.getOrCreateTag();
        nbt.putInt(GUN_RARITY_TAG, level);
        gunItem.setTag(nbt);
    }

    @Override
    public void setPaPLevel(ItemStack gunItem, int level) {
        CompoundTag nbt = gunItem.getOrCreateTag();
        nbt.putInt(GUN_PAP_TAG, level);
        gunItem.setTag(nbt);
    }

    // @Inject(method =
    // "defaultTickReload(Lcom/tacz/guns/item/ModernKineticGunScriptAPI;)Lcom/tacz/guns/api/entity/ReloadState;",
    // at = @At(value = "INVOKE_ASSIGN", target =
    // "Lcom/tacz/guns/resource/pojo/data/gun/GunReloadData;getCooldown()Lcom/tacz/guns/resource/pojo/data/gun/GunReloadTime;",
    // remap = false), locals = LocalCapture.CAPTURE_FAILHARD, remap = false)
    // public void mixinDefaultTickReload(ModernKineticGunScriptAPI api,
    // CallbackInfo ci) {

    // }

    // "shoot(Lnet/minecraft/world/item/ItemStack;Ljava/util/function/Supplier;Ljava/util/function/Supplier;ZLnet/minecraft/world/entity/LivingEntity;)V"
    // 此处修改，可以影响到弹种的发射数等其他属性
    // 找到赋值语句之后的位置，插入代码，否则报错
    // @Inject(method = "shoot", at = @At(value = "INVOKE_ASSIGN",
    // target =
    // "Lcom/tacz/guns/resource/index/CommonGunIndex;getGunData()Lcom/tacz/guns/resource/pojo/data/gun/GunData;",
    // remap = false), locals = LocalCapture.CAPTURE_FAILHARD, remap = false)
    // public void mixinShoot(ItemStack gunItem, Supplier<Float> pitch,
    // Supplier<Float> yaw,
    // boolean tracer, LivingEntity shooter, CallbackInfo ci, @Local BulletData
    // bulletData) {

    // float _damageModifier = 1.0f;

    // IGun _iGun = IGun.getIGunOrNull(gunItem);
    // if (_iGun != null) {
    // _damageModifier = Pap.getDamageModifier(gunItem, _iGun);
    // }

    // ((IMixinBulletData) (Object)
    // bulletData).applyLeveledDamageModifier(_damageModifier);
    // }

    // 此处更改只能影响到生成的单个子弹的伤害
    // @Inject(method = "doSpawnBulletEntity", at = @At("HEAD"), remap = false)
    // public void modifyBulletDamage(Level world, LivingEntity shooter, ItemStack
    // gunItem,
    // float pitch, float yaw, float speed, float inaccuracy, ResourceLocation
    // ammoId,
    // ResourceLocation gunId, boolean tracer, GunData gunData, BulletData
    // bulletData,
    // CallbackInfo ci) {
    // float damageModifier = 1.0f;
    // IGun iGun = IGun.getIGunOrNull(gunItem);
    // if (iGun != null) {
    // damageModifier = Pap.getDamageModifier(gunItem, iGun);
    // }
    // ((IMixinBulletData) (Object)
    // bulletData).applyLeveledDamageModifier(damageModifier);
    // }
}
