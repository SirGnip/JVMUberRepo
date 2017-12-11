package com.juxtaflux.fluxlib;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.transform.Translate;
import javafx.util.Duration;
import javafx.util.Pair;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.List;
import java.util.Random;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/** General JavaFX, sound, graphics utilities.
 * Will break this into separate classes/packages as more stuff is added. */
public abstract class Flx {
    public static String dbgBounds(Bounds b) {
        return String.format("%.1f,%.1f/%.1f,%.1f", b.getMinX(), b.getMinY(), b.getMaxX(), b.getMaxY());
    }

    public static double boundsMidX(Bounds b) {
        return b.getMinX() + (b.getWidth() / 2);
    }

    public static double boundsMidY(Bounds b) {
        return b.getMinY() + (b.getHeight() / 2);
    }

    public static Vector2D boundsMid(Bounds b1, Bounds b2) {
        double x = (boundsMidX(b1) + boundsMidX(b2)) / 2;
        double y = (boundsMidY(b1) + boundsMidY(b2)) / 2;
        return new Vector2D(x, y);
    }

    /** Convenience function for returning a random double, but creates a new Random object each time */
    public static double rnd() {
        return new Random().nextDouble();
    }

    public static Vector2D rnd(Random rnd, BoundingBox box) {
        return new Vector2D(
                new Range(box.getMinX(), box.getMaxX()).rand(rnd),
                new Range(box.getMinY(), box.getMaxY()).rand(rnd)
        );
    }

    /** Convenience function to return a random item from given list (creates Random object each call)*/
    public static <T> T rndChoice(List<T> items) {
        checkArgument(!items.isEmpty(), "rndChoice was given an empty list of items but requires at least one item");
        return items.get(new Random().nextInt(items.size()));
    }

    /** return a random item from given list */
    public static <T> T rndChoice(List<T> items, Random rnd) {
        checkArgument(!items.isEmpty(), "rndChoice was given an empty list of items but requires at least one item");
        return items.get(rnd.nextInt(items.size()));
    }


    // For future: Random no-repeat: (remember last number generated, then generate random number from set of one less, then map that number to all values except the excluded one (see: https://stackoverflow.com/a/13862514))
    // - what if list is mutated between calls as I'll obviously store the previous value? It invalidates that number. Just assume no mutation?
    // - I should create a separate class. Have a factory method in Flx?

