package com.juxtaflux.gfluxlib

import javafx.geometry.Point2D
import javafx.geometry.Rectangle2D

class Rect2D {
    /** return scaling factor (as double) and offset (as Point2D) to letter box this rectangle in the given container */
    static List letterboxIn(Rectangle2D scene, Rectangle2D container) {
        def ratioX = container.getWidth() / scene.getWidth()
        def ratioY = container.getHeight() / scene.getHeight()
        def ratio = Math.min(ratioX, ratioY) // do I round down to the nearest .25 or .5? Does this help the scaling algorithm? Don't do any work until I know it is necessary.
        def scaledWidth = scene.getWidth() * ratio
        def scaledHeight = scene.getHeight() * ratio
        assert Math.abs((scaledWidth/scaledHeight) - (scene.getWidth()/scene.getHeight())) < 0.00001
        def offsetX = (container.getWidth() - scaledWidth) / 2
        def offsetY = (container.getHeight() - scaledHeight) / 2
        return [ratio, new Point2D(offsetX, offsetY)]
    }
}

