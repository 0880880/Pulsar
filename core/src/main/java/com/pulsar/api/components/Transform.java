package com.pulsar.api.components;

import com.pulsar.api.Component;
import com.pulsar.api.math.Vector2;

public class Transform extends Component {

    public Vector2 position = new Vector2();
    public Vector2 localPosition = new Vector2();
    public Vector2 scale = new Vector2();
    public float rotation = 0;

}
