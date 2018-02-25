package com.pimme.game.entities.objects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.utils.Array;
import com.pimme.game.PyroGame;
import com.pimme.game.screens.FinishScreen;
import com.pimme.game.screens.PlayScreen;
import com.pimme.game.tools.Highscore;
import com.pimme.game.tools.Manager;
import com.pimme.game.tools.Manager.Level;



public class Goal extends InteractiveObject
{
	public Goal(final PlayScreen screen, final MapObject object) {
		super(screen, object);
		fixture.setUserData(this);
		setCategoryFilter(PyroGame.GOAL_BIT);
	}

	@Override public void onCollision() {
		manager.getAssetManager().get("audio/sounds/goal_reached.wav", Sound.class).play();

		Array<Level> completedLevels = manager.getCompletedLevels();
		int totalHighscore = manager.getTotalHighscore();
		int score = screen.getHud().getScore();
		manager.loadHighScores();

		if (completedLevels != null) {	// If in play all levels mode
			completedLevels.add(manager.getCurrentLevel());
			manager.setTotalHighscore(totalHighscore + score);
			if (completedLevels.size == 4)	// If all levels finished
				manager.setHighScore(totalHighscore);
			else if (manager.getHighScore(Highscore.MAX_SCORES - 1) < score)
				manager.setHighScore(score);
		}
		else if (manager.getHighScore(Highscore.MAX_SCORES - 1) < score) // Is score higher than lowest highscore?
			manager.setHighScore(score);

		screen.getGame().setScreen(new FinishScreen(screen.getGame(), score));
	}
}
