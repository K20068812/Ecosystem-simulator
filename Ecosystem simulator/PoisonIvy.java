import java.util.*;

/**
 * A simple model of a Poison Ivy plant.
 * Plants age, propagate, and increase hunger of consumers.
 * 
 * @author Amman Kiani and Aarjav Jain
 * @version 2021.02.24
 */
public class PoisonIvy extends Plant
{
    // Characteristics shared by all Poison Ivy plants (class variables).

    // The age at which a Poison Ivy plant can start to breed.
    private static final int BREEDING_AGE = 3;
    // The age to which a Poison Ivy can live.
    private static final int MAX_AGE = 100;
    // The likelihood of Poison Ivy breeding.
    private static final double BREEDING_PROBABILITY = 0.10;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 1;
    // The food value gained when this plant is consumed.
    private static final int myFoodValue = -5;

    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();

    /**
     * Create a new Poison Ivy. A Poison Ivy may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the Poison Ivy will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public PoisonIvy(boolean randomAge, Field field, Location location)
    {
        super(randomAge, field, location);
    }
    
    /**
     * Gets the food value of this plant.
     * @return The number describing the food value of this plant.
     */
    protected int getFV(){
        return myFoodValue;
    }

    /**
     * Check whether or not this Poison Ivy is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newPoisonIvy A list to return newly born Poison Ivy plants.
     */
    protected void giveBirth(List<Actor> newPoisonIvy)
    {
        // New Poison Ivy plants are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        String curWeather = Weather.getWeather();
        if(curWeather != null && (curWeather.equals("wind") )){
            int births = breed();
            for(int b = 0; b < births && free.size() > 0; b++) {
                Location loc = free.remove(0);
                PoisonIvy strand = new PoisonIvy(false, field, loc);
                newPoisonIvy.add(strand);
            }
        }
    }

    /**
     * Generates a random breeding probability if the
     * weather is misty.
     * @return The breeding probability of this plant.
     */
    protected double getBreedingProbability(){
            return BREEDING_PROBABILITY;
    }
    
    /**
     * @return The age from which this plant can breed.
     */
    protected int getBreedingAge(){
        return BREEDING_AGE;
    }
    
    /**
     * @return The maximum number of offspring this plant
     * can produce.
     */
    protected int getMaxLitterSize(){
        return MAX_LITTER_SIZE;
    }
    
    /**
     * Gets the maximum age this animal can live to.
     * @return The max age.
     */
    protected int getMaxAge(){
        return MAX_AGE;
    }
}