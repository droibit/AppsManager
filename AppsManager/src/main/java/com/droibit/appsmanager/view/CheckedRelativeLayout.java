package com.droibit.appsmanager.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.RelativeLayout;

import com.droibit.appsmanager.R;
import com.droibit.utils.NullCheck;

/**
 * チェック可能な{@linkplain android.widget.RelativeLayout}。<br>
 * チェックされた場合はビューの背景がハイライトになる
 * 
 * @author kumagai
 *
 */
public class CheckedRelativeLayout extends RelativeLayout implements Checkable {

	private static final int[] CHECKED_STATE_SET = { android.R.attr.state_checked };

	/** チェックフラグ */
	private boolean checked;

	/**
	 * コンストラクタ
	 * 
	 * @param context コンテキスト
	 */
	public CheckedRelativeLayout(Context context) {
		this(context, null, 0);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param context コンテキスト
	 * @param attrs 属性セット
	 */
	public CheckedRelativeLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param context コンテキスト
	 * @param attrs 属性セット
	 * @param defStyle スタイル定義
	 */
	public CheckedRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		final Drawable background = getBackground();
		if (NullCheck.isNull(background)) {
			setBackgroundResource(R.drawable.checked_background);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void setChecked(boolean checked) {
		if (this.checked != checked) {
			this.checked = checked;
			refreshDrawableState();
		}
	}

	/** {@inheritDoc} */
	@Override
	public boolean isChecked() {
		return checked;
	}

	/** {@inheritDoc} */
	@Override
	public void toggle() {
		setChecked(!checked);
	}

	/** {@inheritDoc} */
	@Override
	protected int[] onCreateDrawableState(int extraSpace) {
		final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
		if (isChecked()) {
			mergeDrawableStates(drawableState, CHECKED_STATE_SET);
		}
		return drawableState;
	}
}
