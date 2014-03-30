package com.example.oncogram;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class Thumbnails extends Activity implements OnClickListener{
	ImageView imgView;
	Bitmap thumbnail;
	GridView images;

	public static final Uri CONTENT_URI = Uri.parse("content://com.example.oncogram/");

	private static final HashMap<String, String> MIME_TYPES = new HashMap<String, String>();

	static {

		MIME_TYPES.put(".jpg", "image/jpeg");

		MIME_TYPES.put(".jpeg", "image/jpeg");

	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.thumbnails);

		imgView = (ImageView) findViewById(R.id.img_preview);
		
		//SEND
		Button button = (Button) findViewById(R.id.send);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click
				Intent i = new Intent(Intent.ACTION_SEND);
				i.putExtra("address","4385000341");
				i.putExtra("sms_body","hello..");
				Uri uri = Uri.parse("file:///Users/sarapak/Documents/trashcan.png");
				i.putExtra(Intent.EXTRA_STREAM, uri);
				i.setType("image/png");
				startActivity(i);
			}
		});

		//CAMERA
		final int REQUEST_IMAGE_CAPTURE = 1;
  
		Button camera = (Button) findViewById(R.id.camera);
		camera.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
					startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
				}
			} 
		});

		//TRASH
		Button trash = (Button) findViewById(R.id.trash);
		trash.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

			}
		});
		
		//THUMBNAILS
		images = (GridView) findViewById(R.id.images);
	    images.setAdapter(new ImageAdapter(this));

	    images.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            //Toast.makeText(this, "" + position, Toast.LENGTH_SHORT).show();
	        }
	    });
		
	}


	/*	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    // Check which request we're responding to
		int i = 0;
		System.out.println(""+i);
	} */

/*	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode){
		case 0:
			if(resultCode==RESULT_OK){
				thumbnail = (Bitmap) data.getExtras().get("data");
				imgView.setImageBitmap(thumbnail);
			}
		}
	} */

	private final int CAMERA_RESULT = 1;
	private final String Tag = getClass().getName();
	Button button1;
	ImageView imageView1;

	/*public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		button1 = (Button)findViewById(R.id.button1);
		imageView1 = (ImageView)findViewById(R.id.imageView1);
		button1.setOnClickListener(this);
	}*/


	public void onClick(View v) {
		PackageManager pm = getPackageManager();
		if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			i.putExtra(MediaStore.EXTRA_OUTPUT, MyFileContentProvider.CONTENT_URI);
			startActivityForResult(i, CAMERA_RESULT);
		} else {
			Toast.makeText(getBaseContext(), "Camera is not available", Toast.LENGTH_LONG).show();
		}   
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		Log.i(Tag, "Receive the camera result");
		if (resultCode == RESULT_OK && requestCode == CAMERA_RESULT) {
			File out = new File(getFilesDir(), "newImage.jpg");
			if(!out.exists()) {
				Toast.makeText(getBaseContext(),
						"Error while capturing image", Toast.LENGTH_LONG)
						.show();
				return;
			}
			Bitmap mBitmap = BitmapFactory.decodeFile(out.getAbsolutePath());
			imageView1.setImageBitmap(mBitmap);
		}
	}

	@Override

	protected void onDestroy() {

		super.onDestroy();

		imageView1 = null;

	} 
}
