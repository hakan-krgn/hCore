package com.hakan.core.npc;

import com.hakan.core.HCore;
import com.hakan.core.hologram.Hologram;
import com.hakan.core.npc.action.NpcAction;
import com.hakan.core.npc.entity.NpcEntity;
import com.hakan.core.npc.utils.NpcUtils;
import com.hakan.core.pathfinder.Pathfinder;
import com.hakan.core.renderer.Renderer;
import com.hakan.core.skin.Skin;
import com.hakan.core.utils.Validate;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Npc class to create and manages
 * NPCs' easily and client-side.
 */
public final class Npc {

    private final String id;
    private final Renderer renderer;
    private final Hologram hologram;
    private final NpcAction action;
    private final NpcEntity entity;
    private final Map<EquipmentType, ItemStack> equipments;

    private Skin skin;
    private LookTarget target;
    private boolean dead = false;
    private boolean walking = false;

    /**
     * Constructor to create new NPC.
     *
     * @param id           NPC id.
     * @param location     NPC location.
     * @param skin         NPC skin.
     * @param target       NPC look target.
     * @param lines        NPC hologram lines.
     * @param viewers      NPC viewers.
     * @param equipments   NPC equipments.
     * @param showEveryone Show everyone or not.
     */
    public Npc(@Nonnull String id,
               @Nonnull Location location,
               @Nonnull Skin skin,
               @Nonnull LookTarget target,
               @Nonnull List<String> lines,
               @Nonnull Set<UUID> viewers,
               @Nonnull Map<EquipmentType, ItemStack> equipments,
               boolean showEveryone) {

        this.renderer = new Renderer(location, 30, viewers,
                this::show, this::hide, renderer -> this.hide(renderer.getShownPlayers()));
        this.hologram = HCore.hologramBuilder("hcore_npc_hologram:" + id)
                .location(location).setViewers(viewers)
                .showEveryone(showEveryone).forceBuild();

        this.action = new NpcAction(this);
        this.id = Validate.notNull(id, "id cannot be null!");
        this.skin = Validate.notNull(skin, "skin cannot be null!");
        this.target = Validate.notNull(target, "target cannot be null!");
        this.equipments = Validate.notNull(equipments, "equipments cannot be null!");
        this.entity = NpcUtils.createEntity(this);

        this.hologram.addLines(Validate.notNull(lines, "lines cannot be null!"));
        this.hologram.showEveryone(showEveryone);
        this.renderer.showEveryone(showEveryone);
        this.action.onSpawn();

        this.setLocation(location);
        this.hologram.getRenderer().render();
        this.renderer.render();
    }

    /**
     * Gets walking status.
     *
     * @return Walking.
     */
    public boolean isWalking() {
        return this.walking;
    }

    /**
     * Gets if NPC is removed.
     *
     * @return If NPC is removed, returns true.
     */
    public boolean isDead() {
        return this.dead;
    }

    /**
     * ID of NPC.
     *
     * @return NPC id.
     */
    @Nonnull
    public String getID() {
        return this.id;
    }

    /**
     * Gets action manager of NPC.
     *
     * @return NPC action manager.
     */
    @Nonnull
    public NpcAction getAction() {
        return this.action;
    }

    /**
     * Gets npc location.
     *
     * @return Location.
     */
    @Nonnull
    public Location getLocation() {
        return this.renderer.getLocation();
    }

    /**
     * Gets npc world.
     *
     * @return World.
     */
    @Nonnull
    public World getWorld() {
        return Validate.notNull(this.getLocation().getWorld());
    }

    /**
     * Gets skin of npc.
     *
     * @return Skin.
     */
    @Nonnull
    public Skin getSkin() {
        return this.skin;
    }

    /**
     * Gets look target of npc.
     *
     * @return Look target.
     */
    @Nonnull
    public LookTarget getLookTarget() {
        return this.target;
    }

    /**
     * Gets renderer.
     *
     * @return Renderer.
     */
    @Nonnull
    public Renderer getRenderer() {
        return this.renderer;
    }

    /**
     * Gets hologram.
     *
     * @return Hologram.
     */
    @Nonnull
    public Hologram getHologram() {
        return this.hologram;
    }

