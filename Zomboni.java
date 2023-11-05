import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;

public class Zomboni extends Vehicle
{

    public Zomboni(VehicleSpawner origin){
        super (origin); // call the superclass' constructor first

        GreenfootImage zomboni = new GreenfootImage("zomboni0.png");
        zomboni.scale(110, 105);
        maxSpeed = 2.5;
        speed = maxSpeed;
        if (direction == 1) {
            zomboni.mirrorHorizontally();
        }


        setImage(zomboni);
    }

    public void act()
    {
        super.act();
        if (getWorld() != null) {
            putIce();
        }

    }

    public void putIce() {
        if (Greenfoot.getRandomNumber(60) == 0) {
            getWorld().addObject(new ZomboniIce(), getX(), getY());
        }
    }


}

