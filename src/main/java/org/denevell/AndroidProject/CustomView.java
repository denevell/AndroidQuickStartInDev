package org.denevell.AndroidProject;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;

import org.denevell.AndroidProject.R;

public class CustomView extends FrameLayout {

	public CustomView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CustomView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		View v = LayoutInflater.from(context).inflate(R.layout.custom_view, this, true);
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyCustomView, 0, 0);
		CheckBox bx = (CheckBox) v.findViewById(R.id.custom_view_checkbox);
		try {
			String myString = a.getString(R.styleable.MyCustomView_my_attr);
			bx.setText(myString);
		} finally {
			a.recycle();
		}
	}

	public CustomView(Context context) {
		super(context);
	}

}

