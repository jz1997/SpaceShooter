package com.example.space.shooter;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlayerShip extends Ship {

    private int lives;

    public PlayerShip(float width, float height,
                      float xCenter, float yCenter,
                      float movementSpeed, int shield,
                      float laserWidth, float laserHeight,
                      float laserMovementSpeed, float timeBetweenShots,
                      TextureRegion shipTexture, TextureRegion shieldTexture,
                      TextureRegion laserTexture) {
        super(width, height,
                xCenter, yCenter,
                movementSpeed, shield,
                laserWidth, laserHeight,
                laserMovementSpeed,
                timeBetweenShots, shipTexture,
                shieldTexture, laserTexture
        );
        this.lives = Const.DEFAULT_PLAYER_LIVES;
    }

    @Override
    public Laser[] fireLasers() {
        Laser[] lasers = new Laser[2];
        lasers[0] = new Laser(rectangle.x + rectangle.width * 0.03f, rectangle.y + rectangle.height * 0.45f,
                laserWidth, laserHeight, laserMovementSpeed, laserTexture);
        lasers[1] = new Laser(rectangle.x + rectangle.width * 0.97f, rectangle.y + rectangle.height * 0.45f,
                laserWidth, laserHeight, laserMovementSpeed, laserTexture);
//        Laser laser = new Laser(rectangle.x + rectangle.width / 2,
//                rectangle.y + rectangle.height, laserWidth, laserHeight, laserMovementSpeed, laserTexture);

        timeSinceLastShot = 0;
        return lasers;
    }

    public int getLives() {
        return lives;
    }

    public boolean killAndCheckDied() {
        if (this.lives == 0) {
            return true;
        }
        this.lives--;
        this.shield = 3;
        return this.lives < 0;
    }
}
