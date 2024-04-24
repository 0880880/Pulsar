package com.pulsar.api.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pulsar.api.Component;
import com.pulsar.api.Debug;
import com.pulsar.api.graphics.Color;
import com.pulsar.api.math.Vector2;
import com.pulsar.api.math.Vector3;

public class Camera extends Component {

    public Vector2 size = new Vector2(19.2f, 10.8f);

    public Color backgroundColor = new Color(.4f, .4f, .4f, 1);

    private ExtendViewport viewport;

    public void start() {
        viewport = new ExtendViewport(size.x, size.y);
    }

    public void update() {
        Vector2 position = gameObject.transform.position;
        viewport.getCamera().position.set(position.x, position.y, 1);
    }

    public void debugUpdate() {
        Vector2 position = gameObject.transform.position;
        Debug.rectangle(position.x, position.y, size.x, size.y, 0, Color.WHITE, 1);
    }

    public void apply(SpriteBatch batch, int width, int height) {
        viewport.apply();
        viewport.update(width, height);
        batch.setProjectionMatrix(viewport.getCamera().combined);
    }

    private static com.badlogic.gdx.math.Vector2 tmp2 = new com.badlogic.gdx.math.Vector2();
    private static com.badlogic.gdx.math.Vector3 tmp3 = new com.badlogic.gdx.math.Vector3();

    public Vector2 project(Vector2 worldCoords) {
        tmp2.set(viewport.project(tmp2.set(worldCoords.x, worldCoords.y)));
        return worldCoords.set(tmp2.x, tmp2.y);
    }

    public Vector3 project(Vector3 worldCoords) {
        tmp3.set(viewport.project(tmp3.set(worldCoords.x, worldCoords.y, worldCoords.z)));
        return worldCoords.set(tmp3.x, tmp3.y, tmp3.z);
    }

    public Vector2 unproject(Vector2 screenCoords) {
        tmp2.set(viewport.unproject(tmp2.set(screenCoords.x, screenCoords.y)));
        return screenCoords.set(tmp2.x, tmp2.y);
    }

    public Vector3 unproject(Vector3 screenCoords) {
        tmp3.set(viewport.unproject(tmp3.set(screenCoords.x, screenCoords.y, screenCoords.z)));
        return screenCoords.set(tmp3.x, tmp3.y, tmp3.z);
    }

    public Viewport getViewport() {
        return viewport;
    }

}
