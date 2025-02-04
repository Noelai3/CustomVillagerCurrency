package net.theratkking.villagercurrencymod.event;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.theratkking.villagercurrencymod.VillagerCurrencyMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = VillagerCurrencyMod.MOD_ID)
public class ModEvents {

    private static final Logger log = LogManager.getLogger(ModEvents.class);

    public class DummyTrader extends Villager {
        private DummyTrader(Level level) {
            super(EntityType.VILLAGER, level, VillagerType.PLAINS); // Fake trader
        }
    }

    public Item getNumismaticsItem() {
        if (isNumismaticsLoaded()) {
            return ForgeRegistries.ITEMS.getValue(new ResourceLocation("numismatics", "bevel"));
        }
        return null;
    }

    public boolean isNumismaticsLoaded() {
        return ModList.get().isLoaded("numismatics");
    }

    @SubscribeEvent
    public void  updateTrades(VillagerTradesEvent event) {
        if(event.getType() == VillagerProfession.FARMER) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            Item coin = getNumismaticsItem();
            if (coin != null) {
                // Use the item here (e.g., add it to a recipe)

                List<VillagerTrades.ItemListing> newTrades = new ArrayList<>();
                trades.forEach((level, tradesAtLevel) -> {
                    for (VillagerTrades.ItemListing trade : tradesAtLevel) {
                        newTrades.add(replaceEmeraldTrade(trade, coin));
                    }
                    tradesAtLevel.clear();
                    tradesAtLevel.addAll(newTrades);
                });
            }
        }

    }

    public static VillagerTrades.ItemListing replaceEmeraldTrade(VillagerTrades.ItemListing trade, Item coin) {
        return (trader, random) -> {
            MerchantOffer originalOffer = trade.getOffer(trader, random);
            if (originalOffer == null) return null;  // Skip invalid trades

            ItemStack costA = originalOffer.getBaseCostA();
            ItemStack costB = originalOffer.getCostB();
            boolean costAIsEmerald = costA.getItem() == Items.EMERALD;
            boolean costBIsEmerald = costB.getItem() == Items.EMERALD;

            if (!costAIsEmerald && !costBIsEmerald) {
                return originalOffer; // Keep trade unchanged if no emeralds
            }

            ItemStack newCostA = costAIsEmerald ? new ItemStack(coin, costA.getCount()) : costA;
            ItemStack newCostB = costBIsEmerald ? new ItemStack(coin, costB.getCount()) : costB;

            return new MerchantOffer(newCostA, newCostB, originalOffer.getResult(),
                    originalOffer.getMaxUses(), originalOffer.getXp(), originalOffer.getPriceMultiplier());
        };
    }
}
