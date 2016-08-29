package com.example.paintproject;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.graphics.Paint;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView colorBtn,sizeBtn,newBtn;
    private final static String TAG = "Main";
    private static final int REQUEST_CODE_PERMISSION = 2;
    private static final int SELECT_PICTURE = 100;
    private DrawView canvas;                                   //instance/object of DrawView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        handleUndoRedo();


        canvas = (DrawView) findViewById(R.id.drawing);      //instantiating the class


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        sizeBtn = (ImageView) findViewById(R.id.draw_btn);
        sizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popupSize = new PopupMenu(MainActivity.this, sizeBtn);

                try {
                    Field[] fields = popupSize.getClass().getDeclaredFields();
                    for (Field field : fields) {
                        if ("mPopup".equals(field.getName())) {
                            field.setAccessible(true);
                            Object menuPopupHelper = field.get(popupSize);
                            Class<?> classPopupHelper = Class.forName(menuPopupHelper
                                    .getClass().getName());
                            Method setForceIcons = classPopupHelper.getMethod(
                                    "setForceShowIcon", boolean.class);
                            setForceIcons.invoke(menuPopupHelper, true);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Inflating the Popup using xml file
                popupSize.getMenuInflater().inflate(R.menu.brushsize_popup, popupSize.getMenu());
                popupSize.show(); //showing popup menu

                //registering popup with OnMenuItemClickListener
                popupSize.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId()) {

                            case R.id.twenty:
                                canvas.drawPaint.setStrokeWidth(20);
                                return true;

                            case R.id.fourty:
                                canvas.drawPaint.setStrokeWidth(40);
                                return true;

                            case R.id.sixty:
                                canvas.drawPaint.setStrokeWidth(60);
                                return true;

                            case R.id.eighty:
                                canvas.drawPaint.setStrokeWidth(80);
                                return true;

                            case R.id.hundred:
                                canvas.drawPaint.setStrokeWidth(100);
                                return true;

                            default:
                                return true;
                        }
                    }
                });
            }
        }); //closing the setOnClickListener method

        colorBtn = (ImageView) findViewById(R.id.color_btn);
        colorBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popupColor = new PopupMenu(MainActivity.this, colorBtn);

                try {
                    Field[] fields = popupColor.getClass().getDeclaredFields();
                    for (Field field : fields) {
                        if ("mPopup".equals(field.getName())) {
                            field.setAccessible(true);
                            Object menuPopupHelper = field.get(popupColor);
                            Class<?> classPopupHelper = Class.forName(menuPopupHelper
                                    .getClass().getName());
                            Method setForceIcons = classPopupHelper.getMethod(
                                    "setForceShowIcon", boolean.class);
                            setForceIcons.invoke(menuPopupHelper, true);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Inflating the Popup using xml file
                popupColor.getMenuInflater().inflate(R.menu.color_popup, popupColor.getMenu());
                popupColor.show(); //showing popup menu

                //registering popup with OnMenuItemClickListener
                popupColor.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId()) {

                            case R.id.black:
                                canvas.drawPaint.setColor(Color.BLACK);
                                return true;

                            case R.id.blue:
                                canvas.drawPaint.setColor(Color.parseColor("#0066FF"));
                                return true;

                            case R.id.green:
                                canvas.drawPaint.setColor(Color.parseColor("#00832B"));
                                return true;

                            case R.id.orange:
                                canvas.drawPaint.setColor(Color.parseColor("#FFA70F"));
                                return true;

                            case R.id.purple:
                                canvas.drawPaint.setColor(Color.parseColor("#5C00A3"));
                                return true;

                            case R.id.red:
                                canvas.drawPaint.setColor(Color.parseColor("#E10000"));
                                return true;

                            case R.id.white:
                                canvas.drawPaint.setColor(Color.WHITE);
                                return true;

                            case R.id.yellow:
                                canvas.drawPaint.setColor(Color.parseColor("#FFF227"));
                                return true;

                            default:
                                return true;
                        }
                    }
                });
            }
        }); //closing the setOnClickListener method
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
}

    //get permission to save the file to the folder, permission is asked in the Manifest File and from the instantiated object
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length >= 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //andriod 6.0 usage
    private void checkWritingPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // permission wasn't granted
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
            }
        }
    }

    //creating file to save
    public void saveImage(View view) {
        File sdCard = Environment.getExternalStorageDirectory();
        File folder = new File(sdCard.getAbsolutePath() + "/Master Piece");

        boolean success = false;
        if (!folder.exists()) {
            success = folder.mkdirs();
            Log.i(TAG, " " + success);
        }

        File file = new File(folder, "drawing.png");        //creating folder for the file to save in

        if (!file.exists()) {
            try {
                success = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream ostream = null;
        try {
            ostream = new FileOutputStream(file);

            Bitmap well = canvas.getBitmap();
            Bitmap save = Bitmap.createBitmap(320, 480,
                    Bitmap.Config.ARGB_8888);
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            Canvas now = new Canvas(save);
            now.drawRect(new Rect(0, 0, 320, 480), paint);
            now.drawBitmap(well,
                    new Rect(0, 0, well.getWidth(), well.getHeight()),
                    new Rect(0, 0, 320, 480), null);

            if (save == null) {
            }
            save.compress(Bitmap.CompressFormat.PNG, 100, ostream);
            Toast.makeText(this, "File saved", Toast.LENGTH_SHORT).show();
        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(this, "Null error",
                    Toast.LENGTH_SHORT).show();
        }

        catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "File error", Toast.LENGTH_SHORT).show();
        }
    }



    //alert dialog for when user wants to delete all
    public void ClearScreen(View view){
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(this);
        deleteDialog.setTitle(getString(R.string.delete_drawing));
        deleteDialog.setMessage(getString(R.string.new_drawing_warning));
        deleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                canvas.eraseAll();
                dialog.dismiss();
            }
        });
        deleteDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        deleteDialog.show();
    }

    private void handleUndoRedo() {

        ImageButton undo = (ImageButton) findViewById(R.id.undo_btn);
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canvas.onClickUndo();
            }
        });

        ImageButton redo = (ImageButton) findViewById(R.id.redo_btn);
        redo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canvas.onClickRedo();
            }
        });

        }
    }