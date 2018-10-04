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

package com.shredware.swol.test.datamodel;

import java.util.GregorianCalendar;

import android.test.AndroidTestCase;
import com.shredware.swol.basic.ExerciseType;
import com.shredware.swol.basic.FSet;
import com.shredware.swol.basic.ExerciseType.ExerciseSource;
import com.shredware.swol.basic.FSet.SetParameter;
import com.shredware.swol.basic.FitnessExercise;
import com.shredware.swol.basic.TrainingEntry;
import com.shredware.swol.basic.Workout;

/**
 * Tests for {@link FitnessExercise}.
 *
 */
public class FitnessExerciseTest extends AndroidTestCase {
	/** Tag for logging */
	public static final String TAG = "FitnessExerciseTest";
	

	public void testEquals(){
		ExerciseType ex = (new ExerciseType.Builder("Crunch", ExerciseSource.CUSTOM)).build();
		FitnessExercise fEx = new FitnessExercise(ex);
		
		Workout w = new Workout("-", fEx);
		w.addTrainingEntry(GregorianCalendar.getInstance().getTime());
		
		
		TrainingEntry entry = fEx.getTrainingEntryList().get(0);
		
		FSet set1 = new FSet(new SetParameter.Duration(2));
		entry.add(set1);
		assert(fEx.isTrainingEntryFinished(entry));

		entry.setHasBeenDone(set1, false);
		assertFalse(fEx.isTrainingEntryFinished(entry));
		
		entry.setHasBeenDone(set1, true);
		assert(fEx.isTrainingEntryFinished(entry));

	}
	
		

}
