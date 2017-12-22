package com.juxtaflux.experiments;

import com.juxtaflux.app.*;
import com.juxtaflux.fluxlib.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
// JInput JOYSTICK
//import net.java.games.input.Controller;
//import net.java.games.input.ControllerEnvironment;
//import net.java.games.input.Event;
//import net.java.games.input.EventQueue;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.juxtaflux.fluxlib.Flx.*;
import static java.lang.Thread.sleep;

public class KitchenSinkExample extends ExampleBase implements Stepable {
    private List<Bird> birds = new ArrayList<>();
    private ActorList actorList = new ActorList();
    private FrameStepper stepper;
    private BoundingBox edges = new BoundingBox(50, 50, width-50, height-50);
    private Range widthRange = new Range(0, width);
    private Range heightRange = new Range(0, height);
    private AudioClip laserClip;
    private List<AudioClip> flapClips = new ArrayList<>();
    private EventHandler<ActionEvent> joyHandler;
    private Pane graphRoot;
    private Random rnd = new Random();

    private AudioClip loadAudio(String resourceName) {
        System.out.println("Loading audio from resource: " + resourceName);
        URL url = getClass().getResource(resourceName);
        checkNotNull(url);
        System.out.println("Loading audio from URL: " + url.toString());
        return new AudioClip(url.toString());
    }

    public List<Bird> getBirds() { return birds; }

    @Override
    protected void buildRoot(Stage stage, Pane pane) {
        graphRoot = pane;
        graphRoot.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
//        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
//        String appConfigPath = rootPath + "/resources/flux.properties";
//        System.out.println("prpoerties path: " + appConfigPath);

//        URL propUrl = getClass().getResource("/resources/flux.properties");

        // read from .properties file
        InputStream in = getClass().getResourceAsStream("/resources/flux.properties");
        Properties props = new Properties();
        try {
            props.load(in);
            in.close();
            System.out.println(props);
            String stuff = props.getProperty("stuff");
            System.out.println("STUFF read from flie: " + stuff);
        } catch (Exception ex) {
            System.out.println("ERROR " + ex);
        }

        // read text file from .jar
        InputStream in2 = getClass().getResourceAsStream("/resources/blah.txt");
        System.out.println("input stream: " + in2);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in2));
        System.out.println("reader: " + reader);
//        String txt = reader.lines().collect(Collectors.joining("\n"));
//        System.out.println(txt);
        List<String> txt = reader.lines().collect(Collectors.toList());
        txt.forEach(s -> System.out.println(s));

        // set up image
        URL imgUrl = getClass().getResource("/resources/img/cloud.png");
        System.out.println("Loading image from URL: " + imgUrl);
        checkNotNull(imgUrl);
        ImageView img = new ImageView(imgUrl.toString());
        img.setPreserveRatio(true);
        img.setTranslateX(300);
        img.setTranslateY(250);
        img.setOpacity(0.15);
        graphRoot.getChildren().add(img);

        // set up sound
        laserClip = loadAudio("/resources/audio/laser.wav");
        flapClips.add(loadAudio("/resources/audio/flap1.wav"));
        flapClips.add(loadAudio("/resources/audio/flap2.wav"));
        flapClips.add(loadAudio("/resources/audio/flap3.wav"));

        // button
        Button btn = new Button("Click ME");
        btn.setTranslateX(100);
        btn.setTranslateY(100);

        Supplier<Node> s1 = () -> {
            Vector2D pt = Flx.rnd(rnd, edges);
            Rectangle r = new Rectangle(pt.getX(), pt.getY(), 30, 30);
            r.setFill(Color.GREEN);
            return r;
        };
        Supplier<Node> s2 = () -> Flx.makeCross((int)widthRange.rand(rnd), (int)heightRange.rand(rnd), 40, Color.GREENYELLOW);
        Supplier<Node> s3 = () -> Flx.makeGrid((int)widthRange.rand(rnd), (int)heightRange.rand(rnd), 50, 50, 20, Color.LIGHTBLUE);
        List<Supplier<Node>> suppliers = Arrays.asList(s1, s2, s3);

        btn.setOnAction(e -> {
            System.out.println("click");
            laserClip.play();
            actorList.actors.add(new GenericLifetimeActor(10, graphRoot, Flx.rndChoice(suppliers, rnd)));
        });
        graphRoot.getChildren().add(btn);

        int goalLevel = 500;
        graphRoot.getChildren().add(Flx.makeLine(0, goalLevel, 800, goalLevel, Color.GRAY));

        // birds
        double startX = 100;
        birds.add(new Bird(startX, goalLevel,"Blue", alphaize(Color.BLUE), graphRoot));
        startX += 100;
        birds.add(new Bird(startX, goalLevel,"Red", alphaize(Color.RED), graphRoot));
        startX += 100;
        birds.add(new Bird(startX, goalLevel,"Green", alphaize(Color.GREEN), graphRoot));
        startX += 100;
        birds.add(new Bird(startX, goalLevel,"Yellow", alphaize(Color.YELLOW), graphRoot));

        // Controller
        new BirdAnimController(birds.get(0));
        BirdHoverRuleController bController1 = new BirdHoverRuleController(birds.get(1), 250, 10, 4);
        actorList.actors.add(bController1);
        BirdHoverAndFlyController bController2 = new BirdHoverAndFlyController(birds.get(2), goalLevel, 5, 3, 1);
        actorList.actors.add(bController2);

        // FrameStepper
        stepper = new FrameStepper(this).register();

