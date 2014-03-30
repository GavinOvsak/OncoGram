package com.example.oncogram;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;
import android.widget.RadioButton;

public class ProfilePage extends Activity{
	ProfilesList p = new ProfilesList();
	TextView name, my_birth, age, gender, location, baby_birth;
	AlertDialog.Builder builder1;
	View dialoglayout;
	EditText textBox;
	RadioGroup genSelect;
	Button camera, delete;

	int index = -1;
	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_profile);

		intent = getIntent();
		index = intent.getIntExtra("Index", -1);
		builder1 = new AlertDialog.Builder(this);

		name = (TextView)findViewById(R.id.name);
		my_birth = (TextView)findViewById(R.id.my_birth);
		age = (TextView)findViewById(R.id.age);
		gender = (TextView)findViewById(R.id.gender);
		location = (TextView)findViewById(R.id.location);
		baby_birth = (TextView)findViewById(R.id.baby_birth);

		//DELETE BUTTON
		delete = (Button) findViewById(R.id.delete);
		delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//TODO remove current profile
				//and return to ProfilesList
				//ProfilesList.profiles.remove();
				ProfilesList.adapter.remove(ProfilesList.adapter.getItem(index));
				finish();
//				Intent i = new Intent(getApplicationContext(), ProfilesList.class);
//				startActivity(i);
			}
		});

		//CAMERA BUTTON
		camera = (Button) findViewById(R.id.camera);
		final int REQUEST_IMAGE_CAPTURE = 1;
		camera.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
					startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
				}
			}
		});

		//IF SELECT NAME
		name.setText("Name: " + ProfilesList.profiles.get(index).getName());
		final EditText input = new EditText(this);
		LayoutInflater inflater = getLayoutInflater();
		dialoglayout = inflater.inflate(R.layout.editpopup, (ViewGroup) getCurrentFocus());
		builder1.setView(dialoglayout);
		textBox = (EditText) dialoglayout.findViewById(R.id.name);

		name.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			   LayoutInflater inflater = LayoutInflater.from(ProfilePage.this);
			   final View dialogview = inflater.inflate(R.layout.namepopup, null);
			   AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(ProfilePage.this);
			   dialogbuilder.setTitle("Login");
			   dialogbuilder.setCancelable(true);
			   dialogbuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						EditText textBox = (EditText) dialogview.findViewById(R.id.name);
						System.out.println("Name: " + textBox.getText().toString());
						String tempName = textBox.getText().toString();
						//TODO Check if has name. If so, go to profile page
						if(!tempName.equals(null))
						{
							//Profile p = profiles.get(index);
							//p.setName(tempName);
							//ProfilesList.adapter.getItem(index).r  insert(tempName, index);
							//Intent i= new Intent(getApplicationContext(), ProfilePage.class);
							//i.putExtra("Index", profiles.size() - 1);
							//startActivity(i);
						}
						dialog.cancel(); 
					}
				}); 
			   dialogbuilder.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
			   dialogbuilder.setView(dialogview);
			   
			   AlertDialog dialogDetails = null;
			   dialogDetails = dialogbuilder.create();
			   dialogDetails.show();
			   
				builder1.setCancelable(true);
				builder1.setPositiveButton("Done",
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						System.out.println("Name: " + textBox.getText().toString());
						String tempName = textBox.getText().toString();
						ProfilesList.profiles.get(index).setName(tempName);
						dialog.cancel(); 
					}
				}); 
				builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
				
			}
		});


		//AGE
		age.setText("Age: " + ProfilesList.profiles.get(index).getAge());
		dialoglayout = inflater.inflate(R.layout.editpopup, (ViewGroup) getCurrentFocus());
		builder1.setView(dialoglayout);
		textBox = (EditText) dialoglayout.findViewById(R.id.age);

		age.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				builder1.setCancelable(true);
				builder1.setPositiveButton("Done",
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						System.out.println("Age: " + textBox.getText().toString());
						int tempAge = Integer.parseInt(textBox.getText().toString());
						ProfilesList.profiles.get(index).setAge(tempAge);
						dialog.cancel(); 
					}
				}); 
				builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

			}
		});

		//BIRTHDATE
		my_birth.setText("Birthdate: " + ProfilesList.profiles.get(index).getMyBirth());
		dialoglayout = inflater.inflate(R.layout.editpopup, (ViewGroup) getCurrentFocus());
		builder1.setView(dialoglayout);
		textBox = (EditText) dialoglayout.findViewById(R.id.birthdate);

		my_birth.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				builder1.setCancelable(true);
				builder1.setPositiveButton("Done",
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						System.out.println("Birthdate: " + textBox.getText().toString());
						String tempBday = textBox.getText().toString();
						ProfilesList.profiles.get(index).setMyBirth(tempBday);
						dialog.cancel(); 
					}
				}); 
				builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

			}
		});

		//GENDER
		gender.setText("Gender: " + ProfilesList.profiles.get(index).getGen());
		dialoglayout = inflater.inflate(R.layout.editpopup, (ViewGroup) getCurrentFocus());
		builder1.setView(dialoglayout);
		genSelect = (RadioGroup) dialoglayout.findViewById(R.id.gender);

		gender.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				builder1.setCancelable(true);
				builder1.setPositiveButton("Done",
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						System.out.println("Gender: " + genSelect.getCheckedRadioButtonId());
						int tempGen = genSelect.getCheckedRadioButtonId();
						char gen = 'F';
						if(tempGen == 1)
						{
							gen = 'M';
						}
						else if(tempGen == 2)
						{
							gen = 'O';
						}
						ProfilesList.profiles.get(index).setGen(gen);
						dialog.cancel(); 
					}
				}); 
				builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

			}
		});

		//LOCATION
		location.setText("Location: " + ProfilesList.profiles.get(index).getLoc());
		dialoglayout = inflater.inflate(R.layout.editpopup, (ViewGroup) getCurrentFocus());
		builder1.setView(dialoglayout);
		textBox = (EditText) dialoglayout.findViewById(R.id.location);

		location.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				builder1.setCancelable(true);
				builder1.setPositiveButton("Done",
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						System.out.println("Location: " + textBox.getText().toString());
						String tempLoc = textBox.getText().toString();
						ProfilesList.profiles.get(index).setLoc(tempLoc);
						dialog.cancel(); 
					}
				}); 
				builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

			}
		});

		//EXPECTED BABY BIRTHDATE
		baby_birth.setText("Expected Baby Birthdate: " + ProfilesList.profiles.get(index).getLoc());
		dialoglayout = inflater.inflate(R.layout.editpopup, (ViewGroup) getCurrentFocus());
		builder1.setView(dialoglayout);
		textBox = (EditText) dialoglayout.findViewById(R.id.babybirth);

		baby_birth.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				builder1.setCancelable(true);
				builder1.setPositiveButton("Done",
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						System.out.println("Expected Baby Birthdate: " + textBox.getText().toString());
						String tempBabyBday = textBox.getText().toString();
						ProfilesList.profiles.get(index).setBabyBirth(tempBabyBday);
						dialog.cancel(); 
					}
				}); 
				builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

			}
		});





	}
	/*
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String pos=(String)getListAdapter().getItem(position);
		AlertDialog.Builder alerter=new AlertDialog.Builder(this);
		alerter.setPositiveButton("Done", null);
		alerter.setNegativeButton("Cancel", null);


		if(pos.equals("Name"))
		{
			textBox = (EditText) dialoglayout.findViewById(R.id.name);
		}
		else if(pos.equals("Age"))
		{
			textBox = (EditText) dialoglayout.findViewById(R.id.name);
			textBox.setText("Age");
		}
		else if(pos.equals("Birthdate"))
		{
			textBox = (EditText) dialoglayout.findViewById(R.id.name);
			textBox.setText("Client's Birthdate");
		}
		else if(pos.equals("Gender"))
		{
			genSelect = (RadioGroup) findViewById(R.id.gender);
			RadioButton fem = new RadioButton(this);
			RadioButton male = new RadioButton(this);
			RadioButton other = new RadioButton(this);
			fem.setLayoutParams (new LayoutParams 
					(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			male.setLayoutParams (new LayoutParams 
					(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			other.setLayoutParams (new LayoutParams 
					(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			genSelect.addView(fem);
			genSelect.addView(male);
			genSelect.addView(other);
		}
		else if(pos.equals("Location"))
		{
			textBox = (EditText) dialoglayout.findViewById(R.id.name);
			textBox.setText("Current location");
		}
		else if(pos.equals("Expected Baby's Birthdate"))
		{
			textBox = (EditText) dialoglayout.findViewById(R.id.name);
			textBox.setText("Expected Baby's Birthdate");
		}
		alerter.show();
	}
	 */
}
