import greenfoot.*;

public class Wallnut extends Pedestrian {
    private GreenfootImage[] wallnutRoll;
    private int frame;
    private int delayCounter;
    private int delay;
    private boolean isRolling;
    
    private GreenfootSound rolling;

    public Wallnut(int direction) {
        super(direction);
        // Initialize the wallnutRoll array and images here
        wallnutRoll = new GreenfootImage[5];
        for(int i = 0; i < wallnutRoll.length; i++) {
            wallnutRoll[i] = new GreenfootImage("walnutRoll" + i + ".png");
            wallnutRoll[i].scale(55, 60);
        }
        isRolling = false;
        
        rolling = new GreenfootSound ("SFX roll in.mp3");
    }

    public void act() {
        super.act(); 

        if (getWorld() != null) {
            if (!isAwake()) {
                GreenfootImage walnut1Image = new GreenfootImage("walnut1.png");
                walnut1Image.scale(55, 60);
                setImage(walnut1Image);
            } else {
                if (getOneIntersectingObject(ZomboniIce.class) != null) {
                    if (!isRolling) {
                        isRolling = true;
                        rolling.play();
                        frame = 0;
                    }

                    delay = 15;
                    if (delayCounter >= delay) {
                        frame = (frame + 1) % wallnutRoll.length;
                        setImage(wallnutRoll[frame]);
                        delayCounter = 0;
                    } else {
                        delayCounter++;
                    }
                } else {
                    isRolling = false;
                    GreenfootImage wallNut = new GreenfootImage("walnut0.png");
                    wallNut.scale(55, 60);
                    setImage(wallNut);
                }
            }
            
        }
    }
}
