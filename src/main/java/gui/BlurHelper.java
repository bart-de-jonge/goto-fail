package gui;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by markv on 5/3/2016.
 * Class to assist in quickly blurring behind a supplied node.
 * Also has support for moving elements!
 */
public class BlurHelper {

    private WritableImage writableImage;
    private SnapshotParameters parameters;
    private Bounds bounds;

    @Getter
    private GaussianBlur gaussianBlur;

    @Getter
    private double radius;

    @Getter
    private ImageView imageView;

    @Getter @Setter
    private Node node;

    @Getter @Setter
    private boolean hideNode;


    /**
     * Constructor of class.
     * @param node the node behind which we want to blur!
     */
    public BlurHelper(Node node) {
        this.node = node;
        this.radius = 10.0;
        init();
    }

    /**
     * Constructor of class.
     * @param node the node behind which we want to blur!
     * @param radius basically gaussian blur radius
     */
    public BlurHelper(Node node, double radius) {
        this.node = node;
        this.radius = radius;
        init();
    }

    /**
     * Constructor helper.
     */
    private void init() {
        hideNode = true;
        gaussianBlur = new GaussianBlur(radius);
        parameters = new SnapshotParameters();
        imageView = new ImageView();
        imageView.setEffect(gaussianBlur);
    }

    /**
     * Radius setter, that also updates the blur effect radius.
     * @param radius new radius to use/
     */
    public void setRadius(double radius) {
        this.radius = radius;
        gaussianBlur.setRadius(radius);
    }

    /**
     * Process blur for this BlurHelper, by using the bounds of the node.
     * WARNING: if the node is being dragged or resized, make sure this is called AFTER
     * the node has moved or resized. It may prevent jittering!
     */
    public void processBlurUsingBounds() {
        // get information on current size of object in scene, and set proper parameters
        bounds = node.localToScene(node.getBoundsInLocal());
        Rectangle2D rekt = new Rectangle2D(bounds.getMinX(), bounds.getMinY(),
                bounds.getWidth(), bounds.getHeight());
        parameters.setViewport(rekt);

        // just a catch, in case something goes wrong.
        Point2D imageSize = new Point2D(Math.round(bounds.getWidth()),
                Math.round(bounds.getHeight()));
        if (imageSize.getX() <= 0.0 || imageSize.getY() <= 0.0) {
            return;
        }

        // create writable image using these bounds.
        writableImage = new WritableImage((int) imageSize.getX(), (int) imageSize.getY());

        if (hideNode) { // blurs and returns content behind the node
            double opacity = node.getOpacity();
            node.setOpacity(0.0);
            node.getScene().getRoot().snapshot(parameters, writableImage);
            node.setOpacity(opacity); // restore old node opacity
        } else { // blurs and returns the node
            node.getScene().getRoot().snapshot(parameters, writableImage);
        }

        imageView.setImage(writableImage);
    }

}
