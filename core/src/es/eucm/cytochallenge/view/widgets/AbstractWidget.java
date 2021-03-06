package es.eucm.cytochallenge.view.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import com.badlogic.gdx.utils.Predicate;

public class AbstractWidget extends WidgetGroup {

    private static final UserObjectPredicate USER_OBJECT_PREDICATE = new UserObjectPredicate();

    private static final InputListener requestFocus = new InputListener() {

        @Override
        public boolean touchDown(InputEvent event, float x, float y,
                                 int pointer, int button) {
            Actor widget = event.getListenerActor();
            event.getStage().setKeyboardFocus(widget);
            return true;
        }
    };

    /**
     * Sets if the this view must acquire the keyboard focus when this or any of
     * its children is clicked
     *
     * @param requestKeyboardFocus
     *            if the focus must be requested. It is set {@code false} by
     *            default
     */
    public void setRequestKeyboardFocus(boolean requestKeyboardFocus) {
        if (requestKeyboardFocus) {
            this.addCaptureListener(requestFocus);
        } else {
            this.removeCaptureListener(requestFocus);
        }
    }

    /**
     * Requests the keyboard focus
     */
    public void requestKeyboardFocus() {
        getStage().setKeyboardFocus(this);
    }

    public static float getPrefWidth(Actor a) {
        if (a == null) {
            return 0;
        } else if (a instanceof Layout) {
            return ((Layout) a).getPrefWidth();
        } else {
            return a.getWidth();
        }
    }

    public static float getPrefHeight(Actor a) {
        if (a == null) {
            return 0;
        } else if (a instanceof Layout) {
            return ((Layout) a).getPrefHeight();
        } else {
            return a.getHeight();
        }
    }

    protected static float getMaxWidth(Actor a) {
        if (a instanceof Layout) {
            return ((Layout) a).getMaxWidth();
        } else {
            return a.getWidth();
        }
    }

    protected static float getMaxHeight(Actor a) {
        if (a instanceof Layout) {
            return ((Layout) a).getMaxHeight();
        } else {
            return a.getHeight();
        }
    }

    protected float getChildrenTotalWidth() {
        return getChildrenTotalWidth(true);
    }

    protected float getChildrenTotalWidth(boolean computeInvisibles) {
        float totalWidth = 0;
        for (Actor a : this.getChildren()) {
            if (a.isVisible() || computeInvisibles) {
                totalWidth += getPrefWidth(a);
            }
        }
        return totalWidth;
    }

    protected float getChildrenTotalHeight() {
        return getChildrenTotalHeight(true);
    }

    protected float getChildrenTotalHeight(boolean computeInvisibles) {
        float totalHeight = 0;
        for (Actor a : this.getChildren()) {
            if (a.isVisible() || computeInvisibles) {
                totalHeight += getPrefHeight(a);
            }
        }
        return totalHeight;
    }

    protected float getChildrenMaxHeight() {
        return getChildrenMaxHeight(true);
    }

    protected float getChildrenMaxHeight(boolean computeInvisibles) {
        float maxHeight = 0;
        for (Actor a : this.getChildren()) {
            if (a.isVisible() || computeInvisibles) {
                maxHeight = Math.max(getPrefHeight(a), maxHeight);
            }
        }
        return maxHeight;
    }

    protected float getChildrenMaxWidth() {
        return getChildrenMaxWidth(true);
    }

    protected float getChildrenMaxWidth(boolean computeInvisibles) {
        float maxWidth = 0;
        for (Actor a : this.getChildren()) {
            if (a.isVisible() || computeInvisibles) {
                maxWidth = Math.max(getPrefWidth(a), maxWidth);
            }
        }
        return maxWidth;
    }

    @Override
    public void setX(float x) {
        super.setX(Math.round(x));
    }

    @Override
    public void setY(float y) {
        super.setY(Math.round(y));
    }

    @Override
    public void setWidth(float width) {
        super.setWidth(Math.round(width));
    }

    @Override
    public void setHeight(float height) {
        super.setHeight(Math.round(height));
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(Math.round(x), Math.round(y));
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(Math.round(width), Math.round(height));
    }

    @Override
    public void setBounds(float x, float y, float width, float height) {
        super.setBounds(Math.round(x), Math.round(y), Math.round(width),
                Math.round(height));
    }

    public static void setPosition(Actor a, float x, float y) {
        a.setPosition(Math.round(x), Math.round(y));
    }

    public static void setBounds(Actor a, float x, float y, float width,
                                 float height) {
        a.setBounds(Math.round(x), Math.round(y), Math.round(width),
                Math.round(height));
    }

    /**
     * Finds an actor that fulfills the given predicate, starting the search in
     * the given root
     *
     * @return the actor found. Could be {@code null} if no actor matched the
     *         predicate
     */
    public static Actor findActor(Group root, Predicate<Actor> predicate) {
        if (predicate.evaluate(root)) {
            return root;
        }

        for (Actor child : root.getChildren()) {
            if (predicate.evaluate(child)) {
                return child;
            } else if (child instanceof Group) {
                Actor actor = findActor((Group) child, predicate);
                if (actor != null) {
                    return actor;
                }
            }
        }
        return null;
    }

    /**
     * Returns a child widget whose user object is the given one
     */
    public Actor findUserObject(Object userObject) {
        USER_OBJECT_PREDICATE.setUserObject(userObject);
        return findActor(this, USER_OBJECT_PREDICATE);
    }

    public static class UserObjectPredicate implements Predicate<Actor> {

        private Object userObject;

        public void setUserObject(Object userObject) {
            this.userObject = userObject;
        }

        @Override
        public boolean evaluate(Actor actor) {
            return actor.getUserObject() == userObject;
        }

    }
}