//        InputMapper inputMapper = new NaiveInputMapper(this);
        InputMapper inputMapper = new TraditionalInputMapper(this);

        // Keyboard
        stage.getScene().setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ESCAPE)) {
                close();
            } else if (e.getCode().equals(KeyCode.DIGIT1)) {
                birds.forEach(bird ->
                        actorList.actors.add(SimpleExplosion.make(bird.getX(), bird.getY(), 100, alphaize(bird.getColor()), graphRoot)));
            } else {
                inputMapper.handleKeyInput(e);
            }
        });
        stage.getScene().setOnKeyReleased(e -> {
            inputMapper.handleKeyInput(e);
        });

        // Mouse
        stage.getScene().setOnMousePressed(e -> {
//            System.out.println(e.isPrimaryButtonDown() + "/" + e.isSecondaryButtonDown() + " " + e);
            inputMapper.handleMouseInput(e);
        });
        stage.getScene().setOnMouseReleased(e -> {
            inputMapper.handleMouseInput(e);
        });

//        // JInput JOYSTICK
//        joyHandler = (e -> {
//            System.out.println("joy handler " + e.getEventType());
//            System.out.println(e);
//            bird1.addVel(new Vector2D(0, -vertImpulse));
//        });

        stage.setFullScreen(false);
    }

    void respondToInput(Bird bird) {
        double bal = calcBalance(bird.getX());
        rndChoice(flapClips, rnd).play(1.0, bal, 1, 0.0, 1);
        actorList.actors.add(new LifetimeRect(Color.gray(0.2), 3, bird.getX(), bird.getY(), 3, graphRoot));
    }

    void doBirdSound(Bird bird) {
        double bal = calcBalance(bird.getX());
        rndChoice(flapClips, rnd).play(1.0, bal, 1, 0.0, 1);
        actorList.actors.add(new LifetimeRect(Color.gray(0.2), 3, bird.getX(), bird.getY(), 3, graphRoot));
    }

    /**  Given an x value, return a value that can be used for audio balance (-1->1) */
    private double calcBalance(double x) {
        double norm = Flx.normalize(x, edges.getMinX(), edges.getMaxX());
        double balance = (norm * 2.0) - 1.0;
        return balance;
    }

    @Override
    public void step(double delta) {
        checkNotNull(actorList);
        birds.forEach(bird -> {
            checkNotNull(bird);
            bird.step(delta);
        });
        actorList.step(delta);
        if (stepper.getIntervalFrameCount() >= 200) {
            System.out.println(stepper.getFpsSummary());
        }
        birds.forEach(bird -> {
            if (!edges.contains(bird.getBounds())) {
                bird.handleEdges(edges);
//                bird.wrapAtEdge(edges);
            }
        });

        for (int a = 0; a < birds.size(); ++a) {
            for (int b = a + 1; b < 4; ++b) {
                Bird bird1 = birds.get(a);
                Bounds bounds1 = bird1.getBounds();
                Bird bird2 = birds.get(b);
                Bounds bounds2 = bird2.getBounds();
                if (bounds1.intersects(bounds2)) {
                    laserClip.play(0.3, calcBalance(bird1.getX()), 1, 0.0, 1);
                    Vector2D intersectPt = boundsMid(bounds1, bounds2);
                    actorList.actors.add(SimpleExplosion.make(intersectPt, 50, alphaize(Color.WHITE, 0.5), graphRoot));
                    if (bounds1.getMinY() < bounds2.getMinY()) {
                        System.out.println("higher joust by " + bird1.getName());
                        bird2.doDie();
                    } else if (bounds2.getMinY() < bounds1.getMinY()) {
                        System.out.println("higher joust by " + bird2.getName());
                        bird1.doDie();
                    }
                }
            }
        }


//        // JInput JOYSTICK
//        joyStep();
    }

//    // JInput JOYSTICK
//    private void joyStep() {
//        Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
//        int controllerIdx = 0;
//        for (Controller cont : controllers) {
//            cont.poll();
//            EventQueue queue = cont.getEventQueue();
//            Event event = new Event();
//            while (queue.getNextEvent(event)) {
//                if (controllerIdx == 4 && event.getComponent().getIdentifier().getName().equals("3") && event.getValue() > 0.0) {
//                    System.out.println("firing joy handler");
//                    joyHandler.handle(new ActionEvent());
//                }
//            }
//            controllerIdx++;
//        }
//    }
}


