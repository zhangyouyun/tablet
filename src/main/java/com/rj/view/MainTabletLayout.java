package com.rj.view;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
//import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rj.view.MyTabletView.ShowPageListener;
import com.rj.view.TabletMenuLayout.OperateTabletListener;
import com.rj.widget.utils.Compress;
import com.rj.widgetlib.R;

public class MainTabletLayout extends LinearLayout implements OperateTabletListener {

	private MyTabletView mMyTabletView;
	private TabletMenuLayout mTabletMenuLayout;
	private String mPngPath, mHwPath, mLastHWPath, mHwPathShow;
	private Context mContext;
	private TextView mPageTV;

	public MainTabletLayout(Context context) {
		super(context);
		mContext = context;
	}

	public MainTabletLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.main_tablet, this, true);
		mContext = context;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mPageTV = (TextView) findViewById(R.id.page_count);
		mTabletMenuLayout = (TabletMenuLayout) findViewById(R.id.tablet_menu);
		mMyTabletView = (MyTabletView) findViewById(R.id.mytabletview);
		mTabletMenuLayout.setOperateTabletListener(this);
		mMyTabletView.setShowPageListener(new ShowPageListener() {
			@Override
			public void hidePreBT() {

			}

			@Override
			public void showPreBT() {

			}

			@Override
			public void showPageCount(String count) {
				mPageTV.setText(count);
			}
		});

	}

	public void save() {
		Bitmap tempBitmap = mMyTabletView.processBitmap();
		if (mOnTabletResultListener != null) {
			boolean isSaveSuccess = mMyTabletView.saveBitmapAndPoint(tempBitmap, mMyTabletView.printPointsToByte(), mPngPath, mHwPath);
			// mOnTabletResultListener.onShowBitmap(tempBitmap);
			if (isSaveSuccess) {
				mOnTabletResultListener.onSaveSuccessCallBack(mPngPath, mHwPath);
			} else {
				mOnTabletResultListener.onSaveErrorCallBack();
			}
		}
	}
//	public void setSavePath(String pngPath, String hwPath, String lastHwPath, String hwPathShow) {
//		mPngPath = pngPath;
//		mHwPath = hwPath;
//		mLastHWPath = lastHwPath;
//		mHwPathShow = hwPathShow;
//		mMyTabletView.getPointFsByBytes(mHwPathShow);
//		mTabletMenuLayout.setData(TextUtils.isEmpty(mLastHWPath) ? false : true);
//	}

	public interface onTabletResultListener {
		public void onShowBitmap(Bitmap bitmap);

		public void onSavePointFs(String pointfsInfo);

		/**
		 * 手写图片保存成功后回调接口
		 */
		public void onSaveSuccessCallBack(String pngPath, String hwPath);

		/**
		 * 手写图片保存失败后回调接口
		 */
		public void onSaveErrorCallBack();

		public void onCloseTablet();
	}

	private onTabletResultListener mOnTabletResultListener;

//	public onTabletResultListener getTabletResultListener() {
//		return mOnTabletResultListener;
//	}
//
//	public void setOnTabletResultListener(onTabletResultListener mOnTabletResultListener) {
//		this.mOnTabletResultListener = mOnTabletResultListener;
//	}
//
//	public void recycle() {
//		mMyTabletView.setEraser(false);
//		mTabletMenuLayout.setEraserFase();
//		mMyTabletView.recycleAll();
//	}
//
//	public boolean isModify() {
//		if (mMyTabletView != null)
//			return mMyTabletView.isModify();
//		else {
//			return false;
//		}
//	}

	@Override
	public void clear() {
		mMyTabletView.clearCanvas();
		if (mMyTabletView.getPointFsList().size() != 0)
			mMyTabletView.clear();
	}

	@Override
	public void rem() {
		Compress.buildDialog(mContext, "上次痕迹", "即将删除当前痕迹", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mMyTabletView.lastPath(mLastHWPath);
			}
		}, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
	}

	@Override
	public void undo() {
		mMyTabletView.undo();
	}

	@Override
	public void redo() {
		mMyTabletView.redo();
	}

	@Override
	public void prePage() {
		mMyTabletView.prePage();
	}

	@Override
	public void nextPage() {
		mMyTabletView.nextPage();
	}

	@Override
	public void setEraser(boolean b) {
		mMyTabletView.setEraser(b);
	}

	@Override
	public boolean isEraser() {
		return mMyTabletView.isEraser();
	}

	@Override
	public void saveTablet() {
		save();
	}

	@Override
	public void exit() {
		if (mOnTabletResultListener != null) {
			mOnTabletResultListener.onCloseTablet();
		}
	}

}
