package org.intelehealth.ezazi.activities.homeActivity;

import static org.intelehealth.ezazi.utilities.StringUtils.en__as_dob;
import static org.intelehealth.ezazi.utilities.StringUtils.en__bn_dob;
import static org.intelehealth.ezazi.utilities.StringUtils.en__gu_dob;
import static org.intelehealth.ezazi.utilities.StringUtils.en__hi_dob;
import static org.intelehealth.ezazi.utilities.StringUtils.en__kn_dob;
import static org.intelehealth.ezazi.utilities.StringUtils.en__ml_dob;
import static org.intelehealth.ezazi.utilities.StringUtils.en__mr_dob;
import static org.intelehealth.ezazi.utilities.StringUtils.en__or_dob;
import static org.intelehealth.ezazi.utilities.StringUtils.en__ru_dob;
import static org.intelehealth.ezazi.utilities.StringUtils.en__ta_dob;
import static org.intelehealth.ezazi.utilities.StringUtils.en__te_dob;
import static org.intelehealth.ezazi.utilities.StringUtils.getFullMonthName;

import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.WorkManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import org.intelehealth.apprtc.ChatActivity;
import org.intelehealth.apprtc.CompleteActivity;
import org.intelehealth.apprtc.data.Manager;
import org.intelehealth.apprtc.utils.FirebaseUtils;
import org.intelehealth.ezazi.R;
import org.intelehealth.ezazi.activities.activePatientsActivity.ActivePatientAdapter;
import org.intelehealth.ezazi.activities.loginActivity.LoginActivity;
import org.intelehealth.ezazi.activities.searchPatientActivity.SearchPatientActivity;
import org.intelehealth.ezazi.activities.settingsActivity.SettingsActivity;
import org.intelehealth.ezazi.app.AppConstants;
import org.intelehealth.ezazi.app.IntelehealthApplication;
import org.intelehealth.ezazi.database.dao.EncounterDAO;
import org.intelehealth.ezazi.database.dao.ObsDAO;
import org.intelehealth.ezazi.database.dao.VisitsDAO;
import org.intelehealth.ezazi.models.ActivePatientModel;
import org.intelehealth.ezazi.models.CheckAppUpdateRes;
import org.intelehealth.ezazi.models.DownloadMindMapRes;
import org.intelehealth.ezazi.models.dto.EncounterDTO;
import org.intelehealth.ezazi.models.dto.ObsDTO;
import org.intelehealth.ezazi.models.dto.VisitDTO;
import org.intelehealth.ezazi.networkApiCalls.ApiClient;
import org.intelehealth.ezazi.networkApiCalls.ApiInterface;
import org.intelehealth.ezazi.services.firebase_services.CallListenerBackgroundService;
import org.intelehealth.ezazi.services.firebase_services.DeviceInfoUtils;
import org.intelehealth.ezazi.syncModule.SyncUtils;
import org.intelehealth.ezazi.utilities.DialogUtils;
import org.intelehealth.ezazi.utilities.DownloadMindMaps;
import org.intelehealth.ezazi.utilities.FileUtils;
import org.intelehealth.ezazi.utilities.Logger;
import org.intelehealth.ezazi.utilities.NetworkConnection;
import org.intelehealth.ezazi.utilities.OfflineLogin;
import org.intelehealth.ezazi.utilities.SessionManager;
import org.intelehealth.ezazi.utilities.StringUtils;
import org.intelehealth.ezazi.utilities.UuidDictionary;
import org.intelehealth.ezazi.utilities.exception.DAOException;
import org.intelehealth.ezazi.widget.materialprogressbar.CustomProgressDialog;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Home Screen
 */

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private static final String ACTION_NAME = "org.intelehealth.app.RTC_MESSAGING_EVENT";
    SessionManager sessionManager = null;
    //ProgressDialog TempDialog;
    private ProgressDialog mSyncProgressDialog, mRefreshProgressDialog, mResetSyncDialog;
    CountDownTimer CDT;
    private boolean hasLicense = false;
    int i = 5;
    String encUUID_visitComplete = "";

    TextView lastSyncTextView, locationSetupTextView;
    TextView lastSyncAgo;
    CardView manualSyncButton;

    EditText etvSearchVisit;
    ImageView ivFilterAction;
    CharSequence search = "";
    //IntentFilter filter;
    //Myreceiver reMyreceive;
    SyncUtils syncUtils = new SyncUtils();
    CardView c2, c3, c4, c5, c6;
    private String key = null;
    private String licenseUrl = null;

    Context context;
    CustomProgressDialog customProgressDialog;
    private String mindmapURL = "";
    private DownloadMindMaps mTask;
    ProgressDialog mProgressDialog;
    private ImageView ivSync;

    private int versionCode = 0;
    private CompositeDisposable disposable = new CompositeDisposable();
    TextView findPatients_textview, todaysVisits_textview,
            activeVisits_textview, appointment_textview, followup_textview, videoLibrary_textview, help_textview, tvTodayVisitsBadge, tvActiveVisitsBadge;
    private ObjectAnimator syncAnimator;

    /*eZazi*/
    private SQLiteDatabase db;
    private RecyclerView mActiveVisitsRecyclerView;
    private ActivePatientAdapter mActivePatientAdapter;
    private ArrayList<String> listPatientUUID = new ArrayList<String>();
    int limit = 120, offset = 0;
    boolean fullyLoaded = false;
    EncounterDAO encounterDAO = new EncounterDAO();
    EncounterDTO encounterDTO = null;
    String encounterUUID = "";
    ObsDAO obsDAO = new ObsDAO();
    List<ObsDTO> obsDTOList = null;

    /*eZazi End*/

    private void saveToken() {
        Manager.getInstance().setBaseUrl("https://" + sessionManager.getServerUrl());
        // save fcm reg. token for chat (Video)
        FirebaseUtils.saveToken(this, sessionManager.getProviderID(), IntelehealthApplication.getInstance().refreshedFCMTokenID, sessionManager.getAppLanguage());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.v(TAG, "onNewIntent");
        catchFCMMessageData();
    }

    private void catchFCMMessageData() {
        // get the chat notification click info
        if (getIntent().getExtras() != null) {
            Logger.logV(TAG, " getIntent - " + getIntent().getExtras().getString("actionType"));
            Bundle remoteMessage = getIntent().getExtras();
            try {
                if (remoteMessage.containsKey("actionType") && remoteMessage.getString("actionType").equals("TEXT_CHAT")) {
                    //Log.d(TAG, "actionType : TEXT_CHAT");
                    String fromUUId = remoteMessage.getString("toUser");
                    String toUUId = remoteMessage.getString("fromUser");
                    String patientUUid = remoteMessage.getString("patientId");
                    String visitUUID = remoteMessage.getString("visitId");
                    String patientName = remoteMessage.getString("patientName");
                    JSONObject connectionInfoObject = new JSONObject();
                    connectionInfoObject.put("fromUUID", fromUUId);
                    connectionInfoObject.put("toUUID", toUUId);
                    connectionInfoObject.put("patientUUID", patientUUid);

                    Intent intent = new Intent(ACTION_NAME);
                    intent.putExtra("visit_uuid", visitUUID);
                    intent.putExtra("connection_info", connectionInfoObject.toString());
                    intent.setComponent(new ComponentName("org.intelehealth.unicef", "org.intelehealth.unicef.utilities.RTCMessageReceiver"));
                    getApplicationContext().sendBroadcast(intent);

                    Intent chatIntent = new Intent(this, ChatActivity.class);
                    chatIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    chatIntent.putExtra("patientName", patientName);
                    chatIntent.putExtra("visitUuid", visitUUID);
                    chatIntent.putExtra("patientUuid", patientUUid);
                    chatIntent.putExtra("fromUuid", fromUUId);
                    chatIntent.putExtra("toUuid", toUUId);
                    startActivity(chatIntent);

                } else if (remoteMessage.containsKey("actionType") && remoteMessage.getString("actionType").equals("VIDEO_CALL")) {
                    //Log.d(TAG, "actionType : VIDEO_CALL");
                    Intent in = new Intent(this, CompleteActivity.class);
                    String roomId = remoteMessage.getString("roomId");
                    String doctorName = remoteMessage.getString("doctorName");
                    String nurseId = remoteMessage.getString("nurseId");
                    boolean isOldNotification = false;
                    if (remoteMessage.containsKey("timestamp")) {
                        String timestamp = remoteMessage.getString("timestamp");

                        Date date = new Date();
                        if (timestamp != null) {
                            date.setTime(Long.parseLong(timestamp));
                            SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss"); //this format changeable
                            dateFormatter.setTimeZone(TimeZone.getDefault());

                            try {
                                Date ourDate = dateFormatter.parse(dateFormatter.format(date));
                                long seconds = 0;
                                if (ourDate != null) {
                                    seconds = Math.abs(new Date().getTime() - ourDate.getTime()) / 1000;
                                }
                                Log.v(TAG, "Current time - " + new Date());
                                Log.v(TAG, "Notification time - " + ourDate);
                                Log.v(TAG, "seconds - " + seconds);
                                if (seconds >= 30) {
                                    isOldNotification = true;
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }


                    in.putExtra("roomId", roomId);
                    in.putExtra("isInComingRequest", true);
                    in.putExtra("doctorname", doctorName);
                    in.putExtra("nurseId", nurseId);

                    int callState = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getCallState();
                    if (callState == TelephonyManager.CALL_STATE_IDLE && !isOldNotification) {
                        startActivity(in);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sessionManager = new SessionManager(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextAppearance(this, R.style.ToolbarTheme);
        toolbar.setTitleTextColor(Color.WHITE);
        DeviceInfoUtils.saveDeviceInfo(this);

        catchFCMMessageData();
        String language = sessionManager.getAppLanguage();
        if (!language.equalsIgnoreCase("")) {
            Locale locale = new Locale(language);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }

        setTitle(R.string.title_activity_login);
        context = HomeActivity.this;
        customProgressDialog = new CustomProgressDialog(context);
        mResetSyncDialog = new ProgressDialog(HomeActivity.this, R.style.AlertDialogStyle);
        mResetSyncDialog.setTitle(R.string.app_sync);
        mResetSyncDialog.setCancelable(false);
        mResetSyncDialog.setProgress(i);
        //reMyreceive = new Myreceiver();
        //filter = new IntentFilter("lasysync");

        sessionManager.setCurrentLang(getResources().getConfiguration().locale.toString());

        checkAppVer();  //auto-update feature.

        Logger.logD(TAG, "onCreate: " + getFilesDir().toString());
        /*NEW*/
        mActiveVisitsRecyclerView = findViewById(R.id.rcvActiveVisits);
        LinearLayoutManager reLayoutManager = new LinearLayoutManager(getApplicationContext());
        mActiveVisitsRecyclerView.setLayoutManager(reLayoutManager);
        /*mActiveVisitsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!fullyLoaded && newState == RecyclerView.SCROLL_STATE_IDLE &&
                        reLayoutManager.findLastVisibleItemPosition() == mActivePatientAdapter.getItemCount() - 1) {
                    Toast.makeText(HomeActivity.this, R.string.loading_more, Toast.LENGTH_SHORT).show();
                    offset += limit;
                    List<ActivePatientModel> allPatientsFromDB = doQuery(offset);
                    if (allPatientsFromDB.size() < limit) {
                        fullyLoaded = true;
                    }

                    if (!allPatientsFromDB.isEmpty()) {
                        mActivePatientAdapter.activePatientModels.addAll(allPatientsFromDB);
                        mActivePatientAdapter.notifyDataSetChanged();
                    }
                }
            }
        });*/
        db = AppConstants.inteleHealthDatabaseHelper.getWriteDb();
        loadVisits();

        findViewById(R.id.tvPatientsMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*//Loads the config file values and check for the boolean value of privacy key.
                ConfigUtils configUtils = new ConfigUtils(HomeActivity.this);
                if (configUtils.privacy_notice()) {
                    Intent intent = new Intent(HomeActivity.this, PrivacyNotice_Activity.class);
                    startActivity(intent);
                } else {
                    //Clear HouseHold UUID from Session for new registration
                    //  sessionManager.setHouseholdUuid("");
                    Intent intent = new Intent(HomeActivity.this, IdentificationActivity.class);
                    startActivity(intent);
                }*/

                Intent intent = new Intent(HomeActivity.this, SearchPatientActivity.class);
                startActivity(intent);
            }
        });


        //Search pateint code
        etvSearchVisit = findViewById(R.id.etvSearchVisit);
        ivFilterAction = findViewById(R.id.ivFilterAction);
        ivFilterAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        etvSearchVisit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mActivePatientAdapter.getFilter().filter(charSequence);
                search = charSequence;

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        findViewById(R.id.tvHelpMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumberWithCountryCode = "+917005308163";
                String message =
                        getString(R.string.hello_my_name_is) + " " + sessionManager.getChwname() + " " +
                                getString(R.string.i_need_assistance);

                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(
                                String.format("https://api.whatsapp.com/send?phone=%s&text=%s",
                                        phoneNumberWithCountryCode, message))));
            }
        });
        findViewById(R.id.tvSyncMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkConnected()) {
                    Toast.makeText(context, getString(R.string.syncInProgress), Toast.LENGTH_LONG).show();
                    //ivSync.clearAnimation();
                    //syncAnimator.start();
                    syncUtils.syncForeground("home");
                } else {
                    Toast.makeText(context, context.getString(R.string.failed_synced), Toast.LENGTH_LONG).show();
                }
            }
        });

        if (sessionManager.isFirstTimeLaunched()) {
            mSyncProgressDialog = new ProgressDialog(HomeActivity.this, R.style.AlertDialogStyle); //thats how to add a style!
            mSyncProgressDialog.setTitle(R.string.syncInProgress);
            mSyncProgressDialog.setCancelable(false);
            mSyncProgressDialog.setProgress(i);
            mSyncProgressDialog.show();

            syncUtils.initialSync("home");
        } else {
            // if initial setup done then we can directly set the periodic background sync job
            WorkManager.getInstance().enqueueUniquePeriodicWork(AppConstants.UNIQUE_WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, AppConstants.PERIODIC_WORK_REQUEST);
            saveToken();
            requestPermission();
        }
        showProgressbar();
        /*END*/
        /*lastSyncTextView = findViewById(R.id.lastsynctextview);
        locationSetupTextView = findViewById(R.id.locationTV);
        lastSyncAgo = findViewById(R.id.lastsyncago);
        manualSyncButton = findViewById(R.id.manualsyncbutton);
//        manualSyncButton.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
//        c1 = findViewById(R.id.cardview_newpat);
        c2 = findViewById(R.id.cardview_find_patient);
        c3 = findViewById(R.id.cardview_today_patient);
        c4 = findViewById(R.id.cardview_active_patients);
        c5 = findViewById(R.id.cardview_video_libraby);
        c6 = findViewById(R.id.cardview_help_whatsapp);
        findViewById(R.id.cardview_appointment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AppointmentListingActivity.class);
                startActivity(intent);
            }
        });
        ((TextView) findViewById(R.id.tvLocation)).setText(String.format("%s: %s", getText(R.string.location), sessionManager.getLocationName()));

        tvTodayVisitsBadge = findViewById(R.id.tvTodayVisitsBadge);
        tvActiveVisitsBadge = findViewById(R.id.tvActiveVisitsBadge);
        //card textview referrenced to fix bug of localization not working in some cases...
        *//*newPatient_textview = findViewById(R.id.newPatient_textview);
        newPatient_textview.setText(R.string.new_patient);*//*

        findPatients_textview = findViewById(R.id.findPatients_textview);
        findPatients_textview.setText(R.string.find_patient);

        todaysVisits_textview = findViewById(R.id.todaysVisits_textview);
        todaysVisits_textview.setText(R.string.today_visits);

        activeVisits_textview = findViewById(R.id.activeVisits_textview);
        activeVisits_textview.setText(R.string.active_visits);

        followup_textview = findViewById(R.id.followUpVisittxt);
        followup_textview.setText(R.string.title_follow_up);

        appointment_textview = findViewById(R.id.appointment_textview);
        appointment_textview.setText(R.string.doctor_appointments);

        videoLibrary_textview = findViewById(R.id.videoLibrary_textview);
        videoLibrary_textview.setText(R.string.video_library);

        help_textview = findViewById(R.id.help_textview);
        help_textview.setText(R.string.Whatsapp_Help_Cardview);

        // manualSyncButton.setText(R.string.sync_now);
//        manualSyncButton.setText(R.string.refresh);

        //Help section of watsapp...
        c6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumberWithCountryCode = "+917005308163";
                String message =
                        getString(R.string.hello_my_name_is) + " " + sessionManager.getChwname() + " " +
                                *//*" from " + sessionManager.getState() + *//*getString(R.string.i_need_assistance);

                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(
                                String.format("https://api.whatsapp.com/send?phone=%s&text=%s",
                                        phoneNumberWithCountryCode, message))));
            }
        });

        *//*c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Loads the config file values and check for the boolean value of privacy key.
                ConfigUtils configUtils = new ConfigUtils(HomeActivity.this);
                if (configUtils.privacy_notice()) {
                    Intent intent = new Intent(HomeActivity.this, PrivacyNotice_Activity.class);
                    startActivity(intent);
                } else {
                    //Clear HouseHold UUID from Session for new registration
                    sessionManager.setHouseholdUuid("");

                    Intent intent = new Intent(HomeActivity.this, IdentificationActivity.class);
                    startActivity(intent);
                }
            }
        });*//*
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SearchPatientActivity.class);
                startActivity(intent);
            }
        });
        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, TodayPatientActivity.class);
                startActivity(intent);
            }
        });
        c4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ActivePatientActivity.class);
                startActivity(intent);
            }
        });
        c5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoLibrary();
            }
        });

        findViewById(R.id.btnFollowUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, FollowUpPatientActivity.class);
                startActivity(intent);
            }
        });

        ivSync = findViewById(R.id.iv_sync);

        setLastSyncTime(getString(R.string.last_synced) + "\n" + sessionManager.getLastSyncDateTime());
//        lastSyncTextView.setText(getString(R.string.last_synced) + " \n" + sessionManager.getLastSyncDateTime());
        locationSetupTextView.setText(getString(R.string.location_setup) + " " + sessionManager.getLocationName());
//        if (!sessionManager.getLastSyncDateTime().equalsIgnoreCase("- - - -")
//                && Locale.getDefault().toString().equalsIgnoreCase("en")) {
////            lastSyncAgo.setText(CalculateAgoTime());
//        }

        syncAnimator = ObjectAnimator.ofFloat(ivSync, View.ROTATION, 0f, 359f).setDuration(1200);
        syncAnimator.setRepeatCount(ValueAnimator.INFINITE);
        syncAnimator.setInterpolator(new LinearInterpolator());
        manualSyncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AppConstants.notificationUtils.showNotifications(getString(R.string.sync), getString(R.string.syncInProgress), 1, context);

                if (isNetworkConnected()) {
                    Toast.makeText(context, getString(R.string.syncInProgress), Toast.LENGTH_LONG).show();
                    ivSync.clearAnimation();
                    syncAnimator.start();
                    syncUtils.syncForeground("home");
                } else {
                    Toast.makeText(context, context.getString(R.string.failed_synced), Toast.LENGTH_LONG).show();
                }
//                if (!sessionManager.getLastSyncDateTime().equalsIgnoreCase("- - - -")
//                        && Locale.getDefault().toString().equalsIgnoreCase("en")) {
//                    lastSyncAgo.setText(sessionManager.getLastTimeAgo());
//                }
            }
        });
        //WorkManager.getInstance().enqueueUniquePeriodicWork(AppConstants.UNIQUE_WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, AppConstants.PERIODIC_WORK_REQUEST);
        if (sessionManager.isFirstTimeLaunched()) {
            mSyncProgressDialog = new ProgressDialog(HomeActivity.this, R.style.AlertDialogStyle); //thats how to add a style!
            mSyncProgressDialog.setTitle(R.string.syncInProgress);
            mSyncProgressDialog.setCancelable(false);
            mSyncProgressDialog.setProgress(i);
            mSyncProgressDialog.show();

            syncUtils.initialSync("home");
        } else {
            // if initial setup done then we can directly set the periodic background sync job
            WorkManager.getInstance().enqueueUniquePeriodicWork(AppConstants.UNIQUE_WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, AppConstants.PERIODIC_WORK_REQUEST);
            saveToken();
            requestPermission();
        }
        *//*sessionManager.setMigration(true);

        if (sessionManager.isReturningUser()) {
            syncUtils.syncForeground("");
        }*//*

        showProgressbar();*/


        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
                12345, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
        // to set different alarams for different patients.

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(),
                    30 * 1000, pendingIntent);
        }
    }

    private void loadVisits() {

        if (sessionManager.isPullSyncFinished()) {
            getVisits();
            findViewById(R.id.tvEmpty).setVisibility(View.GONE);
            mActiveVisitsRecyclerView.setVisibility(View.VISIBLE);
            List<ActivePatientModel> activePatientModels = doQuery(offset);
            List<ActivePatientModel> filteractivePatient = doQuery(offset);

            // #-- Alert logic -- start
            for (int j = 0; j < activePatientModels.size(); j++) {
                encounterDTO = encounterDAO.getEncounterByVisitUUIDLimit1(activePatientModels.get(j).getUuid()); // get latest encounter by visit uuid
                encUUID_visitComplete = encounterDAO.getVisitCompleteEncounterByVisitUUID(activePatientModels.get(j).getUuid());
                encounterUUID = encounterDTO.getUuid();

                if (!encUUID_visitComplete.equalsIgnoreCase("")) { // birthoutcome
                    String birthoutcome = obsDAO.checkBirthOutcomeObsExistsOrNot(encUUID_visitComplete);
                    if (!birthoutcome.equalsIgnoreCase("")) {
                        activePatientModels.get(j).setBirthOutcomeValue(birthoutcome);
                        filteractivePatient.get(j).setBirthOutcomeValue(birthoutcome);
                    }
                }


                if (encounterDTO.getEncounterTypeUuid() != null) {
                    String latestEncounterName = new EncounterDAO().getEncounterTypeNameByUUID(encounterDTO.getEncounterTypeUuid());
                    if (latestEncounterName.toLowerCase().contains("stage2")) {
                        activePatientModels.get(j).setStageName("Stage-2");
                        filteractivePatient.get(j).setStageName("Stage-2");
                    } else if (latestEncounterName.toLowerCase().contains("stage1")) {
                        activePatientModels.get(j).setStageName("Stage-1");
                        filteractivePatient.get(j).setStageName("Stage-1");
                    } else {
                        activePatientModels.get(j).setStageName("");
                        filteractivePatient.get(j).setStageName("");
                    }
                }

                int red = 2, yellow = 1, green = 0;
                int r_count = 0, y_count = 0, g_count = 0;
                int count = 0;

                // alert logic - start
                if (encounterUUID != null && !encounterUUID.equalsIgnoreCase("")) {
                    obsDTOList = obsDAO.obsCommentList(encounterUUID);
                    if (obsDTOList != null) {
                        for (int i = 0; i < obsDTOList.size(); i++) {
                            if (obsDTOList.get(i).getComment() != null) {
                                if (obsDTOList.get(i).getComment().trim().equalsIgnoreCase("R")) {
                                    r_count++;
                                } else if (obsDTOList.get(i).getComment().trim().equalsIgnoreCase("Y")) {
                                    y_count++;
                                } else if (obsDTOList.get(i).getComment().trim().equalsIgnoreCase("G")) {
                                    g_count++;
                                }
                            }
                        }
                    }
                    // TODO: uncomment - done

                    // testing - start // TODO: remove line - done
                   /* r_count = 40;
                    y_count = 6;
                    g_count = 8;*/
                    // test - end

                    // multiply and add
                    red = red * r_count;
                    yellow = yellow * y_count;
                    green = green * g_count;

                    count = red + yellow + green;

                    // set count of total to this visit to which it belongs to...
                    activePatientModels.get(j).setAlertFlagTotal(count);
                    filteractivePatient.get(j).setAlertFlagTotal(count);

                    if (count > 22) { // Red
                        activePatientModels.get(j).setVisibilityOrder(3);
                        filteractivePatient.get(j).setVisibilityOrder(3);
                    } else if (count >= 15 && count <= 22) { // Yellow
                        activePatientModels.get(j).setVisibilityOrder(2);
                        filteractivePatient.get(j).setVisibilityOrder(2);
                    } else if (count < 15) { // Green
                        activePatientModels.get(j).setVisibilityOrder(1);
                        filteractivePatient.get(j).setVisibilityOrder(1);
                    }

                    if (encounterUUID != null && !encounterUUID.equalsIgnoreCase("")) { // blinking part
                        int issubmitted = obsDAO.checkObsExistsOrNot(encounterUUID);
                        if (issubmitted == 1) { // not yet filled
                            activePatientModels.get(j).setObsExistsFlag(true);
                            filteractivePatient.get(j).setObsExistsFlag(true);

                            activePatientModels.get(j).setVisibilityOrder(4);
                            filteractivePatient.get(j).setVisibilityOrder(4);
                        }
                    }

                }

            }

            // #-- Alert logic -- end
            Collections.sort(activePatientModels, new Comparator<ActivePatientModel>() {
                @Override
                public int compare(ActivePatientModel t1, ActivePatientModel t2) {
                    Integer i1 = t1.getVisibilityOrder();
                    Integer i2 = t2.getVisibilityOrder();
                    return i2.compareTo(i1);
                }
            });

            // #-- Alert logic -- end
            Collections.sort(filteractivePatient, new Comparator<ActivePatientModel>() {
                @Override
                public int compare(ActivePatientModel t1, ActivePatientModel t2) {
                    Integer i1 = t1.getVisibilityOrder();
                    Integer i2 = t2.getVisibilityOrder();
                    return i2.compareTo(i1);
                }
            });

            mActivePatientAdapter = new ActivePatientAdapter(activePatientModels, filteractivePatient, HomeActivity.this, listPatientUUID, sessionManager);
            mActiveVisitsRecyclerView.setAdapter(mActivePatientAdapter);
            mActivePatientAdapter.setActionListener(new ActivePatientAdapter.OnActionListener() {
                @Override
                public void onEndVisitClicked(ActivePatientModel activePatientModel, boolean hasPrescription) {
                    String encounterAdultIntialslocal = "";
                    String encounterVitalslocal = null;
                    String encounterIDSelection = "visituuid = ?";

                    String visitUuid = activePatientModel.getUuid();
                    String visitnote = "", followupdate = "";
                    String[] encounterIDArgs = {visitUuid};
                    EncounterDAO encounterDAO = new EncounterDAO();
                    Cursor encounterCursor = db.query("tbl_encounter", null, encounterIDSelection, encounterIDArgs, null, null, null);
                    if (encounterCursor != null && encounterCursor.moveToFirst()) {
                        do {
                            if (encounterDAO.getEncounterTypeUuid("ENCOUNTER_VITALS").equalsIgnoreCase(encounterCursor.getString(encounterCursor.getColumnIndexOrThrow("encounter_type_uuid")))) {
                                encounterVitalslocal = encounterCursor.getString(encounterCursor.getColumnIndexOrThrow("uuid"));
                            }
                            if (encounterDAO.getEncounterTypeUuid("ENCOUNTER_ADULTINITIAL").equalsIgnoreCase(encounterCursor.getString(encounterCursor.getColumnIndexOrThrow("encounter_type_uuid")))) {
                                encounterAdultIntialslocal = encounterCursor.getString(encounterCursor.getColumnIndexOrThrow("uuid"));
                            }

                            if (encounterDAO.getEncounterTypeUuid("ENCOUNTER_VISIT_NOTE").equalsIgnoreCase(encounterCursor.getString(encounterCursor.getColumnIndexOrThrow("encounter_type_uuid")))) {
                                visitnote = encounterCursor.getString(encounterCursor.getColumnIndexOrThrow("uuid"));
                            }

                        } while (encounterCursor.moveToNext());
                    }
                    encounterCursor.close();

                    String[] visitArgs = {visitnote, UuidDictionary.FOLLOW_UP_VISIT};
                    String[] columns = {"value", " conceptuuid"};
                    String visitSelection = "encounteruuid = ? AND conceptuuid = ? and voided!='1' ";
                    Cursor visitCursor = db.query("tbl_obs", columns, visitSelection, visitArgs, null, null, null);
                    if (visitCursor.moveToFirst()) {
                        do {
//                            String dbConceptID = visitCursor.getString(visitCursor.getColumnIndex("conceptuuid"));
                            String dbValue = visitCursor.getString(visitCursor.getColumnIndex("value"));
                            followupdate = dbValue;
                        } while (visitCursor.moveToNext());
                    }
                    visitCursor.close();

                    MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(HomeActivity.this);
                    //if (hasPrescription) {
                    alertDialogBuilder.setMessage(HomeActivity.this.getResources().getString(R.string.end_visit_msg));
                    alertDialogBuilder.setNegativeButton(HomeActivity.this.getResources().getString(R.string.generic_cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    String finalFollowupdate = followupdate;
                    String finalEncounterVitalslocal = encounterVitalslocal;
                    String finalEncounterAdultIntialslocal = encounterAdultIntialslocal;
                    alertDialogBuilder.setPositiveButton(HomeActivity.this.getResources().getString(R.string.generic_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            EncounterDTO encounterDTO = new EncounterDTO();
                            String uuid = UUID.randomUUID().toString();
                            EncounterDAO encounterDAO = new EncounterDAO();
                            encounterDTO = new EncounterDTO();
                            encounterDTO.setUuid(uuid);
                            encounterDTO.setEncounterTypeUuid("bd1fbfaa-f5fb-4ebd-b75c-564506fc309e");

                            //As per issue #785 - we fixed it by subtracting 1 minute from Encounter Time
                            try {
                                encounterDTO.setEncounterTime(fiveMinutesAgo(AppConstants.dateAndTimeUtils.currentDateTime()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            encounterDTO.setVisituuid(visitUuid);
                            //        encounterDTO.setProvideruuid(encounterDTO.getProvideruuid());  //handles correct provideruuid for every patient
                            encounterDTO.setProvideruuid(sessionManager.getProviderID());  //handles correct provideruuid for every patient
                            encounterDTO.setSyncd(false);
                            encounterDTO.setVoided(0);
                            try {
                                encounterDAO.createEncountersToDB(encounterDTO);
                            } catch (DAOException e) {
                                FirebaseCrashlytics.getInstance().recordException(e);
                            }

                            VisitsDAO visitsDAO = new VisitsDAO();
                            try {
                                visitsDAO.updateVisitEnddate(visitUuid, AppConstants.dateAndTimeUtils.currentDateTime());
                            } catch (DAOException e) {
                                FirebaseCrashlytics.getInstance().recordException(e);
                            }

                            //SyncDAO syncDAO = new SyncDAO();
                            //syncDAO.pushDataApi();
                            syncUtils.syncForeground("survey"); //Sync function will work in foreground of org and
                            // the Time will be changed for last sync.

//        AppConstants.notificationUtils.DownloadDone(getString(R.string.end_visit_notif), getString(R.string.visit_ended_notif), 3, PatientSurveyActivity.this);

                            sessionManager.removeVisitSummary(activePatientModel.getPatientuuid(), visitUuid);

                               /* VisitUtils.endVisit(HomeActivity.this,
                                        visitUuid,
                                        activePatientModel.getPatientuuid(),
                                        finalFollowupdate,
                                        finalEncounterVitalslocal,
                                        finalEncounterAdultIntialslocal,
                                        null,
                                        String.format("%s %s", activePatientModel.getFirst_name(), activePatientModel.getLast_name()),
                                        ""
                                );*/


//                                AppointmentDAO appointmentDAO = new AppointmentDAO();
//                                appointmentDAO.deleteAppointmentByVisitId(visitUuid);
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.show();
                    //alertDialog.show();
                    IntelehealthApplication.setAlertDialogCustomTheme(HomeActivity.this, alertDialog);

                    /*} else {
                        alertDialogBuilder.setMessage(HomeActivity.this.getResources().getString(R.string.error_no_data));
                        alertDialogBuilder.setNeutralButton(HomeActivity.this.getResources().getString(R.string.generic_ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alertDialog = alertDialogBuilder.show();
                        //alertDialog.show();
                        IntelehealthApplication.setAlertDialogCustomTheme(HomeActivity.this, alertDialog);
                    }*/
                }
            });
        }


    }

    public String fiveMinutesAgo(String timeStamp) throws ParseException {

        long FIVE_MINS_IN_MILLIS = 5 * 60 * 1000;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        long time = df.parse(timeStamp).getTime();

        return df.format(new Date(time - FIVE_MINS_IN_MILLIS));
    }

    //function for handling the video library feature...
    private void videoLibrary() {
        if (!sessionManager.getLicenseKey().isEmpty())
            hasLicense = true;
        //Check for license key and load the correct config file
        try {
            JSONObject obj = null;
            if (hasLicense) {
                obj = new JSONObject(Objects.requireNonNullElse
                        (FileUtils.readFileRoot(AppConstants.CONFIG_FILE_NAME, context),
                                String.valueOf(FileUtils.encodeJSON(context, AppConstants.CONFIG_FILE_NAME)))); //Load the config file
            } else {
                obj = new JSONObject(String.valueOf(FileUtils.encodeJSON(this, AppConstants.CONFIG_FILE_NAME)));
            }

            if (obj.has("video_library")) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse(obj.getString("video_library"));
                intent.setData(uri);
                startActivity(intent);
            } else {
                Toast.makeText(context, "No config attribute found", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            Toast.makeText(getApplicationContext(), "JsonException" + e, Toast.LENGTH_LONG).show();
        }
    }

    private void showProgressbar() {


// instantiate it within the onCreate method
        mProgressDialog = new ProgressDialog(HomeActivity.this);
        mProgressDialog.setMessage(getString(R.string.download_protocols));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);
    }


    private String CalculateAgoTime() {
        String finalTime = "";

        String syncTime = sessionManager.getLastSyncDateTime();

        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault());
        ParsePosition pos = new ParsePosition(0);
        long then = formatter.parse(syncTime, pos).getTime();
        long now = new Date().getTime();

        long seconds = (now - then) / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        String time = "";
        long num = 0;
        if (days > 0) {
            num = days;
            time = days + " " + context.getString(R.string.day);
        } else if (hours > 0) {
            num = hours;
            time = hours + " " + context.getString(R.string.hour);
        } else if (minutes >= 0) {
            num = minutes;
            time = minutes + " " + context.getString(R.string.minute);
        }
//      <For Seconds>
//      else {
//            num = seconds;
//            time = seconds + " second";
//      }
        if (num > 1) {
            time += context.getString(R.string.s);
        }
        finalTime = time + " " + context.getString(R.string.ago);

        sessionManager.setLastTimeAgo(finalTime);

        return finalTime;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private MenuItem mLastUpdateMenuItem;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        mLastUpdateMenuItem = menu.findItem(R.id.updateTimeItem);
        setLastSyncTime(getString(R.string.last_synced) + " \n" + sessionManager.getLastSyncDateTime());
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.syncOption:
//                refreshDatabases();
//                return true;
            case R.id.settingsOption:
                settings();
                return true;
            case R.id.updateProtocolsOption: {


                if (NetworkConnection.isOnline(this)) {

                    if (!sessionManager.getLicenseKey().isEmpty()) {

                        String licenseUrl = sessionManager.getMindMapServerUrl();
                        String licenseKey = sessionManager.getLicenseKey();
                        getMindmapDownloadURL("https://" + licenseUrl + ":3004/", licenseKey);

                    } else {
//                        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(this);
//                        // AlertDialog.Builder dialog = new AlertDialog.Builder(this,R.style.AlertDialogStyle);
//                        LayoutInflater li = LayoutInflater.from(this);
//                        View promptsView = li.inflate(R.layout.dialog_mindmap_cred, null);
//                        dialog.setTitle(getString(R.string.enter_license_key))
//                                .setView(promptsView)
//                                .setPositiveButton(getString(R.string.button_ok), new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//
//                                        Dialog d = (Dialog) dialog;
//
//                                        EditText etURL = d.findViewById(R.id.licenseurl);
//                                        EditText etKey = d.findViewById(R.id.licensekey);
//                                        String url = etURL.getText().toString().trim();
//                                        String key = etKey.getText().toString().trim();
//
//                                        if (url.isEmpty()) {
//                                            etURL.setError(getResources().getString(R.string.enter_server_url));
//                                            etURL.requestFocus();
//                                            return;
//                                        }
//                                        if (url.contains(":")) {
//                                            etURL.setError(getResources().getString(R.string.invalid_url));
//                                            etURL.requestFocus();
//                                            return;
//                                        }
//                                        if (key.isEmpty()) {
//                                            etKey.setError(getResources().getString(R.string.enter_license_key));
//                                            etKey.requestFocus();
//                                            return;
//                                        }
//
//                                        sessionManager.setMindMapServerUrl(url);
//                                        getMindmapDownloadURL("https://" + url + ":3004/", key);
//
//                                    }
//                                })
//                                .setNegativeButton(getString(R.string.button_cancel), new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.dismiss();
//                                    }
//                                });
//                        Dialog builderDialog = dialog.show();
//                        IntelehealthApplication.setAlertDialogCustomTheme(this, builderDialog);

                        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(this);
                        LayoutInflater li = LayoutInflater.from(this);
                        View promptsView = li.inflate(R.layout.dialog_mindmap_cred, null);

                        dialog.setTitle(getString(R.string.enter_license_key))
                                .setView(promptsView)
                                .setPositiveButton(getString(R.string.button_ok), null)
                                .setNegativeButton(getString(R.string.button_cancel), null);

                        AlertDialog alertDialog = dialog.create();
                        alertDialog.setView(promptsView, 20, 0, 20, 0);
                        alertDialog.show();
                        alertDialog.setCanceledOnTouchOutside(false); //dialog wont close when clicked outside...

                        // Get the alert dialog buttons reference
                        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                        // Change the alert dialog buttons text and background color
                        positiveButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                        negativeButton.setTextColor(getResources().getColor(R.color.colorPrimary));

                        positiveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EditText text = promptsView.findViewById(R.id.licensekey);
                                EditText url = promptsView.findViewById(R.id.licenseurl);

                                url.setError(null);
                                text.setError(null);

                                //If both are not entered...
                                if (url.getText().toString().trim().isEmpty() && text.getText().toString().trim().isEmpty()) {
                                    url.requestFocus();
                                    url.setError(getResources().getString(R.string.enter_server_url));
                                    text.setError(getResources().getString(R.string.enter_license_key));
                                    return;
                                }

                                //If Url is empty...key is not empty...
                                if (url.getText().toString().trim().isEmpty() && !text.getText().toString().trim().isEmpty()) {
                                    url.requestFocus();
                                    url.setError(getResources().getString(R.string.enter_server_url));
                                    return;
                                }

                                //If Url is not empty...key is empty...
                                if (!url.getText().toString().trim().isEmpty() && text.getText().toString().trim().isEmpty()) {
                                    text.requestFocus();
                                    text.setError(getResources().getString(R.string.enter_license_key));
                                    return;
                                }

                                //If Url has : in it...
                                if (url.getText().toString().trim().contains(":")) {
                                    url.requestFocus();
                                    url.setError(getResources().getString(R.string.invalid_url));
                                    return;
                                }

                                //If url entered is Invalid...
                                if (!url.getText().toString().trim().isEmpty()) {
                                    if (Patterns.WEB_URL.matcher(url.getText().toString().trim()).matches()) {
                                        String url_field = "https://" + url.getText().toString() + ":3004/";
                                        if (URLUtil.isValidUrl(url_field)) {
                                            key = text.getText().toString().trim();
                                            licenseUrl = url.getText().toString().trim();

                                            sessionManager.setMindMapServerUrl(licenseUrl);

                                            if (keyVerified(key)) {
                                                getMindmapDownloadURL("https://" + licenseUrl + ":3004/", key);
                                                alertDialog.dismiss();
                                            }
                                        } else {
                                            Toast.makeText(HomeActivity.this, getString(R.string.url_invalid), Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        //invalid url || invalid url and key.
                                        Toast.makeText(HomeActivity.this, R.string.invalid_url, Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(HomeActivity.this, R.string.please_enter_url_and_key, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        negativeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });

                        IntelehealthApplication.setAlertDialogCustomTheme(this, alertDialog);


                    }

                } else {
                    Toast.makeText(context, getString(R.string.mindmap_internect_connection), Toast.LENGTH_SHORT).show();
                }

                return true;
            }

         /*   case R.id.sync:
//                pullDataDAO.pullData(this);
//                pullDataDAO.pushDataApi();
//                AppConstants.notificationUtils.showNotifications(getString(R.string.sync), getString(R.string.syncInProgress), 1, this);
                boolean isSynced = syncUtils.syncForeground();
//                boolean i = imagesPushDAO.patientProfileImagesPush();
//                boolean o = imagesPushDAO.obsImagesPush();
//                if (isSynced)
//                    AppConstants.notificationUtils.showNotifications_noProgress(getString(R.string.sync_not_available), getString(R.string.please_connect_to_internet), getApplicationContext());
//                else
//                    AppConstants.notificationUtils.showNotifications(getString(R.string.image_upload), getString(R.string.image_upload_failed), 4, this);
                return true;
                */
//            case R.id.backupOption:
//                manageBackup(true, false);  // to backup app data at any time of the day
//                return true;
//
//            case R.id.restoreOption:
//                manageBackup(false, false); // to restore app data if db is empty
//                return true;

            case R.id.logoutOption:
//                manageBackup(true, false);

                MaterialAlertDialogBuilder alertdialogBuilder = new MaterialAlertDialogBuilder(this);
                alertdialogBuilder.setMessage(R.string.sure_to_logout);
                alertdialogBuilder.setPositiveButton(R.string.generic_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        logout();
                    }
                });
                alertdialogBuilder.setNegativeButton(R.string.generic_no, null);
                AlertDialog alertDialog = alertdialogBuilder.create();
                alertDialog.show();
                Button positiveButton = alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE);
                Button negativeButton = alertDialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE);
                positiveButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                negativeButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                IntelehealthApplication.setAlertDialogCustomTheme(this, alertDialog);

                return true;

            case R.id.restAppOption:

                if ((isNetworkConnected())) {
                    mResetSyncDialog.show();
                    boolean isSynced = syncUtils.syncForeground("home");
                    if (isSynced) {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() { //Do something after 100ms
                                showResetConfirmationDialog();
                            }
                        }, 3000);
                    } else {
                        mResetSyncDialog.dismiss();
                        DialogUtils dialogUtils = new DialogUtils();
                        dialogUtils.showOkDialog(this, getString(R.string.error), getString(R.string.sync_failed), getString(R.string.generic_ok));
                    }
                    return true;
                } else {
                    DialogUtils dialogUtils = new DialogUtils();
                    dialogUtils.showOkDialog(this, getString(R.string.error_network), getString(R.string.no_network_sync), getString(R.string.generic_ok));
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This method starts intent to another activity to change settings
     *
     * @return void
     */
    public void settings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    /**
     * Logs out the user. It removes user account using AccountManager.
     *
     * @return void
     */
    public void logout() {

        OfflineLogin.getOfflineLogin().setOfflineLoginStatus(false);

//        parseLogOut();

       /* AccountManager manager = AccountManager.get(HomeActivity.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }*/
     /*   Account[] accountList = manager.getAccountsByType("io.intelehealth.openmrs");
        if (accountList.length > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                manager.removeAccount(accountList[0], HomeActivity.this, null, null);
            } else {
                manager.removeAccount(accountList[0], null, null); // Legacy implementation
            }
        }
*/
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

        SyncUtils syncUtils = new SyncUtils();
        syncUtils.syncBackground();
        sessionManager.setReturningUser(false);
        sessionManager.setLogout(true);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //registerReceiver(reMyreceive, filter);
        checkAppVer();  //auto-update feature.
//        lastSyncTextView.setText(getString(R.string.last_synced) + " \n" + sessionManager.getLastSyncDateTime());
        if (!sessionManager.getLastSyncDateTime().equalsIgnoreCase("- - - -")
                && Locale.getDefault().toString().equals("en")) {
//            lastSyncAgo.setText(CalculateAgoTime());
        }

        if (mActivePatientAdapter != null)
            mActivePatientAdapter.notifyDataSetChanged();

        registerReceiver(mCardMessageReceiver, new IntentFilter(AppConstants.NEW_CARD_INTENT_ACTION));
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(AppConstants.SYNC_INTENT_ACTION);
        registerReceiver(syncBroadcastReceiver, filter);
        showBadge();
        requestPermission();
        if (mActivePatientAdapter != null)
            mActivePatientAdapter.notifyDataSetChanged();
    }

    private void showBadge() {
       /* long activePatientCount = ActivePatientActivity.getActiveVisitsCount(AppConstants.inteleHealthDatabaseHelper.getWriteDb());
        long todayPatientCount = TodayPatientActivity.getTodayVisitsCount(AppConstants.inteleHealthDatabaseHelper.getWriteDb());

        tvTodayVisitsBadge.setVisibility(View.VISIBLE);
        if (todayPatientCount > 0) {
            tvTodayVisitsBadge.setText("(" + String.valueOf(todayPatientCount) + ")");
        } else {
            tvTodayVisitsBadge.setText("(0)");
        }
        tvActiveVisitsBadge.setVisibility(View.VISIBLE);
        if (activePatientCount > 0) {
            tvActiveVisitsBadge.setText("(" + String.valueOf(activePatientCount) + ")");
        } else {
            tvActiveVisitsBadge.setText("(0)");
        }*/
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            unregisterReceiver(syncBroadcastReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private boolean keyVerified(String key) {
        //TODO: Verify License Key
        return true;
    }

    @Override
    public void onBackPressed() {
        /*new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to EXIT ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        moveTaskToBack(true);
                        finish();

                    }

                })
                .setNegativeButton("No", null)
                .show();
*/
        MaterialAlertDialogBuilder alertdialogBuilder = new MaterialAlertDialogBuilder(this);

        // AlertDialog.Builder alertdialogBuilder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        alertdialogBuilder.setMessage(R.string.sure_to_exit);
        alertdialogBuilder.setPositiveButton(R.string.generic_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                moveTaskToBack(true);
                // finish();
            }
        });
        alertdialogBuilder.setNegativeButton(R.string.generic_no, null);

        AlertDialog alertDialog = alertdialogBuilder.create();
        alertDialog.show();

        Button positiveButton = alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE);
        Button negativeButton = alertDialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE);

        positiveButton.setTextColor(getResources().getColor(R.color.colorPrimary));
        //positiveButton.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        negativeButton.setTextColor(getResources().getColor(R.color.colorPrimary));
        //negativeButton.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        IntelehealthApplication.setAlertDialogCustomTheme(this, alertDialog);

    }


    private void getMindmapDownloadURL(String url, String key) {
        customProgressDialog.show();
        ApiClient.changeApiBaseUrl(url);
        ApiInterface apiService = ApiClient.createService(ApiInterface.class);
        try {
            Observable<DownloadMindMapRes> resultsObservable = apiService.DOWNLOAD_MIND_MAP_RES_OBSERVABLE(key);
            resultsObservable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableObserver<DownloadMindMapRes>() {
                        @Override
                        public void onNext(DownloadMindMapRes res) {
                            customProgressDialog.dismiss();
                            if (res.getMessage() != null && res.getMessage().equalsIgnoreCase("Success")) {

                                Log.e("MindMapURL", "Successfully get MindMap URL");
                                mTask = new DownloadMindMaps(context, mProgressDialog, "home");
                                mindmapURL = res.getMindmap().trim();
                                sessionManager.setLicenseKey(key);
                                checkExistingMindMaps();

                            } else {
                                Toast.makeText(context, getResources().getString(R.string.no_protocols_found), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            customProgressDialog.dismiss();
                            Toast.makeText(context, getResources().getString(R.string.unable_to_get_proper_response), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "changeApiBaseUrl: " + e.getMessage());
            Log.e(TAG, "changeApiBaseUrl: " + e.getStackTrace());
        }
    }

    private void checkExistingMindMaps() {

        //Check is there any existing mindmaps are present, if yes then delete.

        File engines = new File(context.getFilesDir().getAbsolutePath(), "/Engines");
        Log.e(TAG, "Engines folder=" + engines.exists());
        if (engines.exists()) {
            engines.delete();
        }
        File logo = new File(context.getFilesDir().getAbsolutePath(), "/logo");
        Log.e(TAG, "Logo folder=" + logo.exists());
        if (logo.exists()) {
            logo.delete();
        }
        File physicalExam = new File(context.getFilesDir().getAbsolutePath() + "/physExam.json");
        Log.e(TAG, "physExam.json=" + physicalExam.exists());
        if (physicalExam.exists()) {
            physicalExam.delete();
        }
        File familyHistory = new File(context.getFilesDir().getAbsolutePath() + "/famHist.json");
        Log.e(TAG, "famHist.json=" + familyHistory.exists());
        if (familyHistory.exists()) {
            familyHistory.delete();
        }
        File pastMedicalHistory = new File(context.getFilesDir().getAbsolutePath() + "/patHist.json");
        Log.e(TAG, "patHist.json=" + pastMedicalHistory.exists());
        if (pastMedicalHistory.exists()) {
            pastMedicalHistory.delete();
        }
        File config = new File(context.getFilesDir().getAbsolutePath() + "/config.json");
        Log.e(TAG, "config.json=" + config.exists());
        if (config.exists()) {
            config.delete();
        }

        //Start downloading mindmaps
        mTask.execute(mindmapURL, context.getFilesDir().getAbsolutePath() + "/mindmaps.zip");
        Log.e("DOWNLOAD", "isSTARTED");

    }

    private void checkAppVer() {

        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            versionCode = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        disposable.add((Disposable) AppConstants.apiInterface.checkAppUpdate()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<CheckAppUpdateRes>() {
                    @Override
                    public void onSuccess(CheckAppUpdateRes res) {
                        int latestVersionCode = 0;
                        if (!res.getLatestVersionCode().isEmpty()) {
                            latestVersionCode = Integer.parseInt(res.getLatestVersionCode());
                        }

                        if (latestVersionCode > versionCode) {
                            android.app.AlertDialog.Builder builder;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                builder = new android.app.AlertDialog.Builder(HomeActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                            } else {
                                builder = new android.app.AlertDialog.Builder(HomeActivity.this);
                            }


                            builder.setTitle(getResources().getString(R.string.new_update_available))
                                    .setCancelable(false)
                                    .setMessage(getResources().getString(R.string.update_app_note))
                                    .setPositiveButton(getResources().getString(R.string.update), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                            try {
                                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                            } catch (ActivityNotFoundException anfe) {
                                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                            }

                                        }
                                    })

                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setCancelable(false);

                            Dialog dialog = builder.show();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                int textViewId = dialog.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
                                TextView tv = (TextView) dialog.findViewById(textViewId);
                                tv.setTextColor(getResources().getColor(R.color.white));
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Error", "" + e);
                    }
                })
        );

    }

    private List<Integer> mTempSyncHelperList = new ArrayList<Integer>();
    private BroadcastReceiver syncBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.logD("syncBroadcastReceiver", "onReceive! " + intent);

            if (intent != null && intent.hasExtra(AppConstants.SYNC_INTENT_DATA_KEY)) {
                int flagType = intent.getIntExtra(AppConstants.SYNC_INTENT_DATA_KEY, AppConstants.SYNC_FAILED);
                if (sessionManager.isFirstTimeLaunched()) {
                    if (flagType == AppConstants.SYNC_FAILED) {
                        hideSyncProgressBar(false);
                        /*Toast.makeText(context, R.string.failed_synced, Toast.LENGTH_SHORT).show();
                        finish();*/
                        new AlertDialog.Builder(HomeActivity.this)
                                .setMessage(R.string.failed_initial_synced)
                                .setPositiveButton(R.string.generic_ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }

                                }).setCancelable(false)

                                .show();
                    } else {
                        mTempSyncHelperList.add(flagType);
                        if (mTempSyncHelperList.contains(AppConstants.SYNC_PULL_DATA_DONE)
//                                && mTempSyncHelperList.contains(AppConstants.SYNC_PUSH_DATA_DONE)
                                /*&& mTempSyncHelperList.contains(AppConstants.SYNC_PATIENT_PROFILE_IMAGE_PUSH_DONE)
                                && mTempSyncHelperList.contains(AppConstants.SYNC_OBS_IMAGE_PUSH_DONE)*/) {
                            hideSyncProgressBar(true);
                        }
                    }
                    showBadge();
                }
            }

            setLastSyncTime(getString(R.string.last_synced) + " \n" + sessionManager.getLastSyncDateTime());
//            lastSyncTextView.setText(getString(R.string.last_synced) + " \n" + sessionManager.getLastSyncDateTime());
//          lastSyncAgo.setText(sessionManager.getLastTimeAgo());

            if (syncAnimator != null && syncAnimator.getCurrentPlayTime() > 200) {
                syncAnimator.cancel();
                syncAnimator.end();
            }
        }
    };

    private void hideSyncProgressBar(boolean isSuccess) {
        saveToken();
        requestPermission();
        if (mTempSyncHelperList != null) mTempSyncHelperList.clear();
        if (mSyncProgressDialog != null && mSyncProgressDialog.isShowing()) {
            mSyncProgressDialog.dismiss();
            if (isSuccess) {
                saveToken();
                sessionManager.setFirstTimeLaunched(false);
                sessionManager.setMigration(true);
                // initial setup/sync done and now we can set the periodic background sync job
                // given some delay after initial sync
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        WorkManager.getInstance().enqueueUniquePeriodicWork(AppConstants.UNIQUE_WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, AppConstants.PERIODIC_WORK_REQUEST);
                    }
                }, 10000);
            }
        }

    }

    private static final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 10021;

    private void requestPermission() {
        Intent serviceIntent = new Intent(this, CallListenerBackgroundService.class);
        if (!CallListenerBackgroundService.isInstanceCreated()) {
            //CallListenerBackgroundService.getInstance().stopForegroundService();
            context.startService(serviceIntent);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + this.getPackageName()));
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
            } else {
                //Permission Granted-System will work
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "Is BG Service On - " + CallListenerBackgroundService.isInstanceCreated());
        if (!CallListenerBackgroundService.isInstanceCreated()) {
            Intent serviceIntent = new Intent(this, CallListenerBackgroundService.class);
            context.startService(serviceIntent);
        }
    }

    @Override
    public void onAttachedToWindow() {
        Window window = getWindow();

        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
        );

        super.onAttachedToWindow();
    }

    private void showResetConfirmationDialog() {
        mResetSyncDialog.dismiss();
        MaterialAlertDialogBuilder resetAlertdialogBuilder = new MaterialAlertDialogBuilder(this);
        resetAlertdialogBuilder.setMessage(R.string.sure_to_reset_app);
        resetAlertdialogBuilder.setPositiveButton(R.string.generic_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showResetProgressbar();
                deleteCache(getApplicationContext());
            }
        });
        resetAlertdialogBuilder.setNegativeButton(R.string.generic_no, null);
        AlertDialog resetAlertDialog = resetAlertdialogBuilder.create();
        resetAlertDialog.show();
        Button resetPositiveButton = resetAlertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE);
        Button resetNegativeButton = resetAlertDialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE);
        resetPositiveButton.setTextColor(getResources().getColor(R.color.colorPrimary));
        resetNegativeButton.setTextColor(getResources().getColor(R.color.colorPrimary));
        IntelehealthApplication.setAlertDialogCustomTheme(this, resetAlertDialog);
    }


    private void showResetProgressbar() {
        mRefreshProgressDialog = new ProgressDialog(HomeActivity.this, R.style.AlertDialogStyle);
        mRefreshProgressDialog.setTitle(R.string.resetting_app_dialog);
        mRefreshProgressDialog.setCancelable(false);
        mRefreshProgressDialog.setProgress(i);
        mRefreshProgressDialog.show();
    }

    public void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            boolean success = deleteDir(dir);
            if (success) {
                clearAppData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    private void setLastSyncTime(String lastSyncTime) {
        String convertedString = getFullMonthName(lastSyncTime);

        if (mLastUpdateMenuItem != null) {
            if (sessionManager.getAppLanguage().equalsIgnoreCase("hi")) {
                String sync_text = en__hi_dob(convertedString); //to show text of English into Hindi...
                mLastUpdateMenuItem.setTitle(sync_text);
            } else if (sessionManager.getAppLanguage().equalsIgnoreCase("or")) {
                String sync_text = en__or_dob(convertedString); //to show text of English into Odiya...
                mLastUpdateMenuItem.setTitle(sync_text);
            } else if (sessionManager.getAppLanguage().equalsIgnoreCase("bn")) {
                String sync_text = en__bn_dob(convertedString); //to show text of English into Odiya...
                mLastUpdateMenuItem.setTitle(sync_text);
            } else if (sessionManager.getAppLanguage().equalsIgnoreCase("gu")) {
                String sync_text = en__gu_dob(convertedString); //to show text of English into Gujarati...
                mLastUpdateMenuItem.setTitle(sync_text);
            } else if (sessionManager.getAppLanguage().equalsIgnoreCase("te")) {
                String sync_text = en__te_dob(convertedString); //to show text of English into telugu...
                mLastUpdateMenuItem.setTitle(sync_text);
            } else if (sessionManager.getAppLanguage().equalsIgnoreCase("mr")) {
                String sync_text = en__mr_dob(convertedString); //to show text of English into telugu...
                mLastUpdateMenuItem.setTitle(sync_text);
            } else if (sessionManager.getAppLanguage().equalsIgnoreCase("as")) {
                String sync_text = en__as_dob(convertedString); //to show text of English into telugu...
                mLastUpdateMenuItem.setTitle(sync_text);
            } else if (sessionManager.getAppLanguage().equalsIgnoreCase("ml")) {
                String sync_text = en__ml_dob(convertedString); //to show text of English into telugu...
                mLastUpdateMenuItem.setTitle(sync_text);
            } else if (sessionManager.getAppLanguage().equalsIgnoreCase("kn")) {
                String sync_text = en__kn_dob(convertedString); //to show text of English into telugu...
                mLastUpdateMenuItem.setTitle(sync_text);
            } else if (sessionManager.getAppLanguage().equalsIgnoreCase("ru")) {
                String sync_text = en__ru_dob(convertedString); //to show text of English into Russian...
                mLastUpdateMenuItem.setTitle(sync_text);
            } else if (sessionManager.getAppLanguage().equalsIgnoreCase("ta")) {
                String sync_text = en__ta_dob(convertedString); //to show text of English into Tamil...
                mLastUpdateMenuItem.setTitle(sync_text);
            } else {
                mLastUpdateMenuItem.setTitle(lastSyncTime);
            }
        }

        loadVisits();
    }

    private void clearAppData() {
        try {
            // clearing app data
            if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
                Toast.makeText(getApplicationContext(), getString(R.string.app_reset_toast), Toast.LENGTH_LONG).show();
                mRefreshProgressDialog.dismiss();
                ((ActivityManager) getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData(); // note: it has a return value!
            } else {
                String packageName = getApplicationContext().getPackageName();
                Runtime runtime = Runtime.getRuntime();
                runtime.exec("pm clear " + packageName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*EZAZI*/

    /**
     * This method retrieves visit details about patient for a particular date.
     *
     * @return void
     */
    private List<ActivePatientModel> doQuery(int offset) {
        List<ActivePatientModel> activePatientList = new ArrayList<>();
        Date cDate = new Date();
        String query = "SELECT   a.uuid, a.sync, a.patientuuid, a.startdate, a.enddate, b.first_name, b.middle_name, b.last_name, b.date_of_birth, b.openmrs_id, b.gender " +
                "FROM tbl_visit a, tbl_patient b " +
                "WHERE a.patientuuid = b.uuid " +
                "AND a.enddate is NULL OR a.enddate='' GROUP BY a.uuid ORDER BY a.startdate DESC  limit ? offset ?";
        final Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(limit), String.valueOf(offset)});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    try {
                        ActivePatientModel model = new ActivePatientModel(
                                cursor.getString(cursor.getColumnIndexOrThrow("uuid")),
                                cursor.getString(cursor.getColumnIndexOrThrow("patientuuid")),
                                cursor.getString(cursor.getColumnIndexOrThrow("startdate")),
                                cursor.getString(cursor.getColumnIndexOrThrow("enddate")),
                                cursor.getString(cursor.getColumnIndexOrThrow("openmrs_id")),
                                cursor.getString(cursor.getColumnIndexOrThrow("first_name")),
                                cursor.getString(cursor.getColumnIndexOrThrow("middle_name")),
                                cursor.getString(cursor.getColumnIndexOrThrow("last_name")),
                                cursor.getString(cursor.getColumnIndexOrThrow("date_of_birth")),
                                StringUtils.mobileNumberEmpty(phoneNumber(cursor.getString(cursor.getColumnIndexOrThrow("patientuuid")))),
                                cursor.getString(cursor.getColumnIndexOrThrow("sync")));
                        model.setGender(cursor.getString(cursor.getColumnIndexOrThrow("gender")));
                        activePatientList.add(model);
                    } catch (DAOException e) {
                        e.printStackTrace();
                    }
                } while (cursor.moveToNext());
            }
        }
        if (cursor != null) {
            cursor.close();
        }

        return activePatientList;

//        if (!activePatientList.isEmpty()) {
//            for (ActivePatientModel activePatientModel : activePatientList)
//                Logger.logD(TAG, activePatientModel.getFirst_name() + " " + activePatientModel.getLast_name());
//
//            ActivePatientAdapter mActivePatientAdapter = new ActivePatientAdapter(activePatientList, ActivePatientActivity.this, listPatientUUID);
//            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ActivePatientActivity.this);
//            recyclerView.setLayoutManager(linearLayoutManager);
//           /* recyclerView.addItemDecoration(new
//                    DividerItemDecoration(this,
//                    DividerItemDecoration.VERTICAL));*/
//            recyclerView.setAdapter(mActivePatientAdapter);
//            mActivePatientAdapter.setActionListener(new ActivePatientAdapter.OnActionListener() {
//                @Override
//                public void onEndVisitClicked(ActivePatientModel activePatientModel, boolean hasPrescription) {
//                    String encounterAdultIntialslocal = "";
//                    String encounterVitalslocal = null;
//                    String encounterIDSelection = "visituuid = ?";
//
//                    String visitUuid = activePatientModel.getUuid();
//                    String visitnote = "", followupdate = "";
//                    String[] encounterIDArgs = {visitUuid};
//                    EncounterDAO encounterDAO = new EncounterDAO();
//                    Cursor encounterCursor = db.query("tbl_encounter", null, encounterIDSelection, encounterIDArgs, null, null, null);
//                    if (encounterCursor != null && encounterCursor.moveToFirst()) {
//                        do {
//                            if (encounterDAO.getEncounterTypeUuid("ENCOUNTER_VITALS").equalsIgnoreCase(encounterCursor.getString(encounterCursor.getColumnIndexOrThrow("encounter_type_uuid")))) {
//                                encounterVitalslocal = encounterCursor.getString(encounterCursor.getColumnIndexOrThrow("uuid"));
//                            }
//                            if (encounterDAO.getEncounterTypeUuid("ENCOUNTER_ADULTINITIAL").equalsIgnoreCase(encounterCursor.getString(encounterCursor.getColumnIndexOrThrow("encounter_type_uuid")))) {
//                                encounterAdultIntialslocal = encounterCursor.getString(encounterCursor.getColumnIndexOrThrow("uuid"));
//                            }
//
//                            if (encounterDAO.getEncounterTypeUuid("ENCOUNTER_VISIT_NOTE").equalsIgnoreCase(encounterCursor.getString(encounterCursor.getColumnIndexOrThrow("encounter_type_uuid")))) {
//                                visitnote = encounterCursor.getString(encounterCursor.getColumnIndexOrThrow("uuid"));
//                            }
//
//                        } while (encounterCursor.moveToNext());
//                    }
//                    encounterCursor.close();
//
//                    String[] visitArgs = {visitnote, UuidDictionary.FOLLOW_UP_VISIT};
//                    String[] columns = {"value", " conceptuuid"};
//                    String visitSelection = "encounteruuid = ? AND conceptuuid = ? and voided!='1' ";
//                    Cursor visitCursor = db.query("tbl_obs", columns, visitSelection, visitArgs, null, null, null);
//                    if (visitCursor.moveToFirst()) {
//                        do {
////                            String dbConceptID = visitCursor.getString(visitCursor.getColumnIndex("conceptuuid"));
//                            String dbValue = visitCursor.getString(visitCursor.getColumnIndex("value"));
//                            followupdate = dbValue;
//                        } while (visitCursor.moveToNext());
//                    }
//                    visitCursor.close();
//
//                    MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(ActivePatientActivity.this);
//                    if (hasPrescription) {
//                        alertDialogBuilder.setMessage(ActivePatientActivity.this.getResources().getString(R.string.end_visit_msg));
//                        alertDialogBuilder.setNegativeButton(ActivePatientActivity.this.getResources().getString(R.string.generic_cancel), new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                dialogInterface.dismiss();
//                            }
//                        });
//                        String finalFollowupdate = followupdate;
//                        String finalEncounterVitalslocal = encounterVitalslocal;
//                        String finalEncounterAdultIntialslocal = encounterAdultIntialslocal;
//                        alertDialogBuilder.setPositiveButton(ActivePatientActivity.this.getResources().getString(R.string.generic_ok), new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                                VisitUtils.endVisit(ActivePatientActivity.this,
//                                        visitUuid,
//                                        activePatientModel.getPatientuuid(),
//                                        finalFollowupdate,
//                                        finalEncounterVitalslocal,
//                                        finalEncounterAdultIntialslocal,
//                                        null,
//                                        String.format("%s %s", activePatientModel.getFirst_name(), activePatientModel.getLast_name()),
//                                        ""
//                                );
//                            }
//                        });
//                        AlertDialog alertDialog = alertDialogBuilder.show();
//                        //alertDialog.show();
//                        IntelehealthApplication.setAlertDialogCustomTheme(ActivePatientActivity.this, alertDialog);
//
//                    } else {
//                        alertDialogBuilder.setMessage(ActivePatientActivity.this.getResources().getString(R.string.error_no_data));
//                        alertDialogBuilder.setNeutralButton(ActivePatientActivity.this.getResources().getString(R.string.generic_ok), new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//                        AlertDialog alertDialog = alertDialogBuilder.show();
//                        //alertDialog.show();
//                        IntelehealthApplication.setAlertDialogCustomTheme(ActivePatientActivity.this, alertDialog);
//                    }
//                }
//            });
//        }
    }

    /*EZAZI*/
    private void getVisits() {

        ArrayList<String> encounterVisitUUID = new ArrayList<String>();
        HashSet<String> hsPatientUUID = new HashSet<String>();

        //Get all Visits
        VisitsDAO visitsDAO = new VisitsDAO();
        List<VisitDTO> visitsDTOList = visitsDAO.getAllVisits();

        //Get all Encounters
        EncounterDAO encounterDAO = new EncounterDAO();
        List<EncounterDTO> encounterDTOList = encounterDAO.getAllEncounters();

        //Get Visit Complete Encounters only, visit complete encounter id - bd1fbfaa-f5fb-4ebd-b75c-564506fc309e
        if (encounterDTOList.size() > 0) {
            for (int i = 0; i < encounterDTOList.size(); i++) {
                if (encounterDTOList.get(i).getEncounterTypeUuid().equalsIgnoreCase("bd1fbfaa-f5fb-4ebd-b75c-564506fc309e")) {
                    encounterVisitUUID.add(encounterDTOList.get(i).getVisituuid());
                }
            }
        }

        //Get patientUUID from visitList
        for (int i = 0; i < encounterVisitUUID.size(); i++) {

            for (int j = 0; j < visitsDTOList.size(); j++) {

                if (encounterVisitUUID.get(i).equalsIgnoreCase(visitsDTOList.get(j).getUuid())) {
                    listPatientUUID.add(visitsDTOList.get(j).getPatientuuid());
                }
            }
        }

        if (listPatientUUID.size() > 0) {

            hsPatientUUID.addAll(listPatientUUID);
            listPatientUUID.clear();
            listPatientUUID.addAll(hsPatientUUID);

        }
    }

    private String phoneNumber(String patientuuid) throws DAOException {
        String phone = null;
        Cursor idCursor = db.rawQuery("SELECT value  FROM tbl_patient_attribute where patientuuid = ? AND person_attribute_type_uuid='14d4f066-15f5-102d-96e4-000c29c2a5d7' ", new String[]{patientuuid});
        try {
            if (idCursor.getCount() != 0) {
                while (idCursor.moveToNext()) {

                    phone = idCursor.getString(idCursor.getColumnIndexOrThrow("value"));

                }
            }
        } catch (SQLException s) {
            FirebaseCrashlytics.getInstance().recordException(s);
        }
        idCursor.close();

        return phone;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (mSyncProgressDialog != null && mSyncProgressDialog.isShowing()) {
            syncUtils.initialSync("home");
        }
    }

    private BroadcastReceiver mCardMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract data included in the Intent
          recreate();
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mCardMessageReceiver);
    }


}