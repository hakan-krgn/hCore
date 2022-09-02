package com.hakan.core.npc.builder;

import com.hakan.core.npc.HNPC;
import com.hakan.core.npc.HNPCHandler;
import com.hakan.core.skin.Skin;
import com.hakan.core.utils.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * HNpcBuilder class to build
 * NPCs' easily.
 */
public final class HNpcBuilder {

    private final String id;
    private Boolean show;
    private Skin skin;
    private Location location;
    private HNPC.LookTarget target;
    private Set<UUID> viewers;
    private List<String> lines;
    private Map<HNPC.EquipmentType, ItemStack> equipments;

    private long clickDelay;
    private Consumer<HNPC> spawnConsumer;
    private Consumer<HNPC> deleteConsumer;
    private BiConsumer<Player, HNPC.Action> clickConsumer;


    /**
     * Constructor to create builder.
     *
     * @param id NPC id.
     */
    public HNpcBuilder(@Nonnull String id) {
        this.id = Validate.notNull(id, "id cannot be null!");
        this.skin = Skin.STEVE;
        this.lines = new ArrayList<>();
        this.viewers = new HashSet<>();
        this.equipments = new HashMap<>();
        this.target = HNPC.LookTarget.CONSTANT;
        this.location = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
    }

    /**
     * Can everyone see this npc or not.
     *
     * @param show Show or not.
     * @return HNpcBuilder instance.
     */
    @Nonnull
    public HNpcBuilder showEveryone(boolean show) {
        this.show = show;
        return this;
    }

    /**
     * Set viewers of npc.
     *
     * @param viewers Viewers.
     * @return HNpcBuilder instance.
     */
    @Nonnull
    public HNpcBuilder viewers(@Nonnull UUID... viewers) {
        this.viewers = new HashSet<>(Arrays.asList(Validate.notNull(viewers, "viewers cannot be null!")));
        return this;
    }

    /**
     * Set viewers of npc.
     *
     * @param viewers Viewers.
     * @return HNpcBuilder instance.
     */
    @Nonnull
    public HNpcBuilder viewers(@Nonnull Set<UUID> viewers) {
        this.viewers = Validate.notNull(viewers, "viewers cannot be null!");
        return this;
    }

    /**
     * Adds viewers to npc.
     *
     * @param viewers Viewers.
     * @return HNpcBuilder instance.
     */
    @Nonnull
    public HNpcBuilder addViewers(@Nonnull UUID... viewers) {
        this.viewers.addAll(Arrays.asList(Validate.notNull(viewers, "viewers cannot be null!")));
        return this;
    }

    /**
     * Adds viewers to npc hologram.
     *
     * @param viewers Viewers.
     * @return HNpcBuilder instance.
     */
    @Nonnull
    public HNpcBuilder addViewers(@Nonnull Set<UUID> viewers) {
        this.viewers.addAll(Validate.notNull(viewers, "viewers cannot be null!"));
        return this;
    }

    /**
     * Sets lines of npc hologram.
     *
     * @param lines Lines.
     * @return HNpcBuilder instance.
     */
    @Nonnull
    public HNpcBuilder lines(@Nonnull List<String> lines) {
        this.lines = Validate.notNull(lines, "lines cannot be null!");
        return this;
    }

    /**
     * Sets lines of npc hologram.
     *
     * @param lines Lines.
     * @return HNpcBuilder instance.
     */
    @Nonnull
    public HNpcBuilder lines(@Nonnull String... lines) {
        this.lines = Arrays.asList(Validate.notNull(lines, "lines cannot be null!"));
        return this;
    }

    /**
     * Appends lines to npc hologram.
     *
     * @param lines Lines.
     * @return HNpcBuilder instance.
     */
    @Nonnull
    public HNpcBuilder appendLines(@Nonnull List<String> lines) {
        this.lines.addAll(Validate.notNull(lines, "lines cannot be null!"));
        return this;
    }

    /**
     * Appends lines to npc hologram.
     *
     * @param lines Lines.
     * @return HNpcBuilder instance.
     */
    @Nonnull
    public HNpcBuilder appendLines(@Nonnull String... lines) {
        this.lines.addAll(Arrays.asList(Validate.notNull(lines, "lines cannot be null!")));
        return this;
    }

