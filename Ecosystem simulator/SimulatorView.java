import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.border.MatteBorder;

/**
 * A graphical view of the simulation grid.
 * The view displays a colored rectangle for each location 
 * representing its contents. It uses a default background color.
 * Colors for each type of species can be defined using the
 * setColor method.
 * 
 * @author David J. Barnes, Michael Kölling, Amman Kiani and Aarjav Jain
 * @version 2021.02.24
 */
public class SimulatorView extends JFrame implements ActionListener
{
    // Colors used for empty locations.
    private static final Color EMPTY_COLOR = Color.white;

    // Color used for objects that have no defined color.
    private static final Color UNKNOWN_COLOR = Color.gray;

    private final String STEP_PREFIX = "Step: ";
    private final String POPULATION_PREFIX = "Population: ";
    private JLabel stepLabel, population, infoLabel, timeLabel, weatherLabel, diseaseLabel;

    private Box verticalBox;
    private JLabel hyenaLabel, lionLabel;
    private JLabel gazelleLabel, giraffeLabel, zebraLabel;
    private JLabel grassLabel, poisonIvyLabel;

    // The individual species' progress bars.
    private JProgressBar hyenaCount, lionCount;
    private JProgressBar gazelleCount, giraffeCount, zebraCount;
    private JProgressBar grassCount, poisonIvyCount;

    // A map for storing colors for participants in the simulation
    private Map<Class, Color> colors;
    // A statistics object computing and storing simulation information
    private FieldStats stats;
    private int hour;

    // The progress bar's panel.
    private JPanel progressPanel;
    private JProgressBar progressBar;

    // The buttons used in the simulation.
    private JButton stopButton;
    private JToggleButton delayButton;

    // The scroll bar used when the window is too small.
    private JScrollPane scrollPane;

    private FieldView fieldView;
    private Simulator simulator;

    private Color defaultColor;
    /**
     * Create a view of the given width and height.
     * @param height The simulation's height.
     * @param width The simulation's width.
     * @param simulator The simulation being run.
     */
    public SimulatorView(int height, int width, Simulator simulator)
    {
        this.simulator = simulator;
        stats = new FieldStats();
        colors = new LinkedHashMap<>();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setTitle("The Savannah simulation");
        setLocation(100, 50);
        
        fieldView = new FieldView(height, width);

        Container contents = getContentPane();

        // Creates a new JPanel and sets the appearance.
        JPanel infoPane = new JPanel();
        infoPane.setBackground(Color.GRAY);
        infoPane.setLayout(new GridLayout(1, 0, 0, 0));
        
        JPanel populationPane = new JPanel();
        populationPane.setBackground(Color.LIGHT_GRAY);
        populationPane.setLayout(new GridLayout(1, 0, 0, 0));

        progressPanel = new JPanel();
        progressPanel.setLayout(new BorderLayout(0, 0));

        
        // Adds the Panes to the container.
        contents.add(fieldView, BorderLayout.CENTER);
        contents.add(infoPane, BorderLayout.NORTH);
        contents.add(populationPane, BorderLayout.EAST);
        contents.add(progressPanel, BorderLayout.SOUTH);

        scrollPane = new JScrollPane();
        populationPane.add(scrollPane);
        verticalBox = Box.createVerticalBox();
        scrollPane.setViewportView(verticalBox);

        // Creates new JLabels in desired location.
        population = new JLabel("Population Statistics: ");
        population.setAlignmentX(Component.RIGHT_ALIGNMENT);
        verticalBox.add(population);
        
        // Adds the statistics for all species.
        addPopulationLabels(verticalBox);

        diseaseLabel = new JLabel();
        diseaseLabel.setHorizontalAlignment(SwingConstants.LEFT);
        diseaseLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        verticalBox.add(diseaseLabel);

        Component verticalStrut_3 = Box.createVerticalStrut(20);
        verticalBox.add(verticalStrut_3);

        stepLabel = new JLabel();
        stepLabel.setForeground(Color.WHITE);
        stepLabel.setHorizontalAlignment(SwingConstants.CENTER);
        infoPane.add(stepLabel);

        timeLabel = new JLabel();
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        infoPane.add(timeLabel);

        weatherLabel = new JLabel();
        weatherLabel.setForeground(Color.WHITE);
        weatherLabel.setHorizontalAlignment(SwingConstants.CENTER);
        infoPane.add(weatherLabel);

        // Creates and implements button functionality
        stopButton = new JButton("Stop");
        infoPane.add(stopButton);

        delayButton = new JToggleButton("Toggle Delay");
        infoPane.add(delayButton);
        
        delayButton.addActionListener(this);
        stopButton.addActionListener(this);
        
        // Creates a progress bar for the simulation.
        progressBar = new JProgressBar();
        progressPanel.add(progressBar, BorderLayout.NORTH);
        progressBar.setMaximum(4000);
        progressBar.setMinimum(0);
        pack();
        setVisible(true);
    }

