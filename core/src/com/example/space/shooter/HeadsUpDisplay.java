package com.example.space.shooter;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Align;

import java.util.Locale;

/**
 * 头部展示
 * Score    Shield      Lives
 * 000001    03           03
 */
public class HeadsUpDisplay {

    private final BitmapFont font;
    private int score;
    private int shield;
    private int lives;

    private float rowMargin;
    private float colMargin;
    private float firstRowY;
    private float secondRowY;
    private float firstColX;
    private float secondColX;
    private float thirdColX;
    private float colWidth;

    public HeadsUpDisplay(int score, int shield, int lives) {

        // 生成字体
        font = generateFont();

        this.score = score;
        this.shield = shield;
        this.lives = lives;

        // 初始化坐标
        rowMargin = font.getCapHeight() / 2;
        colMargin = rowMargin;
        firstColX = colMargin;
        secondColX = Const.WORLD_WIDTH / 3;
        thirdColX = Const.WORLD_WIDTH * 2 / 3 - colMargin;
        colWidth = Const.WORLD_WIDTH / 3;
        firstRowY = Const.WORLD_HEIGHT - rowMargin;
        secondRowY = firstRowY - rowMargin - font.getCapHeight();
    }

    private BitmapFont generateFont() {
        final BitmapFont font;
        FreeTypeFontGenerator generator = SpaceShooterGame.fontGeneratorFactory.getInstance();
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 100;
        parameter.color = new Color(1, 1, 1, 0.3f);
        parameter.borderWidth = 3f;
        parameter.borderColor = new Color(0, 0, 0, 0.3f);

        font = generator.generateFont(parameter);
        font.getData().setScale(0.06f);

        generator.dispose();
        return font;
    }


    public void draw(Batch batch) {
        // 绘制标签栏
        font.draw(batch, "Score", firstColX, firstRowY, colWidth, Align.left, false);
        font.draw(batch, "Shield", secondColX, firstRowY, colWidth, Align.center, false);
        font.draw(batch, "Lives", thirdColX, firstRowY, colWidth, Align.right, false);

        // 绘制值
        font.draw(batch, String.format(Locale.getDefault(), "%06d", score), firstColX, secondRowY, colWidth, Align.left, false);
        font.draw(batch, String.format(Locale.getDefault(), "%02d", shield), secondColX, secondRowY, colWidth, Align.center, false);
        font.draw(batch, String.format(Locale.getDefault(), "%02d", lives), thirdColX, secondRowY, colWidth, Align.right, false);
    }

    public void update(int score, int shield, int lives) {
        this.score = score;
        this.shield = shield;
        this.lives = lives;
    }
}
