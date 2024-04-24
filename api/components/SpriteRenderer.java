package com.pulsar.api.components;

import com.pulsar.api.Component;
import com.pulsar.api.Renderer;
import com.pulsar.api.graphics.Color;
import com.pulsar.api.graphics.Material;
import com.pulsar.api.graphics.Texture;

public class SpriteRenderer extends Component {

 public Texture texture = new Texture();
 public Color tint = new Color(Color.WHITE);
 public Material material;

 public void update() {}

 public void debugUpdate() {}

}
