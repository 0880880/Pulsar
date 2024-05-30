package com.pulsar.api.components;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.pulsar.api.Component;
import com.pulsar.api.FileHandle;
import com.pulsar.api.graphics.Color;
import com.pulsar.api.math.Vector2;

public class Text extends Component {

    public static Batch batch;

    public FileHandle fontFile = new FileHandle("ttf");

    public String text = "Hello, World!";
    public int fontSize = 18;
    public Color color = new Color(Color.WHITE);
    public float scaleX = 1;
    public float scaleY = 1;
    public String characters = "\u0000ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890\"!`?'.,;:()[]{}<>|/@\\^$€-%+=#_&~*\u007f\u0080\u0081\u0082\u0083\u0084\u0085\u0086\u0087\u0088\u0089\u008a\u008b\u008c\u008d\u008e\u008f\u0090\u0091\u0092\u0093\u0094\u0095\u0096\u0097\u0098\u0099\u009a\u009b\u009c\u009d\u009e\u009f ¡¢£¤¥¦§¨©ª«¬\u00ad®¯°±²³´µ¶·¸¹º»¼½¾¿ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõö÷øùúûüýþÿ";
    public boolean kerning = true;
    public boolean mono = false;
    public boolean incremental;
    public boolean flip = false;

    public float borderWidth = 0;
    public Color borderColor = new Color(Color.BLACK);
    public float borderGamma = 1.8f;
    public boolean borderStraight = false;

    public Color shadowColor = new Color(0, 0, 0, .75f);
    public int shadowOffsetX = 0;
    public int shadowOffsetY = 0;

    public int faceIndex = 0;

    private String oldFontFile;
    private BitmapFont font;
    private Transform transform;

    public void updateFont() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile.gdxFile, faceIndex);
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = fontSize;
        parameter.color = com.badlogic.gdx.graphics.Color.WHITE;
        parameter.characters = characters;

        parameter.flip = flip;
        parameter.incremental = incremental;
        parameter.kerning = kerning;
        parameter.mono = mono;

        parameter.borderColor.set(borderColor.r, borderColor.g, borderColor.b, borderColor.a);
        parameter.borderWidth = borderWidth;
        parameter.borderGamma = borderGamma;
        parameter.borderStraight = borderStraight;

        parameter.shadowColor.set(shadowColor.r, shadowColor.g, shadowColor.b, shadowColor.a);
        parameter.shadowOffsetX = shadowOffsetX;
        parameter.shadowOffsetY = shadowOffsetY;

        font = generator.generateFont(parameter);
        font.setUseIntegerPositions(false);
        oldFontFile = fontFile.path();
    }

    @Override
    public void start() {
        updateFont();
        transform = this.gameObject.getComponent(Transform.class);
    }

    @Override
    public void update() {
        if (!oldFontFile.equals(fontFile.path())) updateFont();
        if (batch != null && !oldFontFile.isBlank()) {
            font.getData().setScale(1/(float)fontSize);
            font.getData().scaleX *= scaleX;
            font.getData().scaleY *= scaleY;

            font.setColor(color.r, color.g, color.b, color.a);
            font.draw(batch, text, transform.position.x, transform.position.y);
        }
    }

    public float getTextWidth(String text) {
        return new GlyphLayout(font, text).width;
    }

    public float getTextHeight(String text) {
        return new GlyphLayout(font, text).height;
    }

    public Vector2 getTextSize(String text) {
        GlyphLayout layout = new GlyphLayout(font, text);
        return new Vector2(layout.width, layout.height);
    }

}
