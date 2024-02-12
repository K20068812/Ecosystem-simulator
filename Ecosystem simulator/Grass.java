import java.util.List;
import java.util.Random;
import java.util.Iterator;

/**
 * A simple model of a grass.
 * Grass age, move, breed, and die.
 * 
 * @author Amman Kiani and Aarjav Jain
 * @version 2021.02.24
 */
public class Grass extends Plant
{
    // Characteristics shared by all grass (class variables).

    // The age at which a Grass plant can start to breed.
    private static final int BREEDING_AGE = 3;
    // The age to which a Grass plant can live.
    private static final int MAX_AGE = 20;
    // The likelihood of Grass breeding.
    private static final double BREEDING_PROBABILITY = 0.20;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 1;
    // The food value gained when this plant is consumed.
    private static final int myFoodValue = 1;

    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).
    // The Grass's age.
    private int age;

    /**
     * Create a new grass. A grass may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the grass will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Grass(boolean randomAge, Field field, Location location)
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
     * Check whether or not this grass is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newGrass A list to return newly born grass.
     */
    protected void giveBirth(List<Actor> newGrass)
    {
        // New grass are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        String curWeather = Weather.getWeather();
        if(curWeather != null && (curWeather.equals("rain") || curWeather.equals("mist"))){
            int births = breed();
            for(int b = 0; b < births && free.size() > 0; b++) {
                Location loc = free.remove(0);
                Grass strand = new Grass(false, field, loc);
                newGrass.add(strand);
            }
        }
    }

    /**
     * Generates a random breeding probability if the 
     * weather is misty
     * @return The breeding probability of this plant.
     */
    protected double getBreedingProbability(){
        String curWeather = Weather.getWeather();
        if(curWeather.equals("mist")){ 
            double randomValue = 0.01 + (0.01 - 0.001) * rand.nextDouble();
            return randomValue;
        } 
        else {
            return BREEDING_PROBABILITY;
        }
    }
    
    /**
     * Gets the maximum age this animal can live to.
     * @return The max age.
     */
    protected int getMaxAge(){
        return MAX_AGE;
    }
    
    /**
     * @return The age from which this plant can breed.
     */
    protected int getBreedingAge(){
        return BREEDING_AGE;
    }
    
    /**
     * @return The maximum number of offspring this plant can 
     * produce.
     */
    protected int getMaxLitterSize(){
        return MAX_LITTER_SIZE;
    }
}