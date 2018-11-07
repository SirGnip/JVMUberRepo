package com.juxtaflux.app

import com.juxtaflux.experiments.ExampleBase
import com.juxtaflux.fluxlib.ActorList
import com.juxtaflux.fluxlib.Flx
import com.juxtaflux.fluxlib.FrameStepper
import com.juxtaflux.fluxlib.Stepable
import com.juxtaflux.gfluxlib.Rect2D
import com.juxtaflux.gfluxlib.Utl
import javafx.animation.FadeTransition
import javafx.animation.KeyFrame
import javafx.animation.ScaleTransition
import javafx.animation.Timeline
import javafx.beans.value.ChangeListener
import javafx.geometry.BoundingBox
import javafx.geometry.Bounds
import javafx.geometry.Insets
import javafx.geometry.Rectangle2D
import javafx.scene.Cursor
import javafx.scene.Parent
import javafx.scene.control.Label
import javafx.scene.effect.DisplacementMap
import javafx.scene.effect.FloatMap
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCombination
import javafx.scene.input.KeyEvent
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.Pane
import javafx.scene.media.AudioClip
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Polygon
import javafx.scene.shape.Rectangle
import javafx.scene.text.Font
import javafx.scene.transform.Scale
import javafx.scene.transform.Translate
import javafx.stage.Screen
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
    private BoundingBox edges = new BoundingBox(0, 50, width, height-50)
    private AudioClip deathAudio
    private List<AudioClip> flapClips = new ArrayList<>()
    private Pane graphRoot
    private Random rnd = new Random()
    private DisplacementMap fuzzMap1
    private DisplacementMap fuzzMap2
    private DisplacementMap fuzzMap3
    private int scoreboardLabelX = Cfg.Scoreboard.LABEL_X_START
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
            // "blue" light
            ([Controller.Type.GAMEPAD, 'Controller (ZD Game For Windows)', Component.Identifier.Button._0]): [
                [Component.Identifier.Button._0, 1.0 as Float, {bird -> doBirdSound(bird); bird.handlePressFlap()}],
                [Component.Identifier.Button._0, 0.0 as Float, {bird -> bird.handleReleaseFlap()}],
                [Component.Identifier.Axis.POV, Component.POV.LEFT as Float, {bird -> bird.handleDirectionLeft()}],
                [Component.Identifier.Axis.POV, Component.POV.RIGHT as Float, {bird -> bird.handleDirectionRight()}],
                [Component.Identifier.Axis.POV, Component.POV.CENTER as Float, {bird -> bird.handleDirectionRelease()}]
            ],
            // "red" light (name is same as "purple" so would need to go to component count to differentiate between them)
            ([Controller.Type.GAMEPAD, 'ZD-V', Component.Identifier.Button._2]): [
                    [Component.Identifier.Button._2, 1.0 as Float, {bird -> doBirdSound(bird); bird.handlePressFlap()}],
                    [Component.Identifier.Button._2, 0.0 as Float, {bird -> bird.handleReleaseFlap()}],
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

    private void enableFullscreen(Stage stage, Parent root) {
        stage.setFullScreen(true)
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH)
        root.setClip(new Rectangle(0, 0, width, height))
        def screenRect = Screen.getPrimary().getBounds()
        def (scaleRatio, letterboxOffset) = Rect2D.letterboxIn(new Rectangle2D(0, 0, width, height), screenRect)
        println "Fullscreen scaling of ${width}x${height} to $screenRect with ratio=$scaleRatio and offset=$letterboxOffset"
        root.getTransforms().add(new Translate(letterboxOffset.getX(), letterboxOffset.getY()))
        root.getTransforms().add(new Scale(scaleRatio, scaleRatio))
        stage.getScene().setCursor(Cursor.NONE)
    }

    private static DisplacementMap makeFuzzMap(rnd, Double offset, width, height) {
        Double half = offset/2
        FloatMap floatMap = new FloatMap()
        floatMap.setWidth(width)
        floatMap.setHeight(height)
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                floatMap.setSamples(i, j, rnd.nextDouble()*offset-half as Float, rnd.nextDouble()*offset-half as Float)
            }
        }
        DisplacementMap displacementMap = new DisplacementMap()
        displacementMap.setMapData(floatMap)
        return displacementMap
    }

    @Override
    protected void buildRoot(Stage stage, Pane pane) {
        Utl.metaprogrammingInit()

        def fuzzOffset = 0.4
        int fuzzWidth = 20
        int fuzzHeight = 20
        fuzzMap1 = makeFuzzMap(rnd, fuzzOffset, fuzzWidth, fuzzHeight)
        fuzzMap2 = makeFuzzMap(rnd, fuzzOffset, fuzzWidth, fuzzHeight)
        fuzzMap3 = makeFuzzMap(rnd, fuzzOffset, fuzzWidth, fuzzHeight)

        graphRoot = pane
        graphRoot.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)))

        // background
        def gridGray1 = Flx.makeGrid(0, 0, width, height, 14, Color.GRAY.darker().darker().darker().darker())
        def gridGray2 = Flx.makeGrid(0, 0, width, height, 15, Color.GRAY.darker().darker().darker())
        def gridBlue = Flx.makeGrid(0, 0, width, height, 16, Color.MIDNIGHTBLUE.darker().darker())
        graphRoot.getChildren().addAll(gridGray1, gridGray2, gridBlue)

        // Fullscreen
        if (Cfg.FULLSCREEN) {
            enableFullscreen(stage, pane)
        }

        // playfield border
        def border = new Rectangle(0, height-edges.getHeight(), width, edges.getHeight())
        border.setStroke(Color.WHITE)
        border.setFill(Color.TRANSPARENT)
        border.setStrokeWidth(3)
        graphRoot.getChildren().add(border)

        // set up sound
        deathAudio = loadAudio(Cfg.Audio.DEATH)
        flapClips.add(loadAudio(Cfg.Audio.FLAP1))
        flapClips.add(loadAudio(Cfg.Audio.FLAP2))
        flapClips.add(loadAudio(Cfg.Audio.FLAP3))

        // Auto-Controllers
