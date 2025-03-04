package net.theratkking.villagercurrencymod.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.theratkking.villagercurrencymod.VillagerCurrencyMod;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, VillagerCurrencyMod.MOD_ID);

    public static final RegistryObject<Item> ALEXANDRITE = ITEMS.register("alexandrite", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAW_ALEXANDRITE = ITEMS.register("raw_alexandrite", () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
