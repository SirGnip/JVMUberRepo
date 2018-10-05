package com.juxtaflux.fluxlib;

import java.util.Random;

/*
The Apache Commons and Google Guava implementation of "Range" are focused on
generic implementation of Ranges, supporting mostly just "contains"

This class is more specialized to offer methods specific to "numeric" ranges
(such as "span", "midpoint")
*/
/*
- Do I SUPPORT both int and double? (just start w/ double)
- How do i support being able to do "include" with a list of values?
    r = new Range(42.5); // single element constructor?
    r.include(23)
    r.include(99.9)

    r = new Range() // default constructor creates a non-implemented Range
    //r.span()        // would fail as range is uninitialized at this point
    r.include(42.5) // initializes internals of range
    r.include(99.9) // expands range

Use case:
- i want to build a range from a list of values. the list of values may be empty.
What is an efficient way to do this?  Can I use streams (doesn't the reduce feature have some kind of ?)?  Have a convenience
static method that takes some kind of iterable?

current thought: support one or two value constructors. This way, the range is always initialized.
Throw exception on incorrect ordering of lo/high. Allow same value for lo/high. Range is then "isEmpty".

Ranges are immutable. have methods like "include" or "extends" return a new Range.

Have the "generate random number in range" be a method? an external util?
*/
/** Represents a numeric range (lo is inclusive, hi is exclusive).
 * though I don't know how rigorously I can enforce this with the imprecision of doubles)
*/
public class Range {
    public class RangeCreationException extends RuntimeException {
        public RangeCreationException(String message) {
            super(message);
        }
    }

    private double lo;
    private double hi;

    /** Generate random range inside the given range */
    public static Range nextRand(Random randGenerator, Range range) {
        double lo = range.rand(randGenerator);
        double hi = range.rand(randGenerator);
        if (hi < lo) {
            return new Range(hi, lo);
        }
        return new Range(lo, hi);
    }

    public Range(double v) {
        lo = v;
        hi = v;
    }

    public Range(double lo, double hi) {
        if (hi < lo) {
            throw new RangeCreationException(String.format("Can not create Range when hi:%f is below lo:%f", hi, lo));
        }
        this.lo = lo;
        this.hi = hi;
    }

    public double getLo() {
        return lo;
    }

    public double getHi() {
        return hi;
    }

    @Override
    public String toString() {
        return String.format("%.3f,%.3f", lo, hi);
    }

    /** tests if given value is inside of range */
    public boolean contains(double v) {
        return v >= lo && v < hi;
    }

    /** tests if given inner range is contained dinside of range */
    public boolean contains(Range innerRange) {
        return innerRange.getLo() >= lo && innerRange.getHi() < hi;
    }

    /** return distance covered by the Range */
    public double span() {
        return hi - lo;
    }

    /** return midpoint value of range */
    public double mid() {
        return ((lo + hi) / 2.0);
    }

    public double clamp(double v) {
        return Flx.clamp(v, lo, hi);
    }

    public double clampLo(double v) {
        return Flx.clampLo(v, lo);
    }

    public double clampHi(double v) {
        return Flx.clampHi(v, hi);
    }

    public double lerp(double u) { return Flx.lerp(lo, hi, u); }

    public double normalize(double v) { return Flx.normalize(v, lo, hi); }

    /** return a range that encompasses the existing range and the given value */
    public Range include(double v) {
        if (v > getHi()) {
            return new Range(getLo(), v);
        }
        if (v < getLo()) {
            return new Range(v, getHi());
        }
        return this;
    }

    /** return a range that encompasses the existing range and the given range */
    public Range include(Range otherRange) {
            return new Range(
                    Math.min(getLo(), otherRange.getLo()),
                    Math.max(getHi(), otherRange.getHi())
            );
    }

    /** Get random number between range lo <= val < hi (though, I think the numeric imprecision means this is a weak guarantee) */
    public double rand(Random randGenerator) {
        return (randGenerator.nextDouble() * span()) + lo;
    }
}
