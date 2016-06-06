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
    
    public GeneralShotData(String name, String description, double startCount, double endCount) {
        this.name = name;
        this.description = description;
        this.startCount = startCount;
        this.endCount = endCount;
    }

}
