import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.*;

/**
 * A class that represents shared characteristics of
 * predator animals. 
 * Predators age, move, eat prey, breed, combat and die.
 *
 * @author Amman Kiani and Aarjav Jain.
 * @version 2021.02.24
 */
public abstract class Predator extends Animal
{

    // The probabilities that each individual predator will kill during combat.
    private static final double HYENA_KILLING_INSTINCT_PROBABILITY = 0.03;
    private static final double LION_KILLING_INSTINCT_PROBABILITY = 0.05;
    
    // The probability that a predator kills their own species.
    private static final double SAME_KILLING_PROBABILITY = 0.007;
    
    // A shared random number generator.
    private static final Random rand = Randomizer.getRandom();

    /**
     * Create a predator. A predator can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the predator will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Predator(boolean randomAge, Field field, Location location)
    {
        super(randomAge, field, location);
    }

    /**
     * Predators search nearby locations for prey to eat.
     * Updates food level of the animal by adding
     * food value of prey.
     * If the prey is diseased, the predator may get infected.
     * @return The location of prey to be eaten.
     */
    protected Location findFood(){
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation(), 1 ,1);
        if(Weather.getWeather().equals("sun")){
            adjacent = field.adjacentLocations(getLocation(), 2, 2);
        }
        else if(Weather.getWeather().equals("fog")){
            adjacent = field.adjacentLocations(getLocation(), 0, 1);
        }
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()){
            Location where = it.next();
            Object being = field.getObjectAt(where);
            if(being != null){
                String preyName = being.getClass().getName();
                if (being instanceof Prey){
                    Prey prey = (Prey) being;
                    int preyFV = prey.getFV();
                    if (getFoodLevel() + preyFV <= getMaxFL()){
                        prey.setDead();
                        if (prey.hasDisease()){
                            setInfected(Disease.setDiseased(this));
                        }
                        setFoodLevel(getFoodLevel() + preyFV);
                        return where;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Predators can fight amongst eachother (themselves included).
     * Each species has their own probability of emerging victorious.
     * @return The location of killed enemy.
     */
    protected Location combatEnemy()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation(),1,1);
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if (animal != null){
                Class myClass = getClass();
                Class animalClass = animal.getClass();                
                if(animal instanceof Predator && animalClass != myClass) {
                    Predator opponent = (Predator) animal;
                    double OPPONENT_KILLING_PROBABILITY = 0;
                    if (myClass.getName().equals("Lion")){
                        OPPONENT_KILLING_PROBABILITY = LION_KILLING_INSTINCT_PROBABILITY;
                    }
                    else if (myClass.getName().equals("Hyena")){
                        OPPONENT_KILLING_PROBABILITY = HYENA_KILLING_INSTINCT_PROBABILITY;
                    }
                    if(opponent.isAlive() && rand.nextDouble()<= OPPONENT_KILLING_PROBABILITY) { 
                        opponent.setDead();
                        return where;
                    }
                }
                else if(animalClass == myClass) {
                    Predator opponent = (Predator) animal;
                    if(opponent.isAlive() && rand.nextDouble()<= SAME_KILLING_PROBABILITY) { 
                        opponent.setDead();
                        return where;
                    }
                }
            }
        }
        return null;
    }

    /**
     * This is what the lion does most of the time: it hunts for
     * prey. In the process, it might breed, die of hunger,
     * combat, catch a disease or die of old age.
     * @param newBabies A list to return newly born lions.
     */
    public void act(List<Actor> newBabies)
    {
        incrementAge();
        if (sickDays == 5){
            isInfected = !isInfected;
            sickDays = 0;
            Disease.decrementPlagueCount(this);
        }
        if (hasDisease()){
            int randomValue = 1 + rand.nextInt(10);
            age+= randomValue;
            sickDays++;
        }
        setAsleep();
        setAwake();
        if(isAwake()){
            incrementHunger();
            if(isAlive()) {
                giveBirth(newBabies);            
                // Move towards a source of food if found if hungry.
                Location newLocation = null;
                if(newLocation == null){
                    newLocation = combatEnemy();
                }
                if(getFoodLevel() < getMaxFL()){
                    newLocation = findFood();
                }
                if(newLocation == null) { 
                    // No food found - try to move to a free location.
                    newLocation = getField().freeAdjacentLocation(getLocation());
                }
                if (newLocation == null){
                    newLocation = ignoreGrass();
                }
                // See if it was possible to move.
                if(newLocation != null) {
                    setLocation(newLocation);
                    if(!hasDisease()){
                        setInfected(Disease.setDiseased(this));
                    }
                }
                else {
                    // Overcrowding.
                    setDead();
                }
            }
        }
    }
    
    /**
     * @return The bedtime of this animal.
     */
    protected abstract int getBedtime();
    
    /**
     * @return The waketime of this animal.
     */
    protected abstract int getWaketime();
    
    /**
     * Check whether or not this predator is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newBabies A list to return newly born predators.
     */
    protected abstract void giveBirth(List<Actor> newBabies);

    /**
     * @return The probability this animal can breed.
     */
    protected abstract double getBreedingProbability();
}
