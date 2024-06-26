package com.pulsar.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.CpuSpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.pulsar.*;
import com.pulsar.api.*;
import com.pulsar.api.Component;
import com.pulsar.api.components.*;
import com.pulsar.api.graphics.Material;
import com.pulsar.api.graphics.TextureAsset;
import com.pulsar.api.math.MathUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import space.earlygrey.shapedrawer.ShapeDrawer;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.lang.reflect.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.pulsar.Statics.*;
import static org.lwjgl.util.nfd.NativeFileDialog.*;

public class Utils {

    public static TextureRegion getPixel() {
        Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.drawPixel(0, 0);
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return new TextureRegion(texture, 0, 0, 1, 1);
    }

    static Array<FileHandle> getFiles(FileHandle folder) {
        Array<FileHandle> fs = new Array<>();
        for (FileHandle f : folder.list()) {
            if (f.isDirectory()) {
                Array<FileHandle> tfs = getFiles(f);
                fs.addAll(tfs);
            } else {
                fs.add(f);
            }
        }
        return fs;
    }

    static Process compileJavaFile(String filePath, String dir, String dependencies) throws IOException {
        String[] command = {"javac", "-encoding", "utf8", "-cp", "\"" + dependencies + "\"", "-d", ".", filePath};

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(new File(dir));
        return pb.start();
    }

    static Process compileJavaFiles(String[] filePaths, String dir) throws IOException {
        String[] command = {"javac", "-encoding", "utf8"};
        command = ArrayUtils.addAll(command, filePaths);
        ProcessBuilder pb = new ProcessBuilder(command);

        pb.directory(new File(dir));
        return pb.start();
    }

    static Process classToJar(String filePath, String dest, String dir) throws IOException {
        String[] command = {"jar", "cmvf", "META-INF/MANIFEST.MF", dest, filePath};
        ProcessBuilder pb = new ProcessBuilder(command);
        ;
        pb.directory(new File(dir));
        return pb.start();
    }

