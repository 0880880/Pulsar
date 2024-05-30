package com.pulsar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.pulsar.api.*;
import com.pulsar.api.annotations.TreeEnd;
import com.pulsar.api.annotations.TreeStart;
import com.pulsar.api.audio.AudioClip;
import com.pulsar.audio.BufferedSoundSource;
import com.pulsar.api.components.*;
import com.pulsar.api.graphics.*;
import com.pulsar.api.math.Matrix3;
import com.pulsar.api.math.Matrix4;
import com.pulsar.api.math.Vector2;
import com.pulsar.api.math.Vector3;
import com.pulsar.utils.AssetPackInfo;
import com.pulsar.utils.AssetPackUtils;
import com.pulsar.utils.GameObjectPreset;
import com.pulsar.utils.Utils;

import imgui.ImVec2;
import imgui.extension.imguizmo.ImGuizmo;
import imgui.internal.ImGui;
import imgui.ImGuiIO;
import imgui.ImGuiViewport;
import imgui.flag.*;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import imgui.internal.flag.ImGuiDockNodeFlags;
import imgui.type.*;
import lombok.AllArgsConstructor;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.nfd.NativeFileDialog;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;

import static com.pulsar.Statics.*;

public class Gui {

    final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    FileHandle[] assets;

    ImGuiIO io;

    HashMap<String, ArrayList<FileHandle>> assetsMap = new HashMap<>();

    ImString renameData = new ImString();

    ImString tempString = new ImString();
    ImInt tempInteger = new ImInt();
    ImFloat tempFloat = new ImFloat();
    ImDouble tempDouble = new ImDouble();
    ImBoolean tempBoolean = new ImBoolean();
    float[] tempVec2 = new float[2];
    float[] tempVec3 = new float[3];
    float[] tempColor = new float[4];

    private String projectExplorerPath = "main";

    ImBoolean toggleBuildWindow = new ImBoolean();

    ImString fileName = new ImString();

    FileHandle contextMenuFile = null;

    FileHandle renameFile = null;

    ImBoolean newProjModal = new ImBoolean(false);
    ImBoolean openProjModal = new ImBoolean(false);
    ImString projectPath = new ImString();
    ImString projectName = new ImString();
    String lastProjectPath = "SDLJFNSFIJGHOUSTLJWNFLVBLJBLJSJTSVNLVBSLJRTLJFLVBJNGLJKRLTN";
    boolean folderEmpty = true;

    float menuBarSize = 0;

    boolean focus;

    public boolean isAnyWindowHovered = false;

    private final ImBoolean showPreferences = new ImBoolean(false);
    private final ImBoolean showProjectSettings = new ImBoolean(false);
    private final ImBoolean showAssetsWindow = new ImBoolean(false);

    private final ImString imString = new ImString();

    private final String consoleID = UUID.randomUUID().toString();

    public com.badlogic.gdx.graphics.Texture fileIcon = new com.badlogic.gdx.graphics.Texture("icons/file.png");
    public com.badlogic.gdx.graphics.Texture codeIcon = new com.badlogic.gdx.graphics.Texture("icons/file-code.png");
    public com.badlogic.gdx.graphics.Texture folderIcon = new com.badlogic.gdx.graphics.Texture("icons/folder.png");
    public com.badlogic.gdx.graphics.Texture volumeIcon = new com.badlogic.gdx.graphics.Texture("icons/volume-2.png");
    public com.badlogic.gdx.graphics.Texture handIcon = new com.badlogic.gdx.graphics.Texture("icons/hand.png");
    public com.badlogic.gdx.graphics.Texture playIcon = new com.badlogic.gdx.graphics.Texture("icons/play.png");
    public com.badlogic.gdx.graphics.Texture pauseIcon = new com.badlogic.gdx.graphics.Texture("icons/pause.png");
    public com.badlogic.gdx.graphics.Texture expandIcon = new com.badlogic.gdx.graphics.Texture("icons/expand.png");
    public com.badlogic.gdx.graphics.Texture moveIcon = new com.badlogic.gdx.graphics.Texture("icons/move.png");
    public com.badlogic.gdx.graphics.Texture refreshIcon = new com.badlogic.gdx.graphics.Texture("icons/refresh-cw.png");

    public BufferedSoundSource selectedAudioSource;
    private final float[] audioSourcePosition = new float[1];

    public com.badlogic.gdx.graphics.Texture fboTexture;
    public int sceneX;
    public int sceneY;
    public int lastSceneWidth;
    public int lastSceneHeight;

    Runtime.Version version;

    int buildPlatform = 0;
    int os = 0;

    int buildProgress = -1;

    GameObjectPreset emptyGameObjectPreset = (name, parent) -> {
        GameObject gameObject = new GameObject(name);
        parent.addGameObject(gameObject);
        return gameObject;
    };

    GameObjectPreset spriteGameObjectPreset = (name, parent) -> {
        GameObject gameObject = new GameObject(name);
        gameObject.transform.scale.set(1,1);
        gameObject.addComponent(new SpriteRenderer());
        parent.addGameObject(gameObject);
        return gameObject;
    };

    GameObjectPreset physicsSpriteGameObjectPreset = (name, parent) -> {
        GameObject gameObject = new GameObject(name);
        gameObject.transform.scale.set(1,1);
        gameObject.addComponent(new SpriteRenderer());
        gameObject.addComponent(new RigidBody());
        gameObject.addComponent(new BoxCollider());
        parent.addGameObject(gameObject);
        return gameObject;
    };

    public Gui() {

        long handle = ((Lwjgl3Graphics) Gdx.graphics).getWindow().getWindowHandle();
        GLFW.glfwMakeContextCurrent(handle);
        ImGui.setCurrentContext(ImGui.createContext());

        imGuiGlfw.init(handle, true);
        imGuiGl3.init("#version 330");

        imgui.internal.ImGui.getIO().addConfigFlags(ImGuiConfigFlags.DockingEnable);

        io = ImGui.getIO();
        io.setConfigWindowsMoveFromTitleBarOnly(true);
        ImGui.getStyle().setFrameBorderSize(1);
        ImGui.getStyle().setTabBorderSize(1);
        ImGui.getStyle().setWindowRounding(3);
        ImGui.getStyle().setFrameRounding(3);
        ImGui.getStyle().setPopupRounding(3);
        ImGui.getStyle().setTabRounding(3);
        ImGui.getStyle().setGrabRounding(3);
        ImGui.getStyle().setLogSliderDeadzone(3);
        ImGui.getStyle().setColorButtonPosition(ImGuiDir.Left);

        version = Runtime.version();

        String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
        if ((OS.contains("mac")) || (OS.contains("darwin"))) {
            os = 3;
        } else if (OS.contains("win")) {
            os = 2;
        } else if (OS.contains("nux")) {
            os = 1;
        }

    }

    private void createAssets(FileHandle parent, String parentName) {
        FileHandle[] list = parent.list();
        ArrayList<FileHandle> arr = new ArrayList<>();
        assetsMap.put(parentName, arr);
        for (FileHandle file : list) {
            arr.add(file);
            if (file.isDirectory()) createAssets(file, parentName + "/" + file.nameWithoutExtension());
        }
    }

    public void createAssets() {
        File[] asts = Statics.currentProject.path.child("Assets").file().listFiles();
        if (asts == null) return;
        assets = new FileHandle[asts.length];
        for (int i = 0; i < asts.length; i++) {
            assets[i] = Gdx.files.absolute(asts[i].getAbsolutePath());
        }
        createAssets(currentProject.path.child("Assets"), "main");
    }

    private boolean gameObjectExists(GameObject parent, String name) {
        boolean exists = false;
        for (int i = 0; i < parent.children.size(); i++) {
            if (parent.children.get(i).name.equals(name)) {
                exists = true;
                break;
            }
        }
        return exists;
    }

    String getName(GameObject parent, String name) {
        boolean exists = gameObjectExists(parent, name);
        int i = 0;
        while (exists) {
            i++;
            name = name + " (" + i + ")";
            exists = gameObjectExists(parent, name);
        }
        return name;
    }

    void treeNodeContextMenu(GameObject object) {
        ImGui.setNextWindowSizeConstraints(150, 0, 150, 10000);
        if (ImGui.beginPopupContextItem(object.ID + "PopupContext")) {
            ImGui.setNextWindowSizeConstraints(150, 0, 150, 10000);
            if (ImGui.beginMenu("New")) {
                if (ImGui.selectable("Empty Object")) {
                    String name = getName(object, "Empty Object");
                    Utils.captureState();
                    emptyGameObjectPreset.create(name, object);
                }
                if (ImGui.selectable("Sprite")) {
                    String name = getName(object, "Sprite");
                    Utils.captureState();
                    spriteGameObjectPreset.create(name, object);
                }
                if (ImGui.selectable("Box Physics")) {
                    String name = getName(object, "Box Physics");
                    Utils.captureState();
                    physicsSpriteGameObjectPreset.create(name, object);
                }
                ImGui.endMenu();
            }
            ImGui.separator();
            if (ImGui.selectable("Delete")) {
                Utils.captureState();
                object.parent.children.remove(object);
                allGameObjects.get(currentProject.currentScene).removeValue(object, true);
            }
            isAnyWindowHovered |= ImGui.isWindowHovered();
            ImGui.endPopup();
        }
    }

    private boolean checkChildren(GameObject gameObject) {
        if (searchBar.isBlank() && searchBar.isEmpty()) return true;
        if (gameObject.name.toLowerCase().contains(searchBar.toLowerCase())) return true;
        for (GameObject child : gameObject.children) {
            if (child.name.toLowerCase().contains(searchBar.toLowerCase())) return true;
            else if (checkChildren(gameObject)) return true;
        }
        return searchBar.isBlank() || searchBar.isEmpty();
    }

