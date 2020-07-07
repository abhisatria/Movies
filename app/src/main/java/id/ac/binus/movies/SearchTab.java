package id.ac.binus.movies;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import id.ac.binus.movies.api.ApiClient;
import id.ac.binus.movies.api.ApiInterface;
import id.ac.binus.movies.models.Movie;
import id.ac.binus.movies.models.Search;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static id.ac.binus.movies.MainActivity.API_KEY;


public class SearchTab extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    SearchRecyclerViewAdapter adapter;
    List<Movie> movies = new ArrayList<>();
    RecyclerView myRv;
    EditText etSearch;
    private RelativeLayout errorLayout;
    private ImageView errorImage;
    private TextView errorTitle,errorMessage;
    private Button btnRetry;
    private SwipeRefreshLayout swipeRefreshLayout;

    public SearchTab() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_search_tab, container, false);

        errorLayout = rootView.findViewById(R.id.errorLayout);
        errorImage = rootView.findViewById(R.id.connection_failed_logo);
        errorMessage = rootView.findViewById(R.id.errorMessage);
        errorTitle = rootView.findViewById(R.id.errorTitle);
        btnRetry = rootView.findViewById(R.id.btnRetry);
        swipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        onLoadingSwipeRefresh("");

        myRv = rootView.findViewById(R.id.recyclerView);
        myRv.setLayoutManager(new GridLayoutManager(getActivity(),2));
        etSearch = rootView.findViewById(R.id.etSearch);
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_SEARCH)
                {
                    onLoadingSwipeRefresh(v.getText().toString());
                    return true;
                }
                return false;
            }
        });
        return rootView;
    }


    public void LoadJson(final String title){
        errorLayout.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(true);
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Search> call;

        if(title.length()>0)
        {
            call = apiInterface.getMovies(title,API_KEY);
        }
        else
        {
            call = apiInterface.getMovies("dark",API_KEY);
        }


        call.enqueue(new Callback<Search>() {
            @Override
            public void onResponse(Call<Search> call, Response<Search> response) {
                if(response.isSuccessful() && response.body().getMovies() != null)
                {
                    if(movies.isEmpty()){
                        movies.clear();
                    }
                    movies = response.body().getMovies();
                    adapter = new SearchRecyclerViewAdapter(getActivity(),movies);
                    myRv.setAdapter(adapter);
                    myRv.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();

                    swipeRefreshLayout.setRefreshing(false);
                }
                else
                {

                    swipeRefreshLayout.setRefreshing(false);

                    String errorCode;
                    switch (response.code()){
                        case 404 :
                            errorCode = "404 not found";
                            break;
                        case 500 :
                            errorCode = "500 server broken";
                            break;
                        default:
                            errorCode = "unknown error";
                            break;
                    }
                    Toast.makeText(getActivity(),"Failed " +errorCode,Toast.LENGTH_SHORT).show();
                    showErrorMessage(R.drawable.connection_failed,"No Result", "Please Try Again!\n"+ errorCode);
                    myRv.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<Search> call, Throwable t) {
                Toast.makeText(getActivity(),"Failed "+t.getMessage(),Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
//                topHeadline.setVisibility(View.INVISIBLE);
                showErrorMessage(R.drawable.connection_failed,"Oops...", "Network failure, Please Try Again\n"+ t.toString());
                myRv.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onRefresh() {
        LoadJson(etSearch.getText().toString());
    }

    private void onLoadingSwipeRefresh(final String keyword){
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                LoadJson(keyword);
            }
        });
    }

    private void showErrorMessage(int imageView,String title,String message)
    {
        if(errorLayout.getVisibility() == View.GONE)
        {
            errorLayout.setVisibility(View.VISIBLE);
        }
        errorImage.setImageResource(imageView);
        errorTitle.setText(title);
        errorMessage.setText(message);

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoadingSwipeRefresh("");
            }
        });
    }

}