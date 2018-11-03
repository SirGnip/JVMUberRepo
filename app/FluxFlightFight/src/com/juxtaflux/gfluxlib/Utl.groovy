package com.juxtaflux.gfluxlib

import javafx.scene.Group
import javafx.scene.Node

/** Generic Groovy-based JavaFX utils */
class Utl {
    static <T extends Node> T setPos(double x, double y, T node) {
        node.setTranslateX(x)
        node.setTranslateY(y)
        return node
    }

    static Group group(Node... children) {
        def group = new Group()
        children.each({
            group.getChildren().add(it)
        })
        return group
    }

    /** Custom "with" that returns the value of the object that "with" is operating over, useful for chaining calls.
     *
     * Groovy's "with" evaluates to the value of the last call in the "with" block (Groovy's "return" semantics):
     *
     * def result = myObj.returnWith {
     *     setFoo(42)
     *     setBar("hello")
     * }
     *
     * The "result" object will be whatever "setBar" returns. But, most setters don't return
     * sensible values. This makes "with" hard to use with chaining. You can trick "with" into more chain-friendly
     * behavior like this, but it doesn't read as cleanly:
     *
     * def result = myObj.returnWith {
     *     setFoo(42)
     *     setBar("hello")
     *     it
     * }
     *
     * The "it" will cause the "with" statement to evaluate to the value of "it", which in this case is the "myObj" object.
     * This utl.with() method enables a "with" block that returns a value.
     * def result = utl.returnWith(new MyObj(42)) {
     *     setFoo(42)
     *     setBar("hello")
     * }
     */
    static <T> T returnWith(T obj, Closure closure) {
        closure.setDelegate(obj)
        closure()
        return obj
    }

    /** initialize some metaprogramming items */
    static void metaprogrammingInit() {
        // When using returnWith, the IDE doesn't seem to know the type of the delegate object, so the IDE
        // reports "cannot resolve symbol" for method calls in the closure.
        Object.metaClass.returnWith = { Closure clos ->
            return Utl.returnWith(delegate, clos)
        }
    }
}
