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
import javafx.scene.shape.Rectangle
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
    private final int GOAL_LEVEL = 500
    private final int INITIAL_SCORE = 0
    private final int SCOREBOARD_FONT_SIZE = 24
    private final int SCOREBOARD_MAX_PLAYERS = 11
    private int scoreboardLabelX = 20
    private Map inputMap = [:]
    private List playerList = [
            ["Red", Color.RED],
            ["Blue", Color.BLUE],
            ["Yellow", Color.YELLOW],
            ["Green", Color.GREEN],
            ["Purple", Color.PURPLE],
            ["Pink", Color.PINK],
            ["Orange", Color.ORANGE],
            ["White", Color.WHITE],
            ["Brown", Color.BROWN],
            ["Gray", Color.DARKGRAY],
    ]

    private Map keyboardInputMaps = [
            (KeyCode.A): [
                [KeyEvent.KEY_PRESSED, KeyCode.DIGIT1, {bird -> bird.handlePressLeft()}],
                [KeyEvent.KEY_RELEASED, KeyCode.DIGIT1, {bird -> bird.handleReleaseLeft()}],
                [KeyEvent.KEY_PRESSED, KeyCode.Q, {bird -> bird.handlePressRight()}],
                [KeyEvent.KEY_RELEASED, KeyCode.Q, {bird -> bird.handleReleaseRight()}],
                [KeyEvent.KEY_PRESSED, KeyCode.A, {bird -> doBirdSound(bird); bird.handlePressFlap()}],
                [KeyEvent.KEY_RELEASED, KeyCode.A, {bird -> bird.handleReleaseFlap()}]
            ],
            (KeyCode.C): [
                [KeyEvent.KEY_PRESSED, KeyCode.Z, {bird -> bird.handlePressLeft()}],
                [KeyEvent.KEY_RELEASED, KeyCode.Z, {bird -> bird.handleReleaseLeft()}],
                [KeyEvent.KEY_PRESSED, KeyCode.X, {bird -> bird.handlePressRight()}],
                [KeyEvent.KEY_RELEASED, KeyCode.X, {bird -> bird.handleReleaseRight()}],
                [KeyEvent.KEY_PRESSED, KeyCode.C, {bird -> doBirdSound(bird); bird.handlePressFlap()}],
                [KeyEvent.KEY_RELEASED, KeyCode.C, {bird -> bird.handleReleaseFlap()}]
            ],
            (KeyCode.COMMA): [
                [KeyEvent.KEY_PRESSED, KeyCode.N, {bird -> bird.handlePressLeft()}],
                [KeyEvent.KEY_RELEASED, KeyCode.N, {bird -> bird.handleReleaseLeft()}],
                [KeyEvent.KEY_PRESSED, KeyCode.M, {bird -> bird.handlePressRight()}],
                [KeyEvent.KEY_RELEASED, KeyCode.M, {bird -> bird.handleReleaseRight()}],
                [KeyEvent.KEY_PRESSED, KeyCode.COMMA, {bird -> doBirdSound(bird); bird.handlePressFlap()}],
                [KeyEvent.KEY_RELEASED, KeyCode.COMMA, {bird -> bird.handleReleaseFlap()}]
            ],
            (KeyCode.RIGHT): [
                [KeyEvent.KEY_PRESSED, KeyCode.LEFT, {bird -> bird.handlePressLeft()}],
                [KeyEvent.KEY_RELEASED, KeyCode.LEFT, {bird -> bird.handleReleaseLeft()}],
                [KeyEvent.KEY_PRESSED, KeyCode.DOWN, {bird -> bird.handlePressRight()}],
                [KeyEvent.KEY_RELEASED, KeyCode.DOWN, {bird -> bird.handleReleaseRight()}],
                [KeyEvent.KEY_PRESSED, KeyCode.RIGHT, {bird -> doBirdSound(bird); bird.handlePressFlap()}],
                [KeyEvent.KEY_RELEASED, KeyCode.RIGHT, {bird -> bird.handleReleaseFlap()}]
            ],
            (KeyCode.BACK_SPACE): [
                [KeyEvent.KEY_PRESSED, KeyCode.ENTER, {bird -> bird.handlePressLeft()}],
                [KeyEvent.KEY_RELEASED, KeyCode.ENTER, {bird -> bird.handleReleaseLeft()}],
                [KeyEvent.KEY_PRESSED, KeyCode.BACK_SLASH, {bird -> bird.handlePressRight()}],
                [KeyEvent.KEY_RELEASED, KeyCode.BACK_SLASH, {bird -> bird.handleReleaseRight()}],
                [KeyEvent.KEY_PRESSED, KeyCode.BACK_SPACE, {bird -> doBirdSound(bird); bird.handlePressFlap()}],
                [KeyEvent.KEY_RELEASED, KeyCode.BACK_SPACE, {bird -> bird.handleReleaseFlap()}]
            ],
    ]

    private Map gamepadInputMaps = [
            ([Controller.Type.GAMEPAD, 'Controller (ZD Game For Windows)', Component.Identifier.Button._0]): [
                [Component.Identifier.Button._0, 1.0 as Float, {bird -> doBirdSound(bird); bird.handlePressFlap()}],
                [Component.Identifier.Button._0, 0.0 as Float, {bird -> bird.handleReleaseFlap()}],
                [Component.Identifier.Axis.POV, Component.POV.LEFT as Float, {bird -> bird.handleDirectionLeft()}],
                [Component.Identifier.Axis.POV, Component.POV.RIGHT as Float, {bird -> bird.handleDirectionRight()}],
                [Component.Identifier.Axis.POV, Component.POV.CENTER as Float, {bird -> bird.handleDirectionRelease()}]
            ],
            ([Controller.Type.STICK, 'TigerGame PS/PS2 Game Controller Adapter', Component.Identifier.Button._2]): [
                [Component.Identifier.Button._2, 1.0 as Float, {bird -> doBirdSound(bird); bird.handlePressFlap()}],
                [Component.Identifier.Button._2, 0.0 as Float, {bird -> bird.handleReleaseFlap()}],
                [Component.Identifier.Button._15, 1.0 as Float, {bird -> bird.handleDirectionLeft()}],
                [Component.Identifier.Button._15, 0.0 as Float, {bird -> bird.handleDirectionRelease()}],
                [Component.Identifier.Button._13, 1.0 as Float, {bird -> bird.handleDirectionRight()}],
                [Component.Identifier.Button._13, 0.0 as Float, {bird -> bird.handleDirectionRelease()}],
            ]
    ]

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

        // playfield border
        def border = new Rectangle(0, height-edges.getHeight(), width, edges.getHeight())
        border.setStroke(Color.WHITE)
        border.setStrokeWidth(3)
        graphRoot.getChildren().add(border)

        // set up sound
        laserClip = loadAudio("/resources/audio/laser.wav")
        flapClips.add(loadAudio("/resources/audio/flap1.wav"))
        flapClips.add(loadAudio("/resources/audio/flap2.wav"))
        flapClips.add(loadAudio("/resources/audio/flap3.wav"))

        // Auto-Controllers
