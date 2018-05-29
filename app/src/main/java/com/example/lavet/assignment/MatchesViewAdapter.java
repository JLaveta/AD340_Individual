package com.example.lavet.assignment;

import android.content.Context;
import android.location.Location;
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
    private View view;
    public double startLat, startLong;

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

            //Calculates distance to match and sets text
            float [] distanceToMatch = new float[1];
            Location.distanceBetween(startLat, startLong,
                    Double.parseDouble(holder.mMatch.lat), Double.parseDouble(holder.mMatch.longitude),
                    distanceToMatch);
            holder.matchDist.setText(
                    holder.matchDist.getContext()
                            .getString(R.string.distanceText,
                                    String.format("%.01f", distanceToMatch[0]/1609.34)));

            //Checks "liked" state in database and sets heart icon appropriately
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
            public TextView matchDist;

            public ViewHolder(View view) {
                super(view);
                picture = view.findViewById(R.id.card_image);
                name = view.findViewById(R.id.card_title);
                matchDist = view.findViewById(R.id.matchDistance);

                //Set onclick listener for heart icon
                likeButton = itemView.findViewById(R.id.like_button);
                likeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != mListener) {
                            Context context = v.getContext();
                            CharSequence toastText;

                            //Change state of heart icon and update database if clicked
                            if (mMatch.liked) {
                                likeButton.getDrawable().setTint(getColor(context, R.color.button_grey));
                                toastText = context.getString(R.string.unliked, name.getText());
                                mMatch.liked = false;
                            } else {
                                likeButton.getDrawable().setTint(getColor(context, R.color.red));
                                toastText = context.getString(R.string.liked, name.getText());
                                mMatch.liked = true;
                            }

                            //Toast message indicating like/unlike of specific match
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

    //Sets a new latitude and longitude of user's location and requests an
    //update of all matches.
    public void setLatLong(Double latitude, Double longitude){
        startLat = latitude;
        startLong = longitude;
        notifyDataSetChanged();
    }
}

