package com.example.space.shooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ResourceManager {

    TextureAtlas textureAtlas;
    public TextureRegion[] backgrounds;
    public TextureRegion playerShipTextureRegion;
    public TextureRegion playerShieldTextureRegion;
    public TextureRegion playerLaserTextureRegion;
    public TextureRegion enemyShipTextureRegion;
    public TextureRegion enemyShieldTextureRegion;
    public TextureRegion enemyLaserTextureRegion;
    public Texture explosionTexture;

    public ResourceManager() {
        textureAtlas = new TextureAtlas(Const.IMAGES_ATLAS);
        // set up background texture region
        backgrounds = new TextureRegion[4];
        backgrounds[0] = textureAtlas.findRegion("Starscape00");
        backgrounds[1] = textureAtlas.findRegion("Starscape01");
        backgrounds[2] = textureAtlas.findRegion("Starscape02");
        backgrounds[3] = textureAtlas.findRegion("Starscape03");


        // set up texture
        playerShipTextureRegion = textureAtlas.findRegion("playerShip2_blue");
        playerShieldTextureRegion = textureAtlas.findRegion("shield2");
        playerLaserTextureRegion = textureAtlas.findRegion("laserBlue03");
        enemyShipTextureRegion = textureAtlas.findRegion("enemyRed3");
        enemyShieldTextureRegion = textureAtlas.findRegion("shield1");
        enemyShieldTextureRegion.flip(false, true);
        enemyLaserTextureRegion = textureAtlas.findRegion("laserRed03");
        explosionTexture = new Texture(Gdx.files.internal("explosion.png"));
    }
}
