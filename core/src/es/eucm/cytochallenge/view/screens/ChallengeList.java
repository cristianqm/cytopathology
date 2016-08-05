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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Scaling;
import es.eucm.cytochallenge.model.Challenge;
import es.eucm.cytochallenge.model.TextChallenge;
import es.eucm.cytochallenge.model.control.MultipleAnswerControl;
import es.eucm.cytochallenge.model.control.MultipleImageAnswerControl;
import es.eucm.cytochallenge.model.control.TextControl;
import es.eucm.cytochallenge.model.control.draganddrop.DragAndDropControl;
import es.eucm.cytochallenge.model.control.filltheblank.FillTheBlankControl;
import es.eucm.cytochallenge.model.course.Course;
import es.eucm.cytochallenge.utils.ChallengeResourceProvider;
import es.eucm.cytochallenge.utils.InternalFilesChallengeResourceProvider;
import es.eucm.cytochallenge.view.SkinConstants;
import es.eucm.cytochallenge.view.transitions.Fade;
import es.eucm.cytochallenge.view.widgets.ChallengeButton;
import es.eucm.cytochallenge.view.widgets.TopToolbarLayout;
import es.eucm.cytochallenge.view.widgets.WidgetBuilder;

public class ChallengeList extends BaseScreen {

    private InternalFilesChallengeResourceProvider challengeResourceProvider = new InternalFilesChallengeResourceProvider();

    private Course currentCourse;
    private Table layout;

    public void setCurrentCourse(Course currentCourse) {
        this.currentCourse = currentCourse;
        challenges.setCurrentCourse(currentCourse);
    }

    @Override
    public void create() {
        super.create();

        layout = new Table();

        ScrollPane scroll = new ScrollPane(layout, BaseScreen.skin, "verticalScroll");
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


        Button play = es.eucm.cytochallenge.view.widgets.WidgetBuilder.toolbarIcon(SkinConstants.IC_PLAY);
        play.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playCourse();
            }
        });

        topTable.add(icon);
        topTable.add(label)
                .expandX();
        topTable.add(play);

        TopToolbarLayout rootLayout = new TopToolbarLayout();
        rootLayout.setTopToolbar(topTable);
        rootLayout.setContainer(scroll);

        root.add(rootLayout).expand().fill();
    }

    private void playCourse() {
        if (currentCourse == null) {
            return;
        }
        challenges.setCurrentCourse(currentCourse);
        challenges.setChallengeResourceProvider(challengeResourceProvider);
        game.changeScreen(challenges);
    }

    @Override
    public void show() {
        super.show();
        layout.clearChildren();
        loadChallenges(layout);
    }

    private void loadChallenges(final Table layout) {

        layout.top();
        Json json = new Json();
        float pad = WidgetBuilder.dpToPixels(54f);
        layout.pad(pad);
        final float maxWidth = Gdx.graphics.getWidth() - pad;
        String challengesPath = "challenges/";
        String challengeJson = "challenge.json";

        Array<String> challengesPaths;
        if (currentCourse == null) {
            challengesPaths = json.fromJson(Array.class,
                    Gdx.files.internal(challengesPath + "challenges.json"));
        } else {
            challengesPaths = currentCourse.getChallenges();
        }

        for (int i = 0; i < challengesPaths.size; i++) {
            final String challengeFolder = challengesPath + challengesPaths.get(i) + "/";
            challengeResourceProvider.setResourcePath(challengeFolder);
            challengeResourceProvider.getChallenge(challengeJson, new ChallengeResourceProvider.ResourceProvidedCallback<Challenge>() {
                @Override
                public void loaded(Challenge challenge) {
                    Gdx.app.log("Files", "json challenge: " + challenge.getImagePath());

                    if (challenge instanceof TextChallenge) {
                        TextChallenge textChallenge = (TextChallenge) challenge;

                        final Button button = new ChallengeButton(textChallenge,
                                skin);
                        button.setUserObject(challengeFolder);
                        Gdx.app.log("Files", "json challenge getTextControl: " + textChallenge.getTextControl());

                        button.addListener(new ClickListener() {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                challenges.setCurrentCourse(null);
                                challengeResourceProvider.setResourcePath(button.getUserObject().toString());
                                challenges.setChallengeResourceProvider(challengeResourceProvider);
                                game.changeScreen(challenges);
                            }
                        });

                        layout.add(button).left().fillX().width(maxWidth);
                        layout.row();
                    }
                }

                @Override
                public void failed() {

                }
            });

        }
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public void draw() {
        super.draw();
    }

    @Override
    public void onBackPressed() {
        game.changeScreen(courseList, Fade.init(1f, true));
    }


}