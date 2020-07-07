package id.ac.binus.movies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import id.ac.binus.movies.models.Movie;

public class MovieDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "moviesdata.db";
    public static final int DATABASE_VERSION = 1;
    public static final String MOVIE_TABLE = "movie";
    public static final String IMDB_ID = "id";
    public static final String MOVIE_TITLE = "title";
    public static final String YEAR = "year";
    public static final String IMAGE_FILE = "image";

    private static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + MOVIE_TABLE + ";";


    public MovieDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+MOVIE_TABLE +"("+IMDB_ID+" TEXT PRIMARY KEY , "
                + MOVIE_TITLE+" TEXT NOT NULL,"
                + YEAR + " TEXT NOT NULL,"
                + IMAGE_FILE + " TEXT NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_TABLE);
        onCreate(db);
    }

    public void addData(String imdbId,String title, String year,String image){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(IMDB_ID, imdbId);
        cv.put(MOVIE_TITLE,title);
        cv.put(YEAR, year);
        cv.put(IMAGE_FILE,image);
        db.insert(MOVIE_TABLE, null, cv);
    }

    public boolean isMovieExist(String id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+ MOVIE_TABLE+" WHERE "+ IMDB_ID+" = ?",new String[]{id});
        if (c.getCount()>0 )
        {
            return true; //movie already exists
        }
        else return false;
    }

    public Movie getMovie(String id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+ MOVIE_TABLE+" WHERE "+ IMDB_ID+" = ?",new String[]{id});
        Movie movie = new Movie();
        if(c.moveToFirst()){
            c.moveToFirst();
            movie.setImdb_id(c.getString(0));
            movie.setTitle(c.getString(1));
            movie.setYear(c.getString(2));
            movie.setUrlToImage(c.getString(3));

            c.close();
        }
        else
        {
            return movie=null;
        }
        return movie;
    }

    public List<Movie> getAllMovie(){
        List<Movie> movies = new ArrayList<>();
        String query = "SELECT * FROM " + MOVIE_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query,null);

        if(c.moveToFirst())
        {
            do{
                Movie movie = new Movie();
                movie.setImdb_id(c.getString(0));
                movie.setTitle(c.getString(1));
                movie.setYear(c.getString(2));
                movie.setUrlToImage(c.getString(3));
                movies.add(movie);
            }while(c.moveToNext());
        }
        db.close();
        return movies;
    }

    public int deleteMovie(String ID){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(MOVIE_TABLE,IMDB_ID + " = ?",new String[]{(ID)});
    }
}
