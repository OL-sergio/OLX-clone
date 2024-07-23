package exemple.udemy.java.olx.utilities;


import static androidx.appcompat.content.res.AppCompatResources.getDrawable;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import exemple.udemy.java.olx.R;


public class CustomHorizontalProgressDialog extends Dialog {

    private ProgressBar progressBar;

    public CustomHorizontalProgressDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_horizontal_progress_dialog);

        progressBar = findViewById(R.id.progress_bar);

        progressBar.setProgressDrawable(getDrawable(getContext(),R.drawable.custom_progress_bar));

        ObjectAnimator progressBarAnimation = ObjectAnimator.ofInt(progressBar, "progress",0, 100);
        progressBarAnimation.setDuration(3000);
        progressBarAnimation.setRepeatCount(199);
        progressBarAnimation.start();

    }

    public void setProgressDialog(int progress) {
        progressBar.setProgress(progress);
    }
}
