package sg.edu.nus.learnandroid.activity.database;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import sg.edu.nus.learnandroid.R;
import sg.edu.nus.learnandroid.database.UserAccountDB;

/**
 * Created by Yongxue
 */

public class DatabaseQuizActivity extends AppCompatActivity {

    UserAccountDB userAccountDB;
    Dialog dialog;

    public static final String MY_SHAREDPREF_NAME = "DatabaseQuizAnsSharedPref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_quiz);

        userAccountDB = new UserAccountDB(this);

        // Set up custom action bar with back button
        getSupportActionBar().setDisplayOptions(getActionBar().DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar_with_crossbtn);

        // Set up the back button and title on action bar
        View view = getSupportActionBar().getCustomView();

        TextView actionBarTitleTV = (TextView) findViewById(R.id.action_bar_title_with_crossbtn);
        actionBarTitleTV.setText(R.string.course_passing_data);

        ImageView backBtnIV = (ImageView) findViewById(R.id.action_bar_cross_with_crossbtn);
        backBtnIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initiateCancelQuizDialog();
            }
        });

        WebView webView = (WebView) findViewById(R.id.database_quiz_webview);
        JavaScriptInterface myJavaScriptInterface = new JavaScriptInterface(this, webView);

        webView.getSettings().setJavaScriptEnabled(true);
        WebView.setWebContentsDebuggingEnabled(true);
        webView.addJavascriptInterface(myJavaScriptInterface, "DatabaseQuiz");
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl("file:///android_asset/www/database_quiz.html");
    }

    @Override
    public void onBackPressed() {
        initiateCancelQuizDialog();
    }

    public class JavaScriptInterface {

        private DatabaseQuizActivity parentActivity;
        private WebView webView;

        public JavaScriptInterface(DatabaseQuizActivity databaseQuizActivity, WebView mWebView) {
            parentActivity = databaseQuizActivity;
            webView = mWebView;
        }

        @JavascriptInterface
        public void getQuizAnsFromWebView(String[] answers, int counts) {
            SharedPreferences.Editor editor = getSharedPreferences(MY_SHAREDPREF_NAME,
                    MODE_PRIVATE).edit();

            editor.putString("q1", answers[0]);
            editor.putString("q2", answers[1]);
            editor.commit();

            initiateSubmitQuizDialog(answers, counts);
        }
    }

    private void initiateSubmitQuizDialog(final String[] answers, final int counts) {

        dialog = new Dialog(DatabaseQuizActivity.this, R.style.Theme_Dialog_Cancel_Btn);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.submit_popup);

        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER_VERTICAL);
        dialogWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogWindow.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

        dialog.show();

        TextView title = (TextView) dialogWindow.findViewById(R.id.submit_popup_title_TV);

        if ((2 - counts) == 1) {
            title.setText(R.string.one_question_unanswered);
        } else if (counts != 2 && (2 - counts) != 1) {
            title.setText("There are " + (2 - counts) + " questions that you didn't answer. "
                    + "Are you sure you want to submit your quiz?");
        } else {
            title.setText(R.string.some_questions_unanswered);
        }

        Button cancelBtn = (Button) dialog.findViewById(R.id.submit_popup_cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Button yesBtn = (Button) dialog.findViewById(R.id.submit_popup_yes_btn);
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                calculatePoints(answers, counts);
                Intent myIntent = new Intent(getApplicationContext(), DatabaseQuizResultsActivity.class);
                startActivity(myIntent);
            }
        });
    }

    private void calculatePoints(String[] answers, int counts) {

        int points = 0;
        String[] correctAns = {"b", "d"};

        for (int i = 0; i < correctAns.length; i++) {
            if (answers[i].equals(correctAns[i])) {
                points = points + 1;
            }
        }

        userAccountDB.open();
        Cursor pointsCursor = userAccountDB.getRecordByIsLogin(1);

        int totalPtsFromDB = 0;
        int finalPoints = 0;
        int databaseQuizPtsFromDB = 0;
        int databaseCoursePassedFromDB = -1;

        if (pointsCursor != null && pointsCursor.moveToFirst() && (pointsCursor.getCount() == 1)) {
            do {
                totalPtsFromDB = Integer.valueOf(pointsCursor.getString(pointsCursor.getColumnIndex("points")));
                databaseQuizPtsFromDB = Integer.valueOf(pointsCursor.getString(pointsCursor
                        .getColumnIndex("databaseQuizPts")));
                databaseCoursePassedFromDB = Integer.valueOf(pointsCursor.getString(pointsCursor.getColumnIndex("databaseCoursePassed")));
            } while (pointsCursor.moveToNext());
        }

        if (databaseCoursePassedFromDB == 0) {
            if (points == 2) {
                databaseCoursePassedFromDB = databaseCoursePassedFromDB + 1;
                userAccountDB.updateDatabaseCoursePassedByIsLogin(1, databaseCoursePassedFromDB);
            }
        }

        if (databaseQuizPtsFromDB == 0) {
            finalPoints = totalPtsFromDB + points;
        } else {
            finalPoints = totalPtsFromDB - databaseQuizPtsFromDB + points;
        }

        userAccountDB.updatePointsByIsLogin(1, finalPoints);
        userAccountDB.updateDatabaseQuizPtsByIsLogin(1, points);
        userAccountDB.close();
        pointsCursor.close();
    }

    private void initiateCancelQuizDialog() {

        dialog = new Dialog(DatabaseQuizActivity.this, R.style.Theme_Dialog_Cancel_Btn);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.cancel_quiz_popup);

        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER_VERTICAL);
        dialogWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogWindow.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

        dialog.show();

        TextView title = (TextView) dialogWindow.findViewById(R.id.cancel_quiz_popup_title_TV);
        title.setText(R.string.cancel_quiz_popup_title_TV);

        Button leaveBtn = (Button) dialog.findViewById(R.id.cancel_quiz_popup_leave_btn);
        leaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent myIntent = new Intent(getApplicationContext(), DatabaseQuizInfoActivity.class);
                startActivity(myIntent);
            }
        });

        Button stayBtn = (Button) dialog.findViewById(R.id.cancel_quiz_popup_stay_btn);
        stayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}
