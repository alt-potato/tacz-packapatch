package mod.tropidragon.packapunch.mixin.tacz;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.tacz.guns.api.item.IGun;
import com.tacz.guns.client.tooltip.ClientGunTooltip;

import mod.tropidragon.packapunch.common.Pap;
import mod.tropidragon.packapunch.common.internal.IMixinModernKineticGunItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

@Mixin(ClientGunTooltip.class)
public class MixinClientGunTooltip {

    // 会改变已注册的方法？导致Unknown TooltipComponent 崩溃
    // public String[] papTierSymbol = { "", "I", "II", "III" };
    // public String[] rarityTierSymbol = { "C", "UC", "R", "E", "L" };

    private ChatFormatting getRarityColor(int level) {
        switch (level) {
            case 0:
                return ChatFormatting.GRAY;
            case 1:
                return ChatFormatting.GREEN;
            case 2:
                return ChatFormatting.AQUA;
            case 3:
                return ChatFormatting.LIGHT_PURPLE;
            case 4:
                return ChatFormatting.GOLD;

            default:
                return ChatFormatting.BLACK;
        }
    }

    @Shadow(remap = false)
    @Final
    private ItemStack gun;

    @Shadow(remap = false)
    @Final
    private IGun iGun;

    @Shadow(remap = false)
    private MutableComponent levelInfo;

    @Shadow(remap = false)
    private int maxWidth;

    // @Shadow
    // public abstract boolean shouldShow(GunTooltipPart part);

    // 覆写枪械等级，用于显示 稀有度 和 超改等级
    // @Inject(method = "getText", at = @At("TAIL"), remap = false)
    // public void overwriteLevelInfo() {
    // // if (this.shouldShow(GunTooltipPart.BASE_INFO)) {

    // }

    // ===1.0.2===
    // @ModifyExpressionValue(method = {"getText"},
    // at = @At(value = "INVOKE",
    // target =
    // "Lcom/tacz/guns/resource/pojo/data/gun/BulletData;getDamageAmount()F",
    // remap = false),
    // remap = false)
    // public float withLevelDamageModifier(float original) {
    // float damageModifier = Pap.getDamageModifier(gun, iGun);
    // return original * damageModifier;
    // }

    // ===1.0.3===
    // 显示计算过超改倍率的伤害
    @SuppressWarnings("null")
    @ModifyExpressionValue(method = "getText", at = @At(value = "INVOKE", target = "Lcom/tacz/guns/util/AttachmentDataUtils;getDamageWithAttachment(Lnet/minecraft/world/item/ItemStack;Lcom/tacz/guns/resource/pojo/data/gun/GunData;)D", remap = false), remap = false)
    public double applyLeveledDamageModifier(double original) {

        // 覆写枪械等级，用于显示 稀有度 和 超改等级
        // overrides weapon rarity, used to display rarity and upgrade level

        int papLevel = ((IMixinModernKineticGunItem) this.iGun).getPaPLevel(this.gun);
        int rarityLevel = ((IMixinModernKineticGunItem) this.iGun).getRarityLevel(this.gun);

        // String papTier = Pap.getPaPTierSymbol(papLevel);
        String papTier = String.format("[%s]", Pap.getPaPTierSymbol(papLevel));
        String rarityTier = Pap.getRarityTierSymbol(rarityLevel);

        this.levelInfo = (Component.translatable("tooltip.tacz.gun.level"))
                .append((Component.literal(rarityTier)).withStyle(getRarityColor(rarityLevel)))
                .append(" ")
                .append((Component.literal(papTier)).withStyle(ChatFormatting.WHITE));

        Font font = Minecraft.getInstance().font;
        this.maxWidth = Math.max(font.width(this.levelInfo), this.maxWidth);

        return original * (double) Pap.getDamageModifier(gun);
    }
}
