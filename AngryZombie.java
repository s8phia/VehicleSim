import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The Car subclass
 */
public class AngryZombie extends Vehicle
{

    public AngryZombie(VehicleSpawner origin) {
        super(origin); // call the superclass' constructor
        GreenfootImage newspaperzombie = new GreenfootImage("newspaperzombie0.png");
        newspaperzombie.scale(70, 90);

        // If the vehicle spawns from the left, horizontally flip the image
        if (direction == 1) {
            newspaperzombie.mirrorHorizontally();
        }

        setImage(newspaperzombie);
        maxSpeed = 1.5 + ((Math.random() * 30)/5);
        speed = maxSpeed;
        yOffset = 4;
        followingDistance = 6;
    }

    public void act()
    {
        super.act();
    }

    /**
     * When a Car hit's a Pedestrian, it should knock it over
     */
    public boolean checkHitPedestrian () {
        Pedestrian p = (Pedestrian)getOneObjectAtOffset((int)speed + getImage().getWidth()/2, 0, Pedestrian.class);
        if (p != null)
        {
            p.knockDown();
            return true;
        }
        return false;
    }

    private void checkCollisionWithSun() {
        Sun sun = (Sun) getOneIntersectingObject(Sun.class);
        if (sun != null) {
            World world = getWorld();
            world.removeObject(sun);
        }
    }

    private void checkCollisionWithTashzombie() {
        Trashzombie trashzombie = (Trashzombie) getOneIntersectingObject(Trashzombie.class);
        if (trashzombie != null) {
            World world = getWorld();
            world.removeObject(trashzombie);
        }
    }
}