    /**
     * Define a color to be used for a given class of animal.
     * @param animalClass The animal's Class object.
     * @param color The color to be used for the given class.
     */
    public void setColor(Class animalClass, Color color)
    {
        colors.put(animalClass, color);
    }

    /**
     * Adds the population statistics for all species.
     * @param verticalBox The box in which statistics are added.
     */
    private void addPopulationLabels(Box verticalBox) {
        
        // Defines a custom color for all species.
        Color zebraColor = new Color(153, 204, 255);
        Color hyenaColor = new Color(102, 51, 0);
        Color lionColor = new Color(153, 0, 0);
        Color normGrass = new Color(0, 204, 0);
        Color poisonIvyColor = new Color(204, 153, 255);
        Color giraffeColor = new Color(204, 0, 102);

        Component verticalStrut = Box.createVerticalStrut(10);
        verticalBox.add(verticalStrut);

        // Creates the labels and progress bars for all species.
        hyenaLabel = new JLabel();
        hyenaLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        verticalBox.add(hyenaLabel);
        hyenaCount = new JProgressBar();
        verticalBox.add(hyenaCount);
        hyenaCount.setMaximum(5000);
        hyenaCount.setMinimum(0);
       
        hyenaLabel.setBorder(new MatteBorder(0, 0, 0, 15, hyenaColor));

        lionLabel = new JLabel();
        lionLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        verticalBox.add(lionLabel); 
        lionCount = new JProgressBar();
        verticalBox.add(lionCount);
        lionCount.setMaximum(5000);
        lionCount.setMinimum(0);
        lionLabel.setBorder(new MatteBorder(0, 0, 0, 15, Color.RED));

        Component verticalStrut_1 = Box.createVerticalStrut(10);
        verticalBox.add(verticalStrut_1);

        gazelleLabel = new JLabel();
        gazelleLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        verticalBox.add(gazelleLabel);
        gazelleCount = new JProgressBar();
        verticalBox.add(gazelleCount);
        gazelleCount.setMaximum(5000);
        gazelleCount.setMinimum(0);
        gazelleLabel.setBorder(null);
        gazelleLabel.setBorder(new MatteBorder(0, 0, 0, 15, Color.ORANGE));

        giraffeLabel = new JLabel();
        giraffeLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        verticalBox.add(giraffeLabel);
        giraffeCount = new JProgressBar();
        verticalBox.add(giraffeCount);
        giraffeCount.setMaximum(5000);
        giraffeCount.setMinimum(0);
        giraffeLabel.setBorder(new MatteBorder(0, 0, 0, 15, giraffeColor));

        zebraLabel = new JLabel();
        zebraLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        verticalBox.add(zebraLabel);
        zebraCount = new JProgressBar();
        verticalBox.add(zebraCount);
        zebraCount.setMaximum(5000);
        zebraCount.setMinimum(0);
        zebraLabel.setBorder(new MatteBorder(0, 0, 0, 15, zebraColor));

        Component verticalStrut_2 = Box.createVerticalStrut(10);
        verticalBox.add(verticalStrut_2);

        grassLabel = new JLabel();
        grassLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        verticalBox.add(grassLabel);
        grassCount = new JProgressBar();
        verticalBox.add(grassCount);
        grassCount.setMaximum(5000);
        grassCount.setMinimum(0);
        grassLabel.setBorder(new MatteBorder(0, 0, 0, 15, normGrass));

        poisonIvyLabel = new JLabel();
        poisonIvyLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        verticalBox.add(poisonIvyLabel);
        poisonIvyCount = new JProgressBar();
        verticalBox.add(poisonIvyCount);
        poisonIvyCount.setMaximum(5000);
        poisonIvyCount.setMinimum(0);
        poisonIvyLabel.setBorder(new MatteBorder(0, 0, 0, 15, poisonIvyColor));

        Component verticalStrut_4 = Box.createVerticalStrut(20);
        verticalBox.add(verticalStrut_4);
    }

