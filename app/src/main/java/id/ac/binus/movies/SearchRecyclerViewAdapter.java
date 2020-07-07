package id.ac.binus.movies;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import id.ac.binus.movies.models.Movie;

public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.MyViewHolder> {

    private static Context mContext;
    private SavedRecyclerViewAdapter savedAdapter;
    private static List<Movie> movieList;
    private OnItemClickListener onItemClickListener;

    public SearchRecyclerViewAdapter(Context mContext, List<Movie> movieList) {
        this.mContext = mContext;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cardview_item,parent,false);
        return new MyViewHolder(view,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.tvTitle.setText(movieList.get(position).getTitle());
        Picasso.get().load(movieList.get(position).getUrlToImage()).into(holder.imgMovie);
    }



    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener , PopupMenu.OnMenuItemClickListener{
        TextView tvTitle;
        ImageView imgMovie;
        CardView cardView;
        OnItemClickListener onItemClickListener;
        public MyViewHolder(@NonNull final View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.movie_title);
            imgMovie = itemView.findViewById(R.id.movie_image);
            cardView = itemView.findViewById(R.id.cardViewID);
            itemView.setOnClickListener(this);

            this.onItemClickListener = onItemClickListener;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(),DetailActivity.class);
                    intent.putExtra("Title",movieList.get(getLayoutPosition()).getTitle());
                    intent.putExtra("Year",movieList.get(getLayoutPosition()).getYear());
                    intent.putExtra("IMDB_ID",movieList.get(getLayoutPosition()).getImdb_id());
                    intent.putExtra("ImageUrl",movieList.get(getLayoutPosition()).getUrlToImage());
                    itemView.getContext().startActivity(intent);

                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showPopup(v,movieList.get(getLayoutPosition()).getImdb_id());
                    return true;
                }
            });
        }

        public void showPopup(View v,String id)
        {
            PopupMenu popupMenu = new PopupMenu(itemView.getContext(),v);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.inflate(R.menu.menu);

            Menu menuOpts = popupMenu.getMenu();
            MovieDbHelper db = new MovieDbHelper(itemView.getContext());
            if(db.isMovieExist(id))
            {
                menuOpts.getItem(0).setTitle("Delete");
            }
            else
            {
                menuOpts.getItem(0).setTitle("Save");
            }
            popupMenu.show();
        }
        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v,getAdapterPosition());
        }

        @Override
        public boolean onMenuItemClick(final MenuItem item) {
            switch (item.getItemId()){
                case R.id.item1:
                    final MovieDbHelper db = new MovieDbHelper(itemView.getContext());
                    if(db.isMovieExist(movieList.get(getLayoutPosition()).getImdb_id()))
                    {
                        //delete
                        AlertDialog.Builder alert = new AlertDialog.Builder(itemView.getContext(),R.style.AlertDialogTheme);
                        alert.setTitle("Delete Movie");
                        alert.setMessage("Are you sure want to delete this movie ?");
                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Integer deleteData = db.deleteMovie(movieList.get(getLayoutPosition()).getImdb_id());
                                if(deleteData > 0){
                                    Toast.makeText(itemView.getContext(), movieList.get(getLayoutPosition()).getTitle()+" Deleted!", Toast.LENGTH_SHORT).show();
//                                    Intent refresh = new Intent(itemView.getContext(),MainActivity.class);

                                    SavedTab.adapter = new SavedRecyclerViewAdapter(SavedRecyclerViewAdapter.mContext,db.getAllMovie());
                                    SavedTab.recyclerView.setLayoutManager(new GridLayoutManager(SavedRecyclerViewAdapter.mContext,2));
                                    SavedTab.recyclerView.setAdapter(SavedTab.adapter);
                                    SavedTab.adapter.notifyDataSetChanged();
//                                    savedAdapter.notifyDataSetChanged();
//                                    notifyDataSetChanged();

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
                        //save
                        AlertDialog.Builder alert = new AlertDialog.Builder(itemView.getContext(),R.style.AlertDialogTheme);
                        alert.setTitle("Save Movie");
                        alert.setMessage("Are you sure want to save this movie ?");
                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.addData(movieList.get(getLayoutPosition()).getImdb_id(),movieList.get(getLayoutPosition()).getTitle(),movieList.get(getLayoutPosition()).getYear(),movieList.get(getLayoutPosition()).getUrlToImage());
                                Toast.makeText(itemView.getContext(), movieList.get(getLayoutPosition()).getTitle()+" Saved!", Toast.LENGTH_SHORT).show();

                                SavedTab.adapter = new SavedRecyclerViewAdapter(SavedRecyclerViewAdapter.mContext,db.getAllMovie());
                                SavedTab.recyclerView.setLayoutManager(new GridLayoutManager(SavedRecyclerViewAdapter.mContext,2));
                                SavedTab.recyclerView.setAdapter(SavedTab.adapter);
                                SavedTab.adapter.notifyDataSetChanged();
                            }
                        })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();

                    }
                    break;
            }
            return true;
        }
    }
}
