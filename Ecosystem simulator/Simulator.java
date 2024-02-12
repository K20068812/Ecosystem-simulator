import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;

/**
 * A simple predator-prey simulator, based on a rectangular field
 * containing different species.
 * 
 * @author David J. Barnes, Michael Kölling, Amman Kiani and Aarjav Jain
 * @version 2021.02.24
 */
public class Simulator
{
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 210;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 150;

    // The probability that an animal will be created in any given grid position.
    private static final double ZEBRA_CREATION_PROBABILITY = 0.30;
    private static final double GAZELLE_CREATION_PROBABILITY = 0.30;
    private static final double GIRAFFE_CREATION_PROBABILITY = 0.30;
    private static final double LION_CREATION_PROBABILITY = 0.03;
    private static final double HYENA_CREATION_PROBABILITY = 0.03;

    // The probability that a plant will be created in any given grid position.
    private static final double GRASS_CREATION_PROBABILITY = 0.50;
    private static final double POISONIVY_CREATION_PROBABILITY = 0.01;

    // List of animals in the field.
    private List<Actor> actors;
    // The current state of the field.
    private Field field;

    // The current weather situation
    private Weather weather;

    // The current step of the simulation.
    private static int step;

    private Disease disease;
    // A graphical view of the simulation.
    private SimulatorView view;

    // The current time of the simulation.
    private static int time;
    
    // Determines whether the simulation is to be delayed or not.
    private boolean toBeDelayed;
    
    // Determines whether the simulation is to be stopped or not.
    private boolean isStopped;

    /**
     * Construct a simulation field with default size.
     */
    public Simulator()
    {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }

    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width)
    {
        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }

        actors = new ArrayList<>();
        field = new Field(depth, width);
        weather = new Weather();
        // Create a view of the state of each location in the field.
        view = new SimulatorView(depth, width, this);
        disease = new Disease();

        // Sets delay off by default.
        toBeDelayed = false;
        // Keeps the simulation running by default.
        isStopped = false;

        // Creates custom colors with RGB values
        Color zebraColor = new Color(153, 204, 255);
        Color hyenaColor = new Color(102, 51, 0);
        Color normGrass = new Color(0, 204, 0);
        Color poisonIvyColor = new Color(204, 153, 255);
        Color giraffeColor = new Color(204, 0, 102);

        // Sets the color for actor classes on the grid
        view.setColor(Zebra.class, zebraColor);
        view.setColor(Lion.class, Color.RED);
        view.setColor(Gazelle.class, Color.ORANGE);
        view.setColor(Hyena.class, hyenaColor);
        view.setColor(PoisonIvy.class, poisonIvyColor);
        view.setColor(Giraffe.class, giraffeColor);
        view.setColor(Grass.class, normGrass);
        // Setup a valid starting point.
        reset();
    }

    /**
     * Main method (optional).
     * Creates a new Simulator and runs for 4000 steps.
     */
    public static void main(String[] args) {
        Simulator simulator = new Simulator();
        simulator.runLongSimulation();
    }

    /**
     * @return A string detailing the current time.
     */
    public static String getTimeString(){
        StringBuffer buffer = new StringBuffer();
        buffer.append(time);
        return buffer.toString();
    }

    /**
     * @return The current time of the simulation. 
     */
    public static int getTime(){
        return time;
    }   

    /**
     * Run the simulation from its current state for a reasonably long period.
     * (4000 steps).
     */
    public void runLongSimulation()
    {
        simulate(4000);
    }

    /**
     * Run the simulation from its current state for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps)
    {
        for(int step = 1; step <= numSteps && view.isViable(field); step++) {
            if (isStopped) {
                break;
            }
            simulateOneStep();
            if(toBeDelayed){
                delay(120);   //uncomment this to run more slowly 
            }
        }
    }

    /**
     * Toggles delay for simulation.
     */
    public void toggleDelay() {
        toBeDelayed = !toBeDelayed;
    }

    /**
     * Stops the simulation when called.
     */
    public void stopSim() {
        isStopped = !isStopped;
    }

    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each
     * lion and zebra.
     */
    public void simulateOneStep()     {
        step++;
        
        // Increments the hourly clock every 3 steps.
        if(step %3 == 0) {
            time++;   
        }

        // Cycles the weather every 50 steps.
        if(step % 50 == 0){
            weather.cycle();
        }

        // Rolls over the 24 hour clock.
        if(time == 24){
            time =0;}

        // Provide space for newborn animals.
        List<Actor> newActors = new ArrayList<>();
        // Let all Actors act.
        for(Iterator<Actor> it = actors.iterator(); it.hasNext();) {
            Actor actor = it.next();
            actor.act(newActors);
            if(!actor.isAlive()){ 
                it.remove();
            }
        }
        // Add the newly born actors to the main lists.
        actors.addAll(newActors);

        view.showStatus(step, field, time, weather);
    }

    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        step = 0;
        time = 0;
        actors.clear();
        populate();

        // Show the starting state in the view.
        view.showStatus(step, field, time, weather);
    }

    /**
     * Randomly populate the field with Animals and Plants.
     */
    private void populate()
    {
        Random rand = Randomizer.getRandom();
        field.clear();
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                if(rand.nextDouble() <= LION_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Lion lion = new Lion(true, field, location);
                    actors.add(lion);
                }

                if(rand.nextDouble() <= ZEBRA_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Zebra zebra = new Zebra(true, field, location);
                    actors.add(zebra);
                }

                if(rand.nextDouble() <= GAZELLE_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Gazelle gazelle = new Gazelle(true, field, location);
                    actors.add(gazelle);
                }

                if(rand.nextDouble() <= GRASS_CREATION_PROBABILITY) {
                    Location location = new Location (row,col);
                    Grass grass = new Grass(true, field, location);
                    actors.add(grass);
                }

                if(rand.nextDouble() <= POISONIVY_CREATION_PROBABILITY) {
                    Location location = new Location (row,col);
                    PoisonIvy poisonivy = new PoisonIvy(true, field, location);
                    actors.add(poisonivy);
                }

                if(rand.nextDouble() <= GIRAFFE_CREATION_PROBABILITY) {
                    Location location = new Location (row,col);
                    Giraffe giraffe = new Giraffe(true, field, location);
                    actors.add(giraffe);
                }

                else if(rand.nextDouble() <= HYENA_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Hyena hyena = new Hyena(true, field, location);
                    actors.add(hyena);
                    // else leave the location empty.
                }
            }
        }
    }

    /**
     * Pause for a given time.
     * @param millisec  The time to pause for, in milliseconds
     */
    private void delay(int millisec)
    {
        try {
            Thread.sleep(millisec);
        }
        catch (InterruptedException ie) {
            // wake up
        }
    }
}
