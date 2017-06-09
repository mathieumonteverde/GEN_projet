package com.heigvd.gen.button;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 *
 * @author mathieu
 */
public class Buttons {

   public static TextButton createButton(String label, int width, int height) {
      
      // Create the font
      BitmapFont font = new BitmapFont();
      font.setColor(Color.LIGHT_GRAY);
      
      // Get the button atlas
      TextureAtlas buttonAtlas;
      buttonAtlas = new TextureAtlas(Gdx.files.internal("button.pack"));
      
      // Set the button appearances
      TextureRegionDrawable drawableUp = new TextureRegionDrawable(buttonAtlas.findRegion("button_up9"));
      TextureRegionDrawable drawableDown = new TextureRegionDrawable(buttonAtlas.findRegion("button_down9"));
      TextureRegionDrawable drawableChecked = new TextureRegionDrawable(buttonAtlas.findRegion("button_down9"));
      
      // Create the button style
      TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle(drawableUp, drawableDown, drawableChecked, font);
      
      // Create the button and set its dimensions
      TextButton button = new TextButton(label, textButtonStyle);
      button.setSize(width, height);

      return button;
   }
}
