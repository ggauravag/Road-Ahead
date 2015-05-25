package com.example.gcmdemo;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class ReportFragment extends Fragment
{
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			View v = inflater.inflate(R.layout.report_fm, container, false);
			
			final Button emailbutton = (Button)v.findViewById(R.id.button1);
			
			emailbutton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent email = new Intent(Intent.ACTION_SEND);
					
					email.putExtra(Intent.EXTRA_EMAIL, new String[] {"ggauravag@gmail.com","abhishek.roadaheadtech@gmail.com"});
					email.setType("text/plain");
					try {
				         startActivity(Intent.createChooser(email, "Send mail..."));
				         Log.i("Finished sending email...", "");
				      } catch (android.content.ActivityNotFoundException ex) {
				         Toast.makeText(NoticeActivity.context,"There is no email client installed.", Toast.LENGTH_SHORT).show();
				      }
				}
			});
			
			return v;
		}
}
