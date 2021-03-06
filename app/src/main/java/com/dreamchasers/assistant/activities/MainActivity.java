package com.dreamchasers.assistant.activities;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.Nullable;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.app.Fragment;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.calendar.AllInOneActivity;
import com.android.calendar.CalendarController;
import com.android.calendar.agenda.AgendaFragment;

import com.dreamchasers.assistant.R;
import com.dreamchasers.assistant.fragments.NewsFeedFragment;
import com.dreamchasers.assistant.utils.FirebaseRef;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.dreamchasers.assistant.R.id.userNameTextView;


public class MainActivity extends AppCompatActivity implements RecognitionListener {

    public static final String RECEIVE_JSON = "com.dreamchasers.assistant";
   // @BindView(R.id.tabs) PagerSlidingTabStrip pagerSlidingTabStrip;
  //  @BindView(R.id.toolbar) Toolbar toolbar;
    private Toolbar toolbar;
    @BindView(R.id.calButton) Button calButton;
    @BindView(R.id.fab_button) FloatingActionButton floatingActionButton;
    @BindView(R.id.fab_button1) FloatingActionButton floatingActionButton1;
    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToogle;
    @BindView(R.id.view_textview) EditText vTextView;
    @BindView(R.id.return_textView) TextView rTextView;
    @Nullable TextView userNameText;
    @BindView(R.id.navigation_view) NavigationView navigationView;
    //@BindView(R.id.viewpager) ViewPager viewPager;
    @BindView(R.id.materialViewPager) MaterialViewPager mViewPager;
    private FirebaseRecyclerAdapter mAdapter;
    private CalendarController mController;
    private TextView mHeaderView;


    // private DatabaseReference fDataBase = FirebaseDatabase.getInstance().getReference().child("server/saving-data/sidekicks/");

    private boolean fabIsHidden = false;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private TabLayout tabLayout;
    private int tabPosition;
    private String mainServerUrl = "https://stormy-wildwood-31519.herokuapp.com/android";
    private String sendTextString;
    public final String DEFAULTUSERID = "N/A";
    private Calendar calendar1;






    public static String userKeyId;
    public Intent recognizerIntent;
    private String LOG_TAG = "VoiceRecognition";
    private SpeechRecognizer speech = null;
    public static final String TAG = MainActivity.class.getSimpleName();
    private final OkHttpClient client = new OkHttpClient();




