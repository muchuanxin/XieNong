package com.xidian.xienong.shoppingmall.brand;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.xidian.xienong.R;
import com.xidian.xienong.agriculture.me.FeedbackActivity;
import com.xidian.xienong.home.HomePageActivity;
import com.xidian.xienong.home.LoginActivity;
import com.xidian.xienong.model.Commodity;
import com.xidian.xienong.model.CommodityComment;
import com.xidian.xienong.model.CommodityImage;
import com.xidian.xienong.network.BaseCallback;
import com.xidian.xienong.network.OKHttp;
import com.xidian.xienong.network.Url;
import com.xidian.xienong.util.CircleImageView;
import com.xidian.xienong.util.Constants;
import com.xidian.xienong.util.GradationScrollView;
import com.xidian.xienong.util.MD5Util;
import com.xidian.xienong.util.MyImageLoader;
import com.xidian.xienong.util.NoScrollListView;
import com.xidian.xienong.util.ScrollViewContainer;
import com.xidian.xienong.util.SharePreferenceUtil;
import com.xidian.xienong.util.StatusBarUtil;
import com.xidian.xienong.util.Time;
import com.xidian.xienong.util.ToastCustom;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by koumiaojuan on 2017/6/30.
 */

public class CommodityDetailActivity extends AppCompatActivity implements GradationScrollView.ScrollViewListener{

    @Bind(R.id.top_scrollview)
    GradationScrollView topScrollView;
    @Bind(R.id.iv_commodity_detail_img)
    ImageView commodityDetailImg;
    @Bind(R.id.ll_commodity_detail)
    RelativeLayout llTitle;
    @Bind(R.id.ll_commodity_content)
    LinearLayout llCommodityContent;
    @Bind(R.id.tv_commodity_detail_name)
    TextView name;
    @Bind(R.id.tv_commodity_detail_rule)
    TextView rule;
    @Bind(R.id.tv_commodity_detail_sale_price)
    TextView sale_price;
    @Bind(R.id.tv_good_detail_on_shelves_price)
    TextView on_shelves_price;
    @Bind(R.id.tv_good_detail_expense)
    TextView expense;
    @Bind(R.id.tv_good_detail_month_sale)
    TextView month_sale;
    @Bind(R.id.tv_good_detail_producing_area)
    TextView producing_area;
    @Bind(R.id.ll_comment_area)
    LinearLayout llCommentArea;
    @Bind(R.id.comment_person_photo)
    CircleImageView photo;
    @Bind(R.id.comment_person_name)
    TextView person;
    @Bind(R.id.iv_start_5)
    ImageView start_5;
    @Bind(R.id.iv_start_4)
    ImageView start_4;
    @Bind(R.id.iv_start_3)
    ImageView start_3;
    @Bind(R.id.iv_start_2)
    ImageView start_2;
    @Bind(R.id.iv_start_1)
    ImageView start_1;
    @Bind(R.id.tv_comment_time)
    TextView time;
    @Bind(R.id.tv_comment_content)
    TextView content;
    @Bind(R.id.tv_comments_detail_open)
    TextView all_comments;



    @Bind(R.id.sv_container)
    ScrollViewContainer scrollViewContainer;
    @Bind(R.id.tv_commodity_detail_brief)
    TextView tvCommodityTitle;
    @Bind(R.id.nlv_good_detial_imgs)
    NoScrollListView nlvImgs;//图片详情
    @Bind(R.id.tv_good_detail_shop_cart)
    TextView addCart;
    @Bind(R.id.tv_good_detail_number)
    TextView productParameter;
    @Bind(R.id.iv_commodity_back)
    ImageView commodityDetailBack;
    @Bind(R.id.iv_commodity_shop)
    ImageView shop;
    @Bind(R.id.iv_commodity_share)
    ImageView share;

