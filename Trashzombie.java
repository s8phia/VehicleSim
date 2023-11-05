import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The Bus subclass
 */
public class Trashzombie extends Vehicle
{
    public static final int STOP_DURATION = 60;

    private int stopActsLeft;
    
    private GreenfootSound inTrash;
    public Trashzombie(VehicleSpawner origin){
        super (origin); // call the superclass' constructor first

        GreenfootImage penny = new GreenfootImage("trashcanzombie0.png");
        penny.scale(70, 85);

        // If the vehicle spawns from the left, horizontally flip the image
        if (direction == 1) {
            penny.mirrorHorizontally();
        }


        setImage(penny);

        stopActsLeft = 0;
        //Set up values for Bus
        maxSpeed = 1.5 + ((Math.random() * 10)/5);
        speed = maxSpeed;
        // because the Bus graphic is tall, offset it a up (this may result in some collision check issues)
        yOffset = 15;
        inTrash = new GreenfootSound("Voices gulp.mp3");
    }

    /**
     * Act - do whatever the Bus wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        if(moving) {
            super.act();
        }
        else {
            if(stopActsLeft == 0 ) {
                moving = true;
            }
            else {
                stopActsLeft--;
            }
        }
    }

    public boolean checkHitPedestrian () {
        // currently empty
        Pedestrian p = (Pedestrian)getOneObjectAtOffset((int)speed + getImage().getWidth() / 2, 0, Pedestrian.class);

        if(p != null && p.isAwake()) {
            moving = false;
            stopActsLeft = STOP_DURATION;
            inTrash.setVolume(50);
            inTrash.play();

            getWorld().removeObject(p);

            return true;
        }
        return false;
    }
}