    private BroadcastReceiver bReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(RECEIVE_JSON)) {
                String serviceJsonString = intent.getStringExtra("json");
                Log.v("cia" , serviceJsonString);

                JSONObject obj = null;
                try {
                    obj = new JSONObject(serviceJsonString);
                } catch (JSONException e) {
                    Log.v("We got an error..", "ERROR");
                    e.printStackTrace();
                }
                try {
                    obj = (JSONObject) obj.get("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                String msgType = "";

                if(!obj.has("intent")) {
                    Log.v("nera intento", "sveikutis");
                    
                }
                else if(obj.has("responseText")){
                    String msg1 = "";
                    Log.v("MSG!", "" + msg1);
                    try {
                        msg1 = (String) obj.get("responseText");
                        msg1.toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.v("MSG!", "" + msg1);

                    rTextView.setText(msg1);


                }



                else{
                    try {
                        msgType = (String) obj.get("intent");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                switch(msgType){
                    case "reminder":
                        String msg = "";
                        try {
                            msg = obj.getString("intentText");
                            rTextView.setText(msg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(getApplicationContext(),"kaimnyas", Toast.LENGTH_LONG).show();
                        Intent it = new Intent(MainActivity.this, CreateEditActivity.class);
                        Bundle b=  new Bundle();
                        String date="";
                        try {
                            date = obj.getString("date");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        b.putString("msg", msg);
                        b.putString("date", date);
                        it.putExtras(b);
                        startActivity(it);

                        break;
                }





            }
        }
    };
    LocalBroadcastManager bManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //  speech = SpeechRecognizer.createSpeechRecognizer(this);
        // speech.setRecognitionListener(this);
        speech = SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(this);

        mToogle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);





        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mHeaderView = (TextView)inflater.inflate(R.layout.agenda_header_footer, null);


        mDrawerLayout.addDrawerListener(mToogle);
        mToogle.syncState();




        calButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, AllInOneActivity.class);
                //  myIntent.putExtra("key", value); //Optional parametpackage com.dreamchasers.assistant.activities;
                startActivity(myIntent);

            }});


        toolbar = mViewPager.getToolbar();


        mController = CalendarController.getInstance(this);




        if (toolbar != null) {
                setSupportActionBar(toolbar);
                toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
                ActionBar actionBar = getSupportActionBar();
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setDisplayUseLogoEnabled(false);
                actionBar.setHomeButtonEnabled(true);
            }


/*//        setTitle("");
        if (toolbar != null) {
            setSupportActionBar(toolbar);
           // toolbar.setNavigationIcon(R.drawable.ic_navigation_white_24dp);

            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);


        }
*/

 /*       if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
*/
        CheckandSaveUserID();

      //  promptSpeechInput();





        mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getFragmentManager()) {



            @Override
            public Fragment getItem(int position) {
                Bundle bundle = new Bundle();

                switch (position % 6) {
                    case 0:
                        return NewsFeedFragment.newInstance();
                    case 1:
                        return AgendaFragment.newInstance();
//                    case 4:
//                        return AlarmClockFragment.newInstance();

                  //  new AgendaFragment(timeMillis, false);



                        //mController.sendEvent(this, CalendarController.EventType.GO_TO, null, null, -1, CalendarController.ViewType.AGENDA);
//                    case 5:
//                        bundle.putInt("TYPE", Reminder.ACTIVE);
//                        TabFragment.newInstance().setArguments(bundle);
//                        return TabFragment.newInstance();
                    default:
                        return NewsFeedFragment.newInstance();
                        //return AgendaFragment.newInstance();
                        //return NewsFeedFragment.newInstance();
                }
            }

            @Override
            public int getCount() {
                return 6;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position % 6) {
                    case 0:
                        return "Main feed";
                    case 1:
                        return "Agenda";
                    case 2:
                        return "TODO";
                    case 3:
                        return "Alarms";
                    case 4:
                        return "Habits";
                    case 5:
                        return "Reminders";
                }
                return "";
            }
        });




        FabSpeedDial fabSpeedDial = (FabSpeedDial) findViewById(R.id.fab_speed_dial);
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                // TODO: Do something with yout menu items, or return false if you don't want to show them
                return true;
            }
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                //TODO: Start some activity

                int id = menuItem.getItemId();



                switch(id){
                    case R.id.action_event:
                        CreateCalendarEvent();
                        break;
                    case R.id.action_reminder:
                        Intent caIntent = new Intent(MainActivity.this, CreateEditActivity.class);
                        startActivity(caIntent);
                        break;

                    default:
                        Log.v("AA", "None of these menu items were selected");
                        break;


                }


                return super.onMenuItemSelected(menuItem);
            }


        });








