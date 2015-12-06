package cc.horizoom.ssl.xwyq.MainNewsPage.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import cc.horizoom.ssl.xwyq.DataManager.CardData;
import cc.horizoom.ssl.xwyq.DataManager.MenuListData;
import cc.horizoom.ssl.xwyq.DataManager.entity.CardEntity;
import cc.horizoom.ssl.xwyq.MainNewsPage.CardNewsAdapter;
import cc.horizoom.ssl.xwyq.MainNewsPage.MainNewsPageActivity;
import cc.horizoom.ssl.xwyq.MainNewsPage.MenuAdapter;
import cc.horizoom.ssl.xwyq.MainNewsPage.MenuListActivity;
import cc.horizoom.ssl.xwyq.MainNewsPage.NewsAdapter;
import cc.horizoom.ssl.xwyq.R;
import cn.com.myframe.MyUtils;
import cn.com.myframe.view.BounceListView;
import cn.com.myframe.view.MyBounceListView.MyBounceListview;

/**
 * Created by pizhuang on 2015/9/8.
 * 选项卡视图
 */
public class CardsView extends LinearLayout implements ViewPager.OnPageChangeListener{

    private Context context;

    private ViewPager pager = null;
    private LinearLayout indicatorLl;

    private MainNewsPageActivity mainNewsPageActivity;

    private ArrayList<View> viewContainter;

    private ArrayList<CardEntity> cardEntities = null;

    private int lastState = -1;

    private int currentPosition;

    private OnChangeListener onChangeListener;

    private MyPagerAdapter myPagerAdapter;

    public CardsView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public CardsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    private void initView() {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.view_cards, this);
        pager = (ViewPager) view.findViewById(R.id.viewpager);
        indicatorLl = (LinearLayout) view.findViewById(R.id.indicatorLl);
    }

    /**
     * 设置监听器
     * @param onChangeListener
     */
    public void setOnChangeListener(OnChangeListener onChangeListener) {
        this.onChangeListener = onChangeListener;
    }

    /**
     * 更新控件显示
     *
     * @param mainNewsPageActivity
     */

    public void updateView(MainNewsPageActivity mainNewsPageActivity) {
        this.mainNewsPageActivity = mainNewsPageActivity;
        getViewContainter();
        myPagerAdapter = new MyPagerAdapter();
        pager.setAdapter(myPagerAdapter);
        pager.setOnPageChangeListener(this);
        pager.setCurrentItem(getCardPosition(), false);
        updateIndicatorLl(getCardPosition()-1);
    }

    /**
     * 返回卡片位置
     * @return
     */
    private int getCardPosition() {
        CardEntity selecedCardEntity = MenuListData.getInstance().getSelectedCardEntity();
        if (cardEntities == null
                || cardEntities.size() == 0
                || selecedCardEntity == null) {
            return 1;
        }

        for (int i=0;i<cardEntities.size();i++) {
            CardEntity tmp = cardEntities.get(i);
            if (TextUtils.equals(selecedCardEntity.getName(),tmp.getName())) {
                return i+1;
            }
        }
        return 1;
    }

    /**
     * 获得要显示等控件列表
     */
    private void getViewContainter() {
        viewContainter = new ArrayList<View>();
        cardEntities = CardData.getInstance().getCardDatas(mainNewsPageActivity);
        if (cardEntities == null || cardEntities.size()==0) {
            return;
        }
        for (int i = 0; i < cardEntities.size(); i++) {
            CardEntity cardEntity = cardEntities.get(i);
            viewContainter.add(getViewIntem(cardEntity));
        }
    }

    /**
     * 根据参数绘制控件
     *
     * @param cardEntity
     * @return
     */
    private View getViewIntem(final CardEntity cardEntity) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.view_cards_item, null);
        TextView titleTv = (TextView) view.findViewById(R.id.cardTietleTv);
        titleTv.setText(cardEntity.getName());
        ListView myListView = (ListView) view.findViewById(R.id.myListView);
        CardNewsAdapter cardNewsAdapter = new CardNewsAdapter(mainNewsPageActivity,cardEntity.getNewsData());
        myListView.setAdapter(cardNewsAdapter);
        return view;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        MyUtils.log(CardsView.class,"positionOffset="+positionOffset+"   positionOffsetPixels="+positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
        updateIndicatorLl(position);
        currentPosition = position;
        int num = (int) cardEntities.get(position).getWarning_push_content_nums();
        if (onChangeListener != null) {
            onChangeListener.onChangeListener(num);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (lastState == 1 && state==0 && currentPosition == (cardEntities.size()-1)) {
            mainNewsPageActivity.startLoginNewsActivity();
        }
        lastState = state;

    }

    /**
     * 向左移动
     */
    public void scrollToLeft () {

        if (pager.getCurrentItem() >= 1) {
            pager.setCurrentItem(pager.getCurrentItem() - 1, true);
        }
    }

    /**
     * 像右移动
     */
    public void scrollToRight() {
        if (pager.getCurrentItem() < (cardEntities.size()-1)) {
            pager.setCurrentItem(pager.getCurrentItem() + 1, true);
        } else {
            mainNewsPageActivity.startLoginNewsActivity();
        }
    }

    /**
     * 根据当前等状态显示指示器
     *
     * @param selectId
     */
    private void updateIndicatorLl(int selectId) {
        indicatorLl.removeAllViews();
        cardEntities = CardData.getInstance().getCardDatas(mainNewsPageActivity);
        for (int i = 0; i < cardEntities.size(); i++) {
            ImageView imageView = new ImageView(context);
            if (i == selectId) {
                imageView.setImageResource(R.drawable.focus_point_select);
            } else {
                imageView.setImageResource(R.drawable.focus_point_default);
            }
            imageView.setPadding(5, 15, 15, 15);
            indicatorLl.addView(imageView);
        }

    }

    /**
     * 适配器
     */
    class MyPagerAdapter extends PagerAdapter {

        //viewpager中的组件数量
        @Override
        public int getCount() {
            return viewContainter.size();
        }

        //滑动切换的时候销毁当前的组件
        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            ((ViewPager) container).removeView(viewContainter.get(position));
        }

        //每次滑动的时候生成的组件
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ((ViewPager) container).addView(viewContainter.get(position));
            return viewContainter.get(position);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }
    }

    public interface OnChangeListener{
        public void onChangeListener(int num);
    }

}
