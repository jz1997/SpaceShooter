package com.example.space.shooter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.example.space.shooter.Const;
import com.example.space.shooter.RenderUtil;
import com.example.space.shooter.SpaceShooterGame;

import java.util.Locale;

public class WinScreen implements Screen {
    private Camera camera;
    private SpaceShooterGame game;
    private SpriteBatch batch;
    private Viewport viewport;

    private BitmapFont font;
    private BitmapFont scoreFont;
    private BitmapFont descFont;

    public WinScreen(SpaceShooterGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new StretchViewport(Const.WORLD_WIDTH, Const.WORLD_HEIGHT, camera);
        viewport.setScreenBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch = new SpriteBatch();
        generateFont();
    }

    private void generateFont() {
        FreeTypeFontGenerator generator = SpaceShooterGame.fontGeneratorFactory.getInstance();

        // Title Font
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 120;
        parameter.color = new Color(1, 1, 1, 0.3f);
        parameter.borderWidth = 3f;
        parameter.borderColor = new Color(0, 0, 0, 0.3f);
        parameter.spaceX = 4;
        font = generator.generateFont(parameter);
        font.getData().setScale(0.08f);

        // Description font
        FreeTypeFontGenerator.FreeTypeFontParameter scoreFontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        scoreFontParameter.size = 100;
        scoreFontParameter.color = new Color(1, 1, 1, 0.3f);
        scoreFontParameter.borderWidth = 3f;
        scoreFontParameter.borderColor = new Color(0, 0, 0, 0.3f);
        scoreFontParameter.spaceX = 2;
        scoreFont = generator.generateFont(scoreFontParameter);
        scoreFont.getData().setScale(0.08f);

        // desc font
        FreeTypeFontGenerator.FreeTypeFontParameter descFontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        descFontParameter.size = 100;
        descFontParameter.color = new Color(1, 1, 1, 0.3f);
        descFontParameter.borderWidth = 3f;
        descFontParameter.borderColor = new Color(0, 0, 0, 0.3f);
        descFontParameter.spaceX = 2;
        descFont = generator.generateFont(descFontParameter);
        descFont.getData().setScale(0.04f);


        generator.dispose();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        camera.update();
        viewport.apply(true);
        batch.setProjectionMatrix(camera.combined);

        RenderUtil.clear();

        // begin draw
        batch.begin();

        // draw you win title
        float firstY = Const.WORLD_HEIGHT * 2 / 3;
        font.draw(batch, "YOU WIN", 0, firstY, Const.WORLD_WIDTH, Align.center, false);

        // draw score
        float secondY = firstY - font.getCapHeight() * 2;
        scoreFont.draw(batch, String.format(Locale.getDefault(), "Score: %d", game.getScore()), 0,
                secondY, Const.WORLD_WIDTH, Align.center, false);

        // draw description
        float thirdY = secondY - font.getCapHeight() * 2;
        descFont.draw(batch, "Press R to restart the game", 0, thirdY, Const.WORLD_WIDTH, Align.center, false);


        batch.end();
        // end draw
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
