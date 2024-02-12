import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A class representing shared characteristics of animals.

 * @author David J. Barnes, Michael Kölling, Amman Kiani and Aarjav Jain 
 * @version 2021.02.24
 */
public abstract class Animal extends Actor
{
    //The animal's gender (male or not)
    private boolean isMale;
    //The animal's sleep status
    private boolean awake;
    //The level of food currently consumed by the animal.
    private int foodLevel;
    //The time this animal goes to sleep.
    private int bedtime;
    //The time this animal wakes up.
    private int waketime;
    //Whether the animal has a disease or not.
    protected boolean isInfected;
    //The number of days the animal has been sick for
    protected int sickDays;
    //A tracker that allows the synchronisation of age and time
    private int ageTimeTracker = 0;

    // A shared random number generator.
    private static final Random rand = Randomizer.getRandom();
    /**
     * Create a new animal at location in field.
     * 
     * @param randomAge If true, the animal will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Animal(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        isInfected = false;
        sickDays = 0;
        setSleepSchedule();

        if(randomAge) {
            age = (rand.nextInt(getMaxAge()));
            setFoodLevel(rand.nextInt(getMaxFL()));
        }
        else{
            age = 0;
            setFoodLevel(getMaxFL());
        }

        setLocation(location);
        isMale = rand.nextBoolean();
    }
    
    /**
     * Set all necessary traits of animals sleep
     * schedule, i.e. bedtime, waketime, and
     * nocturnal or diurnal
     */
    private void setSleepSchedule(){
        setBedtime(getBedtime());
        setWaketime(getWaketime());

        //Determines whether animal is nocturnal or diurnal
        if (bedtime < waketime){
            //nocturnal
            awake = true;
        }
        else{
            //diurnal
            awake = false;
        }
    }
    
    /**
     * Sets the time this animal goes to sleep
     */
    private void setBedtime(int value){
        bedtime = value;
    }

    /**
     * Sets the time this animal wakes up
     */
    private void setWaketime(int value){
        waketime = value;
    }

    /**
     * Gets the time this animal goes to sleep
     * @return The bedtime
     */
    protected abstract int getBedtime();

    /**
     * Gets the time this animal wakes up.
     * @return The waketime.
     */
    protected abstract int getWaketime();

    /**
     * Gets the maximum capacity of food that can be
     * consumed by this Animal.
     * @return The max food level of this Animal.
     */
    protected abstract int getMaxFL();

    /**
     * Determines whether animal has disease or not
     * @returns true if animal is infected
     */
    protected boolean hasDisease(){
        return isInfected;
    }

    /**
     * Gives the animal disease depending on condition
     * @param bool determines whether animal will get 
     * disease or not
     */
    protected void setInfected(boolean bool){
        isInfected = bool;
    }

    /**
     * Sets the current level of food consumed by the 
     * animal
     * @param level The level of food consumed
     */
    protected void setFoodLevel(int level){
        foodLevel = level;
        if (foodLevel > getMaxFL()){
            foodLevel = getMaxFL();
        }
    }

    /**
     * Gets the current level of food consumed by the 
     * animal
     * @return The current level of food consumed
     */
    protected int getFoodLevel(){
        return foodLevel;
    }

    /**
     * Increment the hunger of the animal by reducing their
     * current level of food consumed. If food level
     * reaches or drops below zero, the animal dies.
     */
    protected void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }  

    /**
     * Gets the maximum age this animal can live to.
     * @return The max age.
     */
    protected abstract int getMaxAge();

    /**
     * Make this animal act - that is: make it do
     * whatever it wants/needs to do.
     * @param newAnimals A list to receive newly born animals.
     */
    public abstract void act(List<Actor> newAnimals);

    /**
     * Sets the gender of this animal
     * @param isMale Determines whether animal is a male
     * or not
     */
    protected void setGender(boolean isMale) {
        isMale = this.isMale;
    }

    /**
     * Gets the gender of this animal
     * @return true if the animal is a male
     */
    protected boolean getGender(){
        return isMale;   
    }

    /**
     * Animals search nearby locations for potential partners
     * to breed with. If the animal is diseased, then the
     * potential partner may get infected. If the potential
     * partner is diseased, then the animal may get infected. 
     * @returns true if animal has found a potential
     * partner to breed with
     */
    protected boolean findPartner()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation(),1,1);
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object being = field.getObjectAt(where);
            Class myClass = this.getClass();
            if(being != null){
                if(being instanceof Animal){
                    Animal animal = (Animal) being;
                    if (animal.hasDisease()){
                        setInfected(Disease.setDiseased(this));
                    }
                    else if(hasDisease()){
                        setInfected(Disease.setDiseased(animal));
                    }
                    if(animal.getClass() == myClass){
                        Animal prospect = (Animal) animal;
                        boolean prospGender = prospect.getGender();
                        boolean myGender = getGender();
                        if (myGender = !prospGender){
                            return true;
                        }
                    }
                }
            } 
        }

        return false;
    }

    /**
     * Returns the probability that the animal can breed.
     * @return The breeding probability.
     */
    protected abstract double getBreedingProbability();  

    /**
     * Determines whether animal is awake or asleep
     * @return true if the animal is awake
     */
    protected boolean isAwake()
    {
        return awake;   
    }

    /**
     * Indicate that the animal is no longer alive.
     * It is removed from the field. If it was
     * diseased at death, it is removed from the
     * HashSet of infected patients
     * @Override
     */
    protected void setDead()
    {
        super.setDead();
        if(hasDisease()){
            Disease.decrementPlagueCount(this);
        }
    }
    
    /**
     * Sets the animal's sleep status to asleep
     */
    protected void setAsleep()
    {
        if(Simulator.getTime() == bedtime) {
            awake = false;
        }
    }
    
    /**
     * Sets the animal's sleep status to awake 
     */
    protected void setAwake(){
        if(Simulator.getTime() == waketime) {
            awake = true;
        }
    }
    
    /**
     * Ignores nearby grass on field and can relocate to the 
     * location of the grass on the field
     * @return The location of the grass for animal to
     * relocate to
     */
    protected Location ignoreGrass()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation(),1,1);
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object food = field.getObjectAt(where);
            if(food instanceof Grass) {
                Grass grass = (Grass) food;
                if(grass.isAlive()) {
                    grass.setDead();
                    return where;
                }
            }
        }
        return null;
    }
    
    /**
     * Increments the age of the animal
     */
    protected void incrementAge()
    {
        ageTimeTracker++;
        if(ageTimeTracker%3 == 0){
            age++;
            if(age > getMaxAge()) {
                setDead();
            }
        }
    }
}
