package com.example.android.bakeryrecipes.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bakeryrecipes.R;
import com.example.android.bakeryrecipes.data.Recipe;
import com.example.android.bakeryrecipes.data.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;

import static com.example.android.bakeryrecipes.ui.MainActivity.SELECTED_RECIPES;
import static com.example.android.bakeryrecipes.ui.MainActivity.SELECTED_STEPS;
import static com.example.android.bakeryrecipes.ui.MainActivity.SELECTED_INDEX;
import static com.example.android.bakeryrecipes.ui.MainActivity.EXTRA_PLAYER_POSITION;
import static com.example.android.bakeryrecipes.ui.MainActivity.TITLE;


public class FragmentDetails extends Fragment {

    private SimpleExoPlayer exoPlayer;
    private SimpleExoPlayerView exoPlayerView;
    private BandwidthMeter bandwidthMeter;
    private Handler handler;

    private ArrayList<Step> steps = new ArrayList<>();
    ArrayList<Recipe> recipe;
    String recipeName;
    private int selectedIndex;
    private long positionInMillis;

    public FragmentDetails() {
    }

    public interface ListItemListener {
        void onListItemClick(List<Step> allSteps, int index, String recipeName);
    }

    private ListItemListener listItemListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView textView;
        handler = new Handler();
        bandwidthMeter = new DefaultBandwidthMeter();

        listItemListener = (StepsActivity) getActivity();

        recipe = new ArrayList<>();

        if (savedInstanceState != null) {
            steps = savedInstanceState.getParcelableArrayList(SELECTED_STEPS);
            selectedIndex = savedInstanceState.getInt(SELECTED_INDEX);
            recipeName = savedInstanceState.getString(TITLE);
            positionInMillis = savedInstanceState.getLong(EXTRA_PLAYER_POSITION);
        } else {
            steps = getArguments().getParcelableArrayList(SELECTED_STEPS);
            if (steps != null) {
                steps = getArguments().getParcelableArrayList(SELECTED_STEPS);
                selectedIndex = getArguments().getInt(SELECTED_INDEX);
                recipeName = getArguments().getString(TITLE);
            } else {
                recipe = getArguments().getParcelableArrayList(SELECTED_RECIPES);
                steps = (ArrayList<Step>) recipe.get(0).getSteps();
                selectedIndex = 0;
            }
        }

        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        textView = rootView.findViewById(R.id.step_description);
        textView.setText(steps.get(selectedIndex).getDescription());
        textView.setVisibility(View.VISIBLE);

        exoPlayerView = rootView.findViewById(R.id.player_view);
        exoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);

        if (rootView.findViewWithTag("sw600dp-land") != null) {
            recipeName = ((StepsActivity) getActivity()).recipeName;
            ((StepsActivity) getActivity()).getSupportActionBar().setTitle(recipeName);
        }

        String imageUrl = steps.get(selectedIndex).getThumbnailUrl();

        if (!TextUtils.isEmpty(imageUrl)) {
            Uri builtUri = Uri.parse(imageUrl).buildUpon().build();
            ImageView thumbImage = rootView.findViewById(R.id.thumbnail);
            Picasso.with(getContext()).load(builtUri).into(thumbImage);
        }

        String videoUrl = steps.get(selectedIndex).getvideoURL();

        if (!TextUtils.isEmpty(videoUrl)) {
            initializePlayer(Uri.parse(steps.get(selectedIndex).getvideoURL()));
            if (rootView.findViewWithTag("sw600dp-land") != null) {
                getActivity().findViewById(R.id.fragment_container_2).setLayoutParams(new LinearLayout
                        .LayoutParams(-1, -2));
                exoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
            } else if (isInLandscapeMode(getContext())) {
                textView.setVisibility(View.GONE);
            }
        } else {
            exoPlayer = null;
            exoPlayerView.setForeground(ContextCompat.getDrawable(getContext(), R.drawable.mixer));
            exoPlayerView.setLayoutParams(new LinearLayout.LayoutParams(300, 300));
        }


        Button previousStep = rootView.findViewById(R.id.previousStep);
        Button nextStep = rootView.findViewById(R.id.nextStep);

        previousStep.setOnClickListener(view -> {
            if (steps.get(selectedIndex).getId() > 0) {
                if (exoPlayer != null) {
                    exoPlayer.stop();
                }
                listItemListener.onListItemClick(steps, steps.get(selectedIndex).getId() - 1, recipeName);
            } else {
                Toast.makeText(getActivity(), R.string.step_first_toast, Toast.LENGTH_SHORT).show();
            }
        });

        nextStep.setOnClickListener(view -> {
            int lastIndex = steps.size() - 1;
            if (steps.get(selectedIndex).getId() < steps.get(lastIndex).getId()) {
                if (exoPlayer != null) {
                    exoPlayer.stop();
                }
                listItemListener.onListItemClick(steps, steps.get(selectedIndex).getId() + 1, recipeName);
            } else {
                Toast.makeText(getContext(), R.string.step_last_toast, Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    private void initializePlayer(Uri mediaUri) {
        if (exoPlayer == null) {
            TrackSelection.Factory videoTrackSelection = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(handler, videoTrackSelection);
            LoadControl loadControl = new DefaultLoadControl();

            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            exoPlayerView.setPlayer(exoPlayer);

            String userAgent = Util.getUserAgent(getContext(), "Bakery Recipes");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);
            exoPlayer.seekTo(positionInMillis);
        }
    }

    public boolean isInLandscapeMode(Context context) {
        return (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList(SELECTED_STEPS, steps);
        savedInstanceState.putInt(SELECTED_INDEX, selectedIndex);
        savedInstanceState.putString("Title", recipeName);
        if (exoPlayer != null)
            savedInstanceState.putLong(EXTRA_PLAYER_POSITION, exoPlayer.getCurrentPosition());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            selectedIndex = savedInstanceState.getInt(SELECTED_INDEX);
            positionInMillis = savedInstanceState.getLong(EXTRA_PLAYER_POSITION, 0);
        } else {
            selectedIndex = getArguments().getInt(SELECTED_INDEX);
            positionInMillis = 0;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.release();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (exoPlayer != null) {
            positionInMillis = exoPlayer.getCurrentPosition();
            exoPlayer.setPlayWhenReady(false);
            exoPlayer.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (exoPlayer != null) {
            exoPlayer.seekTo(positionInMillis);
        }
    }
}