//        new BirdAnimController(birds.get(0))
//        BirdHoverRuleController bController1 = new BirdHoverRuleController(birds.get(1), 250, 10, 4)
//        actorList.actors.add(bController1)
        def robotBird = new Bird(500, GOAL_LEVEL, "Robot", alphaize(Color.DARKGRAY.darker().darker()), graphRoot, 0)
        createScoreboard(robotBird)
        birds.add(robotBird)
        BirdHoverAndFlyController bController2 = new BirdHoverAndFlyController(robotBird, GOAL_LEVEL, 5, 3, 1)
        actorList.actors.add(bController2)

        // FrameStepper
        stepper = new FrameStepper(this).register()

        // Keyboard
        stage.getScene().setOnKeyPressed {e ->
            if (e.getCode().equals(KeyCode.ESCAPE)) {
                close()
            } else if (e.getCode().equals(KeyCode.DIGIT5)) {
                birds.each { bird ->
                    actorList.actors.add(SimpleExplosion.make(bird.getX(), bird.getY(), 100, alphaize(bird.getColor()), graphRoot))
                }
            } else if (e.getCode() == KeyCode.DIGIT6) {
                println "actorList: $actorList.actors.size"
            } else if ([KeyEvent.KEY_PRESSED, e.getCode()] in inputMap) {
                inputMap[[KeyEvent.KEY_PRESSED, e.getCode()]].run()
            } else {
                println "handling unknown input $e"
                List keyboardMap = keyboardInputMaps[e.getCode()]
                if (keyboardMap != null) {
                    println "Adding keyboard map $keyboardMap"
                    addNextPlayerWithKeyboard(keyboardMap)
                } else {
                    println "No known kayboard map for ${e.getCode()}"
                }
            }
        }

        stage.getScene().setOnKeyReleased {e ->
            if ([KeyEvent.KEY_RELEASED, e.getCode()] in inputMap) {
                inputMap[[KeyEvent.KEY_RELEASED, e.getCode()]].run()
            }
        }

        stage.setFullScreen(false)
    }

    void addNextPlayerWithKeyboard(List keyboardMap) {
        def (name, color) = playerList.removeAt(0)
        def newBird = new Bird(500, GOAL_LEVEL, name, alphaize(color), graphRoot, 0)
        birds.add(newBird)
        assignKeyboardInput(newBird, keyboardMap)
        createScoreboard(newBird)
    }

    void addNextPlayerWithGamepad(Controller controller, List gamepadMap) {
        def (name, color) = playerList.removeAt(0)
        def newBird = new Bird(500, GOAL_LEVEL, name, alphaize(color), graphRoot, 0)
        birds.add(newBird)
        assignGamepadInput(newBird, controller, gamepadMap)
        createScoreboard(newBird)
    }

    /** Assign specific keyboardMap to player */
    void assignKeyboardInput(Bird bird, List keyboardMap) {
        for (row in keyboardMap) {
            println "Adding keyboard input $row"
            def newRow = row.clone() // create clone to mutate
            def closure = newRow.pop()
            def wrappedClosure = { closure(bird) }
            inputMap[newRow] = wrappedClosure
        }
    }

    /** Assign specific gamepadMap to player */
    void assignGamepadInput(Bird bird, Controller controller, List gamepadMap) {
        for (List row in gamepadMap) {
            def newRow = row.clone() // create clone to mutate
            def closure = newRow.pop()
            def wrappedClosure = { closure(bird) }
            newRow.add(0, controller)
            println "Adding gamepad input $newRow"
            inputMap[newRow] = wrappedClosure
        }
    }

    void createScoreboard(Bird bird) {
        Font scoreFont = new Font(SCOREBOARD_FONT_SIZE)
        Label score = new Label(bird.getName() + ": " + INITIAL_SCORE)
        score.with {
            setTextFill(bird.getColor())
            setFont(scoreFont)
            setTranslateX(scoreboardLabelX)
            setTranslateY(5)
            setId("scoreboard-${bird.getName()}")
        }
        scoreboardLabelX += width / SCOREBOARD_MAX_PLAYERS

        ScaleTransition pulse = new ScaleTransition(Duration.seconds(0.2), score)
        pulse.with {
            setByX(0.3)
            setByY(0.3)
            setCycleCount(2)
            setAutoReverse(true)
        }

        graphRoot.getChildren().add(score)
        bird.scoreProperty().addListener({ obs, old, cur ->
            score.setText(bird.getName() + ": " + cur)
            pulse.jumpTo(Duration.ZERO) // This seems to keep scale in a good state when multiple animations are triggered and overlap
            pulse.play()
        } as ChangeListener)
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

        // collision detection
        for (int a = 0; a < birds.size(); ++a) {
            for (int b = a + 1; b < birds.size(); ++b) {
                Bird bird1 = birds.get(a)
                Bounds bounds1 = bird1.getBounds()
                Bird bird2 = birds.get(b)
                Bounds bounds2 = bird2.getBounds()
                if (bounds1.intersects(bounds2)) {
                    laserClip.play(0.3, calcBalance(bird1.getX()), 1, 0.0, 1)
                    Vector2D intersectPt = boundsMid(bounds1, bounds2)
                    actorList.actors.add(SimpleExplosion.make(intersectPt, 50, alphaize(Color.WHITE, 0.5), graphRoot))
                    if (bounds1.getMinY() < bounds2.getMinY()) {
                        System.out.println(bird1.getName() + " hit " + bird2.getName())
                        bird1.changeScore(1)
                        bird2.doDie()
                    } else if (bounds2.getMinY() < bounds1.getMinY()) {
                        System.out.println(bird2.getName() + " hit " + bird1.getName())
                        bird2.changeScore(1)
                        bird1.doDie()
                    }
                }
            }
        }

        // JInput JOYSTICK
        joyStep()
    }

    // JInput GAMEPAD
    private void joyStep() {
        Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers()
        for (Controller cont : controllers) {
            if (! (cont.getType() in [Controller.Type.GAMEPAD, Controller.Type.STICK])) {
                continue
            }
            cont.poll()
            EventQueue queue = cont.getEventQueue()
            Event event = new Event()
            while (queue.getNextEvent(event)) {
                if (event.getComponent().analog) { continue }
                if ([cont, event.getComponent().getIdentifier(), event.getValue()] in inputMap) {
                    inputMap[[cont, event.getComponent().getIdentifier(), event.getValue()]].run()
                } else {
                    def key = [cont.getType(), cont.getName(), event.getComponent().getIdentifier()]
                    if (key in gamepadInputMaps) {
                        println "adding new player from gamepad from event: $event"
                        addNextPlayerWithGamepad(cont, gamepadInputMaps[key])
                    }
                }
            }
        }
    }
}
