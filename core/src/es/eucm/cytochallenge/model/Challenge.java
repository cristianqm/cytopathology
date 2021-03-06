package es.eucm.cytochallenge.model;

import es.eucm.cytochallenge.model.hint.Hint;


/**
 * Created by dan on 23/02/2016.
 */
public class Challenge {

    private String _id;

    private Difficulty difficulty = Difficulty.EASY;

    private Category category = Category.OTHER;

    private String imagePath;

    private Hint hint;

    private String name;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Hint getHint() {
        return hint;
    }

    public void setHint(Hint hint) {
        this.hint = hint;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return super.toString() + " " + difficulty;
    }
}
