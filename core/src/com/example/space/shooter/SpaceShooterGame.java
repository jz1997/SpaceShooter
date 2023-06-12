package com.example.space.shooter;

import com.badlogic.gdx.Game;
import com.example.space.shooter.factory.FontGeneratorFactory;
import com.example.space.shooter.screens.FailedScreen;
import com.example.space.shooter.screens.GameScreen;
import com.example.space.shooter.screens.WinScreen;

import java.util.Random;

public class SpaceShooterGame extends Game {

    public static GameScreen gameScreen;
    public static FailedScreen failedScreen;
    public static WinScreen winScreen;
    public static ResourceManager resourceManager;

    public static FontGeneratorFactory fontGeneratorFactory;
    public static Random random = new Random();

    public void setFailedScreen() {
        setScreen(failedScreen);
    }

    public void setWinScreen() {
        setScreen(winScreen);
    }

    @Override
    public void create() {
        fontGeneratorFactory = new FontGeneratorFactory();
        resourceManager = new ResourceManager();
        gameScreen = new GameScreen(this);
        failedScreen = new FailedScreen(this);
        winScreen = new WinScreen(this);
        setScreen(winScreen);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        gameScreen.dispose();
    }

    @Override
    public void resize(int width, int height) {
        gameScreen.resize(width, height);
    }

    public int getScore() {
        return gameScreen.getScore();
    }
}
