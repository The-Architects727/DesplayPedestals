package net.architects.itemdisplay;

import net.architects.itemdisplay.blocks.*;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Function;
import java.util.function.Supplier;

import static net.architects.itemdisplay.ItemDisplay.RegistryEvents.*;
import static net.minecraft.world.level.block.Blocks.*;
import static net.minecraft.world.level.block.state.BlockBehaviour.Properties.copy;
import static net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.MOD;

@Mod("itemdisplay")
public class ItemDisplay {
    public static final String MODID = "itemdisplay";

    public static final Logger LOGGER = LogManager.getLogger(MODID);
    IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID);

    public ItemDisplay() {
        MinecraftForge.EVENT_BUS.register(this);
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());


        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
//        this.setRenderLayer(SWORD_CASE, WOODEN_SWORD_CASE, DARK_OAK_SWORD_CASE, BIRCH_SWORD_CASE, ACACIA_SWORD_CASE, JUNGLE_SWORD_CASE, SPRUCE_SWORD_CASE, PRISMARINE_SWORD_CASE, IRON_SWORD_CASE, GOLDEN_SWORD_CASE, DIAMOND_SWORD_CASE, EMERALD_SWORD_CASE);

        BlockEntityRenderers.register(ALTAR_DISPLAY_TYPE.get(), TESRAltarDisplay::new);
        BlockEntityRenderers.register(ITEM_DISPLAY_TYPE.get(), TESRItemDisplay::new);
        BlockEntityRenderers.register(INDESTRUCTABLE_ALTAR_DISPLAY_TYPE.get(), TESRIndestructableAltarDisplay::new);


    }

//    @SafeVarargs
//    private void setRenderLayer(RegistryObject<Block>... blocks) {
//        Arrays.stream(blocks).forEach(block -> ItemBlockRenderTypes.setRenderLayer(block.get(), RenderType.cutout()));
//    }

    @Mod.EventBusSubscriber(modid = MODID, bus = MOD)
    public static class RegistryEvents {


        //region Normal Sword Displays
        public static final RegistryObject<Block> ITEM_DISPLAY = registerBlockWithItem("item_display", () -> new ItemDisplayBlock(copy(STONE)));
        public static final RegistryObject<Block> ALTAR_DISPLAY = registerBlockWithItem("altar_display", () -> new AltarDisplayBlock(copy(STONE)));
        public static final RegistryObject<Block> INDESTRUCTABLE_ALTAR_DISPLAY = registerBlockWithItem("indestructable_altar_display", () -> new IndestructibleAltarDisplayBlock(copy(STONE)));
        //endregion

        //endregion
        public static <BLOCK extends Block> RegistryObject<BLOCK> registerBlockWithItem(String name, DeferredRegister<Block> blocks, DeferredRegister<Item> items, Supplier<BLOCK> blockSupplier, Function<BLOCK, Item> itemFactory) {
            RegistryObject<BLOCK> block = blocks.register(name, blockSupplier);
            items.register(name, () -> itemFactory.apply(block.get()));
            return block;
        }

        public static <BLOCK extends Block> RegistryObject<BLOCK> registerBlockWithItem(String name, Supplier<BLOCK> blockSupplier, Function<BLOCK, Item> itemFactory) {
            return registerBlockWithItem(name, BLOCKS, ITEMS, blockSupplier, itemFactory);
        }

        public static <BLOCK extends Block> RegistryObject<BLOCK> registerBlockWithItem(String name, Supplier<BLOCK> blockSupplier) {
            return registerBlockWithItem(name, BLOCKS, ITEMS, blockSupplier, SDBlockItem::new);
        }

        public static final RegistryObject<BlockEntityType<ItemDisplayTile>> ITEM_DISPLAY_TYPE = TILE_ENTITIES.register("item_display", () -> build(BlockEntityType.Builder.of(ItemDisplayTile::new, ITEM_DISPLAY.get())));
        public static final RegistryObject<BlockEntityType<AltarDisplayTile>> ALTAR_DISPLAY_TYPE = TILE_ENTITIES.register("altar_display", () -> build(BlockEntityType.Builder.of(AltarDisplayTile::new, ALTAR_DISPLAY.get())));
        public static final RegistryObject<BlockEntityType<IndestructableAltarDisplayTile>> INDESTRUCTABLE_ALTAR_DISPLAY_TYPE = TILE_ENTITIES.register("indestructable_altar_display", () -> build(BlockEntityType.Builder.of(IndestructableAltarDisplayTile::new, INDESTRUCTABLE_ALTAR_DISPLAY.get())));

        private static <T extends BlockEntity> BlockEntityType<T> build(BlockEntityType.Builder<T> builder) {
            return builder.build(null);
        }

        public static CreativeModeTab SD_GROUP;



//        @SubscribeEvent
//
//        public static void registerTabs(CreativeModeTabEvent.Register event) {
//            SD_GROUP = event.registerCreativeModeTab(new ResourceLocation(MODID, "core"), builder -> builder.icon(() -> new ItemStack(ITEM_DISPLAY.get()))
//                    .title(Component.translatable("tabs.itemdisplay.core"))
//                    .withBackgroundLocation(new ResourceLocation(MODID, "textures/gui/container/creative_inventory/tab_item_displays.png"))
//                    .withLabelColor(0xFFFFFF)
//                    .withSearchBar(40)
//                    .displayItems((featureFlags, output, hasOp) -> {
//                        output.accept(ITEM_DISPLAY.get());
//                        output.accept(SWORD_CASE.get());
//                        output.accept(WOODEN_ITEM_DISPLAY.get());
//                        output.accept(WOODEN_SWORD_CASE.get());
//                        output.accept(DARK_OAK_ITEM_DISPLAY.get());
//                        output.accept(DARK_OAK_SWORD_CASE.get());
//                        output.accept(BIRCH_ITEM_DISPLAY.get());
//                        output.accept(BIRCH_SWORD_CASE.get());
//                        output.accept(ACACIA_ITEM_DISPLAY.get());
//                        output.accept(ACACIA_SWORD_CASE.get());
//                        output.accept(JUNGLE_ITEM_DISPLAY.get());
//                        output.accept(JUNGLE_SWORD_CASE.get());
//                        output.accept(SPRUCE_ITEM_DISPLAY.get());
//                        output.accept(SPRUCE_SWORD_CASE.get());
//                        output.accept(PRISMARINE_ITEM_DISPLAY.get());
//                        output.accept(PRISMARINE_SWORD_CASE.get());
//                        output.accept(IRON_ITEM_DISPLAY.get());
//                        output.accept(IRON_SWORD_CASE.get());
//                        output.accept(GOLDEN_ITEM_DISPLAY.get());
//                        output.accept(GOLDEN_SWORD_CASE.get());
//                        output.accept(DIAMOND_ITEM_DISPLAY.get());
//                        output.accept(DIAMOND_SWORD_CASE.get());
//                        output.accept(EMERALD_ITEM_DISPLAY.get());
//                        output.accept(EMERALD_SWORD_CASE.get());
//                    }));
//        }

    }
}
