package com.hakan.core.npc.entity;

import com.hakan.core.HCore;
import com.hakan.core.npc.HNPC;
import com.hakan.core.npc.pathfinder.PathfinderEntity_v1_17_R1;
import com.hakan.core.npc.skin.HNpcSkin;
import com.hakan.core.renderer.HRenderer;
import com.hakan.core.utils.Validate;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.protocol.game.PacketPlayOutEntity;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment;
import net.minecraft.network.protocol.game.PacketPlayOutEntityHeadRotation;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo;
import net.minecraft.network.protocol.game.PacketPlayOutScoreboardTeam;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.ScoreboardTeam;
import net.minecraft.world.scores.ScoreboardTeamBase;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * {@inheritDoc}
 */
public final class HNpcEntity_v1_17_R1 implements HNpcEntity {

    /**
     * Creates nms player.
     *
     * @param npc HNPC instance.
     * @return nms player.
     */
    @Nonnull
    private static EntityPlayer createEntityPlayer(@Nonnull HNPC npc) {
        Validate.notNull(npc, "npc cannot be null");

        HNpcSkin skin = npc.getSkin();
        Location location = npc.getLocation();
        WorldServer world = ((CraftWorld) npc.getWorld()).getHandle();
        DedicatedServer server = ((CraftServer) Bukkit.getServer()).getServer();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), npc.getID());

        Property property = new Property("textures", skin.getTexture(), skin.getSignature());
        gameProfile.getProperties().clear();
        gameProfile.getProperties().put("textures", property);

        EntityPlayer entityPlayer = new EntityPlayer(server, world, gameProfile);
        entityPlayer.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        entityPlayer.setInvisible(false);
        entityPlayer.setHealth(77.21f);
        return entityPlayer;
    }


    private final HNPC hnpc;
    private final HRenderer renderer;
    private final ScoreboardTeam scoreboard;
    private EntityPlayer nmsPlayer;

    /**
     * {@inheritDoc}
     */
    public HNpcEntity_v1_17_R1(@Nonnull HNPC hnpc) {
        this.hnpc = Validate.notNull(hnpc, "hnpc cannot be null!");
        this.renderer = this.hnpc.getRenderer();
        this.nmsPlayer = createEntityPlayer(hnpc);
        this.scoreboard = new ScoreboardTeam(new Scoreboard(), hnpc.getID());
        this.scoreboard.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.b);
        this.scoreboard.getPlayerNameSet().add(this.nmsPlayer.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getID() {
        return this.nmsPlayer.getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void walk(double speed, @Nonnull Location to, @Nonnull Runnable runnable) {
        PathfinderEntity_v1_17_R1.create(this.hnpc.getLocation(), to, speed,
                (custom) -> this.hnpc.setLocation(custom.getBukkitEntity().getLocation()),
                (custom) -> {
                    custom.die();
                    this.hnpc.setLocation(to);
                    runnable.run();
                });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateLocation() {
        Location location = this.hnpc.getLocation();
        this.nmsPlayer.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        float yaw = Math.round(location.getYaw() % 360f * (256f / 360f));
        float pitch = Math.round(location.getPitch() % 360f * (256f / 360f));
        HCore.sendPacket(this.renderer.getShownViewersAsPlayer(),
                new PacketPlayOutEntityHeadRotation(this.nmsPlayer, (byte) (location.getYaw() * (256f / 360f))),
                new PacketPlayOutEntity.PacketPlayOutEntityLook(this.getID(), (byte) yaw, (byte) pitch, false),
                new PacketPlayOutEntityTeleport(this.nmsPlayer));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateSkin() {
        List<Player> players = this.renderer.getShownViewersAsPlayer();
        this.hide(players);
        this.nmsPlayer = createEntityPlayer(this.hnpc);
        HCore.syncScheduler().after(20).run(() -> this.show(players));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateEquipments() {
        if (this.hnpc.getEquipments().size() == 0)
            return;

        List<Pair<EnumItemSlot, ItemStack>> equipmentList = new ArrayList<>();
        this.hnpc.getEquipments().forEach((key, value) -> equipmentList.add(new Pair<>(EnumItemSlot.valueOf(key.name()), CraftItemStack.asNMSCopy(value))));
        HCore.sendPacket(this.renderer.getShownViewersAsPlayer(), new PacketPlayOutEntityEquipment(this.getID(), equipmentList));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAbsorptionHealth(float health) {
        this.nmsPlayer.setAbsorptionHearts(health);

        HCore.sendPacket(this.renderer.getShownViewersAsPlayer(),
                new PacketPlayOutEntityMetadata(this.getID(), this.nmsPlayer.getDataWatcher(), true));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show(@Nonnull List<Player> players) {
        DataWatcher dataWatcher = this.nmsPlayer.getDataWatcher();
        dataWatcher.set(new DataWatcherObject<>(17, DataWatcherRegistry.a), (byte) 127);

        players.forEach(player -> this.scoreboard.getPlayerNameSet().add(player.getName()));
        HCore.sendPacket(players,
                new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, this.nmsPlayer),
                new PacketPlayOutNamedEntitySpawn(this.nmsPlayer),
                new PacketPlayOutEntityMetadata(this.getID(), dataWatcher, true),
                PacketPlayOutScoreboardTeam.a(this.scoreboard, true));
        HCore.asyncScheduler().after(5)
                .run(() -> HCore.sendPacket(players, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.e, this.nmsPlayer)));

        this.updateEquipments();
        this.updateLocation();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void hide(@Nonnull List<Player> players) {
        players.forEach(player -> this.scoreboard.getPlayerNameSet().remove(player.getName()));
        HCore.sendPacket(players,
                new PacketPlayOutEntityDestroy(this.getID()),
                PacketPlayOutScoreboardTeam.a(this.scoreboard, true));
    }
}