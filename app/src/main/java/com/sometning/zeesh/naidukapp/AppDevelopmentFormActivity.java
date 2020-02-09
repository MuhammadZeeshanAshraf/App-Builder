package com.sometning.zeesh.naidukapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.tooltip.Tooltip;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import in.mayanknagwanshi.imagepicker.ImageSelectActivity;
import mehdi.sakout.fancybuttons.FancyButton;
import pl.droidsonroids.gif.GifImageView;

public class AppDevelopmentFormActivity extends AppCompatActivity {

    //Paypal intent request code to track onActivityResult method
    public static final int PAYPAL_REQUEST_CODE = 123;
    //Paypal Configuration Object
    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);
    final int UPI_PAYMENT = 0;
    String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    int GOOGLE_PAY_REQUEST_CODE = 1231;
    int price = 0;
    Button uploadLogo, urlAdd, urlRemove, submit;
    RoundedImageView logo;
    LinearLayout addEmailContainer, addCallContainer, addWhatsContainer, addRate, addPush, addFeedContainer, addAdsContainer, emailContainer, callContainer, whatsContainer, feedContainer, adsContainer, addActionContainer, actionContainer;
    ImageView emailAdd, callAdd, whatsAdd, rateAdd, feedAdd, actionAdd, pushAdd, signout;
    RadioGroup logoType;
    RadioButton a, b;
    CheckBox adsCheck;
    TextInputLayout logoContainer;
    TextView logoText;
    LinearLayout urlLayout;
    EditText appname, urlNo, email, call, whatsapp, feed, adsID, appCode, color , bannerid , interid;
    List<EditText> concernedEditTexts;
    ProgressBar loadingBar;
    FancyButton priceIndicator;

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    DatabaseReference rootReference;
    String currentUserId;
    String paymentAmount, PushID, PayingUsername, logoPath;
    Bitmap logoBitmap;
    Uri fileUri, filepathUri;

    ImageView emailInfo , urlInfo , callInfo , whatInfo , rateInfo , actionInfo, pushInfo , feedInfo, adsInfo;
    Tooltip emailTooltip , urlTooltip , callTooltip , whatTooltip, rateTooltip ,actionTooltip ,pushTooltip , feedTooltip , adsTooltip;
    Button remove, learn ;

    ScrollView mainlayout;
    FrameLayout main;
    RelativeLayout mainlay;
    LinearLayout mai;
    int toolcheck = 0;
    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_app_development_form);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        rootReference = FirebaseDatabase.getInstance().getReference();

        Intent intent = new Intent(this, PayPalService.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        startService(intent);

        mainlayout = findViewById(R.id.mainLayout);
        main = findViewById(R.id.fra);
        mainlay= findViewById(R.id.rel);
        mai = findViewById(R.id.lin);

        remove = findViewById(R.id.infoRemove);
        learn = findViewById(R.id.learn);
        loadingBar = findViewById(R.id.submitLoadingBar);
        Wave wave = new Wave();
        loadingBar.setIndeterminateDrawable(wave);
        priceIndicator = findViewById(R.id.priceIndicator);
        price = 10;

        emailInfo = findViewById(R.id.emailInfo);
        urlInfo = findViewById(R.id.urlInfo);
        callInfo = findViewById(R.id.callInfo);
        whatInfo = findViewById(R.id.whatInfo);
        rateInfo = findViewById(R.id.rateInfo);
        actionInfo = findViewById(R.id.actionInfo);
        pushInfo = findViewById(R.id.pushInfo);
        feedInfo = findViewById(R.id.feedInfo);
        adsInfo = findViewById(R.id.adsInfo);

        mainlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissTooltip();
            }
        });
        mai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissTooltip();
            }
        });
        mainlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissTooltip();
            }
        });
        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissTooltip();
            }
        });

        learn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://frnato.com/app-builder/"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                dismissTooltip();
            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               dismissTooltip();
               Uri uri = Uri.parse("mailto:amatyarevenue@gmail.com"); // missing 'http://' will cause crashed
               Intent intent = new Intent(Intent.ACTION_VIEW, uri);
               startActivity(intent);

           }

       });

       emailInfo.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               dismissTooltip();
               emailTooltip = new Tooltip.Builder(emailInfo)
                       .setText("Add your email address here and let your customer contact you by an email")
                       .setBackgroundColor(getResources().getColor(R.color.green))
                       .setTextColor(Color.WHITE)
                       .setGravity(Gravity.START)
                       .show();
               toolcheck = 1;
           }
       });
        urlInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissTooltip();
                urlTooltip = new Tooltip.Builder(urlInfo)
                        .setText("Add your website page link which you want to add in sidebar")
                        .setBackgroundColor(getResources().getColor(R.color.green))
                        .setTextColor(Color.WHITE)
                        .setGravity(Gravity.START)
                        .show();
                toolcheck = 2;

            }
        });
        callInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissTooltip();
                callTooltip = new Tooltip.Builder(callInfo)
                        .setText("Add your phone no. here and let your customer contact you by an phone call")
                        .setBackgroundColor(getResources().getColor(R.color.green))
                        .setTextColor(Color.WHITE)
                        .setGravity(Gravity.START)
                        .show();
                toolcheck = 3;

            }
        });
        whatInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissTooltip();
                whatTooltip = new Tooltip.Builder(whatInfo)
                        .setText("Add your whatsapp no. here to let your userstay connected with you on whatsapp")
                        .setBackgroundColor(getResources().getColor(R.color.green))
                        .setTextColor(Color.WHITE)
                        .setGravity(Gravity.START)
                        .show();
                toolcheck = 4;

            }
        });
        rateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissTooltip();
                rateTooltip = new Tooltip.Builder(rateInfo)
                        .setText("let your user to rate this app on playstore")
                        .setBackgroundColor(getResources().getColor(R.color.green))
                        .setTextColor(Color.WHITE)
                        .setGravity(Gravity.START)
                        .show();
                toolcheck = 5;

            }
        });
        actionInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissTooltip();
                actionTooltip = new Tooltip.Builder(actionInfo)
                        .setText("choose your brand colour ( if any )")
                        .setBackgroundColor(getResources().getColor(R.color.green))
                        .setTextColor(Color.WHITE)
                        .setGravity(Gravity.START)
                        .show();
                toolcheck = 6;

            }
        });
        pushInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissTooltip();
                pushTooltip = new Tooltip.Builder(pushInfo)
                        .setText("keep your users updated about new offers, deal, and informaltion through an notification (Learn more )")
                        .setBackgroundColor(getResources().getColor(R.color.green))
                        .setTextColor(Color.WHITE)
                        .setGravity(Gravity.START)
                        .show();
                toolcheck = 7;

            }
        });
        feedInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissTooltip();
                feedTooltip = new Tooltip.Builder(feedInfo)
                        .setText("show your blogs post to your users and keep them updated ( learn more)")
                        .setBackgroundColor(getResources().getColor(R.color.green))
                        .setTextColor(Color.WHITE)
                        .setGravity(Gravity.START)
                        .show();
                toolcheck = 8;

            }
        });
        adsInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissTooltip();
                adsTooltip = new Tooltip.Builder(adsInfo)
                        .setText("monitize your app with admob ads (  learn more )")
                        .setBackgroundColor(getResources().getColor(R.color.green))
                        .setTextColor(Color.WHITE)
                        .setGravity(Gravity.START)
                        .show();
                toolcheck = 9;

            }
        });



        signout = findViewById(R.id.signOut);
        appname = findViewById(R.id.appName);
        email = findViewById(R.id.appEmail);
        call = findViewById(R.id.appCall);
        whatsapp = findViewById(R.id.appWhats);
        feed = findViewById(R.id.appFeed);
        adsID = findViewById(R.id.appId);
        appCode = findViewById(R.id.appCode);
        color = findViewById(R.id.appActionColor);
        bannerid = findViewById(R.id.bannerID);
        interid = findViewById(R.id.InterstitialID);

        logoContainer = findViewById(R.id.uploadLogoContainer);
        uploadLogo = findViewById(R.id.uploadLogo);
        logo = findViewById(R.id.uploadedLogoImage);
        logoType = findViewById(R.id.logoType);
        logoText = findViewById(R.id.logoText);

