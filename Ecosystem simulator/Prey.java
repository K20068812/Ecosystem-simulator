import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * A class that represents shared characteristics of
 * prey animals.
 * Prey age, move, eat plants, breed, and die.
 *
 * @author Amman Kiani and Aarjav Jain.
 * @version 2021.02.24
 */
public abstract class Prey extends Animal
{
    
    //A shared random number generator.
    private static final Random rand = Randomizer.getRandom();
    
    /**
     * Create a prey. A prey can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the prey will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Prey(boolean randomAge, Field field, Location location)
    {
        super(randomAge, field, location);
    }

    /**
     * This is what the prey does most of the time: it searches for
     * grass. In the process, it might breed, die of hunger, catch
     * a disease or die of old age.
     * @param newBabies A list to return newly born prey.
     */
    public void act(List<Actor> newBabies)
    {
        incrementAge();
        if (sickDays == 5){
            isInfected = !isInfected;
            sickDays = 0;
            Disease.decrementPlagueCount(this);
        }
        setAsleep(); 
        setAwake();
        if(isAwake()){
            if (hasDisease()){
                int randomValue = 1 + rand.nextInt(10);
                age+= randomValue;
                sickDays++;
            }
            incrementHunger();
            if(isAlive()) {
                giveBirth(newBabies);            
                // Move towards a source of food if found.
                Location newLocation = null;
                if(getFoodLevel() < getMaxFL()){
                    newLocation = findFood();
                }

                if(newLocation == null) { 
                    // No food found - try to move to a free location.
                    newLocation = getField().freeAdjacentLocation(getLocation());
                }
                // See if it was possible to move.
                if (newLocation == null){
                    newLocation = ignoreGrass();
                }
                if(newLocation != null) {
                    setLocation(newLocation);
                }
                else {
                    // Overcrowding.
                    setDead();
                }
            }
        }
    }

    /**
     * Prey search nearby locations for plants to eat.
     * Updates food level of the animal by adding
     * food value of plant.
     * If prey eat Poison Ivy, they become hungrier.
     * @return The location of food to be eaten.
     */
    protected Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation(),1,1);
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object food = field.getObjectAt(where);
            if(food instanceof Plant) {
                Plant plant = (Plant) food;
                if(plant.isAlive() && plant.getAge() >= plant.getBreedingAge()) {
                    int plantFV = plant.getFV();
                    if (getFoodLevel() + plantFV <= getMaxFL()){
                        plant.setDead();
                        setFoodLevel(getFoodLevel() + plantFV);
                        return where;
                    }
                }
            }
        }
        return null;
    }

    /**
     * @return The food value gained by consumption
     * of this animal.
     */
    protected abstract int getFV();

    /**
     * Check whether or not this prey is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newBabies A list to return newly born prey.
     */
    protected abstract void giveBirth(List<Actor> newBabies);
}