package com.hakan.core.pathfinder;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;

public class Pathfinder {

    private final Location startLocation;
    private final Location endLocation;

    private Node endNode;
    private final Node startNode;

    private boolean pathFound = false;
    private final ArrayList<Node> checkedNodes = new ArrayList<>();
    private final ArrayList<Node> uncheckedNodes = new ArrayList<>();

    private final int maxNodeTests;
    private final boolean canClimbLadders;
    private final double maxFallDistance;

    public Pathfinder(Location start, Location end, int maxNodeTests, boolean canClimbLadders, double maxFallDistance) {
        this.endLocation = end;
        this.startLocation = start;

        this.endNode = new Node(this.endLocation, 0, null);
        this.startNode = new Node(this.startLocation, 0, null);

        this.maxNodeTests = maxNodeTests;
        this.canClimbLadders = canClimbLadders;
        this.maxFallDistance = maxFallDistance;
    }

    public Pathfinder(Location start, Location end) {
        this(start, end, 1000, false, 1);
    }

    public Location[] getPath() {
        return getPath(1);
    }

    public Location[] getPath(int slice) {
        if (!(this.canStandAt(this.startLocation) && this.canStandAt(this.endLocation)))
            return new Location[0];

        this.uncheckedNodes.add(this.startNode);

        while (this.checkedNodes.size() < this.maxNodeTests && !this.pathFound && this.uncheckedNodes.size() > 0) {
            Node n = this.uncheckedNodes.get(0);
            for (Node nt : this.uncheckedNodes)
                if (nt.getEstimatedFinalExpense() < n.getEstimatedFinalExpense())
                    n = nt;

            if (n.estimatedExpenseLeft < 1) {
                this.pathFound = true;
                this.endNode = n;
                break;
            }

            n.getReachableLocations();
            this.uncheckedNodes.remove(n);
            this.checkedNodes.add(n);
        }

        if (!this.pathFound)
            return new Location[0];

        int length = 1;
        Node n = this.endNode;
        while (n.origin != null) {
            n = n.origin;
            length++;
        }

        Location[] locations = new Location[length];

        n = this.endNode;
        for (int i = length - 1; i > 0; i--) {
            locations[i] = n.getLocation();
            n = n.origin;
        }

        locations[0] = this.startNode.getLocation();

        Location[] lastLocations = new Location[locations.length * slice];
        for (int i = 0; i < locations.length; i++) {
            if (i == locations.length - 1)
                break;

            Location fromLocation = locations[i];
            Location toLocation = locations[i + 1];

            double disX = toLocation.getX() - fromLocation.getX();
            double disY = toLocation.getY() - fromLocation.getY();
            double disZ = toLocation.getZ() - fromLocation.getZ();

            for (int j = 0; j < slice; j++) {
                lastLocations[i * slice + j] = fromLocation.clone().add(disX * j / slice, disY * j / slice, disZ * j / slice);
            }
        }

        return lastLocations;
    }

    private Node getNode(Location loc) {
        Node test = new Node(loc, 0, null);
        for (Node n : this.checkedNodes)
            if (n.id == test.id)
                return n;
        return test;
    }



    private boolean isObstructed(Location loc) {
        return loc.getBlock().getType().isSolid();
    }

    private boolean canStandAt(Location loc) {
        return !(isObstructed(loc) || isObstructed(loc.clone().add(0, 1, 0)) || !isObstructed(loc.clone().add(0, -1, 0)));
    }

    private double distanceTo(Location loc1, Location loc2) {
        if (loc1.getWorld() != loc2.getWorld())
            return Double.MAX_VALUE;

        double disX = loc1.getX() - loc2.getX();
        double disY = loc1.getY() - loc2.getY();
        double disZ = loc1.getZ() - loc2.getZ();

        return Math.sqrt(disX * disX + disY * disY + disZ * disZ);
    }



    private class Node {

        public double id;
        public Node origin;
        public double expense;

        private final Location location;
        private double estimatedExpenseLeft = -1;

        public Node(Location loc, double expense, Node origin) {
            this.location = loc;
            this.origin = origin;
            this.expense = expense;
            this.id = loc.getBlockX() + 30000000d * loc.getBlockY() + 30000000d * 30000000d * loc.getBlockZ();
        }

        public Location getLocation() {
            return this.location;
        }

        public double getEstimatedFinalExpense() {
            if (this.estimatedExpenseLeft == -1)
                this.estimatedExpenseLeft = distanceTo(this.location, endLocation);

            return this.expense + 1.5 * this.estimatedExpenseLeft;
        }

        public void getReachableLocations() {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    if (!(x == 0 && z == 0) && x * z == 0) {
                        Location loc = new Location(Bukkit.getWorlds().get(0), this.location.getBlockX() + x, this.location.getBlockY(), this.location.getBlockZ() + z);

                        if (canStandAt(loc))
                            reachNode(loc, expense + 1);

                        if (!isObstructed(loc.clone().add(-x, 2, -z))) {
                            Location nLoc = loc.clone().add(0, 1, 0);
                            if (canStandAt(nLoc)) reachNode(nLoc, this.expense + 1.4142);
                        }

                        if (!isObstructed(loc.clone().add(0, 1, 0))) {
                            Location nLoc = loc.clone().add(0, -1, 0);
                            if (canStandAt(nLoc)) {
                                reachNode(nLoc, expense + 1.4142);
                            } else if (!isObstructed(nLoc) && !isObstructed(nLoc.clone().add(0, 1, 0))) {
                                int drop = 1;
                                while (drop <= maxFallDistance && !isObstructed(loc.clone().add(0, -drop, 0))) {
                                    Location locF = loc.clone().add(0, -drop, 0);
                                    if (canStandAt(locF)) {
                                        Node fallNode = addFallNode(loc, expense + 1);
                                        fallNode.reachNode(locF, expense + drop * 2);
                                    }

                                    drop++;
                                }
                            }
                        }

                        if (canClimbLadders) {
                            if (loc.clone().add(-x, 0, -z).getBlock().getType() == Material.LADDER) {
                                Location nLoc = loc.clone().add(-x, 0, -z);
                                int up = 1;
                                while (nLoc.clone().add(0, up, 0).getBlock().getType() == Material.LADDER) up++;
                                reachNode(nLoc.clone().add(0, up, 0), expense + up * 2);
                            }
                        }
                    }
                }
            }
        }

        public void reachNode(Location locThere, double expenseThere) {
            Node nt = getNode(locThere);

            if (nt.origin == null && nt != startNode) {
                nt.expense = expenseThere;
                nt.origin = this;
                uncheckedNodes.add(nt);
                return;
            }

            if (nt.expense > expenseThere) {
                nt.expense = expenseThere;
                nt.origin = this;
            }
        }

        public Node addFallNode(Location loc, double expense) {
            return new Node(loc, expense, this);
        }
    }
}
