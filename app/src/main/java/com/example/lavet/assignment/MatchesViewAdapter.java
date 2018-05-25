package com.example.lavet.assignment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lavet.assignment.models.Matches;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.content.ContextCompat.getColor;
/**
 * {@link RecyclerView.Adapter} that can display a {@link Matches} and makes a call to the
 * specified {@link MatchesFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MatchesViewAdapter extends RecyclerView.Adapter<MatchesViewAdapter.ViewHolder>{

    private List<Matches> mMatchesList;
    private final MatchesFragment.OnListFragmentInteractionListener mListener;
    //Set numbers of List in RecyclerView.
    private View view;

    public MatchesViewAdapter(List<Matches> matches, MatchesFragment.OnListFragmentInteractionListener listener) {
            mMatchesList = matches;
            mListener = listener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_matches_fragment, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            holder.mMatch = mMatchesList.get(position);
            Picasso.get().load(holder.mMatch.imageUrl).into(holder.picture);
            holder.name.setText(holder.mMatch.name);

            if (!holder.mMatch.liked) {
                holder.likeButton.getDrawable().setTint(getColor(view.getContext(), R.color.button_grey));
            } else if (holder.mMatch.liked) {
                holder.likeButton.getDrawable().setTint(getColor(view.getContext(), R.color.red));
            }
        }

        @Override
        public int getItemCount() {
            if(mMatchesList != null)
                return mMatchesList.size();

            return 0;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView picture;
            public TextView name;
            public ImageButton likeButton;
            public Matches mMatch;
            public final View mView;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                picture = view.findViewById(R.id.card_image);
                name = view.findViewById(R.id.card_title);

                likeButton = itemView.findViewById(R.id.like_button);
                likeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != mListener) {
                            Context context = v.getContext();
                            CharSequence toastText;

                            if (mMatch.liked) {
                                likeButton.getDrawable().setTint(getColor(context, R.color.button_grey));
                                toastText = context.getString(R.string.unliked, name.getText());
                                mMatch.liked = false;
                            } else {
                                likeButton.getDrawable().setTint(getColor(context, R.color.red));
                                toastText = context.getString(R.string.liked, name.getText());
                                mMatch.liked = true;
                            }

                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, toastText, duration);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();

                            mListener.onListFragmentInteraction(mMatch);
                        }
                    }
                });
            }
        }

    public void updateMatchesList(List<Matches> matchesList) {
        if(mMatchesList == null){
            mMatchesList = new ArrayList<>();
        }
        final MatchesDiffCallback diffCallback = new MatchesDiffCallback(mMatchesList, matchesList);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);


        mMatchesList.clear();
        mMatchesList.addAll(matchesList);
        diffResult.dispatchUpdatesTo(this);
    }
}

