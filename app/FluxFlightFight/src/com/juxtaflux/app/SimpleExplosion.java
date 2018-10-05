package com.juxtaflux.app;

import com.juxtaflux.fluxlib.ActorList;
import com.juxtaflux.fluxlib.Flx;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.stream.IntStream;

/** Return an {@link ActorList} of {@link SimpleParticle}s to represent an explosion */
public abstract class SimpleExplosion {
    public static ActorList make(double x, double y, int count, Color color, Pane parent) {
        ActorList particles = new ActorList();
        IntStream.range(0, count).forEach(i -> {
            double lifetime = Flx.rnd() * 0.7 + 0.3;
            particles.actors.add(new SimpleParticle(color, 2, x, y, lifetime, parent));
        });
        return particles;
    }

    public static ActorList make(Vector2D pos, int count, Color color, Pane parent) {
        return make(pos.getX(), pos.getY(), count, color, parent);
    }
}
