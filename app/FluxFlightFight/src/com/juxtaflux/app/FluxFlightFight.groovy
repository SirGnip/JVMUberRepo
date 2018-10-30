package com.juxtaflux.app

import com.juxtaflux.experiments.ExampleBase
import com.juxtaflux.fluxlib.ActorList
import com.juxtaflux.fluxlib.Flx
import com.juxtaflux.fluxlib.FrameStepper
import com.juxtaflux.fluxlib.Stepable
import javafx.animation.ScaleTransition
import javafx.beans.value.ChangeListener
import javafx.geometry.BoundingBox
import javafx.geometry.Bounds
import javafx.geometry.Insets
import javafx.scene.control.Label
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.Pane
import javafx.scene.media.AudioClip
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.stage.Stage
import javafx.util.Duration
import net.java.games.input.Component
import net.java.games.input.Controller
import net.java.games.input.ControllerEnvironment
import net.java.games.input.Event
import net.java.games.input.EventQueue
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D

import static com.google.common.base.Preconditions.checkNotNull
import static com.juxtaflux.fluxlib.Flx.alphaize
import static com.juxtaflux.fluxlib.Flx.boundsMid
import static com.juxtaflux.fluxlib.Flx.rndChoice

class FluxFlightFight extends ExampleBase implements Stepable {
    private List<Bird> birds = new ArrayList<>()
    private ActorList actorList = new ActorList()
    private FrameStepper stepper
    private BoundingBox edges = new BoundingBox(50, 50, width-50, height-50)
    private AudioClip laserClip
    private List<AudioClip> flapClips = new ArrayList<>()
    private Pane graphRoot
    private Random rnd = new Random()
    private Controller joyController
    private def inputMap = [:]

    private AudioClip loadAudio(String resourceName) {
        System.out.println("Loading audio from resource: " + resourceName)
        URL url = getClass().getResource(resourceName)
        checkNotNull(url)
        System.out.println("Loading audio from URL: " + url.toString())
        return new AudioClip(url.toString())
    }

    List<Bird> getBirds() { return birds }

    @Override
    protected void buildRoot(Stage stage, Pane pane) {
        graphRoot = pane
        graphRoot.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)))

        // set up sound
        laserClip = loadAudio("/resources/audio/laser.wav")
        flapClips.add(loadAudio("/resources/audio/flap1.wav"))
        flapClips.add(loadAudio("/resources/audio/flap2.wav"))
        flapClips.add(loadAudio("/resources/audio/flap3.wav"))

        // birds
        int goalLevel = 500
        int initialScore = 0
        double startX = 100
        birds.add(new Bird(startX, goalLevel,"Blue", alphaize(Color.BLUE), graphRoot, initialScore))
        startX += 100
        birds.add(new Bird(startX, goalLevel,"Red", alphaize(Color.RED), graphRoot, initialScore))
        startX += 100
        birds.add(new Bird(startX, goalLevel,"Green", alphaize(Color.GREEN), graphRoot, initialScore))
        startX += 100
        birds.add(new Bird(startX, goalLevel,"Yellow", alphaize(Color.YELLOW), graphRoot, initialScore))

        // scoreboard
        int scoreX = 50
        Font scoreFont = new Font(30)
        for (Bird bird: birds) {
            Label score = new Label(bird.getName() + ": " + initialScore)
            score.setTextFill(bird.getColor())
            score.setFont(scoreFont)
            score.setTranslateX(scoreX += 170)
            score.setTranslateY(5)

            ScaleTransition pulse = new ScaleTransition(Duration.seconds(0.4), score)
            pulse.setByX(1.2)
            pulse.setByY(1.2)
            pulse.setCycleCount(2)
            pulse.setAutoReverse(true)

            graphRoot.getChildren().add(score)
            def currentBird = bird // I originally used just "bird" in the closure, but see: http://blog.freeside.co/2013/03/29/groovy-gotcha-for-loops-and-closure-scope/
            bird.scoreProperty().addListener({ obs, old, cur ->
                score.setText(currentBird.getName() + ": " + cur)
                pulse.jumpTo(Duration.ZERO) // This seems to keep scale in a good state when multiple animations are triggered and overlap
                pulse.play()
            } as ChangeListener)
        }

        // Controller