    /**
     * Gets nms entity of npc.
     *
     * @return NpcEntity.
     */
    @Nonnull
    public NpcEntity getEntity() {
        return this.entity;
    }

    /**
     * Get the id of nms entity.
     *
     * @return Entity id.
     */
    public int getEntityID() {
        return this.entity.getID();
    }

    /**
     * Is client side mode active?
     *
     * @return if client side mode active, return true.
     */
    public boolean canEveryoneSee() {
        return this.renderer.canEveryoneSee();
    }

    /**
     * If set this mode as false, everyone
     * can see this hologram.
     *
     * @param mode Mode status.
     * @return Instance of this class.
     */
    @Nonnull
    public Npc showEveryone(boolean mode) {
        this.renderer.showEveryone(mode);
        this.hologram.showEveryone(mode);
        return this;
    }

    /**
     * NPC expires after a certain time.
     *
     * @param expire   Amount.
     * @param timeUnit Time Unit.
     * @return Npc for chain.
     */
    @Nonnull
    public Npc expire(int expire, @Nonnull TimeUnit timeUnit) {
        Validate.notNull(timeUnit, "time unit cannot be null!");
        HCore.syncScheduler().after(expire, timeUnit).run(this::delete);
        return this;
    }

    /**
     * NPC expires after a certain time.
     *
     * @param duration Duration.
     * @return Npc for chain.
     */
    @Nonnull
    public Npc expire(@Nonnull Duration duration) {
        Validate.notNull(duration, "duration cannot be null!");
        HCore.syncScheduler().after(duration).run(this::delete);
        return this;
    }

    /**
     * NPC expires after a certain time.
     *
     * @param ticks Ticks.
     * @return Npc for chain.
     */
    @Nonnull
    public Npc expire(int ticks) {
        HCore.syncScheduler().after(ticks)
                .run(this::delete);
        return this;
    }

    /**
     * Adds player to hologram to show.
     *
     * @param players Player list.
     * @return instance of this class.
     */
    @Nonnull
    public Npc addViewer(@Nonnull List<Player> players) {
        Validate.notNull(players, "players cannot be null!");
        this.hologram.addViewer(players);
        players.forEach(this.renderer::addViewer);
        return this;
    }

    /**
     * Adds player to hologram to show.
     *
     * @param players Player list.
     * @return instance of this class.
     */
    @Nonnull
    public Npc addViewer(@Nonnull Player... players) {
        Validate.notNull(players, "players cannot be null!");
        return this.addViewer(Arrays.asList(players));
    }

    /**
     * Adds player to hologram to show.
     *
     * @param uids Player list.
     * @return instance of this class.
     */
    @Nonnull
    public Npc addViewerByUID(@Nonnull List<UUID> uids) {
        Validate.notNull(uids, "uids cannot be null!");
        this.hologram.addViewerByUID(uids);
        uids.forEach(this.renderer::addViewer);
        return this;
    }

    /**
     * Adds player to hologram to show.
     *
     * @param uids Player list.
     * @return instance of this class.
     */
    @Nonnull
    public Npc addViewerByUID(@Nonnull UUID... uids) {
        Validate.notNull(uids, "uids cannot be null!");
        return this.addViewerByUID(Arrays.asList(uids));
    }

    /**
     * Removes player from hologram to hide.
     *
     * @param players Player list.
     * @return instance of this class.
     */
    @Nonnull
    public Npc removeViewer(@Nonnull List<Player> players) {
        Validate.notNull(players, "players cannot be null!");
        this.hologram.removeViewer(players);
        players.forEach(this.renderer::removeViewer);
        return this;
    }

    /**
     * Removes player from hologram to hide.
     *
     * @param players Player list.
     * @return instance of this class.
     */
    @Nonnull
    public Npc removeViewer(@Nonnull Player... players) {
        Validate.notNull(players, "players cannot be null!");
        return this.removeViewer(Arrays.asList(players));
    }

    /**
     * Removes player from hologram to hide.
     *
     * @param uids Player list.
     * @return instance of this class.
     */
    @Nonnull
    public Npc removeViewerByUID(@Nonnull List<UUID> uids) {
        Validate.notNull(uids, "uids cannot be null!");
        this.hologram.removeViewerByUID(uids);
        uids.forEach(this.renderer::removeViewer);
        return this;
    }

