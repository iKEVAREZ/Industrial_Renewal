package cassiokf.industrialrenewal.blocks;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BlockRoof extends BlockBase {

    public static final ImmutableList<IProperty<Boolean>> CONNECTED_PROPERTIES = ImmutableList.copyOf(
            Stream.of(EnumFacing.VALUES).map(facing -> PropertyBool.create(facing.getName())).collect(Collectors.toList()));
    protected static final AxisAlignedBB FULL_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(0.0D, 0.75D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB BOT_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D);

    public BlockRoof(String name, CreativeTabs tab) {
        super(Material.IRON, name, tab);
        setSoundType(SoundType.METAL);
        setHardness(0.8f);
        setLightOpacity(255);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, CONNECTED_PROPERTIES.toArray(new IProperty[CONNECTED_PROPERTIES.size()]));
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return getDefaultState();
    }

    @Override
    public int getMetaFromState(final IBlockState state) {
        return 0;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(final IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(final IBlockState state) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    private Boolean isEven(BlockPos pos) {
        Integer number = pos.getZ();
        if ((number % 2) == 0) {
            return true;
        } else {
            return false;
        }
    }

    protected boolean isValidConnection(final IBlockAccess world, final BlockPos ownPos, final EnumFacing neighbourDirection) {
        final BlockPos neighbourPos = ownPos.offset(neighbourDirection);
        final IBlockState neighbourState = world.getBlockState(neighbourPos);
        Block nb = neighbourState.getBlock();
        if ((neighbourDirection == EnumFacing.EAST || neighbourDirection == EnumFacing.WEST)) {
            IBlockState dState = world.getBlockState(ownPos.offset(EnumFacing.DOWN));
            IBlockState sState = world.getBlockState(ownPos.offset(EnumFacing.SOUTH));
            IBlockState nState = world.getBlockState(ownPos.offset(EnumFacing.NORTH));
            Block dBlock = dState.getBlock();
            Block sBlock = sState.getBlock();
            Block nBlock = nState.getBlock();
            if ((sBlock instanceof BlockRoof || sBlock instanceof BlockCatwalkLadder || sBlock.isFullCube(sState)) && (nBlock instanceof BlockRoof || nBlock instanceof BlockCatwalkLadder || nBlock.isFullCube(nState))) {
                // (block pos is Even) && (neighbour SW) && !down connection
                return isEven(ownPos)
                        && (nb instanceof BlockRoof || nb.isFullCube(neighbourState) || nb instanceof BlockPillar || nb instanceof BlockColumn)
                        && !dBlock.isFullCube(dState);
            }
        }
        return neighbourDirection == EnumFacing.DOWN && nb.isFullCube(neighbourState);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess world, final BlockPos pos) {
        for (final EnumFacing facing : EnumFacing.VALUES) {
            state = state.withProperty(CONNECTED_PROPERTIES.get(facing.getIndex()),
                    isValidConnection(world, pos, facing));
        }
        return state;
    }

    public final boolean isConnected(final IBlockState state, final EnumFacing facing) {
        return state.getValue(CONNECTED_PROPERTIES.get(facing.getIndex()));
    }

    @SuppressWarnings("deprecation")
    @Override
    public void addCollisionBoxToList(IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean isActualState) {
        IBlockState actualState = state.getActualState(worldIn, pos);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, BASE_AABB);
        if (isConnected(actualState, EnumFacing.DOWN) || isConnected(actualState, EnumFacing.EAST) || isConnected(actualState, EnumFacing.WEST)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, BOT_AABB);
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        IBlockState actualState = state.getActualState(source, pos);
        if (isConnected(actualState, EnumFacing.DOWN) || isConnected(actualState, EnumFacing.EAST) || isConnected(actualState, EnumFacing.WEST)) {
            return FULL_AABB;
        } else {
            return BASE_AABB;
        }
    }

    @Deprecated
    public boolean isTopSolid(IBlockState state) {
        return true;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        if (face == EnumFacing.UP) {
            return BlockFaceShape.SOLID;
        }
        return BlockFaceShape.UNDEFINED;
    }
}
