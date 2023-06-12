package com.example.space.shooter;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;


public class Explosion {
    private Animation<TextureRegion> explosionAnimation;
    private float explosionTimer = 0;
    private Rectangle boundingBox;

    public Explosion(Texture texture, Rectangle boundingBox, float totalAnimationTime) {
        this.boundingBox = boundingBox;

        // split texture region
        TextureRegion[][] textureRegions2D = TextureRegion.split(texture, 64, 64);

        // convert to 1D array
        int index = 0;
        TextureRegion[] textureRegion1D = new TextureRegion[16];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                textureRegion1D[index++] = textureRegions2D[i][j];
            }
        }

        explosionAnimation = new Animation<>(totalAnimationTime / 16, textureRegion1D);
        explosionTimer = 0;
    }

    public void update(float deltaTime) {
        this.explosionTimer += deltaTime;
    }

    public void draw(Batch batch) {
        batch.draw(explosionAnimation.getKeyFrame(this.explosionTimer),
                this.boundingBox.x, this.boundingBox.y,
                this.boundingBox.width, this.boundingBox.height);
    }

    public boolean isFinished() {
        return this.explosionAnimation.isAnimationFinished(this.explosionTimer);
    }
}
