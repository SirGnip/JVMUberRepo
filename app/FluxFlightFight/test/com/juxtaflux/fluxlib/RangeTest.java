package com.juxtaflux.fluxlib;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class RangeTest {
    private final double DELTA = 0.0000000001;

    @Test
    public void canCreateWithOneValueCtor() {
        Range r = new Range(42.0);
        assertEquals(42.0, r.getLo(), DELTA);
        assertEquals(42.0, r.getHi(), DELTA);
    }

    @Test
    public void canCreateWithTwoValueCtor() {
        Range r = new Range(42.0, 99.5);
        assertEquals(42.0, r.getLo(), DELTA);
        assertEquals(99.5, r.getHi(), DELTA);
    }

    @Test
    public void canCreateAndUseEmptyRange() {
        Range r = new Range(42.0, 42.0);
        assertEquals(0.0, r.span(), DELTA);
        assertEquals(42.0, r.mid(), DELTA);
    }

    @Test
    public void throwsExceptionOnUnordered() {
        try {
            Range r = new Range(99.5, 42.0);
            fail("Should have thrown exception");
        } catch (Exception e) {
        }
    }

    @Test
    public void testToString() {
        assertEquals("-5.001,23.998", new Range(-5.001,23.9982).toString());
    }

    @Test
    public void calculatesSpanAndMid() {
        Range r = new Range(20.0, 40.0);
        assertEquals(20.0, r.span(), DELTA);
        assertEquals(30, r.mid(), DELTA);
    }

    @Test
    public void clamping() {
        Range r = new Range(20.0, 40.0);
        assertEquals(30.0, r.clamp(30.0), DELTA);
        assertEquals(20.0, r.clamp(-1.0), DELTA);
        assertEquals(40.0, r.clamp(50.0), DELTA);

        assertEquals(30.0, r.clampLo(30.0), DELTA);
        assertEquals(20.0, r.clampLo(-30.0), DELTA);

        assertEquals(30.0, r.clampHi(30.0), DELTA);
        assertEquals(40.0, r.clampHi(50.0), DELTA);
    }

    @Test
    public void containsValue() {
        Range range = new Range(23.5, 42.7);
        assertFalse(range.contains(23.4999999));
        assertTrue(range.contains(23.5));
        assertTrue(range.contains(40.0));
        assertTrue(range.contains(42.6999999));
        assertFalse(range.contains(42.7)); // edge case
    }

    @Test
    public void containsRange() {
        Range outerRange = new Range(30.0, 60.0);
        Range innerRange = new Range(44.4, 55.5);
        assertTrue(outerRange.contains(innerRange));
    }

    @Test
    public void containsRangeWithEqualLo() {
        Range outerRange = new Range(44.4, 55.5);
        Range innerRange = new Range(44.4, 55.4);
        assertTrue(outerRange.contains(innerRange));
    }

    @Test
    public void notContainsWithEqualHi() {
        Range outerRange = new Range(44.4, 55.5);
        Range innerRange = new Range(44.4, 55.5);
        assertFalse(outerRange.contains(innerRange));
    }

    @Test
    public void includeValue() {
        Range r = new Range(45.5, 55.5);
        r = r.include(50.0);
        assertEquals(45.5, r.getLo(), DELTA);
        assertEquals(55.5, r.getHi(), DELTA);

        r = r.include(100.0);
        assertEquals(45.5, r.getLo(), DELTA);
        assertEquals(100.0, r.getHi(), DELTA);

        r = r.include(-10.1);
        assertEquals(-10.1, r.getLo(), DELTA);
        assertEquals(100.0, r.getHi(), DELTA);
    }

    @Test
    public void includeRange() {
        Range r = new Range(30.0, 40.0);
        r = r.include(new Range(31.0, 39.0));
        assertEquals(30.0, r.getLo(), DELTA);
        assertEquals(40.0, r.getHi(), DELTA);
        r = r.include(new Range(25.0, 35.0));
        assertEquals(25.0, r.getLo(), DELTA);
        assertEquals(40.0, r.getHi(), DELTA);
        r = r.include(new Range(35.0, 50.0));
        assertEquals(25.0, r.getLo(), DELTA);
        assertEquals(50.0, r.getHi(), DELTA);
        r = r.include(new Range(-3.0, 101.0));
        assertEquals(-3.0, r.getLo(), DELTA);
        assertEquals(101.0, r.getHi(), DELTA);
    }

    @Test
    public void lerpReturnsValue() {
        Range r = new Range(30.0, 40.0);
        assertEquals(38, r.lerp(0.8), DELTA);
    }

    @Test
    public void normalizeReturnsValue() {
        Range r = new Range(30.0, 40.0);
        assertEquals(-1.5, r.normalize(15.0), DELTA);
        assertEquals(0.0, r.normalize(30.0), DELTA);
        assertEquals(0.5, r.normalize(35.0), DELTA);
        assertEquals(1.0, r.normalize(40.0), DELTA);
        assertEquals(1.5, r.normalize(45.0), DELTA);
    }

    @Test
    public void generateRandomValueInRange() {
        Random rnd = new Random(424242);
        Range range = new Range(45.6, 45.60001);
        for (int i = 0; i < 1000000; ++i) {
            double v = range.rand(rnd);
            assertTrue(range.contains(v));
        }
    }
}