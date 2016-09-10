package com.devionlabs.ray.studentaggregator.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.devionlabs.ray.studentaggregator.R;
import com.devionlabs.ray.studentaggregator.helper.DBHelper;
import com.devionlabs.ray.studentaggregator.helper.DatabaseHelper;
import com.devionlabs.ray.studentaggregator.helper.LocalDB;
import com.devionlabs.ray.studentaggregator.model.Fee;
import com.devionlabs.ray.studentaggregator.model.ImageHelper;
import com.devionlabs.ray.studentaggregator.model.Student;
import com.devionlabs.ray.studentaggregator.model.User;
import com.devionlabs.ray.studentaggregator.other.AlarmReceiver;
import com.devionlabs.ray.studentaggregator.other.SampleBootReceiver;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ActivityMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private static final String LOG_TAG = "mylog";
    private TextView textViewName;
    private TextView textViewClass;
    private TextView textViewDivision;
    private TextView textViewHouse;
    private ImageButton imageButtonAttendance;
    private ImageButton imageButtonNotification;
    private ImageButton imageButtonFee;
    private ImageButton imageButtonService;
    private ImageView imageViewStudentPic;
    private ImageButton imageButtonRedDotAttendance;
    private ImageButton imageButtonRedDotFee;

    private TextView textViewParentName;
    private TextView textViewParentEmail;
    private TextView textViewParentContact;

    LocalDB localdb;
    User user;
    View header;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DBHelper db = new DBHelper(ActivityMenu.this);
        localdb = new LocalDB(ActivityMenu.this);
        user = localdb.getUser();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
        header = navigationView.getHeaderView(0);
        android.view.Menu menu = navigationView.getMenu();

        int i;
        ArrayList<Student> listStudents = db.getAllStudents();
        for (i = 0; i < listStudents.size(); i++) {

            final Student mystudent = listStudents.get(i);

            MenuItem menuItem = menu.add(R.id.nav_group_students, i, i, mystudent.getName());
            menuItem.setIcon(R.mipmap.studentet);
            final int finalI = i;
            menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Student newStudent = db.getStudent(mystudent.getId());
                    localdb.setStudent(newStudent);
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                    return false;
                }
            });
            Log.d(LOG_TAG, "ActivityMenu - " + listStudents.get(i).toString());
        }//end for


        bindUIComponents();
        setAllTexts();
        setStudentImage();
        setOnClickListeners();
        changeDueDateColor();

        if (!localdb.isAlarmSet()) {
            checkAndAlarmFeeDue();
        } else {
            Log.d(LOG_TAG, "ActivityMenu - Alarm is laready set");
        }


    }

    private void setStudentImage() {
        DatabaseHelper db2 = new DatabaseHelper(ActivityMenu.this);
        Student student = new LocalDB(ActivityMenu.this).getStudent();
        ImageHelper imageHelper = db2.getImage(student.getId());
        byte[] bytes = imageHelper.getImageByteArray();
        if (bytes == null) {
            Log.d(LOG_TAG, "ActivityMenu - Student Image in null");
        } else {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            imageViewStudentPic.setImageBitmap(bitmap);
        }
    }

    private void checkAndAlarmFeeDue() {

        Log.d(LOG_TAG, "ActivityMenu - Setting Alarm");

        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        Calendar calendarToday = Calendar.getInstance();
        Calendar calendarTomorrow = Calendar.getInstance();
        calendarTomorrow.set(calendarTomorrow.get(Calendar.YEAR),
                calendarTomorrow.get(Calendar.MONTH), (calendarTomorrow.get(Calendar.DATE) + 1), 9, 0, 0);
        /*
        Log.d(LOG_TAG, "calendarToday=" + calendarToday);
        Log.d(LOG_TAG, "calendarTommorow=" + calendarTomorrow);
        */

        long morningAt9 = calendarTomorrow.getTimeInMillis() - calendarToday.getTimeInMillis();

        // Set the alarm for a particular time.
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendarToday.getTimeInMillis(),
                morningAt9, alarmIntent);
        ComponentName receiver = new ComponentName(this, SampleBootReceiver.class);
        PackageManager pm = getPackageManager();

        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        int seconds = (int) (morningAt9 / 1000) % 60;
        int minutes = (int) ((morningAt9 / (1000 * 60)) % 60);
        int hours = (int) ((morningAt9 / (1000 * 60 * 60)) % 24);
        Log.d(LOG_TAG, "ActivityMenu - Alarm scheduled after : seconds=" + seconds + " minutes=" + minutes
                + " hours=" + hours);
    }


    private void bindUIComponents() {

        imageButtonAttendance = (ImageButton) findViewById(R.id.imageButtonAttendance);
        Glide.with(this).load(R.mipmap.attendancemin).into(imageButtonAttendance);
        imageButtonNotification = (ImageButton) findViewById(R.id.imageButtonNotification);
        Glide.with(this).load(R.mipmap.notify).into(imageButtonNotification);
        imageButtonFee = (ImageButton) findViewById(R.id.imageButtonFee);
        Glide.with(this).load(R.mipmap.feemin).into(imageButtonFee);
        imageButtonService = (ImageButton) findViewById(R.id.imageButtonService);
        Glide.with(this).load(R.mipmap.servicev).into(imageButtonService);

        imageButtonRedDotAttendance = (ImageButton) findViewById(R.id.imageButtonRedDotAttendance);
        imageButtonRedDotFee = (ImageButton) findViewById(R.id.imageButtonRedDotFee);

        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewClass = (TextView) findViewById(R.id.textViewClass);
        textViewDivision = (TextView) findViewById(R.id.textViewDivision);
        textViewHouse = (TextView) findViewById(R.id.textViewHouse);

        textViewParentName = (TextView) header.findViewById(R.id.textViewParentName);
        textViewParentEmail = (TextView) header.findViewById(R.id.textViewParentEmail);
        textViewParentContact = (TextView) header.findViewById(R.id.textViewParentContact);

        int width = 0;
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        imageViewStudentPic = (ImageView) findViewById(R.id.imageViewStudentPic);
        imageViewStudentPic.setAdjustViewBounds(true);
        imageViewStudentPic.setMaxWidth(width);
        imageViewStudentPic.setMaxHeight(width);
        imageViewStudentPic.setMinimumWidth(width);
        imageViewStudentPic.setMinimumHeight(width);
    }


    private void setAllTexts() {

        Student student = localdb.getStudent();
        textViewName.setText(student.getName());
        textViewClass.setText(student.getClassOfStudent());
        textViewDivision.setText(student.getDivision());
        textViewHouse.setText(student.getHouse());

        textViewParentName.setText(user.getName());
        textViewParentEmail.setText(user.getEmail());
        textViewParentContact.setText(user.getContact());

    }


    private void setOnClickListeners() {
        imageButtonAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityMenu.this, AttendanceActivity.class);
                startActivity(i);
            }
        });
        imageButtonFee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityMenu.this, ActivityFee.class);
                startActivity(i);
            }
        });
        imageButtonNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityMenu.this, Notification.class);
                startActivity(i);
            }
        });
        imageViewStudentPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityMenu.this, StudentsInformation.class);
                startActivity(i);
            }
        });
        imageButtonService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityMenu.this, ServicesActivity.class);
                startActivity(i);
            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu_activity, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_settings) {


            return true;
        } else if (id == R.id.action_change_pic) {
            pickFromGallery();
            return true;
        }
        else if(id==R.id.gallery){
            Intent intent=new Intent(this, ActivityGallery.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my_account) {

        } else if (id == R.id.nav_add_student) {
            Intent i = new Intent(ActivityMenu.this, AddStudent.class);
            startActivity(i);

        } else if (id == R.id.nav_manage_student) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*-----------------------crop image------------------------------*/
    private static final int REQUEST_SELECT_PICTURE = 0x01;

    private void pickFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_SELECT_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_SELECT_PICTURE) {
                final Uri selectedUri = data.getData();
                if (selectedUri != null) {
                    startCropActivity(data.getData());
                } else {
                    Toast.makeText(ActivityMenu.this, "cannot_retrieve_selected_image", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == UCrop.REQUEST_CROP) {
                handleCropResult(data);
            }
        }
        if (resultCode == UCrop.RESULT_ERROR) {
            handleCropError(data);
        }
    }

    private void startCropActivity(@NonNull Uri uri) {
        String destinationFileName = "MyImage.png";

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));
        uCrop.withAspectRatio(1, 1);
        uCrop.withMaxResultSize(500, 500);
        uCrop = advancedConfig(uCrop);

        uCrop.start(ActivityMenu.this);
    }

    private void handleCropResult(@NonNull Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                bitmap = getCroppedBitmap(bitmap);
                imageViewStudentPic.setImageBitmap(bitmap);

                //insert image into database
                DatabaseHelper db = new DatabaseHelper(ActivityMenu.this);
                db.insertImage(bitmap, new LocalDB(ActivityMenu.this).getStudent().getId().toString());

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(ActivityMenu.this, "cannot_retrieve_cropped_image", Toast.LENGTH_SHORT).show();
        }
    }

    private UCrop advancedConfig(@NonNull UCrop uCrop) {
        UCrop.Options options = new UCrop.Options();

        options.setCompressionFormat(Bitmap.CompressFormat.PNG);

        options.setCompressionQuality(90);


        //options.setMaxScaleMultiplier(5);
        options.setImageToCropBoundsAnimDuration(400);
        options.setDimmedLayerColor(Color.WHITE);
        options.setOvalDimmedLayer(true);
        //options.setShowCropFrame(false);
        //options.setCropGridStrokeWidth(20);
        //options.setCropGridColor(Color.GREEN);
        //options.setCropGridColumnCount(2);
        //options.setCropGridRowCount(1);


        // Color palette
        options.setToolbarColor(ContextCompat.getColor(this, R.color.MainThemeColor));
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        options.setActiveWidgetColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        options.setToolbarWidgetColor(ContextCompat.getColor(this, R.color.colorWhite));


        return uCrop.withOptions(options);
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Log.e(LOG_TAG, "handleCropError: ", cropError);
            Toast.makeText(ActivityMenu.this, cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(ActivityMenu.this, "unexpected_error", Toast.LENGTH_SHORT).show();
        }
    }

    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
    /*-----------------------end crop image------------------------------*/

    private void changeDueDateColor() {
        Fee fee = localdb.getNextPayment(localdb.getStudent().getId());
        String dueTime = fee.getTime();

        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Date inputDate;
        Date todaysDate = new Date();

        try {
            inputDate = fmt.parse(dueTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }

        inputDate.setDate(inputDate.getDate() - 7);

        //Log.d(MyUtils.LOG_TAG, "ActivityMenu - inputDate = " + inputDate);
        //Log.d(MyUtils.LOG_TAG, "ActivityMenu - today'sDate = " + todaysDate);

        if (todaysDate.compareTo(inputDate) > 0) {
            imageButtonRedDotFee.setVisibility(View.VISIBLE);

            final Animation animation = new AlphaAnimation(1, 0);
            animation.setDuration(1000);
            animation.setInterpolator(new LinearInterpolator());
            animation.setRepeatCount(Animation.INFINITE);
            animation.setRepeatMode(Animation.REVERSE);
            imageButtonRedDotFee.startAnimation(animation);
        }
    }
}
