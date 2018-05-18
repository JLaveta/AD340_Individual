package com.example.lavet.assignment;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lavet.assignment.viewmodels.FirebaseViewModel;

/**
 * Provides UI for the view with List.
 */
public class MatchesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);
        ContentAdapter adapter = new ContentAdapter(recyclerView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return recyclerView;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView picture;
        public TextView name;
        public TextView description;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.activity_matches_fragment, parent, false));

            picture = itemView.findViewById(R.id.card_image);
            name = itemView.findViewById(R.id.card_title);
            description = itemView.findViewById(R.id.card_text);
            ImageButton likeButton = itemView.findViewById(R.id.like_button);

            likeButton.setOnClickListener(this);

        }

        public void onClick(View view){
            Context context = getContext();
            CharSequence text = getString(R.string.liked, name.getText());
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    public class ContentAdapter extends RecyclerView.Adapter<ViewHolder>{
        //Set numbers of List in RecyclerView.
        private static final int LENGTH = 4;
        private final String[] mMatches;
        private final String[] mMatchDesc;
        private final Drawable[] mMatchPhoto;


        public ContentAdapter(Context context) {

            Resources resources = context.getResources();
            mMatches = resources.getStringArray(R.array.matches);
            mMatchDesc = resources.getStringArray(R.array.descriptions);
            TypedArray a = resources.obtainTypedArray(R.array.match_photo);
            mMatchPhoto = new Drawable[a.length()];

            for (int i = 0; i < mMatchPhoto.length; i++) {
                mMatchPhoto[i] = a.getDrawable(i);
            }

            a.recycle();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position){
            holder.picture.setImageDrawable(mMatchPhoto[position % mMatchPhoto.length]);
            holder.name.setText(mMatches[position % mMatches.length]);
            holder.description.setText(mMatchDesc[position % mMatchDesc.length]);
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }
}