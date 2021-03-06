package es.eucm.cytochallenge.view.transitions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;

public class Fade implements TransitionManager.Transition {
    private static final Fade instance = new Fade();

    private float duration;
    private boolean fadeCurrentScreen;

    public static Fade init (float duration) {
        return init(duration, false);
    }

    public static Fade init (float duration, boolean fadeCurrentScreen) {
        instance.fadeCurrentScreen = fadeCurrentScreen;
        instance.duration = duration;
        return instance;
    }
    @Override
    public float getDuration () {
        return duration;
    }
    @Override
    public void render (Batch batch, TextureRegion currScreen,
                        TextureRegion nextScreen, float alpha) {
        alpha = Interpolation.fade.apply(alpha);
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        if(fadeCurrentScreen) batch.setColor(1, 1, 1, 1-alpha);
        batch.draw(currScreen, 0, 0);
        batch.setColor(1, 1, 1, alpha);
        batch.draw(nextScreen, 0, 0);
        batch.end();
    }
}