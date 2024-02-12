import java.util.Random;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
/**
 * Computes and generates random weather conditions
 * 
 *
 * @author Amman Kiani and Aarjav Jain
 * @version 2021.02.24
 */
public class Weather
{
    // An ArrayList of all possible weather types.
    private static ArrayList<String> allWeatherTypes;
    // The number of all possible weather types.
    private static int numOfWeathers;
    // The current weather condition.
    private static String curWeather;

    /**
     * Constructor for objects of class Weather.
     */
    public Weather()
    {
        // initialise instance variables
        allWeatherTypes = new ArrayList<>();
        addWeathers();
        cycle();
    }
    
    /**
     * Adds all the possible weather conditions
     * and updates the total number of weather
     * conditions.
     */
    private static void addWeathers(){
        allWeatherTypes.add("rain");
        allWeatherTypes.add("sun");
        allWeatherTypes.add("fog");
        allWeatherTypes.add("wind");
        allWeatherTypes.add("mist");
        numOfWeathers = allWeatherTypes.size();
    }

    /**
     * Cycles the current weather condition with
     * a random weather condition.
     */
    public static void cycle(){
        Random rand = new Random();
        int x = rand.nextInt(numOfWeathers);
        setWeather(x);
    }
    
    /**
     * Sets and updates the current weather condition.
     * @param x The input for the desired weather condition.
     */
    public static void setWeather(int x){
        String weather = allWeatherTypes.get(x);
        curWeather = weather;
    }
    
    /**
     * @return The current weather condition.
     */
    public static String getWeather(){
        return curWeather;
    }
}