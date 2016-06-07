package data;

import lombok.Getter;

public class GeneralShotData {
    
    @Getter
    private String name;
    @Getter
    private String description;
    @Getter
    private double startCount;
    @Getter
    private double endCount;
    
    /**
     * Constructor.
     * @param name the name of the shot
     * @param description the description of the shot
     * @param startCount the start count of the shot
     * @param endCount the end count of the shot
     */
    public GeneralShotData(String name, String description, double startCount, double endCount) {
        this.name = name;
        this.description = description;
        this.startCount = startCount;
        this.endCount = endCount;
    }

}
