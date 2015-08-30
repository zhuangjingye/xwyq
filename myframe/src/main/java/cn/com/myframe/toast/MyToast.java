package cn.com.myframe.toast;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cn.com.myframe.MyUtils;
import cn.com.myframe.R;

public class MyToast extends Toast {
	private View mView;
	private static Context mContext;

	public MyToast(Context context) {
		super(context);

		init(context);
	}

	private void init(Context context) {
		mContext = context;
	}

	public static Toast makeText(Context context, CharSequence text,
			int duration) {
//		MyToast result = new MyToast(context);
//		View view = LayoutInflater.from(mContext).inflate(
//				R.layout.toast_custom, null);
//		TextView tv = (TextView) view.findViewById(R.id.textView);
//		tv.setText(text);
//		result.setView(view);
//		result.setDuration(duration);
//		result.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP, 0, MyUtils.dipToPx(mContext, 65));
//		return result;
		return Toast.makeText(context, text, duration);
	}

}
