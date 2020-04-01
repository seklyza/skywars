package com.seklyza.skywars.utils;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.List;

public class LocationUtils {
    public static Location parseLocation(World world, String loc) {
        String[] axes = loc.split(" ");

        double x = 0, y = 0, z = 0, yaw = 0, pitch = 0;
        for (int i = 0; i < axes.length; i++) {
            double axis = Double.parseDouble(axes[i]);

            switch (i) {
                case 0:
                    x = axis;

                case 1:
                    y = axis;

                case 2:
                    z = axis;

                case 3:
                    yaw = axis;

                case 4:
                    pitch = axis;
            }
        }

        return new Location(world, x, y, z, (float) yaw, (float) pitch);
    }

    public static Location[] parseLocations(World world, List<String> locations) {
        return locations.stream().map(loc -> parseLocation(world, loc)).toArray(Location[]::new);
    }
}