    /** clamp value to range */
    public static double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }

    /** clamp value to lo if is less than min */
    public static double clampLo(double v, double min) {
        if (v < min) {
            return min;
        }
        return v;
    }

    /** clamp value to hi if is more than max */
    public static double clampHi(double v, double max) {
        if (v > max) {
            return max;
        }
        return v;
    }

    /** get vector describing how much a smaller Box has "escaped" a surrounding Box */
    // Problem: what if "smaller" is larger in a dimension than "surround"
    public static Vector2D escapingBy(Bounds smaller, Bounds surround, double slushAmount) {
        double xDepth = 0.0;
        double yDepth = 0.0;
        double x = 0.0;
        double y = 0.0;
        // when "smaller" box penetrates "surround", return offset.
        yDepth = surround.getMaxY() - smaller.getMaxY();
        if (yDepth < 0) {
//            System.out.println("collide bottom");
            y = yDepth - slushAmount;
        } else {
            yDepth = surround.getMinY() - smaller.getMinY();
            if (yDepth > 0) {
//            System.out.println("collide top");
                y = yDepth + slushAmount;
            }
        }
        xDepth = surround.getMaxX() - smaller.getMaxX();
        if (xDepth < 0) {
//            System.out.println("collide right");
            x = xDepth - slushAmount;
        } else {
            xDepth = surround.getMinX() - smaller.getMinX();
            if (xDepth > 0) {
//            System.out.println("collide left");
                x = xDepth + slushAmount;
            }
        }
        return new Vector2D(x, y);
    }

    /** Return new color representing current color with opacity set to 0.5 */
    public static Color alphaize(Color color) {
        return alphaize(color, 0.5);
    }

    /** Return new color representing current color with opacity set to given value */
    public static Color alphaize(Color color, double opacity) {
        checkNotNull(color);
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity);
    }

    /** convenience method for making a line */
    public static Line makeLine(int x1, int y1, int x2, int y2, Paint stroke) {
        checkNotNull(stroke);
        Line vertLine = new Line();
        vertLine.setStroke(stroke);
        vertLine.setStartX(x1);
        vertLine.setStartY(y1);
        vertLine.setEndX(x2);
        vertLine.setEndY(y2);
        return checkNotNull(vertLine);
    }

    /** convenience method for making a cross */
    public static Pane makeCross(int x, int y, int size, Paint stroke) {
        checkNotNull(stroke);
        int offset = size/2;
        Pane pane = new Pane();
        pane.getTransforms().add(new Translate(x, y));
        pane.getChildren().addAll(
                makeLine(0, -offset, 0, offset, stroke),
                makeLine(-offset, 0, offset, 0, stroke)
        );
        return checkNotNull(pane);
    }

    /** convenience method for making a grid */
    public static Pane makeGrid(int x, int y, int width, int height, int gridSize, Color stroke) {
        checkNotNull(stroke);
        Pane pane = new Pane();
        for (int curX = 0; curX <= width; curX += gridSize) {
            pane.getChildren().add(makeLine(curX, 0, curX, height, stroke));
        }
        for (int curY = 0; curY <= height; curY += gridSize) {
            pane.getChildren().add(makeLine(0, curY, width, curY, stroke));
        }
        pane.getTransforms().add(new Translate(x, y));
        return checkNotNull(pane);
    }

    public static Timeline doRepeating(int cycleCount, Duration duration, EventHandler<ActionEvent> onRepeat) {
        final Timeline timeline = new Timeline();
        timeline.setCycleCount(cycleCount);
        timeline.getKeyFrames().add(new KeyFrame(duration, onRepeat));
        timeline.play();
        return timeline;
    }

    public static Timeline doRepeatingForever(Duration duration, EventHandler<ActionEvent> onRepeat) {
        return doRepeating(Animation.INDEFINITE, duration, onRepeat);
    }

    /** sleep current thread for given duration */
    public static void sleep(Duration duration) {
        try {
            Thread.sleep((long) duration.toMillis());
        } catch (Exception e) {
        }
    }

    /** dump JavaFX scene graph. Reference: http://stackoverflow.com/a/21176630 */
    public static void dump(Node n) {
        DumpImpl.dump(checkNotNull(n), 999, 0);
    }

    /** traverse the JavaFX scene graph and print out the basic hierarchy */
    private static class DumpImpl {
        private static void dump(Node n, int maxDepth, int curDepth) {
            checkNotNull(n);
            if (n.getClass().getName().startsWith("com.sun")) {
                return;
            }

            final String oneIndent = "  ";
            String indent = "";

            for (int i = 0; i < curDepth; ++i) {
                indent += oneIndent;
            }
            String msg = indent + n.getClass().getName() + ((n.getId() != null) ? " id:" + n.getId() : "");
            msg += getNodeSpecificTag(n);
            System.out.println(msg);
            if (curDepth < maxDepth && n instanceof Parent && dumpDoRecurse(n)) {
                for (Node child : ((Parent) n).getChildrenUnmodifiable()) {
                    dump(child, maxDepth, curDepth + 1);
                }
            }
        }

        private static boolean dumpDoRecurse(Node n) {
            return !(checkNotNull(n) instanceof Labeled);
        }

        private static String getNodeSpecificTag(Node n) {
            checkNotNull(n);
            String s = " ";
            if (n instanceof Labeled) {
                s += "'" + ((Labeled) n).getText() + "'";
            }
            if (n instanceof Pane) {
                s += "childCnt=" + ((Pane) n).getChildrenUnmodifiable().size();
            }
            if (n instanceof ListView) {
                s += "itemCnt=" + ((ListView) n).getItems().size();
            }
            if (n instanceof TextArea) {
                s += "len=" + ((TextArea) n).getText().length();
            }
            return checkNotNull(s);
        }
    }
}
