package com.careerlauncher.gkninja.utils.hankbase;

import android.animation.Animator;
import android.os.Build;

import androidx.annotation.RequiresApi;

/**
 *
 */

@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class DefaultAnimatorListener implements Animator.AnimatorListener {
    @Override
    public void onAnimationStart(Animator animation) {
        // no-op
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        // no-op
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        // no-op
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
        // no-op
    }
}
