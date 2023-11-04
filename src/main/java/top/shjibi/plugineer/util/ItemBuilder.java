package top.shjibi.plugineer.util;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * A utility class that helps you build items conveniently
 */
public final class ItemBuilder {

    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    private ItemBuilder(ItemStack item, ItemMeta meta) {
        itemStack = item;
        itemMeta = meta;
    }

    private ItemBuilder(Material material, int amount) {
        itemStack = new ItemStack(material, amount);
        itemMeta = itemStack.getItemMeta();
    }

    private ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        itemMeta = itemStack.getItemMeta();
    }

    /**
     * Gets an ItemBuilder with the given material and item amount
     *
     * @param material material to build with
     * @param amount   item amount to build with
     * @return An ItemBuilder
     */
    @NotNull
    public static ItemBuilder of(@NotNull Material material, int amount) {
        return new ItemBuilder(material, amount);
    }

    /**
     * Gets an ItemBuilder with the given material and 1 as item amount
     *
     * @param material The material to build with
     * @return An ItemBuilder
     */
    @NotNull
    public static ItemBuilder of(@NotNull Material material) {
        return of(material, 1);
    }

    /**
     * Gets an ItemBuilder with the given item stack
     *
     * @param itemStack The old ItemStack used for the final ItemStack
     * @return An ItemBuilder
     */
    @NotNull
    public static ItemBuilder of(@NotNull ItemStack itemStack) {
        return new ItemBuilder(itemStack);
    }

    /**
     * Sets the display name of the final item to the given name
     *
     * @param name name to build with
     * @return The modified {@link ItemBuilder}
     */
    @NotNull
    public ItemBuilder withDisplayName(@NotNull String name) {
        itemMeta.setDisplayName(name);
        return this;
    }

    /**
     * Sets the lore of the final item to the given lore
     *
     * @param lore lore to build with
     * @return The modified {@link ItemBuilder}
     */
    @NotNull
    public ItemBuilder withLore(String... lore) {
        itemMeta.setLore(Arrays.asList(lore));
        return this;
    }

    /**
     * Sets the lore of the final item to the given lore
     *
     * @param lore lore to build with
     * @return The modified {@link ItemBuilder}
     */
    @NotNull
    public ItemBuilder withLore(List<String> lore) {
        itemMeta.setLore(lore);
        return this;
    }

    /**
     * Adds an {@link AttributeModifier} to the final item
     *
     * @param attribute attribute to add
     * @param modifier  modifier to add
     * @return The modified {@link ItemBuilder}
     */
    @NotNull
    public ItemBuilder withAttributeModifier(@NotNull Attribute attribute, @NotNull AttributeModifier modifier) {
        itemMeta.addAttributeModifier(attribute, modifier);
        return this;
    }

    /**
     * Removes an {@link AttributeModifier} from the final item
     *
     * @param attribute The attribute to remove
     * @return The modified {@link ItemBuilder}
     */
    @NotNull
    public ItemBuilder withoutAttributeModifier(@NotNull Attribute attribute) {
        itemMeta.removeAttributeModifier(attribute);
        return this;
    }

    /**
     * Removes an {@link AttributeModifier} from the final item
     *
     * @param attribute The attribute to remove
     * @param modifier  The modifier to remove
     * @return The modified {@link ItemBuilder}
     */
    @NotNull
    public ItemBuilder withoutAttributeModifier(@NotNull Attribute attribute, @NotNull AttributeModifier modifier) {
        itemMeta.removeAttributeModifier(attribute, modifier);
        return this;
    }

    /**
     * Adds some flags to the final item
     *
     * @param flags flags to add
     * @return The modified {@link ItemBuilder}
     */
    @NotNull
    public ItemBuilder withItemFlags(@NotNull ItemFlag... flags) {
        itemMeta.addItemFlags(flags);
        return this;
    }

    /**
     * Removes some flags from the final item
     *
     * @param flags The flags to remove
     * @return The modified {@link ItemBuilder}
     */
    @NotNull
    public ItemBuilder withoutItemFlags(@NotNull ItemFlag... flags) {
        itemMeta.removeItemFlags(flags);
        return this;
    }

    /**
     * Adds an {@link Enchantment} to the final item
     *
     * @param enchantment            enchantment to add
     * @param level                  level of the enchantment
     * @param ignoreLevelRestriction Whether to ignore the level restriction
     * @return The modified {@link ItemBuilder}
     */
    @NotNull
    public ItemBuilder withEnchant(@NotNull Enchantment enchantment, int level, boolean ignoreLevelRestriction) {
        itemMeta.addEnchant(enchantment, level, ignoreLevelRestriction);
        return this;
    }

    /**
     * Removes an {@link Enchantment} from the final item
     *
     * @param enchantment The enchantment to remove
     * @return The modified {@link ItemBuilder}
     */
    @NotNull
    public ItemBuilder withoutEnchant(@NotNull Enchantment enchantment) {
        itemMeta.removeEnchant(enchantment);
        return this;
    }

    /**
     * Adds data to the final item
     *
     * @param key  key of data
     * @param type type of data
     * @param z    value of data
     * @return The modified {@link ItemBuilder}
     */
    @NotNull
    public <T, Z> ItemBuilder withPersistentData(NamespacedKey key, PersistentDataType<T, Z> type, Z z) {
        itemMeta.getPersistentDataContainer().set(key, type, z);
        return this;
    }

    /**
     * removes data from the final item
     *
     * @param key the key to remove
     * @return The modified {@link ItemBuilder}
     */
    @NotNull
    public ItemBuilder withoutPersistentData(NamespacedKey key) {
        itemMeta.getPersistentDataContainer().remove(key);
        return this;
    }

    /**
     * Gets the {@link ItemMeta} of the ItemBuilder
     *
     * @return The {@link ItemMeta}
     */
    @NotNull
    public ItemMeta getItemMeta() {
        return itemMeta;
    }

    /**
     * Builds the final {@link ItemStack}
     *
     * @return The final item
     */
    @NotNull
    public ItemStack build() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public String toString() {
        return "ItemBuilder {material: " + itemStack.getType() + ", amount: " + itemStack.getAmount() + ", itemMeta: " + itemMeta + "}";
    }
}
