package tech.iwish.taxi.extended;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import tech.iwish.taxi.R;


public class ButtonFont extends Button {
    public ButtonFont(Context context) {
        super(context);
        changefont(null);
    }

    public ButtonFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        changefont(attrs);
    }

    public ButtonFont(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        changefont(attrs);
    }

//    public ButtonFont(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }
public void changefont(AttributeSet attributeSet){
    Typeface typeface =Typeface.createFromAsset(getContext().getAssets(),"OpenSans-Bold.ttf");
    if(attributeSet != null){
        TypedArray aa = getContext().obtainStyledAttributes(attributeSet, R.styleable.ProximaFonts);
        String fontvalue = aa.getString(R.styleable.ProximaFonts_fonttype);
        try {
            if (fontvalue != null) {
                switch (fontvalue) {
                    case "1":
                        typeface = Typeface.createFromAsset(getContext().getAssets(), "OpenSans-BoldItalic.ttf");
                        break;
                    case "2":
                        typeface = Typeface.createFromAsset(getContext().getAssets(), "OpenSans-ExtraBold.ttf");
                        break;
                    case "3":
                        typeface = Typeface.createFromAsset(getContext().getAssets(), "OpenSans-ExtraBoldItalic.ttf");
                        break;
                    case "4":
                        typeface = Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Italic.ttf");
                        break;
                    case "5":
                        typeface = Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Light.ttf");
                        break;
                    case "6":
                        typeface = Typeface.createFromAsset(getContext().getAssets(), "OpenSans-LightItalic.ttf");
                        break;
                    case "7":
                        typeface = Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Regular.ttf");
                        break;
                    case "8":
                        typeface = Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Semibold.ttf");
                        break;
                    case "9":
                        typeface = Typeface.createFromAsset(getContext().getAssets(), "OpenSans-SemiboldItalic.ttf");
                        break;
                    case "10":
                        typeface = Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Bold.ttf");
                        break;

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setTypeface(typeface);

        aa.recycle();

    }
}
}