    public static void error(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private static String os = System.getProperty("os.name").toLowerCase();
    private static String pathSeparator = os.contains("win") ? ";" : ":";

    public static void compile() {
        currentProject.components.clear();
        try {
            FileHandle assetsDir = Statics.currentProject.path.child("Assets");
            FileHandle tempDir = Statics.currentProject.path.child("Temp");
            FileHandle apiDir = Statics.currentProject.path.child("Api");

            clearFolder(tempDir);

            FileHandle pulsarApi = tempDir.child("com").child("pulsar").child("api");

            if (!pulsarApi.child("components").exists())
                pulsarApi.child("components").mkdirs();
            if (!pulsarApi.child("math").exists())
                pulsarApi.child("math").mkdirs();
            if (!pulsarApi.child("graphics").exists())
                pulsarApi.child("graphics").mkdirs();
            if (!pulsarApi.child("audio").exists())
                pulsarApi.child("audio").mkdirs();
            if (!pulsarApi.child("annotations").exists())
                pulsarApi.child("annotations").mkdirs();
            if (!pulsarApi.child("physics").exists())
                pulsarApi.child("physics").mkdirs();

            if (!tempDir.child("components").exists())
                tempDir.child("components").mkdirs();
            if (!tempDir.child("math").exists())
                tempDir.child("math").mkdirs();
            if (!tempDir.child("graphics").exists())
                tempDir.child("graphics").mkdirs();
            if (!tempDir.child("audio").exists())
                tempDir.child("audio").mkdirs();
            if (!tempDir.child("physics").exists())
                tempDir.child("physics").mkdirs();
            if (!tempDir.child("annotations").exists())
                tempDir.child("annotations").mkdirs();

            String[] compileApiOrder = new String[]{"Engine.java", "Debug.java", "FileHandle.java", "Renderer.java", "Input.java", "ShaderLanguage.java", "GameObject.java", "Time.java", "Component.java", "GameObjectCondition.java", "AudioManager.java", "Label.java", "Curve.java", "Button.java", "ColorRange.java", "Graphics.java", "CameraHolder.java", "Scene.java"};
            String[] foldersToCopy = new String[]{"components", "math", "graphics", "audio", "physics", "annotations"};

            for (String s : compileApiOrder) {
                FileHandle a = apiDir.child(s);
                a.copyTo(tempDir);
            }

            for (String s : foldersToCopy) {
                FileHandle a = apiDir.child(s);

                FileUtils.copyDirectory(a.file(), tempDir.child(s).file());
            }

            for (int i = 0; i < compileApiOrder.length; i++) {
                compileApiOrder[i] = tempDir.path() + "\\" + compileApiOrder[i];
            }

            for (String s : foldersToCopy) {
                FileHandle a = apiDir.child(s);
                Array<FileHandle> fs = getFiles(a);
                for (int i = 0; i < fs.size; i++) {
                    FileHandle b = fs.get(i);
                    if (b.extension().equals("java"))
                        compileApiOrder = ArrayUtils.add(compileApiOrder, tempDir.child(a.name()).child(b.name()).path().replace('/', '\\'));
                }
            }

            Process p0 = compileJavaFiles(compileApiOrder, tempDir.path());

            while (p0.waitFor() != 0) {
                if (p0.getErrorStream().available() > 0) {
                    throw new RuntimeException("Compilation Error: \n" + new String(p0.getErrorStream().readAllBytes()));
                }
                if (p0.getInputStream().available() > 0) {
                    throw new RuntimeException("Compilation Error: \n" + new String(p0.getInputStream().readAllBytes()));
                }
                ;
            }

            for (String s : compileApiOrder) {
                FileHandle a = apiDir.child(s);
                if (a.parent().name().equals("Temp")) {
                    tempDir.child(a.nameWithoutExtension() + ".class").moveTo(tempDir.child("com").child("pulsar").child("api"));
                } else {
                    tempDir.child(a.parent().name()).child(a.nameWithoutExtension() + ".class").moveTo(tempDir.child("com").child("pulsar").child("api").child(a.parent().name()));
                }

            }

            Array<FileHandle> assets = getFiles(assetsDir);

            boolean exit = false;

            javaComponentLoader = new JavaComponentLoader();

            FileHandle dependenciesDir = apiDir.child("Dependencies");

            StringBuilder dependencies = new StringBuilder();
            for (String dependency : currentProject.dependencies) {
                String[] parts = dependency.split(":");
                String artifactId = parts[1];
                String version = parts[2];
                dependencies.append(artifactId).append("-").append(version).append(".jar").append(" ");
            }
            for (String dependency : currentProject.desktopDependencies) { // Engine only
                String[] parts = dependency.split(":");
                String artifactId = parts[1];
                String version = parts[2];
                dependencies.append(artifactId).append("-").append(version).append(".jar").append(" ");
            }

            for (FileHandle dependency : dependenciesDir.list()) {
                dependency.copyTo(tempDir);
            }

            tempDir.child("META-INF").mkdirs();
            tempDir.child("META-INF").child("MANIFEST.MF").writeString("Manifest-Version: 1.0\nClass-Path: " + dependencies, false);

            for (FileHandle a : assets) {

                if (a.extension().equalsIgnoreCase("java")) {
                    if (!a.readString().isEmpty()) {

                        FileHandle dir = tempDir.child(getPackage(a).replaceAll("\\.", "/"));
                        dir.mkdirs();

                        a.copyTo(tempDir);

                        StringBuilder dependenciesList = new StringBuilder();
                        for (int i = 0; i < currentProject.dependencies.size(); i++) {
                            String[] parts = currentProject.dependencies.get(i).split(":");
                            String artifactId = parts[1];
                            String version = parts[2];
                            dependenciesList.append(artifactId).append("-").append(version).append(".jar");
                            if (i != currentProject.dependencies.size() - 1) dependenciesList.append(pathSeparator);
                        }
                        for (int i = 0; i < currentProject.desktopDependencies.size(); i++) { // Engine only
                            String[] parts = currentProject.desktopDependencies.get(i).split(":");
                            String artifactId = parts[1];
                            String version = parts[2];
                            dependenciesList.append(artifactId).append("-").append(version).append(".jar");
                            if (i != currentProject.desktopDependencies.size() - 1) dependenciesList.append(pathSeparator);
                        }
                        Process p1 = compileJavaFile(tempDir.child(a.name()).path(), tempDir.path(), dependenciesList.toString());

                        while (p1.waitFor() != 0) {
                            if (p1.getErrorStream().available() > 0) {
                                //System.out.println(new String(p1.getErrorStream().readAllBytes()));
                                Debug.error(a.name() + " : " + new String(p1.getErrorStream().readAllBytes()));
                                exit = true;
                                break;
                            }
                        }
                        if (exit) break;

                        String fp1 = "." + dir.child(a.nameWithoutExtension() + ".class").path().replaceAll(tempDir.path(), "");
                        Process p2 = classToJar(fp1, dir.child(a.nameWithoutExtension() + ".jar").path(), tempDir.path());

                        while (p2.waitFor() != 0) {
                            if (p2.getErrorStream().available() > 0) {
                                Debug.error(a.name() + " : " + new String(p2.getErrorStream().readAllBytes()));
                                exit = true;
                                break;
                            }
                        }
                        if (exit) break;
                    }

                }
            }

            Array<FileHandle> tempFiles = getFiles(tempDir);

            if (!exit) {
                for (FileHandle t : tempFiles) {
                    if (t.extension().equals("java") || t.extension().equals("class")) {
                        if (!(t.name().equals("Component.class") || t.name().equals("Engine.class") || t.name().equals("Entity.class") || t.name().equals("Debug.class"))) {
                            t.delete();
                        }
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    static void clearFolder(FileHandle folder) {
        for (FileHandle f : folder.list()) {
            if (f.isDirectory())
                f.deleteDirectory();
            else
                f.delete();
        }
    }

    public static String pickFolder() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            PointerBuffer buf = stack.callocPointer(1);
            int result = NFD_PickFolder((ByteBuffer) null, buf);
            if (result == NFD_OKAY) {
                return MemoryUtil.memUTF8(buf.get(0));
            } else if (result == NFD_ERROR) {
                return null;
            }
        }
        return null;
    }

    @Deprecated
    public static void setGameObjectCounter() {

    }

    private static void addComponents(FileHandle parent) {
        FileHandle[] assets = parent.list();
        for (FileHandle file : assets) {
            if (file.isDirectory()) {
                addComponents(file);
            } else if (file.extension().equalsIgnoreCase("java")) {
                try (InputStream stream = file.read()) {
                    if (stream.available() > 0) {
                        FileHandle dir = currentProject.path.child("Temp").child(getPackage(file).replaceAll("\\.", "/"));
                        if (!dir.child(file.nameWithoutExtension() + ".jar").exists())
                            continue;
                        String source = file.readString();
                        String packageName = "";
                        String[] words = source.split("\\s");
                        if (words[0].equals("package"))
                            packageName = words[1].substring(0, words[1].length() - 1);
                        String name = packageName + (packageName.isEmpty() ? "" : ".") + file.nameWithoutExtension();
                        Class<?> c = Utils.getClass(name);
                        if (Component.class.isAssignableFrom(c)) {
                            Class<? extends Component> nc = (Class<? extends Component>) c;
                            currentProject.components.add(nc);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void addComponents() {
        currentProject.components.clear();
        addComponents(currentProject.path.child("Assets"));

        currentProject.components.add(Transform.class);
        currentProject.components.add(SpriteRenderer.class);
        currentProject.components.add(Camera.class);
        currentProject.components.add(AudioListener.class);
        currentProject.components.add(AudioSource.class);
        currentProject.components.add(StreamedAudioSource.class);
        currentProject.components.add(RigidBody.class);
        currentProject.components.add(CircleCollider.class);
        currentProject.components.add(BoxCollider.class);
        currentProject.components.add(PolygonCollider.class);
        currentProject.components.add(DistanceJoint.class);
        currentProject.components.add(RevoluteJoint.class);
        currentProject.components.add(PrismaticJoint.class);
        currentProject.components.add(RopeJoint.class);
        currentProject.components.add(WheelJoint.class);
        currentProject.components.add(WeldJoint.class);
        currentProject.components.add(ParticleEmitter.class);

        currentProject.components.add(Text.class);
    }

    public static void loadProject(Project project) {

        engine = new Engine(new Sprite(), drawer, cameraHolder);

        currentProject = project;
        if (currentProject == null)
            Gdx.app.exit();

        main.setScreen(new Editor());

        Utils.compile();

        addComponents();

        setGameObjectCounter();

    }

    public static boolean loadProject(String path) {

        allGameObjects.clear();

        javaComponentLoader = new JavaComponentLoader();

        engine = new Engine(new Sprite(), drawer, cameraHolder);

        currentProject = null;

        currentProject = Project.loadProject(path);

        if (currentProject == null) {
            return false;
        }

        Statics.main.setScreen(new Editor());

        addComponents();

        setGameObjectCounter();

        Utils.fixGameObjects(currentProject.getCurrentScene().rootGameObject);

        Gdx.files.local("recent_projects").writeString(path + "\n", true);

        recentProjects.add(path);

        return true;

    }

    private static void fixTextureAssets(Object object) {
        // TODO Replace recursion with a while loop
        // TODO Doesn't work with ArrayLists
        if (object == null) return;
        for (Field field : object.getClass().getFields()) {
            if (
                (!field.getType().isPrimitive() || field.getType().getClassLoader() != "".getClass().getClassLoader()) &&
                    !Modifier.isStatic(field.getModifiers()) && field.getType() != object.getClass() &&
                    !field.getType().getPackageName().startsWith("com.badlogic.gdx") && field.getType() != GameObject.class
            ) {
                if (field.getType() == TextureAsset.class) {
                    try {
                        TextureAsset tex = (TextureAsset) field.get(object);
                        if (tex != null) {
                            FileHandle file = Gdx.files.absolute(tex.getPath());
                            TextureAsset newTex = engine.loadedTextures.get(file.nameWithoutExtension());
                            if (newTex == null) newTex = engine.loadTexture(file.nameWithoutExtension(), file);
                            field.set(object, newTex);
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    try {
                        fixTextureAssets(field.get(object));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            Class<?> typeC = field.getType();
            Type type = field.getGenericType();
            try {
                if (List.class.isAssignableFrom(typeC)) {
                    List<Object> list = (List<Object>) field.get(object);
                    ParameterizedType parameterizedType = (ParameterizedType) type;
                    Type componentType = parameterizedType.getActualTypeArguments()[0];
                    Class<?> componentTypeC = (Class<?>) componentType;
                    for (int j = 0; j < list.size(); j++) {
                        Object o = list.get(j);
                        if (componentTypeC == com.pulsar.api.graphics.Texture.class) {
                            com.pulsar.api.graphics.Texture tex = (com.pulsar.api.graphics.Texture) o;
                            if (tex != null) {
                                FileHandle file = Gdx.files.absolute(tex.textureAsset.getPath());
                                TextureAsset newTex = engine.loadedTextures.get(file.nameWithoutExtension());
                                if (newTex == null) newTex = engine.loadTexture(file.nameWithoutExtension(), file);
                                tex.textureAsset = newTex;
                                o = tex;
                            }
                        }
                        list.set(j, o);
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void fixGameObjects(GameObject parent) {
        for (GameObject gameObject : parent.children) {
            for (Component component : gameObject.components) {
                fixTextureAssets(component);
            }
            fixGameObjects(gameObject);
        }
    }

    private static final String imlFile = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<module type=\"JAVA_MODULE\" version=\"4\">\n" +
        "  <component name=\"NewModuleRootManager\" inherit-compiler-output=\"true\">\n" +
        "    <exclude-output />\n" +
        "    <content url=\"file://$MODULE_DIR$\">\n" +
        "      <sourceFolder url=\"file://$MODULE_DIR$\" />\n" +
        "      <sourceFolder url=\"file://$MODULE_DIR$/Api\" isTestSource=\"false\" packagePrefix=\"com.pulsar.api\" />\n" +
        "      <sourceFolder url=\"file://$MODULE_DIR$/Assets\" isTestSource=\"false\" />\n" +
        "      <excludeFolder url=\"file://$MODULE_DIR$/Temp\" />\n" +
        "    </content>\n" +
        "    <orderEntry type=\"inheritedJdk\" />\n" +
        "    <orderEntry type=\"sourceFolder\" forTests=\"false\" />\n" +
        "    <orderEntry type=\"library\" name=\"Dependencies\" level=\"project\" />\n" +
        "  </component>\n" +
        "</module>";

    private static final String ideaModulesFile = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<project version=\"4\">\n" +
        "  <component name=\"ProjectModuleManager\">\n" +
        "    <modules>\n" +
        "      <module fileurl=\"file://$PROJECT_DIR$/[REPLACE].iml\" filepath=\"$PROJECT_DIR$/REPLACE.iml\" />\n" +
        "    </modules>\n" +
        "  </component>\n" +
        "</project>";

    public static void createProject(String path, String name) {

        FileHandle folder = Gdx.files.absolute(path);

        folder.child("Temp").mkdirs();
        folder.child("Assets").mkdirs();
        folder.child("Api").mkdirs();

        FileHandle[] files = Gdx.files.local("api").list();

        for (FileHandle f : files) {
            f.copyTo(folder.child("Api"));
        }

        SerializableProject sp = new SerializableProject();
        sp.path = Gdx.files.absolute(path);
        sp.projectName = name;
        Scene scene = new Scene();
        scene.name = "Main";
        scene.rootGameObject = new GameObject(0);
        sp.scenes.put("Main.scene", new SerializableScene(scene));
        sp.mainScene = "Main.scene";

        folder.child(folder.nameWithoutExtension() + ".iml").writeString(imlFile, false);
        folder.child(".idea").mkdirs();
        folder.child(".idea").child("modules.xml").writeString(ideaModulesFile.replaceAll("\\[REPLACE]", folder.nameWithoutExtension()), false);

        folder.child("main.prj").writeString(sp.toJson(), false);

        loadProject(path);

        GameObject camera = new GameObject("Camera");

        Camera cameraCmp = new Camera();
        AudioListener listenerCmp = new AudioListener();

        camera.addComponent(cameraCmp);
        camera.addComponent(listenerCmp);

        scene.rootGameObject.addGameObject(camera);

        sp.scenes.put("Main.scene", new SerializableScene(scene));
        folder.child("main.prj").writeString(sp.toJson(), false);

        loadProject(path);

        save();

    }

    public static void initialize() {
        batch = new CpuSpriteBatch();

        Pixmap pixel = new Pixmap(1, 1, Format.RGBA8888);
        pixel.setColor(Color.WHITE);
        pixel.fill();

        drawer = new ShapeDrawer(batch, new TextureRegion(new Texture(pixel)));

        fbo = new FrameBuffer(Format.RGBA8888, 1080, 720, false);

        javaComponentLoader = new JavaComponentLoader();

        gui = new Gui();

        audio.init();

        json.setOutputType(JsonWriter.OutputType.json);

        preferences = Gdx.app.getPreferences("My Preferences");
        if (!preferences.contains("idePath")) preferences.putString("idePath", "");

        if (Gdx.files.local("recent_projects").exists()) {
            String recentProjectsFile = Gdx.files.local("recent_projects").readString();
            HashSet<String> recentProjectsSet = new HashSet<>();
            for (String line : recentProjectsFile.split("\n")) {
                if (Gdx.files.absolute(line).exists() && !recentProjectsSet.contains(line)) {
                    recentProjects.add(line);
                    recentProjectsSet.add(line);
                }
            }
            recentProjects.reverse();
            StringBuilder sb = new StringBuilder();
            for (String line : recentProjects)
                sb.append(line).append("\n");
            Gdx.files.local("recent_projects").writeString(sb.toString(), false);
        }

    }

    public static Component getComponent(String componentName) {
        try {
            FileHandle dir = currentProject.path.child("Temp");
            int lastIndexOfDot = Math.max(componentName.lastIndexOf('.') + 1, 0);
            if (lastIndexOfDot > 0)
                dir = dir.child(componentName.substring(0, lastIndexOfDot).replaceAll("\\.", "/"));
            File f = dir.child(componentName.substring(lastIndexOfDot) + ".jar").file();
            return javaComponentLoader.loadScript(f, componentName);
        } catch (FileNotFoundException | ClassNotFoundException | MalformedURLException e) {
            try {
                Thread.sleep(500);
                return getComponent(componentName);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static Class<?> getClass(String componentName) {
        Class<?> c = getComponent(componentName).getClass();
        return c;
    }

    public static void save() {

        for (Material material : engine.loadedMaterials.values()) {
            Gdx.files.absolute(material.materialFile).writeString(material.save(), false);
        }

        String j = new SerializableProject(Statics.currentProject).toJson();
        Gdx.files.absolute(Statics.currentProject.path.path() + "\\main.prj").writeString(j, false);

        projectChange = false;

    }

    public static void load() {
    }

    static boolean moveXLock = false;
    static boolean moveYLock = false;
    static boolean moveLock = false;
    static boolean scaleXLock = false;
    static boolean scaleYLock = false;
    static boolean rotateLock = false;

    static float mouseOffsetX = 0;
    static float mouseOffsetY = 0;

    static float initialScaleX;
    static float initialScaleY;

    static boolean change;

    com.pulsar.api.math.Vector2 tmp = new com.pulsar.api.math.Vector2();

    public static boolean tools() {
        boolean canMoveX = false;
        boolean canMoveY = false;
        boolean canMove = false;
        boolean canScaleX = false;
        boolean canScaleY = false;
        boolean canRotate = false;

        if (selectedGameObjects != null) {
            for (int i = 0; i < selectedGameObjects.length; i++) {
                GameObject gameObject = selectedGameObjects[i];

                com.pulsar.api.math.Vector2 position = gameObject.transform.position;
                float offsetX = position.x - selectedGameObject.transform.position.x;
                float offsetY = position.y - selectedGameObject.transform.position.y;
                position.set(selectedGameObject.transform.position);

                com.pulsar.api.math.Vector2 scale = gameObject.transform.scale;
                float scaleOffsetX = scale.x - selectedGameObject.transform.scale.x;
                float scaleOffsetY = scale.y - selectedGameObject.transform.scale.y;
                scale.set(selectedGameObject.transform.scale);
                float rotationOffset = gameObject.transform.rotation - selectedGameObject.transform.rotation;
                gameObject.transform.rotation = selectedGameObject.transform.rotation;

                if (editMode == 1) {
                    if (mouse.x > position.x + .2f && mouse.y > position.y - .1f && mouse.x < position.x + 1.5f && mouse.y < position.y + .1f)
                        canMoveX = true;
                    if (mouse.x > position.x - .1f && mouse.y > position.y + .2f && mouse.x < position.x + .1f && mouse.y < position.y + 1.5f)
                        canMoveY = true;
                    if (mouse.x > position.x && mouse.y > position.y && mouse.x < position.x + .2f && mouse.y < position.y + .2f)
                        canMove = true;
                }
                if (editMode == 2) {
                    if (mouse.x > position.x + .2f && mouse.y > position.y - .1f && mouse.x < position.x + 1.5f && mouse.y < position.y + .1f)
                        canScaleX = true;
                    if (mouse.x > position.x - .1f && mouse.y > position.y + .2f && mouse.x < position.x + .1f && mouse.y < position.y + 1.5f)
                        canScaleY = true;
                }
                if (editMode == 3) {
                    float dst = mouse.dst(position.x, position.y);
                    if (dst > 1.4f && dst < 1.6f)
                        canRotate = true;
                }
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    if (canMoveX)
                        moveXLock = true;
                    if (canMoveY)
                        moveYLock = true;
                    if (canMove)
                        moveLock = true;
                    if (canScaleX) scaleXLock = true;
                    if (canScaleY) scaleYLock = true;
                    if (canRotate) rotateLock = true;
                    mouseOffsetX = position.x - mouse.x;
                    mouseOffsetY = position.y - mouse.y;
                    initialScaleX = scale.x;
                    initialScaleY = scale.y;
                }
                if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                    if (moveXLock)
                        position.x = mouse.x + mouseOffsetX;
                    if (moveYLock)
                        position.y = mouse.y + mouseOffsetY;
                    if (moveLock) {
                        position.x = mouse.x + mouseOffsetX;
                        position.y = mouse.y + mouseOffsetY;
                    }
                    if (scaleXLock)
                        scale.x = initialScaleX + (mouse.x - position.x) + mouseOffsetX;
                    if (scaleYLock)
                        scale.y = initialScaleY + (mouse.y - position.y) + mouseOffsetY;
                    if (rotateLock) {

                        float val = MathUtils.atan2Deg(mouse.y - position.y, mouse.x - position.x);

                        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT))
                            val = MathUtils.round(val);
                        else if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT))
                            val = MathUtils.round(val / 5f) * 5;

                        gameObject.transform.rotation = val;

                    }
                    if (moveXLock || moveYLock || moveLock || scaleXLock || scaleYLock || rotateLock) {
                        change = true;
                    }
                } else {
                    moveXLock = false;
                    moveYLock = false;
                    moveLock = false;
                    scaleXLock = false;
                    scaleYLock = false;
                    rotateLock = false;
                    if (change)
                        captureState();
                    change = false;
                }
                position.add(offsetX, offsetY);
                scale.add(scaleOffsetX, scaleOffsetY);
                gameObject.transform.rotation += rotationOffset;
            }
        }

        return !moveXLock && !moveYLock && !canMove && !moveLock && !canMoveY && !canMoveX && !canScaleX &&
            !canScaleY && !scaleXLock && !scaleYLock && !canRotate && !rotateLock;
    }

    public static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    if (!f.delete()) {
                        throw new RuntimeException("Failed to delete file " + f.getAbsolutePath());
                    }
                }
            }
        }
    }

    public static void copyFiles(FileHandle folder, FileHandle dest) {
        FileHandle[] files = folder.list();
        for (int i = 0; i < files.length; i++) {
            FileHandle file = files[i];
            if (file.isDirectory()) {
                file.copyTo(dest);
            } else {
                file.copyTo(dest.child(file.name()));
            }
        }
    }

    private static String preprocessScript(String source, String pkg) {
        StringBuilder result = new StringBuilder();
        result.append("package ");
        result.append(pkg);
        result.append(";\n\n");
        result.append(source);
        return result.toString();
    }

    public static void copyScripts(FileHandle assetsDir, FileHandle srcDirectory, String curPackage) {
        FileHandle[] files = assetsDir.list();
        for (int i = 0; i < files.length; i++) {
            FileHandle file = files[i];
            if (file.isDirectory()) {
                copyScripts(file, srcDirectory, curPackage + "." + file.name());
            } else if (file.extension().equalsIgnoreCase("java")) {
                file.moveTo(srcDirectory.child(file.name()));
                String text = srcDirectory.child(file.name()).readString();
                srcDirectory.child(file.name()).writeString(preprocessScript(text, curPackage), false);
            }
        }
    }

    public static void openFolder(FileHandle folder) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(folder.file().toURI());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static boolean checkFile(String f) {
        File file = new File(f);
        return file.exists() && !file.isDirectory();
    }

    private static void buildSetup() {


        boolean exists = currentProject.path.child("Build").exists();
        if (exists)
            deleteFolder(currentProject.path.child("Build").file());
        else
            currentProject.path.child("Build").child("Temp").mkdirs();

        copyFiles(Gdx.files.local("TemplateGame"), currentProject.path.child("Build").child("Temp").child("GameFolder"));

        FileHandle buildFolder = currentProject.path.child("Build");
        FileHandle projectAssets = currentProject.path.child("Assets");
        FileHandle gameFolder = buildFolder.child("Temp").child("GameFolder");
        FileHandle src = gameFolder.child("core").child("src").child("main").child("java").child("com").child("game").child("scripts");

        gameFolder.child("assets").child("Assets").mkdirs();
        copyFiles(projectAssets, gameFolder.child("assets").child("Assets"));

        copyScripts(gameFolder.child("assets"), src, "com.game.scripts");

        FileHandle mainGameObject = gameFolder.child("assets").child("main.json");
        mainGameObject.writeString(json.toJson(new SerializableProject(currentProject)), false);

        FileHandle coreBuildFile = gameFolder.child("core").child("build.gradle");
        FileHandle desktopBuildFile = gameFolder.child("lwjgl3").child("build.gradle");
        FileHandle htmlBuildFile = gameFolder.child("html").child("build.gradle");

        StringBuilder coreDependencies = new StringBuilder();
        StringBuilder desktopDependencies = new StringBuilder();
        StringBuilder htmlDependencies = new StringBuilder();

        for (int i = 0; i < currentProject.dependencies.size(); i++)
            coreDependencies.append("implementation \"").append(currentProject.dependencies.get(i)).append("\"");

        for (int i = 0; i < currentProject.desktopDependencies.size(); i++)
            desktopDependencies.append("implementation \"").append(currentProject.desktopDependencies.get(i)).append("\"");

        for (int i = 0; i < currentProject.htmlDependencies.size(); i++)
            htmlDependencies.append("implementation \"").append(currentProject.htmlDependencies.get(i)).append("\"");

        coreBuildFile.writeString(coreBuildFile.readString().replace("$CORE_DEPENDENCIES$", coreDependencies.toString()), false);
        desktopBuildFile.writeString(desktopBuildFile.readString().replace("$DESKTOP_DEPENDENCIES$", desktopDependencies.toString()), false);
        htmlBuildFile.writeString(htmlBuildFile.readString().replace("$HTML_DEPENDENCIES$", htmlDependencies.toString()), false);

        if (checkFile(currentProject.appIconLinux))
            Gdx.files.absolute(currentProject.appIconLinux).copyTo(gameFolder.child("lwjgl3").child("icons"));
        if (checkFile(currentProject.appIconWin))
            Gdx.files.absolute(currentProject.appIconWin).copyTo(gameFolder.child("lwjgl3").child("icons"));
        if (checkFile(currentProject.appIconMac))
            Gdx.files.absolute(currentProject.appIconMac).copyTo(gameFolder.child("lwjgl3").child("icons"));

        if (checkFile(currentProject.windowIcon16))
            Gdx.files.absolute(currentProject.windowIcon16).copyTo(gameFolder.child("assets"));
        if (checkFile(currentProject.windowIcon32))
            Gdx.files.absolute(currentProject.windowIcon32).copyTo(gameFolder.child("assets"));
        if (checkFile(currentProject.windowIcon64))
            Gdx.files.absolute(currentProject.windowIcon64).copyTo(gameFolder.child("assets"));
        if (checkFile(currentProject.windowIcon128))
            Gdx.files.absolute(currentProject.windowIcon128).copyTo(gameFolder.child("assets"));
    }

    public static void build(boolean showFolder) {

        buildSetup();

        FileHandle buildFolder = currentProject.path.child("Build");
        FileHandle projectAssets = currentProject.path.child("Assets");
        FileHandle gameFolder = buildFolder.child("Temp").child("GameFolder");
        FileHandle src = gameFolder.child("core").child("src").child("main").child("java").child("com").child("game").child("scripts");

        ProcessBuilder pb = new ProcessBuilder(buildFolder.child("Temp").child("GameFolder").child("gradlew.bat").path(), "jar");
        pb.directory(new File(gameFolder.file().getAbsolutePath()));
        Process p;
        try {
            p = pb.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (true) {
            try {
                if (p.getErrorStream().available() > 0) {
                    throw new RuntimeException("Compilation Error: \n" + new String(p.getErrorStream().readAllBytes()));
                }
                if (p.waitFor(500, TimeUnit.MILLISECONDS)) break;
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        }

        gameFolder.child("lwjgl3").child("build").child("lib").child("Game-1.0.0.jar").copyTo(buildFolder.child(currentProject.buildName + "-" + currentProject.version + ".jar"));
        gameFolder.parent().deleteDirectory();

        if (showFolder) openFolder(buildFolder);

    }

    private static GameObject search(GameObject parent, int ID) {
        for (GameObject gameObject : parent.children) {
            if (gameObject.ID == ID) {
                return gameObject;
            } else {
                return search(gameObject, ID);
            }
        }
        return null;
    }

    public static GameObject getGameObject(int ID) {
        return search(currentProject.getCurrentScene().rootGameObject, ID);
    }

    public static void copyTo(FileHandle src, FileHandle dest, String name) {
        src.copyTo(dest);
        dest.child(src.name()).moveTo(dest.child(name));
    }

    public static void buildApp() {

        buildSetup();
        FileHandle buildFolder = currentProject.path.child("Build");
        FileHandle gameFolder = buildFolder.child("Temp").child("GameFolder");

        ProcessBuilder pb = new ProcessBuilder(buildFolder.child("Temp").child("GameFolder").child("gradlew.bat").path(), "jpackageImage");
        pb.directory(new File(gameFolder.file().getAbsolutePath()));
        Process p;
        try {
            p = pb.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (true) {
            try {
                if (p.getErrorStream().available() > 0) {
                    throw new RuntimeException("Compilation Error: \n" + new String(p.getErrorStream().readAllBytes()));
                }
                if (p.waitFor(500, TimeUnit.MILLISECONDS)) break;
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        }

        gameFolder.child("lwjgl3").child("build").child("jpackage").child("Game").copyTo(buildFolder);
        gameFolder.parent().deleteDirectory();

        openFolder(buildFolder);
    }

    public static void copyComponentsFields(HashMap<Integer, HashMap<String, HashMap<String, Object>>> map, GameObject parent) {
        for (GameObject gameObject : parent.children) {
            HashMap<String, HashMap<String, Object>> components = new HashMap<>();

            for (int i = 0; i < gameObject.components.size(); i++) {
                Component component = gameObject.components.get(i);
                if (component.getClass().getModule() != Transform.class.getModule()) {
                    HashMap<String, Object> fieldMap = new HashMap<>();

                    for (com.badlogic.gdx.utils.reflect.Field f : component.fields) {
                        try {
                            fieldMap.put(f.getName(), f.get(component));
                        } catch (ReflectionException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    components.put(component.getClass().getSimpleName(), fieldMap);
                    gameObject.components.remove(i);
                }
            }

            map.put(gameObject.ID, components);
            copyComponentsFields(map, gameObject);
        }
    }

    public static void addComponentFields(HashMap<Integer, HashMap<String, HashMap<String, Object>>> map, GameObject parent) {
        for (GameObject gameObject : parent.children) {
            HashMap<String, HashMap<String, Object>> components = map.get(gameObject.ID);

            for (String key : components.keySet()) {

                HashMap<String, Object> fieldMap = components.get(key);

                Component component = Utils.getComponent(key);

                for (String fieldName : fieldMap.keySet()) {
                    try {
                        Field field = component.getClass().getField(fieldName);
                        field.set(component, fieldMap.get(fieldName));
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }

                gameObject.addComponent(component);
            }

            addComponentFields(map, gameObject);
        }
    }

    public static <T> boolean contains(T[] arr, T obj) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == obj) return true;
        }
        return false;
    }

    public static boolean isCtrlKeyPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT);
    }

    public static boolean isShiftKeyPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT);
    }

    public static void addAllGameObjects(GameObject parent) {
        allGameObjects.get(currentProject.currentScene).add(parent);
        for (GameObject g : parent.children) {
            addAllGameObjects(g);
        }
    }

    public static void captureState() {
        for (int i = historyIdx; i < history.size; i++)
            history.removeIndex(i);
        history.add(new SerializableGameObject(currentProject.getCurrentScene().rootGameObject));
        if (history.size > 20) history.removeIndex(0);
        historyIdx = history.size;
        projectChange = true;
    }

    private static void fixSelection() {
        if (selectedGameObject != null) {
            int targetID = selectedGameObject.ID;
            for (GameObject gameObject : allGameObjects.get(currentProject.currentScene)) {
                if (gameObject.ID == targetID) {
                    selectedGameObject = gameObject;
                    break;
                }
            }
        } else {
            selection = null;
        }
    }

    public static void undo() {
        if (historyIdx > 0) {
            historyIdx--;
            allGameObjects.get(currentProject.currentScene).clear();
            currentProject.getCurrentScene().rootGameObject = history.get(historyIdx).createGameObject(null);
            addAllGameObjects(currentProject.getCurrentScene().rootGameObject);
            setGameObjectCounter();
            fixSelection();
            projectChange = true;
        }
    }

    public static void redo() {
        if (historyIdx < history.size - 1) {
            historyIdx++;
            allGameObjects.get(currentProject.currentScene).clear();
            currentProject.getCurrentScene().rootGameObject = history.get(historyIdx).createGameObject(null);
            addAllGameObjects(currentProject.getCurrentScene().rootGameObject);
            setGameObjectCounter();
            fixSelection();
            projectChange = true;
        }
    }

    public static void buildGwt(boolean showFolder) {

        Utils.buildSetup();
        FileHandle buildFolder = currentProject.path.child("Build");
        FileHandle gameFolder = buildFolder.child("Temp").child("GameFolder");

        ProcessBuilder pb = new ProcessBuilder(buildFolder.child("Temp").child("GameFolder").child("gradlew.bat").path(), "html:dist");
        pb.directory(new File(gameFolder.file().getAbsolutePath()));
        Process p;
        try {
            p = pb.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (true) {
            System.out.print("");
            try {
                if (p.getErrorStream().available() > 0)
                    throw new RuntimeException("Compilation Error: \n" + new String(p.getErrorStream().readAllBytes()));
                if (p.waitFor(500, TimeUnit.MILLISECONDS)) break;
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        }

        gameFolder.child("html").child("build").child("dist").copyTo(buildFolder);
        gameFolder.parent().deleteDirectory();

        if (showFolder) openFolder(buildFolder);
    }

    public static void buildSource(boolean showFolder) {

        Utils.buildSetup();
        FileHandle buildFolder = currentProject.path.child("Build");
        FileHandle gameFolder = buildFolder.child("Temp").child("GameFolder");

        gameFolder.copyTo(buildFolder);
        gameFolder.parent().deleteDirectory();

        if (showFolder) openFolder(buildFolder);
    }

    public static FileHandle getFile(String path) {
        if (isEngine)
            return Gdx.files.absolute(path);
        else
            return Gdx.files.internal(path.substring(path.indexOf("Assets") + 7));
    }

    public static String getPackage(FileHandle file, String spacing) {
        if (!file.parent().name().equals("Assets")) {
            StringBuilder pkg = new StringBuilder();
            String parent = getPackage(file.parent());
            if (!parent.isEmpty()) pkg.append(getPackage(file.parent())).append(spacing);
            pkg.append(file.parent().name());
            return pkg.toString();
        }
        return file.name();
    }

    public static String getPackage(FileHandle file) {
        return getPackage(file, ".");
    }

    private static XmlReader xmlReader = new XmlReader();

    public static class Artifact {

        public String groupId;
        public String artifactId;
        public String version;
        public String[] other;

        public XmlReader.Element pom;
        public String jarURL;

        public ArrayList<Artifact> dependencies = new ArrayList<>();

    }

    private static Artifact loadArtifact(String artifactURL, String repositoryUrl) {

        if (repositoryUrl.isEmpty()) repositoryUrl = "https://repo.maven.apache.org/maven2/";

        String[] parts = artifactURL.split(":");
        String groupId = parts[0];
        String artifactId = parts[1];
        String version = parts[2];
        String[] other = new String[parts.length - 3];
        for (int i = 0; i < other.length; i++) {
            other[i] = parts[i + 3];
        }
        StringBuilder otherSB = new StringBuilder();
        for (int i = 0; i < other.length; i++) {
            otherSB.append("-").append(other[i]);
        }
        StringBuilder pomPath = new StringBuilder();
        pomPath.append(groupId.replace('.', '/')).append("/").append(artifactId).append("/")
            .append(version).append("/").append(artifactId).append("-").append(version).append(".pom");

        try {
            URL url = new URL(repositoryUrl + pomPath);

            URLConnection connection = url.openConnection();

            connection.setRequestProperty("Accept", "application/xml");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String inputLine;
            java.lang.StringBuilder content = new java.lang.StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            Artifact artifact = new Artifact();
            artifact.groupId = groupId;
            artifact.artifactId = artifactId;
            artifact.version = version;
            artifact.other = other;

            XmlReader.Element element = xmlReader.parse(String.valueOf(content));
            artifact.pom = element;
            artifact.jarURL = repositoryUrl + String.format("%s/%s/%s/%s-%s%s.jar", groupId.replaceAll("\\.", "/"), artifactId, version, artifactId, version, otherSB);

            if (element.hasChild("dependencies")) {
                Array<XmlReader.Element> dependencies = element.getChildByName("dependencies").getChildrenByName("dependency");

                for (XmlReader.Element dependency : dependencies) {

                    String depGroupId = dependency.getChildByName("groupId").getText();
                    String depArtifactId = dependency.getChildByName("artifactId").getText();
                    String depVersion = dependency.getChildByName("version").getText();
                    String depArtifactURL = depGroupId + ":" + depArtifactId + ":" + depVersion;

                    Artifact dependencyArtifact = loadArtifact(depArtifactURL,  "");
                    if (dependencyArtifact != null) {
                        artifact.dependencies.add(dependencyArtifact);
                    } else {
                        if (element.hasChild("repositories")) {
                            Array<XmlReader.Element> repositories = element.getChildByName("repositories").getChildrenByName("repository");
                            for (XmlReader.Element repo : repositories) {
                                String repoUrl = repo.getChildByName("url").getText();
                                dependencyArtifact = loadArtifact(depArtifactURL, repoUrl);
                                if (dependencyArtifact != null) {
                                    artifact.dependencies.add(dependencyArtifact);
                                    break;
                                }
                            }
                        }
                    }

                }
            }

            return artifact;

        } catch (IOException ignored) {}

        return null;

    }

    private static void downloadArtifacts(FileHandle apiDir, ArrayList<Artifact> deps) {
        for (Artifact artifact : deps) {
            downloadArtifact(apiDir, artifact);
        }
    }

    private static void downloadArtifact(FileHandle apiDir, Artifact artifact) {
        try {
            if (artifact != null) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < artifact.other.length; i++) {
                    sb.append("-").append(artifact.other[i]);
                }
                FileUtils.copyURLToFile(new URL(artifact.jarURL), apiDir.child("Dependencies").child(artifact.artifactId + "-" + artifact.version + sb + ".jar").file());
                downloadArtifacts(apiDir, artifact.dependencies);
            }
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    private static void downloadDependencies(FileHandle apiDir, ArrayList<String> deps) {
        for (String dependency : deps) {
            Artifact artifact = null;
            for (int i = 0; i < currentProject.repositories.size(); i++) {
                artifact = loadArtifact(dependency, currentProject.repositories.get(i));
                System.out.println(artifact);
                if (artifact != null) break;
            }
            downloadArtifact(apiDir, artifact);
        }
    }

    public static void updateDependencies() {
        FileHandle apiDir = currentProject.path.child("Api");

        apiDir.child("Dependencies").mkdirs();
        apiDir.child("Dependencies").emptyDirectory();

        downloadDependencies(apiDir, currentProject.dependencies);
        downloadDependencies(apiDir, currentProject.desktopDependencies);
        downloadDependencies(apiDir, currentProject.htmlDependencies);

    }

}