//        new BirdAnimController(birds.get(0))
//        BirdHoverRuleController bController1 = new BirdHoverRuleController(birds.get(1), 250, 10, 4)
//        actorList.actors.add(bController1)
        def robotBird = new Bird(500, Cfg.GOAL_LEVEL, "Robot", Color.DARKGRAY.darker().darker(), graphRoot, 0)
        createScoreboard(robotBird)
        birds.add(robotBird)
//        BirdHoverAndFlyController bController2 = new BirdHoverAndFlyController(robotBird, Cfg.GOAL_LEVEL, 5, 3, 1)
        def bController2 = new BirdHoverAndFlyRandomlyController(robotBird, 100, 500, 2, 2, 3)
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
    }

    void addNextPlayerWithKeyboard(List keyboardMap) {
        def (name, color) = playerList.removeAt(0)
        def newBird = new Bird(500, Cfg.GOAL_LEVEL, name, color, graphRoot, 0)
        birds.add(newBird)
        assignKeyboardInput(newBird, keyboardMap)
        createScoreboard(newBird)
    }

    void addNextPlayerWithGamepad(Controller controller, List gamepadMap) {
        def (name, color) = playerList.removeAt(0)
        def newBird = new Bird(500, Cfg.GOAL_LEVEL, name, alphaize(color), graphRoot, 0)
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
        Font scoreFont = new Font(Cfg.Scoreboard.FONT_SIZE)
        Label score = new Label(bird.getName() + ": " + Cfg.Scoreboard.INITIAL_SCORE)
        score.with {
            setTextFill(bird.getColor())
            setFont(scoreFont)
            setTranslateX(scoreboardLabelX)
            setTranslateY(5)
            setId("scoreboard-${bird.getName()}")
        }
        scoreboardLabelX += width / Cfg.Scoreboard.MAX_PLAYERS

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
                    deathAudio.play(0.3, calcBalance(bird1.getX()), 1, 0.0, 1)
                    Vector2D intersectPt = boundsMid(bounds1, bounds2)
                    actorList.actors.add(SimpleExplosion.make(intersectPt, 50, alphaize(Color.WHITE, 0.5), graphRoot))
                    if (bounds1.getMinY() < bounds2.getMinY()) {
                        System.out.println(bird1.getName() + " hit " + bird2.getName())
                      handleBirdCollision(bird1, bird2, intersectPt)
                    } else if (bounds2.getMinY() < bounds1.getMinY()) {
                        System.out.println(bird2.getName() + " hit " + bird1.getName())
                        handleBirdCollision(bird2, bird1, intersectPt)
                    }
                }
            }
        }

        // JInput JOYSTICK
        joyStep()
    }

    private void handleBirdCollision(Bird winnerBird, Bird loserBird, Vector2D intersectPt) {
        def deathFx = new GenericLifetimeFadeActor(0.3, graphRoot,
            {new Circle(intersectPt.getX(), intersectPt.getY(), loserBird.size/2).returnWith({setFill(Color.WHITE)})})
        actorList.actors.add(deathFx)
        winnerBird.changeScore(1)
        Polygon deathPoly = loserBird.doDie()

        FadeTransition fade = new FadeTransition(Duration.seconds(4), deathPoly)
        fade.setFromValue(1.0)
        fade.setToValue(0.0)
        fade.play()
        deathPoly.setEffect(fuzzMap1)
        def timeline = new Timeline()
        timeline.setOnFinished {
            deathPoly.setEffect(null)
        }
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.seconds(0.04), {e -> deathPoly.setEffect(fuzzMap2)}),
                new KeyFrame(Duration.seconds(0.08), {e -> deathPoly.setEffect(fuzzMap3)}),
                new KeyFrame(Duration.seconds(0.12), {e -> deathPoly.setEffect(fuzzMap1)}),
        )
        timeline.setCycleCount(30)
        timeline.play()
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

    @Override
    <T extends javafx.event.Event> void onExit(T event) {
        stepper.deregister()
        super.onExit(event)
    }
}
