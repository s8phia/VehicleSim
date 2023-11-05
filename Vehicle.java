import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * This is the superclass for Vehicles.
 * 
 */
public abstract class Vehicle extends SuperSmoothMover
{
    protected double maxSpeed;
    protected double speed;
    protected int direction; // 1 = right, -1 = left
    protected boolean moving;
    protected boolean isNew;
    protected int yOffset;
    protected VehicleSpawner origin;
    protected int followingDistance;
    protected int myLaneNumber;
    private LaneSpawnCheck lanecheck;
    private LaneSpawnCheck lanecheck2;    
    protected boolean hasChangedLanes = false;

    private GreenfootSound honk;

    protected boolean checkHitPedestrian() {
        int numberOfCollisionPoints = 3; 
        int collisionPointOffset = getImage().getWidth() / numberOfCollisionPoints;

        int xOffset1 = 0 * collisionPointOffset * direction;
        int xOffset2 = 1 * collisionPointOffset * direction;
        int xOffset3 = 2 * collisionPointOffset * direction;

        Actor pedestrian1 = getOneObjectAtOffset(xOffset1, 0, Pedestrian.class);
        Actor pedestrian2 = getOneObjectAtOffset(xOffset2, 0, Pedestrian.class);
        Actor pedestrian3 = getOneObjectAtOffset(xOffset3, 0, Pedestrian.class);

        if (pedestrian1 != null || pedestrian2 != null || pedestrian3 != null ) {
            if (pedestrian1 != null) ((Pedestrian) pedestrian1).knockDown();
            if (pedestrian2 != null) ((Pedestrian) pedestrian2).knockDown();
            if (pedestrian3 != null) ((Pedestrian) pedestrian3).knockDown();
            return true;
        }

        return false;
    }

    public Vehicle (VehicleSpawner origin) {
        // remember the VehicleSpawner I came from. This includes information
        // about which lane I'm in and which direction I should face
        this.origin = origin;
        moving = true;
        // ask the Spawner that spawned me what my lane number is
        myLaneNumber = origin.getLaneNumber();
        // Determine if this lane is facing towards the right and
        // set the direction accordingly
        if (origin.facesRightward()){ // Right facing vehicles
            direction = 1;        
        } else { // left facing Vehicles
            direction = -1;
            // Reverse the image so it appears correct when moving the opposite direction
            getImage().mirrorHorizontally();
        }
        // If speed modifiers were set for lanes, this will change the max speed
        // accordingly. If speed modifiers are not set, this multiplies by 1.0 (as in,
        // does nothing).
        maxSpeed *= origin.getSpeedModifier();
        speed = maxSpeed;
        isNew = true;

        honk = new GreenfootSound("Voices newspaper rarrgh.mp3");
        honk.setVolume(60);

    }

    /**
     * This method is called automatically when the Vehicle is added to the World, and places
     * the Vehicle just off screen (centered 100 pixels beyond the center of the lane spawner)
     * so it will appear to roll onto the screen smoothly.
     */
    public void addedToWorld (World w){
        if (isNew){
            setLocation (origin.getX() - (direction * 100), origin.getY() - yOffset);
            isNew = false;
        }
    }

    /**
     * The superclass Vehicle's act() method. This can be called by a Vehicle subclass object 
     * (for example, by a Car) in two ways:
     * - If the Vehicle simply does NOT have a method called public void act(), this will be called
     *   instead. 
     * - subclass' act() method can invoke super.act() to call this, as is demonstrated here.
     */
    public void act () {
        drive(); 
        if (!checkHitPedestrian()){
            repelPedestrians();
        }

        if (checkEdge()){
            getWorld().removeObject(this);
        }

    }

    public void increaseSpeed (double amount){
        this.maxSpeed += amount;
        //this.speed = maxSpeed;
    }

    /**
     * A method used by all Vehicles to check if they are at the edge.
     * 
     * Note that this World is set to unbounded (The World's super class is (int, int, int, FALSE) which means
     * that objects should not be stopped from leaving the World. However, this introduces a challenge as there
     * is the potential for objects to disappear off-screen but still be fully acting and thus wasting resources
     * and affecting the simulation even though they are not visible.
     */
    protected boolean checkEdge() {
        if (direction == 1)
        { // if moving right, check 200 pixels to the right (above max X)
            if (getX() > getWorld().getWidth() + 200){
                return true;
            }
        } 
        else 
        { // if moving left, check 200 pixels to the left (negative values)
            if (getX() < -200){
                return true;
            }
        }
        return false;
    }

