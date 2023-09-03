package com.hakan.core.npc.builder;

import com.hakan.core.npc.Npc;
import com.hakan.core.npc.NpcHandler;
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
 * NpcBuilder class to build
 * NPCs' easily.
 */
public final class NpcBuilder {

    private final String id;
    private Boolean show;
    private Skin skin;
    private Location location;
    private Npc.LookTarget target;
    private Set<UUID> viewers;
    private List<String> lines;
    private Map<Npc.EquipmentType, ItemStack> equipments;

    private long clickDelay;
    private Consumer<Npc> spawnConsumer;
    private Consumer<Npc> deleteConsumer;
    private BiConsumer<Player, Npc.Action> clickConsumer;


    /**
     * Constructor to create builder.
     *
     * @param id NPC id.
     */
    public NpcBuilder(@Nonnull String id) {
        this.id = Validate.notNull(id, "id cannot be null!");
        this.skin = Skin.STEVE;
        this.lines = new ArrayList<>();
        this.viewers = new HashSet<>();
        this.equipments = new HashMap<>();
        this.target = Npc.LookTarget.CONSTANT;
        this.location = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
        this.clickDelay = 0;
    }

    /**
     * Can everyone see this npc or not.
     *
     * @param show Show or not.
     * @return NpcBuilder instance.
     */
    @Nonnull
    public NpcBuilder showEveryone(boolean show) {
        this.show = show;
        return this;
    }

    /**
     * Set viewers of npc.
     *
     * @param viewers Viewers.
     * @return NpcBuilder instance.
     */
    @Nonnull
    public NpcBuilder viewers(@Nonnull UUID... viewers) {
        this.viewers = new HashSet<>(Arrays.asList(Validate.notNull(viewers, "viewers cannot be null!")));
        return this;
    }

    /**
     * Set viewers of npc.
     *
     * @param viewers Viewers.
     * @return NpcBuilder instance.
     */
    @Nonnull
    public NpcBuilder viewers(@Nonnull Set<UUID> viewers) {
        this.viewers = Validate.notNull(viewers, "viewers cannot be null!");
        return this;
    }

    /**
     * Adds viewers to npc.
     *
     * @param viewers Viewers.
     * @return NpcBuilder instance.
     */
    @Nonnull
    public NpcBuilder addViewers(@Nonnull UUID... viewers) {
        this.viewers.addAll(Arrays.asList(Validate.notNull(viewers, "viewers cannot be null!")));
        return this;
    }

    /**
     * Adds viewers to npc hologram.
     *
     * @param viewers Viewers.
     * @return NpcBuilder instance.
     */
    @Nonnull
    public NpcBuilder addViewers(@Nonnull Set<UUID> viewers) {
        this.viewers.addAll(Validate.notNull(viewers, "viewers cannot be null!"));
        return this;
    }

    /**
     * Sets lines of npc hologram.
     *
     * @param lines Lines.
     * @return NpcBuilder instance.
     */
    @Nonnull
    public NpcBuilder lines(@Nonnull List<String> lines) {
        this.lines = Validate.notNull(lines, "lines cannot be null!");
        return this;
    }

    /**
     * Sets lines of npc hologram.
     *
     * @param lines Lines.
     * @return NpcBuilder instance.
     */
    @Nonnull
    public NpcBuilder lines(@Nonnull String... lines) {
        this.lines = Arrays.asList(Validate.notNull(lines, "lines cannot be null!"));
        return this;
    }

    /**
     * Appends lines to npc hologram.
     *
     * @param lines Lines.
     * @return NpcBuilder instance.
     */
    @Nonnull
    public NpcBuilder appendLines(@Nonnull List<String> lines) {
        this.lines.addAll(Validate.notNull(lines, "lines cannot be null!"));
        return this;
    }

    /**
     * Appends lines to npc hologram.
     *
     * @param lines Lines.
     * @return NpcBuilder instance.
     */
    @Nonnull
    public NpcBuilder appendLines(@Nonnull String... lines) {
        this.lines.addAll(Arrays.asList(Validate.notNull(lines, "lines cannot be null!")));
        return this;
    }

    /**
     * Sets location of npc.
     *
     * @param location Location.
     * @return NpcBuilder instance.
     */
    @Nonnull
    public NpcBuilder location(@Nonnull Location location) {
        this.location = Validate.notNull(location, "location cannot be null!");
        return this;
    }

