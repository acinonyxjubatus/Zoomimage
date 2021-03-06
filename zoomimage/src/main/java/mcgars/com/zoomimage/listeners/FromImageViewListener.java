package mcgars.com.zoomimage.listeners;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.alexvasilkov.gestures.animation.ViewPositionAnimator;
import com.alexvasilkov.gestures.transition.ViewsCoordinator;
import com.alexvasilkov.gestures.transition.ViewsTracker;
import com.alexvasilkov.gestures.transition.ViewsTransitionAnimator;

public class FromImageViewListener<ID> implements ViewsCoordinator.OnRequestViewListener<ID> {


    private final ImageView imageView;
    private final ViewsTracker<ID> mTracker;
    private final ViewsTransitionAnimator<ID> mAnimator;

    private ID mId;

    public FromImageViewListener(@NonNull ImageView imageView,
                                 @NonNull ViewsTracker<ID> tracker,
                                 @NonNull ViewsTransitionAnimator<ID> animator) {
        this.imageView = imageView;
        mTracker = tracker;
        mAnimator = animator;
        mAnimator.addPositionUpdateListener(new UpdateListener());
    }

    @Override
    public void onRequestView(@NonNull ID id) {
        // Trying to find requested view on screen. If it is not currently on screen
        // or it is not fully visible than we should scroll to it at first.
        mId = id;
        int position = mTracker.getPositionForId(id);

        if (position == ViewsTracker.NO_POSITION) {
            return; // Nothing we can do
        }

        mAnimator.setFromView(mId, imageView);
    }

    private class UpdateListener implements ViewPositionAnimator.PositionUpdateListener {
        @Override
        public void onPositionUpdate(float state, boolean isLeaving) {
            if (state == 0f && isLeaving) {
                mId = null;
            }
            imageView.setVisibility(state == 0f && isLeaving ? View.VISIBLE : View.INVISIBLE);
        }
    }

}
