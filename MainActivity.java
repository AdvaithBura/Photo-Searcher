package edu.ncssm.bura24a.camera_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private int PHOTO_IDS = 20000;
    private ImageView contextItem;

    private final int Gallery_Req_Code = 1000; //to access gallery on phone
    private final int Camera_Req_Code = 100; // to access camera on phone

    public ArrayList<ImageView> allImg= new ArrayList<ImageView>();
    public ArrayList<String> tags= new ArrayList<String>();

    Button btnGallery;
    Button btnCamera;
    Button searchPhoto;

    GridLayout imageGrid;
    private Uri imageUri;
    private Uri galleryUri;
    private Bitmap thumbnail;
    Bitmap bitmap;


    /*

     * When the app is launched
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnGallery = findViewById(R.id.btnGallery);
        btnCamera = findViewById(R.id.btnCamera);
        imageGrid = findViewById(R.id.theimageGrid);
        searchPhoto=findViewById(R.id.searchphoto);
        imageGrid.setColumnCount(2);
        imageGrid.setRowCount(2);


        btnCamera.setOnClickListener(this); // setting on click listeners for the gallery and camera buttons
        btnGallery.setOnClickListener(this);
        searchPhoto.setOnClickListener(this);



    }

    /*

     *On the click of the buttons to access either gallery or camera
     */
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnCamera) {
            Intent iCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(iCamera, Camera_Req_Code);

        } else if (v == btnGallery) {
            Intent iGallery = new Intent(Intent.ACTION_PICK);
            iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(iGallery, Gallery_Req_Code);
        } else if (v== searchPhoto){
            Intent intent = new Intent(this,theSearch.class);
            Intent intent1 = new Intent(this, searchCategory.class);
            intent1.putExtra("theImg",allImg);
            startActivity(intent);
        }

    }
    /*
     * Drop down menu when image is clicked
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        Log.d("Context", "MEnu");
        contextItem = (ImageView) v;
        String[] menuItems = getResources().getStringArray(R.array.photo_categories);
        for(String s : menuItems){
            menu.add(s);
        }
    }
    /*
     *What to do when a menu item is selected
     */
    @Override
    public boolean onContextItemSelected(MenuItem item){
        contextItem.setPadding(50, 50, 50, 50);
        //AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        Log.d("What item", ""+item);
        return true;
    }



    //code for scrolling (Not used so far)
    public boolean onTouch(View arg0, MotionEvent event) {
        float mx=0;
        float my=0;

        float curX, curY;

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                mx = event.getX();
                my = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                curX = event.getX();
                curY = event.getY();
                imageGrid.scrollBy((int) (mx - curX), (int) (my - curY));
                mx = curX;
                my = curY;
                break;
            case MotionEvent.ACTION_UP:
                curX = event.getX();
                curY = event.getY();
                imageGrid.scrollBy((int) (mx - curX), (int) (my - curY));
                break;
        }

        return true;
    }




    /*

     *Method to add the selected gallery picture into a file and display the contents of the file (Not used)
     */
    private void updateImages() throws IOException {
        //creating file
        String[] files = this.fileList();
        for (String file : files) {
            FileInputStream fis = this.openFileInput(file);
            //byte[] data = fis.readAllBytes();
            byte[] data = new byte[(int) file.length()];
            DataInputStream dis = new DataInputStream(Files.newInputStream(Paths.get(file))); //reads in the data
            dis.readFully(data);
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length); //converting to bitmap to display
            dis.close();

            ImageView image = new ImageView(this);
            image.setImageBitmap(bitmap);
            //image.setImageResource(bitmap);
            image.setMinimumHeight(0);
            image.setMinimumWidth(0);

            imageGrid.addView(image);

        }

    }
    /*
     *Scaling the image when it comes on the phone's screen
     */

    public static Bitmap cropAndScale(Bitmap source, int scale) {
        int factor = Math.min(source.getHeight(), source.getWidth());
        int longer = Math.max(source.getHeight(), source.getWidth());
        int x = source.getHeight() >= source.getWidth() ? 0 : (longer - factor) / 2;
        int y = source.getHeight() <= source.getWidth() ? 0 : (longer - factor) / 2;
        source = Bitmap.createBitmap(source, x, y, factor, factor);
        source = Bitmap.createScaledBitmap(source, scale, scale, false);
        return source;
    }

    @SuppressLint("ClickableViewAccessibility")
    /*
     * Saving the image taken by the camera
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Camera_Req_Code && resultCode == RESULT_OK) {
            Log.d("Camtest","Cam ON");


            Bitmap camImage = (Bitmap) data.getExtras().get("data");

            //bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            bitmap = cropAndScale(camImage, 300); // if you mind scaling

            ImageView image = new ImageView(this);
            image.setImageBitmap(bitmap);
            image.setMinimumHeight(0);
            image.setMinimumWidth(0);
            image.setPadding(20, 20, 20, 20);
            image.setBackgroundColor(Color.BLACK);


            image.isClickable();
            image.isFocusable();

            image.setOnTouchListener((View.OnTouchListener) (v, e) -> {
                int doubleClickLastTime = 0;


                if( e.getAction() == MotionEvent.ACTION_MOVE){

                    image.setImageResource(0);





                }
                //Your code here
                return true;
            });


            allImg.add(image);
            imageGrid.addView(image);

            image.setId(PHOTO_IDS++);
            MainActivity.this.registerForContextMenu(image);

        }


        /*
        Saving the image that is selected in the Gallery
         */
        if (requestCode == Gallery_Req_Code && resultCode == RESULT_OK) {
            Log.d("Gal test", "IN");

            Uri selectedImage = data.getData();
            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(selectedImage);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);


            //bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), galleryUri);
            //bitmap = cropAndScale(bitmap, 300); // if you mind scaling
            bitmap = cropAndScale(yourSelectedImage, 300); // if you mind scaling


            ImageView image1 = new ImageView(this);
            image1.setImageBitmap(bitmap);
            image1.setMinimumHeight(0);
            image1.setMinimumWidth(0);
            image1.setPadding(20, 20, 20, 20);
            image1.setBackgroundColor(Color.BLACK);


            imageGrid.addView(image1);
            image1.setId(PHOTO_IDS++);
            MainActivity.this.registerForContextMenu(image1);

        }
    }
}
