package com.dreamchasers.assistant.activities;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.dreamchasers.assistant.R;
import com.dreamchasers.assistant.activities.shortcut.CreateEditShortcut;
import com.dreamchasers.assistant.activities.shortcut.testas;
import com.dreamchasers.assistant.adapters.ReminderAdapter;
import com.dreamchasers.assistant.adapters.ViewPageAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.R.attr.data;
import static com.dreamchasers.assistant.R.color.error;
import static com.dreamchasers.assistant.R.id.rTextView;



public class MainActivity extends AppCompatActivity implements ReminderAdapter.RecyclerListener, RecognitionListener {

    public static final String RECEIVE_JSON = "com.dreamchasers.assistant";
    @BindView(R.id.tabs) PagerSlidingTabStrip pagerSlidingTabStrip;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fab_button) FloatingActionButton floatingActionButton;
    @BindView(R.id.fab_button1) FloatingActionButton floatingActionButton1;
    @BindView(R.id.vTextView) EditText vTextView;
    @BindView(R.id.rTextView) TextView rTextView;
    @BindView(R.id.viewpager) ViewPager viewPager;

    private DatabaseReference mDatabase;
// ...

    private boolean fabIsHidden = false;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private TabLayout tabLayout;
    private int tabPosition;
    private String mainServerUrl = "https://stormy-wildwood-31519.herokuapp.com/android";
    private String sendTextString;

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
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
        }




        ViewPageAdapter adapter = new ViewPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);


        pagerSlidingTabStrip.setViewPager(viewPager);
        int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        viewPager.setPageMargin(pageMargin);


        promptSpeechInput();
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs1);
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
        });

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




    } // onCreate finishes here@@@@2





    @Override
    protected void onDestroy() {
        bManager.unregisterReceiver(bReceiver);
        super.onDestroy();
    }

    @OnClick(R.id.fab_button)
    public void fabClicked() {
        Intent intent = new Intent(this, CreateEditActivity.class);
        startActivity(intent);


    }


    @OnClick(R.id.fab_button1)
    public void mainFabClicked() {
        promptSpeechInput();

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


    @Override
    public void hideFab() {
        floatingActionButton.hide();
        fabIsHidden = true;
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            case R.id.sync:
                Log.i("messenger", "syncing");
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
///


}