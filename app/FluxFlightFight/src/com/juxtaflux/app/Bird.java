package com.juxtaflux.app;

import com.juxtaflux.fluxlib.Actor;
import com.juxtaflux.fluxlib.Flx;
import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.juxtaflux.fluxlib.Flx.*;

/** Represents the main create the player and robots control in this game */
public class Bird implements Actor {
    public final double size = 40.0; // roughly...
    private String name;
    private Color color;
    private DoubleProperty x;
    private DoubleProperty y;
    private IntegerProperty score; // TODO: theoretically this probably should be on some kind of player object, not the Actor
    private Polygon poly;
    private Polygon deathPoly;
    private Vector2D vel = new Vector2D(0, 120);
    private Vector2D gravity = new Vector2D(0, 400);
    private double maxYVel = 600;
    private double maxXVel = 600;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    enum MotionState {
        FLYING, WALKING
    }
    private MotionState motionState = MotionState.FLYING;
    private enum Facing {
        LEFT, RIGHT, NEUTRAL
    }
    private Facing facing = Facing.NEUTRAL;
    private boolean isFlapDown = false;

    private final double VERT_IMPULSE = 100.0;
    private final double HORIZ_IMPULSE = 60.0;
    private final double HORIZ_WALK_SPEED = 100;
    private final Vector2D RIGHT_IMPULSE = new Vector2D(HORIZ_IMPULSE, 0);
    private final Vector2D LEFT_IMPULSE = new Vector2D(-HORIZ_IMPULSE, 0);
    private final Vector2D UP_IMPULSE = new Vector2D(0, -VERT_IMPULSE);
    private final Vector2D LEFT_WALK = new Vector2D(-HORIZ_WALK_SPEED, 0);
    private final Vector2D RIGHT_WALK = new Vector2D(HORIZ_WALK_SPEED, 0);

    public Bird(double x, double y, String name, Color color, Pane parent, int startScore) {
        this.name = name;
        this.color = color;
        makeGraphRoot();
        parent.getChildren().addAll(poly, deathPoly);
        this.x.setValue(x);
        this.y.setValue(y);
        this.score = new SimpleIntegerProperty(startScore);
    }

    public double getX() {
        return x.get();
    }
    public double getY() { return y.get(); }
    public Color getColor() { return color; }
    public String getName() { return name; }
    public Vector2D getVel() { return vel; }
    public void changeScore(int scoreDelta) { score.set(score.get() + scoreDelta); }
    public IntegerProperty scoreProperty() {
        return score;
    }


    @Override
    public void step(double delta) {
        Vector2D offset = new Vector2D(delta, vel);
        Vector2D newPos = new Vector2D(x.get(), y.get()).add(offset);
        if (motionState == MotionState.FLYING) {
            vel = vel.add(new Vector2D(delta, gravity));
        }

        // clamp velocity
        vel = vel.getY() > maxYVel ? new Vector2D(vel.getX(), maxYVel) : vel;
        vel = vel.getX() > maxXVel ? new Vector2D(maxXVel, vel.getY()) : vel;
        vel = vel.getX() < -maxXVel ? new Vector2D(-maxXVel, vel.getY()) : vel;

        x.setValue(newPos.getX());
        y.setValue(newPos.getY());
    }

    public boolean isWalking() {
        return motionState == MotionState.WALKING;
    }

    public void enterWalking(double floorLockY) {
        motionState = MotionState.WALKING;
        // TODO: need to clamp or decay xvelocity
        vel = new Vector2D(vel.getX(), 0);
        y.setValue(floorLockY);
    }

    public void enterFlying() {
        motionState = MotionState.FLYING;
    }

    @Override
    public boolean canReap() {
        return false;
    }

    @Override
    public void reapImpl() {
        // noop
    }

    // block bird from leaving top or bottom, wrap on left and right sides
    public void handleEdges(Bounds edges) {
        final double halfWidth = (size/2) + 10; // half of the width of bird plus some slush
        double slushAmount = 1;
        Vector2D offset = escapingBy(getBounds(), edges, slushAmount);
        if (offset.getX() < 0.0) {
            x.setValue(halfWidth);
        } else if (offset.getX() > 0.0) {
            x.setValue(edges.getMaxX() - halfWidth);
        }
        if (offset.getY() != 0.0) {
            y.setValue(y.get() + offset.getY());
            vel = new Vector2D(vel.getX() * 0.95, 0); // stop any Y velocity and dampen X velocity // NOTE: this is NOT frame-rate independent
        }
    }

