package com.example.oncogram;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class PopUp extends Activity implements OnClickListener {
	LinearLayout layoutOfPopup;
	PopupWindow popupMessage;
	Button newProf, insidePopUpDone, insidePopUpCancel;
	EditText name;
	private String myName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profiles);
		init();
		popupInit();
	}

	public void init() {
		newProf = (Button) findViewById(R.id.newProf);
	// popupText = new TextView(this);
		name = new EditText(this);
		name.setHint("Name");
		insidePopUpDone = new Button(this);
		layoutOfPopup = new LinearLayout(this);
		insidePopUpDone.setText("Done");
		insidePopUpCancel = new Button(this);
		layoutOfPopup = new LinearLayout(this);
		insidePopUpCancel.setText("Cancel");
		//popupText.setText("This is Popup Window.press OK to dismiss it.");
		//popupText.setPadding(0, 0, 0, 20);
		layoutOfPopup.setOrientation(1);
		//layoutOfPopup.addView(popupText);
		layoutOfPopup.addView(insidePopUpDone);
		layoutOfPopup.addView(insidePopUpCancel);
	}

	public void popupInit() {
		newProf.setOnClickListener(this);
		insidePopUpDone.setOnClickListener(this);
		insidePopUpCancel.setOnClickListener(this);
		popupMessage = new PopupWindow(layoutOfPopup, LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		popupMessage.setContentView(layoutOfPopup);
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.newProf) {
			popupMessage.showAsDropDown(newProf, 0, 0);
			myName = name.getText().toString();
		}

		else {
			popupMessage.dismiss();
		}
	}
	
	public String getName()
	{
		return myName;
	}
}