/*
        ViewPageAdapter adapter = new ViewPageAdapter(getSupportFragmentManager());
        ViewPager viewPager = mViewPager.getViewPager();
        viewPager.setAdapter(adapter);
*/


        //After set an adapter to the ViewPager
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());


       //pagerSlidingTabStrip.setViewPager(viewPager);
        //int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
       // viewPager.setPageMargin(pageMargin);


        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        tabPosition = 0;
                        floatingActionButton1.show();
                        floatingActionButton.hide();
                        vTextView.setVisibility(View.VISIBLE);
                        rTextView.setVisibility(View.VISIBLE);
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.blue,
                                "http://wallpapercave.com/wp/aZlqiAT.png");




                    case 1:
                        tabPosition = 1;
                        floatingActionButton1.hide();
                        floatingActionButton.show();
                        vTextView.setVisibility(View.INVISIBLE);
                        rTextView.setVisibility(View.INVISIBLE);
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.green,
                                "https://alistapart.com/d/438/fig-6--background-blend-mode.jpg");
                    case 2:

                        tabPosition = 2;
                        floatingActionButton.show();
                        floatingActionButton1.hide();
                        vTextView.setVisibility(View.INVISIBLE);
                        rTextView.setVisibility(View.INVISIBLE);
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.cyan, "http://i-cdn.phonearena.com/images/articles/145581-image/January.jpg");
                    case 3:
                        tabPosition = 3;
                        floatingActionButton.show();
                        floatingActionButton1.hide();
                        vTextView.setVisibility(View.INVISIBLE);
                        rTextView.setVisibility(View.INVISIBLE);

                        return HeaderDesign.fromColorResAndUrl(
                                R.color.red,
                                "http://more-sky.com/data/out/4/IMG_40740.png");
                }


                //execute others actions if needed (ex : modify you headder logo ? :/

                return null;
            }
        });


       /* TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs1);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                animateFab(tab.getPosition());

                switch (tab.getPosition()) {
                    case 0:
                        tabPosition = 0;
                        break;
                    case 1:
                        tabPosition = 1;
                        break;
                    case 2:
                        tabPosition = 2;
                        break;

                    case 3:
                        tabPosition = 3;
                        break;

                    default:
                        tabPosition = 0;
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/

        bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RECEIVE_JSON);
        bManager.registerReceiver(bReceiver, intentFilter);




        vTextView.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String sndText = vTextView.getText().toString();
                    try {
                       run1(sndText);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    return true;
                }
                return false;
            }
        });



        startService(new Intent(this, OnChildAdded.class));




    } // ONCREATE FINISHES HERE!@!@!@!@!!@!@



public void CreateCalendarEvent(){

    //Create new Event
    Time t = new Time();
    t.set(mController.getTime());
    t.second = 0;
    if (t.minute > 30) {
        t.hour++;
        t.minute = 0;
    } else if (t.minute > 0 && t.minute < 30) {
        t.minute = 30;
    }
    mController.sendEventRelatedEvent(
            this, CalendarController.EventType.CREATE_EVENT, -1, t.toMillis(true), 0, 0, 0, -1);



}
    // Check if we have user id, if not saves it into sharedpreferences.
    public void CheckandSaveUserID(){
        Log.v("usr", "LLOOOL "  + FirebaseInstanceId.getInstance().getToken());


        SharedPreferences sharedPreferences = getSharedPreferences("userKey", MODE_PRIVATE);
        String usrKey2 = sharedPreferences.getString("userKey", DEFAULTUSERID);

        if(usrKey2.equals(DEFAULTUSERID)){

            try {

                FirebaseRef.getDatabase().child("server/saving-data/sidekicks/users")
                        .orderByChild("android_id")
                        .equalTo(FirebaseInstanceId.getInstance().getToken())
                        .limitToFirst(1)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                    String userKey = childSnapshot.getKey();
                                    userKeyId = userKey;
                                    Log.v("usr", "CANCEELL " + userKeyId );

                                    SharedPreferences sharedPreferences1 = getSharedPreferences("userKey", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences1.edit();
                                    editor.putString("userKey", userKeyId);
                                    editor.apply();

                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.v("PLOVYKLA", "CANCEELL " );

                            }
                        });
            } catch(Exception e){}

        }
        else {
            Log.v("PETRAS", "asaasa" );
            FirebaseRef.getDatabase().child("server/saving-data/sidekicks/users").child(usrKey2).child("first_name").addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String userName = dataSnapshot.getValue(String.class);
                    Log.v("PETRAS", "asaasa" + userName );

                    if(userName != null && userNameText != null){
                        userNameText = (TextView) findViewById(userNameTextView);
                        userNameText.setText(userName);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }




    }

    @Override
    protected void onDestroy() {
        bManager.unregisterReceiver(bReceiver);
        super.onDestroy();

    }

    @OnClick(R.id.fab_button)
    public void fabClicked() {
        //Create new Event
        Time t = new Time();
        t.set(mController.getTime());
        t.second = 0;
        if (t.minute > 30) {
            t.hour++;
            t.minute = 0;
        } else if (t.minute > 0 && t.minute < 30) {
            t.minute = 30;
        }
        mController.sendEventRelatedEvent(
                this, CalendarController.EventType.CREATE_EVENT, -1, t.toMillis(true), 0, 0, 0, -1);


    }


    @OnClick(R.id.fab_button1)
    public void mainFabClicked() {
        promptSpeechInput();





    }

    // Function to sent user to messenger interface


    public void goToMessenger(){

        Uri uri = Uri.parse("fb-messenger://user/");
        uri = ContentUris.withAppendedId(uri,580366185473843L);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

    }

    /**
     * Showing google speech input dialog
     */
    public void promptSpeechInput() {

   //     if (speech == null ){
     //       speech = SpeechRecognizer.createSpeechRecognizer(this);
       //     speech.setRecognitionListener(this);
        //}


         recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 3000);
        try {
            speech.startListening(recognizerIntent);
          //k\  startActivityForResult(recognizerIntent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }



    }

    /**
     * Receiving speech input, not in use right one.
     */

    
    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                   vTextView.setText(result.get(0));
                }
                break;
            }

        }
    } */



    @Override
    public void onBeginningOfSpeech() {
        Log.i(LOG_TAG, "onBeginningOfSpeech");

    }
    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(LOG_TAG, "onBufferReceived: " + buffer);
    }

    @Override
    public void onEndOfSpeech() {
        Log.i(LOG_TAG, "onEndOfSpeech");

    }

    private void sendVoiceInput() {

        try {
            String finalVoiceInput = vTextView.getText().toString();

            run(finalVoiceInput);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onError(int errorCode) {
        String errorMessage = getErrorText(errorCode);
        Log.d(LOG_TAG, "FAILED " + errorMessage);
     //   vTextView.setText(errorMessage);

      //  promptSpeechInput();
    }

    @Override
    public void onEvent(int arg0, Bundle arg1) {
        Log.i(LOG_TAG, "onEvent");
    }

    @Override
    public void onPartialResults(Bundle arg0) {
        Log.i(LOG_TAG, "onPartialResults");
        ArrayList<String> matches = arg0
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text = "";
        for (String result : matches)
            text += result + "\n";

        vTextView.setText(text);


    }

    @Override
    public void onReadyForSpeech(Bundle arg0) {
        Log.i(LOG_TAG, "onReadyForSpeech");
    }

    @Override
    public void onResults(Bundle results) {
        Log.i(LOG_TAG, "onResults");
    //    speech.stopListening();

        sendVoiceInput();
        //speech.stopListening();
        //speech.cancel();
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.i(LOG_TAG, "onRmsChanged: " + rmsdB);

    }

    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }


    public void showFab() {
        floatingActionButton.show();
        fabIsHidden = false;
    }

    public void animateFab(int position) {
        switch (position) {
            case 0:
                floatingActionButton1.show();
                floatingActionButton.hide();
                vTextView.setVisibility(View.VISIBLE);
                rTextView.setVisibility(View.VISIBLE);

                break;
            case 1:
                floatingActionButton.show();
                floatingActionButton1.hide();
                vTextView.setVisibility(View.GONE);
                rTextView.setVisibility(View.GONE);

                break;
            case 2:
                floatingActionButton.show();
                floatingActionButton1.hide();
                vTextView.setVisibility(View.GONE);
                rTextView.setVisibility(View.GONE);

                break;

            case 3:
                floatingActionButton.show();
                floatingActionButton1.hide();
                vTextView.setVisibility(View.GONE);
                rTextView.setVisibility(View.GONE);
                break;

            default:
                floatingActionButton1.show();
                floatingActionButton.hide();
                vTextView.setVisibility(View.VISIBLE);
                rTextView.setVisibility(View.VISIBLE);

                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        speech = SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(this);

        switch (tabPosition) {
            case 0:
                floatingActionButton1.show();
                floatingActionButton.hide();
                vTextView.setVisibility(View.VISIBLE);
                rTextView.setVisibility(View.VISIBLE);

                break;
            case 1:
                floatingActionButton.show();
                floatingActionButton1.hide();
                vTextView.setVisibility(View.GONE);
                rTextView.setVisibility(View.GONE);

                break;
            case 2:
                floatingActionButton.show();
                floatingActionButton1.hide();
                vTextView.setVisibility(View.GONE);
                rTextView.setVisibility(View.GONE);

                break;

            case 3:
                floatingActionButton.show();
                floatingActionButton1.hide();
                vTextView.setVisibility(View.GONE);
                rTextView.setVisibility(View.GONE);

                break;
            default:
                floatingActionButton1.show();
                floatingActionButton.hide();
                vTextView.setVisibility(View.VISIBLE);
                rTextView.setVisibility(View.VISIBLE);

                break;
        }

        //if(speech == null){
          //  promptSpeechInput();
        //}

       // speech.cancel();
     //   promptSpeechInput();

    }


    @Override
    protected void onPause(){
        super.onPause();
        if (speech != null) {
         //   speech.stopListening();

             speech.destroy();
            Log.i(LOG_TAG, "destroy212");
        }
    }









    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent preferenceIntent = new Intent(this, PreferenceActivity.class);
                startActivity(preferenceIntent);
                return true;
            case R.id.action_about:
                Intent aboutIntent = new Intent(this, AboutActivity.class);
                startActivity(aboutIntent);
                return true;
            case R.id.sync1:
                String newId = FirebaseRef.getDatabase().child("server/saving-data/sidekicks").child("sync").push().getKey();
                FirebaseRef.getDatabase().child("server/saving-data/sidekicks").child("sync").child(newId).child("id").setValue(FirebaseInstanceId.getInstance().getToken());
                FirebaseRef.getDatabase().child("server/saving-data/sidekicks").child("sync").child(newId).child("type").setValue("android");
              //  fDataBase.child("sync").push().
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://stormy-wildwood-31519.herokuapp.com/auth/facebook?id="+newId));
                startActivity(browserIntent);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        FirebaseRef.getDatabase().child("server/saving-data/sidekicks/users").child(FirebaseInstanceId.getInstance().getToken()).setValue(null);
                        CheckandSaveUserID();
                    }
                }, 3000);
                Log.i("messenger", "syncing");
                return true;


        }


        if(mToogle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }






    public void run(String sendText) throws Exception {
        String sendTextString;
        sendTextString = sendText;

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        //  String sendTextString = vTextView.getText().toString();

        String paramString =
                "{\n\"object\":page"+ ",\n" +
                        "\"entry\":[\n" +
                        "{\n\"id\":580366185473843"+ ",\n" +
                        "\"time\":1458692752478"+ ",\n" +
                        "\"messaging\":[\n" +

                        "{\n\"sender\":{\n" +
                        "            \"id\":\"1063172630393040\"\n" +
                        "          },\n" +
                        "\"recipient\":{\n" +
                        "            \"id\":\"580366185473843\"\n" +

                        "},\n" +
                        "\"timestamp\":1458692752478"+ ",\n" +
                        "\"message\":{\n" +
                        "\"mid\":\"mid.145\""+ ",\n" +
                        "\"seq\":14"+ ",\n" +
                        "\"androidID\":\""+ FirebaseInstanceId.getInstance().getToken() + "\",\n" +
                        "\"text\":" +"\"" + sendTextString + "\""+ "\n" +
                        "}\n"
                        + "}\n"
                        + "]\n" + "}\n" + "]\n" + "}\n" ;




        Log.e("params", paramString.toString());
        // JSONArray parameter1 = new JSONArray(paramString);

        JSONObject parameter = new JSONObject(paramString);
        OkHttpClient client = new OkHttpClient();
        Log.e("params", parameter.toString());

        RequestBody body = RequestBody.create(JSON, parameter.toString());
        Request request = new Request.Builder()
                .url(mainServerUrl)
                .post(body)
                .addHeader("content-type", "application/json; charset=utf-8")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("responseError", call.request().body().toString());

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
               //   Log.e("response", response.body().string());
                Log.e("response", "@@@@@@@@@@@@@@@@@@@@@@@");

             //   final String response1 = response.body().string();


                final String json = response.body().string();
 //Galima tiesiai response body i object mesti

              /*  try {
                    JSONObject jsnobject = new JSONObject(json);

                    JSONArray jsonArray = jsnobject.getJSONArray("reminder");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject reminder = null;

                        reminder = jsonArray.getJSONObject(i);
                        String reminderValue = reminder.getString("value");
                        Log.e("jsonObject", "" + reminder);

                        Log.e("ReminderValue", "" + reminderValue);


                    }
                } catch (Exception exception){
                    exception.printStackTrace();
                }


*/

                Log.e("response", " cia turetu buti n   " + response.toString());

               // JsonArray jsonArray = response.
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //ijunkBroadCasta();
                        rTextView.setText(json);
                    }
                });

            }


        });

    }

////

    // @@@ Run1

    public void run1(String sendText) {

        String sendTextString;
        sendTextString = sendText;
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
// //////sa121
        OkHttpClient client1 = new OkHttpClient();



        Request request1 = new Request.Builder()
                .addHeader("Authorization", "Bearer UUGTBYRVSCXBQRC72NI2OQGOPK63EFGG")
                .url("https://api.wit.ai/message?20161208&q=remind%20me%20to%20buy%20grocories%20tomorrow%205%20pm")
                .build();

        client.newCall(request1).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.e("responseError", call.request().body().toString());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {

                    Log.e("response", response.body().string() + " asdasdsadadsadsadsa");
                }
            }
        });
    }




}