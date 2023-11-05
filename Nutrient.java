import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Nutrient class represents a nutriente in the game.
 * Nutrients move randomly in a circular path and transform Pedestrian objects into explosive PotatoMine objects upon contact.
 * If a Nutrient reaches the world edge, it disappears.
 * */
 
public class Nutrient extends Effect
{
    private int speed = 2; 
    
    private GreenfootSound chime;

    public Nutrient () {
        GreenfootImage nutrient = new GreenfootImage("nutrients.png");
        nutrient.scale(90, 90);
        nutrient.setTransparency(200);
        setImage(nutrient);
        

    }

    public void act()
    {
        if (atWorldEdge()) {
            getWorld().removeObject(this);
            return;
        }

        nutrientMovement();
    }
    
    private void nutrientMovement() {
        if(getWorld() != null) {
            //checks if nutrient is intersecting with a pedestrian
            Pedestrian p = (Pedestrian) getOneIntersectingObject(Pedestrian.class);
            if(p != null && p.isAwake()) {
                // removes the pedestrian and replaces it with a potato mine obeject
                getWorld().removeObject(p);
                PotatoMine potatomine = new PotatoMine();
                getWorld().addObject(potatomine, getX(), getY());
                getWorld().removeObject(this); // removes the nutrient object as well

            } else {
                // Move the Nutrient object smoothly in a circular path
                double angle = Math.toRadians(getRotation()); // Current angle in radians
                int xOffset = (int) (speed * Math.cos(angle)); // Calculate horizontal offset
                int yOffset = (int) (speed * Math.sin(angle)); // Calculate vertical offset
                setLocation(getX() + xOffset, getY() + yOffset); // sets the new location for the nutrient 

                // Change the direction smoothly
                setRotation(getRotation() + Greenfoot.getRandomNumber(20) - 10); // Random change in direction

            }
        }
        
    }

    private boolean atWorldEdge() {
        //checks if the nutrient object is at the edge of the world.
        int x = getX();
        int y = getY();
        int halfWidth = getImage().getWidth() / 2;
        int halfHeight = getImage().getHeight() / 2;
        World world = getWorld();
        return x - halfWidth <= 0 || x + halfWidth >= world.getWidth() - 1 ||
        y - halfHeight <= 0 || y + halfHeight >= world.getHeight() - 1;
    }
}

