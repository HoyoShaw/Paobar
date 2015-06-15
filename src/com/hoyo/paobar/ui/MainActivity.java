package com.hoyo.paobar.ui;


import android.graphics.Canvas;
import android.os.Bundle;

import com.hoyo.paobar.R;
import com.hoyo.paobar.ui.fragment.CardFragment;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.SlidingMenu.CanvasTransformer;

public class MainActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// set the Above View
		setContentView(R.layout.content_frame);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, new CardFragment())
		.commit();


		SlidingMenu sm = getSlidingMenu();
		setSlidingActionBarEnabled(true);
		sm.setBehindScrollScale(0.0f);
        sm.setBehindCanvasTransformer(new CanvasTransformer() {
            @Override
            public void transformCanvas(Canvas canvas, float percentOpen) {
                float scale = (float) (-percentOpen * 0.5 + 1.5);
                canvas.saveLayerAlpha(0, 0, canvas.getWidth(), canvas.getHeight(), (int) (percentOpen * 255),Canvas.HAS_ALPHA_LAYER_SAVE_FLAG);
                canvas.scale(scale, scale, canvas.getWidth() / 2, canvas.getHeight() / 2);
            }
        });

        sm.setAboveCanvsTransformer(new CanvasTransformer() {

            @Override
            public void transformCanvas(Canvas canvas, float percentOpen) {
                float scale = (float) (-percentOpen * 0.2 + 1);
                canvas.scale(1, scale, canvas.getWidth() / 2, canvas.getHeight() / 2);
            }
        });
	}

}