    // block bird when crossing any edge
//    public void handleEdges(Bounds edges) {
//        double slushAmount = 1;
//        Vector2D offset = escapingBy(getBounds(), edges, slushAmount);
//        x.setValue(x.get() + offset.getX());
//        y.setValue(y.get() + offset.getY());
//        vel = Vector2D.ZERO;
//    }

    public void setVel(Vector2D vel) {
        this.vel = vel;
    }

    private void addVel(Vector2D vel) {
        this.vel = this.vel.add(vel);
    }

    // Naive input interface
    public void handleRight() {
        addVel(RIGHT_IMPULSE);
    }
    public void handleLeft() {
        addVel(LEFT_IMPULSE);
    }
    public void handleFlap() {
        addVel(UP_IMPULSE);
    }

    private void calcDirection() {
        if (leftPressed && rightPressed) {
            // noop
        } else if (leftPressed) {
            facing = Facing.LEFT;
            poly.setScaleX(-1);
            if (isWalking()) {
                setVel(LEFT_WALK);
            }
        } else if (rightPressed) {
            facing = Facing.RIGHT;
            poly.setScaleX(1);
            if (isWalking()) {
                setVel(RIGHT_WALK);
            }
        } else {
            facing = Facing.NEUTRAL;
            if (isWalking()) {
                setVel(Vector2D.ZERO);
            }
        }
    }

    // Input: Button presses
    public void handlePressRight() {
        rightPressed = true;
        calcDirection();
    }
    public void handlePressLeft() {
        leftPressed = true;
        calcDirection();
    }
    public boolean handlePressFlap() {
        if (isFlapDown) {
            return false;
        }
        isFlapDown = true;
        if (isWalking()) {
            System.out.println("enter flying via flap");
            enterFlying();
            y.setValue(y.getValue()-2.0); // give extra bump to make sure Bird isn't still colliding with floor
        }
        switch (facing) {
            case RIGHT: handleFlap(); handleRight(); break;
            case LEFT: handleFlap(); handleLeft(); break;
            case NEUTRAL: handleFlap(); break;
            default:
                checkArgument(false, "Unexpected 'Facing' enum value");
        }
        return true;
    }
    public void handleReleaseRight() {
        rightPressed = false;
        calcDirection();
    }
    public void handleReleaseLeft() {
        leftPressed = false;
        calcDirection();
    }
    public void handleReleaseFlap() {
        isFlapDown = false;
    }
    // Input: POV
    public void handleDirectionRight() {
        handlePressRight();
    }
    public void handleDirectionLeft() {
        handlePressLeft();
    }
    public void handleDirectionRelease() {
        handleReleaseRight();
        handleReleaseLeft();
    }

    public Bounds getBounds() {
        return poly.getBoundsInParent();
    }

    private Node makeGraphRoot() {
        poly = new Polygon();
        deathPoly = new Polygon();
        double s = size / 2;
        double s2 = (size / 2) + 5;
        double s3 = 5.0;
        poly.getPoints().addAll(new Double[]{
                0.0, s, // bottom
                -s, 0.0, // left
                0.0, -s, // top
                s, 0.0, // right
                s2, -s3, // beak
                s2, s3, // beak
                s, 0.0 // right
        });
        deathPoly.getPoints().addAll(poly.getPoints());
        poly.setFill(color);
        deathPoly.setFill(color);
        poly.setStroke(color);
        deathPoly.setStroke(color);
        deathPoly.setOpacity(0.0);
        x = poly.translateXProperty();
        y = poly.translateYProperty();

        RotateTransition rot = new RotateTransition(Duration.seconds(0.3), poly);
        rot.setFromAngle(-10);
        rot.setByAngle(20);
        rot.setCycleCount(Animation.INDEFINITE);
        rot.setAutoReverse(true);
        rot.play();

        return poly;
    }

    public Polygon doDie() {
        deathPoly.setTranslateX(x.getValue());
        deathPoly.setTranslateY(y.getValue());
        List<Double> startingX = Arrays.asList(50.0, 250.0, 450.0, 650.0, 850.0, 1050.0);
        x.setValue(Flx.rndChoice(startingX));
        y.setValue(650);
        vel = Vector2D.ZERO;
        return deathPoly;
    }
}