    /**
     * Sets aim type of npc.
     *
     * @param target Aim type.
     * @return NpcBuilder instance.
     */
    @Nonnull
    public NpcBuilder target(@Nonnull Npc.LookTarget target) {
        this.target = Validate.notNull(target, "target cannot be null!");
        return this;
    }

    /**
     * Sets click action of npc.
     *
     * @param action action on click.
     * @return NpcBuilder instance.
     */
    @Nonnull
    public NpcBuilder whenClicked(@Nonnull BiConsumer<Player, Npc.Action> action) {
        return this.whenClicked(action, 20);
    }

    /**
     * Sets click action of npc.
     *
     * @param action action on click.
     * @param delay  delay between clicks.
     * @return NpcBuilder instance.
     */
    @Nonnull
    public NpcBuilder whenClicked(@Nonnull BiConsumer<Player, Npc.Action> action, long delay) {
        this.clickConsumer = Validate.notNull(action, "action cannot be null!");
        this.clickDelay = Math.max(0, delay);
        return this;
    }

    /**
     * Sets click action of npc.
     *
     * @param action action on spawn.
     * @return NpcBuilder instance.
     */
    @Nonnull
    public NpcBuilder whenSpawned(@Nonnull Consumer<Npc> action) {
        this.spawnConsumer = Validate.notNull(action, "action cannot be null!");
        return this;
    }

    /**
     * Sets click action of npc.
     *
     * @param action action on delete.
     * @return NpcBuilder instance.
     */
    @Nonnull
    public NpcBuilder whenDeleted(@Nonnull Consumer<Npc> action) {
        this.deleteConsumer = Validate.notNull(action, "action cannot be null!");
        return this;
    }

    /**
     * Sets skin of npc.
     *
     * @param skin Skin.
     * @return NpcBuilder instance.
     */
    @Nonnull
    public NpcBuilder skin(@Nonnull Skin skin) {
        this.skin = Validate.notNull(skin, "skin cannot be null!");
        return this;
    }

    /**
     * Sets skin of npc.
     *
     * @param skin Skin.
     * @return NpcBuilder instance.
     */
    @Nonnull
    public NpcBuilder skin(@Nonnull String skin) {
        return this.skin(Skin.from(skin));
    }

    /**
     * Sets equipment of npc.
     *
     * @param type Equipment type.
     * @param item Item.
     * @return NpcBuilder instance.
     */
    @Nonnull
    public NpcBuilder equipment(@Nonnull Npc.EquipmentType type, @Nonnull ItemStack item) {
        this.equipments.put(Validate.notNull(type, "type cannot be null!"),
                Validate.notNull(item, "item cannot be null!"));
        return this;
    }

    /**
     * Sets equipments of npc.
     *
     * @param equipments Equipments.
     * @return NpcBuilder instance.
     */
    @Nonnull
    public NpcBuilder equipments(@Nonnull Map<Npc.EquipmentType, ItemStack> equipments) {
        this.equipments = Validate.notNull(equipments, "equipments cannot be null!");
        return this;
    }

    /**
     * Builds npc but if there is npc
     * with this id, removes it.
     *
     * @return Npc instance.
     */
    @Nonnull
    public Npc forceBuild() {
        NpcHandler.findByID(this.id).ifPresent(Npc::delete);
        return this.build();
    }

    /**
     * Builds npc.
     *
     * @return Npc instance.
     */
    @Nonnull
    public Npc build() {
        Validate.isTrue(NpcHandler.has(this.id), "npc with id(" + this.id + ") already exists!");


        if (this.show == null)
            this.show = (this.viewers.size() > 0);

        Npc npc = new Npc(this.id, this.location, this.skin, this.target, this.lines, this.viewers, this.equipments, this.show);
        if (this.clickConsumer != null)
            npc.whenClicked(this.clickConsumer);
        if (this.spawnConsumer != null)
            npc.whenSpawned(this.spawnConsumer);
        if (this.deleteConsumer != null)
            npc.whenDeleted(this.deleteConsumer);
        npc.getAction().setClickDelay(this.clickDelay);

        NpcHandler.getContent().put(this.id, npc);
        return npc;
    }
}