//        new BirdAnimController(birds.get(0))
//        BirdHoverRuleController bController1 = new BirdHoverRuleController(birds.get(1), 250, 10, 4)
//        actorList.actors.add(bController1)
//        BirdHoverAndFlyController bController2 = new BirdHoverAndFlyController(birds.get(2), goalLevel, 5, 3, 1)
//        actorList.actors.add(bController2)

        // FrameStepper
        stepper = new FrameStepper(this).register()

        inputMap[[KeyEvent.KEY_PRESSED, KeyCode.E]] = {birds[0].handlePressLeft()}
        inputMap[[KeyEvent.KEY_RELEASED, KeyCode.E]] = {birds[0].handleReleaseLeft()}
        inputMap[[KeyEvent.KEY_PRESSED, KeyCode.R]] = {birds[0].handlePressRight()}
        inputMap[[KeyEvent.KEY_RELEASED, KeyCode.R]] = {birds[0].handleReleaseRight()}
        inputMap[[KeyEvent.KEY_PRESSED, KeyCode.T]] = {doBirdSound(birds[0]); birds[0].handlePressFlap()}
        inputMap[[KeyEvent.KEY_RELEASED, KeyCode.T]] = {birds[0].handleReleaseFlap()}

        def controller = ControllerEnvironment.getDefaultEnvironment().getControllers()[4]
        inputMap[[1.0 as Float, controller, Component.Identifier.Button._0]] = {doBirdSound(birds[1]); birds[1].handlePressFlap()}
        inputMap[[0.0 as Float, controller, Component.Identifier.Button._0]] = {birds[1].handleReleaseFlap()}
        inputMap[[Component.POV.LEFT as Float, controller, Component.Identifier.Axis.POV]] = {birds[1].handleDirectionLeft()}
        inputMap[[Component.POV.RIGHT as Float, controller, Component.Identifier.Axis.POV]] = {birds[1].handleDirectionRight()}
        inputMap[[Component.POV.CENTER as Float, controller, Component.Identifier.Axis.POV]] = {birds[1].handleDirectionRelease()}

        inputMap[[KeyEvent.KEY_PRESSED, KeyCode.SEMICOLON]] = {birds[3].handlePressLeft()}
        inputMap[[KeyEvent.KEY_RELEASED, KeyCode.SEMICOLON]] = {birds[3].handleReleaseLeft()}
        inputMap[[KeyEvent.KEY_PRESSED, KeyCode.QUOTE]] = {birds[3].handlePressRight()}
        inputMap[[KeyEvent.KEY_RELEASED, KeyCode.QUOTE]] = {birds[3].handleReleaseRight()}
        inputMap[[KeyEvent.KEY_PRESSED, KeyCode.ENTER]] = {doBirdSound(birds[3]); birds[3].handlePressFlap()}
        inputMap[[KeyEvent.KEY_RELEASED, KeyCode.ENTER]] = {birds[3].handleReleaseFlap()}

        // Keyboard
        stage.getScene().setOnKeyPressed {e ->
            if (e.getCode().equals(KeyCode.ESCAPE)) {
                close()
            } else if (e.getCode().equals(KeyCode.DIGIT1)) {
                birds.each {bird ->
                        actorList.actors.add(SimpleExplosion.make(bird.getX(), bird.getY(), 100, alphaize(bird.getColor()), graphRoot))}
            } else {
                if ([KeyEvent.KEY_PRESSED, e.getCode()] in inputMap) {
                    inputMap[[KeyEvent.KEY_PRESSED, e.getCode()]].run()
                }
            }
        }

        stage.getScene().setOnKeyReleased {e ->
            if ([KeyEvent.KEY_RELEASED, e.getCode()] in inputMap) {
                inputMap[[KeyEvent.KEY_RELEASED, e.getCode()]].run()
            }
        }

        // JInput JOYSTICK
        Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers()
        for (int i = 0; i < controllers.length; ++i) {
            Controller c = controllers[i]
            System.out.printf("Controller #%d %d %s - %s\n", i, c.getPortNumber(), c.getType(), c.getName())
            if (joyController == null && (c.getType() == Controller.Type.GAMEPAD || c.getType() == Controller.Type.STICK)) {
                joyController = c
                System.out.printf("Found valid joystick. Using controller #%d %s - %s\n", i, joyController.getType().toString(), joyController.getName())
            }
        }

        stage.setFullScreen(false)
    }

    void doBirdSound(Bird bird) {
        double bal = calcBalance(bird.getX())
        rndChoice(flapClips, rnd).play(1.0, bal, 1, 0.0, 1)
        actorList.actors.add(new LifetimeRect(bird.getColor(), 4, bird.getX(), bird.getY(), 4, graphRoot))
    }

    /**  Given an x value, return a value that can be used for audio balance (-1->1) */
    private double calcBalance(double x) {
        final double edgeOffset = 200
        double norm = Flx.normalize(x, edges.getMinX()+edgeOffset, edges.getMaxX()-edgeOffset)
        norm = Flx.clamp(norm, 0.0, 1.0)
        double balance = (norm * 2.0) - 1.0
        return balance
    }

    @Override
    void step(double delta) {
        checkNotNull(actorList)
        birds.each {bird ->
            checkNotNull(bird)
            bird.step(delta)
        }
        actorList.step(delta)
        if (stepper.getIntervalFrameCount() >= 200) {
            System.out.println(stepper.getFpsSummary())
        }
        birds.each {bird ->
            if (!edges.contains(bird.getBounds())) {
                bird.handleEdges(edges)
            }
        }

        for (int a = 0; a < birds.size(); ++a) {
            for (int b = a + 1; b < 4; ++b) {
                Bird bird1 = birds.get(a)
                Bounds bounds1 = bird1.getBounds()
                Bird bird2 = birds.get(b)
                Bounds bounds2 = bird2.getBounds()
                if (bounds1.intersects(bounds2)) {
                    println "got collision between $bird1 ${bird1.getName()} $bird2 ${bird2.getName()}"
                    laserClip.play(0.3, calcBalance(bird1.getX()), 1, 0.0, 1)
                    Vector2D intersectPt = boundsMid(bounds1, bounds2)
                    actorList.actors.add(SimpleExplosion.make(intersectPt, 50, alphaize(Color.WHITE, 0.5), graphRoot))
                    if (bounds1.getMinY() < bounds2.getMinY()) {
                        System.out.println("higher joust by " + bird1 + " " + bird1.getName())
                        bird1.changeScore(1)
                        bird2.doDie()
                    } else if (bounds2.getMinY() < bounds1.getMinY()) {
                        System.out.println("higher joust by " + bird2 + " " + bird2.getName())
                        bird2.changeScore(1)
                        bird1.doDie()
                    }
                }
            }
        }

        // JInput JOYSTICK
        joyStep()
    }

    // JInput JOYSTICK
    private void joyStep() {
        Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers()
        for (Controller cont : controllers) {
            if (cont.getType() != Controller.Type.GAMEPAD) {
                continue
            }
            cont.poll()
            EventQueue queue = cont.getEventQueue()
            Event event = new Event()
            while (queue.getNextEvent(event)) {
                if ([event.getValue(), cont, event.getComponent().getIdentifier()] in inputMap) {
                    inputMap[[event.getValue(), cont, event.getComponent().getIdentifier()]].run()
                }
            }
        }
    }
}
