package es.eucm.cytochallenge.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Scaling;
import es.eucm.cytochallenge.model.Challenge;
import es.eucm.cytochallenge.model.TextChallenge;
import es.eucm.cytochallenge.model.control.MultipleAnswerControl;
import es.eucm.cytochallenge.model.control.MultipleImageAnswerControl;
import es.eucm.cytochallenge.model.control.TextControl;
import es.eucm.cytochallenge.model.control.draganddrop.DragAndDropControl;
import es.eucm.cytochallenge.model.control.filltheblank.FillTheBlankControl;
import es.eucm.cytochallenge.view.SkinConstants;
import es.eucm.cytochallenge.view.transitions.Fade;
import es.eucm.cytochallenge.view.widgets.ChallengeButton;
import es.eucm.cytochallenge.view.widgets.TopToolbarLayout;
import es.eucm.cytochallenge.view.widgets.WidgetBuilder;

public class ChallengeList extends BaseScreen {

    @Override
    public void create() {
        super.create();

        Table layout = new Table();

        loadChallenges(layout);

        ScrollPane scroll = new ScrollPane(layout);
        scroll.setScrollingDisabled(true, false);


        Table topTable = new Table();
        topTable.background(BaseScreen.skin.getDrawable(SkinConstants.DRAWABLE_9P_TOOLBAR));
        topTable.setColor(SkinConstants.COLOR_TOOLBAR_TOP);

        Label label = new Label(i18n.get("challenges"), BaseScreen.skin,
                SkinConstants.STYLE_TOOLBAR);

        Button icon = es.eucm.cytochallenge.view.widgets.WidgetBuilder.toolbarIcon(SkinConstants.IC_UNDO);
        icon.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onBackPressed();
            }
        });

        topTable.add(icon);
        topTable.add(label)
                .expandX();

        TopToolbarLayout rootLayout = new TopToolbarLayout();
        rootLayout.setTopToolbar(topTable);
        rootLayout.setContainer(scroll);

        root.add(rootLayout).expand().fill();
    }

    private void loadChallenges(Table layout) {

        layout.top();
        Json json = new Json();
        float pad = WidgetBuilder.dpToPixels(54f);
        layout.pad(pad);
        float maxWidth = Gdx.graphics.getWidth() - pad;
        String challengesPath = "challenges/";
        String challengeJson = "challenge.json";
        String[] challengesPaths = json.fromJson(String[].class,
                Gdx.files.internal(challengesPath + "challenges.json"));

        Gdx.app.log("Files", "Challenges list: " + challengesPaths.length);


        for (int i = 0; i < challengesPaths.length; i++) {
            String challengeFolder = challengesPath + challengesPaths[i] + "/";
            final FileHandle child = Gdx.files.internal(challengeFolder + challengeJson);
            Gdx.app.log("Files", "file: " + child.path());

            Challenge challenge = json.fromJson(Challenge.class, child);
            Gdx.app.log("Files", "json challenge: " + challenge.getImagePath());

            if (challenge instanceof TextChallenge) {
                TextChallenge textChallenge = (TextChallenge) challenge;

                final Button button = new ChallengeButton(textChallenge.getTextControl(),
                        skin);
                button.setUserObject(challengeFolder);
                Gdx.app.log("Files", "json challenge getTextControl: " + textChallenge.getTextControl());

                button.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        challenges.setChallengePath(button.getUserObject().toString());
                        game.changeScreen(challenges);
                    }
                });

                layout.add(button).left().fillX().width(maxWidth);
                layout.row();
            }
        }
    }

    @Override
    public void draw() {
        super.draw();
    }

    @Override
    public void onBackPressed() {
        game.changeScreen(menu, Fade.init(1f, true));
    }

}