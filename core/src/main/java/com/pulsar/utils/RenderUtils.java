package com.pulsar.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.pulsar.Statics;
import com.pulsar.api.Debug;

import static com.pulsar.Statics.*;

public class RenderUtils {

    public static void renderArrow(float originX, float originY, float endX, float endY, float triWidth, float triHeight, Color color, float lineWidth) {
        Statics.drawer.line(originX, originY, endX, endY, color, lineWidth);
        float dir = MathUtils.atan2(endY - originY, endX - originX) - MathUtils.HALF_PI;
        float bax = -triWidth / 2f;
        float bay = 0;
        float bbx = 0;
        float bcx = triWidth / 2f;
        float bcy = 0;

        float ax = bax * MathUtils.cos(dir) - bay * MathUtils.sin(dir);
        float ay = bax * MathUtils.sin(dir) + bay * MathUtils.cos(dir);
        float bx = bbx * MathUtils.cos(dir) - triHeight * MathUtils.sin(dir);
        float by = bbx * MathUtils.sin(dir) + triHeight * MathUtils.cos(dir);
        float cx = bcx * MathUtils.cos(dir) - bcy * MathUtils.sin(dir);
        float cy = bcx * MathUtils.sin(dir) + bcy * MathUtils.cos(dir);

        drawer.filledTriangle(ax+endX,ay+endY,bx+endX,by+endY,cx+endX,cy+endY,color);
    }

    static Vector2 tmp0 = new Vector2();
    static Vector2 tmp1 = new Vector2();
    static float debugScale;

    public static void renderGrid(OrthographicCamera gameCamera) {

        editorViewport.unproject(tmp0.set(0,0));
        editorViewport.unproject(tmp1.set(1,1));
        debugScale = Math.min(tmp1.x, tmp1.y) - Math.min(tmp0.x, tmp0.y) + .01f;
        Debug.debugScale = debugScale;

        float gridSize = 1;

        if (gameCamera.zoom > 3) gridSize = 2.5f;
        if (gameCamera.zoom > 5) gridSize = 5;

        if (gameCamera.zoom < 10) {
            float offsetX = MathUtils.round(gameCamera.position.x / gridSize) * gridSize;
            for (int i = (int) (-15 * gameCamera.zoom); i < 10 * gameCamera.zoom; i++) {
                drawer.line(gridSize * i + offsetX, -10000 * gameCamera.zoom + gameCamera.position.y,gridSize * i + offsetX,1000 * gameCamera.zoom + gameCamera.position.y,Color.DARK_GRAY, debugScale);
            }
            float offsetY = MathUtils.round(gameCamera.position.y / gridSize) * gridSize;
            for (int i = (int) (-15 * gameCamera.zoom); i < 10 * gameCamera.zoom; i++) {
                drawer.line(-10000 * gameCamera.zoom + gameCamera.position.x, gridSize * i + offsetY, 1000 * gameCamera.zoom + gameCamera.position.x,gridSize * i + offsetY,Color.DARK_GRAY, debugScale);
            }
        }

        drawer.line(-10000 * gameCamera.zoom + gameCamera.position.x,0,1000 * gameCamera.zoom + gameCamera.position.x,0,Color.LIGHT_GRAY, debugScale);
        drawer.line(0, -10000 * gameCamera.zoom + gameCamera.position.y,0,1000 * gameCamera.zoom + gameCamera.position.y,Color.LIGHT_GRAY, debugScale);

    }

}
