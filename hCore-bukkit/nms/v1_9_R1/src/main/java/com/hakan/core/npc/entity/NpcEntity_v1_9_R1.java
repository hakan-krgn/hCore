package com.hakan.core.npc.entity;

import com.hakan.core.HCore;
import com.hakan.core.npc.Npc;
import com.hakan.core.skin.Skin;
import com.hakan.core.utils.Validate;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_9_R1.DataWatcher;
import net.minecraft.server.v1_9_R1.DataWatcherObject;
import net.minecraft.server.v1_9_R1.DataWatcherRegistry;
import net.minecraft.server.v1_9_R1.EntityPlayer;
import net.minecraft.server.v1_9_R1.EnumItemSlot;
import net.minecraft.server.v1_9_R1.MinecraftServer;
import net.minecraft.server.v1_9_R1.PacketPlayOutAnimation;
import net.minecraft.server.v1_9_R1.PacketPlayOutEntity;
import net.minecraft.server.v1_9_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_9_R1.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_9_R1.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_9_R1.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_9_R1.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_9_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_9_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_9_R1.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_9_R1.PlayerInteractManager;
import net.minecraft.server.v1_9_R1.Scoreboard;
import net.minecraft.server.v1_9_R1.ScoreboardTeam;
import net.minecraft.server.v1_9_R1.ScoreboardTeamBase;
import net.minecraft.server.v1_9_R1.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R1.CraftServer;
import org.bukkit.craftbukkit.v1_9_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

/**
 * {@inheritDoc}
 */
public final class NpcEntity_v1_9_R1 implements NpcEntity {

    /**
     * Creates nms player.
     *
     * @param npc Npc instance.
     * @return nms player.
     */
    @Nonnull
    private static EntityPlayer createEntityPlayer(@Nonnull Npc npc) {
        Validate.notNull(npc, "npc cannot be null");

        Skin skin = npc.getSkin();
        Location location = npc.getLocation();
        WorldServer world = ((CraftWorld) npc.getWorld()).getHandle();
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), npc.getID());
        PlayerInteractManager manager = new PlayerInteractManager(((CraftWorld) npc.getWorld()).getHandle());

        Property property = new Property("textures", skin.getTexture(), skin.getSignature());
        gameProfile.getProperties().clear();
        gameProfile.getProperties().put("textures", property);

        EntityPlayer entityPlayer = new EntityPlayer(server, world, gameProfile, manager);
        entityPlayer.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        entityPlayer.setInvisible(false);
        entityPlayer.setHealth(77.21f);
        return entityPlayer;
    }


    private final Npc npc;
    private final EntityPlayer nmsPlayer;
    private final ScoreboardTeam scoreboard;

    /**
     * {@inheritDoc}
     */
    public NpcEntity_v1_9_R1(@Nonnull Npc npc) {
        this.npc = Validate.notNull(npc, "npc cannot be null!");
        this.nmsPlayer = createEntityPlayer(npc);
        this.scoreboard = new ScoreboardTeam(new Scoreboard(), npc.getID());
        this.scoreboard.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.NEVER);
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
    public void playAnimation(@Nonnull List<Player> players, @Nonnull Npc.Animation animation) {
        Validate.notNull(players, "players cannot be null");
        HCore.sendPacket(players, new PacketPlayOutAnimation(this.nmsPlayer, animation.getId()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateLocation(@Nonnull List<Player> players) {
        this.updateHeadRotation(Validate.notNull(players, "players cannot be null!"));
        HCore.sendPacket(players, new PacketPlayOutEntityTeleport(this.nmsPlayer));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateHeadRotation(@Nonnull List<Player> players) {
        Validate.notNull(players, "players cannot be null!");

        Location location = this.npc.getLocation();
        this.nmsPlayer.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        float yaw = Math.round(location.getYaw() % 360f * (256f / 360f));
        float pitch = Math.round(location.getPitch() % 360f * (256f / 360f));
        HCore.sendPacket(players, new PacketPlayOutEntityHeadRotation(this.nmsPlayer, (byte) (location.getYaw() * (256f / 360f))),
                new PacketPlayOutEntity.PacketPlayOutEntityLook(this.getID(), (byte) yaw, (byte) pitch, false));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateSkin(@Nonnull List<Player> players) {
        Validate.notNull(players, "players cannot be null!");

        this.hide(players);

        GameProfile gameProfile = this.nmsPlayer.getProfile();
        gameProfile.getProperties().get("textures").clear();
        gameProfile.getProperties().put("textures", new Property("textures", this.npc.getSkin().getTexture(), this.npc.getSkin().getSignature()));

        this.show(players);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateEquipments(@Nonnull List<Player> players) {
        Validate.notNull(players, "players cannot be null!");

        if (this.npc.getEquipments().size() == 0)
            return;

        this.npc.getEquipments().forEach((slot, item) -> HCore.sendPacket(players,
                new PacketPlayOutEntityEquipment(this.getID(), EnumItemSlot.valueOf(slot.name()), CraftItemStack.asNMSCopy(item))));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show(@Nonnull List<Player> players) {
        Validate.notNull(players, "players cannot be null!");

        GameProfile gameProfile = this.nmsPlayer.getProfile();
        DataWatcher dataWatcher = this.nmsPlayer.getDataWatcher();
        gameProfile.getProperties().get("textures").clear();
        gameProfile.getProperties().put("textures", new Property("textures", this.npc.getSkin().getTexture(), this.npc.getSkin().getSignature()));
        dataWatcher.set(new DataWatcherObject<>(12, DataWatcherRegistry.a), (byte) 127);

        players.forEach(player -> this.scoreboard.getPlayerNameSet().add(player.getName()));
        HCore.sendPacket(players,
                new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, this.nmsPlayer),
                new PacketPlayOutNamedEntitySpawn(this.nmsPlayer),
                new PacketPlayOutEntityMetadata(this.getID(), dataWatcher, true),
                new PacketPlayOutScoreboardTeam(this.scoreboard, 0));
        HCore.asyncScheduler().after(5)
                .run(() -> HCore.sendPacket(players, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, this.nmsPlayer)));

        HCore.asyncScheduler().after(2)
                .run(() -> this.updateLocation(players));
        this.updateEquipments(players);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void hide(@Nonnull List<Player> players) {
        Validate.notNull(players, "players cannot be null!");

        players.forEach(player -> this.scoreboard.getPlayerNameSet().remove(player.getName()));
        HCore.sendPacket(players, new PacketPlayOutEntityDestroy(this.getID()),
                new PacketPlayOutScoreboardTeam(this.scoreboard, 0));
    }
}