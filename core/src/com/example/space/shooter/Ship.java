package com.example.space.shooter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public abstract class Ship {
    // 特征属性 ship characteristics
    public float movementSpeed; // world units per second
    protected int shield;

    // 坐标和尺寸 position & dimension
    public Rectangle rectangle;

    // 激光武器 laser information
    float laserWidth, laserHeight;
    float laserMovementSpeed;
    float timeBetweenShots;
    float timeSinceLastShot = 0;

    // 图形信息 graphics
    TextureRegion shipTexture, shieldTexture, laserTexture;

    public Ship(float width, float height,
                float xCenter, float yCenter, // the center position of ship
                float movementSpeed, int shield,
                float laserWidth, float laserHeight, float laserMovementSpeed, float timeBetweenShots,
                TextureRegion shipTexture, TextureRegion shieldTexture, TextureRegion laserTexture
    ) {
        this.movementSpeed = movementSpeed;
        this.shield = shield;
        this.laserWidth = laserWidth;
        this.laserHeight = laserHeight;
        this.laserMovementSpeed = laserMovementSpeed;
        this.timeBetweenShots = timeBetweenShots;
        this.shipTexture = shipTexture;
        this.shieldTexture = shieldTexture;
        this.laserTexture = laserTexture;
        rectangle = new Rectangle(xCenter - width / 2, yCenter - height / 2, width, height);
    }

    public void update(float deltaTime) {
        timeSinceLastShot += deltaTime;
    }

    public boolean canFireLaser() {
        return timeSinceLastShot - timeBetweenShots >= 0;
    }

    public abstract Laser[] fireLasers();

    public void draw(Batch batch) {
        // drawing ship
        batch.draw(shipTexture, rectangle.x, rectangle.y, rectangle.width, rectangle.height);

        // draw shield
        if (hasShield()) {
            batch.draw(shieldTexture, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        }
    }

    /**
     * 检测是否碰撞
     *
     * @param otherRectangle /
     * @return /
     */
    public boolean overlaps(Rectangle otherRectangle) {
        return rectangle.overlaps(otherRectangle);
    }

    /**
     * 非常被击中
     *
     * @param laser {@link Laser} 激光
     */
    public boolean hitAndCheckDestroyed(Laser laser) {
        if (shield > 0) {
            shield--;
            return false;
        }
        return true;
    }

    public void translate(float xChange, float yChange) {
        rectangle.setPosition(rectangle.x + xChange, rectangle.y + yChange);
    }

    public int getShield() {
        return shield;
    }

    public void setShield(int shield) {
        this.shield = shield;
    }

    public boolean hasShield() {
        return this.shield > 0;
    }
}
