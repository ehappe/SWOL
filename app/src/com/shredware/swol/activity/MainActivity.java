/**
 * 
 * This is OpenTraining, an Android application for planning your your fitness training.
 * Copyright (C) 2012-2014 Christian Skubich
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package com.shredware.swol.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.animation.AlphaAnimation;
import android.widget.Adapter;

import java.util.List;

import com.shredware.swol.R;
import com.shredware.swol.activity.create_workout.ExerciseTypeListActivity;
import com.shredware.swol.activity.manage_workouts.WorkoutListActivity;
import com.shredware.swol.activity.settings.SettingsActivity;
import com.shredware.swol.activity.start_training.Fader;
import com.shredware.swol.basic.Workout;
import com.shredware.swol.db.Cache;
import com.shredware.swol.db.DataProvider;
import com.shredware.swol.db.IDataProvider;


public class MainActivity extends AppCompatActivity {
	/** Tag for logging */
	public static final String TAG = MainActivity.class.getName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_navigation_layout);
		//getSupportActionBar().setIcon(R.drawable.icon_dumbbell);

		setContentView(R.layout.activity_navigation_layout);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayShowTitleEnabled(false);


		setUpNavigation();

		// load data/parse .xml files in background
		final Context mContext = this.getApplicationContext();
		new Thread() {
			@Override
			public void run() {
				Cache.INSTANCE.updateCache(mContext);
			}
		}.start();

		
		//Launch what's new dialog
		final WhatsNewDialog whatsNewDialog = new WhatsNewDialog(this);
		whatsNewDialog.show(); // (will only be shown if started the first time since last change)
		
		// show disclaimer
		//SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		/*Boolean showDisclaimer = settings.getBoolean(DisclaimerDialog.PREFERENCE_SHOW_DISCLAIMER, true);
		if (showDisclaimer) {
			new DisclaimerDialog(this);
		}*/
		
	}
	
	private void setUpNavigation(){
		Button trainButton = this.findViewById(R.id.train_button);
		Button createWorkoutButton = this.findViewById(R.id.create_workout_button);
		Button manageWorkoutsButton = this.findViewById(R.id.manage_workouts_button);
		ImageView toolbarSettingsButton = this.findViewById(R.id.toolbar_settings);
		ImageView toolbarSearchButton = this.findViewById(R.id.toolbar_search);

		trainButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Fader.runAlphaAnimation((Activity) view.getContext(), trainButton.getId());
				showSelectWorkoutDialog();
			}
		});

		createWorkoutButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Fader.runAlphaAnimation((Activity) view.getContext(), createWorkoutButton.getId());
				startActivity(new Intent(MainActivity.this.getApplicationContext(), ExerciseTypeListActivity.class));
			}
		});

		manageWorkoutsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Fader.runAlphaAnimation((Activity) view.getContext(), manageWorkoutsButton.getId());
				startActivity(new Intent(MainActivity.this.getApplicationContext(), WorkoutListActivity.class));
			}
		});

		toolbarSettingsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(MainActivity.this.getApplicationContext(), SettingsActivity.class));
			}
		});

		toolbarSearchButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(MainActivity.this.getApplicationContext(), ExerciseTypeListActivity.class));
			}
		});
	}

	/** Shows a dialog for choosing a {@link Workout} */
	private void showSelectWorkoutDialog() {
		IDataProvider dataProvider = new DataProvider(this);

		// get all Workouts
		final List<Workout> workoutList = dataProvider.getWorkouts();

		Log.d(TAG, "Number of Workouts: " + workoutList.size());
		switch (workoutList.size()) {
		// show error message, if there is no Workout
		case 0:
			Toast.makeText(MainActivity.this, getString(R.string.no_workout), Toast.LENGTH_LONG).show();
			break;
		// choose Workout, if there is/are one or more Workout(s)
		default:
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
			if (prev != null) {
				ft.remove(prev);
			}
			ft.addToBackStack(null);

			// Create and show the dialog.
			DialogFragment newFragment = SelectWorkoutDialogFragment.newInstance();
			newFragment.show(ft, "dialog");
		}

	}

}
