package com.rj.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rj.widget.utils.TabletButton;
import com.rj.widgetlib.R;

public class TabletMenuLayout extends LinearLayout implements SmartBar.OnItemClickListener {
	private SmartBar smartBar;
	private List<TabletButton> listDatas = new ArrayList<TabletButton>();
	private ImageView mBtnExit;
	private Button mBtnEraser;
	public static final int BT_TYPE_CLEAR = 1;
	public static final int BT_TYPE_REM = BT_TYPE_CLEAR + 1;
	public static final int BT_TYPE_UNDO = BT_TYPE_CLEAR + 2;
	public static final int BT_TYPE_REDO = BT_TYPE_CLEAR + 3;
	public static final int BT_TYPE_PREPAGE = BT_TYPE_CLEAR + 4;
	public static final int BT_TYPE_NEXTPAGE = BT_TYPE_CLEAR + 5;
	public static final int BT_TYPE_SAVE = BT_TYPE_CLEAR + 6;

	public TabletMenuLayout(Context context) {
		this(context, null);
	}

	public TabletMenuLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TabletMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		setOrientation(HORIZONTAL);
		setGravity(Gravity.CENTER_VERTICAL);
		LayoutInflater.from(getContext()).inflate(R.layout.tablet_topbar, this, true);
		mBtnExit = (ImageView) findViewById(R.id.btn_exit);
		mBtnExit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mOperateTabletListener != null) {
					mOperateTabletListener.exit();
				}
			}
		});
		mBtnEraser = (Button) findViewById(R.id.btn_eraser);
		mBtnEraser.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				boolean isEraser = mOperateTabletListener.isEraser();
				isEraser = !isEraser;
				mOperateTabletListener.setEraser(isEraser);
				if (isEraser) {
					mBtnEraser.setBackgroundResource(R.drawable.tablet_btn_true);
				} else {
					mBtnEraser.setBackgroundResource(R.drawable.tablet_save_btn_false);
				}

			}
		});
		smartBar = (SmartBar) findViewById(R.id.smartbar);
		smartBar.setSmartBarItemClickListener(this);
	}

	public void setData(boolean showLastBT) {
		listDatas.clear();
		TabletButton tb1 = new TabletButton();
		tb1.setCallBackType(BT_TYPE_CLEAR);
		tb1.setTitle(getResources().getString(R.string.tablet_clear));
		listDatas.add(tb1);

		if (showLastBT) {
			TabletButton tb2 = new TabletButton();
			tb2.setCallBackType(BT_TYPE_REM);
			tb2.setTitle(getResources().getString(R.string.tablet_rem));
			listDatas.add(tb2);
		}

		TabletButton tb3 = new TabletButton();
		tb3.setCallBackType(BT_TYPE_UNDO);
		tb3.setTitle(getResources().getString(R.string.tablet_undo));
		listDatas.add(tb3);

		TabletButton tb4 = new TabletButton();
		tb4.setCallBackType(BT_TYPE_REDO);
		tb4.setTitle(getResources().getString(R.string.tablet_redo));
		listDatas.add(tb4);

		TabletButton tb5 = new TabletButton();
		tb5.setCallBackType(BT_TYPE_PREPAGE);
		tb5.setTitle(getResources().getString(R.string.tablet_pre));
		listDatas.add(tb5);

		TabletButton tb6 = new TabletButton();
		tb6.setCallBackType(BT_TYPE_NEXTPAGE);
		tb6.setTitle(getResources().getString(R.string.tablet_next));
		listDatas.add(tb6);

		TabletButton tb7 = new TabletButton();
		tb7.setCallBackType(BT_TYPE_SAVE);
		tb7.setTitle(getResources().getString(R.string.tablet_save));
		listDatas.add(tb7);

		smartBar.setCustomWidgetButtons(listDatas);
	}

	public void setEraserFase() {
		mBtnEraser.setBackgroundResource(R.drawable.tablet_save_btn_false);
	}
	public void onItemClick(TabletButton btnInfo) {
		Log.i("wanan", "onItemClick="+btnInfo.getTitle());
		switch (btnInfo.getCallBackType()) {
		case BT_TYPE_CLEAR:
			mOperateTabletListener.clear();
			break;
		case BT_TYPE_REM:
			mOperateTabletListener.rem();
			break;

		case BT_TYPE_UNDO:
			mOperateTabletListener.undo();
			break;

		case BT_TYPE_REDO:
			mOperateTabletListener.redo();
			break;

		case BT_TYPE_PREPAGE:
			mOperateTabletListener.prePage();
			break;

		case BT_TYPE_NEXTPAGE:
			mOperateTabletListener.nextPage();
			break;

		case BT_TYPE_SAVE:
			mOperateTabletListener.saveTablet();
			break;

		default:
			break;
		}
	}

	public OperateTabletListener getOperateTabletListener() {
		return mOperateTabletListener;
	}

	public void setOperateTabletListener(OperateTabletListener operateTabletListener) {
		this.mOperateTabletListener = operateTabletListener;
	}

	private OperateTabletListener mOperateTabletListener;

	public interface OperateTabletListener {
		void clear();

		void rem();

		void undo();

		void redo();

		void prePage();

		void nextPage();

		void saveTablet();

		void setEraser(boolean b);

		void exit();

		boolean isEraser();
	}

}
