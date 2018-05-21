package com.example.lavet.assignment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lavet.assignment.models.Matches;
import com.example.lavet.assignment.viewmodels.FirebaseViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.support.v4.content.ContextCompat.getColor;


/**
 * Provides UI for the view with List.
 */
public class MatchesFragment extends Fragment {

    private FirebaseViewModel viewModel;
    private ArrayList<Matches> mMatchList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewModel = new FirebaseViewModel();
        mMatchList = new ArrayList<>();

        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);

        viewModel.getMatches((response) -> {
            for(Matches match:response){
                mMatchList.add(match);
            }

            ContentAdapter adapter = new ContentAdapter(recyclerView.getContext(), mMatchList);
            recyclerView.setAdapter(adapter);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        });

        return recyclerView;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView picture;
        public TextView name;
        public TextView description;
        public ImageButton likeButton;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.activity_matches_fragment, parent, false));

            picture = itemView.findViewById(R.id.card_image);
            name = itemView.findViewById(R.id.card_title);
            description = itemView.findViewById(R.id.card_text);
            likeButton = itemView.findViewById(R.id.like_button);

            likeButton.setOnClickListener(this);

        }

        public void onClick(View view){
            Context context = getContext();
            Matches match = mMatchList.get(this.getAdapterPosition());
            CharSequence text="";

            if(match.liked){
                text = getString(R.string.unliked, name.getText());
                match.liked = false;
            } else if(!match.liked){
                text = getString(R.string.liked, name.getText());
                match.liked = true;
            }

            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            viewModel.updateMatches(match);
        }
    }

    public class ContentAdapter extends RecyclerView.Adapter<ViewHolder>{
        //Set numbers of List in RecyclerView.
        private static final int LENGTH=6;
        private ArrayList<String> mMatches = new ArrayList<>();
        private ArrayList<String> mMatchId = new ArrayList<>();
        private ArrayList<String> mMatchPhotoUrl = new ArrayList<>();
        private ArrayList<Boolean> mMatchLiked = new ArrayList<>();

        private ContentAdapter(Context context, ArrayList<Matches> mMatchList) {
            for(Matches matches:mMatchList) {
                mMatches.add(matches.name);
                mMatchId.add(matches.uid);
                mMatchPhotoUrl.add(matches.imageUrl);
                mMatchLiked.add(matches.liked);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position){
            Picasso.get().load(mMatchPhotoUrl.get(position)).into(holder.picture);
            holder.name.setText(mMatches.get(position));

            if(!mMatchLiked.get(position)){
                holder.likeButton.getDrawable().setTint(getColor(getContext(), R.color.button_grey));
            } else if (mMatchLiked.get(position)){
                holder.likeButton.getDrawable().setTint(getColor(getContext(), R.color.red));
            }
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }

    @Override
    public void onPause() {
        viewModel.clear();
        super.onPause();
    }
}