import java.util.List;
import java.util.Random;
import java.util.Iterator;

/**
 * A simple model of a gazelle.
 * Gazelles age, move, eat plants, breed, and die.
 * 
 * @author Amman Kiani and Aarjav Jain
 * @version 2021.02.24
 */
public class Gazelle extends Prey
{
    // Characteristics shared by all gazelles (class variables).

    // The age at which a gazelle can start to breed.
    private static final int BREEDING_AGE = 4;
    // The age to which a gazelle can live.
    private static final int MAX_AGE = 100;
    // The likelihood of a gazelle breeding.
    private static final double BREEDING_PROBABILITY = 0.80;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 9;
    // The food value gained when this animal is consumed.
    private static final int myFoodValue = 3;
    // The time this animal goes to sleep.
    private static final int bedtime = 20;
    // The time this animal wakes up.
    private static final int waketime = 9;
    // The maximum level of food that can be consumed by this animal.
    private static final int maxFL = 15;
    
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();

    /**
     * Create a new gazelle. A gazelle may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the gazelle will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Gazelle(boolean randomAge, Field field, Location location)
    {
        super(randomAge, field, location);
    }
    
    /**
     * @return The max level of food that can be consumed
     * by this Animal.
     */
    protected int getMaxFL(){
        return maxFL;
    }

    /**
     * @return The food value gained by consumption of
     * this animal.
     */
    protected int getFV(){
        return myFoodValue;
    }
    
    /**
     * Determines whether this animal has found a partner of the
     * opposite gender and is of breeding age.
     * @return true if the animal has found a partner and is of
     * breeding age.
     */
    protected boolean canBreed()
    {
        return getAge() >= BREEDING_AGE && findPartner();
    }

    /**
     * @return The max age this animal can live to.
     */
    protected int getMaxAge() {
        return MAX_AGE;   
    }
    
    /**
     * @return The bedtime of this animal.
     */
    protected int getBedtime(){
        return bedtime;
    }
    
    /**
     * @return The waketime of this animal.
     */
    protected int getWaketime(){
        return waketime;
    }
    
    /**
     * Check whether or not this gazelle is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newBabies A list to return newly born gazelles.
     */
    protected void giveBirth(List<Actor> newBabies)
    {
        // New gazelles are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        Class myClass = getClass();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Gazelle young = new Gazelle(false, field, loc);
            if (hasDisease()){
                young.setInfected(Disease.giveDisease(young));
            }
            newBabies.add(young);
        }
    }

    /**
     * Generate a number representing the number of births,
     * if it can breed
     * @return The number of births (may be zero).
     */
    private int breed()
    {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    /**
     * @return The probability this animal can breed.
     */
    public double getBreedingProbability(){
        return BREEDING_PROBABILITY;   
    }

}