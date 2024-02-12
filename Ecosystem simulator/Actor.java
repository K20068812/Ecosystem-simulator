import java.util.List;

/**
 * A class representing shared characteristics of Actors.

 * @author Amman Kiani and Aarjav Jain 
 * @version 2021.02.24
 */
public abstract class Actor
{
    // The actor's field.
    protected Field field;
    // The actor's position in the field.
    protected Location location;
    // Whether the actor is alive or not.
    private boolean alive;
    //The actor's age
    protected int age;

    /**
     * Create a new Actor at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Actor(Field field, Location location){
        alive = true;
        this.field = field;
        setLocation(location);
    }
    
    /**
     * Check whether the animal is alive or not.
     * @return true if the animal is still alive.
     */
    protected boolean isAlive()
    {
        return alive;
    }
    
    /**
     * Sets an Actor alive.
     */
    protected void setAlive(boolean bool){
        alive = bool;
    }
    
    /**
     * Sets an Actor dead and removes it from the field.
     */
    protected void setDead(){
        setAlive(false);
        if(getLocation() != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }
    
     /**
     * Make this Actor act - that is: make it do
     * whatever it wants/needs to do.
     * @param newActors A list to receive newly born actors.
     */
    protected abstract void act(List<Actor> newActors);
    
    /**
     * Gets the current age of the animal
     * @return The age of the animal
     */
    protected int getAge(){
        return age;   
    }
    
    /**
     * Return the plant's location.
     * @return The plant's location.
     */
    protected Location getLocation()
    {
        return location;
    }
    
    /**
     * Return the animal's field.
     * @return The animal's field.
     */
    protected Field getField()
    {
        return field;
    }
    
    /**
     * Place the animal at the new location in the given field.
     * @param newLocation The animal's new location.
     */
    public void setLocation(Location newLocation)
    {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }
}