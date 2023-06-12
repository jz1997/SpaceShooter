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

public class FailedScreen implements Screen {
    private Camera camera;
    private SpaceShooterGame game;
    private SpriteBatch batch;
    private Viewport viewport;

    private BitmapFont font;

    public FailedScreen(SpaceShooterGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new StretchViewport(Const.WORLD_WIDTH, Const.WORLD_HEIGHT, camera);
        viewport.setScreenBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch = new SpriteBatch();
        font = generateFont();
    }

    private BitmapFont generateFont() {
        final BitmapFont font;
        FreeTypeFontGenerator generator = SpaceShooterGame.fontGeneratorFactory.getInstance();
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 120;
        parameter.color = new Color(1, 1, 1, 0.3f);
        parameter.borderWidth = 3f;
        parameter.borderColor = new Color(0, 0, 0, 0.3f);
        parameter.spaceX = 4;
        font = generator.generateFont(parameter);
        font.getData().setScale(0.08f);

        generator.dispose();
        return font;
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

        font.draw(batch, "GAME OVER", 0, Const.WORLD_HEIGHT * 2 / 3, Const.WORLD_WIDTH, Align.center, false);

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
