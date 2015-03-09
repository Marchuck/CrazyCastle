package com.lukasyno.crazycastle;

import java.util.ArrayList;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lukasyno.crazycastle.Character.GESTURE;


/**
 * TODO: splash screen oraz wygodne menu naprawić przechodzenie przez drzwi -
 * nie randomowo tylko ma być to cykl Hamiltona wygenerować klasę LevelManager,
 * która dba o przebieg gry usunąć niepotrzebne zmienne stworzyć interfejs, po
 * którym będzie implementował Character, EvilBugs oraz Blendzior
 * 
 * @author lukasz
 * 
 */
public class MainActivity extends Activity {

	// public ArrayList<Pair<Integer, Integer>> track = new
	// ArrayList<Pair<Integer, Integer>>();
	// public Pair<Integer, Integer> temporaryPair = new Pair<Integer,
	// Integer>(0,0);
	public CastleEngine CrazyCastle;
	public SceneProvider sceneProvider;
	public Character character;
	public EvilBugs evilBugs;
	public CarrotManager carrotManager;

	private final int ROOMS_COUNT = 14;

	Random generator = new Random();
	// int BunnyStep = 20;
	// int HEIGHT = 1000;
	boolean DirectionLeft = false;
	boolean DirectionLeftForEvil = false;
	int CollectedCarrots = 0;
	int CurrentStage = 0;
	int time = 0;
	int PreviousStage = 0;
	float X0, Y0;
	int FingerSize = 100;
	int current_level = 0;

	private Animation fade;

	// private LinearLayout customLay = new LinearLayout(null);

	@SuppressLint("UseValueOf")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		sceneProvider = new SceneProvider(ROOMS_COUNT);

		CrazyCastle = (CastleEngine) findViewById(R.id.castle_view);
		CrazyCastle.setBackground(getResources().getDrawable(R.drawable.wall));
		CrazyCastle.setScene(sceneProvider.getCurrentScene());
		View myView = (View) findViewById(R.id.mainLayout);
		carrotManager = new CarrotManager(this);

		fade = AnimationUtils.loadAnimation(this, R.anim.fade_out);

		character = new Character(this);
		character.CurrentCharacterEntity.setX(character.CurrentCharacterEntity
				.getX() + 111);
		evilBugs = new EvilBugs(this, sceneProvider);

		// createFailLayout();

