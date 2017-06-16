package com.xidian.xienong.agriculture.me;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.xidian.xienong.R;
import com.xidian.xienong.adapter.CommentAdapter;
import com.xidian.xienong.model.CommentBean;
import com.xidian.xienong.network.BaseCallback;
import com.xidian.xienong.network.OKHttp;
import com.xidian.xienong.network.Url;
import com.xidian.xienong.util.Constants;
import com.xidian.xienong.util.SharePreferenceUtil;
import com.xidian.xienong.util.SweetAlert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Response;


public class AllCommentActivity extends AppCompatActivity {

	private List<CommentBean> lists = new ArrayList<CommentBean>();
	private List<String> content_lists = new ArrayList<String>();
	private TextView comprehensive_value;
	private ListView list;
	private CommentAdapter adapter;
	private View headerView;
	private SharePreferenceUtil sp;
	private RequestQueue requestQueue;
	private String comprehensive_evaluation="";
	private LinearLayout evaluate_1,evaluate_2;
	private TextView label1,label2,label3,label4,label5;
	private List<TextView> views = new ArrayList<TextView>();
	private String whichActivity="";
	private String id="";
    private Toolbar mToolbar;
	private OKHttp httpUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_comment_activity);
		setTitle("全部评论");
		/*showAllCommentViews();*/
		initViews();
		initDatas();
		initEvents();
		RequestComment();

	}

	private void initEvents() {
		mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void RequestComment() {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		if(whichActivity.equals("detail_activity")){
			id = getIntent().getStringExtra("id");
			map.put("worker_id", id);
		}else{
			map.put("worker_id", sp.getWorkerId());
		}

		httpUrl.post(Url.GetAllComment,map,new BaseCallback<String>(){
			@Override
			public void onRequestBefore() {

			}

			@Override
			public void onFailure(okhttp3.Request request, Exception e) {

			}

			@Override
			public void onSuccess(Response response, String resultResponse) {
				Log.i("kmj", "result : " + resultResponse);

					parseResponse(resultResponse);

			}

			@Override
			public void onError(Response response, int errorCode, Exception e) {
				Log.i("kmj", "error : " + e.toString());
			}
		});




	}

	protected void parseResponse(String response) {
		// TODO Auto-generated method stub
		try {
			JSONObject jb = new JSONObject(response);
			String result = jb.getString("reCode");
			comprehensive_evaluation = jb.getString("comprehensive_evaluation");
			if(result.equals("SUCCESS")){
				JSONArray content = jb.getJSONArray("contentList");
				for (int k = 0; k < content.length(); k++) {
					content_lists.add(((JSONObject)content.get(k)).getString("comment_content"));
				}
				JSONArray ja = jb.getJSONArray("commentList");
				for (int i = 0; i < ja.length(); i++) {
					JSONObject jo = (JSONObject) ja.get(i);
					CommentBean cb = new CommentBean();
					cb.setComment_content(jo.getString("comment_content"));
					cb.setComment_date(jo.getString("comment_date"));
					cb.setInput_content(jo.getString("input_content"));
					cb.setComment_id(jo.getString("comment_id"));
					cb.setServeValue(jo.getString("serve_value"));
					cb.setFarmer_name(jo.getString("farmer_name"));
					cb.setFarmer_headPhoto(jo.getString("farmer_headphoto"));
					lists.add(cb);
				}

				if(lists.size() == 0){
					headerView.setVisibility(View.GONE);
					list.setVisibility(View.GONE);
					SweetAlert.ToastWarningConfirmDialog(AllCommentActivity.this);
				}else{
					Collections.sort(lists);
					headerView.setVisibility(View.VISIBLE);
					list.setVisibility(View.VISIBLE);
					for(int i=0; i < content_lists.size(); i ++){
						views.get(i).setVisibility(View.VISIBLE);
						views.get(i).setText(content_lists.get(i));
					}
					comprehensive_value.setText("综合评价:  " + comprehensive_evaluation);
				}


			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		adapter.notifyDataSetChanged();
	}

	private void initDatas() {
		// TODO Auto-generated method stub
		setSupportActionBar(mToolbar);
		getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		sp = new SharePreferenceUtil(getApplicationContext(), Constants.SAVE_USER);
		requestQueue = Volley.newRequestQueue(getApplicationContext());
		whichActivity = getIntent().getStringExtra("flag");
		httpUrl = OKHttp.getInstance();
	}

	private void initViews() {
		// TODO Auto-generated method stub
		list = (ListView) findViewById(R.id.comment_list);
		headerView = (View)LayoutInflater.from(getApplicationContext()).inflate(R.layout.comment_header, null);
		evaluate_1 = (LinearLayout)headerView.findViewById(R.id.ll_evaluate_1);
		evaluate_2 = (LinearLayout)headerView.findViewById(R.id.ll_evaluate_2);
		label1 = (TextView)headerView.findViewById(R.id.label1);
		label2 = (TextView)headerView.findViewById(R.id.label2);
		label3 = (TextView)headerView.findViewById(R.id.label3);
		label4 = (TextView)headerView.findViewById(R.id.label4);
		label5 = (TextView)headerView.findViewById(R.id.label5);
		mToolbar = (Toolbar)findViewById(R.id.comment_detail_toolbar);
		views.add(label1);
		views.add(label2);
		views.add(label3);
		views.add(label4);
		views.add(label5);
		comprehensive_value = (TextView)headerView.findViewById(R.id.comprehensive_value);

		list.addHeaderView(headerView);
		adapter = new CommentAdapter(lists, getApplicationContext());
		list.setAdapter(adapter);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}

}