    /**
     * @param myClass The class for which the colour is to change.
     * @return The color to be used for a given class of animal.
     */
    private Color getColor(Class myClass)
    {
        Color col = colors.get(myClass);
        if(col == null) {
            // no color defined for this class
            return UNKNOWN_COLOR;
        }
        else {
            return col;
        }
    }

    /**
     * Sets the text for the species' labels and values for the species' progress bar.
     * Changes colour for the progress bars depending on survival status.
     * @param field The field whose status is to be displayed.
     */
    private void speciesDisplay(Field field){
        hyenaLabel.setText("  " + stats.getPopulationDetails(field, "Hyena"));
        hyenaCount.setValue(stats.getPopulationCount(field, "Hyena"));
        if((stats.getPopulationCount(field, "Hyena")) >= 750) {
            hyenaCount.setForeground(Color.green);
        }
        else if((stats.getPopulationCount(field, "Hyena")) <= 250) {
            hyenaCount.setForeground(Color.red);
        }
        else { 
            hyenaCount.setForeground(Color.orange);
        }
    
        lionLabel.setText("  " + stats.getPopulationDetails(field, "Lion"));
        lionCount.setValue(stats.getPopulationCount(field, "Lion"));
        if((stats.getPopulationCount(field, "Lion")) >= 750) {
            lionCount.setForeground(Color.green);
        }
        else if((stats.getPopulationCount(field, "Lion")) <= 250) {
            lionCount.setForeground(Color.red);
        }
        else { 
            lionCount.setForeground(Color.orange);
        }

        gazelleLabel.setText("  " + stats.getPopulationDetails(field, "Gazelle"));
        gazelleCount.setValue(stats.getPopulationCount(field, "Gazelle"));
        if((stats.getPopulationCount(field, "Gazelle")) >= 7500) {
            gazelleCount.setForeground(Color.green);
        }
        else if((stats.getPopulationCount(field, "Gazelle")) <= 1500) {
            gazelleCount.setForeground(Color.red);
        }
        else { 
            gazelleCount.setForeground(Color.orange);
        }
        
        giraffeLabel.setText("  " + stats.getPopulationDetails(field, "Giraffe"));
        giraffeCount.setValue(stats.getPopulationCount(field, "Giraffe"));
        if((stats.getPopulationCount(field, "Giraffe")) >= 7500) {
            giraffeCount.setForeground(Color.green);
        }
        else if((stats.getPopulationCount(field, "Giraffe")) <= 1500) {
            giraffeCount.setForeground(Color.red);
        }
        else { 
            giraffeCount.setForeground(Color.orange);
        }
        
        zebraLabel.setText("  " + stats.getPopulationDetails(field, "Zebra"));
        zebraCount.setValue(stats.getPopulationCount(field, "Zebra"));
        if((stats.getPopulationCount(field, "Zebra")) >= 7500) {
            zebraCount.setForeground(Color.green);
        }
        else if((stats.getPopulationCount(field, "Zebra")) <= 1500) {
            zebraCount.setForeground(Color.red);
        }
        else { 
            zebraCount.setForeground(Color.orange);
        }
        
        grassLabel.setText("  " + stats.getPopulationDetails(field, "Grass"));
        grassCount.setValue(stats.getPopulationCount(field, "Grass"));
        
        
        poisonIvyLabel.setText("  " + stats.getPopulationDetails(field, "PoisonIvy"));
        poisonIvyCount.setValue(stats.getPopulationCount(field, "PoisonIvy"));

    }

