package com.indoori.aditya.gifpeg;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Activities that contain this fragment must implement the
 * {@link E_GifFragmentViewPager.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link E_GifFragmentViewPager#newInstance} factory method to
 * create an instance of this fragment.
 */
public class E_GifFragmentViewPager extends Fragment implements GifRecyclerViewAdapter.ListItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    String mParam1;
    String mParam2;
    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;
    private OnFragmentInteractionListener mListener;
    int gifArray[] = {
            R.drawable.agif,
            R.drawable.bgif,
            R.drawable.cgif,
            R.drawable.dgif,
            R.drawable.egif,
            R.drawable.fgif};
    String subtitleArray[] = {
            "impressive!",
            "Such Swag! Thug Life!",
            "Like A BOSS!!",
            "Great Jobb!",
            "O-M-G!! WOWOWW!!",
            "Greaat HOLY!!"};
    private View rootView;
    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;

    public E_GifFragmentViewPager() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment E_GifFragmentViewPager.
     */
    // TODO: Rename and change types and number of parameters
    public static E_GifFragmentViewPager newInstance(String param1, String param2) {
        E_GifFragmentViewPager fragment = new E_GifFragmentViewPager();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_gif_fragment_view_pager, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_gif);
        floatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2, LinearLayoutManager.VERTICAL,false);//2 = number of columns
        recyclerView.setLayoutManager(gridLayoutManager);
        if (mParam2.equals("Trending")) {
            GifRecyclerViewAdapter gifRecyclerViewAdapter = new GifRecyclerViewAdapter(getContext(), gifArray, subtitleArray, this);
            recyclerView.setAdapter(gifRecyclerViewAdapter);
        }
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(int uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onListItemClick(int clickedItemIndex, int viewCode, ImageView gifView) {
        //shareDrawable(getContext(),gifArray[clickedItemIndex],"myGifFileShare");
        zoomImageFromThumb(gifView,gifArray[clickedItemIndex]);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        Fragment onFragmentInteraction(int pageNumber);
    }

    private void zoomImageFromThumb(final ImageView thumbView, final int imageResId) {
        // If there's an animation in progress, cancel it immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }
        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) rootView.findViewById(R.id.expanded_gif);
        Glide.with(getContext()).load(imageResId).into(expandedImageView);
        // Calculate the starting and ending bounds for the zoomed-in image. This step
        // involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail, and the
        // final bounds are the global visible rectangle of the container view. Also
        // set the container view's offset as the origin for the bounds, since that's
        // the origin for the positioning animation properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        rootView.findViewById(R.id.gif_container).getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x+16, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final bounds using the
        // "center crop" technique. This prevents undesirable stretching during the animation.
        // Also calculate the start scaling factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation begins,
        // it will position the zoomed-in view in the place of the thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);
        recyclerView.setAlpha(0.1f);
        rootView.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.black));
        // Set the pivot point for SCALE_X and SCALE_Y transformations to the top-left corner of
        // the zoomed-in view (the default is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and scale properties
        // (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left,
                        finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top,
                        finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        //Floating Action Button:
        floatingActionButton.setVisibility(View.VISIBLE);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //shareDrawable(getContext(),imageResId,"myJpegFileShare");
                shareDrawable(imageResId);
            }
        });

        // Upon clicking the zoomed-in image, it should zoom back down to the original bounds
        // and show the thumbnail instead of the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel, back to their
                // original values.
                floatingActionButton.setVisibility(View.GONE);
                recyclerView.setAlpha(1f);
                rootView.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorBackground));

                AnimatorSet set = new AnimatorSet();
                set
                        .play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView, View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView, View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }

    private void shareDrawable(int resourceId) {
        try {
            Log.v("ShareGif", "Inside TRY");
            //create an temp file in app cache folder
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/LatestGifShare.gif";
            File outputFile = new File(path);
            FileOutputStream outPutStream = new FileOutputStream(outputFile);
            Log.v("ShareGif", "Created outputFile");

            //Saving the resource GIF into the outputFile:
            InputStream is = getResources().openRawResource(resourceId);
            BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int current = 0;
            while ((current = bis.read()) != -1) {
                baos.write(current);
            }
            try {
                FileOutputStream fos = new FileOutputStream(outputFile);
                fos.write(baos.toByteArray());
                outPutStream.flush();
                outPutStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //
            Log.v("ShareGif", "Wrote GIF to File");

            //share file

            path = outputFile.getPath();
            Uri bmpUri = Uri.parse("file://" + path);
            Log.v("ShareGif", "Calling Intent");
            Intent shareIntent = new Intent();
            shareIntent = new Intent(android.content.Intent.ACTION_SEND);
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            shareIntent.setType("image/gif");
            startActivity(Intent.createChooser(shareIntent, "Share with"));
            Log.v("ShareGif", "Called Intent");
        } catch (Exception e) {
            Log.e("Intent Exception", "" + e);
        }
    }
}
