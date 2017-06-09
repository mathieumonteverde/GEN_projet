package com.heigvd.gen.guicomponent;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

/**
 * Classe utilitaire pour créer des boutons et des champs textes et d'autres
 * encore par la suite
 */
public class GuiComponent {
   // Font
   private static BitmapFont font = new BitmapFont();
   // Chemin vers la skin
   private static Skin skin = new Skin(Gdx.files.internal("default/skin/uiskin.json"));
   
   /**
    * Créer un bouton libgdx
    * @param label le texte affiché dans le bouton
    * @param width la largeur du bouton en px
    * @param height la hauteur du bouton en px
    * @return un nouveau bouton
    */
   public static TextButton createButton(String label, int width, int height) {
      
      // Create the font
      font.setColor(Color.LIGHT_GRAY);
      
      // Create the button and set its dimensions
      TextButton button = new TextButton(label, skin);
      button.setSize(width, height);

      return button;
   }
   
   /**
    * Créer un nouveau champ texte contenant par défaut le texte label
    * @param label le texte par défaut
    * @return un nouveau champ texte
    */
   public static TextField createTextField(String label) {
      TextField text = new TextField(label, skin);
      
      return text;
   }
   
   /**
    * Centrer un élément au milieu du stage, en leur appliquant un offset 
    * éventuel
    * @param element élément à centrer
    * @param stage le stage dans lequel sont contenu les éléments
    * @param offsetX offset x
    * @param offsetY offset y
    */
   public static void centerGuiComponent(Actor element, Stage stage, int offsetX, int offsetY) {
      element.setX(stage.getWidth() / 2 - element.getWidth() / 2 + offsetX);
      element.setY(stage.getHeight() / 2 - element.getHeight() / 2 + offsetY);
   }
   
   public Skin getSkin() {
      return skin;
   }
}
