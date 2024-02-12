import java.util.List;
import java.util.Iterator;
import java.util.Random;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;

/**
 * A simple model of a hyena.
 * Hyenas age, move, eat prey, breed, combat and die.
 * 
 * @author Amman Kiani and Aarjav Jain
 * @version 2021.02.24
 */
public class Hyena extends Predator
{
    // Characteristics shared by all hyenas (class variables).

    // The age at which a hyena can start to breed.
    private static final int BREEDING_AGE = 50;
    // The age to which a hyena can live.
    private static final int MAX_AGE = 200;
    // The likelihood of a hyena breeding.
    private static double BREEDING_PROBABILITY = 0.20;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 1;
    // The time this animal goes to sleep.
    private static final int bedtime = 4;
    // The time this animal wakes up.
    private static final int waketime = 18;
    // The maximum level of food that can be consumed by this animal.
    private static final int maxFL = 40;
    
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();

    /**
     * Create a hyena. A hyena can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the hyena will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Hyena(boolean randomAge, Field field, Location location)
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
     * Check whether or not this hyena is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newHyenas A list to return newly born hyenas.
     */
    protected void giveBirth(List<Actor> newBabies)
    {
        // New hyenas are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Hyena young = new Hyena(false, field, loc);
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
        if(canBreed() && rand.nextDouble() <= getBreedingProbability()) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    /**
     * @return The probability this animal can breed.
     */
    protected double getBreedingProbability(){
        return BREEDING_PROBABILITY;
    }
}