    /**
     * Show the current status of the field.
     * @param step Which iteration step it is.
     * @param field The field whose status is to be displayed.
     * @param time The time whose status is to be displayed.
     * @param weather The weather whose status is to be displayed.
     */
    public void showStatus(int step, Field field, int time, Weather weather)
    {
        if(!isVisible()) {
            setVisible(true);
        }

        // Sets the step label.
        stepLabel.setText("Steps: " + step);
        stats.reset();
        
        // Sets time label in 24 hour format.
        if(time <= 6 || time >=21){
            if(time <10){
                timeLabel.setText("0" + time + ":00" + " " + "Night-time");
            }
            else{
                timeLabel.setText(time + ":00" + " " + "Night-time");
            }
        }
        else if(time > 6 || time < 21){
            if(time <10){
                timeLabel.setText("0" + time + ":00" + " " + "Day-time");
            }
            else{
                timeLabel.setText(time + ":00"+ " " + "Day-time");
            }
        }

        // Sets the weather label.
        weatherLabel.setText("Weather: " + weather.getWeather() + "   ");

        fieldView.preparePaint();

        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                Object animal = field.getObjectAt(row, col);
                if(animal != null) {
                    stats.incrementCount(animal.getClass());
                    fieldView.drawMark(col, row, getColor(animal.getClass()));
                }
                else {
                    fieldView.drawMark(col, row, EMPTY_COLOR);
                }
            }
        }

        stats.countFinished();

        // Sets the disease label.
        diseaseLabel.setText("Infected patients: " + Disease.getPlagueInt());

        // Sets the species label.
        speciesDisplay(field); 

        // Defines a new custom color that updates according to weather.
        Color rainGrass = new Color(0, 153, 0);
        Color mistGrass = new Color(102, 204, 0);
        if (Weather.getWeather().equals("rain")) {
            setColor(Grass.class, rainGrass);
        }
        else if(Weather.getWeather().equals("mist")) {
            setColor(Grass.class, mistGrass);
        }
        else {
            setColor(Grass.class, Color.GREEN);
        }

        fieldView.repaint();

        // Sets the value of the progress bar based on the step count.
        progressBar.setValue(step);

    }

    /**
     * Determine whether the simulation should continue to run.
     * @param field The field whose status is to be displayed.
     * @return true If there is more than one species alive.
     */
    public boolean isViable(Field field)
    {
        return stats.isViable(field);
    }

    /**
     * Provide a graphical view of a rectangular field. This is 
     * a nested class (a class defined inside a class) which
     * defines a custom component for the user interface. This
     * component displays the field.
     * This is rather advanced GUI stuff - you can ignore this 
     * for your project if you like.
     */
    private class FieldView extends JPanel
    {
        private final int GRID_VIEW_SCALING_FACTOR = 6;

        private int gridWidth, gridHeight;
        private int xScale, yScale;
        Dimension size;
        private Graphics g;
        private Image fieldImage;

        /**
         * Create a new FieldView component.
         */
        public FieldView(int height, int width)
        {
            gridHeight = height;
            gridWidth = width;
            size = new Dimension(0, 0);
        }

        /**
         * Tell the GUI manager how big we would like to be.
         */
        public Dimension getPreferredSize()
        {
            return new Dimension(gridWidth * GRID_VIEW_SCALING_FACTOR,
                gridHeight * GRID_VIEW_SCALING_FACTOR);
        }

        /**
         * Prepare for a new round of painting. Since the component
         * may be resized, compute the scaling factor again.
         */
        public void preparePaint()
        {
            if(! size.equals(getSize())) {  // if the size has changed...
                size = getSize();
                fieldImage = fieldView.createImage(size.width, size.height);
                g = fieldImage.getGraphics();

                xScale = size.width / gridWidth;
                if(xScale < 1) {
                    xScale = GRID_VIEW_SCALING_FACTOR;
                }
                yScale = size.height / gridHeight;
                if(yScale < 1) {
                    yScale = GRID_VIEW_SCALING_FACTOR;
                }
            }
        }

        /**
         * Paint on grid location on this field in a given color.
         */
        public void drawMark(int x, int y, Color color)
        {
            g.setColor(color);
            g.fillRect(x * xScale, y * yScale, xScale-1, yScale-1);
        }

        /**
         * The field view component needs to be redisplayed. Copy the
         * internal image to screen.
         */
        public void paintComponent(Graphics g)
        {
            if(fieldImage != null) {
                Dimension currentSize = getSize();
                if(size.equals(currentSize)) {
                    g.drawImage(fieldImage, 0, 0, null);
                }
                else {
                    // Rescale the previous image.
                    g.drawImage(fieldImage, 0, 0, currentSize.width, currentSize.height, null);
                }
            }
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == delayButton){
            simulator.toggleDelay(); // Toggles delay when button is clicked.
        }
        if (e.getSource() == stopButton){
            simulator.stopSim(); // Stops the simulation when button is clicked.
        }
    }
}
