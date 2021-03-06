package com.eraare.satellitemenu;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 卫星菜单
 *
 * @author Leo
 * @version 1
 * @since 2017-02-16
 */
public class SatelliteMenu extends FrameLayout {
    private static final int D_VALUE = 20;
    private static final float DEFAULT_MAIN_ANGLE = 135F; /*默认主菜单旋转角度*/
    private static final int DEFAULT_MAIN_ICON = R.drawable.icon_main_sat; /*主菜单默认图片资源*/
    private static final int DEFAULT_SIZE = 50; /*主菜单默认尺寸*/
    private static final int DEFAULT_GRAVITY = Gravity.BOTTOM | Gravity.LEFT; /*菜单位置*/
    private static final int DEFAULT_RADIUS = 150; /*默认半径*/
    private static final float DEFAULT_ANGLE = 90F; /*默认角度*/
    private static final int DEFAULT_DURATION = 400; /*默认时长*/

    private Context mContext; /*上下文*/
    private List<MenuItem> mMenuItems; /*菜单项*/
    private ImageView mainMenu; /*主菜单*/
    private int xDirection; /*x轴的方向左右*/
    private int yDirection; /*y轴的方向上下*/
    private boolean isShowing; /*菜单状态*/

    /*以下为属性值*/
    private int mainIcon; /*主菜单图标*/
    private int size; /*菜单尺寸*/
    private int radius; /*扇形半径*/
    private float angle; /*扇形角度*/
    private int duration; /*动画时长*/
    private int gravity; /*菜单位置*/

    public SatelliteMenu(Context context) {
        this(context, null);
    }