    public void drawTreeChildren(GameObject base, boolean selected) {
        for (int i = 0; i < base.children.size(); i++) {
            GameObject child = base.children.get(i);

            if (checkChildren(child)) {
                if (!child.children.isEmpty()) {

                    boolean sel = false;

                    boolean treeOpen = false;
                    if (!child.renameMode) {
                        String name = child.name;
                        ImGui.getWindowDrawList().channelsSplit(2);
                        ImGui.getWindowDrawList().channelsSetCurrent(1);
                        treeOpen = ImGui.treeNodeEx(name, ImGuiTreeNodeFlags.SpanFullWidth);
                        ImGui.getWindowDrawList().channelsSetCurrent(0);
                        if (selectedGameObjects != null && Utils.contains(selectedGameObjects, child) || selection == child || selected) {
                            ImVec2 p_min = ImGui.getItemRectMin();
                            ImVec2 p_max = ImGui.getItemRectMax();
                            ImGui.getWindowDrawList().addRectFilled(p_min.x, p_min.y, p_max.x, p_max.y, ImGui.getColorU32(selected ? ImGuiCol.Button : (selection == child ? ImGuiCol.ButtonActive : ImGuiCol.Button)));
                            if (selection != child) sel = true;
                        }
                        ImGui.getWindowDrawList().channelsMerge();
                        if (ImGui.isItemHovered() && ImGui.isMouseDoubleClicked(0)) {
                            child.renameMode = true;
                            renameData.set(child.name);
                        }
                    } else {
                        if (ImGui.inputText("##RenameTo", renameData)) {
                            selection = child;
                        }
                        boolean hovered = ImGui.isItemHovered();
                        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || (!hovered && ImGui.isMouseClicked(0))) {
                            Utils.captureState();
                            child.name = renameData.get();
                            child.renameMode = false;
                        }
                    }
                    treeNodeContextMenu(child);

                    if (ImGui.isItemClicked()) {
                        selection = child;
                    }
                    if (treeOpen) {
                        drawTreeChildren(child, sel);
                        ImGui.treePop();
                    }
                } else {
                    if (!child.renameMode) {
                        ImGui.getWindowDrawList().channelsSplit(2);
                        ImGui.getWindowDrawList().channelsSetCurrent(1);
                        if (ImGui.selectable(child.name)) {
                            if (Utils.isShiftKeyPressed() && selection != null && selection != child && selection.getClass() == GameObject.class &&
                                base.children.contains(selection)) {
                                int dstIdx = base.children.indexOf(selection);

                                int n = Math.abs(dstIdx - i) + 1;
                                selectedGameObjects = new GameObject[n];

                                if (dstIdx > i) {
                                    int idx = 0;
                                    for (int j = i; j <= dstIdx; j++) {
                                        selectedGameObjects[idx] = base.children.get(j);
                                        idx++;
                                    }
                                } else {
                                    int idx = 0;
                                    for (int j = i; j >= dstIdx; j--) {
                                        selectedGameObjects[idx] = base.children.get(j);
                                        idx++;
                                    }
                                }
                            } else {
                                selectedGameObjects = null;
                            }
                            selection = child;
                        }
                        ImGui.getWindowDrawList().channelsSetCurrent(0);
                        if (selectedGameObjects != null && Utils.contains(selectedGameObjects, child) || selection == child || selected) {
                            ImVec2 p_min = ImGui.getItemRectMin();
                            ImVec2 p_max = ImGui.getItemRectMax();
                            ImGui.getWindowDrawList().addRectFilled(p_min.x, p_min.y, p_max.x, p_max.y, ImGui.getColorU32(selected ? ImGuiCol.Button : (selection == child ? ImGuiCol.ButtonActive : ImGuiCol.Button)));
                        }
                        ImGui.getWindowDrawList().channelsMerge();
                        if (ImGui.beginDragDropSource()) {
                            ImGui.setDragDropPayload("GameObject", child);
                            ImGui.selectable(child.name);
                            ImGui.endDragDropSource();
                        }
                        if (ImGui.beginDragDropTarget()) {
                            Object payload = ImGui.acceptDragDropPayload("GameObject");
                            if (payload != null && payload.getClass() == GameObject.class) {
                                GameObject g = (GameObject) payload;
                                g.parent.children.remove(g);
                                child.children.add(g);
                                g.parent = child;
                            }
                            ImGui.endDragDropTarget();
                        }
                        ImGui.setCursorPosY(ImGui.getCursorPosY() - 3);
                        ImGui.invisibleButton("##GameObject_" + child.ID + "_TARGET", ImGui.getContentRegionAvailX(), 6);
                        ImGui.setCursorPosY(ImGui.getCursorPosY() - 3);
                        if (ImGui.beginDragDropTarget()) {
                            Object payload = ImGui.acceptDragDropPayload("GameObject");
                            if (payload != null && payload.getClass() == GameObject.class) {
                                GameObject g = (GameObject) payload;
                                g.parent.children.remove(g);
                                child.parent.children.add(child.parent.children.indexOf(child) + 1, g);
                                g.parent = child.parent;
                            }
                            ImGui.endDragDropTarget();
                        }

                        if (ImGui.isItemHovered() && ImGui.isMouseDoubleClicked(0)) {
                            child.renameMode = true;
                            renameData.set(child.name);
                        }
                    } else {
                        if (ImGui.inputText("##RenameTo", renameData)) {
                            selection = child;
                        }
                        boolean hovered = ImGui.isItemHovered();
                        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || (!hovered && ImGui.isMouseClicked(0))) {
                            Utils.captureState();
                            child.name = renameData.get();
                            child.renameMode = false;
                        }
                    }
                    treeNodeContextMenu(child);
                }
            }
        }
    }

    private Rectangle rectangleFromGameObject(GameObject gameObject) {
        Rectangle rectangle = new Rectangle();
        rectangle.set(gameObject.transform.position.x - gameObject.transform.scale.x / 2f, gameObject.transform.position.y - gameObject.transform.scale.y / 2f, gameObject.transform.scale.x, gameObject.transform.scale.y);
        return rectangle;
    }

    private String inputText(String label, String text, float w0, float w1) {
        imString.set(text);
        ImGui.text(label);
        ImGui.sameLine();
        ImGui.setCursorPosX(w0);
        ImGui.setNextItemWidth(w1);
        ImGui.inputText("##" + label, imString);
        return imString.get();
    }

    private String inputText(String label, String text) {
        return inputText(label, text, 200, 700);
    }

    private String inputTextID(String id, String text) {
        imString.set(text);
        ImGui.inputText("##" + id, imString);
        return imString.get();
    }

    public void refresh() {
        createAssets();
    }

    private void open() {
        PointerBuffer path = MemoryUtil.memCallocPointer(1);

        int res = NativeFileDialog.NFD_PickFolder("", path);

        if (res == NativeFileDialog.NFD_OKAY) {
            Utils.loadProject(MemoryUtil.memUTF8(path.get(0)));
        }

        MemoryUtil.memFree(path);
    }

    void mainMenuBar() {

        menuBarSize = ImGui.getWindowHeight();

        if (Utils.isCtrlKeyPressed() && Gdx.input.isKeyJustPressed(Input.Keys.N)) System.out.println("New Project");
        if (Utils.isCtrlKeyPressed() && Gdx.input.isKeyJustPressed(Input.Keys.S)) Utils.save();
        if (Utils.isCtrlKeyPressed() && Gdx.input.isKeyJustPressed(Input.Keys.O)) open();
        if (Utils.isCtrlKeyPressed() && Gdx.input.isKeyJustPressed(Input.Keys.B)) toggleBuildWindow.set(!toggleBuildWindow.get());

        if (ImGui.beginMenu("File")) {

            if (ImGui.menuItem("New", "Ctrl+N")) System.out.println("New Project");
            if (ImGui.menuItem("Save", "Ctrl+S")) Utils.save();
            if (ImGui.menuItem("Open", "Ctrl+O")) open();
            ImGui.separator();
            ImGui.beginDisabled(currentProject == null);
            if (ImGui.menuItem("Build", "Ctrl+B")) toggleBuildWindow.set(!toggleBuildWindow.get());
            ImGui.endDisabled();
            ImGui.separator();
            if (ImGui.menuItem("Preferences")) {
                showPreferences.set(!showPreferences.get());
            }
            ImGui.separator();
            if (ImGui.menuItem("Exit")) {
                Gdx.app.exit();
            }

            ImGui.endMenu();
        }

    }

    public void renderMainMenu() {

        isAnyWindowHovered = false;

        imGuiGlfw.newFrame();
        ImGui.newFrame();

        ImGui.beginMainMenuBar();

        mainMenuBar();

        ImGui.endMainMenuBar();

        if (showPreferences.get()) {
            ImGui.begin("Preferences", showPreferences);

            String oldPath = preferences.getString("idePath");
            String newPath = inputText("IDE Path", oldPath);
            if (!newPath.equals(oldPath)) preferences.putString("idePath", newPath);

            int oldUndoHistory = preferences.getInteger("undoHistorySize", 20);
            int newUndoHistory = inputInt("Undo History Size", oldUndoHistory);
            if (newUndoHistory != oldUndoHistory) preferences.putInteger("undoHistorySize", newUndoHistory);

            ImGui.end();
        }

        ImGui.setNextWindowPos(0,menuBarSize);
        ImGui.setNextWindowSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - menuBarSize);
        ImGui.begin("MainMenu", ImGuiWindowFlags.NoBringToFrontOnFocus | ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoResize);

        float w = Gdx.graphics.getWidth() - 100;

        ImGui.beginChild("Options", w, 100);

        if (ImGui.button("New Project##NewProjectButton", 200, 70)) {
            projectPath.set("");
            projectName.set("");
            ImGui.openPopup("New Project");
            newProjModal.set(true);
        }
        ImGui.sameLine();
        if (ImGui.button("Open Project##OpenProjectButton", 200, 70)) {
            projectPath.set("");
            projectName.set("");
            ImGui.openPopup("Open Project");
            openProjModal.set(true);
        }

        ImGui.setNextWindowSizeConstraints(500, 150, 500, 150);
        if (ImGui.beginPopupModal("New Project", newProjModal)) {

            ImGui.text("Project Path");
            ImGui.sameLine();
            ImGui.inputText("##ProjectPath", projectPath);
            ImGui.sameLine();
            if (ImGui.button("...")) {
                projectPath.set(Utils.pickFolder());
            }

            ImGui.text("Project Name");
            ImGui.sameLine();
            ImGui.inputText("##ProjectName", projectName);

            boolean canCreate = projectPath.isNotEmpty() && !folderEmpty;

            if (!lastProjectPath.equals(projectPath.get()) && projectPath.isNotEmpty()) {
                folderEmpty = Gdx.files.absolute(projectPath.get()).list().length == 0;
            }

            lastProjectPath = projectPath.get();

            ImGui.beginDisabled(canCreate);
            ImGui.setCursorPosY(ImGui.getWindowHeight() - 40);
            if (ImGui.button("Create Project", 180, 30)) {
                Utils.createProject(projectPath.get(), projectName.get());
            }
            ImGui.endDisabled();

            isAnyWindowHovered |= ImGui.isWindowHovered();
            ImGui.endPopup();
        }

        ImGui.setNextWindowSizeConstraints(500, 150, 500, 150);
        if (ImGui.beginPopupModal("Open Project", openProjModal)) {

            ImGui.text("Project Path");
            ImGui.sameLine();
            ImGui.inputText("##ProjectPath", projectPath);
            ImGui.sameLine();
            if (ImGui.button("...")) {
                projectPath.set(Utils.pickFolder());
            }

            boolean canCreate = projectPath.isEmpty();

            ImGui.beginDisabled(canCreate);
            ImGui.setCursorPosY(ImGui.getWindowHeight() - 40);
            if (ImGui.button("Open Project", 180, 30)) {
                Utils.loadProject(projectPath.get());
            }
            ImGui.endDisabled();

            isAnyWindowHovered |= ImGui.isWindowHovered();
            ImGui.endPopup();
        }

        ImGui.endChild();

        ImGui.beginChild("Projects", w,  ImGui.getContentRegionAvailY());

        ImGui.text("Projects");
        ImGui.separator();
        ImGui.spacing();

        for (String project : recentProjects) {
            if (ImGui.selectable(project)) {
                if (!Utils.loadProject(project)) {
                    recentProjects.removeValue(project, false);
                }
            }
        }

        ImGui.endChild();

        ImGui.end();

        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());

    }

    float[] colorPickerCol = new float[4];

    private void colorRangeEditor(String ID, ColorRange colorRange, float sizeX, float sizeY) {

        float curX = ImGui.getCursorPosX() + ImGui.getWindowPosX() - ImGui.getScrollX();
        float curY = ImGui.getCursorPosY() + ImGui.getWindowPosY() - ImGui.getScrollY() + 10;

        int selectedPoint = ImGui.getStateStorage().getInt(ImGui.getID(ID), -1);

        Color lastColor = colorRange.getColor(0);
        float lastTime = colorRange.getTime(0);

        int leftCol = ImGui.getColorU32(lastColor.r, lastColor.g, lastColor.b, lastColor.a);

        ImGui.getWindowDrawList().addRectFilled(curX, curY, curX + sizeX * lastTime, curY + sizeY, leftCol);

        for (int i = 1; i < colorRange.size(); i++) {
            Color c = colorRange.getColor(i);
            float time = colorRange.getTime(i);

            leftCol = i == 1 ? leftCol : ImGui.getColorU32(lastColor.r, lastColor.g, lastColor.b, lastColor.a);
            int rightCol = ImGui.getColorU32(c.r, c.g, c.b, c.a);

            ImGui.getWindowDrawList().addRectFilledMultiColor(curX + sizeX * lastTime, curY, curX + sizeX * time, curY + sizeY, leftCol, rightCol, rightCol, leftCol);

            lastColor = c;
            lastTime = time;
        }

        leftCol = ImGui.getColorU32(lastColor.r, lastColor.g, lastColor.b, lastColor.a);
        ImGui.getWindowDrawList().addRectFilled(curX + sizeX * lastTime, curY, curX + sizeX, curY + sizeY, leftCol);

        for (int i = 0; i < colorRange.size(); i++) {
            float time = colorRange.getTime(i);
            Color c = colorRange.getColor(i);
            ImGui.getWindowDrawList().addRectFilled(curX + sizeX * time - 4, curY - 6, curX + sizeX * time + 4, curY + sizeY + 6, ImGui.getColorU32(1,1,1,1), 8);
            ImGui.getWindowDrawList().addRectFilled(curX + sizeX * time - 3, curY - 5, curX + sizeX * time + 3, curY + sizeY + 5, ImGui.getColorU32(c.r, c.g, c.b, c.a), 6);
            float mouseX = ImGui.getMousePosX();
            float mouseY = ImGui.getMousePosY();
            boolean isHovering = mouseX >= curX + sizeX * time - 4 && mouseX <= curX + sizeX * time + 4 &&
                mouseY >= curY - 6 && mouseY <= curY + sizeY + 6;
            if (isHovering) Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
            if (isHovering && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && selectedPoint == -1) selectedPoint = i;
            if (isHovering && Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT) && i != 0) colorRange.remove(i);
        }

        if (ImGui.getStateStorage().getBool(ImGui.getID(ID + "_cp"), false)) {
            Color c = colorRange.getColor(ImGui.getStateStorage().getInt(ImGui.getID(ID + "_cp_p"), 0));
            ImGui.setNextWindowPos(ImGui.getCursorScreenPosX() - 200, ImGui.getCursorScreenPosY() - 300);
            ImGui.setNextWindowSize(250, 250);
            ImGui.begin("Color Picker");
            colorPickerCol[0] = ImGui.getStateStorage().getFloat(ImGui.getID(ID + "_cp_0"), c.r);
            colorPickerCol[1] = ImGui.getStateStorage().getFloat(ImGui.getID(ID + "_cp_1"), c.g);
            colorPickerCol[2] = ImGui.getStateStorage().getFloat(ImGui.getID(ID + "_cp_2"), c.b);
            colorPickerCol[3] = ImGui.getStateStorage().getFloat(ImGui.getID(ID + "_cp_3"), c.a);
            ImGui.colorPicker4("##" + ID + "_color_picker", colorPickerCol);
            c.set(colorPickerCol[0],colorPickerCol[1],colorPickerCol[2],colorPickerCol[3]);
            ImGui.getStateStorage().setFloat(ImGui.getID(ID + "_cp_0"), colorPickerCol[0]);
            ImGui.getStateStorage().setFloat(ImGui.getID(ID + "_cp_1"), colorPickerCol[1]);
            ImGui.getStateStorage().setFloat(ImGui.getID(ID + "_cp_2"), colorPickerCol[2]);
            ImGui.getStateStorage().setFloat(ImGui.getID(ID + "_cp_3"), colorPickerCol[3]);
            boolean hovered = ImGui.getMousePosX() > ImGui.getCursorScreenPosX() &&
                ImGui.getMousePosX() < ImGui.getCursorScreenPosX() + 250 &&
                ImGui.getMousePosY() > ImGui.getCursorScreenPosY() - 250 &&
                ImGui.getMousePosY() < ImGui.getCursorScreenPosY();

            ImGui.end();
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && !hovered) {
                ImGui.getStateStorage().setBool(ImGui.getID(ID + "_cp"), false);
            }
        }

        if (selectedPoint != -1 && ImGui.isMouseDoubleClicked(0)) {
            ImGui.getStateStorage().setInt(ImGui.getID(ID + "_cp_p"), selectedPoint);
            ImGui.getStateStorage().setBool(ImGui.getID(ID + "_cp"), true);
            Color c = colorRange.getColor(selectedPoint);
            ImGui.getStateStorage().setFloat(ImGui.getID(ID + "_cp_0"), c.r);
            ImGui.getStateStorage().setFloat(ImGui.getID(ID + "_cp_1"), c.g);
            ImGui.getStateStorage().setFloat(ImGui.getID(ID + "_cp_2"), c.b);
            ImGui.getStateStorage().setFloat(ImGui.getID(ID + "_cp_3"), c.a);
        } else {
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && selectedPoint > 0) {
                float mouseX = ImGui.getMousePosX();
                float t = MathUtils.clamp((mouseX - curX) / sizeX, 0, 1);
                boolean swapF = selectedPoint < colorRange.size() - 1 && t > colorRange.getTime(selectedPoint + 1);
                boolean swapB = t < colorRange.getTime(selectedPoint - 1);
                colorRange.setTime(selectedPoint, t);
                if (swapF) {
                    Color c = colorRange.getColor(selectedPoint);
                    colorRange.remove(selectedPoint);
                    colorRange.insert(selectedPoint + 1, t, c);
                    selectedPoint++;
                }
                if (swapB) {
                    Color c = colorRange.getColor(selectedPoint);
                    colorRange.remove(selectedPoint);
                    colorRange.insert(selectedPoint - 1, t, c);
                    selectedPoint--;
                }
            } else {
                selectedPoint = -1;
            }
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            float mouseX = ImGui.getMousePosX();
            float mouseY = ImGui.getMousePosY();
            boolean isHovering = mouseX >= curX && mouseX <= curX + sizeX &&
                mouseY >= curY && mouseY <= curY + sizeY;
            float t = MathUtils.clamp((mouseX - curX) / sizeX, 0, 1);
            boolean canInsert = true;
            for (int i = 0; i < colorRange.size(); i++) {
                if (MathUtils.isEqual(t, colorRange.getTime(i), .05f)) {
                    canInsert = false;
                    break;
                }
            }
            if (isHovering && canInsert && !MathUtils.isZero(t)) {
                int i = 0;
                while (i < colorRange.size()) {
                    if (t < colorRange.getTime(i)) break;
                    i++;
                }
                colorRange.insert(i, t, colorRange.valueAt(new Color(), t));
            }
        }

        ImGui.setCursorPos(curX + sizeX + ImGui.getScrollX(), curY - ImGui.getWindowPosY() + ImGui.getScrollY() + sizeY + 10);

        ImGui.getStateStorage().setInt(ImGui.getID(ID), selectedPoint);

    }

    private void curveEditor(String ID, Curve curve, float sizeX, float sizeY) {

        float curX = ImGui.getCursorPosX() + ImGui.getWindowPosX() - ImGui.getScrollX();
        float curY = ImGui.getCursorPosY() + ImGui.getWindowPosY() - ImGui.getScrollY();

        int selectedPoint = ImGui.getStateStorage().getInt(ImGui.getID(ID), -1);

        ImGui.getWindowDrawList().addRect(curX, curY, curX + sizeX, curY + sizeY, ImGui.getColorU32(ImGuiCol.Border));
        ImGui.getWindowDrawList().addRectFilled(curX, curY, curX + sizeX, curY + sizeY, ImGui.getColorU32(ImGuiCol.FrameBg));

        ImGui.getWindowDrawList().addLine(curX + sizeX * 1/4f, curY, curX + sizeX * 1/4f, curY + sizeY, ImGui.getColorU32(ImGuiCol.Border), 1);
        ImGui.getWindowDrawList().addLine(curX + sizeX * 2/4f, curY, curX + sizeX * 2/4f, curY + sizeY, ImGui.getColorU32(ImGuiCol.Border), 1);
        ImGui.getWindowDrawList().addLine(curX + sizeX * 3/4f, curY, curX + sizeX * 3/4f, curY + sizeY, ImGui.getColorU32(ImGuiCol.Border), 1);

        ImGui.getWindowDrawList().addLine(curX, curY + sizeY * 1/4f, curX + sizeX, curY + sizeY * 1/4f, ImGui.getColorU32(ImGuiCol.Border), 1);
        ImGui.getWindowDrawList().addLine(curX, curY + sizeY * 2/4f, curX + sizeX, curY + sizeY * 2/4f, ImGui.getColorU32(ImGuiCol.Border), 1);
        ImGui.getWindowDrawList().addLine(curX, curY + sizeY * 3/4f, curX + sizeX, curY + sizeY * 3/4f, ImGui.getColorU32(ImGuiCol.Border), 1);

        int i = 0;
        if (curve.size() > 0) {
            float lastX = curX + curve.getPoint(i).x * sizeX;
            float lastY = curY + sizeY - curve.getPoint(i).y * sizeY;
            for (i = 1; i < curve.size(); i++) {
                float vX = curve.getPoint(i).x;
                float vY = curve.getPoint(i).y;
                ImGui.getWindowDrawList().addLine(lastX, lastY, curX + sizeX * vX, curY + sizeY - vY * sizeY, ImGui.getColorU32(ImGuiCol.Button), 3);
                lastX = curX + sizeX * vX;
                lastY = curY + sizeY - vY * sizeY;
            }
            ImGui.getWindowDrawList().addLine(lastX, lastY, curX + sizeX, lastY, ImGui.getColorU32(ImGuiCol.Button), 3);
        }

        boolean anyPointHovered = false;

        ImGui.pushStyleVar(ImGuiStyleVar.FrameRounding, 20);
        for (i = 0; i < curve.size(); i++) {
            ImGui.setCursorPos(curX - ImGui.getWindowPosX() + sizeX * curve.getPoint(i).x - 5 + ImGui.getScrollX(), curY - ImGui.getWindowPosY() + sizeY - curve.getPoint(i).y * sizeY - 5 + ImGui.getScrollY());
            if (ImGui.button("##CurvePoint" + i, 10, 10) ||
                (ImGui.isItemHovered() && Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) || ImGui.isItemActive()) {
                if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
                    curve.remove(i);
                    selectedPoint = -1;
                } else {
                    selectedPoint = i;
                }
            }
            if (ImGui.isItemHovered()) ImGui.setTooltip(curve.getPoint(i).toString());
            anyPointHovered |= ImGui.isItemHovered();
        }
        ImGui.popStyleVar();

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && selectedPoint != -1) {
            float cX = ((ImGui.getMousePosX() - curX) / sizeX);
            float cY = 1 - ((ImGui.getMousePosY() - curY) / sizeY);
            curve.getPoint(selectedPoint).set(selectedPoint > 0 ? MathUtils.clamp(cX, curve.getPoint(selectedPoint - 1).x, selectedPoint != curve.size() - 1 ? curve.getPoint(selectedPoint + 1).x : 1) : 0, MathUtils.clamp(cY, 0, 1));
            ImGui.setTooltip(curve.getPoint(selectedPoint).toString());
        } else {
            selectedPoint = -1;
        }

        if (!anyPointHovered && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && curve.size() > 0 &&
            ImGui.getMousePosX() >= curX && ImGui.getMousePosY() >= curY && ImGui.getMousePosX() <= curX + sizeX &&
            ImGui.getMousePosY() <= curY + sizeY) {
            float cX = ((ImGui.getMousePosX() - curX) / sizeX);
            int last = 0;
            for (i = 0; i < curve.size(); i++) {
                if (curve.getPoint(i).x > cX) {
                    break;
                }
                last = i;
            }
            float cY = 1 - ((ImGui.getMousePosY() - curY) / sizeY);
            curve.insert(last + 1, new Vector2(MathUtils.clamp(cX, 0, 1), MathUtils.clamp(cY, 0, 1)));
            selectedPoint = last + 1;
        }

        ImGui.setCursorPos(curX + sizeX + ImGui.getScrollX(), curY - ImGui.getWindowPosY() + ImGui.getScrollY() + sizeY);

        ImGui.getStateStorage().setInt(ImGui.getID(ID), selectedPoint);

    }

    @AllArgsConstructor
    static class TexturePayload {
        public TextureAsset textureAsset;
    }

    @AllArgsConstructor
    static class AudioClipPayload {
        public AudioClip audioClip;
    }

    @AllArgsConstructor
    static class GameObjectPayload {
        public GameObject gameObject;
    }

    @AllArgsConstructor
    static class MaterialPayload {
        public Material material;
    }

    @AllArgsConstructor
    static class FileHandlePayload {
        public com.pulsar.api.FileHandle fileHandle;
    }

    private float toolbarHeight;

    private void toolbar() {

        float itemSize = 32;
        toolbarHeight = itemSize * 2.1f;

        ImGui.setNextWindowPos(0,menuBarSize);
        ImGui.setNextWindowSize(Gdx.graphics.getWidth(), toolbarHeight);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0);
        ImGui.begin("Toolbar##ToolbarWindow", ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse | ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.None | ImGuiWindowFlags.NoBringToFrontOnFocus);

        if (ImGui.imageButton(Statics.isGameRunning ? pauseIcon.getTextureObjectHandle() : playIcon.getTextureObjectHandle(), itemSize, itemSize, 0, 0, 1, 1, 10)) {
            if (!Statics.isGameRunning)
                engine.start();
            else
                engine.stop();
            Statics.isGameRunning = !Statics.isGameRunning;
        }

        ImGui.sameLine();

        ImGui.beginDisabled(editMode == 0);
        if (ImGui.imageButton(handIcon.getTextureObjectHandle(), itemSize, itemSize, 0, 0, 1, 1, 10)) Statics.editMode = 0;
        ImGui.endDisabled();
        ImGui.sameLine();
        ImGui.beginDisabled(editMode == 1);
        if (ImGui.imageButton(moveIcon.getTextureObjectHandle(), itemSize, itemSize, 0, 0, 1, 1, 10)) Statics.editMode = 1;
        ImGui.sameLine();
        ImGui.endDisabled();
        ImGui.beginDisabled(editMode == 2);
        if (ImGui.imageButton(expandIcon.getTextureObjectHandle(), itemSize, itemSize, 0, 0, 1, 1, 10)) Statics.editMode = 2;
        ImGui.sameLine();
        ImGui.endDisabled();
        ImGui.beginDisabled(editMode == 3);
        if (ImGui.imageButton(refreshIcon.getTextureObjectHandle(), itemSize, itemSize, 0, 0, 1, 1, 10)) Statics.editMode = 3;
        ImGui.endDisabled();

        isAnyWindowHovered |= ImGui.isWindowHovered() | ImGui.isAnyItemHovered();

        ImGui.end();

        ImGui.popStyleVar();

        if (Statics.editMode == 1 && selectedGameObject != null) {
            if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
                selectedGameObject.transform.position.set(Statics.mouse.x, Statics.mouse.y);
            }
        }

    }

    String searchBar = "";

    private void hierarchy() {
        ImGui.setNextWindowPos(Gdx.graphics.getWidth() / 2f + 150, Gdx.graphics.getHeight() / 2f, ImGuiCond.FirstUseEver);
        ImGui.setNextWindowSizeConstraints(300,150, 100000, 100000);
        ImGui.begin("Hierarchy##HierarchyWindow", ImGuiWindowFlags.None | ImGuiWindowFlags.NoBringToFrontOnFocus);

        ImGui.setNextItemWidth(ImGui.getContentRegionAvailX());
        searchBar = inputTextID("SearchBar", searchBar);

        ImGui.spacing();

        boolean isTreeNodeOpen = ImGui.treeNodeEx(currentProject.currentScene.substring(0,currentProject.currentScene.length() - 6) + " (" + currentProject.projectName + ")", ImGuiTreeNodeFlags.SpanFullWidth | ImGuiTreeNodeFlags.DefaultOpen);
        treeNodeContextMenu(currentProject.getCurrentScene().rootGameObject);
        if (ImGui.beginDragDropTarget()) {
            Object payload = ImGui.acceptDragDropPayload("GameObject");
            if (payload != null && payload.getClass() == GameObject.class) {
                GameObject g = (GameObject) payload;
                g.parent.children.remove(g);
                currentProject.getCurrentScene().rootGameObject.children.add(g);
                g.parent = currentProject.getCurrentScene().rootGameObject;
            }
            ImGui.endDragDropTarget();
        }
        ImGui.setCursorPosY(ImGui.getCursorPosY() - 3);
        ImGui.invisibleButton("##GameObject_" + currentProject.getCurrentScene().rootGameObject.ID + "_TARGET", ImGui.getContentRegionAvailX(), 6);
        ImGui.setCursorPosY(ImGui.getCursorPosY() - 3);
        if (ImGui.beginDragDropTarget()) {
            Object payload = ImGui.acceptDragDropPayload("GameObject");
            if (payload != null && payload.getClass() == GameObject.class) {
                GameObject g = (GameObject) payload;
                g.parent.children.remove(g);
                currentProject.getCurrentScene().rootGameObject.parent.children.add(currentProject.getCurrentScene().rootGameObject.parent.children.indexOf(currentProject.getCurrentScene().rootGameObject) + 1, g);
                g.parent = currentProject.getCurrentScene().rootGameObject.parent;
            }
            ImGui.endDragDropTarget();
        }

        if (isTreeNodeOpen) {
            drawTreeChildren(currentProject.getCurrentScene().rootGameObject, false);
            ImGui.treePop();
        }

        isAnyWindowHovered |= ImGui.isWindowHovered() | ImGui.isAnyItemHovered();

        ImGui.end();
    }

    private int fileHandleType = 0;
    private String fileHandlePath = "";

    private Object edit(Type type, String name, Object value) {
        ImGui.setNextItemWidth(250);
        if (type.equals(String.class)) {
            String str = value == null ? "" : value.toString();
            tempString.set(str);
            ImGui.inputText("##Val" + name, tempString);
            if (!tempString.get().equals(str)) Utils.captureState();
            return tempString.get();
        } else if (type.equals(Integer.class) || type.equals(int.class)) {
            tempInteger.set((int) value);
            ImGui.inputInt("##Val" + name, tempInteger);
            if (tempInteger.get() != (int) value) Utils.captureState();
            return tempInteger.get();
        } else if (type.equals(Float.class) || type.equals(float.class)) {
            tempFloat.set((float) value);
            ImGui.inputFloat("##Val" + name, tempFloat);
            if (!MathUtils.isEqual(tempFloat.get(), (float) value)) Utils.captureState();
            return tempFloat.get();
        } else if (type.equals(Double.class) || type.equals(double.class)) {
            tempDouble.set((double) value);
            ImGui.inputDouble("##Val" + name, tempDouble);
            if (tempDouble.get() != (double) value) Utils.captureState();
            return tempDouble.get();
        } else if (type.equals(Boolean.class) || type.equals(boolean.class)) {
            tempBoolean.set((boolean) value);
            ImGui.checkbox("##Val" + name, tempBoolean);
            if (tempBoolean.get() != (boolean) value) Utils.captureState();
            return tempBoolean.get();
        } else if (type.equals(Vector2.class)) {
            Vector2 vec2 = (Vector2) value;
            if (vec2 == null) vec2 = new Vector2();
            tempVec2[0] = vec2.x;
            tempVec2[1] = vec2.y;
            ImGui.inputFloat2("##Val" + name, tempVec2);
            if (tempVec2[0] != vec2.x || tempVec2[1] != vec2.y) {
                Utils.captureState();
                vec2 = new Vector2();
            }
            vec2.x = tempVec2[0];
            vec2.y = tempVec2[1];
            return vec2;
        } else if (type.equals(Vector3.class)) {
            Vector3 vec3 = (Vector3) value;
            if (vec3 == null) vec3 = new Vector3();
            tempVec3[0] = vec3.x;
            tempVec3[1] = vec3.y;
            tempVec3[2] = vec3.z;
            ImGui.inputFloat3("##Val" + name, tempVec3);
            if (ImGui.isItemDeactivatedAfterEdit()) Utils.captureState();
            if (tempVec3[0] != vec3.x || tempVec3[1] != vec3.y || tempVec3[2] != vec3.z) {
                Utils.captureState();
                vec3 = new Vector3();
            }
            vec3.x = tempVec3[0];
            vec3.y = tempVec3[1];
            vec3.z = tempVec3[2];
            return vec3;
        } else if (type.equals(Matrix3.class)) {
            Matrix3 mat = (Matrix3) value;
            if (mat == null) mat = new Matrix3();
            tempVec3[0] = mat.val[Matrix3.M00];
            tempVec3[1] = mat.val[Matrix3.M01];
            tempVec3[2] = mat.val[Matrix3.M02];
            ImGui.inputFloat3("##Val" + name + "0", tempVec3);
            boolean isDifferent = mat.val[Matrix3.M00] != tempVec3[0] || mat.val[Matrix3.M01] != tempVec3[1] || mat.val[Matrix3.M02] != tempVec3[2];
            mat.val[Matrix3.M00] = tempVec3[0];
            mat.val[Matrix3.M01] = tempVec3[1];
            mat.val[Matrix3.M02] = tempVec3[2];
            tempVec3[0] = mat.val[Matrix3.M10];
            tempVec3[1] = mat.val[Matrix3.M11];
            tempVec3[2] = mat.val[Matrix3.M12];
            ImGui.inputFloat3("##Val" + name + "1", tempVec3);
            isDifferent |= mat.val[Matrix3.M10] != tempVec3[0] || mat.val[Matrix3.M11] != tempVec3[1] || mat.val[Matrix3.M12] != tempVec3[2];
            mat.val[Matrix3.M10] = tempVec3[0];
            mat.val[Matrix3.M11] = tempVec3[1];
            mat.val[Matrix3.M12] = tempVec3[2];
            tempVec3[0] = mat.val[Matrix3.M20];
            tempVec3[1] = mat.val[Matrix3.M21];
            tempVec3[2] = mat.val[Matrix3.M22];
            ImGui.inputFloat3("##Val" + name + "2", tempVec3);
            mat.val[Matrix3.M20] = tempVec3[0];
            mat.val[Matrix3.M21] = tempVec3[1];
            mat.val[Matrix3.M22] = tempVec3[2];
            isDifferent |= mat.val[Matrix3.M20] != tempVec3[0] || mat.val[Matrix3.M21] != tempVec3[1] || mat.val[Matrix3.M22] != tempVec3[2];
            if (isDifferent) {
                Utils.captureState();
                mat = new Matrix3(mat.val);
            }
            return mat;
        } else if (type.equals(Matrix4.class)) {
            Matrix4 mat = (Matrix4) value;
            if (mat == null) mat = new Matrix4();
            tempColor[0] = mat.val[Matrix4.M00];
            tempColor[1] = mat.val[Matrix4.M01];
            tempColor[2] = mat.val[Matrix4.M02];
            tempColor[3] = mat.val[Matrix4.M03];
            ImGui.inputFloat4("##Val" + name + "0", tempColor);
            boolean isDifferent = mat.val[Matrix4.M00] != tempColor[0] || mat.val[Matrix4.M01] != tempColor[1] || mat.val[Matrix4.M02] != tempColor[2] || mat.val[Matrix4.M03] != tempColor[3];
            mat.val[Matrix4.M00] = tempColor[0];
            mat.val[Matrix4.M01] = tempColor[1];
            mat.val[Matrix4.M02] = tempColor[2];
            mat.val[Matrix4.M03] = tempColor[3];
            tempColor[0] = mat.val[Matrix4.M10];
            tempColor[1] = mat.val[Matrix4.M11];
            tempColor[2] = mat.val[Matrix4.M12];
            tempColor[3] = mat.val[Matrix4.M13];
            ImGui.inputFloat4("##Val" + name + "1", tempColor);
            mat.val[Matrix4.M10] = tempColor[0];
            mat.val[Matrix4.M11] = tempColor[1];
            mat.val[Matrix4.M12] = tempColor[2];
            mat.val[Matrix4.M13] = tempColor[3];
            isDifferent |= mat.val[Matrix4.M10] != tempColor[0] || mat.val[Matrix4.M11] != tempColor[1] || mat.val[Matrix4.M12] != tempColor[2] || mat.val[Matrix4.M13] != tempColor[3];
            tempColor[0] = mat.val[Matrix4.M20];
            tempColor[1] = mat.val[Matrix4.M21];
            tempColor[2] = mat.val[Matrix4.M22];
            tempColor[3] = mat.val[Matrix4.M23];
            ImGui.inputFloat4("##Val" + name + "2", tempColor);
            mat.val[Matrix4.M20] = tempColor[0];
            mat.val[Matrix4.M21] = tempColor[1];
            mat.val[Matrix4.M22] = tempColor[2];
            mat.val[Matrix4.M23] = tempColor[3];
            isDifferent |= mat.val[Matrix4.M20] != tempColor[0] || mat.val[Matrix4.M21] != tempColor[1] || mat.val[Matrix4.M22] != tempColor[2] || mat.val[Matrix4.M23] != tempColor[3];
            tempColor[0] = mat.val[Matrix4.M30];
            tempColor[1] = mat.val[Matrix4.M31];
            tempColor[2] = mat.val[Matrix4.M32];
            tempColor[3] = mat.val[Matrix4.M33];
            ImGui.inputFloat4("##Val" + name + "3", tempColor);
            mat.val[Matrix4.M30] = tempColor[0];
            mat.val[Matrix4.M31] = tempColor[1];
            mat.val[Matrix4.M32] = tempColor[2];
            mat.val[Matrix4.M33] = tempColor[3];
            isDifferent |= mat.val[Matrix4.M30] != tempColor[0] || mat.val[Matrix4.M31] != tempColor[1] || mat.val[Matrix4.M32] != tempColor[2] || mat.val[Matrix4.M33] != tempColor[3];
            if (isDifferent) {
                Utils.captureState();
                mat = new Matrix4(mat.val);
            }
            return mat;
        } else if (type.equals(Color.class)) {
            Color color = (Color) value;
            if (color == null) color = new Color();
            tempColor[0] = color.r;
            tempColor[1] = color.g;
            tempColor[2] = color.b;
            tempColor[3] = color.a;
            ImGui.colorEdit4("##Val" + name, tempColor);
            if (tempColor[0] != color.r || tempColor[1] != color.g || tempColor[2] != color.b || tempColor[3] != color.a) {
                Utils.captureState();
                color = new Color();
            }
            color.r = tempColor[0];
            color.g = tempColor[1];
            color.b = tempColor[2];
            color.a = tempColor[3];
            return color;
        } else if (type.equals(Material.class)) {
            Material newMaterial = (Material) value;
            String name0 = "Default";
            for (String key : engine.loadedMaterials.keySet()) {
                Material mat = engine.loadedMaterials.get(key);
                if (mat == newMaterial) name0 = key;
            }
            if (ImGui.button("Select (" + name0 + ")##Sel" + name)) {
                ImGui.openPopup("MaterialSelector" + name);
            }
            if (ImGui.beginPopup("MaterialSelector" + name)) {
                if (ImGui.selectable("Default")) {
                    newMaterial = null;
                    ImGui.closeCurrentPopup();
                }
                for (String key : engine.loadedMaterials.keySet()) {
                    Material mat = engine.loadedMaterials.get(key);
                    if (ImGui.selectable(mat.materialFile)) {
                        newMaterial = mat;
                        ImGui.closeCurrentPopup();
                    }
                }
                isAnyWindowHovered |= ImGui.isWindowHovered();
                ImGui.endPopup();
                if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                    ImGui.closeCurrentPopup();
                }
            }
            if (ImGui.beginDragDropTarget()) {
                MaterialPayload payload = ImGui.acceptDragDropPayload("Material", MaterialPayload.class);
                if (payload != null) {
                    newMaterial = payload.material;
                }
                ImGui.endDragDropTarget();
            }
            if (newMaterial != value) Utils.captureState();
            return newMaterial;
        } else if (type.equals(TextureAsset.class)) {
            TextureAsset newTextureAsset = (TextureAsset) value;
            String name0 = "Default";
            for (String key : engine.loadedTextures.keySet()) {
                TextureAsset tex = engine.loadedTextures.get(key);
                if (newTextureAsset != null && tex.getPath().equals(newTextureAsset.getPath())) name0 = key;
            }
            if (ImGui.beginPopup("TextureSelector")) {
                if (ImGui.button("Default", 100 + ImGui.getStyle().getFramePaddingX() * 2, 100 + ImGui.getStyle().getFramePaddingY() * 2)) {
                    newTextureAsset = null;
                    ImGui.closeCurrentPopup();
                }
                for (String key : engine.loadedTextures.keySet()) {
                    TextureAsset tex = engine.loadedTextures.get(key);
                    if (ImGui.imageButton(tex.getGdxTexture().getTextureObjectHandle(), 100, 100)) {
                        newTextureAsset = tex;
                        ImGui.closeCurrentPopup();
                    }
                }
                isAnyWindowHovered |= ImGui.isWindowHovered();
                ImGui.endPopup();
                if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                    ImGui.closeCurrentPopup();
                }
            }
            if (ImGui.button("Select (" + name0 + ")##Sel" + name)) {
                ImGui.openPopup("TextureSelector");
            }
            if (ImGui.beginDragDropTarget()) {
                TexturePayload payload = ImGui.acceptDragDropPayload("Texture", TexturePayload.class);
                if (payload != null) {
                    newTextureAsset = payload.textureAsset;
                }
                ImGui.endDragDropTarget();
            }
            if (newTextureAsset != value) Utils.captureState();
            return newTextureAsset;
        } else if (type.equals(Texture.class)) {
            Texture texture = (Texture) value;

            TextureAsset textureAsset = (TextureAsset) edit(TextureAsset.class, "texture", texture.textureAsset);
            Vector2 uv0 = (Vector2) edit(Vector2.class, "uv0", texture.uv0);
            if (ImGui.isItemDeactivatedAfterEdit()) Utils.captureState();
            Vector2 uv1 = (Vector2) edit(Vector2.class, "uv1", texture.uv1);
            if (ImGui.isItemDeactivatedAfterEdit()) Utils.captureState();

            boolean a = textureAsset != null && !textureAsset.equals(texture.textureAsset);
            boolean b = texture.textureAsset != null && !texture.textureAsset.equals(textureAsset);

            if (a || b || !texture.uv0.equals(uv0) || !texture.uv1.equals(uv1)) {
                texture = new Texture(textureAsset);
                texture.uv0 = uv0;
                texture.uv1 = uv1;
            }

            if (texture != value) Utils.captureState();
            return texture;
        } else if (((Class<?>) type).isEnum()) {
            Enum<?> e = (Enum<?>) value;
            Object[] values = ((Class<?>) type).getEnumConstants();
            if (ImGui.beginCombo("##" + ((Class<?>) type).getSimpleName() + "Combo" + name, e.name())) {
                for (Object i : values) {
                    Enum<?> item = (Enum<?>) i;
                    boolean selected = item.equals(e);
                    if (ImGui.selectable(item.name())) {
                        e = item;
                    }
                    if (selected) ImGui.setItemDefaultFocus();
                }
                ImGui.endCombo();
            }
            if (e != value) Utils.captureState();
            return e;
        } else if (type.equals(AudioClip.class)) {
            AudioClip newAudioClip = (AudioClip) value;
            String name0 = "None";
            for (String key : engine.loadedAudioClips.keySet()) {
                AudioClip audioClip = engine.loadedAudioClips.get(key);
                if (newAudioClip != null && audioClip.soundFile.equals(newAudioClip.soundFile)) {
                    name0 = key;
                    break;
                }
            }
            ImGui.setNextWindowSizeConstraints(250,20,250,100000);
            if (ImGui.beginPopup("AudioClipSelector")) {
                if (ImGui.selectable("None")) {
                    newAudioClip = null;
                    ImGui.closeCurrentPopup();
                }
                for (String key : engine.loadedAudioClips.keySet()) {
                    AudioClip clip = engine.loadedAudioClips.get(key);
                    if (ImGui.selectable(clip.soundFile)) {
                        newAudioClip = clip;
                        ImGui.closeCurrentPopup();
                    }
                }
                isAnyWindowHovered |= ImGui.isWindowHovered();
                ImGui.endPopup();
                if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                    ImGui.closeCurrentPopup();
                }
            }
            if (ImGui.button("Select (" + name0 + ")##Sel" + name)) {
                ImGui.openPopup("AudioClipSelector");
            }
            if (ImGui.beginDragDropTarget()) {
                AudioClipPayload payload = ImGui.acceptDragDropPayload("AudioClip", AudioClipPayload.class);
                if (payload != null) {
                    newAudioClip = payload.audioClip;
                }
                ImGui.endDragDropTarget();
            }
            if (newAudioClip != value) Utils.captureState();
            return newAudioClip;
        } else if (type.equals(GameObject.class)) {
            GameObject newGameObject = (GameObject) value;
            String name0 = "None";
            for (GameObject go : Statics.allGameObjects.get(currentProject.currentScene)) {
                if (go == newGameObject) name0 = go.name;
            }
            if (ImGui.beginPopup("GameObjectSelector")) {
                if (ImGui.selectable("None")) {
                    newGameObject = null;
                    ImGui.closeCurrentPopup();
                }
                for (GameObject go : Statics.allGameObjects.get(currentProject.currentScene)) {
                    if (go != selectedGameObject && ImGui.selectable(go.name)) {
                        newGameObject = go;
                        ImGui.closeCurrentPopup();
                    }
                }
                isAnyWindowHovered |= ImGui.isWindowHovered();
                ImGui.endPopup();
                if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                    ImGui.closeCurrentPopup();
                }
            }
            if (ImGui.button("Select (" + name0 + ")##Sel" + name)) {
                ImGui.openPopup("GameObjectSelector");
            }
            if (ImGui.beginDragDropTarget()) {
                GameObjectPayload payload = ImGui.acceptDragDropPayload("GameObject", GameObjectPayload.class);
                if (payload != null) {
                    newGameObject = payload.gameObject;
                }
                ImGui.endDragDropTarget();
            }
            if (newGameObject != value) Utils.captureState();
            return newGameObject;
        } else if (Component.class.isAssignableFrom((Class<?>) type)) {
            Component newComponent = (Component) value;
            String name0 = "None";
            for (GameObject go : Statics.allGameObjects.get(currentProject.currentScene)) {
                if (go.hasComponent((Class<? extends Component>) type)) {
                    Component comp = (Component) go.getComponent((Class<?>) type);
                    if (comp == newComponent) name0 = go.name;
                }
            }

            if (ImGui.beginPopup("ComponentSelector")) {
                if (ImGui.selectable("None")) {
                    newComponent = null;
                    ImGui.closeCurrentPopup();
                }
                for (GameObject go : Statics.allGameObjects.get(currentProject.currentScene)) {
                    if (go.hasComponent((Class<? extends Component>) type)) {
                        if (go != selectedGameObject && ImGui.selectable(go.name)) {
                            newComponent = (Component) go.getComponent((Class<?>) type);
                            ImGui.closeCurrentPopup();
                        }
                    }
                }
                isAnyWindowHovered |= ImGui.isWindowHovered();
                ImGui.endPopup();
                if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                    ImGui.closeCurrentPopup();
                }
            }
            if (ImGui.button("Select (" + name0 + ")##Sel" + name)) {
                ImGui.openPopup("ComponentSelector");
            }
            if (ImGui.beginDragDropTarget()) {
                GameObjectPayload payload = ImGui.acceptDragDropPayload("GameObject", GameObjectPayload.class);
                if (payload != null && payload.gameObject.hasComponent((Class<? extends Component>) type)) {
                    newComponent = (Component) payload.gameObject.getComponent((Class<?>) type);
                }
                ImGui.endDragDropTarget();
            }
            if (newComponent != value) Utils.captureState();
            return newComponent;
        } else if (type.equals(com.pulsar.api.FileHandle.class)) {
            com.pulsar.api.FileHandle newFileHandle = (com.pulsar.api.FileHandle) value;
            boolean isNull = newFileHandle.path() == null;
            if (ImGui.beginPopup("FileHandleSelector")) {
                if (ImGui.radioButton("Internal", fileHandleType == 0)) fileHandleType = 0;
                ImGui.sameLine();
                if (ImGui.radioButton("Absolute", fileHandleType == 1)) fileHandleType = 1;
                ImGui.sameLine();
                if (ImGui.radioButton("External", fileHandleType == 2)) fileHandleType = 2;
                ImGui.sameLine();
                if (ImGui.radioButton("Local", fileHandleType == 3)) fileHandleType = 3;
                imString.set(fileHandlePath);
                ImGui.inputText("##FileHandlePopupPath" + name, imString);
                fileHandlePath = imString.get();
                if (ImGui.button("Apply")) {
                    if (fileHandleType == 0) newFileHandle.setInternal(fileHandlePath);
                    if (fileHandleType == 1) newFileHandle.setAbsolute(fileHandlePath);
                    if (fileHandleType == 2) newFileHandle.setExternal(fileHandlePath);
                    if (fileHandleType == 3) newFileHandle.setLocal(fileHandlePath);
                }
                ImGui.endPopup();
            }
            if (ImGui.button((isNull ? "" : "(" + newFileHandle.fileType() + ") ") + (isNull ? "None" : newFileHandle.path()) + "##Sel" + name)) {
                fileHandleType = 0;
                fileHandlePath = "";
                if (!isNull) {
                    fileHandlePath = newFileHandle.path();
                    if (newFileHandle.fileType().equals("Internal")) fileHandleType = 0;
                    if (newFileHandle.fileType().equals("Absolute")) fileHandleType = 1;
                    if (newFileHandle.fileType().equals("External")) fileHandleType = 2;
                    if (newFileHandle.fileType().equals("Local")) fileHandleType = 3;
                }
                ImGui.openPopup("FileHandleSelector");
            }
            if (ImGui.beginDragDropTarget()) {
                FileHandlePayload payload = ImGui.acceptDragDropPayload("FileHandle", FileHandlePayload.class);
                if (payload != null)
                    newFileHandle = payload.fileHandle;
                ImGui.endDragDropTarget();
            }
            if (newFileHandle != value) Utils.captureState();
            return newFileHandle;
        } else if (type.equals(Collider.PhysicsFilter.class)) {
            Collider.PhysicsFilter filter = (Collider.PhysicsFilter) value;
            tempInteger.set(filter.categoryBits);
            ImGui.inputInt("Category Bits##PhysicsFilterCategory", tempInteger);
            if (filter.categoryBits != (short) tempInteger.get()) Utils.captureState();
            filter.categoryBits = (short) tempInteger.get();
            tempInteger.set(filter.maskBits);
            ImGui.inputInt("Mask Bits##PhysicsFilterMask", tempInteger);
            if (filter.maskBits != (short) tempInteger.get()) Utils.captureState();
            filter.maskBits = (short) tempInteger.get();
            return filter;
        }
        return null;
    }

    Stack<Boolean> treeStack = new Stack<>();

    private void editField(String name, ParticleEffect object, String label) {
        ImGui.text(label + ":");
        ImGui.sameLine();
        ImGui.indent(160);
        try {
            Field f = ParticleEffect.class.getField(name);
            Type type = f.getGenericType();
            Class<?> typeC = f.getType();
            if (List.class.isAssignableFrom(typeC)) {
                List<Object> list = (List<Object>) f.get(object);
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Type componentType = parameterizedType.getActualTypeArguments()[0];
                Class<?> componentTypeC = (Class<?>) componentType;
                ImGui.text("");
                int remove = -1;
                for (int j = 0; j < list.size(); j++) {
                    Object o = list.get(j);
                    ImGui.text(Integer.toString(j));
                    ImGui.sameLine();
                    if (ImGui.button("Remove##ListRemoveButton" + f.getName() + j)) remove = j;
                    list.set(j, edit(componentType, f.getName() + j, componentTypeC.cast(o)));
                }
                if (remove != -1) list.remove(remove);
                ImGui.spacing();
                if (ImGui.button("+##ListAddButton" + f.getName())) {
                    list.add(componentTypeC.getDeclaredConstructor().newInstance());
                }
            } else if (typeC == Curve.class) {
                Curve curve = (Curve) f.get(object);
                curveEditor(name + "curve", curve, 200, 150);
            } else if (typeC == ColorRange.class) {
                ColorRange colorRange = (ColorRange) f.get(object);
                colorRangeEditor(name + "color_range", colorRange, 200, 35);
            } else {
                f.set(object, edit(f.getType(), name, f.get(object)));
            }
        } catch (NoSuchFieldException | IllegalAccessException | InvocationTargetException |
                 InstantiationException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        ImGui.unindent(160);
    }

    Class<?>[] componentsMainSection = new Class<?>[] {
        Camera.class,
        SpriteRenderer.class,
        ParticleEmitter.class,
        Text.class,
    };

    Class<?>[] componentsPhysicsSection = new Class<?>[] {
        RigidBody.class,
        BoxCollider.class,
        CircleCollider.class,
        PolygonCollider.class,
        DistanceJoint.class,
        PrismaticJoint.class,
        RevoluteJoint.class,
        RopeJoint.class,
        WeldJoint.class,
        WheelJoint.class,
    };

    Class<?>[] componentsAudioSection = new Class<?>[] {
        AudioSource.class,
        AudioListener.class,
    };

    private void inspector() {

        ImGui.setNextWindowPos(Gdx.graphics.getWidth() / 2f + 100, Gdx.graphics.getHeight() / 2f, ImGuiCond.FirstUseEver);
        ImGui.setNextWindowSize(300,150, ImGuiCond.FirstUseEver);
        ImGui.begin("Inspector##InspectorWindow", ImGuiWindowFlags.None | ImGuiWindowFlags.NoBringToFrontOnFocus);

        if (selectedGameObject != null) {
            selectedGameObject.name = inputTextID("gameObjectName", selectedGameObject.name);

            ImGui.separator();

            ImGui.indent();

            boolean close = Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);

            treeStack.clear();

            for (int i = 0; i < selectedGameObject.components.size(); i++) {
                Component component = selectedGameObject.components.get(i);

                ImGui.alignTextToFramePadding();

                boolean nodeOpen = ImGui.treeNodeEx(component.getClass().getSimpleName(), ImGuiTreeNodeFlags.SpanFullWidth | ImGuiTreeNodeFlags.DefaultOpen);

                if (ImGui.beginPopupContextItem(component.getClass().getSimpleName() + "Menu")) {
                    if (ImGui.selectable("Remove Component")) {
                        Utils.captureState();
                        selectedGameObject.removeComponent(component);
                    }
                    isAnyWindowHovered |= ImGui.isWindowHovered();
                    ImGui.endPopup();
                }

                if (nodeOpen) {
                    ImGui.indent();
                    for (int j = 0; j < component.fields.size; j++) {
                        com.badlogic.gdx.utils.reflect.Field field = component.fields.get(j);
                        if (field.isStatic()) continue;

                        if (field.isAnnotationPresent(TreeStart.class)) {
                            TreeStart treeStart = field.getDeclaredAnnotation(TreeStart.class).getAnnotation(TreeStart.class);
                            boolean v = treeStart.isHeader() ? ImGui.collapsingHeader(treeStart.label()) : ImGui.treeNode(treeStart.label());
                            treeStack.push(v);
                        }
                        if (treeStack.isEmpty() || treeStack.peek()) {
                            try {
                                ImGui.text(styleName(field.getName()));
                                ImGui.sameLine();
                                ImGui.indent(90);
                                Type type = field.getType();
                                Class<?> typeC = field.getType();
                                if (List.class.isAssignableFrom(typeC)) {
                                    List<Object> list = (List<Object>) field.get(component);
                                    ParameterizedType parameterizedType = (ParameterizedType) type;
                                    Type componentType = parameterizedType.getActualTypeArguments()[0];
                                    Class<?> componentTypeC = (Class<?>) componentType;
                                    ImGui.text("");
                                    int remove = -1;
                                    for (int k = 0; k < list.size(); k++) {
                                        Object o = list.get(k);
                                        ImGui.text(Integer.toString(k));
                                        ImGui.sameLine();
                                        if (ImGui.button("Remove##ListRemoveButton" + field.getName() + k)) remove = k;
                                        list.set(k, edit(componentType, field.getName() + k, componentTypeC.cast(o)));
                                        if (ImGui.isItemDeactivatedAfterEdit()) Utils.captureState();
                                    }
                                    if (remove != -1) list.remove(remove);
                                    ImGui.spacing();
                                    if (ImGui.button("+##ListAddButton")) {
                                        list.add(componentTypeC.getDeclaredConstructor().newInstance());
                                    }
                                }

                                if (type.equals(Button.class)) {
                                    Button button = (Button) field.get(component);
                                    if (ImGui.button(button.text + "##" + field.getName()) && button.listener != null) {
                                        button.listener.click();
                                    }
                                } else if (type.equals(Curve.class)) {
                                    Curve curve = (Curve) field.get(component);
                                    if (curve == null) curve = new Curve();
                                    curveEditor(field.getName() + "Curve", curve, 200, 150);
                                } else if (type.equals(ColorRange.class)) {
                                    ColorRange colorRange = (ColorRange) field.get(component);
                                    if (colorRange == null) colorRange = new ColorRange();
                                    colorRangeEditor(field.getName() + "Color_Range", colorRange, 200, 35);
                                } else if (type.equals(Label.class)) {
                                    Label label = (Label) field.get(component);
                                    ImGui.text(label.text);
                                } else if (type.equals(ParticleEffect.class)) {
                                    ParticleEffect particleEffect = (ParticleEffect) field.get(component);
                                    ImGui.text("");
                                    ImGui.unindent(90);
                                    editField("name", particleEffect, "Name");
                                    if (ImGui.collapsingHeader("Sprites")) {
                                        editField("sprites", particleEffect, "Sprites");
                                        editField("optionsSpriteMode", particleEffect, "Sprite Mode");
                                    }
                                    if (ImGui.collapsingHeader("Delay")) {
                                        editField("delayActive", particleEffect, "Active");
                                        editField("delayLowMin", particleEffect, "Low Min");
                                        editField("delayLowMax", particleEffect, "Low Max");
                                    }
                                    if (ImGui.collapsingHeader("Duration")) {
                                        editField("durationLowMin", particleEffect, "Low Min");
                                        editField("durationLowMax", particleEffect, "Low Max");
                                    }
                                    if (ImGui.collapsingHeader("Count")) {
                                        editField("countMin", particleEffect, "Min");
                                        editField("countMax", particleEffect, "Max");
                                    }
                                    if (ImGui.collapsingHeader("Emission")) {
                                        editField("emissionActive", particleEffect, "Active");
                                        editField("emissionLowMin", particleEffect, "Low Min");
                                        editField("emissionLowMax", particleEffect, "Low Max");
                                        editField("emissionHighMin", particleEffect, "High Min");
                                        editField("emissionHighMax", particleEffect, "High Max");
                                        editField("emission", particleEffect, "Curve");
                                    }
                                    if (ImGui.collapsingHeader("Life")) {
                                        editField("lifeLowMin", particleEffect, "Low Min");
                                        editField("lifeLowMax", particleEffect, "Low Max");
                                        editField("lifeHighMin", particleEffect, "High Min");
                                        editField("lifeHighMax", particleEffect, "High Max");
                                        editField("life", particleEffect, "Curve");
                                    }
                                    if (ImGui.collapsingHeader("Life Offset")) {
                                        editField("lifeOffsetActive", particleEffect, "Active");
                                        editField("lifeOffsetLowMin", particleEffect, "Low Min");
                                        editField("lifeOffsetLowMax", particleEffect, "Low Max");
                                        editField("lifeOffsetHighMin", particleEffect, "High Min");
                                        editField("lifeOffsetHighMax", particleEffect, "High Max");
                                        editField("lifeOffset", particleEffect, "Curve");
                                    }
                                    if (ImGui.collapsingHeader("Offset")) {
                                        ImGui.text("X");
                                        ImGui.spacing();
                                        editField("xOffsetLowMin", particleEffect, "Low Min");
                                        editField("xOffsetLowMax", particleEffect, "Low Max");

                                        ImGui.spacing();
                                        ImGui.spacing();

                                        ImGui.text("Y");
                                        ImGui.spacing();
                                        editField("yOffsetLowMin", particleEffect, "Low Min");
                                        editField("yOffsetLowMax", particleEffect, "Low Max");
                                    }
                                    if (ImGui.collapsingHeader("Spawn")) {

                                        editField("spawnShape", particleEffect, "Shape");

                                        if (particleEffect.spawnShape == ParticleEffect.SpawnShape.ELLIPSE) {
                                            editField("spawnEdges", particleEffect, "Edges");
                                            ImGui.beginDisabled(!particleEffect.spawnEdges);
                                            editField("spawnEllipseSide", particleEffect, "Side");
                                            ImGui.endDisabled();
                                        }

                                        if (particleEffect.spawnShape != ParticleEffect.SpawnShape.POINT) {
                                            ImGui.text("Width");
                                            ImGui.spacing();
                                            editField("spawnWidthLowMin", particleEffect, "Low Min");
                                            editField("spawnWidthLowMax", particleEffect, "Low Max");
                                            editField("spawnWidthHighMin", particleEffect, "High Min");
                                            editField("spawnWidthHighMax", particleEffect, "High Max");
                                            editField("spawnWidth", particleEffect, "Curve");

                                            ImGui.spacing();
                                            ImGui.spacing();

                                            ImGui.text("Height");
                                            ImGui.spacing();
                                            editField("spawnHeightLowMin", particleEffect, "Low Min");
                                            editField("spawnHeightLowMax", particleEffect, "Low Max");
                                            editField("spawnHeightHighMin", particleEffect, "High Min");
                                            editField("spawnHeightHighMax", particleEffect, "High Max");
                                            editField("spawnHeight", particleEffect, "Curve");
                                        }
                                    }
                                    if (ImGui.collapsingHeader("Scale")) {
                                        ImGui.text("X");
                                        ImGui.spacing();
                                        editField("xScaleLowMin", particleEffect, "Low Min");
                                        editField("xScaleLowMax", particleEffect, "Low Max");
                                        editField("xScaleHighMin", particleEffect, "High Min");
                                        editField("xScaleHighMax", particleEffect, "High Max");
                                        editField("xScale", particleEffect, "Curve");

                                        ImGui.spacing();
                                        ImGui.spacing();

                                        ImGui.text("Y");
                                        ImGui.spacing();
                                        editField("yScaleLowMin", particleEffect, "Low Min");
                                        editField("yScaleLowMax", particleEffect, "Low Max");
                                        editField("yScaleHighMin", particleEffect, "High Min");
                                        editField("yScaleHighMax", particleEffect, "High Max");
                                        editField("yScale", particleEffect, "Curve");
                                    }
                                    if (ImGui.collapsingHeader("Velocity")) {
                                        editField("velocityActive", particleEffect, "Active");
                                        editField("velocityLowMin", particleEffect, "Low Min");
                                        editField("velocityLowMax", particleEffect, "Low Max");
                                        editField("velocityHighMin", particleEffect, "High Min");
                                        editField("velocityHighMax", particleEffect, "High Max");
                                        editField("velocity", particleEffect, "Curve");
                                    }
                                    if (ImGui.collapsingHeader("Angle")) {
                                        editField("angleActive", particleEffect, "Active");
                                        editField("angleLowMin", particleEffect, "Low Min");
                                        editField("angleLowMax", particleEffect, "Low Max");
                                        editField("angleHighMin", particleEffect, "High Min");
                                        editField("angleHighMax", particleEffect, "High Max");
                                        editField("angle", particleEffect, "Curve");
                                    }
                                    if (ImGui.collapsingHeader("Rotation")) {
                                        editField("rotationActive", particleEffect, "Active");
                                        editField("rotationLowMin", particleEffect, "Low Min");
                                        editField("rotationLowMax", particleEffect, "Low Max");
                                        editField("rotationHighMin", particleEffect, "High Min");
                                        editField("rotationHighMax", particleEffect, "High Max");
                                        editField("rotation", particleEffect, "Curve");
                                    }
                                    if (ImGui.collapsingHeader("Wind")) {
                                        editField("windActive", particleEffect, "Active");
                                        editField("windLowMin", particleEffect, "Low Min");
                                        editField("windLowMax", particleEffect, "Low Max");
                                        editField("windHighMin", particleEffect, "High Min");
                                        editField("windHighMax", particleEffect, "High Max");
                                        editField("wind", particleEffect, "Curve");
                                    }
                                    if (ImGui.collapsingHeader("Gravity")) {
                                        editField("gravityActive", particleEffect, "Active");
                                        editField("gravityLowMin", particleEffect, "Low Min");
                                        editField("gravityLowMax", particleEffect, "Low Max");
                                        editField("gravityHighMin", particleEffect, "High Min");
                                        editField("gravityHighMax", particleEffect, "High Max");
                                        editField("gravity", particleEffect, "Curve");
                                    }
                                    if (ImGui.collapsingHeader("Tint")) {
                                        editField("tintColors", particleEffect, "Tint");
                                    }
                                    if (ImGui.collapsingHeader("Transparency")) {
                                        editField("transparency", particleEffect, "Curve");
                                    }
                                    if (ImGui.collapsingHeader("Options")) {
                                        editField("optionsAttached", particleEffect, "Attached");
                                        editField("optionsContinuous", particleEffect, "Continuous");
                                        editField("optionsAligned", particleEffect, "Aligned");
                                        editField("optionsAdditive", particleEffect, "Additive");
                                        editField("optionsBehind", particleEffect, "Behind");
                                        editField("optionsPremultipliedAlpha", particleEffect, "Premultiplied Alpha");
                                    }
                                    field.set(component, particleEffect);
                                    ImGui.indent(90);
                                } else {
                                    field.set(component, edit(type, field.getName(), field.get(component)));
                                }
                                ImGui.unindent(90);
                            } catch (IllegalAccessException | InvocationTargetException | InstantiationException |
                                     NoSuchMethodException e) {
                                throw new RuntimeException(e);
                            } catch (ReflectionException e) {
                                throw new RuntimeException(e);
                            }
                            if (field.isAnnotationPresent(TreeEnd.class) && !field.getDeclaredAnnotation(TreeEnd.class).getAnnotation(TreeStart.class).isHeader()) ImGui.treePop();
                        }
                        if (field.isAnnotationPresent(TreeEnd.class)) treeStack.pop();
                    }
                    ImGui.unindent();
                    ImGui.separator();
                    ImGui.treePop();
                }
            }

            ImGui.spacing();

            ImGui.unindent();

            if (ImGui.button("Add Component")) {
                ImGui.openPopup("AddComponentPopup");
            }

            float wh = ImGui.getContentRegionAvailX();
            float ht = ImGui.getContentRegionAvailY();

            ImGui.invisibleButton("AddComponentTargetBtn", wh, ht);

            if (ImGui.beginDragDropTarget()) {

                String componentName = ImGui.acceptDragDropPayload("Component", String.class);

                for (Class<? extends Component> component : Statics.currentProject.components) {
                    if (component.getName().equals(componentName)) {
                        selectedGameObject.addComponent(Utils.getComponent(componentName));
                        break;
                    }
                }

                ImGui.endDragDropTarget();
            }

            ImGui.setNextWindowSize(240,360);
            if (ImGui.beginPopup("AddComponentPopup")) {

                separatorCenter("Scripts");

                for (Class<?> component : currentProject.components) {
                    if (component.getModule() != Transform.class.getModule() &&
                        ImGui.selectable(component.getName(), false, 0, ImGui.getContentRegionAvailX(), 16)) {
                        Utils.captureState();
                        selectedGameObject.addComponent(Utils.getComponent(component.getName()));
                        close = true;
                    }
                }

                separatorCenter("Main");

                for (Class<?> component : componentsMainSection) {
                    if (ImGui.selectable(component.getSimpleName(), false, 0, ImGui.getContentRegionAvailX(), 16)) {
                        Utils.captureState();
                        try {
                            selectedGameObject.addComponent((Component) component.getDeclaredConstructor().newInstance());
                        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                                 NoSuchMethodException e) {
                            throw new RuntimeException(e);
                        }
                        close = true;
                    }
                }

                separatorCenter("Physics");

                for (Class<?> component : componentsPhysicsSection) {
                    if (ImGui.selectable(component.getSimpleName(), false, 0, ImGui.getContentRegionAvailX(), 16)) {
                        Utils.captureState();
                        try {
                            selectedGameObject.addComponent((Component) component.getDeclaredConstructor().newInstance());
                        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                                 NoSuchMethodException e) {
                            throw new RuntimeException(e);
                        }
                        close = true;
                    }
                }

                separatorCenter("Audio");

                for (Class<?> component : componentsAudioSection) {
                    if (ImGui.selectable(component.getSimpleName(), false, 0, ImGui.getContentRegionAvailX(), 16)) {
                        Utils.captureState();
                        try {
                            selectedGameObject.addComponent((Component) component.getDeclaredConstructor().newInstance());
                        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                                 NoSuchMethodException e) {
                            throw new RuntimeException(e);
                        }
                        close = true;
                    }
                }

                isAnyWindowHovered |= ImGui.isWindowHovered() | ImGui.isAnyItemHovered();
                ImGui.endPopup();

            }

            if (close) ImGui.closeCurrentPopup();

        } else if (selection != null) {
            Class<?> cls = selection.getClass();
            if (cls == AudioClip.class) {
                AudioClip selectedAudioClip = (AudioClip) selection;

                if (selectedAudioSource == null) selectedAudioSource = selectedAudioClip.getSource();

                ImGui.text("Audio: " + selectedAudioClip.soundFile);
                ImGui.text("Duration");
                ImGui.sameLine();
                ImGui.text(MathUtils.round(selectedAudioSource.getDuration() * 100f) / 100f + "s");

                ImGui.separator();

                audioSourcePosition[0] = selectedAudioSource.getPlaybackPosition();

                ImGui.beginDisabled();
                ImGui.sliderFloat("Playback Position", audioSourcePosition, 0, selectedAudioSource.getDuration());
                ImGui.endDisabled();

                if (ImGui.button(selectedAudioSource.isPlaying() ? "Stop" : "Play")) {
                    if (selectedAudioSource.isPlaying())
                        selectedAudioSource.stop();
                    else
                        selectedAudioSource.play();
                }

                ImGui.beginDisabled(!selectedAudioSource.isPlaying());
                if (ImGui.button("Pause")) {
                    selectedAudioSource.pause();
                }
                ImGui.endDisabled();
            } else if (cls == Shader.class) {
                Shader selectedShader = (Shader) selection;

                ImGui.text("Shader: " + selectedShader.shaderFile);

            } else if (cls == Material.class) {
                Material selectedMaterial = (Material) selection;

                ImGui.text("Material: " + selectedMaterial.materialFile);

                ImGui.spacing();
                ImGui.separator();

                ImGui.text("Shader:");
                ImGui.sameLine();

                String name = "None";
                if (selectedMaterial.shaderFile != null) name = selectedMaterial.shaderFile;
                if (ImGui.button("Select (" + name + ")##Sel" + name)) {
                    ImGui.openPopup("ShaderSelectPopup");
                }

                for (ShaderLanguage.Variable variable : selectedMaterial.variableSet) {
                    Object obj = selectedMaterial.variableValueMap.get(variable.name);

                    if (!variable.hidden) {
                        if (obj != null) {
                            if (variable.type == ShaderLanguage.VariableType.INT) {
                                Integer i = (Integer) obj;
                                tempInteger.set(i);
                                ImGui.inputInt(styleName(variable.name), tempInteger);
                                selectedMaterial.variableValueMap.put(variable.name, tempInteger.get());
                            } else if (variable.type == ShaderLanguage.VariableType.FLOAT) {
                                if (!variable.array) {
                                    Float f = (Float) obj;
                                    tempFloat.set(f);
                                    ImGui.inputFloat(styleName(variable.name), tempFloat);
                                    selectedMaterial.variableValueMap.put(variable.name, tempFloat.get());
                                } else {
                                    float[] arr = (float[]) obj;
                                    ImGui.text(styleName(variable.name));
                                    ImGui.indent();
                                    for (int i = 0; i < arr.length; i++) {
                                        tempFloat.set(arr[i]);
                                        ImGui.inputFloat("##Var" + variable.name + i, tempFloat);
                                        arr[i] = tempFloat.get();
                                    }
                                    if (ImGui.smallButton("+##ArrayAppend" + variable.name))
                                        arr = Arrays.copyOf(arr, arr.length + 1);
                                    ImGui.unindent();
                                    selectedMaterial.variableValueMap.put(variable.name, arr);
                                }
                            } else if (variable.type == ShaderLanguage.VariableType.VECTOR2) {
                                if (!variable.array) {
                                    Vector2 vec = (Vector2) obj;
                                    tempVec2[0] = vec.x;
                                    tempVec2[1] = vec.y;
                                    ImGui.inputFloat2(styleName(variable.name), tempVec2);
                                    vec.set(tempVec2[0], tempVec2[1]);
                                    selectedMaterial.variableValueMap.put(variable.name, vec);
                                } else {
                                    float[] arr = (float[]) obj;
                                    ImGui.text(styleName(variable.name));
                                    ImGui.indent();
                                    for (int i = 0; i < arr.length; i += 2) {
                                        tempVec2[0] = arr[i];
                                        tempVec2[1] = arr[i + 1];
                                        ImGui.inputFloat3("##Var" + variable.name + i, tempVec3);
                                        arr[i] = tempVec2[0];
                                        arr[i + 1] = tempVec2[1];
                                    }
                                    if (ImGui.smallButton("+##ArrayAppend" + variable.name))
                                        arr = Arrays.copyOf(arr, arr.length + 2);
                                    ImGui.unindent();
                                    selectedMaterial.variableValueMap.put(variable.name, arr);
                                }
                            } else if (variable.type == ShaderLanguage.VariableType.VECTOR3) {
                                if (!variable.array) {
                                    Vector3 vec = (Vector3) obj;
                                    tempVec3[0] = vec.x;
                                    tempVec3[1] = vec.y;
                                    tempVec3[2] = vec.z;
                                    ImGui.inputFloat3(styleName(variable.name), tempVec3);
                                    vec.set(tempVec3[0], tempVec3[1], tempVec3[2]);
                                    selectedMaterial.variableValueMap.put(variable.name, vec);
                                } else {
                                    float[] arr = (float[]) obj;
                                    ImGui.text(styleName(variable.name));
                                    ImGui.indent();
                                    for (int i = 0; i < arr.length; i += 3) {
                                        tempVec3[0] = arr[i];
                                        tempVec3[1] = arr[i + 1];
                                        tempVec3[2] = arr[i + 2];
                                        ImGui.inputFloat3("##Var" + variable.name + i, tempVec3);
                                        arr[i] = tempVec3[0];
                                        arr[i + 1] = tempVec3[1];
                                        arr[i + 2] = tempVec3[2];
                                    }
                                    if (ImGui.smallButton("+##ArrayAppend" + variable.name))
                                        arr = Arrays.copyOf(arr, arr.length + 3);
                                    ImGui.unindent();
                                    selectedMaterial.variableValueMap.put(variable.name, arr);
                                }
                            } else if (obj.getClass() == Vector4.class) {
                                if (!variable.array) {
                                    Color col = (Color) obj;
                                    tempColor[0] = col.r;
                                    tempColor[1] = col.g;
                                    tempColor[2] = col.b;
                                    tempColor[3] = col.a;
                                    ImGui.inputFloat4(styleName(variable.name), tempColor);
                                    col.set(tempColor[0], tempColor[1], tempColor[2], tempColor[3]);
                                    selectedMaterial.variableValueMap.put(variable.name, col);
                                } else {
                                    float[] arr = (float[]) obj;
                                    ImGui.text(styleName(variable.name));
                                    ImGui.indent();
                                    for (int i = 0; i < arr.length; i += 4) {
                                        tempColor[0] = arr[i];
                                        tempColor[1] = arr[i + 1];
                                        tempColor[2] = arr[i + 2];
                                        ImGui.inputFloat4("##Var" + variable.name + i, tempColor);
                                        arr[i] = tempColor[0];
                                        arr[i + 1] = tempColor[1];
                                        arr[i + 2] = tempColor[2];
                                        arr[i + 3] = tempColor[3];
                                    }
                                    if (ImGui.smallButton("+##ArrayAppend" + variable.name))
                                        arr = Arrays.copyOf(arr, arr.length + 4);
                                    ImGui.unindent();
                                    selectedMaterial.variableValueMap.put(variable.name, arr);
                                }
                            } else if (variable.type == ShaderLanguage.VariableType.MATRIX3) {
                                Matrix3 mat = (Matrix3) obj;
                                float[] values = mat.getValues();

                                tempVec3[0] = values[Matrix3.M00];
                                tempVec3[1] = values[Matrix3.M01];
                                tempVec3[2] = values[Matrix3.M02];
                                ImGui.inputFloat3("##Val" + variable.name + "0", tempVec3);
                                values[Matrix3.M00] = tempVec3[0];
                                values[Matrix3.M01] = tempVec3[1];
                                values[Matrix3.M02] = tempVec3[2];

                                tempVec3[0] = values[Matrix3.M10];
                                tempVec3[1] = values[Matrix3.M11];
                                tempVec3[2] = values[Matrix3.M12];
                                ImGui.inputFloat3("##Val" + variable.name + "1", tempVec3);
                                values[Matrix3.M10] = tempVec3[0];
                                values[Matrix3.M11] = tempVec3[1];
                                values[Matrix3.M12] = tempVec3[2];

                                tempVec3[0] = values[Matrix3.M20];
                                tempVec3[1] = values[Matrix3.M21];
                                tempVec3[2] = values[Matrix3.M22];
                                ImGui.inputFloat3("##Val" + variable.name + "2", tempVec3);
                                values[Matrix3.M20] = tempVec3[0];
                                values[Matrix3.M21] = tempVec3[1];
                                values[Matrix3.M22] = tempVec3[2];

                                selectedMaterial.variableValueMap.put(variable.name, mat.set(values));
                            } else if (variable.type == ShaderLanguage.VariableType.MATRIX4) {
                                Matrix4 mat = (Matrix4) obj;
                                float[] values = mat.getValues();

                                tempColor[0] = values[Matrix4.M00];
                                tempColor[1] = values[Matrix4.M01];
                                tempColor[2] = values[Matrix4.M02];
                                tempColor[3] = values[Matrix4.M03];
                                ImGui.inputFloat4("##Val" + variable.name + "0", tempColor);
                                values[Matrix4.M00] = tempColor[0];
                                values[Matrix4.M01] = tempColor[1];
                                values[Matrix4.M02] = tempColor[2];
                                values[Matrix4.M03] = tempColor[3];

                                tempColor[0] = values[Matrix4.M10];
                                tempColor[1] = values[Matrix4.M11];
                                tempColor[2] = values[Matrix4.M12];
                                tempColor[3] = values[Matrix4.M13];
                                ImGui.inputFloat4("##Val" + variable.name + "1", tempColor);
                                values[Matrix4.M10] = tempColor[0];
                                values[Matrix4.M11] = tempColor[1];
                                values[Matrix4.M12] = tempColor[2];
                                values[Matrix4.M13] = tempColor[3];

                                tempColor[0] = values[Matrix4.M20];
                                tempColor[1] = values[Matrix4.M21];
                                tempColor[2] = values[Matrix4.M22];
                                tempColor[3] = values[Matrix4.M23];
                                ImGui.inputFloat4("##Val" + variable.name + "2", tempColor);
                                values[Matrix4.M20] = tempColor[0];
                                values[Matrix4.M21] = tempColor[1];
                                values[Matrix4.M22] = tempColor[2];
                                values[Matrix4.M23] = tempColor[3];

                                tempColor[0] = values[Matrix4.M30];
                                tempColor[1] = values[Matrix4.M31];
                                tempColor[2] = values[Matrix4.M32];
                                tempColor[3] = values[Matrix4.M33];
                                ImGui.inputFloat4("##Val" + variable.name + "3", tempColor);
                                values[Matrix4.M30] = tempColor[0];
                                values[Matrix4.M31] = tempColor[1];
                                values[Matrix4.M32] = tempColor[2];
                                values[Matrix4.M33] = tempColor[3];

                                selectedMaterial.variableValueMap.put(variable.name, mat.set(values));
                            }
                        }
                        if (variable.type == ShaderLanguage.VariableType.SAMPLER2D) {
                            TextureAsset textureAsset = obj != null ? (TextureAsset) obj : null;
                            String sel = "Default";
                            for (String key : engine.loadedTextures.keySet()) {
                                TextureAsset tex = engine.loadedTextures.get(key);
                                if (tex == textureAsset) {
                                    sel = key;
                                    break;
                                }
                            }
                            if (ImGui.beginPopup("TextureSelector")) {
                                if (ImGui.button("Default", 100 + ImGui.getStyle().getFramePaddingX() * 2, 100 + ImGui.getStyle().getFramePaddingY() * 2)) {
                                    textureAsset = null;
                                    ImGui.closeCurrentPopup();
                                }
                                for (String key : engine.loadedTextures.keySet()) {
                                    TextureAsset tex = engine.loadedTextures.get(key);
                                    if (ImGui.imageButton(tex.getGdxTexture().getTextureObjectHandle(), 100, 100, 0, 0, 1, 1, 0)) {
                                        textureAsset = tex;
                                        ImGui.closeCurrentPopup();
                                    }
                                }
                                isAnyWindowHovered |= ImGui.isWindowHovered();
                                ImGui.endPopup();
                                if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                                    ImGui.closeCurrentPopup();
                                }

                                selectedMaterial.variableValueMap.put(variable.name, textureAsset);
                            }
                            if (ImGui.button("Select (" + sel + ")##Sel" + name)) {
                                ImGui.openPopup("TextureSelector");
                            }
                        }
                    }
                }

                if (ImGui.beginPopup("ShaderSelectPopup")) {
                    for (String key : engine.loadedShaders.keySet()) {
                        Shader shader = engine.loadedShaders.get(key);
                        if (ImGui.selectable(shader.shaderFile)) {
                            selectedMaterial.shader = shader;
                            selectedMaterial.update();
                            ImGui.closeCurrentPopup();
                            break;
                        }
                    }
                    isAnyWindowHovered |= ImGui.isWindowHovered();
                    ImGui.endPopup();
                }

            }

        }

        if (selection == null && selectedAudioSource != null) {
            //selectedAudioSource.dispose();
        }

        isAnyWindowHovered |= ImGui.isWindowHovered() | ImGui.isAnyItemHovered();

        ImGui.end();

    }

    private void console() {
        ImGui.setNextWindowPos(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, ImGuiCond.FirstUseEver);
        ImGui.setNextWindowSizeConstraints(300,150, 100000, 100000);
        ImGui.begin("Console##" + consoleID, ImGuiWindowFlags.NoBringToFrontOnFocus);

        for (String l : Debug.readDebug()) {
            String txt = l.trim().substring(Math.min(5, l.length()));
            if (l.trim().startsWith("[OUT]")) {
                ImGui.text(txt);
            } else if (l.trim().startsWith("[ERR]")) {
                ImGui.textColored(255, 0, 0, 255, txt);
            }
        }

        isAnyWindowHovered |= ImGui.isWindowHovered() | ImGui.isAnyItemHovered();

        ImGui.end();
    }

    int dockSpaceID;
    int dockSpaceFlags = ImGuiDockNodeFlags.PassthruCentralNode;
    boolean first = true;
    ImInt dockSpaceTemp = new ImInt();

    private int inputInt(String label, int f) {
        ImGui.text(label);
        ImGui.sameLine();
        ImGui.setCursorPosX(200);
        tempInteger.set(f);
        ImGui.inputInt("##" + label, tempInteger);
        ImGui.setNextItemWidth(700);
        return tempInteger.get();
    }

    private Vector2 inputVec2(String label, Vector2 v) {
        ImGui.text(label);
        ImGui.sameLine();
        ImGui.setCursorPosX(200);
        tempVec2[0] = v.x;
        tempVec2[1] = v.y;
        ImGui.inputFloat2("##" + label, tempVec2);
        ImGui.setNextItemWidth(700);
        return v.set(tempVec2[0], tempVec2[1]);
    }

    private boolean inputBool(String label, boolean b) {
        ImGui.text(label);
        ImGui.sameLine();
        ImGui.setCursorPosX(200);
        tempBoolean.set(b);
        ImGui.checkbox("##" + label, tempBoolean);
        return tempBoolean.get();
    }

    private ImVec2 tmpImVec2 = new ImVec2();

    private void separator(String label) {

        float cx = ImGui.getCursorScreenPosX();
        float cy = ImGui.getCursorScreenPosY() + 20;

        ImGui.getWindowDrawList().addLine(cx, cy, cx + 50, cy, ImGui.getColorU32(1,1,1,1), 1);
        ImGui.getWindowDrawList().addText(cx + 60, cy - 8, ImGui.getColorU32(1,1,1,1), label);
        ImGui.calcTextSize(tmpImVec2, label);
        ImGui.getWindowDrawList().addLine(cx + 70 + tmpImVec2.x, cy, cx + ImGui.getContentRegionAvailX(), cy, ImGui.getColorU32(1,1,1,1), 1);

        ImGui.setCursorScreenPos(cx, cy + 20);

    }

    private void separatorCenter(String label) {

        float cx = ImGui.getCursorScreenPosX();
        float cy = ImGui.getCursorScreenPosY() + 20;

        ImGui.calcTextSize(tmpImVec2, label);
        ImGui.getWindowDrawList().addLine(cx, cy, cx + ImGui.getContentRegionAvailX() / 2 - tmpImVec2.x / 2, cy, ImGui.getColorU32(1,1,1,1), .7f);
        ImGui.getWindowDrawList().addText(cx + ImGui.getContentRegionAvailX() / 2 - tmpImVec2.x / 2 + 10, cy - 8, ImGui.getColorU32(1,1,1,1), label);
        ImGui.getWindowDrawList().addLine(cx + ImGui.getContentRegionAvailX() / 2 + tmpImVec2.x / 2 + 20, cy, cx + ImGui.getContentRegionAvailX(), cy, ImGui.getColorU32(1,1,1,1), .7f);

        ImGui.setCursorScreenPos(cx, cy + 20);

    }

    private String openDialog(String filter) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            PointerBuffer path = stack.callocPointer(1);
            NativeFileDialog.NFD_OpenDialog(filter, "", path);
            return MemoryUtil.memUTF8Safe(path.get(0));
        }
    }

    private String inputFile(String label, String file, String filter, float w0, float w1) {
        ImGui.text(label);
        ImGui.sameLine();
        ImGui.setCursorPosX(w0);
        imString.set(file);
        ImGui.inputText("##InFile" + label, imString);
        ImGui.setNextItemWidth(w1);
        ImGui.sameLine();
        if (ImGui.button("...##InFile" + label)) {
            imString.set(openDialog(filter));
        }
        return imString.get();
    }

    private String inputFile(String label, String file, String filter) {
        return inputFile(label, file, filter, 200, 700);
    }

    private String inputFile(String label, String file) {
        return inputFile(label, file, "");
    }

    String assetPackName = "";
    String assetPackVersion = "";
    String assetPackDependencies = "";
    FileHandle assetPackFolder;
    String assetsWindowFile = "";
    String assetsWindowDependency = "";
    String assetsWindowRepository = "";
    String assetsWindowDependencyLast = "";
    boolean assetsWindowDependencyEnabled = false;
    int selectedDependencyApp = 0;

    public void render() throws IOException, ClassNotFoundException {

        boolean doubleClick = ImGui.isMouseDoubleClicked(0);

        isAnyWindowHovered = false;

        imGuiGlfw.newFrame();
        ImGui.newFrame();
        ImGuizmo.beginFrame();

        /*ImGui.showStyleEditor();
        ImGui.showDemoWindow();*/

        ImGui.beginMainMenuBar();

        mainMenuBar();

        if (Utils.isCtrlKeyPressed() && Gdx.input.isKeyJustPressed(Input.Keys.Z) && historyIdx >= history.size - 1) Utils.undo();
        if (Utils.isCtrlKeyPressed() && Utils.isShiftKeyPressed() && Gdx.input.isKeyJustPressed(Input.Keys.Z) && historyIdx >= history.size - 1) Utils.redo();

        if (ImGui.beginMenu("Edit")) {
            ImGui.beginDisabled(historyIdx <= 0);
            if (ImGui.menuItem("Undo", "Ctrl+Z"))
                Utils.undo();
            ImGui.endDisabled();
            ImGui.beginDisabled(historyIdx >= history.size - 1);
            if (ImGui.menuItem("Redo", "Ctrl+Shift+Z"))
                Utils.redo();
            ImGui.endDisabled();
            ImGui.separator();
            if (ImGui.menuItem("Project Settings")) {
                showProjectSettings.set(!showProjectSettings.get());
            }
            ImGui.endMenu();
        }

        if (ImGui.beginMenu("Window")) {
            if (ImGui.menuItem("Assets")) {
                showAssetsWindow.set(!showAssetsWindow.get());
            }
            ImGui.endMenu();
        }

        ImGui.endMainMenuBar();

        if (showPreferences.get()) {
            ImGui.begin("Preferences", showPreferences);

            String oldPath = preferences.getString("idePath");
            String newPath;
            newPath = inputText("IDE Path", oldPath);
            if (!newPath.equals(oldPath)) preferences.putString("idePath", newPath);

            if (ImGui.button("Apply")) preferences.flush();

            ImGui.end();
        }

        if (showProjectSettings.get()) {
            ImGui.begin("Project Settings", showProjectSettings);

            separator("Main");

            currentProject.windowTitle = inputText("Window Title", currentProject.windowTitle);
            currentProject.buildName = inputText("Build Name", currentProject.buildName);
            currentProject.version = inputText("Version", currentProject.version);
            currentProject.windowSize.set(inputVec2("Window Size", currentProject.windowSize));
            currentProject.fullscreen = inputBool("Fullscreen", currentProject.fullscreen);

            ImGui.separator();

            currentProject.appIconWin = inputFile("Windows App Icon", currentProject.appIconWin, "ico");
            currentProject.appIconLinux = inputFile("Linux App Icon", currentProject.appIconLinux, "png");
            currentProject.appIconMac = inputFile("Mac App Icon", currentProject.appIconMac, "icns");

            currentProject.windowIcon16 = inputFile("Window Icon x16", currentProject.windowIcon16, "png,jpg");
            currentProject.windowIcon32 = inputFile("Window Icon x32", currentProject.windowIcon32, "png,jpg");
            currentProject.windowIcon64 = inputFile("Window Icon x64", currentProject.windowIcon64, "png,jpg");
            currentProject.windowIcon128 = inputFile("Window Icon x128", currentProject.windowIcon128, "png,jpg");

            separator("Physics");

            currentProject.physicsEnabled = inputBool("Enabled", currentProject.physicsEnabled);
            currentProject.physicsGravity.set(inputVec2("Gravity", currentProject.physicsGravity));
            currentProject.physicsVelocityIterations = inputInt("Velocity Iterations", currentProject.physicsVelocityIterations);
            currentProject.physicsPositionIterations = inputInt("Position Iterations", currentProject.physicsPositionIterations);

            isAnyWindowHovered |= ImGui.isWindowHovered() || ImGui.isAnyItemHovered();

            ImGui.end();
        }

        if (showAssetsWindow.get()) {
            ImGui.setNextWindowSizeConstraints(360,400, 1000, 1000);
            ImGui.begin("Assets", showAssetsWindow);

            separatorCenter("Assets");

            assetsWindowFile = inputFile("Assets", assetsWindowFile, "assets", 80, 200);
            if (ImGui.button("Import", 128, 28)) {
                AssetPackUtils.expandAssetPack(Gdx.files.absolute(assetsWindowFile), currentProject.path.child("Assets"));
                refresh();
            }

            ImGui.spacing();
            separatorCenter("Dependencies");
            ImGui.spacing();

            if (ImGui.button("Refresh", 128, 28))
                Utils.updateDependencies();

            ImGui.spacing();
            ImGui.spacing();
            ImGui.spacing();

            assetsWindowRepository = inputText("Repository", assetsWindowRepository);

            if (ImGui.button("Add##RepositoryAddBtn", 128, 28)) {
                currentProject.repositories.add(assetsWindowRepository.strip());
                assetsWindowRepository = "";
                projectChange = true;
                Utils.updateDependencies();
            }

            for (int i = currentProject.repositories.size() - 1; i >= 0; i--) {
                ImGui.setNextItemWidth(ImGui.getContentRegionAvailX());
                if (ImGui.selectable(currentProject.repositories.get(i))) {
                    currentProject.repositories.remove(i);
                    Utils.updateDependencies();
                }
            }

            ImGui.spacing();
            ImGui.spacing();
            ImGui.spacing();

            assetsWindowDependency = inputText("Dependency", assetsWindowDependency);

            if (!assetsWindowDependency.equals(assetsWindowDependencyLast))
                assetsWindowDependencyEnabled = assetsWindowDependency.split(":").length >= 3;

            ImGui.beginDisabled(!assetsWindowDependencyEnabled);
            if (ImGui.radioButton("Core", selectedDependencyApp == 0)) selectedDependencyApp = 0;
            ImGui.sameLine();
            if (ImGui.radioButton("Desktop", selectedDependencyApp == 1)) selectedDependencyApp = 1;
            ImGui.sameLine();
            if (ImGui.radioButton("HTML", selectedDependencyApp == 2)) selectedDependencyApp = 2;
            if (ImGui.button("Add##DependencyAddBtn", 128, 28)) {
                if (selectedDependencyApp == 0)
                    currentProject.dependencies.add(assetsWindowDependency.strip());
                if (selectedDependencyApp == 1)
                    currentProject.desktopDependencies.add(assetsWindowDependency.strip());
                if (selectedDependencyApp == 2)
                    currentProject.htmlDependencies.add(assetsWindowDependency.strip());
                assetsWindowDependency = "";
                projectChange = true;
                Utils.updateDependencies();
            }
            ImGui.endDisabled();

            assetsWindowDependencyLast = assetsWindowDependency;

            ImGui.spacing();

            separator("CORE");
            for (int i = currentProject.dependencies.size() - 1; i >= 0; i--) {
                ImGui.setNextItemWidth(ImGui.getContentRegionAvailX());
                if (ImGui.selectable(currentProject.dependencies.get(i))) {
                    currentProject.dependencies.remove(i);
                    Utils.updateDependencies();
                }
            }
            separator("DESKTOP");
            for (int i = currentProject.desktopDependencies.size() - 1; i >= 0; i--) {
                ImGui.setNextItemWidth(ImGui.getContentRegionAvailX());
                if (ImGui.selectable(currentProject.desktopDependencies.get(i))) {
                    currentProject.desktopDependencies.remove(i);
                    Utils.updateDependencies();
                }
            }
            separator("HTML");
            for (int i = currentProject.htmlDependencies.size() - 1; i >= 0; i--) {
                ImGui.setNextItemWidth(ImGui.getContentRegionAvailX());
                if (ImGui.selectable(currentProject.htmlDependencies.get(i))) {
                    currentProject.htmlDependencies.remove(i);
                    Utils.updateDependencies();
                }
            }

            isAnyWindowHovered |= ImGui.isWindowHovered() || ImGui.isAnyItemHovered();

            ImGui.end();
        }

        int windowFlags = ImGuiWindowFlags.NoDocking;

        windowFlags |= ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove;
        windowFlags |= ImGuiWindowFlags.NoBringToFrontOnFocus | ImGuiWindowFlags.NoNavFocus | ImGuiWindowFlags.NoBackground;

        ImGuiViewport imGuiViewport = ImGui.getMainViewport();

        ImGui.setNextWindowPos(imGuiViewport.getPosX(), imGuiViewport.getPosY() + menuBarSize + toolbarHeight);
        ImGui.setNextWindowSize(imGuiViewport.getSizeX(), imGuiViewport.getSizeY() - menuBarSize - toolbarHeight);

        ImGui.pushStyleVar(ImGuiStyleVar.WindowPadding, 0, 0);
        ImGui.begin("DockSpace#DockSpaceWindow", windowFlags);
        ImGui.popStyleVar();

        dockSpaceID = ImGui.getID("DockSpace");
        ImGui.dockSpace(dockSpaceID, 0, 0, dockSpaceFlags);

        if (first) {
            first = false;
            ImGui.setWindowFocus(null);
            ImGui.dockBuilderRemoveNode(dockSpaceID); // clear any previous layout
            ImGui.dockBuilderAddNode(dockSpaceID, dockSpaceFlags | 1 << 10);
            ImGui.dockBuilderSetNodeSize(dockSpaceID, imGuiViewport.getSizeX(), imGuiViewport.getSizeY());

            int centralID;

            int rightId = ImGui.dockBuilderSplitNode(dockSpaceID, 1, 0.2f, null, dockSpaceTemp);
            int leftId = dockSpaceTemp.get();

            int bottomId = ImGui.dockBuilderSplitNode(leftId, 3, 0.3f, null, dockSpaceTemp);
            int topId = dockSpaceTemp.get();
            int topLeft = ImGui.dockBuilderSplitNode(topId, 0, 0.2f, null, dockSpaceTemp);
            centralID = dockSpaceTemp.get();

            int rightTopId = ImGui.dockBuilderSplitNode(rightId, 2, 0.5f, null, dockSpaceTemp);

            int bottomLeftId = ImGui.dockBuilderSplitNode(bottomId, 0, 0.4f, null, dockSpaceTemp);

            ImGui.dockBuilderDockWindow("Console##" + consoleID, bottomLeftId);
            ImGui.dockBuilderDockWindow("Hierarchy##HierarchyWindow", topLeft);
            ImGui.dockBuilderDockWindow("Inspector##InspectorWindow", rightTopId);
            ImGui.dockBuilderDockWindow("Project##ProjectWindow", bottomLeftId);
            ImGui.dockBuilderDockWindow("Scene", centralID);

            ImGui.dockBuilderFinish(dockSpaceID);
        }

        ImGui.end();

        ImGui.begin("Scene");
        sceneX = (int) ImGui.getWindowPosX();
        sceneY = (int) ImGui.getWindowPosY();
        int sceneWidth = (int) ImGui.getWindowWidth();
        int sceneHeight = (int) ImGui.getWindowHeight();

        if (lastSceneWidth != sceneWidth || lastSceneHeight != sceneHeight) {
            if (fbo != null) fbo.dispose();
            fbo = new FrameBuffer(Pixmap.Format.RGBA8888, sceneWidth, sceneHeight, false);
        }
        //ImGuizmo.drawGrid(editorViewport.getCamera().view.val, editorViewport.getCamera().projection.val, new Matrix4().val, 10);

        ImGui.getWindowDrawList().addImage(fboTexture.getTextureObjectHandle(), sceneX, sceneY, sceneX + sceneWidth, sceneY + sceneHeight, 0, 1, 1, 0);

        ImGui.end();

        lastSceneWidth = sceneWidth;
        lastSceneHeight = sceneHeight;

        if (toggleBuildWindow.get()) {
            ImGui.setNextWindowSize(800, 400);
            ImGui.begin("Build", toggleBuildWindow, ImGuiWindowFlags.HorizontalScrollbar | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoDocking);

            ImGui.text("Build");

            ImGui.separator();

            ImGui.textDisabled("Platform:");

            ImGui.pushStyleVar(ImGuiStyleVar.FrameBorderSize, 1);
            int col = ImGui.getColorU32(0, 0, 0, 1);

            if (buildPlatform != 0) ImGui.pushStyleColor(ImGuiCol.Button, col);
            boolean b = ImGui.button("Java (JAR)", 170, 40);
            if (buildPlatform != 0) ImGui.popStyleColor();
            if (b) buildPlatform = 0;
            ImGui.sameLine();

            ImGui.beginDisabled(version.feature() < 14);

            if (buildPlatform != 1) ImGui.pushStyleColor(ImGuiCol.Button, col);
            ImGui.beginDisabled(os != 2);
            b = ImGui.button("Windows", 170, 40);
            if (buildPlatform != 1) ImGui.popStyleColor();
            if (b) buildPlatform = 1;
            if (os != 2 && ImGui.isItemHovered(ImGuiHoveredFlags.AllowWhenDisabled)) ImGui.setTooltip("Requires Windows OS");
            if (version.feature() < 14 && ImGui.isItemHovered(ImGuiHoveredFlags.AllowWhenDisabled)) ImGui.setTooltip("Requires Java 14+");
            ImGui.endDisabled();

            ImGui.sameLine();

            if (buildPlatform != 2) ImGui.pushStyleColor(ImGuiCol.Button, col);
            ImGui.beginDisabled(os != 1);
            b = ImGui.button("Linux", 170, 40);
            if (buildPlatform != 2) ImGui.popStyleColor();
            if (b) buildPlatform = 2;
            if (os != 1 && ImGui.isItemHovered(ImGuiHoveredFlags.AllowWhenDisabled)) ImGui.setTooltip("Requires Linux OS");
            if (version.feature() < 14 && ImGui.isItemHovered(ImGuiHoveredFlags.AllowWhenDisabled)) ImGui.setTooltip("Requires Java 14+");
            ImGui.endDisabled();

            ImGui.sameLine();

            if (buildPlatform != 3) ImGui.pushStyleColor(ImGuiCol.Button, col);
            ImGui.beginDisabled(os != 3);
            b = ImGui.button("macOS", 170, 40);
            if (buildPlatform != 3) ImGui.popStyleColor();
            if (b) buildPlatform = 3;
            if (os != 3 && ImGui.isItemHovered(ImGuiHoveredFlags.AllowWhenDisabled)) ImGui.setTooltip("Requires macOS");
            if (version.feature() < 14 && ImGui.isItemHovered(ImGuiHoveredFlags.AllowWhenDisabled)) ImGui.setTooltip("Requires Java 14+");
            ImGui.endDisabled();

            ImGui.endDisabled();

            ImGui.sameLine();

            if (buildPlatform != 4) ImGui.pushStyleColor(ImGuiCol.Button, col);
            b = ImGui.button("GWT", 170, 40);
            if (buildPlatform != 4) ImGui.popStyleColor();
            if (b) buildPlatform = 4;

            ImGui.sameLine();

            if (buildPlatform != 5) ImGui.pushStyleColor(ImGuiCol.Button, col);
            b = ImGui.button("Source", 170, 40);
            if (buildPlatform != 5) ImGui.popStyleColor();
            if (b) buildPlatform = 5;

            ImGui.popStyleVar();

            if (buildProgress >= 0) {
                float progress = Math.min(buildProgress, 100) / 100f;
                ImGui.spacing();
                ImGui.text("Building...");
                ImGui.progressBar(progress, 200, 20);
                if (buildProgress >= 100) {
                    buildProgress++;
                    if (buildProgress >= 200) buildProgress = -1;
                }
            }

            ImGui.setCursorPosY(ImGui.getWindowSizeY() - 50);

            if (ImGui.button("Build", 100, 30)) {
                Gui g = this;
                Thread t = new Thread(() -> {
                    g.buildProgress = 1;
                    switch (buildPlatform) {
                        case 0:
                            Utils.build(true);
                            break;
                        case 4:
                            Utils.buildGwt(true);
                            break;
                        case 5:
                            Utils.buildSource(true);
                            break;
                        default:
                            Utils.buildApp();
                            break;
                    }
                    g.buildProgress = 100;
                });
                t.setDaemon(true);
                t.start();
            }
            ImGui.sameLine();
            ImGui.beginDisabled(buildPlatform >= 4);
            if (ImGui.button("Build & Run", 100, 30)) {
                Gui g = this;
                new Thread(() -> {
                    g.buildProgress = 1;
                    if (buildPlatform == 0) Utils.build(true); else Utils.buildApp();
                    g.buildProgress = 80;
                    ProcessBuilder pb = new ProcessBuilder("java", "-jar", "Game.jar");
                    pb.directory(currentProject.path.child("Build").file());
                    try {
                        pb.start();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    g.buildProgress = 100;
                }).start();
            }
            ImGui.endDisabled();

            isAnyWindowHovered |= ImGui.isWindowHovered() || ImGui.isAnyItemHovered();

            ImGui.end();
        }

        ImGui.setNextWindowPos(Gdx.graphics.getWidth() / 2f + 50, Gdx.graphics.getHeight() / 2f, ImGuiCond.FirstUseEver);
        ImGui.setNextWindowSizeConstraints(300,150, 100000, 100000);
        ImGui.begin("Project##ProjectWindow", ImGuiWindowFlags.NoBringToFrontOnFocus);

        boolean hovered;

        ArrayList<FileHandle> assets = assetsMap.get(projectExplorerPath);

        if (!projectExplorerPath.equals("main")) {
            ImGui.beginGroup();

            ImGui.imageButton(folderIcon.getTextureObjectHandle(), 100, 100);
            if (ImGui.isItemHovered() && doubleClick) {

                StringBuilder sb = new StringBuilder();
                sb.append("main");

                String[] sp = projectExplorerPath.split("/");

                for (int i = 1; i < sp.length - 1; i++) {
                    sb.append("/");
                    sb.append(sp[i]);
                }

                projectExplorerPath = sb.toString();

            }

            ImGui.text("...");

            ImGui.endGroup();

            ImGui.sameLine();
        }

        for (int i = 0; i < assets.size(); i++) {
            FileHandle asset = assets.get(i);

            ImGui.beginGroup();

            if (asset.extension().equalsIgnoreCase("java")) {
                ImGui.imageButton(codeIcon.getTextureObjectHandle(), 100, 100);
                if (ImGui.isItemHovered() || ImGui.isItemActive()) contextMenuFile = asset;
                if (ImGui.isItemHovered() && doubleClick) {
                    FileHandle ideFile = Gdx.files.absolute(preferences.getString("idePath"));
                    if (ideFile.exists() && !ideFile.isDirectory()) {
                        String[] command = new String[]{preferences.getString("idePath"), Statics.currentProject.path.path(), Statics.currentProject.path.child("Assets").child(asset.name()).path()};
                        ProcessBuilder pb = new ProcessBuilder(command);
                        pb.start();
                    } else {
                        showPreferences.set(true);
                    }
                }

                if (ImGui.beginDragDropSource(ImGuiDragDropFlags.None)) {

                    ImGui.setDragDropPayload("Component", Utils.getPackage(asset) + asset.nameWithoutExtension());

                    ImGui.image(codeIcon.getTextureObjectHandle(), 100, 100);

                    ImGui.endDragDropSource();
                }
            } else if (asset.extension().equalsIgnoreCase("png") || asset.extension().equalsIgnoreCase("jpg") || asset.extension().equalsIgnoreCase("jpeg")) {
                if (!engine.loadedTextures.containsKey(asset.nameWithoutExtension())) {
                    engine.loadTexture(asset.nameWithoutExtension(), asset);
                } else {
                    TextureAsset tex = engine.loadedTextures.get(asset.nameWithoutExtension());
                    float ratio = tex.getWidth() / (float) tex.getHeight();
                    float height = 100 / ratio;
                    ImVec2 cursor = ImGui.getCursorPos();
                    ImGui.button("##" + asset.name() + "Texture", 100, 100);
                    if (ImGui.isItemHovered() || ImGui.isItemActive()) contextMenuFile = asset;
                    if (ImGui.beginDragDropSource(ImGuiDragDropFlags.None)) {

                        ImGui.setDragDropPayload("Texture", new TexturePayload(tex));

                        ImGui.image(tex.getGdxTexture().getTextureObjectHandle(), 100, height);

                        ImGui.endDragDropSource();
                    }
                    ImGui.setCursorPos(cursor.x, cursor.y + 50 - (height / 2f));
                    ImGui.image(tex.getGdxTexture().getTextureObjectHandle(), 100, height);
                    ImGui.setCursorPos(cursor.x, cursor.y + 100);
                }
            } else if (asset.extension().equalsIgnoreCase("flac") || asset.extension().equalsIgnoreCase("ogg") || asset.extension().equalsIgnoreCase("mp3") || asset.extension().equalsIgnoreCase("wav")) {
                if (!engine.loadedAudioClips.containsKey(asset.path())) {
                    engine.loadAudio(asset.path(), asset);
                } else {
                    ImGui.pushID(asset.path());
                    if (ImGui.imageButton(volumeIcon.getTextureObjectHandle(), 100, 100)) {
                        selectedGameObject = null;
                        selection = engine.loadedAudioClips.get(asset.path());
                    }
                    ImGui.popID();
                    if (ImGui.isItemHovered() || ImGui.isItemActive()) contextMenuFile = asset;
                    if (ImGui.beginDragDropSource(ImGuiDragDropFlags.None)) {
                        AudioClip audioClip = engine.loadedAudioClips.get(asset.path());

                        ImGui.setDragDropPayload("AudioClip", new AudioClipPayload(audioClip));

                        ImGui.image(volumeIcon.getTextureObjectHandle(), 100, 100);

                        ImGui.endDragDropSource();
                    }
                }
            } else if (asset.extension().equalsIgnoreCase("sha")) {
                if (!engine.loadedShaders.containsKey(asset.path())) {
                    engine.loadShader(asset.path(), asset);
                } else {
                    ImGui.pushID(asset.path());
                    if (ImGui.imageButton(fileIcon.getTextureObjectHandle(), 100, 100)) {
                        selectedGameObject = null;
                        selection = engine.loadedShaders.get(asset.path());
                    }
                    ImGui.popID();
                    if (ImGui.isItemHovered() || ImGui.isItemActive()) contextMenuFile = asset;
                    if (ImGui.beginDragDropSource(ImGuiDragDropFlags.None)) {
                        // TODO : DRAG & DROP

                        ImGui.endDragDropSource();
                    }
                }
            } else if (asset.extension().equalsIgnoreCase("mat")) {
                if (!engine.loadedMaterials.containsKey(asset.path())) {
                    engine.loadMaterial(asset.path(), asset);
                } else {
                    Material mat = engine.loadedMaterials.get(asset.path());
                    ImGui.pushID(asset.path());
                    if (ImGui.imageButton(fileIcon.getTextureObjectHandle(), 100, 100)) {
                        selectedGameObject = null;
                        selection = mat;
                    }
                    ImGui.popID();
                    if (ImGui.isItemHovered() || ImGui.isItemActive()) contextMenuFile = asset;
                    if (ImGui.beginDragDropSource(ImGuiDragDropFlags.None)) {

                        ImGui.setDragDropPayload("Material", new MaterialPayload(mat));

                        ImGui.image(fileIcon.getTextureObjectHandle(), 100, 100);

                        ImGui.endDragDropSource();
                    }
                }
            } else if (asset.extension().equalsIgnoreCase("scene")) {
                ImGui.pushID(asset.path());
                ImGui.imageButton(fileIcon.getTextureObjectHandle(), 100, 100);
                if (ImGui.isItemHovered() && doubleClick) {
                    currentProject.changeScene(Utils.getPackage(asset, "/"));
                }
                ImGui.popID();
            } else if (asset.isDirectory()) {
                ImGui.imageButton(folderIcon.getTextureObjectHandle(), 100, 100);
                if (ImGui.isItemHovered() && doubleClick) {
                    projectExplorerPath += "/" + asset.name();
                }
                if (ImGui.isItemHovered() || ImGui.isItemActive()) contextMenuFile = asset;
            } else {
                ImGui.imageButton(fileIcon.getTextureObjectHandle(), 100, 100);
                if (ImGui.isItemHovered() || ImGui.isItemActive()) contextMenuFile = asset;

            }
            if (!asset.equals(renameFile)) {
                ImGui.pushTextWrapPos(ImGui.getCursorPosX() + 100);
                ImGui.textWrapped(asset.name());
                ImGui.popTextWrapPos();
            } else {
                ImGui.setNextItemWidth(80);
                String newName = inputTextID("fileRename", asset.name());
                if (!ImGui.isItemFocused()) {
                    ImGui.setKeyboardFocusHere();
                }
                boolean hover = ImGui.isItemHovered();
                if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || (!hover && ImGui.isMouseClicked(0))) {
                    asset.moveTo(asset.parent().child(newName));
                    renameFile = null;
                    refresh();
                }
            }

            ImGui.endGroup();

            ImGui.sameLine();

        }

        hovered = ImGui.isAnyItemHovered();

        isAnyWindowHovered |= ImGui.isWindowHovered() | ImGui.isAnyItemHovered();

        ImGui.setCursorPos(0,0);
        ImGui.invisibleButton("ProjectPopupContextItemButton", ImGui.getWindowWidth(), ImGui.getWindowHeight());

        boolean openPopup = false;
        String popupName = "";

        if (ImGui.beginPopupContextWindow("ProjectPopupContextItem" + (contextMenuFile != null ? contextMenuFile.name() : ""))) {
            hovered = true;
            if (ImGui.beginMenu("Create")) {
                if (ImGui.menuItem("Scene")) {
                    fileName.set("");
                    ImGui.closeCurrentPopup();
                    openPopup = true;
                    popupName = "CreateScenePopup";
                }
                ImGui.separator();
                if (ImGui.menuItem("Folder")) {
                    fileName.set("");
                    ImGui.closeCurrentPopup();
                    openPopup = true;
                    popupName = "CreateFolderPopup";
                }
                if (ImGui.menuItem("Java Script")) {
                    fileName.set("");
                    ImGui.closeCurrentPopup();
                    openPopup = true;
                    popupName = "CreateScriptPopup";
                }
                ImGui.separator();
                if (ImGui.menuItem("Shader")) {
                    fileName.set("");
                    ImGui.closeCurrentPopup();
                    openPopup = true;
                    popupName = "CreateShaderPopup";
                }
                if (ImGui.menuItem("Material")) {
                    fileName.set("");
                    ImGui.closeCurrentPopup();
                    openPopup = true;
                    popupName = "CreateMaterialPopup";
                }
                ImGui.endMenu();
            }

            if ((contextMenuFile != null && contextMenuFile.isDirectory()) && ImGui.menuItem("Create Asset Pack")) {
                assetPackName = "";
                assetPackVersion = "1.0.0";
                assetPackFolder = contextMenuFile;
                ImGui.closeCurrentPopup();
                openPopup = true;
                popupName = "CreateAssetPackPopup";
            }

            ImGui.beginDisabled(contextMenuFile == null);

            if (ImGui.menuItem("Delete")) {
                if (contextMenuFile.isDirectory()) contextMenuFile.deleteDirectory();
                else contextMenuFile.delete();
                refresh();
            }

            if (ImGui.menuItem("Rename")) {
                renameFile = contextMenuFile;
                focus = true;
            }

            ImGui.endDisabled();

            ImGui.separator();
            if (ImGui.menuItem("Show in Explorer")) {
                Utils.openFolder(currentProject.path);
                refresh();
            }
            if (ImGui.menuItem("Copy Path")) {
                Gdx.app.getClipboard().setContents(currentProject.path.file().getAbsolutePath());
                refresh();
            }
            ImGui.separator();
            if (ImGui.menuItem("Refresh")) {
                refresh();
            }
            isAnyWindowHovered |= ImGui.isWindowHovered();
            ImGui.endPopup();
        }

        if (openPopup) {
            ImGui.openPopup(popupName);
        }

        if (ImGui.beginPopup("CreateAssetPackPopup")) {
            assetPackName = inputText("Name", assetPackName, 150, 300);
            assetPackVersion = inputText("Version", assetPackVersion, 150, 300);
            assetPackDependencies = inputText("Dependencies", assetPackDependencies, 150, 300);
            ImGui.textDisabled("Separate dependencies with commas");
            ImGui.spacing();
            if (ImGui.button("Create", 220, 20)) {
                ImGui.closeCurrentPopup();
                AssetPackUtils.createAssetPack(assetPackFolder, assetPackFolder.parent().child(assetPackName + ".assets"), new AssetPackInfo(assetPackName, Utils.getPackage(assetPackFolder), assetPackVersion, assetPackDependencies.split(",")));
            }
            ImGui.endPopup();
        }

        if (ImGui.beginPopup("CreateScenePopup")) {
            ImGui.inputText("Scene Name", fileName);
            if (ImGui.button("Create")) {

                String path = projectExplorerPath.substring(Math.min(projectExplorerPath.length(), 5)) + fileName.get() + ".scene";

                if (!Gdx.files.absolute(path).exists()) {
                    ImGui.closeCurrentPopup();

                    Scene scene = new Scene();
                    scene.name = fileName.get();
                    scene.rootGameObject = new GameObject();

                    currentProject.scenes.put(path, scene);

                    GameObject camera = new GameObject("Camera");

                    Camera cameraCmp = new Camera();
                    AudioListener listenerCmp = new AudioListener();

                    camera.addComponent(cameraCmp);
                    camera.addComponent(listenerCmp);

                    currentProject.getScene(path).rootGameObject.addGameObject(camera);

                    currentProject.path.child("Assets").child(projectExplorerPath.substring(Math.min(projectExplorerPath.length(), 5))).child(fileName.get() + ".scene").writeString(json.toJson(new SerializableScene(scene)), false);

                    refresh();
                }
            }
            isAnyWindowHovered |= ImGui.isWindowHovered();
            ImGui.endPopup();
        }

        if (ImGui.beginPopup("CreateFolderPopup")) {
            ImGui.inputText("Folder Name", fileName);
            if (ImGui.button("Create")) {
                ImGui.closeCurrentPopup();
                FileHandle path = currentProject.path.child("Assets").child(projectExplorerPath.substring(Math.min(projectExplorerPath.length(), 5))).child(fileName.get());
                path.mkdirs();
                refresh();
            }
            isAnyWindowHovered |= ImGui.isWindowHovered();
            ImGui.endPopup();
        }

        if (ImGui.beginPopup("CreateScriptPopup")) {
            ImGui.inputText("Script Name", fileName);
            if (ImGui.button("Create")) {
                ImGui.closeCurrentPopup();
                String packageName = projectExplorerPath.substring(Math.min(projectExplorerPath.length(), 5)).replaceAll("/", ".");
                FileHandle path = currentProject.path.child("Assets").child(projectExplorerPath.substring(Math.min(projectExplorerPath.length(), 5))).child(fileName.get() + ".java");
                if (!packageName.isBlank())
                    path.writeString("package " + packageName + ";\n\n", false);
                path.writeString("import com.pulsar.api.Component;\n\npublic class " + fileName.get() + " extends Component {\n\n    public void start() {\n\n    }\n\n    public void update() {\n\n    }\n\n}", true);
                refresh();
            }
            isAnyWindowHovered |= ImGui.isWindowHovered();
            ImGui.endPopup();
        }

        if (ImGui.beginPopup("CreateShaderPopup")) {
            ImGui.inputText("Shader Name", fileName);
            if (ImGui.button("Create")) {
                ImGui.closeCurrentPopup();
                FileHandle path = currentProject.path.child("Assets").child(projectExplorerPath.substring(Math.min(projectExplorerPath.length(), 5))).child(fileName.get() + ".sha");
                path.writeString(Gdx.files.internal("default.sha").readString(), false);
                refresh();
            }
            isAnyWindowHovered |= ImGui.isWindowHovered();
            ImGui.endPopup();
        }

        if (ImGui.beginPopup("CreateMaterialPopup")) {
            ImGui.inputText("Material Name", fileName);
            if (ImGui.button("Create")) {
                ImGui.closeCurrentPopup();
                FileHandle path = currentProject.path.child("Assets").child(projectExplorerPath.substring(Math.min(projectExplorerPath.length(), 5))).child(fileName.get() + ".mat");
                path.writeString(new Material().save(), false);
                refresh();
            }
            isAnyWindowHovered |= ImGui.isWindowHovered();
            ImGui.endPopup();
        }

        ImGui.end();

        if (!hovered) contextMenuFile = null;

        console();

        toolbar();

        hierarchy();

        inspector();

        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());

    }

    private String styleName(String name) {
        StringBuilder res = new StringBuilder();
        for(int i = 0; i < name.length(); i++) {
            Character ch = name.charAt(i);
            if(Character.isUpperCase(ch))
                res.append(" ").append(ch);
            else
                res.append(ch);
        }
        String start = res.substring(0,1).toUpperCase();
        String end = res.substring(1);
        return start + end;
    }

}