    /**
     * Removes player from npc to hide.
     *
     * @param uids Player list.
     * @return instance of this class.
     */
    @Nonnull
    public Npc removeViewerByUID(@Nonnull UUID... uids) {
        Validate.notNull(uids, "uids cannot be null!");
        return this.removeViewerByUID(Arrays.asList(uids));
    }

    /**
     * This consumer will run when NPC is spawned.
     *
     * @param spawnConsumer Consumer.
     * @return Instance of this class.
     */
    @Nonnull
    public Npc whenSpawned(@Nonnull Consumer<Npc> spawnConsumer) {
        this.action.whenSpawned(spawnConsumer);
        return this;
    }

    /**
     * This consumer will run when NPC is deleted.
     *
     * @param deleteConsumer Consumer.
     * @return Instance of this class.
     */
    @Nonnull
    public Npc whenDeleted(@Nonnull Consumer<Npc> deleteConsumer) {
        this.action.whenDeleted(deleteConsumer);
        return this;
    }

    /**
     * This consumer will run when NPC is clicked by player.
     *
     * @param clickConsumer Consumer.
     * @return Instance of this class.
     */
    @Nonnull
    public Npc whenClicked(@Nonnull BiConsumer<Player, Action> clickConsumer) {
        this.action.whenClicked(clickConsumer);
        return this;
    }

    /**
     * Gets items as safe.
     *
     * @return Slot and ItemStack map.
     */
    @Nonnull
    public Map<EquipmentType, ItemStack> getEquipmentsSafe() {
        return new HashMap<>(this.equipments);
    }

    /**
     * Gets items.
     *
     * @return Slot and ItemStack map.
     */
    @Nonnull
    public Map<EquipmentType, ItemStack> getEquipments() {
        return this.equipments;
    }


    /**
     * Looks at location
     * of player.
     *
     * @param target Target.
     */
    public Npc setTarget(@Nonnull LookTarget target) {
        this.target = Validate.notNull(target, "target type cannot be null!");
        return this;
    }

    /**
     * Sets skin on NPC.
     *
     * @param skin Skin.
     */
    public Npc setSkin(@Nonnull Skin skin) {
        this.skin = Validate.notNull(skin, "skin cannot be null!");
        this.entity.updateSkin(this.renderer.getShownPlayers());
        return this;
    }

    /**
     * Equips NPC with items.
     *
     * @param equipmentType Equipment type. Ex: HAND_ITEM, LEGGINGS,
     * @param itemStack     Item.
     */
    public Npc setEquipment(@Nonnull EquipmentType equipmentType, @Nonnull ItemStack itemStack) {
        Validate.notNull(equipmentType, "equipment type cannot be null!");
        Validate.notNull(itemStack, "itemStack type cannot be null!");

        this.equipments.put(equipmentType, itemStack);
        this.entity.updateEquipments(this.renderer.getShownPlayers());
        return this;
    }

    /**
     * Sets location.
     *
     * @param location Location.
     */
    public Npc setLocation(@Nonnull Location location) {
        Validate.notNull(location, "location cannot be null!");

        this.hologram.setLocation(location.clone().add(0, (this.hologram.getLines().size() * this.hologram.getLineDistance() / 2 + 2), 0));
        this.renderer.setLocation(location);
        this.entity.updateLocation(this.renderer.getShownPlayers());
        return this;
    }

    /**
     * Sets head rotation
     * with yaw and pitch.
     *
     * @param yaw   Yaw.
     * @param pitch Pitch.
     */
    public Npc setHeadRotation(double yaw, double pitch) {
        Location location = this.getLocation();
        location.setYaw((float) yaw);
        location.setPitch((float) pitch);

        this.renderer.setLocation(location);
        this.entity.updateHeadRotation(this.renderer.getShownPlayers());
        return this;
    }

    /**
     * Plays animation.
     *
     * @param animation Animation.
     */
    public Npc playAnimation(@Nonnull Animation animation) {
        Validate.notNull(animation, "animation cannot be null!");
        this.entity.playAnimation(this.renderer.getShownPlayers(), animation);
        return this;
    }

    /**
     * Looks at location
     * of player.
     *
     * @param player Player.
     */
    public Npc lookAt(@Nonnull Player player) {
        Validate.notNull(player, "player cannot be null!");
        return this.lookAt(player.getEyeLocation());
    }

