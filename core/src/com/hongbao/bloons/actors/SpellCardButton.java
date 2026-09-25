package com.hongbao.bloons.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.hongbao.bloons.Map;
import com.hongbao.bloons.components.SpellCardAbility;
import com.hongbao.bloons.helpers.ZIndex;

/**
 * Interactive UI button actor for triggering and visualizing spell card ability state.
 */
public class SpellCardButton extends RenderableLabel {

	private final Map map;
	private final Label label;

	public SpellCardButton(Map map, Skin skin) {
		super(new Label("SPELL CARD", skin), ZIndex.MENU_ITEM_Z_INDEX);
		this.map = map;
		this.label = getActor();

		label.setAlignment(Align.center);
		label.setColor(Color.GREEN);

		label.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				map.placeSpellCard();
			}
		});
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		updateVisualState();
	}

	public void updateVisualState() {
		GirlActor selectedGirl = map.getSelectedGirl();
		if (selectedGirl == null || !selectedGirl.isActive()) {
			label.setText("SPELL CARD [X]");
			label.setColor(Color.GRAY);
			return;
		}

		SpellCardAbility ability = selectedGirl.getSpellCardAbility();
		if (ability == null) {
			label.setText("SPELL CARD [X]");
			label.setColor(Color.GRAY);
			return;
		}

		if (ability.isReady()) {
			label.setText("SPELL CARD [X]");
			label.setColor(Color.GREEN);
		} else if (ability.isActive()) {
			label.setText(String.format("ACTIVE (%.1fs)", ability.getActiveRemaining()));
			label.setColor(Color.ORANGE);
		} else if (ability.isRecharging()) {
			label.setText(String.format("RECHARGE (%.1fs)", ability.getCooldownRemaining()));
			label.setColor(Color.RED);
		}
	}
}
