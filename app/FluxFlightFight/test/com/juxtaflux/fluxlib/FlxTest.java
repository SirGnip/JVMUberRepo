package com.juxtaflux.fluxlib;

import javafx.scene.paint.Color;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.juxtaflux.fluxlib.Flx.*;
import static org.junit.Assert.*;


/** Unit tests for {@link Flx} */
public class FlxTest {
    private final double DELTA = 0.0000000001;
    private final Random rnd = new Random(123);

    @Test
    public void alphaize() throws Exception {
        Color c = Flx.alphaize(Color.RED, 0.2);
        assertEquals(new Color(1.0, 0, 0, 0.2), c);
    }

    @Test
    public void rndChoiceWorksWithDifferentTypes() {
        List<String> strings = Arrays.asList("abc", "def", "ghi");
        assertTrue(strings.contains(rndChoice(strings, rnd)));
        List<Integer> integers = Arrays.asList(2, 4, 6, 8);
        assertTrue(integers.contains(rndChoice(integers, rnd)));
    }

    @Test
    public void rndChoiceThrowsWithEmptyList() {
        try {
            rndChoice(new ArrayList<>(), rnd);
            fail("Should have thrown exception");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void checkClamps() {
        assertEquals(-1.0, clamp(-1.0, -2.0, 5.0), DELTA);
        assertEquals(-2.0, clamp(-10.0, -2.0, 5.0), DELTA);
        assertEquals(5.0, clamp(10.0, -2.0, 5.0), DELTA);

        assertEquals(-1.0, clampLo(-1.0, -2.0), DELTA);
        assertEquals(-2.0, clampLo(-10.0, -2.0), DELTA);

        assertEquals(-1.0, clampHi(-1.0, 5.0), DELTA);
        assertEquals(5.0, clampHi(10.0, 5.0), DELTA);
    }

    @Test
    public void testLerp() {
        assertEquals(15.0, lerp(30.0, 40.0, -1.5), DELTA);
        assertEquals(30.0, lerp(30.0, 40.0, 0.0), DELTA);
        assertEquals(35.0, lerp(30.0, 40.0, 0.5), DELTA);
        assertEquals(40.0, lerp(30.0, 40.0, 1.0), DELTA);
        assertEquals(45.0, lerp(30.0, 40.0, 1.5), DELTA);
    }

    @Test
    public void testNormalize() {
        assertEquals(-1.5, normalize(15.0, 30.0, 40.0), DELTA);
        assertEquals(0.0, normalize(30.0, 30.0, 40.0), DELTA);
        assertEquals(0.5, normalize(35.0, 30.0, 40.0), DELTA);
        assertEquals(1.0, normalize(40.0, 30.0, 40.0), DELTA);
        assertEquals(1.5, normalize(45.0, 30.0, 40.0), DELTA);
    }
}