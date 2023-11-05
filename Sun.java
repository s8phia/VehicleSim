import greenfoot.*;

public class Sun extends Vehicle
{
    private GreenfootImage[] sunImages;
    private int frame;
    private int delayCounter;
    private int delay;

    public Sun(VehicleSpawner origin) {
        super(origin);
        sunImages = new GreenfootImage[2];

        for(int i = 0; i < sunImages.length; i++){
                sunImages[i] = new GreenfootImage("sun" + i + ".png");
                sunImages[i].scale(80, 80);
        }

        setImage(sunImages[0]); // Set initial image

        maxSpeed = 2.5;
        speed = maxSpeed;
        frame = 0;
        delay = 20; // Set a delay for the animation speed
        delayCounter = 0;
    }

    public void act() {
        super.act();

        // Animate the sun
        if (delayCounter >= delay) {
            frame = (frame + 1) % sunImages.length; // Cycle through images
            setImage(sunImages[frame]); // Set the current frame
            delayCounter = 0;
        } else {
            delayCounter++;
        }

        // Rest of your act() method code...
    }

    public boolean checkHitPedestrian() {
        Pedestrian p = (Pedestrian) getOneObjectAtOffset((int) speed + getImage().getWidth() / 2, 0, Pedestrian.class);
        if (p != null && !p.isAwake()) {
            p.healMe();
            return true;
        }
        return false;
    }
}
