import greenfoot.*;
import java.util.List;

public class PotatoMine extends Effect
{
    private GreenfootImage image;
    private GreenfootImage[] explosion;
    private int frame;
    private int delayCounter;
    private int delay;

    private GreenfootSound explodeSound;

    public PotatoMine() {
        explosion = new GreenfootImage[9];
        explodeSound = new GreenfootSound("SFX potato mine.wav");

        for(int i = 0; i < explosion.length; i++){
            if (i == 7 || i == 8) {
                explosion[i] = new GreenfootImage("potatomine" + i + ".png");
                explosion[i].scale(230, 210);
            } else {
                explosion[i] = new GreenfootImage("potatomine" + i + ".png");
                explosion[i].scale(70, 62);
            }
        }
        frame = 0;
        image = explosion[frame];
        setImage(image);
        delay = 15;
        delayCounter = 0;

        if (frame == 7) {
            explodeSound.play();
        }

    }

    public void act()
    {
        if (delayCounter >= delay) {
            if (frame < explosion.length - 1) {
                frame++;
                image = explosion[frame];
                setImage(image);
                if (frame == 7) {
                    explodeSound.play();
                }
            } else {
                
                //gets a list of all objects in range and removes any objects if it is in range 
                //excluding the vehicle spawner
                List<Actor> objectsInRange = getObjectsInRange(130, Actor.class); 

                for (Actor object : objectsInRange) {
                    if(getWorld() != null && !(object instanceof VehicleSpawner)){
                        getWorld().removeObject(object);
                    }
                }
                getWorld().removeObject(this);
            }
            delayCounter = 0;
        } else {
            delayCounter++;
        }
    }
}
