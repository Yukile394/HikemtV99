package es.pollitoyeye.vehicles.events;

import es.pollitoyeye.vehicles.enums.VehicleType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public abstract class VehicleEnterEvent extends Event {
    public abstract Player getPlayer();
    public abstract VehicleType getVehicleType();
}
