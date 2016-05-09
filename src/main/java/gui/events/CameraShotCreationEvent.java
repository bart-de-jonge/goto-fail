package gui.events;

import lombok.Getter;

import java.util.List;

/**
 * Class that represents a director shot creation event.
 * @author martijn
 */
public class CameraShotCreationEvent extends ModalEvent {

    @Getter
    private String shotName;
    @Getter
    private String shotDescription;
    @Getter
    private List<Integer> camerasInShot;
    @Getter
    private double shotStart;
    @Getter
    private double shotEnd;

    /**
     * Constructor.
     * @param shotName Name of the shot
     * @param shotDescription Description of the shot
     * @param camerasInShot List of cameras in the shot
     * @param start Start count of the shot
     * @param end End count of the shot
     */
    public CameraShotCreationEvent(String shotName, String shotDescription,
                                     List<Integer> camerasInShot, double start,
                                     double end) {
        super(ModalEventType.CONFIRM);
        this.shotName = shotName;
        this.shotDescription = shotDescription;
        this.camerasInShot = camerasInShot;
        this.shotStart = start;
        this.shotEnd = end;
    }
}
