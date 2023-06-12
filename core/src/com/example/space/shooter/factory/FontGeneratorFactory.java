package com.example.space.shooter.factory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.example.space.shooter.Const;

public class FontGeneratorFactory {
    public FontGeneratorFactory() {
    }

    public FreeTypeFontGenerator getInstance() {
        return new FreeTypeFontGenerator(Gdx.files.internal(Const.DEFAULT_FONT_FILE_PATH));
    }
}
