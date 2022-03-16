package com.hakan.core.particle;

import org.bukkit.Color;
import org.bukkit.util.Vector;

public final class HParticle {

    private String particleName;
    private int amount;
    private double speed;
    private Vector offset;
    private Color color;

    public HParticle(String particleName, int amount, double speed, Vector offset, Color color) {
        this.particleName = particleName;
        this.amount = amount;
        this.speed = speed;
        this.offset = offset;
        this.color = color;
    }

    public HParticle(String particleName, int amount, double speed, Vector offset) {
        this(particleName, amount, speed, offset, Color.WHITE);
    }

    public HParticle(String particleName, double speed, Vector offset) {
        this(particleName, 10, speed, offset, Color.WHITE);
    }

    public HParticle(String particleName, Vector offset) {
        this(particleName, 10, 1, offset, Color.WHITE);
    }

    public HParticle(String particleName) {
        this(particleName, 10, 1, new Vector(0.1, 0.1, 0.1), Color.WHITE);
    }

    public String getParticleName() {
        return this.particleName;
    }

    public void setParticleName(String particleName) {
        this.particleName = particleName;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getSpeed() {
        return this.speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public Vector getOffset() {
        return this.offset;
    }

    public void setOffset(Vector offset) {
        this.offset = offset;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
