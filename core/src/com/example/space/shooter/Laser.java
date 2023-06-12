package com.example.space.shooter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Laser {
    // 位置和尺寸 position & dimension
    public Rectangle boundingBox;

    // 激光物理特性 laser physical characteristics
    public float movementSpeed; // world units per second

    TextureRegion textureRegion;


    public Laser(float xCenter, float yPosition,
                 float width, float height,
                 float movementSpeed, TextureRegion textureRegion) {
        this.boundingBox = new Rectangle(xCenter - width / 2, yPosition, width, height);

        this.movementSpeed = movementSpeed;
        this.textureRegion = textureRegion;
    }

    public void draw(Batch batch) {
        batch.draw(textureRegion, boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
    }

    public Rectangle getLaserRectangle() {
        return boundingBox;
    }
}
