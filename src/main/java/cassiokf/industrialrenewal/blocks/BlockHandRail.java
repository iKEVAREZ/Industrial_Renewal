package cassiokf.industrialrenewal.blocks;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockHandRail extends BlockBase {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool DOWN = PropertyBool.create("down");
    protected static final AxisAlignedBB RNORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.0625D);
    protected static final AxisAlignedBB RSOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.9375D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB RWEST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0625D, 1.0D, 1.0D);
    protected static final AxisAlignedBB REAST_AABB = new AxisAlignedBB(0.9375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.5D, 0.03125D);
    protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.96875D, 1.0D, 1.5D, 1.0D);
    protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.03125D, 1.5D, 1.0D);
    protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.96875D, 0.0D, 0.0D, 1.0D, 1.5D, 1.0D);

    public BlockHandRail(String name, CreativeTabs tab) {
        super(Material.IRON, name, tab);
        setHardness(0.8f);
    }

    private boolean downConnection(IBlockAccess world, BlockPos pos) {
        IBlockState downState = world.getBlockState(pos.down());
        return !downState.getBlock().isFullBlock(downState);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess world, final BlockPos pos) {
        return state.withProperty(DOWN, downConnection(world, pos));
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        EnumFacing face = state.getValue(FACING);
        if (face == EnumFacing.NORTH) {
            return RNORTH_AABB;
        }
        if (face == EnumFacing.SOUTH) {
            return RSOUTH_AABB;
        }
        if (face == EnumFacing.WEST) {
            return RWEST_AABB;
        }
        if (face == EnumFacing.EAST) {
            return REAST_AABB;
        }
        return RNORTH_AABB;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void addCollisionBoxToList(IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean isActualState) {
        IBlockState actualState = getActualState(state, worldIn, pos);
        EnumFacing face = state.getValue(FACING);
        if (face == EnumFacing.NORTH) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_AABB);
        } else if (face == EnumFacing.SOUTH) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_AABB);
        } else if (face == EnumFacing.WEST) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_AABB);
        } else if (face == EnumFacing.EAST) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_AABB);
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, DOWN);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex();
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    @Deprecated
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
}