		Loop.start(this);

	}

	// private void createFailLayout() {
	// customLay.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
	// LayoutParams.WRAP_CONTENT));
	// customLay.setOrientation(LinearLayout.HORIZONTAL);
	// Button btnYES = new Button(this);
	// Button btnNO = new Button(this);
	// btnNO.setText("No");
	// btnYES.setText("Yes");
	//
	// btnNO.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// finish();
	// }
	// });
	// btnYES.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// customLay.startAnimation(fade);
	// setContentView(R.layout.activity_main);
	// RestartGame();
	// }
	// });
	// customLay.addView(btnYES);
	// customLay.addView(btnNO);
	// }

	public float AbsDiff(float x, float y) {
		return (x > y) ? (x - y) : (y - x);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		if (event.getActionMasked() == MotionEvent.ACTION_UP) {
			character.setWalkStatus(GESTURE.STOP);
		}
		if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
			time = 0;
			ImageView AnimationLeft = (ImageView) findViewById(R.id.Bunny_Mirror);
			ImageView AnimationRight = (ImageView) findViewById(R.id.Bunny);
			ImageView ref = (DirectionLeft) ? AnimationLeft : AnimationRight;
			ImageView refMirror = (!DirectionLeft) ? AnimationLeft
					: AnimationRight;

			// variables needed to detect door
			ArrayList<PointAndStage> list = sceneProvider
					.getCurrentDoorPosition();

			PointAndStage pt = list.get(CurrentStage);
			CrazyCastle.setDoorCoordinates(list);

			float FloorHeight = CastleEngine.Floor_Height;
			float Y = event.getY() - FingerSize;

			// if we want to go downstairs to stage 1:
			if (CurrentStage == 0
					&& ref.getX() > pt.DoorStart - Character.DOOR_OFFSET
					&& ref.getX() < pt.DoorEnd + Character.DOOR_OFFSET
					&& 6 * FloorHeight < Y) {

				View lay = (View) findViewById(R.id.mainLayout);
				lay.startAnimation(fade);

				evilBugs.setCurrentStage(generator.nextInt(3));
				CurrentStage = generator.nextInt(3);
				sceneProvider.setCurrentScene(generator.nextInt(ROOMS_COUNT));

				CrazyCastle.setScene(sceneProvider.getCurrentScene());
				list = sceneProvider.getCurrentDoorPosition();
				pt = list.get(CurrentStage);
				ref.setX(pt.DoorStart);
				ref.setY(Y_Position_Is_Essential(CurrentStage));
				character.setMirrorProperly(refMirror, ref);
				carrotManager
						.setProperCarrot(sceneProvider.getCurrentScene().ID);

			}
			// if we want to go downstairs to stage 2:
			else if (CurrentStage == 1
					&& ref.getX() > pt.DoorStart - Character.DOOR_OFFSET
					&& ref.getX() < pt.DoorEnd + Character.DOOR_OFFSET
					&& 12 * FloorHeight < Y) {

				View lay = (View) findViewById(R.id.mainLayout);
				lay.startAnimation(fade);

				evilBugs.setCurrentStage(generator.nextInt(3));
				CurrentStage = generator.nextInt(3);
				sceneProvider.setCurrentScene(generator.nextInt(4));

				CrazyCastle.setScene(sceneProvider.getCurrentScene());
				list = sceneProvider.getCurrentDoorPosition();
				pt = list.get(CurrentStage);
				ref.setX(pt.DoorStart);
				ref.setY(Y_Position_Is_Essential(CurrentStage));
				character.setMirrorProperly(refMirror, ref);
				carrotManager
						.setProperCarrot(sceneProvider.getCurrentScene().ID);
			}
			// if we want to go upstairs to stage 0:
			else if (CurrentStage == 1
					&& ref.getX() > pt.DoorStart - Character.DOOR_OFFSET
					&& ref.getX() < pt.DoorEnd + Character.DOOR_OFFSET
					&& 6 * FloorHeight > Y) {

				View lay = (View) findViewById(R.id.mainLayout);
				lay.startAnimation(fade);

				evilBugs.setCurrentStage(generator.nextInt(3));
				CurrentStage = generator.nextInt(3);
				sceneProvider.setCurrentScene(generator.nextInt(ROOMS_COUNT));

				CrazyCastle.setScene(sceneProvider.getCurrentScene());
				list = sceneProvider.getCurrentDoorPosition();
				pt = list.get(CurrentStage);
				ref.setX(pt.DoorStart);
				ref.setY(Y_Position_Is_Essential(CurrentStage));

				character.setMirrorProperly(refMirror, ref);
				carrotManager
						.setProperCarrot(sceneProvider.getCurrentScene().ID);

			}
			// if we want to go upstairs to stage 1:
			else if (CurrentStage == 2
					&& ref.getX() > pt.DoorStart - Character.DOOR_OFFSET
					&& ref.getX() < pt.DoorEnd + Character.DOOR_OFFSET
					&& 12 * FloorHeight > Y) {

				View lay = (View) findViewById(R.id.mainLayout);
				lay.startAnimation(fade);

				evilBugs.setCurrentStage(generator.nextInt(3));
				CurrentStage = generator.nextInt(3);
				sceneProvider.setCurrentScene(generator.nextInt(ROOMS_COUNT));

				CrazyCastle.setScene(sceneProvider.getCurrentScene());

				list = sceneProvider.getCurrentDoorPosition();
				pt = list.get(CurrentStage);
				ref.setX(pt.DoorStart);
				ref.setY(Y_Position_Is_Essential(CurrentStage));
				character.setMirrorProperly(refMirror, ref);
				carrotManager
						.setProperCarrot(sceneProvider.getCurrentScene().ID);

			}
			// move character left/right
			if (character.CurrentCharacterEntity.getX() > event.getX()) {
				character.setAnimation(GESTURE.RIGHTWALK);
				character.DirectionLeft = true;
				character.setWalkStatus(GESTURE.LEFTWALK);
			} else if (character.CurrentCharacterEntity.getX() < event.getX()) {

				character.setAnimation(GESTURE.LEFTWALK);
				character.DirectionLeft = false;
				character.setWalkStatus(GESTURE.RIGHTWALK);
			}
		}
		if (event.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN) {
			character.setAnimation(GESTURE.TRANSPARENT);

		} else {
			if (character.DirectionLeft)
				character.setAnimation(GESTURE.LEFTWALK);
			else
				character.setAnimation(GESTURE.RIGHTWALK);
		}
		return true;
	}

	private int Y_Position_Is_Essential(int stage) {
		return (5 + 6 * stage - 3) * CastleEngine.Floor_Height;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.restart) {
			RestartGame();
			return true;
		}
		if (id == R.id.action_settings) {

		}
		return super.onOptionsItemSelected(item);
	}

	public void RestartGame() {
		character.onRestart();
		// evilBugs.onRestart();
		carrotManager.onRestart();
	}

	public void getX0(float x) {
		X0 = x;
	}

	public void LoopActions() {
		character.walk(character.WalkStatus);

		if (carrotManager.CollidesWith(character.CurrentCharacterEntity)
				&& carrotManager.isVisible()) {
			carrotManager.DetachCarrot(sceneProvider.getCurrentScene().ID);
			carrotManager.update();
		}
		if (character.CollidesWith(evilBugs.getEvilEntity())) {
			if (character.CHARACTER_VISIBILITY) {
				character.onDeath();
			}
		}
		if (character.CHARACTER_IS_DEAD) {
			character.TTL--;
		}
		if (character.TTL == 0) {
			View lay = (View) findViewById(R.id.mainLayout);
			lay.startAnimation(fade);
			// setContentView(customLay);
			RestartGame();
		}

		evilBugs.EvilWalk();

	}

}
