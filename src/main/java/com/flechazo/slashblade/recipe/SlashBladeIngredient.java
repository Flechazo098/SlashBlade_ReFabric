package com.flechazo.slashblade.recipe;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.flechazo.slashblade.registry.SlashBladeRegister;
import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredient;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class SlashBladeIngredient implements CustomIngredient {
    private final Set<Item> items;
    private final RequestDefinition request;

    protected SlashBladeIngredient(Set<Item> items, RequestDefinition request) {
        if (items.isEmpty()) {
            throw new IllegalArgumentException("Cannot create a SlashBladeIngredient with no items");
        }
        this.items = Collections.unmodifiableSet(items);
        this.request = request;
    }

    public static SlashBladeIngredient of(ItemLike item, RequestDefinition request) {
        return new SlashBladeIngredient(Set.of(item.asItem()), request);
    }

    public static SlashBladeIngredient of(RequestDefinition request) {
        return new SlashBladeIngredient(Set.of(SlashBladeRegister.SLASHBLADE), request);
    }

    @Override
    public boolean test(ItemStack input) {
        if (input == null || input.isEmpty())
            return false;
        return items.contains(input.getItem()) && this.request.test(input);
    }

    @Override
    public List<ItemStack> getMatchingStacks() {
        return items.stream().map(item -> {
            ItemStack stack = new ItemStack(item);
            // copy NBT to prevent the stack from modifying the original, as capabilities or
            // vanilla item durability will modify the tag
            request.initItemStack(stack);
            return stack;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean requiresTesting() {
        return true;
    }

    @Override
    public CustomIngredientSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    public static class Serializer implements CustomIngredientSerializer<SlashBladeIngredient> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation("slashbladerefabriced", "slashblade_ingredient");

        @Override
        public ResourceLocation getIdentifier() {
            return ID;
        }

        @Override
        public SlashBladeIngredient read(FriendlyByteBuf buffer) {
            Set<Item> items = Stream.generate(() -> buffer.readById(BuiltInRegistries.ITEM))
                    .limit(buffer.readVarInt()).collect(Collectors.toSet());
            RequestDefinition request = RequestDefinition.fromNetwork(buffer);
            return new SlashBladeIngredient(items, request);
        }

        @Override
        public SlashBladeIngredient read(JsonObject json) {
            // parse items
            Set<Item> items;
            if (json.has("item")) {
                ResourceLocation itemId = new ResourceLocation(GsonHelper.getAsString(json, "item"));
                Item item = BuiltInRegistries.ITEM.get(itemId);
                if (item == null) {
                    throw new JsonSyntaxException("Unknown item: " + itemId);
                }
                items = Set.of(item);
            } else if (json.has("items")) {
                ImmutableSet.Builder<Item> builder = ImmutableSet.builder();
                JsonArray itemArray = GsonHelper.getAsJsonArray(json, "items");
                for (int i = 0; i < itemArray.size(); i++) {
                    ResourceLocation itemId = new ResourceLocation(GsonHelper.convertToString(itemArray.get(i), "items[" + i + ']'));
                    Item item = BuiltInRegistries.ITEM.get(itemId);
                    if (item == null) {
                        throw new JsonSyntaxException("Unknown item: " + itemId);
                    }
                    builder.add(item);
                }
                items = builder.build();
            } else
                throw new JsonSyntaxException("Must set either 'item' or 'items'");
            var request = RequestDefinition.fromJSON(json.getAsJsonObject("request"));
            return new SlashBladeIngredient(items, request);
        }

        @Override
        public void write(JsonObject json, SlashBladeIngredient ingredient) {
            if (ingredient.items.size() == 1) {
                json.addProperty("item", BuiltInRegistries.ITEM.getKey(ingredient.items.iterator().next()).toString());
            } else {
                JsonArray items = new JsonArray();
                // ensure the order of items in the set is deterministic when saved to JSON
                ingredient.items.stream().map(BuiltInRegistries.ITEM::getKey).sorted()
                        .forEach(name -> items.add(name.toString()));
                json.add("items", items);
            }
            json.add("request", ingredient.request.toJson());
            json.addProperty("type", "slashbladerefabriced:slashblade_ingredient");
        }

        @Override
        public void write(FriendlyByteBuf buffer, SlashBladeIngredient ingredient) {
            buffer.writeVarInt(ingredient.items.size());
            for (Item item : ingredient.items)
                buffer.writeId(BuiltInRegistries.ITEM, item);
            ingredient.request.toNetwork(buffer);
        }
    }
}