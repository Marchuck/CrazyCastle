package com.lukasyno.crazycastle;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Character {
	private Activity context = null;
	private float x;
	private float y;
	private float center_X;
	private float center_Y;
	
	public boolean CHARACTER_VISIBILITY = true;
	public boolean CHARACTER_IS_DEAD = false;
	
	public boolean DirectionLeft = true;
	private boolean animationStopped = true;
	private ImageView CharacterRotateLeft;
	public ImageView CurrentCharacterEntity;
	private ImageView CharacterRotateRight;
	private ImageView InvisibleCharacterRotateLeft;
	private ImageView InvisibleCharacterRotateRight;
	private ImageView CharacterDeathRight;

	// (ImageView)findViewById(R.id.Bunny_Mirror);
	private AnimationDrawable CharacterLeftAnimation;
	private AnimationDrawable CharacterRightAnimation;
	private AnimationDrawable CharacterLeftInvisibleAnimation;
	private AnimationDrawable CharacterRightInvisibleAnimation;
	private AnimationDrawable CharacterRightDeathAnimation;
	public int TTL = 100;
	public GESTURE WalkStatus = GESTURE.STOP;
	private float start_X;
	private float start_Y;

	private void UpdateCenterPosition() {
		center_X = (float) (x + CurrentCharacterEntity.getWidth() / 2.0);
		center_Y = (float) (y + CurrentCharacterEntity.getHeight() / 2.0);
	}

	private void UpdatePos() {
		x = CurrentCharacterEntity.getX();
		y = CurrentCharacterEntity.getY();
	}

	public enum GESTURE {
		LEFTWALK, RIGHTWALK, STOP, DEATH, TRANSPARENT
	};

	public static final int DEATH_ZONE = 10000;
	private static final int STEP = 10;
	private static final float STEP_DX = 1.5f;

	private static final int RUN = 20;
	public static final int WTF_OFFSET = 50;
	public static final int DOOR_OFFSET = 50;
	
	public Character(Activity context) {
		this.context = context;
		CharacterRotateLeft = (ImageView) context.findViewById(R.id.Bunny);
		CharacterRotateRight = (ImageView) context
				.findViewById(R.id.Bunny_Mirror);

		InvisibleCharacterRotateLeft = (ImageView) context
				.findViewById(R.id.InvisibleLeft);
		InvisibleCharacterRotateRight = (ImageView) context
				.findViewById(R.id.InvisibleRight);
		CharacterDeathRight = (ImageView) context.findViewById(R.id.DeathRight);

		CurrentCharacterEntity = CharacterRotateLeft;
		CharacterRotateRight.setY(DEATH_ZONE);
		setupAnimation();
		start_X = CharacterRotateLeft.getX();
		start_Y = CharacterRotateLeft.getY();
		setPosition(start_X, start_Y);
		
		InvisibleCharacterRotateLeft.setX(DEATH_ZONE);
		InvisibleCharacterRotateRight.setX(DEATH_ZONE);
		CharacterDeathRight.setY(DEATH_ZONE);

	}

	private void setPosition(float dx, float dy) {
		x = dx;
		y = dy;
	}

	public void setMirrorProperly(ImageView invisible, ImageView visible) {
		invisible.setY(visible.getY() - DEATH_ZONE);
		invisible.setX(visible.getX());
	}

	public void walk(GESTURE type) {
		if(CHARACTER_IS_DEAD)
			return;
		UpdatePos();
		
		switch (type) {
		case LEFTWALK: {
			if (CurrentCharacterEntity.getX() < 0)
				return;
			CurrentCharacterEntity
					.setX(CurrentCharacterEntity.getX() - STEP_DX);
			if (animationStopped)
				startAnimation();

			break;
		}
		case RIGHTWALK: {
			if (CurrentCharacterEntity.getX() > CastleEngine.ScreenWidth
					- WTF_OFFSET)
				return;
			CurrentCharacterEntity
					.setX(CurrentCharacterEntity.getX() + STEP_DX);
			if (animationStopped)
				startAnimation();
			break;
		}
		case STOP: {
			stopAnimation();
			break;
		}
		default: {
			break;
		}
		}
	}

	public void setAnimation(GESTURE type) {
		float dx = CurrentCharacterEntity.getX();
		float dy = CurrentCharacterEntity.getY();

		switch (type) {
		case LEFTWALK: {
			CHARACTER_VISIBILITY = true;

			CharacterRotateLeft.setX((float) dx);
			CharacterRotateLeft.setY((float) dy);
			CharacterRotateRight.setX(DEATH_ZONE);
			CharacterRotateRight.setY(DEATH_ZONE);
			InvisibleCharacterRotateRight.setX(DEATH_ZONE);
			InvisibleCharacterRotateRight.setY(DEATH_ZONE);
			InvisibleCharacterRotateLeft.setX(DEATH_ZONE);
			InvisibleCharacterRotateLeft.setY(DEATH_ZONE);
			DirectionLeft = true;
			CurrentCharacterEntity = CharacterRotateLeft;
			break;
		}
		case RIGHTWALK: {
			CHARACTER_VISIBILITY = true;

			CharacterRotateRight.setX((float) dx);
			CharacterRotateRight.setY((float) dy);
			CharacterRotateLeft.setX(DEATH_ZONE);
			CharacterRotateLeft.setY(DEATH_ZONE);
			InvisibleCharacterRotateRight.setX(DEATH_ZONE);
			InvisibleCharacterRotateRight.setY(DEATH_ZONE);
			InvisibleCharacterRotateLeft.setX(DEATH_ZONE);
			InvisibleCharacterRotateLeft.setY(DEATH_ZONE);

			DirectionLeft = false;
			CurrentCharacterEntity = CharacterRotateRight;
			break;
		}
		case TRANSPARENT: {
			CHARACTER_VISIBILITY = false;
			CharacterRotateLeft.setX(DEATH_ZONE);
			CharacterRotateRight.setX(DEATH_ZONE);
			if (DirectionLeft) {
				InvisibleCharacterRotateLeft.setX((float) dx);
				InvisibleCharacterRotateLeft.setY((float) dy);
				InvisibleCharacterRotateRight.setX(DEATH_ZONE);
				CurrentCharacterEntity = InvisibleCharacterRotateLeft;
			} else {
				InvisibleCharacterRotateRight.setX((float) dx);
				InvisibleCharacterRotateRight.setY((float) dy);
				InvisibleCharacterRotateLeft.setX(DEATH_ZONE);
				CurrentCharacterEntity = InvisibleCharacterRotateRight;
			}
			break;
		}
		case STOP: {
			break;
		}
		}
	}

	private void stopAnimation() {
		CharacterLeftAnimation.stop();
		CharacterRightAnimation.stop();
		CharacterLeftInvisibleAnimation.stop();
		CharacterRightInvisibleAnimation.stop();
		animationStopped = true;
	}

	private void setupAnimation() {
		CharacterLeftAnimation = (AnimationDrawable) CharacterRotateLeft
				.getDrawable();
		CharacterRightAnimation = (AnimationDrawable) CharacterRotateRight
				.getDrawable();
		CharacterLeftInvisibleAnimation = (AnimationDrawable) InvisibleCharacterRotateLeft
				.getDrawable();
		CharacterRightInvisibleAnimation = (AnimationDrawable) InvisibleCharacterRotateRight
				.getDrawable();
		CharacterRightDeathAnimation = (AnimationDrawable) CharacterDeathRight
				.getDrawable();
		startAnimation();
		stopAnimation();
	}

	private void startAnimation() {
		CharacterLeftAnimation.start();
		CharacterRightAnimation.start();
		CharacterLeftInvisibleAnimation.start();
		CharacterRightInvisibleAnimation.start();
		CharacterRightDeathAnimation.start();
		animationStopped = false;
	}

	public void setWalkStatus(GESTURE f) {
		WalkStatus = f;
	}

	public void onDeath() {
		float dx = CurrentCharacterEntity.getX();
		float dy = CurrentCharacterEntity.getY();
		InvisibleCharacterRotateRight.setX(DEATH_ZONE);
		InvisibleCharacterRotateLeft.setX(DEATH_ZONE);
		CharacterRotateRight.setX(DEATH_ZONE);
		CharacterRotateLeft.setX(DEATH_ZONE);
		CharacterDeathRight.setX(dx);
		CharacterDeathRight.setY(dy);
		CurrentCharacterEntity = CharacterDeathRight;
		CharacterRightDeathAnimation.stop();
		CharacterRightDeathAnimation.start();

		CHARACTER_VISIBILITY = false;
		CHARACTER_IS_DEAD = true;	
	}

	public void onRestart() {
		CharacterDeathRight.setY(DEATH_ZONE);
		CharacterDeathRight.setX(DEATH_ZONE);
		
		CurrentCharacterEntity = CharacterRotateLeft;
		CurrentCharacterEntity.setX(2*CharacterRotateLeft.getWidth());
		CurrentCharacterEntity.setY(2*CastleEngine.Floor_Height);

		CHARACTER_IS_DEAD = false;
		CHARACTER_VISIBILITY =true;
		TTL = 100;
	}

	public boolean CollidesWith(ImageView collider) {
		UpdateCenterPosition();
		return (CarrotManager.AbsDiff(collider.getX(),
				center_X) < 20 && CarrotManager.AbsDiff(
				collider.getY(), center_Y) < 100);
	}

}
