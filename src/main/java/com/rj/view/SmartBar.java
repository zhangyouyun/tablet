package com.rj.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.rj.widget.utils.TabletButton;
import com.rj.widgetlib.R;

/**
 * Created by jxwq on 2016/1/20.
 */
public class SmartBar extends LinearLayout {

	public static final String TAG = SmartBar.class.getSimpleName();

	private final int BASEID = 0x1000; // 控件id为BASEID+控件描述在集合中的位置，用于通过控件id直接从控件描述集合找到控件描述
	private final String BTN_MORE_TXT = "更多"; // 更多按钮的文字

	private List<TabletButton> tabletButtons = null;

	private PopupWindow moreItemPopWindow = null; // 显示“更多”条目的下拉菜单

	private LayoutInflater inflater = null;

	private float density = 0f;
	private int emptyItemWidth = 0; // item内容为空时所占的宽度

	private int directShowItemCounts = 0;
	private int btnMoreWidth = 0;

	private OnItemClickListener smartBarItemClickListener = null;

	private TextPaint p = null; // 用来测量文字宽度
	
	private Context mCtx;

	public interface OnItemClickListener {
		void onItemClick(TabletButton btnInfo);
	}

	public SmartBar(Context context) {
		this(context, null);
		mCtx=context;
	}

	public SmartBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		mCtx=context;
	}

	public SmartBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mCtx=context;
		init();
	}

	private void init() {
		Context context = getContext();
		inflater = LayoutInflater.from(context);
		density = context.getResources().getDisplayMetrics().density;
		emptyItemWidth = (int) (density * SmartBarItem.EMPTY_WIDTH_IN_DP + 0.5f);
		btnMoreWidth = SmartBarItem.EMPTY_WIDTH_IN_DP + getItemWidth(BTN_MORE_TXT);
	}

	public void setSmartBarItemClickListener(OnItemClickListener smartBarItemClickListener) {
		this.smartBarItemClickListener = smartBarItemClickListener;
	}

	/**
	 * 设置子控件信息
	 * 
	 * @param list
	 */
	// public void setCustomWidgetButtons(List<CustomWidgetButton> list) {
	// this.customWidgetButtons = list;
	// resetChilds(getMeasuredWidth());
	// }

	public void setCustomWidgetButtons(List<TabletButton> list) {
		this.tabletButtons = list;
		if (list == null || list.size() == 0) {
			return;
		}
		resetChilds(getMeasuredWidth());
		requestLayout();
	}

	private void resetChilds(final int width) {
		getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				int currentWidth = width;
				Log.d(TAG, "1:::" + getChildCount());
				removeAllViews();
				Log.d(TAG, "2:::" + getChildCount());
				if (tabletButtons != null && tabletButtons.size() != 0) {
					Log.v(TAG, "BTNMORE_WIDTH = " + btnMoreWidth);
					Log.v(TAG, "currentWidth = " + currentWidth);
					currentWidth -= (getPaddingLeft() + getPaddingRight());
					currentWidth -= btnMoreWidth;
					Log.v(TAG, "currentWidth = " + currentWidth);
					directShowItemCounts = 0;
					if (currentWidth >= 0) { // 除“更多”按钮外还有位置放其它按钮
						int childTotalWidth = 0;
						TabletButton curBtnInfo = null;
						for (int i = 0; i < tabletButtons.size(); i++) { // 计算有多少个item可以直接显示
							curBtnInfo = tabletButtons.get(i);
							childTotalWidth += getItemWidth(curBtnInfo.getTitle());
							directShowItemCounts = i;
							if (currentWidth < childTotalWidth) { // 不够放了，那么就只能放前i个按钮和更多按钮
								break;
							}
							directShowItemCounts++;
						}
					}
					Log.v(TAG, "DIRECT = " + directShowItemCounts);
					if (directShowItemCounts == 0) {// 只放更多按钮，其它选项全部丢到popupWindow里
						setPopWindow(tabletButtons);
						addBtnMore();
					} else if (directShowItemCounts == tabletButtons.size()) { // 所有按钮都放的下
						for (int i = 0; i < directShowItemCounts; i++) {
							View v = getViewByWidgetInfo(i);
							addView(v, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
						}
					} else {// 一部分按钮要放到popupWindow里
						for (int i = 0; i < directShowItemCounts; i++) {
							View v = getViewByWidgetInfo(i);
							addView(v, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
						}
						setPopWindow(new ArrayList<TabletButton>(tabletButtons.subList(directShowItemCounts, tabletButtons.size())));
						addBtnMore();
					}
					Log.d(TAG, "3:::" + getChildCount());
				}
				getViewTreeObserver().removeGlobalOnLayoutListener(this);
			}
		});
	}

	/**
	 * （如果有）添加“更多”按钮
	 */
	private void addBtnMore() {
		SmartBarItem moreBtn = new SmartBarItem(getContext());
		moreBtn.getButton().setText(BTN_MORE_TXT);
		moreBtn.getButton().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showMoreItemsPopWindow(v);
			}
		});
		// moreBtn.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// }
		// });
		addView(moreBtn, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
	}

	private int dp=50;
	
	private void showMoreItemsPopWindow(View parent) {
		if (moreItemPopWindow != null) {
//			moreItemPopWindow.showAsDropDown(parent);   
			int xoffpx=(int) (dp * getResources().getDisplayMetrics().density + 0.5f);
			Log.i("wanan", "xoffpx="+xoffpx);
			moreItemPopWindow.showAsDropDown(parent, -xoffpx, 0);		}
	}

	private void hideMoreItemsPopWindow() {
		if (moreItemPopWindow != null && moreItemPopWindow.isShowing()) {
			moreItemPopWindow.dismiss();
		}
	}

	/**
	 * 初始化“更多”按钮下拉菜单
	 * 
	 * @param list
	 */
	private void setPopWindow(final List<TabletButton> list) {
		Log.i("wanan", "setPopWindow size=" + list.size());
		View popLayout = null;
		if (moreItemPopWindow == null) {
			popLayout = inflater.inflate(R.layout.tablet_smart_bar_popwindow, null, false);
			moreItemPopWindow = new PopupWindow(popLayout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
			moreItemPopWindow.setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		} else {
			popLayout = moreItemPopWindow.getContentView();
		}
		CornerListView listView = (CornerListView) popLayout.findViewById(R.id.form_top_btn_popup_listv);
		FormTopBtnPopupAdapter formTopBtnPopupAdapter = new FormTopBtnPopupAdapter(getContext(), list, R.layout.menu_pop_list_item);
		listView.setAdapter(formTopBtnPopupAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (smartBarItemClickListener != null) {
					smartBarItemClickListener.onItemClick(tabletButtons.get(directShowItemCounts + position));
				}

				if (moreItemPopWindow != null && moreItemPopWindow.isShowing()) {
					moreItemPopWindow.dismiss();
				}

			}
		});
	}

	private View getViewByWidgetInfo(int btnInfoIndex) {
		SmartBarItem item = new SmartBarItem(getContext());
		item.getButton().setId(BASEID + btnInfoIndex);
		final TabletButton curBtnInfo = tabletButtons.get(btnInfoIndex);
		item.getButton().setText(curBtnInfo.getTitle());
		// if (curBtnInfo.getBeforeImg() == null) {
		// item.getImvIcon().setVisibility(View.GONE);
		// }
		// item.getTxvTitle().setBackgroundResource(R.drawable.tablet_save_btn_false);

		item.getButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int index = v.getId() - BASEID;
				if (index >= 0 && index < tabletButtons.size() && smartBarItemClickListener != null) {
					smartBarItemClickListener.onItemClick(tabletButtons.get(index));
				}
			}
		});
		// item.setOnTouchListener(new OnTouchListener() {
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// int action = event.getAction();
		// SmartBarItem sbi = (SmartBarItem) v;
		// switch (action) {
		// case MotionEvent.ACTION_DOWN:
		// Log.i("wanan", "MotionEvent.ACTION_DOWN");
		// sbi.getImvIcon().setBackgroundResource(R.drawable.tablet_btn_true);
		//
		// break;
		// case MotionEvent.ACTION_UP:
		// case MotionEvent.ACTION_CANCEL:
		// Log.i("wanan", "MotionEvent.ACTION_UP");
		// sbi.getImvIcon().setBackgroundResource(R.drawable.tablet_save_btn_false);
		//
		// int index = v.getId() - BASEID;
		// if (index >= 0 && index < tabletButtons.size() &&
		// smartBarItemClickListener != null) {
		// smartBarItemClickListener.onItemClick(tabletButtons.get(index));
		// }
		//
		// break;
		// }
		// return true;
		// }
		// });

		return item;
	}

	/**
	 * 在item添加到smartbar之前计算item的宽度，以便判断item是直接显示还是显示在“更多”菜单
	 * 
	 * @param itemContent
	 * @return
	 */
	private int getItemWidth(String itemContent) {
		if (TextUtils.isEmpty(itemContent)) {
			return 0;
		}
		if (p == null) {
			SmartBarItem item = new SmartBarItem(getContext());
			p = item.getButton().getPaint();
		}
		int txtWidth = (int) (p.measureText(itemContent) + 0.5f);
		return emptyItemWidth + txtWidth;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		Log.d(TAG, "oldw = " + oldw + ":: w = " + w);
		super.onSizeChanged(w, h, oldw, oldh);
		if (w == 0 || oldw == w) {
			return;
		}
		resetChilds(w);
	}

}

