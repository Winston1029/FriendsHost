package com.moupress.app.friendshost.uicomponent;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.SpannableString;
import android.text.style.LeadingMarginSpan.LeadingMarginSpan2;
import android.view.Display;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FlowTextHelper {
	private static boolean mNewClassAvailable;

    /* class initialization fails when this throws an exception */
    static {
        try {
            Class.forName("android.text.style.LeadingMarginSpan$LeadingMarginSpan2");
            mNewClassAvailable = true;
        } catch (Exception ex) {
            mNewClassAvailable = false;
        }
    }

    public static void tryFlowText(String text, View thumbnailView, TextView messageView, Display display){
        // There is nothing I can do for older versions, so just return
        if(!mNewClassAvailable) return;

        // Get height and width of the image and height of the text line
         thumbnailView.measure(display.getWidth(), display.getHeight());
         int height = thumbnailView.getMeasuredHeight();
         int width = thumbnailView.getMeasuredWidth();
         float textLineHeight = messageView.getPaint().getTextSize();

         // Set the span according to the number of lines and width of the image
         int lines = (int)Math.round(height / textLineHeight);
         SpannableString ss = new SpannableString(text);
         //For an html text you can use this line: SpannableStringBuilder ss = (SpannableStringBuilder)Html.fromHtml(text);
         ss.setSpan(new MyLeadingMarginSpan2(lines, width), 0, ss.length(), 0);
         messageView.setText(ss);

         // Align the text with the image by removing the rule that the text is to the right of the image
         RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)messageView.getLayoutParams();
         int[]rules = params.getRules();
         rules[RelativeLayout.RIGHT_OF] = 0;
    }
    
}
