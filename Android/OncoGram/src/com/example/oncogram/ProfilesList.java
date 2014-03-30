package com.example.oncogram;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;


public class ProfilesList extends Activity {
	static ArrayList<Profile> profiles = new ArrayList<Profile>();
	ArrayList<String> clientNames = new ArrayList<String>();
	View dialoglayout;
	EditText textBox;
	String tempName;
	int selectedIndex = -1;
	Intent intent;
	static ArrayAdapter<String> adapter;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profiles);

		intent = getIntent();
		selectedIndex = intent.getIntExtra("Index", -1);
		ListView profileList = (ListView)findViewById(R.id.profileList);
		Button newProfile = (Button)findViewById(R.id.newProfile);
		//TODO Populate ListView with Clientnames
		for(Profile p: profiles)
		{
			clientNames.add(p.getName());
		}
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, clientNames); 
		profileList.setAdapter(adapter);

		this.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO go to profile page for that person
				Intent i= new Intent(getApplicationContext(), ProfilePage.class);
				i.putExtra("Index", selectedIndex);
				startActivity(i);
			}
		});

		//builder1.setMessage("Write your message here.");
		final EditText input = new EditText(this);
		//alert.setView(input);

		


		profileList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> l, View v, int position, long id) {
				Intent i= new Intent(getApplicationContext(), ProfilePage.class);
				i.putExtra("Index", position);
				startActivity(i);
			}

		});

		newProfile.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LayoutInflater inflater = LayoutInflater.from(ProfilesList.this);
				   final View dialogview = inflater.inflate(R.layout.namepopup, null);
				   AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(ProfilesList.this);
				   dialogbuilder.setTitle("Login");
				   dialogbuilder.setCancelable(true);
				   dialogbuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							EditText textBox = (EditText) dialogview.findViewById(R.id.name);
							System.out.println("Name: " + textBox.getText().toString());
							tempName = textBox.getText().toString();
							//TODO Check if has name. If so, go to profile page
							if(!tempName.equals(null))
							{
								Profile p = new Profile(tempName);
								profiles.add(p);
								adapter.add(tempName);
								Intent i= new Intent(getApplicationContext(), ProfilePage.class);
								i.putExtra("Index", profiles.size() - 1);
								startActivity(i);
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
			}
		});

	}

	private void setOnClickListener(OnClickListener onClickListener) {
		// TODO Auto-generated method stub

	}

}