package com.robocraft999.compactexpansion.mixin;


import dev.compactmods.machines.dimension.VoidAirBlock;
import dev.compactmods.machines.room.data.CompactRoomData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VoidAirBlock.class)
public class VoidAirBlockMixin {
    @Inject(at=@At("HEAD"),method = "entityInside",remap = false,cancellable = true)
    private void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity, CallbackInfo ci) {
        if (pEntity.level() instanceof ServerLevel compactDim) {
                    final CompactRoomData intern = CompactRoomData.get(compactDim);
                    if(intern.streamRooms().anyMatch(room -> room.getRoomBounds().inflate(1).contains(pEntity.position()))){
                        BlockPos pos = new BlockPos(pEntity.getBlockX(),pEntity.getBlockY(),pEntity.getBlockZ());
                        pEntity.level().setBlock(pos,Blocks.AIR.defaultBlockState(),7);
                        ci.cancel();
                    }
        }

    }
}
