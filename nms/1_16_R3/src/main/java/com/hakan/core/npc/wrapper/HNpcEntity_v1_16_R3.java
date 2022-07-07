package com.hakan.core.npc.wrapper;

import com.hakan.core.HCore;
import com.hakan.core.npc.HNPC;
import com.hakan.core.npc.entity.HNpcEntity;
import com.hakan.core.npc.skin.HNPCSkin;
import com.hakan.core.renderer.HRenderer;
import com.hakan.core.utils.Validate;
import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.EnumItemSlot;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntity;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_16_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_16_R3.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_16_R3.PlayerInteractManager;
import net.minecraft.server.v1_16_R3.Scoreboard;
import net.minecraft.server.v1_16_R3.ScoreboardTeam;
import net.minecraft.server.v1_16_R3.ScoreboardTeamBase;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * {@inheritDoc}
 */
public final class HNpcEntity_v1_16_R3 extends EntityPlayer implements HNpcEntity {

    private final HNPC npc;
    private final HRenderer renderer;
    private final HNPCUtils_v1_16_R3 utils;
    private final ScoreboardTeam scoreboardTeam;

    /**
     * {@inheritDoc}
     */
    public HNpcEntity_v1_16_R3(@Nonnull HNPC npc) {
        super(((CraftServer) Bukkit.getServer()).getServer(),
                ((CraftWorld) npc.getWorld()).getHandle(),
                new GameProfile(UUID.randomUUID(), npc.getId()),
                new PlayerInteractManager(((CraftWorld) npc.getWorld()).getHandle()));
        Location location = npc.getLocation();
        super.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        super.setInvisible(false);
        super.setHealth(77.21f);

        this.npc = Validate.notNull(npc, "hnpc cannot be null!");
        this.renderer = this.npc.getRenderer();
        this.utils = new HNPCUtils_v1_16_R3(this.npc);
        this.scoreboardTeam = new ScoreboardTeam(new Scoreboard(), npc.getId());
        this.scoreboardTeam.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.NEVER);
        this.scoreboardTeam.getPlayerNameSet().add(super.getName());

        HCore.syncScheduler().after(20)
                .run(() -> this.hide(npc.getRenderer().getShownViewersAsPlayer()));
        HCore.syncScheduler().after(25)
                .run(() -> this.show(npc.getRenderer().getShownViewersAsPlayer()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getID() {
        return super.getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void walk(@Nonnull Location to, double speed, @Nonnull Runnable runnable) {
        this.utils.walk(to, speed, runnable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLocation(@Nonnull Location location) {
        super.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        double imp = 256f / 360f;
        float yaw = Math.round(location.getYaw() % 360f * imp);
        float pitch = Math.round(location.getPitch() % 360f * imp);
        HCore.sendPacket(this.renderer.getShownViewersAsPlayer(),
                new PacketPlayOutEntityHeadRotation(this, (byte) (location.getYaw() * imp)),
                new PacketPlayOutEntity.PacketPlayOutEntityLook(this.getId(), (byte) yaw, (byte) pitch, false),
                new PacketPlayOutEntityTeleport(this));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSkin(@Nonnull HNPCSkin skin) {
        List<Player> players = this.renderer.getShownViewersAsPlayer();

        this.hide(players);
        HCore.syncScheduler().after(20).run(() -> this.show(players));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEquipment(@Nonnull HNPC.EquipmentType equipment, @Nonnull ItemStack itemStack) {
        List<Pair<EnumItemSlot, net.minecraft.server.v1_16_R3.ItemStack>> equipmentList = new ArrayList<>();
        this.npc.getEquipments().forEach((key, value) -> equipmentList.add(new Pair<>(EnumItemSlot.valueOf(key.name()), CraftItemStack.asNMSCopy(value))));
        HCore.sendPacket(this.renderer.getShownViewersAsPlayer(), new PacketPlayOutEntityEquipment(this.getID(), equipmentList));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show(@Nonnull List<Player> players) {
        players.forEach(player -> this.scoreboardTeam.getPlayerNameSet().add(player.getName()));
        HCore.sendPacket(players,
                new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, this),
                new PacketPlayOutNamedEntitySpawn(this),
                new PacketPlayOutEntityMetadata(this.getID(), this.utils.createDataWatcher(this), true),
                new PacketPlayOutScoreboardTeam(this.scoreboardTeam, 0));

        if (this.npc.getEquipments().size() > 0) {
            List<Pair<EnumItemSlot, net.minecraft.server.v1_16_R3.ItemStack>> equipmentList = new ArrayList<>();
            this.npc.getEquipments().forEach((key, value) -> equipmentList.add(new Pair<>(EnumItemSlot.valueOf(key.name()), CraftItemStack.asNMSCopy(value))));
            HCore.sendPacket(players, new PacketPlayOutEntityEquipment(this.getID(), equipmentList));
        }

        HCore.asyncScheduler().after(5)
                .run(() -> HCore.sendPacket(players, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, this)));
        this.setLocation(this.npc.getLocation());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void hide(@Nonnull List<Player> players) {
        players.forEach(player -> this.scoreboardTeam.getPlayerNameSet().remove(player.getName()));
        HCore.sendPacket(players,
                new PacketPlayOutEntityDestroy(this.getID()),
                new PacketPlayOutScoreboardTeam(this.scoreboardTeam, 0));
    }
}