    private List<String> imgsUrl;
    private QuickAdapter<String> imgAdapter;
    private int height;
    private int width;
    private PopupWindow pop;
    private View addCartLayoutWindow,productParameterLayoutWindow;
    private TextView popReduce,popNumber,popAdd,popFinish,popPrice,popVolume;
    private final int ADDORREDUCE=1;
    private ImageView popClear;
    private View popView,popProductView;
    private TextView popAddCart,popInstanceBuy;
    private ImageView my_shopping_cart;
    private Intent previousIntent;
    private Commodity commodity;
    private TextView netWeight,manner,area,warranty;
    private List<ImageView> starts = new ArrayList<>();
    private OKHttp httpUrl;
    private ImageView picture;
    private  SharePreferenceUtil sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commodity_detail_activity);

        httpUrl = OKHttp.getInstance();

        initViews();
        initDatas();
        initEvents();
    }

    private void initEvents() {
        commodityDetailBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommodityDetailActivity.this,CartActivity.class);
                startActivity(intent);
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent textIntent = new Intent(Intent.ACTION_SEND);
                textIntent.setType("text/plain");
                textIntent.putExtra(Intent.EXTRA_TEXT, "这是一段分享的文字");
                startActivity(Intent.createChooser(textIntent, "分享"));
            }
        });
        productParameter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow(v);
            }
        });

        popFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
            }
        });

        popProductView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
            }
        });
        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popWindow(v);
            }
        });
        popAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(CommodityDetailActivity.this,CartActivity.class);
                startActivity(intent);*/
                if ("".equals(sp.getUserId())){
                    Intent intent = new Intent(CommodityDetailActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else {
                    addCommodityToCart();
                }
            }
        });
        popInstanceBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"".equals(sp.getUserId())) {
                    Intent intent = new Intent(CommodityDetailActivity.this, AffirmOrderActivity.class);
                    intent.putExtra("commodity", commodity);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(CommodityDetailActivity.this,LoginActivity.class);
                    intent.putExtra("clickView", "AffirmOrderActivity");
                    intent.putExtra("commodity", commodity);
                    startActivity(intent);
                }
            }
        });

        my_shopping_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"".equals(sp.getUserId())){
                    Intent intent = new Intent(CommodityDetailActivity.this,CartActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(CommodityDetailActivity.this,LoginActivity.class);
                    intent.putExtra("clickView", "CartActivity");
                    startActivity(intent);
                }
            }
        });

        popReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!popNumber.getText().toString().equals("1")) {
                    String num_reduce=Integer.valueOf(popNumber.getText().toString())-ADDORREDUCE+"";
                    popNumber.setText(num_reduce);
                }else {
                    Toast.makeText(CommodityDetailActivity.this, "购买数量不能低于1件", Toast.LENGTH_SHORT).show();
                }
            }
        });
        popAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(popNumber.getText().toString())<Integer.parseInt(commodity.getCurrentCount())) {

                    String num_add=Integer.valueOf(popNumber.getText().toString())+ADDORREDUCE+"";
                    popNumber.setText(num_add);
                }else {
                    Toast.makeText(CommodityDetailActivity.this, "不能超过最大产品数量", Toast.LENGTH_SHORT).show();
                }

            }
        });
        popClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
            }
        });
        popView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
            }
        });

    }

    /*private void checkIsLogin() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("telephone", sp.getPhoneNumber());
        map.put("password", MD5Util.getMD5String(sp.getPassword()));
        map.put("token", sp.getToken());
        httpUrl.post(Url.IsLogin, map, new BaseCallback<String>() {
            @Override
            public void onRequestBefore() {
                Log.i("kmj", "IsLogin : " + Url.IsLogin);
            }

            @Override
            public void onFailure(Request request, Exception e) {
                Log.i("kmj", "onFailure : " + e.toString());
            }

            @Override
            public void onSuccess(Response response, String resultResponse) {
                Log.i("kmj", "result : " + resultResponse);
                try {
                    JSONObject jb = new JSONObject(resultResponse);
                    String result = jb.getString("reCode");
                    String msg = jb.getString("message");
                    if(result.equals("SUCCESS")){
                        addCommodityToCart();
                    }else{
                        Intent  intent = new Intent(CommodityDetailActivity.this, LoginActivity.class);
                        intent.putExtra("clickView", "commodity_detail");
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Response response, int errorCode, Exception e) {
                Log.i("kmj", "error : " + e.toString());
            }
        });
    }*/

    private void addCommodityToCart() {

        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", sp.getUserId());
        map.put("commodity_id", commodity.getCommodityId());
        map.put("order_quantities", popNumber.getText().toString());

        httpUrl.post(Url.AddCommodityToCart, map, new BaseCallback<String>() {
            @Override
            public void onRequestBefore() {
                Log.i("mcx", "AddCommodityToCart : " + Url.AddCommodityToCart);
            }

            @Override
            public void onFailure(Request request, Exception e) {
                Log.i("mcx", "onFailure : " + e.toString());
            }

            @Override
            public void onSuccess(Response response, String resultResponse) {
                Log.i("mcx", "result : " + resultResponse);
                try {
                    JSONObject jb = new JSONObject(resultResponse);
                    String result = jb.getString("reCode");
                    if (result.equals("SUCCESS")) {
                        ToastCustom.makeToast(CommodityDetailActivity.this, "加入购物车成功！");
                        pop.dismiss();
                    }
                    else {
                        ToastCustom.makeToast(CommodityDetailActivity.this, "加入失败，请重试！");
                        pop.dismiss();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Response response, int errorCode, Exception e) {
                Log.i("mcx", "error : " + e.toString());
            }
        });
    }


    private void popWindow(View v) {
        if(v==productParameter){
            pop = new PopupWindow(productParameterLayoutWindow, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true) ;
            netWeight.setText(commodity.getProduceParameter().getNetWeight());
            manner.setText(commodity.getProduceParameter().getPackagingManner());
            area.setText(commodity.getProduceParameter().getProducingArea());
            warranty.setText(commodity.getProduceParameter().getWarranty()+"天");
        }else{
            pop = new PopupWindow(addCartLayoutWindow, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true) ;
            Glide.with(CommodityDetailActivity.this).load(commodity.getCommodityImageList().get(0).getCommodityImgUrl()).placeholder(R.drawable.empty_picture)
                    .into(picture);
            popPrice.setText("￥"+String.format("%.2f", Double.parseDouble(commodity.getPrice()) * Double.parseDouble(commodity.getDiscount())));
            Log.e("getCurrentCount",""+commodity.getCurrentCount());
            popVolume.setText(commodity.getCurrentCount());
        }
        pop.setOutsideTouchable(true);
        pop.setFocusable(true);
        pop.update();
        pop.setBackgroundDrawable(new ColorDrawable(0));
        pop.showAtLocation(v, Gravity.BOTTOM, 0, 0);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                // TODO Auto-generated method stub
                pop.dismiss();
            }
        });
    }

    private void initDatas() {
        //透明状态栏
        StatusBarUtil.setTranslucentForImageView(this,llCommodityContent);
        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) llCommodityContent.getLayoutParams();
        params1.setMargins(0,-StatusBarUtil.getStatusBarHeight(this)/4,0,0);
        llCommodityContent.setLayoutParams(params1);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) commodityDetailImg.getLayoutParams();
        params.height = getScreenHeight(this)*2/3;
        commodityDetailImg.setLayoutParams(params);

        scrollViewContainer = new ScrollViewContainer(getApplicationContext());
        previousIntent = getIntent();
        commodity  = (Commodity) previousIntent.getSerializableExtra("commodity");
        sp = new SharePreferenceUtil(getApplicationContext(), Constants.SAVE_USER);
        initBasicDatas();
        initImgDatas();
        initListeners();
    }

    private void initBasicDatas() {
        if(commodity.getCommodityImageList().size() > 0) {
            Glide.with(CommodityDetailActivity.this).load(commodity.getCommodityImageList().get(0).getCommodityImgUrl()).centerCrop().placeholder(R.drawable.empty_picture).into(commodityDetailImg);
        }
        name.setText(commodity.getCommodityName());
        rule.setText(commodity.getSpecification());
        sale_price.setText(String.format("%.2f", Double.parseDouble(commodity.getPrice()) * Double.parseDouble(commodity.getDiscount())));
        on_shelves_price.setText(commodity.getPrice());
        expense.setText("运费"+commodity.getTransportExpense()+"元");
        month_sale.setText("月销"+commodity.getDealVolume()+"笔");
        producing_area.setText("产地"+commodity.getProduceParameter().getProducingArea());
        Log.i("kmj","commodity : " + commodity.getCommodityCommentsList().size());
        if(commodity.getCommodityCommentsList().size() == 0){
            llCommentArea.setVisibility(View.GONE);
            all_comments.setText("暂无评论");
        }else{
            llCommentArea.setVisibility(View.VISIBLE);
            all_comments.setText("查看所有评论");
            CommodityComment cc = commodity.getCommodityCommentsList().get(0);
            Glide.with(CommodityDetailActivity.this).load(cc.getUser().getHead_photo()).centerCrop().placeholder(R.drawable.empty_picture).into(photo);
            person.setText(cc.getUser().getUserName());
            int level = (int) Math.floor(cc.getCommentLevel());
            resetStarts(level);
            time.setText(cc.getCommentTime());
            content.setText(cc.getCommentContent());
        }


    }

    private void resetStarts(int level) {
        for(int i = 0 ; i< 5;i++){
            starts.get(i).setVisibility(View.GONE);
        }
        for(int i = 0 ; i< level;i++){
            starts.get(i).setVisibility(View.VISIBLE);
        }
    }

    private void initListeners() {
        ViewTreeObserver vto = commodityDetailImg.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                llTitle.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                height = commodityDetailImg.getHeight();

                topScrollView.setScrollViewListener(CommodityDetailActivity.this);
            }
        });
    }

    private void initImgDatas(){
        width = getScreenWidth(getApplicationContext());
        imgsUrl = new ArrayList<>();
        for(CommodityImage ci: commodity.getCommodityImageList()){
            imgsUrl.add(ci.getCommodityImgUrl());
        }
        imgAdapter = new QuickAdapter<String>(this,R.layout.adapter_good_detail_imgs) {
            @Override
            protected void convert(BaseAdapterHelper helper, String item) {
                ImageView iv = helper.getView(R.id.iv_adapter_good_detail_img);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) iv.getLayoutParams();
                params.width = width;
                params.height = width/2;
                iv.setLayoutParams(params);
                MyImageLoader.getInstance().displayImageCen(getApplicationContext(),item,iv,width,width/2);
            }
        };
        imgAdapter.addAll(imgsUrl);
        nlvImgs.setAdapter(imgAdapter);
    }

    public  int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    private void initViews() {
        ButterKnife.bind(this);
        starts.add(start_1);
        starts.add(start_2);
        starts.add(start_3);
        starts.add(start_4);
        starts.add(start_5);
        productParameterLayoutWindow = getLayoutInflater().inflate(R.layout.product_parameter_popwindow,null);
        popFinish = (TextView)productParameterLayoutWindow.findViewById(R.id.pop_finish);
        popProductView =  (View)productParameterLayoutWindow.findViewById(R.id.pop_produce_view);
        netWeight = (TextView)productParameterLayoutWindow.findViewById(R.id.net_weigt);
        manner = (TextView)productParameterLayoutWindow.findViewById(R.id.packaging_manner);
        area = (TextView)productParameterLayoutWindow.findViewById(R.id.pop_area);
        warranty = (TextView)productParameterLayoutWindow.findViewById(R.id.warranty);

        addCartLayoutWindow = getLayoutInflater().inflate(R.layout.add_cart_popwindow,null);
        popReduce = (TextView)addCartLayoutWindow.findViewById(R.id.pop_reduce);
        popNumber = (TextView)addCartLayoutWindow.findViewById(R.id.pop_num);
        popAdd = (TextView)addCartLayoutWindow.findViewById(R.id.pop_add);
        popClear = (ImageView)addCartLayoutWindow.findViewById(R.id.pop_clear);
        popView = (View)addCartLayoutWindow.findViewById(R.id.pop_view);
        popAddCart = (TextView)addCartLayoutWindow.findViewById(R.id.pop_add_cart);
        popPrice = (TextView)addCartLayoutWindow.findViewById(R.id.pop_cart_price);
        popVolume = (TextView)addCartLayoutWindow.findViewById(R.id.pop_cart_current_volume);
        picture = (ImageView) addCartLayoutWindow.findViewById(R.id.id_publish_machine_button);

        popInstanceBuy = (TextView)findViewById(R.id.tv_good_detail_buy);
        my_shopping_cart=(ImageView)findViewById(R.id.iv_commodity_shop);

    }

    @Override
    public void onScrollChanged(GradationScrollView scrollView, int x, int y, int oldx, int oldy) {
        // TODO Auto-generated method stub
        if (y <= 0) {   //设置标题的背景颜色
            llTitle.setBackgroundColor(Color.argb((int) 0, 255,255,255));
        } else if (y > 0 && y <= height) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
            float scale = (float) y / height;
            float alpha = (255 * scale);
            tvCommodityTitle.setTextColor(Color.argb((int) alpha, 1,24,28));
            llTitle.setBackgroundColor(Color.argb((int) alpha, 255,255,255));
        } else {    //滑动到banner下面设置普通颜色
            llTitle.setBackgroundColor(Color.argb((int) 255, 255,255,255));
        }

    }
}
