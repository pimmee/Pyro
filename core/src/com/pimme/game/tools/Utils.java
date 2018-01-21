package com.pimme.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

public final class Utils
{
	public static Skin skin;

	public static void load() {
		skin = new Skin();
		BitmapFont font = new BitmapFont(Gdx.files.internal("joker_font.fnt"));
		BitmapFont hoverFont = new BitmapFont(Gdx.files.internal("joker_font_hover.fnt"));
		BitmapFont greenFont = new BitmapFont(Gdx.files.internal("joker_font_green.fnt"));

		LabelStyle ls = new LabelStyle();
		TextButtonStyle tbs = new TextButtonStyle();
		TextButtonStyle hoverTBS = new TextButtonStyle();
		LabelStyle greenLS = new LabelStyle();
		ls.font = font;
		greenLS.font = greenFont;
		tbs.font = font;
		hoverTBS.font = hoverFont;
		skin.add("default", ls);
		skin.add("default", tbs);
		skin.add("hover", hoverTBS);
		skin.add("green", greenLS);
	}
}
