package com.hakan.core.npc.entity;

import com.hakan.core.HCore;
import com.hakan.core.npc.Npc;
import com.hakan.core.skin.Skin;
import com.hakan.core.utils.Validate;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.protocol.game.PacketPlayOutAnimation;
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
import org.bukkit.craftbukkit.v1_18_R1.CraftServer;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * {@inheritDoc}
 */
public final class NpcEntity_v1_18_R1 implements NpcEntity {

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
        DedicatedServer server = ((CraftServer) Bukkit.getServer()).getServer();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), npc.getID());

        Property property = new Property("textures", skin.getTexture(), skin.getSignature());
        gameProfile.getProperties().clear();
        gameProfile.getProperties().put("textures", property);

        EntityPlayer entityPlayer = new EntityPlayer(server, world, gameProfile);
        entityPlayer.a(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        entityPlayer.persistentInvisibility = false; //set invisibility to true
        entityPlayer.b(5, true); //set invisibility to true
        entityPlayer.c(77.21f); //sets health to 77.21f
        return entityPlayer;
    }


    private final Npc npc;
    private final EntityPlayer nmsPlayer;
    private final ScoreboardTeam scoreboard;

    /**
     * {@inheritDoc}
     */
    public NpcEntity_v1_18_R1(@Nonnull Npc npc) {
        this.npc = Validate.notNull(npc, "npc cannot be null!");
        this.nmsPlayer = createEntityPlayer(npc);
        this.scoreboard = new ScoreboardTeam(new Scoreboard(), npc.getID());
        this.scoreboard.a(ScoreboardTeamBase.EnumNameTagVisibility.b);
        this.scoreboard.g().add(npc.getID());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getID() {
        return this.nmsPlayer.ae();
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
        this.nmsPlayer.a(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

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

        GameProfile gameProfile = this.nmsPlayer.fp();
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

        List<Pair<EnumItemSlot, ItemStack>> equipmentList = new ArrayList<>();
        this.npc.getEquipments().forEach((key, value) -> equipmentList.add(new Pair<>(EnumItemSlot.valueOf(key.name()), CraftItemStack.asNMSCopy(value))));
        HCore.sendPacket(players, new PacketPlayOutEntityEquipment(this.getID(), equipmentList));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show(@Nonnull List<Player> players) {
        Validate.notNull(players, "players cannot be null!");

        GameProfile gameProfile = this.nmsPlayer.fp();
        DataWatcher dataWatcher = new DataWatcher(null);
        gameProfile.getProperties().get("textures").clear();
        gameProfile.getProperties().put("textures", new Property("textures", this.npc.getSkin().getTexture(), this.npc.getSkin().getSignature()));
        dataWatcher.a(new DataWatcherObject<>(17, DataWatcherRegistry.a), (byte) 127);

        players.forEach(player -> this.scoreboard.g().add(player.getName()));
        HCore.sendPacket(players,
                new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, this.nmsPlayer),
                new PacketPlayOutNamedEntitySpawn(this.nmsPlayer),
                new PacketPlayOutEntityMetadata(this.getID(), dataWatcher, true),
                PacketPlayOutScoreboardTeam.a(this.scoreboard, true));
        HCore.asyncScheduler().after(5)
                .run(() -> HCore.sendPacket(players, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.e, this.nmsPlayer)));

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

        players.forEach(player -> this.scoreboard.g().remove(player.getName()));
        HCore.sendPacket(players, new PacketPlayOutEntityDestroy(this.getID()),
                PacketPlayOutScoreboardTeam.a(this.scoreboard, true));
    }
}