//        adsType = findViewById(R.id.adsType);
//        c = findViewById(R.id.banner);
//        d = findViewById(R.id.Interstitial);
//        e = findViewById(R.id.Both);

        addEmailContainer = findViewById(R.id.addEmailContainer);
        addCallContainer = findViewById(R.id.addCallContainer);
        addWhatsContainer = findViewById(R.id.addWhatsContainer);
        addFeedContainer = findViewById(R.id.addFeedContainer);
        addAdsContainer = findViewById(R.id.addAdsContainer);
        addActionContainer = findViewById(R.id.addActionContainer);
        addRate = findViewById(R.id.addRateContainer);
        addPush = findViewById(R.id.pushContainer);

        emailContainer = findViewById(R.id.emailContainer);
        callContainer = findViewById(R.id.callContainer);
        whatsContainer = findViewById(R.id.whatsContainer);
        feedContainer = findViewById(R.id.feedContainer);
        adsContainer = findViewById(R.id.adsContainer);
        actionContainer = findViewById(R.id.actionContainer);

        emailContainer.setVisibility(View.GONE);
        callContainer.setVisibility(View.GONE);
        whatsContainer.setVisibility(View.GONE);
        feedContainer.setVisibility(View.GONE);
        adsContainer.setVisibility(View.GONE);
        actionContainer.setVisibility(View.GONE);

        emailAdd = findViewById(R.id.emailAdd);
        callAdd = findViewById(R.id.callAdd);
        whatsAdd = findViewById(R.id.whatsAdd);
        rateAdd = findViewById(R.id.rateAdd);
        pushAdd = findViewById(R.id.pushAdd);
        feedAdd = findViewById(R.id.feedAdd);
        actionAdd = findViewById(R.id.actionAdd);

        a = findViewById(R.id.logoTypeOne);
        b = findViewById(R.id.logoTypeTwo);

        adsCheck = findViewById(R.id.adsCheck);

        concernedEditTexts = new ArrayList<EditText>();
        urlLayout = findViewById(R.id.urlLayout);
        urlNo = findViewById(R.id.noOfurl);
        urlAdd = findViewById(R.id.urlAdd);
        urlRemove = findViewById(R.id.urlRemove);
        urlRemove.setVisibility(View.GONE);
        urlRemove.setClickable(false);


        submit = findViewById(R.id.submit);

        Drawable myDrawable = getResources().getDrawable(R.drawable.add_red);
        final Bitmap cmp = ((BitmapDrawable) myDrawable).getBitmap();


        addEmailContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissTooltip();
                final Bitmap add = ((BitmapDrawable) emailAdd.getDrawable()).getBitmap();
                if (add.sameAs(cmp)) {
                    emailContainer.setVisibility(View.VISIBLE);
                    emailAdd.setImageResource(R.drawable.minu);
                } else {
                    emailContainer.setVisibility(View.GONE);
                    emailAdd.setImageResource(R.drawable.add_red);
                }

            }
        });

        addActionContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissTooltip();
                final Bitmap add = ((BitmapDrawable) actionAdd.getDrawable()).getBitmap();
                if (add.sameAs(cmp)) {
                    actionContainer.setVisibility(View.VISIBLE);
                    actionAdd.setImageResource(R.drawable.minu);
                } else {
                    actionContainer.setVisibility(View.GONE);
                    actionAdd.setImageResource(R.drawable.add_red);
                }

            }
        });
        addCallContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissTooltip();
                final Bitmap add = ((BitmapDrawable) callAdd.getDrawable()).getBitmap();
                if (add.sameAs(cmp)) {
                    callContainer.setVisibility(View.VISIBLE);
                    callAdd.setImageResource(R.drawable.minu);
                    price = price + 5;
                    priceIndicator.setText("$ " + price);
                } else {
                    callContainer.setVisibility(View.GONE);
                    callAdd.setImageResource(R.drawable.add_red);
                    price = price - 5;
                    priceIndicator.setText("$ " + price);
                }


            }
        });

        addWhatsContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissTooltip();
                final Bitmap add = ((BitmapDrawable) whatsAdd.getDrawable()).getBitmap();
                if (add.sameAs(cmp)) {
                    whatsContainer.setVisibility(View.VISIBLE);
                    whatsAdd.setImageResource(R.drawable.minu);
                    price = price + 5;
                    priceIndicator.setText("$ " + price);
                } else {
                    whatsContainer.setVisibility(View.GONE);
                    whatsAdd.setImageResource(R.drawable.add_red);
                    price = price - 5;
                    priceIndicator.setText("$ " + price);
                }

            }
        });
        addFeedContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissTooltip();
                final Bitmap add = ((BitmapDrawable) feedAdd.getDrawable()).getBitmap();
                if (add.sameAs(cmp)) {
                    feedContainer.setVisibility(View.VISIBLE);
                    feedAdd.setImageResource(R.drawable.minu);
                    price = price + 5;
                    priceIndicator.setText("$ " + price);
                } else {
                    feedContainer.setVisibility(View.GONE);
                    feedAdd.setImageResource(R.drawable.add_red);
                    price = price - 5;
                    priceIndicator.setText("$ " + price);
                }

            }
        });
        addAdsContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissTooltip();
                if (adsCheck.isChecked()) {
                    adsContainer.setVisibility(View.VISIBLE);
                } else {

                }


            }
        });

        addRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissTooltip();
                final Bitmap add = ((BitmapDrawable) rateAdd.getDrawable()).getBitmap();
                if (add.sameAs(cmp)) {
                    rateAdd.setImageResource(R.drawable.checked);
                } else {
                    rateAdd.setImageResource(R.drawable.add_red);
                }

            }
        });

        addPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissTooltip();
                final Bitmap add = ((BitmapDrawable) pushAdd.getDrawable()).getBitmap();
                if (add.sameAs(cmp)) {
                    pushAdd.setImageResource(R.drawable.checked);
                    price = price + 5;
                    priceIndicator.setText("$ " + price);
                } else {
                    pushAdd.setImageResource(R.drawable.add_red);
                    price = price - 5;
                    priceIndicator.setText("$ " + price);
                }

            }
        });

        uploadLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissTooltip();
                Intent intent = new Intent(AppDevelopmentFormActivity.this, ImageSelectActivity.class);
                intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, true);
                intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true);
                intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true);
                startActivityForResult(intent, 1213);
            }
        });


        adsCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                dismissTooltip();
                if (b) {
                    adsContainer.setVisibility(View.VISIBLE);
                    price = price + 5;
                    priceIndicator.setText("$ " + price);
                } else {
                    adsContainer.setVisibility(View.GONE);
                    price = price - 5;
                    priceIndicator.setText("$ " + price);
                }
            }
        });

        logoType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                dismissTooltip();
                int selectedId = logoType.getCheckedRadioButtonId();
                if (selectedId == b.getId()) {
                    logoContainer.setVisibility(View.VISIBLE);
                } else {
                    logoContainer.setVisibility(View.GONE);
                }
            }
        });

        urlAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissTooltip();
                if (TextUtils.isEmpty(urlNo.getText())) {
                    Toasty.error(AppDevelopmentFormActivity.this, "Enter the no of website url you want to add.", Toasty.LENGTH_SHORT).show();
                    urlNo.setError("Field Required");
                } else {

                    int couter = Integer.parseInt(urlNo.getText().toString());
                    if (couter > 13 || couter < 1) {
                        Toasty.error(AppDevelopmentFormActivity.this, "Enter the number Range from 1-12.", Toasty.LENGTH_SHORT).show();

                    } else if ((urlLayout.getChildCount() + couter) > 12) {
                        Toasty.info(AppDevelopmentFormActivity.this, "You can only " + (12 - urlLayout.getChildCount()) + " URL Left!", Toasty.LENGTH_SHORT).show();
                    } else {


                        for (int i = 0; i < couter; i++) {

                            // add edittext
                            EditText et = new EditText(AppDevelopmentFormActivity.this);
                            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            et.setLayoutParams(p);
                            et.setHint("https://www.example.com/");
                            et.setId(View.generateViewId());
                            concernedEditTexts.add(et);
                            urlLayout.addView(et);
                        }
                        urlNo.setText(urlLayout.getChildCount() + "");
                        urlLayout.requestFocus();
                        urlRemove.setVisibility(View.VISIBLE);
                        urlRemove.setClickable(true);
                        if (urlLayout.getChildCount() > 4) {
                            price = price + 5;
                            priceIndicator.setText("$ " + price);
                        }
                    }

                }
            }
        });

        urlRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissTooltip();
                if (urlLayout.getChildCount() > 1) {
                    urlLayout.removeViewAt(urlLayout.getChildCount() - 1);
                    urlNo.setText(urlLayout.getChildCount() + "");
                    concernedEditTexts.remove(concernedEditTexts.size() - 1);
                    if (urlLayout.getChildCount() < 5 && urlLayout.getChildCount() >= 4) {
                        price = price - 5;
                        priceIndicator.setText("$ " + price);
                    }

                }
                if (urlLayout.getChildCount() == 1) {
                    urlRemove.setVisibility(View.GONE);
                    urlRemove.setClickable(false);
                }

            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                SendUserToLoginActivity();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissTooltip();
                String finalEmail = email.getText().toString().trim();
                boolean valid = false;
                if (TextUtils.isEmpty(appname.getText())) {
                    Toasty.error(AppDevelopmentFormActivity.this, "Enter the Name of App.", Toasty.LENGTH_SHORT).show();
                    appname.setError("Field Required");
                    valid = true;
                } else if (logoType.getCheckedRadioButtonId() == -1) {
                    Toasty.error(AppDevelopmentFormActivity.this, "Select At least one Logo Option.", Toasty.LENGTH_SHORT).show();
                    valid = true;

                }else if (urlLayout.getChildCount() < 1) {
                    Toasty.error(AppDevelopmentFormActivity.this, "Add At least one Website URL.", Toasty.LENGTH_SHORT).show();
                    valid = true;

                } else if (urlLayout.getChildCount() > 0) {
                    boolean miss = false;
                    for (EditText editText : concernedEditTexts) {
                        if (TextUtils.isEmpty(editText.getText())) {
                            editText.setError("URL Missing");
                            miss = true;
                        }
                    }
                    if (miss) {
                        valid = true;
                        Toasty.error(AppDevelopmentFormActivity.this, "Website URL Missing.", Toasty.LENGTH_SHORT).show();
                    }

                }

                if (finalEmail.equals("")) {
                    valid = true;
                    emailContainer.setVisibility(View.VISIBLE);
                    emailAdd.setImageResource(R.drawable.minu);
                    Toasty.error(AppDevelopmentFormActivity.this, "Enter the Email." + email.getText(), Toasty.LENGTH_SHORT).show();
                    email.setError("Field Required");
                }

                if (!(logoType.getCheckedRadioButtonId() == -1)) {

                    int selectedId = logoType.getCheckedRadioButtonId();
                    if (selectedId == b.getId()) {
                        if(logoBitmap == null)
                        {
                            valid = true;
                            Toasty.error(AppDevelopmentFormActivity.this, "Upload App Logo.", Toasty.LENGTH_SHORT).show();

                        }
                    }

                }

                if (!valid) {
//                    AppDevelopmentFormSubmission();
                    PaymentMethod();
//                    showComplete();
                }
            }
        });
    }

    private void dismissTooltip()
    {
        if(toolcheck == 2)
        {
            urlTooltip.dismiss();
        }
        if(toolcheck == 1)
        {
            emailTooltip.dismiss();
        }

        if(toolcheck == 3)
        {
            callTooltip.dismiss();
        }
        if(toolcheck == 4)
        {
            whatTooltip.dismiss();
        }
        if(toolcheck == 5)
        {
            rateTooltip.dismiss();
        }
        if(toolcheck == 6)
        {
            actionTooltip.dismiss();
        }
        if(toolcheck == 7)
        {
            pushTooltip.dismiss();

        }
        if(toolcheck == 8)
        {
            feedTooltip.dismiss();
        }
        if(toolcheck == 9)
        {
            adsTooltip.dismiss();
        }



    }

    private void AppDevelopmentFormSubmission(int ptype) {
        String Name = appname.getText().toString().trim();
        String Email = email.getText().toString().trim();
        String typeOfLogo = "";
        String logoStoragePath = "Null";
        String noOfURL = urlNo.getText().toString().trim();
        ArrayList<String> urllist = new ArrayList<String>();
        String ContactNo = "";
        String WhatsAppNo = "";
        String ActionBarColor = "";
        String Rate = "No";
        String PushNotification = "No";
        String FeedURL = "";
        String AdsIntergation = "No";
        String AppID = "";
        String AdsType = "Banner";
        String AdmobCode = "";
        String Bannerid = "";
        String Interid = "";

        loadingBar.setVisibility(View.VISIBLE);

        StringBuilder strBuilder = new StringBuilder();
        String str = "";

        for (EditText editText : concernedEditTexts) {
            strBuilder.append(editText.getText().toString());
            strBuilder.append("#:::#");
        }
        str = strBuilder.toString();
        String[] items = str.split("#:::#");
        for (String item : items) {
            urllist.add(item);


        }

        int selectedId = logoType.getCheckedRadioButtonId();
        if (selectedId == b.getId()) {
            typeOfLogo = "Image Logo";
            logoStoragePath = logoPath;
        } else {
            typeOfLogo = "Initial Letter of App Name";
        }


        if (call.getText().toString().trim().equals("")) {
            ContactNo = "Null";
        } else {
            ContactNo = call.getText().toString().trim();
        }
        if (whatsapp.getText().toString().trim().equals("")) {
            WhatsAppNo = "Null";
        } else {
            WhatsAppNo = whatsapp.getText().toString().trim();
        }
        Drawable myDrawable = getResources().getDrawable(R.drawable.checked);
        final Bitmap cmp = ((BitmapDrawable) myDrawable).getBitmap();
        final Bitmap add = ((BitmapDrawable) rateAdd.getDrawable()).getBitmap();
        if (add.sameAs(cmp)) {
            Rate = "Yes";
        }
        final Bitmap p = ((BitmapDrawable) pushAdd.getDrawable()).getBitmap();
        if (add.sameAs(cmp)) {
            PushNotification = "Yes";
        }
        if (color.getText().toString().trim().equals("")) {
            ActionBarColor = "Null";
        } else {
            ActionBarColor = color.getText().toString().trim();
        }
        if (feed.getText().toString().trim().equals("")) {
            FeedURL = "Null";
        } else {
            FeedURL = color.getText().toString().trim();
        }
        if (adsCheck.isChecked()) {
            AdsIntergation = "Yes";

            if (adsID.getText().toString().trim().equals("")) {
                AppID = "Null";
            } else {
                AppID = adsID.getText().toString().trim();
            }
            if (appCode.getText().toString().trim().equals("")) {
                AdmobCode = "Null";
            } else {
                AdmobCode = appCode.getText().toString().trim();
            }
            if (bannerid.getText().toString().trim().equals("")) {
                Bannerid = "Null";
            } else {
                Bannerid = bannerid.getText().toString().trim();
            }
            if (interid.getText().toString().trim().equals("")) {
                Interid = "Null";
            } else {
                Interid = interid.getText().toString().trim();
            }
//            int selected = adsType.getCheckedRadioButtonId();
//            if (selected == c.getId()) {
//                AdsType = "Banner";
//            }
//            if (selected == d.getId()) {
//                AdsType = "Interstitial";
//            }
//            if (selected == c.getId()) {
//                AdsType = "Both";
//            }

        }

        DatabaseReference userFormKeyRef = rootReference.child("AppDevelopmentForm").child(currentUserId).push();
        PushID = userFormKeyRef.getKey();

        HashMap<String, Object> FormMap = new HashMap<>();
        FormMap.put("UserID", currentUserId);
        FormMap.put("PushKey", PushID);
        FormMap.put("AppName", Name);
        FormMap.put("Email", Email);
        FormMap.put("LogoType", typeOfLogo);
        FormMap.put("LogoStoragePath", logoStoragePath);
        FormMap.put("NumberOFWebsiteURL", noOfURL);
        FormMap.put("CallUs", ContactNo);
        FormMap.put("WhatsApp", WhatsAppNo);
        FormMap.put("RateUs", Rate);
        FormMap.put("PushNotification", PushNotification);
        FormMap.put("ActionBarColor", ActionBarColor);
        FormMap.put("WebsiteFeed", FeedURL);
        FormMap.put("Price", "$ " + price);
        FormMap.put("PayMethod", "PayPal");
        FormMap.put("Payment", "Pending");

        String ref = "AppDevelopmentForm";
        Map BodyDetails = new HashMap();
        BodyDetails.put(ref + "/" + currentUserId + "/" + PushID, FormMap);

        HashMap<String, Object> Ads = new HashMap<>();
        Ads.put("AppID", AppID);
        Ads.put("AdsIntergation", AdsIntergation);
        Ads.put("AdsType", AdsType);
        Ads.put("AdMobCode", AdmobCode);
        Ads.put("BannerID", Bannerid);
        Ads.put("InterstitialID", Interid);

        if (!(logoStoragePath.equals("Null"))) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("LogoFiles");
            final StorageReference filepath = storageReference.child(Name + PushID + "." + "jpg");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            logoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = filepath.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            filepathUri = uri;

                            FormMap.put("LogoStoragePath", filepathUri.toString());
                        }
                    });
                }
            });

        }
        rootReference.child("AppDevelopmentForm").child(currentUserId).child(PushID).updateChildren(FormMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {


                if (task.isSuccessful()) {
                    rootReference.child("AppDevelopmentForm").child(currentUserId).child(PushID).child("AdsDetail").updateChildren(Ads).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                for (int i = 0; i < items.length; i++) {
                                    rootReference.child("AppDevelopmentForm").child(currentUserId).child(PushID).child("WebsiteURlAdded").child("url" + (i + 1)).setValue(urllist.get(i));
                                }

                                loadingBar.setVisibility(View.GONE);

                                Toasty.success(AppDevelopmentFormActivity.this, "Form Submit Sucessfully", Toasty.LENGTH_SHORT).show();

                                appname.setText("");
                                urlNo.setText("");
                                email.setText("");
                                call.setText("");
                                whatsapp.setText("");
                                feed.setText("");
                                adsID.setText("");
                                appCode.setText("");
                                color.setText("");
                                pushAdd.setImageResource(R.drawable.add_red);
                                rateAdd.setImageResource(R.drawable.add_red);
                                urlLayout.removeAllViews();

                                if (ptype == 1) {
                                    getPayment();
                                } else {
                                    googleAppMethod();
                                }
                            }
                        }
                    });

                } else {
                    loadingBar.setVisibility(View.GONE);
                    Toasty.error(AppDevelopmentFormActivity.this, task.getException().toString() + "", Toasty.LENGTH_SHORT).show();

                }

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        if (currentUser == null) {
//            SendUserToLoginActivity();
            SendUserToMainActivity();
        } else {
            currentUserId = currentUser.getUid();
            rootReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    PayingUsername = dataSnapshot.child(currentUserId).child("Username").getValue().toString().trim();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void SendUserToLoginActivity() {
        Intent intent = new Intent(AppDevelopmentFormActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void SendUserToMainActivity() {
        Intent intent = new Intent(AppDevelopmentFormActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1213 && resultCode == Activity.RESULT_OK) {
            String filePath = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
            Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
            fileUri = data.getData();
            logoPath = filePath;
            logoBitmap = selectedImage;
            logo.setImageBitmap(Bitmap.createScaledBitmap(selectedImage, 256, 256, true));
            logoText.setVisibility(View.VISIBLE);
        }

        //If the result is from paypal
        if (requestCode == PAYPAL_REQUEST_CODE) {

            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {
                //Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                //if confirmation is not null
                if (confirm != null) {
                    try {
                        //Getting the payment details
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Log.i("paymentExample", paymentDetails);

                        //Starting a new activity for the payment details and also putting the payment details with intent
//                        startActivity(new Intent(this, ConfirmationActivity.class)
//                                .putExtra("PaymentDetails", paymentDetails)
//                                .putExtra("PaymentAmount", paymentAmount));
//


//                        rootReference.child("AppDevelopmentForm").child(currentUserId).child(PushID).child("Payment").setValue("Verifed");
                        showComplete();

                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");

            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }

        if (requestCode == GOOGLE_PAY_REQUEST_CODE) {
            if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                if (data != null) {
                    String trxt = data.getStringExtra("response");
                    Log.e("UPI", "onActivityResult: " + trxt);
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add(trxt);
                    upiPaymentDataOperation(dataList);
                } else {
                    Log.e("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
            } else {
                //when user simply back without payment
                Log.e("UPI", "onActivityResult: " + "Return data is null");
                ArrayList<String> dataList = new ArrayList<>();
                dataList.add("nothing");
                upiPaymentDataOperation(dataList);
            }
        }
    }

    private void showComplete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AppDevelopmentFormActivity.this);
        View view = getLayoutInflater().inflate(R.layout.complete, null);
        GifImageView gif = view.findViewById(R.id.gif);

        builder.setView(view);
        final AlertDialog dialog = builder.create();


        dialog.show();

        rootReference.child("AppDevelopmentForm").child(currentUserId).child(PushID).child("Payment").setValue("Verifed");
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()) {
                    Intent intent = new Intent(AppDevelopmentFormActivity.this, AppDevelopmentFormActivity.class);
                    startActivity(intent);
                    finish();
                    dialog.dismiss();
                }
            }
        };

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });

        handler.postDelayed(runnable, 5000);

    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    private void getPayment() {
        //Getting the amount from editText
        paymentAmount = price + "";

        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(paymentAmount)), "USD", "App Development Cost",
                PayPalPayment.PAYMENT_INTENT_SALE);

        //Creating Paypal Payment activity intent
        Intent intent = new Intent(this, PaymentActivity.class);

        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        //Puting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        //Starting the intent activity for result
        //the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    private void PaymentMethod() {


        AlertDialog.Builder builder = new AlertDialog.Builder(AppDevelopmentFormActivity.this);
        View view = getLayoutInflater().inflate(R.layout.custom_group_dialog, null);
        Button pay = view.findViewById(R.id.pay);
        Button cancel = view.findViewById(R.id.cancel);
        RadioGroup type = view.findViewById(R.id.payType);
        RadioButton paypal = view.findViewById(R.id.paypal);
        RadioButton gpay = view.findViewById(R.id.gPay);
        builder.setView(view);
        final AlertDialog dialog = builder.create();

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (type.getCheckedRadioButtonId() == -1) {
                    Toasty.error(AppDevelopmentFormActivity.this, "Select At least one Pay Option.", Toasty.LENGTH_SHORT).show();

                } else {
                    int selectedId = type.getCheckedRadioButtonId();
                    if (selectedId == paypal.getId()) {
                        AppDevelopmentFormSubmission(1);
                        dialog.cancel();
                    } else {
                        AppDevelopmentFormSubmission(2);
                        dialog.cancel();
//                        googleAppMethod();
                    }

                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private void googleAppMethod() {

        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", "amatyarevenue@okicici")
                .appendQueryParameter("pn", PayingUsername)
                .appendQueryParameter("tn", "App Development Cost")
                .appendQueryParameter("am", (price * 70) + "")
                .appendQueryParameter("cu", "IND")
                .build();
        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);
        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");
        // check if intent resolves
        if (null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(AppDevelopmentFormActivity.this, "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show();
        }


    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(AppDevelopmentFormActivity.this)) {
            String str = data.get(0);
            Log.e("UPIPAY", "upiPaymentDataOperation: " + str);
            String paymentCancel = "";
            if (str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if (equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    } else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                } else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }
            if (status.equals("success")) {
                //Code to handle successful transaction here.
                showComplete();
                Toast.makeText(AppDevelopmentFormActivity.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "payment successfull: " + approvalRefNo);
            } else if ("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(AppDevelopmentFormActivity.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "Cancelled by user: " + approvalRefNo);
            } else {
                Toast.makeText(AppDevelopmentFormActivity.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "failed payment: " + approvalRefNo);
            }
        } else {
            Log.e("UPI", "Internet issue: ");
            Toast.makeText(AppDevelopmentFormActivity.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }


}
