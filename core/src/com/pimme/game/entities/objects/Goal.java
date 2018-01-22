package com.pimme.game.entities.objects;

import com.badlogic.gdx.maps.MapObject;
import com.pimme.game.PyroGame;
import com.pimme.game.screens.FinishScreen;
import com.pimme.game.screens.PlayScreen;
import com.pimme.game.tools.Highscore;

public class Goal extends InteractiveObject
{
	public Goal(final PlayScreen screen, final MapObject object) {
		super(screen, object);
		fixture.setUserData(this);
		setCategoryFilter(PyroGame.GOAL_BIT);
	}

	@Override public void onCollision() {
		if (PyroGame.completedLevels != null) PyroGame.completedLevels.add(PyroGame.currentLevel);
		Highscore.load();
		int score;
		if (PyroGame.currentLevel == PyroGame.Level.BOUNCE) {
			score = screen.getHud().getTimeScore();
			if (Highscore.getHighScore(Highscore.MAX_SCORES - 1)== 0 || Highscore.getHighScore(Highscore.MAX_SCORES - 1) > score)
				Highscore.setHighScore(score);
		}
		else {
			score = screen.getHud().getScore();
			if (Highscore.getHighScore(Highscore.MAX_SCORES - 1) < score) // Is score higher than lowest highscore?
				Highscore.setHighScore(score);
		}

		screen.getGame().setScreen(new FinishScreen(screen.getGame(), score));
	}
}
