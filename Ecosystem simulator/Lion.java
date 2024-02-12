
import java.util.Iterator;
import java.util.Random;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

/**
 * A simple model of a lion.
 * Lions age, move, eat prey, breed, combat and die.
 * 
 * @author Amman Kiani and Aarjav Jain
 * @version 2021.02.24
 */
public class Lion extends Predator
{
    // Characteristics shared by all lions (class variables).

    // The age at which a lion can start to breed.
    private static final int BREEDING_AGE = 85;
    // The age to which a lion can live.
    private static final int MAX_AGE = 270;
    // The likelihood of a lion breeding.
    private static final double BREEDING_PROBABILITY = 0.20;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;
    // The time this animal goes to sleep.
    private static final int bedtime = 20;
    //The time this animal wakes up.
    private static final int waketime = 13;
    // The maximum level of food that can be consumed by this animal.
    private static final int maxFL = 65;

    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();

    /**
     * Create a lion. A lion can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the lion will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Lion(boolean randomAge, Field field, Location location)
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
    public int getMaxAge() {
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
     * Check whether or not this lion is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newBabies A list to return newly born lions.
     */
    protected void giveBirth(List<Actor> newBabies)
    {
        // New lions are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Lion young = new Lion(false, field, loc);
            if (hasDisease()){
                young.setInfected(Disease.giveDisease(young));
            }
            newBabies.add(young);
        }
    }

    /**
     * Generate a number representing the number of births,
     * if it can breed.
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