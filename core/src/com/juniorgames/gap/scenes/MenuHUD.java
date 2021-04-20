package com.juniorgames.gap.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.juniorgames.gap.GapGame;

public class MenuHUD implements Disposable {
    public Stage stage;
    private Viewport viewport;
    private BitmapFont font;
    private Table table;
    private Texture menuButtonTexture;
    private TextButtonStyle menuButtonStyle;
    private TextButton playButton;
    private TextButton keysButton;
    private TextButton tasksButton;
    private Label gameNameLabel;

    public MenuHUD(GapGame game, AssetManager manager){
        viewport = new FitViewport(game.GAME_WIDTH, game.GAME_WIDTH, new OrthographicCamera());
        stage = new Stage(viewport,game.batch);
        Gdx.input.setInputProcessor(stage);
        font = manager.get("fonts/pacifico-regular.fnt", BitmapFont.class);
        menuButtonTexture = manager.get("menu-btn.png", Texture.class);
        menuButtonStyle = new TextButtonStyle();
        menuButtonStyle.font = font;
        menuButtonStyle.down = new TextureRegionDrawable(menuButtonTexture);
        menuButtonStyle.up = new TextureRegionDrawable(menuButtonTexture);
        menuButtonStyle.checked = new TextureRegionDrawable(menuButtonTexture);
        playButton = new TextButton("PLAY", menuButtonStyle);
        keysButton = new TextButton("KEYS", menuButtonStyle);
        tasksButton = new TextButton("TASKS", menuButtonStyle);

        gameNameLabel = new Label("GAP", new Label.LabelStyle(font, Color.WHITE));
        gameNameLabel.setFontScale(5,5);
        initTable();
    }//constructor

    private void initTable(){
        table = new Table();
        table.center();
        table.setFillParent(true);
        table.add(gameNameLabel).colspan(3).padBottom(170);
        table.row();
        table.add(playButton).pad(20);
        table.add(keysButton).pad(20);
        table.add(tasksButton).pad(20);
        //table.row();
        //table.add(scoreLabel).expandX().padTop(30);
        //table.add(levelNameLabel).expandX().padTop(30);
        //table.add(countdownLabel).expandX().padTop(30);

        stage.addActor(table);

    }

    public void update(float dt){
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
