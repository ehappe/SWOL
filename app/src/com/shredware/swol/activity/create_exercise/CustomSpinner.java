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

package com.shredware.swol.activity.create_exercise;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;

/**
 * Custom spinner class.
 * 
 * The onItemSelected method does not fire if the same item is selected again.
 * 
 **/
public class CustomSpinner extends Spinner {
	OnItemSelectedListener listener;

	public CustomSpinner(Context context, AttributeSet attrs) {
	    super(context, attrs);
	}

	@Override
	public void setSelection(int position) {
	    super.setSelection(position);
	    if (listener != null)
	        listener.onItemSelected(null, null, position, 0);
	}

	public void setOnItemSelectedEvenIfUnchangedListener(
	        OnItemSelectedListener listener) {
	    this.listener = listener;
	}
}