    // The Repel Pedestrian Experiment - Currently a work in Progress (Feb 2023)
    public void repelPedestrians() {
        ArrayList<Pedestrian> pedsTouching = (ArrayList<Pedestrian>)getIntersectingObjects(Pedestrian.class);
        pushAwayFromObjects(pedsTouching, this.getImage().getHeight()/2);
    }

    // The Repel Pedestrian Experiment (primary method) - Currently a work in Progress (Feb 2023)
    public void pushAwayFromObjects(ArrayList<Pedestrian> nearbyObjects, double minDistance) {

        // Get the current position of this actor
        int currentX = getX();
        int currentY = getY();

        // Iterate through the nearby objects
        for (Pedestrian object : nearbyObjects) {
            if (!object.isAwake()){ continue;}
            // Get the position of the nearby object
            int objectX = object.getX();
            int objectY = object.getY();

            // Calculate the distance between this actor and the nearby object
            double distance = Math.sqrt(Math.pow(currentX - objectX, 2) + Math.pow(currentY - objectY, 2));

            // Check if the distance is less than the minimum required distance
            if (distance < minDistance) {
                // Calculate the direction vector from this actor to the nearby object
                int deltaX = objectX - currentX;
                int deltaY = objectY - currentY;

                // Calculate the unit vector in the direction of the nearby object
                double length = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                double unitX = deltaX / length;
                double unitY = deltaY / length;

                // Calculate the amount by which to push the nearby object
                double pushAmount = minDistance - distance;

                // Update the position of the nearby object to push it away
                object.setLocation(objectX, objectY + (int)(pushAmount * unitY));
                // removed from line above, objectX ==> + (int)(pushAmount * unitX)
            }
        }
    }

    /**
     * Method that deals with movement. Speed can be set by individual subclasses in their constructors
     */
    public void drive() 
    {
        // Ahead is a generic vehicle - we don't know what type BUT
        // since every Vehicle "promises" to have a getSpeed() method,
        // we can call that on any vehicle to find out it's speed
        Vehicle ahead = (Vehicle) getOneObjectAtOffset (direction * (int)(speed + getImage().getWidth()/2 + 6), 0, Vehicle.class);
        double otherVehicleSpeed = -1;
        if (ahead != null) {
            otherVehicleSpeed = ahead.getSpeed();
            speed = ahead.getSpeed();

            if(hasChangedLanes == false) {
                lanecheck = new LaneSpawnCheck();  // checks top lane

                getWorld().addObject (lanecheck, getX(), getY() - 150); //moves vehicle to top lane
                lanecheck2 = new LaneSpawnCheck(); //checks bottom lane
                getWorld().addObject (lanecheck2, getX(), getY() + 150); //moves vehicle to bottom lane

                if(myLaneNumber == 0 || myLaneNumber == 2){
                    if(lanecheck2.safeToChangeLanes() == true && getY() != 0){
                        honk.play();
                        setLocation(getX(), getY() + 127);
                        hasChangedLanes = true;
                    }
                }
                if(myLaneNumber == 1 || myLaneNumber == 4){
                    if(lanecheck.safeToChangeLanes() == true && getY() != 0){
                        honk.play();
                        setLocation(getX(), getY() - 127);
                        hasChangedLanes = true;
                    }   
                }
                if(myLaneNumber == 3 ) {
                    if(lanecheck.safeToChangeLanes() == true && getY() != 0){
                        honk.play();
                        setLocation(getX(), getY() - 127);
                        hasChangedLanes = true;
                    } 
                    else if(lanecheck2.safeToChangeLanes() == true && getY() != 0){
                        honk.play();
                        setLocation(getX(), getY() + 127);
                        hasChangedLanes = true;
                    }
                }
            }

            getWorld().removeObject(lanecheck);
            getWorld().removeObject(lanecheck2);

        }

        // Various things that may slow down driving speed 
        // You can ADD ELSE IF options to allow other 
        // factors to reduce driving speed.

        if (otherVehicleSpeed >= 0 && otherVehicleSpeed < maxSpeed){
            // Vehicle ahead is slower?
            speed = otherVehicleSpeed;

        } else {
            speed = maxSpeed; // nothing impeding speed, so go max speed
        } 

        move (speed * direction);
    }   

    /**
     * An accessor that can be used to get this Vehicle's speed. Used, for example, when a vehicle wants to see
     * if a faster vehicle is ahead in the lane.
     */
    public double getSpeed(){
        if(moving){
            return speed;
        }
        return 0;
    }

}
