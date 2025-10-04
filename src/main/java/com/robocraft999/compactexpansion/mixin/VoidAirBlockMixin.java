package com.robocraft999.compactexpansion.mixin;


import dev.compactmods.machines.api.room.IRoomHistory;
import dev.compactmods.machines.config.ServerConfig;
import dev.compactmods.machines.dimension.VoidAirBlock;
import dev.compactmods.machines.room.RoomCapabilities;
import dev.compactmods.machines.room.data.CompactRoomData;
import dev.compactmods.machines.util.PlayerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;

@Pseudo
@Mixin(VoidAirBlock.class)
public class    VoidAirBlockMixin {
/*    @Inject(at=@At("HEAD"),method = "entityInside",cancellable = true)
    private void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity, CallbackInfo ci) {
        if (pEntity.level() instanceof ServerLevel compactDim) {
                    final CompactRoomData intern = CompactRoomData.get(compactDim);
                    if(intern.streamRooms().anyMatch(room -> room.getRoomBounds().inflate(1).contains(pEntity.position()))){
                        BlockPos pos = new BlockPos(pEntity.getBlockX(),pEntity.getBlockY(),pEntity.getBlockZ());
                        pEntity.level().setBlock(pos,Blocks.AIR.defaultBlockState(),7);
//                        BlockPos.betweenClosedStream(intern.streamRooms().forEach;)
                        ci.cancel();
                    }
        }

    }

 */
/**
 * @author
 * @reason
 */
@Overwrite
    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        if (!ServerConfig.isAllowedOutsideOfMachine()) {
            if (!pLevel.isClientSide) {
                if (pEntity instanceof ServerPlayer) {
                    ServerPlayer player = (ServerPlayer) pEntity;
                    if (player.isCreative()) {
                        return;
                    }
                    if (pEntity.level() instanceof ServerLevel compactDim) {
                        final CompactRoomData intern = CompactRoomData.get(compactDim);
                        if (intern.streamRooms().anyMatch(room -> room.getRoomBounds().inflate(1).contains(pEntity.position()))) {
                            BlockPos pos = new BlockPos(pEntity.getBlockX(), pEntity.getBlockY(), pEntity.getBlockZ());
                            pEntity.level().setBlock(pos, Blocks.AIR.defaultBlockState(), 7);
                            pEntity.level().setBlock(new BlockPos(pEntity.getBlockX(),pEntity.getBlockY()+1,pEntity.getBlockZ()), Blocks.AIR.defaultBlockState(), 7);
                        }

                        player.addEffect(new MobEffectInstance(MobEffects.POISON, 100));
                        player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100));
                        player.hurt(pLevel.damageSources().fellOutOfWorld(), 1.0F);
                        PlayerUtil.howDidYouGetThere(player);
                        player.getCapability(RoomCapabilities.ROOM_HISTORY).ifPresent(IRoomHistory::clear);
                        PlayerUtil.teleportPlayerToRespawnOrOverworld(player.server, player);
                    }
                }
            }
        }
    }
}
