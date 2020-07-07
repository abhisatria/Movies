package id.ac.binus.movies;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import id.ac.binus.movies.models.Movie;


public class SavedTab extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    public static SavedRecyclerViewAdapter adapter;
    List<Movie> movies = new ArrayList<>();
    public static RecyclerView recyclerView;
    TextView textViewIsEmpty;
    private SwipeRefreshLayout swipeRefreshLayout;


    public SavedTab() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        MovieDbHelper db = new MovieDbHelper(getActivity());
        updateData();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_saved, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerViewSaved);
        textViewIsEmpty = rootView.findViewById(R.id.tvEmpty_saved);
        swipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh_layout_saved);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);

        updateData();
        return rootView;
    }

    @Override
    public void onRefresh() {
        updateData();
    }
    private void updateData()
    {
        swipeRefreshLayout.setRefreshing(false);
        MovieDbHelper db = new MovieDbHelper(getActivity());

        movies.clear();
        SQLiteDatabase obj =db.getReadableDatabase();
        if(obj!=null)
        {
            movies.addAll(db.getAllMovie());
        }
        if(!movies.isEmpty())
        {
            textViewIsEmpty.setVisibility(View.GONE);
        }
        adapter = new SavedRecyclerViewAdapter(getActivity(),movies);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}