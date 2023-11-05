import greenfoot.*;

public class Sunflower extends Pedestrian {
    
    public Sunflower(int direction) {
        super(direction);

        GreenfootImage sunflower = new GreenfootImage("sunflower0.png");
        sunflower.scale(55, 60);
        setImage(sunflower);

    }

    public void act() {
        checkZomboniIce();
        super.act();

    }

    /**
     * Method to handle interaction with AlienSlime
     */
    public void checkZomboniIce() {
        if (isTouching(ZomboniIce.class)) {
            setMaxSpeed(1);
            GreenfootImage coldSunFlower = new GreenfootImage("coldsunflower.png");
            coldSunFlower.scale(55, 60);
            setImage(coldSunFlower);
        } else {
            // Reset maxSpeed to its original value (a random value between 2 and 4)
            setMaxSpeed(2 + Math.random() * 2);
            GreenfootImage backAstronautImage = new GreenfootImage("sunflower0.png");
            backAstronautImage.scale(55, 60);
            setImage(backAstronautImage);
        }
    }
}
