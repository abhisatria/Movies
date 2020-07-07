package id.ac.binus.movies;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    TextView tvTitle,tvYear,tvImdbID;
    ImageView imgMovie;
    Button buttonSave;
    MovieDbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        final String title = intent.getStringExtra("Title");
        final String year = intent.getStringExtra("Year");
        final String id = intent.getStringExtra("IMDB_ID");
        final String imageUrl = intent.getStringExtra("ImageUrl");
        db =new MovieDbHelper(this);

        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvTitle = findViewById(R.id.movie_title_detail);
        tvYear = findViewById(R.id.movie_year_detail);
        tvImdbID = findViewById(R.id.movie_imdb_detail);
        imgMovie = findViewById(R.id.movie_image_detail);
        buttonSave = findViewById(R.id.button_save_detail);
//        db.addData("Tes","Tes juga","Test","Tes");
//        Log.i("Test data",id);


        tvTitle.setText(title);
        tvYear.setText("("+year+")");
        tvImdbID.setText("IMDB ID : "+id);
        Picasso.get().load(imageUrl).into(imgMovie);


//        Log.i("Test", String.valueOf(db.getDatabaseName()));
        if(db.isMovieExist(id))
        {
            buttonSave.setText("Delete Movie");
        }
        else
        {
            buttonSave.setText("Save Movie");
        }

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(db.isMovieExist(id))
                {
                    AlertDialog.Builder alert = new AlertDialog.Builder(DetailActivity.this,R.style.AlertDialogTheme);
                    alert.setTitle("Delete Movie");
                    alert.setMessage("Are you sure want to delete this movie ?");
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Integer deleteData = db.deleteMovie(id);
                            if(deleteData > 0){
                                Toast.makeText(DetailActivity.this, title+" Deleted!", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            }
                        }
                    })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();

                }
                else
                {
                    AlertDialog.Builder alert = new AlertDialog.Builder(DetailActivity.this,R.style.AlertDialogTheme);
                    alert.setTitle("Save Movie");
                    alert.setMessage("Are you sure want to save this movie ?");
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            db.addData(id,title,year,imageUrl);
                            Toast.makeText(DetailActivity.this, title + " saved!", Toast.LENGTH_SHORT).show();
                            DetailActivity.this.finish();
                            onBackPressed();
                        }
                    })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();


                }
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}