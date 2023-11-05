import greenfoot.*;
import java.util.List;

public class Portal extends Effect {
    private static final int SUCTION_RADIUS = 50;
    private int actsLeft;
    private GreenfootImage image;
    private GreenfootImage[] portal;
    private int frame;
    private int delayCounter;
    private int delay;

    private GreenfootSound portalSound;

    public Portal(){
        portalSound = new GreenfootSound("portal.wav");
        actsLeft = 500;
        portal = new GreenfootImage[9];
        for(int i = 0; i < portal.length; i++){

            portal[i] = new GreenfootImage("00" + i  + ".png");
            portal[i].scale(435, 435);

        }
        frame = 0;
        image = portal[frame];
        setImage(image);
        delay = 15;
        delayCounter = 0;
    }

    public void act() {
        suckObjects();
        actsLeft--;

        delay = 15; 
        if (delayCounter >= delay) {
            portalSound.play();
            frame = (frame + 1) % portal.length; 
            setImage(portal[frame]); 
            delayCounter = 0; 
        } else {
            delayCounter++; 
        }

        if (actsLeft == 0) {
            portalSound.stop();
            VehicleWorld vw = (VehicleWorld) getWorld();
            if (vw != null) {
                vw.restartSpawn();
            }
            // Check if the portal is still in the world before removing it
            if (getWorld() != null) {
                getWorld().removeObject(this);
            }
        }
    }

    private void suckObjects() {
        List<Actor> allObjects = getWorld().getObjects(Actor.class);
        
        /* 
         * Loops through all objects in the world and calculates the angle between the portal and all objects except for vehicle spawner in the world.
         * It pulls the object 9 pixels at a time at the calculated angle towards the portal. This sets a new location for each object getting pulled
         * Once object hits the sunction radius, the object will be removed (refer to the isInsidePortal method)
         */

        for (Actor object : allObjects) {
            if (!(object instanceof VehicleSpawner) && object != this && isInsidePortal(object)) {
                getWorld().removeObject(object);
            } else if (object != this && !(object instanceof VehicleSpawner)) {
                int dx = getX() - object.getX();
                int dy = getY() - object.getY();
                double angle = Math.atan2(dy, dx);
                int distance = 9; 
                int newX = object.getX() + (int) (Math.cos(angle) * distance);
                int newY = object.getY() + (int) (Math.sin(angle) * distance);
                newX = Math.max(0, Math.min(getWorld().getWidth() - 1, newX));
                newY = Math.max(0, Math.min(getWorld().getHeight() - 1, newY));

                object.setLocation(newX, newY);
            }
        }
    }

    //method to check if an object is inside the portal's sunction radius
    private boolean isInsidePortal(Actor object) {
        int dx = getX() - object.getX();
        int dy = getY() - object.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance <= SUCTION_RADIUS;
    }

}
