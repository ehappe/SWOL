package com.shredware.swol.activity.create_workout.upload_exercise;

import java.io.File;

import android.content.Context;

import com.shredware.swol.basic.License;
import com.shredware.swol.db.IDataProvider;

public class ExerciseImage {
	File mImagePath;
	String mRealImagePath;

	License mLicense;

	public ExerciseImage(File image, License license, Context context){
		mLicense = license;
		mImagePath = image;
		mRealImagePath = context.getFilesDir().toString() + "/" + IDataProvider.CUSTOM_IMAGES_FOLDER + "/" + mImagePath;
	}

	public String getRealImagePath(){
		return mRealImagePath;
	}

}
