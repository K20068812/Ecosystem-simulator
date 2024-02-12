import java.util.ArrayList;
import java.util.Random;
import java.util.HashSet;
import java.util.Set;

/**
 * Computes a probability for animals to be diseased and
 * records statistics.
 *
 * @author Amman Kiani and Aarjav Jain
 * @version 2021.02.24
 */
public class Disease
{
    // A HashSet of all currently infected patients.
    private static HashSet<Animal> infectedPatients;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    // The probability that an animal will catch a disease.
    private static final double DISEASED_PROBABILITY = 0.001;

    /**
     * Constructor for objects of class Disease.
     */
    public Disease()
    {
        infectedPatients = new HashSet<>();
    }

    /**
     * Gets the count of currently infected patients.
     * @return the Integer of the count.
     */
    public static int getPlagueInt() {
        return infectedPatients.size();
    }
    
    /**
     * Removes the previously infected patient from the HashSet.
     * @param animal The animal that no longer has the disease.
     */
    public static void decrementPlagueCount(Animal animal){
        infectedPatients.remove(animal);
    }

    /**
     * Sets an animal diseased based on probability.
     * @param animal The animal that may be diseased.
     * @return true if the double generated is less than
     * the probability of disease.
     */
    public static boolean setDiseased(Animal animal){
        if (rand.nextDouble()<= DISEASED_PROBABILITY) {
            infectedPatients.add(animal);
            return true;
        }
        return false;
    }

    /**
     * Gives an animal a disease.
     * @param animal The animal that will be diseased.
     * @return true always.
     */
    public static boolean giveDisease(Animal animal){
        infectedPatients.add(animal);
        return true;
    }
}