    public SatelliteMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SatelliteMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;/*上下文*/
        loadAttributes(context, attrs, defStyleAttr);/*加载属性*/
        initialize();/*初始化数据*/
    }

    /**
     * 加载属性值
     *
     * @param context
     * @param attrs
     */
    private void loadAttributes(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SatelliteMenu, defStyleAttr, 0);

        float defaultSize = DisplayUtils.dip2px(context, DEFAULT_SIZE);
        float defaultRadius = DisplayUtils.dip2px(context, DEFAULT_RADIUS);
        this.size = (int) ta.getDimension(R.styleable.SatelliteMenu_size, defaultSize);
        this.radius = (int) ta.getDimension(R.styleable.SatelliteMenu_radius, defaultRadius);
        this.mainIcon = ta.getResourceId(R.styleable.SatelliteMenu_icon, DEFAULT_MAIN_ICON);
        this.duration = ta.getInt(R.styleable.SatelliteMenu_duration, DEFAULT_DURATION);
        this.angle = ta.getFloat(R.styleable.SatelliteMenu_angle, DEFAULT_ANGLE);
        this.gravity = ta.getInt(R.styleable.SatelliteMenu_gravity, DEFAULT_GRAVITY);

        ta.recycle();
    }

    /*初始化数据*/
    private void initialize() {
        this.isShowing = false;
        /*this.radius = DEFAULT_RADIUS;
        this.angle = DEFAULT_ANGLE;
        this.duration = DEFAULT_DURATION;
        this.gravity = DEFAULT_GRAVITY;
        this.size = DEFAULT_SIZE;
        this.mainIcon = DEFAULT_MAIN_ICON;*/
        parseDirection(); /*解析菜单位置*/
        initMainMenu(); /*初始化主菜单*/
    }

    /**
     * 解析菜单的方向
     */
    private void parseDirection() {
        int xGravity = this.gravity & Gravity.HORIZONTAL_GRAVITY_MASK; /*过滤出y轴的Gravity*/
        int yGravity = this.gravity & Gravity.VERTICAL_GRAVITY_MASK; /*过滤出x轴的Gravity*/
        /*左边为正右边为负*/
        if (xGravity == Gravity.LEFT) {
            this.xDirection = 1;
        } else {
            this.xDirection = -1;
        }
        /*上边为整下面为负*/
        if (yGravity == Gravity.TOP) {
            this.yDirection = 1;
        } else {
            this.yDirection = -1;
        }
    }

    /**
     * 初始化主菜单
     */
    private void initMainMenu() {
        mainMenu = new ImageView(mContext);
        mainMenu.setImageResource(mainIcon);
        LayoutParams layoutParams = new LayoutParams(this.size, this.size, gravity);
        mainMenu.setLayoutParams(layoutParams);
        mainMenu.setOnClickListener(this.mOnMenuClickListener);
        this.addView(mainMenu);
    }

    /**
     * 主菜单单机事件
     */
    private OnClickListener mOnMenuClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isShowing) {
                hide();
            } else {
                show();
            }
        }
    };

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /*由于FrameLayout里面有一些测量操作其后setMeasuredDimension()*/
        /*所以super.onMeasure()不能省略且必须在前*/
        int measuredWidth = measureWidth(widthMeasureSpec);
        int measuredHeight = measureHeight(heightMeasureSpec);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    /*宽度测量*/
    private int measureWidth(int widthMeasureSpec) {
        int result;
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = this.radius + this.size - D_VALUE;
        }
        return result;
    }

    /*高度测量*/
    private int measureHeight(int heightMeasureSpec) {
        int result;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = this.radius + this.size - D_VALUE;
        }
        return result;
    }

    /*Section: 回调接口*/

    /**
     * 显示隐藏菜单
     *
     * @param show
     */
    private void showOrHideMenu(boolean show) {
        if (mMenuItems == null) return; //为空退出
        int size = mMenuItems.size();
        float anglePiece = this.angle / (size - 1);

        double translationX;
        double translationY;
        View view;

        for (int i = 0; i < size; i++) {
            translationX = Math.cos(anglePiece * i * Math.PI / 180) * this.radius * xDirection;
            translationY = Math.sin(anglePiece * i * Math.PI / 180) * this.radius * yDirection;
            view = mMenuItems.get(i).menuView;
            startAnimation(show, view, translationX, translationY);
        }
        /*最后的状态*/
        this.isShowing = show;
    }

    /*Section: 显示隐藏动画*/
    private void startAnimation(final boolean show, final View view, double translationX, double translationY) {
        /*初始化属性值*/
        float fromX = view.getTranslationX();
        float fromY = view.getTranslationY();
        float toX = (float) translationX;
        float toY = (float) translationY;
        float alphaFrom = 0.0f;
        float alphaTo = 1.0f;
        float rotateFrom = 0;
        float rotateTo = 360;

        float rotationFrom = 0f;
        float rotationTo = DEFAULT_MAIN_ANGLE;

        /*如果是隐藏*/
        if (!show) {
            fromX = (float) translationX;
            fromY = (float) translationY;
            toX = 0;
            toY = 0;
            alphaFrom = 1.0f;
            alphaTo = 0.0f;
            rotationFrom = DEFAULT_MAIN_ANGLE;
            rotationTo = 0f;
            rotateFrom = 360;
            rotateTo = 0;
        }

        /*子菜单的动画*/
        PropertyValuesHolder translationXHolder = PropertyValuesHolder.ofFloat("translationX", fromX, toX);
        PropertyValuesHolder translationYHolder = PropertyValuesHolder.ofFloat("translationY", fromY, toY);
        PropertyValuesHolder alphaHolder = PropertyValuesHolder.ofFloat("alpha", alphaFrom, alphaTo);
        PropertyValuesHolder rotationHolder = PropertyValuesHolder.ofFloat("rotation", rotateFrom, rotateTo);
        ObjectAnimator itemAnimator = ObjectAnimator
                .ofPropertyValuesHolder(view, translationXHolder, translationYHolder, alphaHolder, rotationHolder);

        /*主菜单的动画*/
        ObjectAnimator mainAnimator = ObjectAnimator.ofFloat(mainMenu, "rotation", rotationFrom, rotationTo);

        /*把两个动画集合起来*/
        AnimatorSet set = new AnimatorSet();
        set.setDuration(this.duration);
        set.setInterpolator(new BounceInterpolator());
        set.playTogether(itemAnimator, mainAnimator);
        set.start();
    }


    /**
     * 菜单单机事件
     */
    public interface OnMenuItemClickListener {
        void onMenuItemClick(View view, Object tag);
    }

    private OnMenuItemClickListener mOnMenuItemClickListener;

    /**
     * 设置选项监听事件
     *
     * @param onMenuItemClickListener
     */
    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.mOnMenuItemClickListener = onMenuItemClickListener;
    }

    /*Section: 用户接口*/

    /**
     * 添加菜单
     *
     * @param menuItem
     */
    public void addMenuItem(MenuItem menuItem) {
        /*1 如果List空则新建*/
        if (this.mMenuItems == null) {
            this.mMenuItems = new ArrayList<>();
        }
        /*2 配置菜单项*/
        setupMenuItem(menuItem);
        /*3 添加Item到List和视图*/
        this.mMenuItems.add(menuItem);
        /*从头插入使主菜单在最上面*/
        this.addView(menuItem.menuView, 0);
    }

    /**
     * 配置MenuItem
     *
     * @param menuItem
     * @return
     */
    private MenuItem setupMenuItem(MenuItem menuItem) {
        ImageView imageView = new ImageView(this.mContext);

        imageView.setImageResource(menuItem.menuIcon);
        imageView.setTag(menuItem.menuTag);

        LayoutParams layoutParams = new LayoutParams(size - D_VALUE, size - D_VALUE, gravity);
        imageView.setLayoutParams(layoutParams);

        imageView.setOnClickListener(this.mOnClickListener);
        imageView.setAlpha(0f);
        menuItem.menuView = imageView;
        return menuItem;
    }

    /* 单机事件*/
    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mOnMenuItemClickListener != null) {
                mOnMenuItemClickListener.onMenuItemClick(view, view.getTag());
            }
        }
    };

    /**
     * @return
     */
    public boolean isShowing() {
        return isShowing;
    }

    /**
     * 显示菜单
     */
    public void show() {
        showOrHideMenu(true);
    }

    /**
     * 隐藏菜单
     */
    public void hide() {
        showOrHideMenu(false);
    }
}