class FormTopBtnPopupAdapter extends BaseAdapter {
	private List<TabletButton> formTopButtonlist;
	private LayoutInflater inflater;
	private int resource;
	private Context context;

	public FormTopBtnPopupAdapter(Context context, List<TabletButton> formTopButtonlist, int resource) {
		this.formTopButtonlist = formTopButtonlist;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.resource = resource;
		this.context = context;

	}

	@Override
	public int getCount() {
		return formTopButtonlist.size();
	}

	@Override
	public Object getItem(int position) {
		return formTopButtonlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		viewCache viewCache = null;
		if (convertView == null) {
			convertView = inflater.inflate(resource, null);
			viewCache = new viewCache();
			viewCache.popupIcon = (ImageView) convertView.findViewById(R.id.popup_icon);
			viewCache.popupText = (TextView) convertView.findViewById(R.id.popup_txt);
			convertView.setTag(viewCache);
		} else {
			viewCache = (viewCache) convertView.getTag();
		}
		final TabletButton info = formTopButtonlist.get(position);
		viewCache.popupIcon.setBackgroundDrawable(info.getBeforeImg());
		if (info.getBeforeImg() == null) {
			viewCache.popupIcon.setVisibility(View.GONE);
		} else {
			viewCache.popupIcon.setVisibility(View.VISIBLE);
		}

		// AutoSizeTool.setFormTopNavigationBarImageView(viewCache.popupIcon);
		viewCache.popupText.setText(info.getTitle());
		// AutoSizeTool.setTopNavigationPopupWindowTextSize(viewCache.popupText);
		// AutoSizeTool.setFormTopNavigationBarImageView(viewCache.popupTickIconImgv);
		return convertView;
	}

	public class viewCache {
		ImageView popupIcon;
		TextView popupText;
	}
}
