package com.example.space.shooter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.example.space.shooter.Const;
import com.example.space.shooter.EnemyShip;
import com.example.space.shooter.Explosion;
import com.example.space.shooter.GameState;
import com.example.space.shooter.HeadsUpDisplay;
import com.example.space.shooter.Laser;
import com.example.space.shooter.PlayerShip;
import com.example.space.shooter.RenderUtil;
import com.example.space.shooter.SpaceShooterGame;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class GameScreen implements Screen {
    // Game
    SpaceShooterGame game;
    // Screen
    private final OrthographicCamera camera;
    private final Viewport viewport;

    // Graphics
    private final SpriteBatch batch;
//    private TextureRegion[] backgrounds;
//    private Texture explosionTexture;

    // Timing
    private float[] backgroundOffsets = {0, 0, 0, 0};
    private float backgroundMaxScrollingSpeed;
    /**
     * 地方战机生成时间
     */
    private float timeBetweenEnemySpawns = 1f;
    private float enemySpawnTimer = 0;

    // World parameter
    private final float WORLD_WIDTH = 72;
    private final float WORLD_HEIGHT = 128;
    private final float TOUCH_MOVEMENT_THRESHOLD = 0.5f;
    private final float PLAYER_SHIP_WIDTH = 8;
    private final float PLAYER_SHIP_HEIGHT = 8;
    private final float ENEMY_SHIP_WIDTH = 8;
    private final float ENEMY_SHIP_HEIGHT = 8;

    // Game objects
    private int score = 0; // 分数
    private PlayerShip playerShip;
    private LinkedList<EnemyShip> enemyShipList;
    private LinkedList<Laser> playerLaserList;
    private LinkedList<Laser> enemyLaserList;
    private LinkedList<Explosion> explosionList;
    private GameState gameState;

    // Heads-Up Display
    private HeadsUpDisplay headsUpDisplay;

    public GameScreen(SpaceShooterGame game) {
        this.game = game;
        this.gameState = GameState.RUNNING;

        // Screen init
        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport.setScreenBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        backgroundMaxScrollingSpeed = Const.WORLD_HEIGHT / 4;

        // set up game objects
        playerShip = new PlayerShip(PLAYER_SHIP_WIDTH, PLAYER_SHIP_HEIGHT, Const.WORLD_WIDTH / 2, Const.WORLD_HEIGHT / 4,
                48, Const.DEFAULT_PLAYER_SHIELD, 0.4f, 4, 50, 0.5f,
                SpaceShooterGame.resourceManager.playerShipTextureRegion,
                SpaceShooterGame.resourceManager.playerShieldTextureRegion,
                SpaceShooterGame.resourceManager.playerLaserTextureRegion
        );

        enemyShipList = new LinkedList<>();


        // laser init
        playerLaserList = new LinkedList<>();
        enemyLaserList = new LinkedList<>();

        // explosion init
        explosionList = new LinkedList<>();

        // headers-up display
        headsUpDisplay = new HeadsUpDisplay(score, playerShip.getShield(), playerShip.getLives());

        // SpriteBatch init
        batch = new SpriteBatch();
    }

    @Override
    public void render(float deltaTime) {
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        viewport.apply(true);

        RenderUtil.clear();

        checkWin();

        System.out.println("game state: " + gameState);
        if (gameState == GameState.RUNNING) {
            renderRunning(deltaTime);
        } else if (gameState == GameState.FINISHED) {
            game.setFailedScreen();
        }
    }

    private void checkWin() {
        if (score >= Const.WIN_GAME_SCORE) {
            gameState = GameState.WIN;
            game.setWinScreen();
        }
    }

    private void renderRunning(float deltaTime) {
        batch.begin();

        // render background
        renderBackground(deltaTime);

        // detect input
        detectInput(deltaTime);

        // play ship update
        playerShip.update(deltaTime);

        // render player ship
        playerShip.draw(batch);

        // random create enemy ship
        randomCreateEnemyShips(deltaTime);

        // render enemy ship
        renderEnemyShips(deltaTime);

        // render laser
        renderLasers(deltaTime);

        // 碰撞检测 detect collisions
        detectCollisions();

        // explosions
        renderExplosions(deltaTime);

        // 绘制顶部标题
        headsUpDisplay.update(score, playerShip.getShield(), playerShip.getLives());
        headsUpDisplay.draw(batch);

        batch.end();
    }


    /**
     * 渲染地方战机
     *
     * @param deltaTime 侦时间
     */
    private void renderEnemyShips(float deltaTime) {
        for (EnemyShip s : enemyShipList) {
            // enemy ship update
            s.update(deltaTime);

            // move enemies
            moveEnemy(s, deltaTime);

            // render enemy ships
            s.draw(batch);
        }
    }

    /**
     * 生成地方战机
     */
    private void randomCreateEnemyShips(float deltaTime) {
        enemySpawnTimer += deltaTime;
        if (enemySpawnTimer >= timeBetweenEnemySpawns) {
            EnemyShip enemyShip = new EnemyShip(ENEMY_SHIP_WIDTH, ENEMY_SHIP_HEIGHT, SpaceShooterGame.random.nextFloat() * (Const.WORLD_WIDTH - 10) + 5,
                    Const.WORLD_HEIGHT - 5, 40, Const.DEFAULT_ENEMY_SHIELD, 0.5f, 3, 40, 1f,
                    SpaceShooterGame.resourceManager.enemyShipTextureRegion,
                    SpaceShooterGame.resourceManager.enemyShieldTextureRegion,
                    SpaceShooterGame.resourceManager.enemyLaserTextureRegion);
            enemyShipList.add(enemyShip);

            enemySpawnTimer -= timeBetweenEnemySpawns;
        }

    }

    /**
     * 地方战机移动
     *
     * @param enemyShip /
     * @param deltaTime /
     */
    private void moveEnemy(EnemyShip enemyShip, float deltaTime) {
        float leftLimit = enemyShip.rectangle.x;
        float rightLimit = WORLD_WIDTH - enemyShip.rectangle.x - enemyShip.rectangle.width;
        float upLimit = WORLD_HEIGHT - enemyShip.rectangle.y - enemyShip.rectangle.height;
        float downLimit = enemyShip.rectangle.y;


        float shipXRealMovement = enemyShip.getDirectionVector().x * enemyShip.movementSpeed * deltaTime;
        float shipYRealMovement = enemyShip.getDirectionVector().y * enemyShip.movementSpeed * deltaTime;

        if (shipXRealMovement > 0) {
            shipXRealMovement = Math.min(shipXRealMovement, rightLimit);
        } else {
            shipXRealMovement = Math.max(shipXRealMovement, -leftLimit);
        }

        if (shipYRealMovement > 0) {
            shipYRealMovement = Math.min(shipYRealMovement, upLimit);
        } else {
            shipYRealMovement = Math.max(shipYRealMovement, -downLimit);
        }

        enemyShip.translate(shipXRealMovement, shipYRealMovement);
    }

    /**
     * 监听输入事件
     *
     * @param deltaTime /
     */
    private void detectInput(float deltaTime) {
        float leftLimit = playerShip.rectangle.x;
        float rightLimit = WORLD_WIDTH - playerShip.rectangle.x - playerShip.rectangle.width;
        float upLimit = WORLD_HEIGHT - playerShip.rectangle.y - playerShip.rectangle.height;
        float downLimit = playerShip.rectangle.y;

        // keyboard input
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && leftLimit > 0) {
            float xChange = playerShip.movementSpeed * deltaTime;
            xChange = Math.min(xChange, leftLimit);
            playerShip.translate(-xChange, 0f);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && rightLimit > 0) {
            float xChange = playerShip.movementSpeed * deltaTime;
            xChange = Math.min(xChange, rightLimit);
            playerShip.translate(xChange, 0f);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) && upLimit > 0) {
            float yChange = playerShip.movementSpeed * deltaTime;
            yChange = Math.min(yChange, upLimit);
            playerShip.translate(0f, yChange);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && downLimit > 0) {
            float yChange = playerShip.movementSpeed * deltaTime;
            yChange = Math.min(yChange, downLimit);
            playerShip.translate(0f, -yChange);
        }

        // touch input (also mouse)
        if (Gdx.input.isTouched()) {
            // get the screen position of the touch
            float xTouchPixels = Gdx.input.getX();
            float yTouchPixels = Gdx.input.getY();

            // convert the screen position to world position
            Vector2 touchPoint = new Vector2(xTouchPixels, yTouchPixels);
            Vector2 worldTouchPoint = viewport.unproject(touchPoint);

            // calculate the x and y differences
            Vector2 playerShipCenter = new Vector2(playerShip.rectangle.x + playerShip.rectangle.width / 2, playerShip.rectangle.y + playerShip.rectangle.height / 2);
            float movementDistance = worldTouchPoint.dst(playerShipCenter);
            if (movementDistance > TOUCH_MOVEMENT_THRESHOLD) {
                float xTouchDifference = worldTouchPoint.x - playerShipCenter.x;
                float yTouchDifference = worldTouchPoint.y - playerShipCenter.y;

                // scale to the maximum speed of ship
                float shipRealMoveDistance = playerShip.movementSpeed * deltaTime;

                float shipXRealMovement = (shipRealMoveDistance * xTouchDifference) / movementDistance;
                float shipYRealMovement = (shipRealMoveDistance * yTouchDifference) / movementDistance;

                if (shipXRealMovement > 0) {
                    shipXRealMovement = Math.min(shipXRealMovement, rightLimit);
                } else {
                    shipXRealMovement = Math.max(shipXRealMovement, -leftLimit);
                }

                if (shipYRealMovement > 0) {
                    shipYRealMovement = Math.min(shipYRealMovement, upLimit);
                } else {
                    shipYRealMovement = Math.max(shipYRealMovement, -downLimit);
                }

                playerShip.translate(shipXRealMovement, shipYRealMovement);
            }


        }
    }

    /**
     * 检测碰撞
     */
    private void detectCollisions() {
        // 检测玩家飞船和敌人激光碰撞
        Iterator<Laser> enemyLaserIter = enemyLaserList.iterator();
        while (enemyLaserIter.hasNext()) {
            Laser laser = enemyLaserIter.next();
            if (playerShip.overlaps(laser.getLaserRectangle())) {
                boolean destroyed = playerShip.hitAndCheckDestroyed(laser);
                if (destroyed) {
                    this.explosionList.add(new Explosion(SpaceShooterGame.resourceManager.explosionTexture, new Rectangle(playerShip.rectangle), 0.7f));
                    boolean finished = this.playerShip.killAndCheckDied();
                    if (finished) {
                        gameState = GameState.FINISHED;
                    }
                }
                enemyLaserIter.remove();
            }
        }

        // 检测敌人非常和玩家激光碰撞
        Iterator<Laser> playLaserIter = playerLaserList.iterator();
        while (playLaserIter.hasNext()) {
            Laser laser = playLaserIter.next();
            ListIterator<EnemyShip> enemyShipListIterator = enemyShipList.listIterator();
            while (enemyShipListIterator.hasNext()) {
                EnemyShip enemyShip = enemyShipListIterator.next();
                if (enemyShip.overlaps(laser.getLaserRectangle())) {
                    // enemy ship is destroyed
                    boolean enemyShipIsDestroyed = enemyShip.hitAndCheckDestroyed(laser);
                    if (enemyShipIsDestroyed) {
                        explosionList.add(new Explosion(SpaceShooterGame.resourceManager.explosionTexture, new Rectangle(enemyShip.rectangle), 0.7f));
                        enemyShipListIterator.remove();
                        score += enemyShip.getScore();
                    }
                    playLaserIter.remove();
                }
            }

        }
    }

    /**
     * render background
     *
     * @param deltaTime /
     */
    public void renderBackground(float deltaTime) {
        backgroundOffsets[0] += deltaTime * backgroundMaxScrollingSpeed / 8;
        backgroundOffsets[1] += deltaTime * backgroundMaxScrollingSpeed / 4;
        backgroundOffsets[2] += deltaTime * backgroundMaxScrollingSpeed / 2;
        backgroundOffsets[3] += deltaTime * backgroundMaxScrollingSpeed;


        // render
        TextureRegion[] backgrounds = SpaceShooterGame.resourceManager.backgrounds;
        for (int layer = 0; layer < backgrounds.length; layer++) {
            // reset zero if offset is big than the world height
            if (backgroundOffsets[layer] > WORLD_HEIGHT) {
                backgroundOffsets[layer] = 0;
            }

            // render background
            batch.draw(backgrounds[layer], 0, -backgroundOffsets[layer], WORLD_WIDTH, WORLD_HEIGHT);
            batch.draw(backgrounds[layer], 0, -backgroundOffsets[layer] + WORLD_HEIGHT, WORLD_WIDTH, WORLD_HEIGHT);
        }
    }

    public void renderLasers(float deltaTime) {
        // create new lasers
        if (playerShip.canFireLaser()) {
            Laser[] lasers = playerShip.fireLasers();
            playerLaserList.addAll(Arrays.asList(lasers));
        }

        for (EnemyShip enemyShip : enemyShipList) {
            if (enemyShip.canFireLaser()) {
                Laser[] lasers = enemyShip.fireLasers();
                enemyLaserList.addAll(Arrays.asList(lasers));
            }
        }

        // draw lasers
        // remove old lasers
        Iterator<Laser> playLaserIter = playerLaserList.iterator();
        while (playLaserIter.hasNext()) {
            Laser laser = playLaserIter.next();
            laser.draw(batch);

            laser.boundingBox.y += laser.movementSpeed * deltaTime;

            if (laser.boundingBox.y > WORLD_HEIGHT) {
                playLaserIter.remove();
            }
        }

        Iterator<Laser> enemyLaserIter = enemyLaserList.iterator();
        while (enemyLaserIter.hasNext()) {
            Laser laser = enemyLaserIter.next();
            laser.draw(batch);

            laser.boundingBox.y -= laser.movementSpeed * deltaTime;

            if (laser.boundingBox.y + laser.boundingBox.height > WORLD_HEIGHT) {
                enemyLaserIter.remove();
            }
        }
    }

    public void renderExplosions(float deltaTime) {
        ListIterator<Explosion> explosionListIterator = explosionList.listIterator();
        while (explosionListIterator.hasNext()) {
            Explosion explosion = explosionListIterator.next();
            explosion.update(deltaTime);
            explosion.draw(batch);
            // explosion finished 爆炸结束
            if (explosion.isFinished()) {
                explosionListIterator.remove();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void show() {

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

    public int getScore() {
        return this.score;
    }
}
