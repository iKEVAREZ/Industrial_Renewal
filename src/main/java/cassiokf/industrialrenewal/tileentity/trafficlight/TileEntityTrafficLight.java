package cassiokf.industrialrenewal.tileentity.trafficlight;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class TileEntityTrafficLight extends TileEntity {

    public TileEntityTrafficLight() {
    }

    public int active() {
        IBlockState state = this.world.getBlockState(this.pos);
        BlockPos offsetPos = this.pos.offset(state.getValue(BlockTrafficLight.FACING));
        if (!state.getValue(BlockTrafficLight.ONWALL) && (this.world.isBlockPowered(this.pos) || this.world.isBlockPowered(this.pos.down()))) {
            int power = Math.max(this.world.getStrongPower(this.pos), this.world.getStrongPower(this.pos.down()));
            if (power > 3 && power < 10) {
                return 1;
            } else if (power >= 10) {
                return 2;
            }
        } else if (state.getValue(BlockTrafficLight.ONWALL) && (this.world.isBlockPowered(offsetPos) || this.world.isBlockPowered(this.pos))) {
            int power = Math.max(this.world.getStrongPower(offsetPos), this.world.getStrongPower(this.pos));
            if (power > 3 && power < 10) {
                return 1;
            } else if (power >= 10) {
                return 2;
            }
        }
        return 0;
    }
}
