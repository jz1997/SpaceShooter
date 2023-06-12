package com.example.space.shooter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class EnemyShip extends Ship {

    private int score;

    /**
     * 方向向量
     */
    private Vector2 directionVector;
    /**
     * 距离上一次方向转换的时间
     */
    private float timeSinceLastDirectionChange = 0;
    /**
     * 方向转化频率
     */
    private float directionChangeFrequency = 0.75f;


    public EnemyShip(float width, float height, float xCenter, float yCenter,
                     float movementSpeed, int shield, float laserWidth, float laserHeight,
                     float laserMovementSpeed, float timeBetweenShots,
                     TextureRegion shipTexture, TextureRegion shieldTexture, TextureRegion laserTexture) {
        super(width, height, xCenter, yCenter, movementSpeed, shield, laserWidth, laserHeight,
                laserMovementSpeed, timeBetweenShots, shipTexture, shieldTexture, laserTexture);

        // 方向朝下
        directionVector = new Vector2(0, -1);

        // 初始化分数
        score = 100;
    }

    public Vector2 getDirectionVector() {
        return directionVector;
    }

    /**
     * 随机移动方向
     */
    private void randomizeDirectionVector() {
        double bearing = SpaceShooterGame.random.nextDouble() * 2 * Math.PI;
        directionVector.x = (float) Math.sin(bearing);
        directionVector.y = (float) Math.cos(bearing);
    }

    public int getScore() {
        return score;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        timeSinceLastDirectionChange += deltaTime;
        if (timeSinceLastDirectionChange >= directionChangeFrequency) {
            randomizeDirectionVector();
            timeSinceLastDirectionChange -= directionChangeFrequency;
        }
    }

    @Override
    public Laser[] fireLasers() {
//        Laser[] lasers = new Laser[2];
//        lasers[0] = new Laser(rectangle.x + rectangle.width * 0.18f, rectangle.y - laserHeight,
//                laserWidth, laserHeight, laserMovementSpeed, laserTexture);
//        lasers[1] = new Laser(rectangle.x + rectangle.width * 0.82f, rectangle.y - laserHeight,
//                laserWidth, laserHeight, laserMovementSpeed, laserTexture);
        Laser laser = new Laser(rectangle.x + rectangle.width / 2, rectangle.y, laserWidth,
                laserHeight, laserMovementSpeed, laserTexture);

        timeSinceLastShot = 0;
        return new Laser[]{laser};
    }

    @Override
    public void draw(Batch batch) {
        // drawing ship
        batch.draw(shipTexture, rectangle.x, rectangle.y, rectangle.width, rectangle.height);

        // draw shield
        if (hasShield()) {
            batch.draw(shieldTexture, rectangle.x, rectangle.y - rectangle.height * 0.1f, rectangle.width, rectangle.height);
        }
    }
}