    /**
     * Looks at location.
     *
     * @param location Location to look.
     */
    public Npc lookAt(@Nonnull Location location) {
        Validate.notNull(location, "location cannot be null!");
        Validate.isTrue(location.getWorld() == null, "location world cannot be null!");
        Validate.isTrue(!location.getWorld().equals(this.getWorld()), "location and npc worlds must be equal!");

        double[] angles = NpcUtils.calculateVector(this.getLocation().add(0, 1.62, 0), location);
        return this.setHeadRotation(angles[0], angles[1]);
    }

    /**
     * Moves NPC.
     *
     * @param to    Destination location.
     * @param speed Speed.
     */
    public Npc walk(@Nonnull Location to, double speed) {
        Validate.notNull(to, "to location cannot be null!");
        Validate.isTrue(this.walking, "NPC is already walking!");
        Validate.isTrue(to.getWorld() == null, "to world cannot be null!");
        Validate.isTrue(!to.getWorld().equals(this.getWorld()), "to and from worlds must be equal!");

        this.walking = true;

        Pathfinder pathfinder = new Pathfinder(this.getLocation(), to);
        Location[] locations = pathfinder.getPath(5);
        locations[locations.length - 1] = to;

        HCore.asyncScheduler()
                .limit(locations.length - 1)
                .terminateIf((task) -> !this.walking)
                .every((long) (100 / speed), TimeUnit.MILLISECONDS)
                .whenEnded(() -> this.walking = false)
                .run((task, count) -> {
                    Location location = locations[Math.toIntExact(count)];
                    if (location == null) return;

                    this.setLocation(location);
                });

        return this;
    }

    /**
     * Who sees NPC?
     *
     * @param players Player list.
     */
    public Npc show(@Nonnull List<Player> players) {
        this.entity.show(Validate.notNull(players, "players cannot be null!"));
        return this;
    }

    /**
     * From whom should this NPC be hidden?
     *
     * @param players Player list.
     */
    public Npc hide(@Nonnull List<Player> players) {
        this.entity.hide(Validate.notNull(players, "players cannot be null!"));
        return this;
    }

    /**
     * Deletes NPC.
     *
     * @return Instance of this class.
     */
    @Nonnull
    public Npc delete() {
        NpcHandler.getContent().remove(this.id);

        this.action.onDelete();
        this.hologram.delete();
        this.renderer.delete();
        this.dead = true;
        this.walking = false;

        return this.hide(this.renderer.getShownPlayers());
    }



    /**
     * Click types.
     */
    public enum Action {

        RIGHT_CLICK,
        LEFT_CLICK,
    }

    /**
     * Look target types.
     */
    public enum LookTarget {

        NEAREST,
        INDIVIDUAL,
        CONSTANT,
    }

    /**
     * Animation types.
     */
    public enum Animation {

        SWING_MAINHAND(0),
        TAKE_DAMAGE(1),
        LEAVE_BED(2),
        SWING_OFFHAND(3),
        CRITICAL_DAMAGE(4),
        MAGICAL_DAMAGE(5),
        ;

        private final int id;

        /**
         * Constructor.
         *
         * @param id Id.
         */
        Animation(int id) {
            this.id = id;
        }

        /**
         * Gets id of animation.
         *
         * @return Id.
         */
        public int getId() {
            return this.id;
        }
    }

    /**
     * Npc slot types.
     */
    public enum EquipmentType {

        MAINHAND(0, "mainhand"),
        OFFHAND(0, "offhand"),
        FEET(4, "feet"),
        LEGS(3, "legs"),
        CHEST(2, "chest"),
        HEAD(1, "head"),
        ;

        private final int slot;
        private final String value;

        /**
         * Constructor with slot and
         * value for its name.
         *
         * @param slot  Slot number.
         * @param value Value.
         */
        EquipmentType(int slot, @Nonnull String value) {
            this.slot = slot;
            this.value = Validate.notNull(value);
        }

        /**
         * Gets slot.
         *
         * @return slot.
         */
        public int getSlot() {
            return this.slot;
        }

        /**
         * Gets value for higher versions.
         *
         * @return value.
         */
        @Nonnull
        public String getValue() {
            return this.value;
        }
    }
}
