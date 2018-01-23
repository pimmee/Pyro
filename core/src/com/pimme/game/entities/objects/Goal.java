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
		int score = screen.getHud().getScore();
		Highscore.load();
		if (PyroGame.completedLevels != null) {
			PyroGame.completedLevels.add(PyroGame.currentLevel);
			PyroGame.totalHighscore += score;
			if (PyroGame.completedLevels.size == 4)
				Highscore.setHighScore(PyroGame.totalHighscore);
			else if (Highscore.getHighScore(Highscore.MAX_SCORES - 1) < score)
				Highscore.setHighScore(score);
		}
		else if (Highscore.getHighScore(Highscore.MAX_SCORES - 1) < score) // Is score higher than lowest highscore?
			Highscore.setHighScore(score);


		screen.getGame().setScreen(new FinishScreen(screen.getGame(), score));
	}
}
