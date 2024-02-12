import java.util.List;
import java.util.*;

/**
 * A class representing shared characteristics of plants.
 
 * @author David J. Barnes, Michael Kölling, Amman Kiani, and Aarjav Jain
 * @version 2021.02.24
 */
public abstract class Plant extends Actor
{
    
    // A shared random number.
    private static final Random rand = Randomizer.getRandom();
    /**
     * Create a new plant at location in field.
     * 
     * @param randomAge If true, the plant will have random age
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Plant(boolean randomAge,Field field, Location location)
    {
        super(field, location);
        age = 0;
        if(randomAge) {
            age = rand.nextInt(getMaxAge());
        }
    }
    
    /**
     * This is what the Plant does most of the time - it
     * disperses. Sometimes it will breed or die of old age.
     * @param newPlants A list to return newly born plants.
     */
    public void act(List<Actor> newPlants)
    {
        incrementAge();
        if(isAlive()) {
            giveBirth(newPlants);
        }
    }
    
    /**
     * Increase the age.
     * This could result in the plants death.
     */
    protected void incrementAge()
    {
        age++;
        String myName = this.getClass().getName();
        if(myName.equals("PosionIvy") && age > getMaxAge()) {
            setDead();
        }
    }
    
    /**
     * Gets the maximum age this plant can live to.
     * @return The max age.
     */
    protected abstract int getMaxAge();
    
    /**
     * A Plant can breed if it has reached the breeding age.
     * @return true if the Plant can breed, false otherwise.
     */
    protected boolean canBreed()
    {
        return age >= getBreedingAge();
    }
    
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    protected int breed()
    {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= getBreedingProbability()) {
            births = rand.nextInt(getMaxLitterSize()) + 1;
        }
        return births;
    }
        
    /**
     * Get the maximum number of offspring the plant can
     * produce when giving birth
     * @return The maximum litter size
     */
    protected abstract int getMaxLitterSize();
    
    /**
     * Generates a random breeding probability. 
     * Can be influenced by weather conditions.
     * @return The breeding probability of this plant.
     */
    protected abstract double getBreedingProbability();
    
    /**
     * Get the breeding age of the plant
     * @return The breeding age
     */
    protected abstract int getBreedingAge();
    
    /**
     * Check whether or not this plant is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newGrass A list to return newly born grass.
     */
    protected abstract void giveBirth(List<Actor> newPlants);
    
    /**
     * Gets the food value of this plant.
     * @return The number describing the food value of this plant.
     */
    protected abstract int getFV();
}