    /**
     * Sets location of npc.
     *
     * @param location Location.
     * @return HNpcBuilder instance.
     */
    @Nonnull
    public HNpcBuilder location(@Nonnull Location location) {
        this.location = Validate.notNull(location, "location cannot be null!");
        return this;
    }

    /**
     * Sets aim type of npc.
     *
     * @param target Aim type.
     * @return HNpcBuilder instance.
     */
    @Nonnull
    public HNpcBuilder target(@Nonnull HNPC.LookTarget target) {
        this.target = Validate.notNull(target, "target cannot be null!");
        return this;
    }

    /**
     * Sets click action of npc.
     *
     * @param action action on click.
     * @return HNpcBuilder instance.
     */
    @Nonnull
    public HNpcBuilder whenClicked(@Nonnull BiConsumer<Player, HNPC.Action> action) {
        return this.whenClicked(action, 20);
    }

    /**
     * Sets click action of npc.
     *
     * @param action action on click.
     * @param delay  delay between clicks.
     * @return HNpcBuilder instance.
     */
    @Nonnull
    public HNpcBuilder whenClicked(@Nonnull BiConsumer<Player, HNPC.Action> action, long delay) {
        this.clickConsumer = Validate.notNull(action, "action cannot be null!");
        this.clickDelay = Math.max(0, delay);
        return this;
    }

    /**
     * Sets click action of npc.
     *
     * @param action action on spawn.
     * @return HNpcBuilder instance.
     */
    @Nonnull
    public HNpcBuilder whenSpawned(@Nonnull Consumer<HNPC> action) {
        this.spawnConsumer = Validate.notNull(action, "action cannot be null!");
        return this;
    }

    /**
     * Sets click action of npc.
     *
     * @param action action on delete.
     * @return HNpcBuilder instance.
     */
    @Nonnull
    public HNpcBuilder whenDeleted(@Nonnull Consumer<HNPC> action) {
        this.deleteConsumer = Validate.notNull(action, "action cannot be null!");
        return this;
    }

    /**
     * Sets skin of npc.
     *
     * @param skin Skin.
     * @return HNpcBuilder instance.
     */
    @Nonnull
    public HNpcBuilder skin(@Nonnull Skin skin) {
        this.skin = Validate.notNull(skin, "skin cannot be null!");
        return this;
    }

    /**
     * Sets skin of npc.
     *
     * @param skin Skin.
     * @return HNpcBuilder instance.
     */
    @Nonnull
    public HNpcBuilder skin(@Nonnull String skin) {
        return this.skin(Skin.from(skin));
    }

    /**
     * Sets equipment of npc.
     *
     * @param type Equipment type.
     * @param item Item.
     * @return HNpcBuilder instance.
     */
    @Nonnull
    public HNpcBuilder equipment(@Nonnull HNPC.EquipmentType type, @Nonnull ItemStack item) {
        this.equipments.put(Validate.notNull(type, "type cannot be null!"),
                Validate.notNull(item, "item cannot be null!"));
        return this;
    }

    /**
     * Sets equipments of npc.
     *
     * @param equipments Equipments.
     * @return HNpcBuilder instance.
     */
    @Nonnull
    public HNpcBuilder equipments(@Nonnull Map<HNPC.EquipmentType, ItemStack> equipments) {
        this.equipments = Validate.notNull(equipments, "equipments cannot be null!");
        return this;
    }

    /**
     * Builds npc but if there is npc
     * with this id, removes it.
     *
     * @return HNPC instance.
     */
    @Nonnull
    public HNPC forceBuild() {
        HNPCHandler.findByID(this.id).ifPresent(HNPC::delete);
        return this.build();
    }

    /**
     * Builds npc.
     *
     * @return HNPC instance.
     */
    @Nonnull
    public HNPC build() {
        Validate.isTrue(HNPCHandler.has(this.id), "npc with id(" + this.id + ") already exists!");


        if (this.show == null)
            this.show = (this.viewers.size() > 0);

        HNPC npc = new HNPC(this.id, this.location, this.skin, this.target, this.lines, this.viewers, this.equipments, this.show);
        if (this.clickConsumer != null)
            npc.whenClicked(this.clickConsumer);
        if (this.spawnConsumer != null)
            npc.whenSpawned(this.spawnConsumer);
        if (this.deleteConsumer != null)
            npc.whenDeleted(this.deleteConsumer);
        npc.getAction().setClickDelay(this.clickDelay);

        HNPCHandler.getContent().put(this.id, npc);
        return npc;
    }
}