import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class ZomboniIce extends Effect
{
    private int timer;
    public ZomboniIce () {
        GreenfootImage slimeImage = new GreenfootImage("zomboniice.png");
        slimeImage.scale(95, 95);
        setImage(slimeImage);
    }

    public void act() {
        if (getWorld() != null) { 
            // after a few seconds, remove the object from the world
            if (++timer == 30 * 10) {
                getWorld().removeObject(this);
                return;
            }
        }
    }

}
