package com.example.gcmdemo;

import java.util.ArrayList;
import java.util.HashMap;

import com.source.Message;
import com.source.MessageUtility;

import android.app.Activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class NoticeActivity extends Activity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	public static final String TAG = "GCM";
	public static Context context;
	public static final String REG_ID = "regId";
	private static final String APP_VERSION = "appVersion";
	private static final String MyPrefs = "MyPrefs";
	
	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;
	
	public ExpandableListView messageView;
	

	public void removeRegistrationId()
	{
		final SharedPreferences prefs = getSharedPreferences(MyPrefs, MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.remove(REG_ID);
		editor.commit();
	}

	public void loadMessages()
	{
		Log.i(TAG,"Messages being loaded !");
		MessageUtility utility = new MessageUtility(this);
		utility.open();
		final ArrayList<Message> messages = (ArrayList<Message>) utility.getAllMessages();
		utility.close();
		Log.i(TAG,"Number of messages : "+messages.size());
		ExpandableListAdapter adapter = new ExpandableListAdapter(getApplicationContext(), messages);
		if(messageView != null)
		{
			messageView.setAdapter(adapter);
			Log.i(TAG,"Success when messageview not null");
		}
		else
		{
			messageView = (ExpandableListView)findViewById(R.id.expandableListView1);	
			messageView.setAdapter(adapter);
			Log.i(TAG,"Success when messageview null");
		}
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.activity_notice);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = "Messages";
		messageView = (ExpandableListView)findViewById(R.id.expandableListView1);	
		//loadMessages();
		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragmeif(ponts
		
		
		
		FragmentManager fragmentManager = getFragmentManager();
		
		if(position == 2)
		{
			mTitle = "About Us";
			fragmentManager.beginTransaction().replace(R.id.container, new AboutUsFragment()).commit();
		}
		else if(position == 1)
		{
			mTitle = "Report";
			fragmentManager.beginTransaction().replace(R.id.container, new ReportFragment()).commit();
		}
		else if(position == 0)
		{
			mTitle = "Messages";
			fragmentManager.beginTransaction().replace(R.id.container, new MessageFragment(getApplicationContext())).commit();
		}
		else if(position == 3)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setMessage("Do you really want to log out ?");
			builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					removeRegistrationId();
					Intent i = new Intent(context,RegistrationActivity.class);
					startActivity(i);
				}
			});
			
			builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Toast.makeText(context, "Phew ! Be careful with log out button.", 0).show();
					
				}
			});
			
			builder.create().show();
			
			
		}
		else
		{
		fragmentManager
				.beginTransaction()
				.replace(R.id.container,
						PlaceholderFragment.newInstance(position + 1)).commit();
		}
		Log.i(TAG,"Navigation Drawer Selected");
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_section1);
			break;
		case 2:
			mTitle = getString(R.string.title_section2);
			break;
		case 3:
			mTitle = getString(R.string.title_section3);
			break;
		case 4:
			mTitle = getString(R.string.title_section4);
		}
		
		Log.i(TAG,"Section Attached, Number : "+number+", Title : "+mTitle);
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
		Log.i(TAG,"Action Bar Restored");
	}

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		loadMessages();
		//Log.i(TAG,"On Resume called");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.notice, menu);
			restoreActionBar();
			return true;
		}
		Log.i(TAG,"Option Menu Created");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if(id == R.id.action_example)
		{
			Log.i(TAG,"Delete button clicked !");
			AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
			dialogBuilder.setMessage("Do you really want to delete all stored messages and data ? ");
			dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					MessageUtility utility = new MessageUtility(context);
					utility.open();
					utility.deleteAll();
					utility.close();
					loadMessages();
				}

				
			});
			
			
			dialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Log.i(TAG,"No Button clicked");
				}
			});
			
			dialogBuilder.create().show();
		}
		
		
		
		Log.i(TAG,"Action Bar Clicked");
		return super.onOptionsItemSelected(item);
	
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_notice,
					container, false);
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((NoticeActivity) activity).onSectionAttached(getArguments()
					.getInt(ARG_SECTION_NUMBER));
		}
	}

}
