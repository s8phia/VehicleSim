import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;

public class LaneSpawnCheck extends Actor
{
    private GreenfootImage blank;

    public LaneSpawnCheck (){
        
        // creates a blank object that is big enough to check the space of the lane beside vehicles 
        blank = new GreenfootImage (80, 55);
        setImage(blank);
    }


    public boolean safeToChangeLanes() {
        // Get the objects intersecting with the current LaneSpawnCheck object
        List<Vehicle> vehicles = getIntersectingObjects(Vehicle.class);

        // If there are vehicles in the list, it's not safe to change lanes
        return vehicles.isEmpty();
    }

}
