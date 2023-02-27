package com.league.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.league.game.Handlers.NetworkHandler;
import com.league.game.models.AbilityEntity;
import com.league.game.models.HeroGameEntity;
import com.league.game.screens.GameRenderScreen;
import com.league.game.screens.HeroSelectionScreen;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.*;

@Getter
@Slf4j
@SpringBootApplication()
public class LeagueOfHorrors extends Game {

    public final int VIEW_PORT_WIDTH = 1000;
    public final int VIEW_PORT_HEIGHT = 500;
    public SpriteBatch spriteBatch;
    public NetworkHandler networkHandler;
    public AssetManager assetManager;
    public Boolean isHeroCreated = false;
    public String selectedHeroName;

    public LinkedList<Map<String, HeroGameEntity>> heroStateQueue;

    public Map<String, Map<String, Animation<TextureRegion>>> animationMap;
    public Map<String, HeroGameEntity> heroes;
    public Map<String, List<AbilityEntity>> abilityEntityMap;

    @Override
    @SuppressWarnings("unchecked")
    public void create() {
        SpringApplication.run(LeagueOfHorrors.class);
        heroStateQueue = new LinkedList<>();
        spriteBatch = new SpriteBatch();
		ApplicationContext ctx = new AnnotationConfigApplicationContext(LeagueOfHorrors.class);
        assetManager = (AssetManager) ctx.getBean("assetManager");
        abilityEntityMap = (Map<String, List<AbilityEntity>>) ctx.getBean("abilityEntityMap");
        animationMap = (Map<String, Map<String, Animation<TextureRegion>>>) ctx.getBean("animationMap");
        heroes = new HashMap<String, HeroGameEntity>();
        networkHandler = new NetworkHandler(this);
        networkHandler.getAndConfigureSocket();
        if (networkHandler.getSocket() != null) {
            setScreen(new HeroSelectionScreen(this));
        } else {
            log.error("Enable to create a socket.");
        }
    }

    @Override
    public void dispose() {
       spriteBatch.dispose();
       assetManager.dispose();
    }
}