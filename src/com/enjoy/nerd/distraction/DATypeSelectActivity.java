package com.enjoy.nerd.distraction;

import java.util.ArrayList;

import com.enjoy.nerd.R;
import com.enjoy.nerd.remoterequest.DATypeReq;
import com.enjoy.nerd.remoterequest.DATypeReq.DAType;
import com.enjoy.nerd.remoterequest.RemoteRequest.FailResponseListner;
import com.enjoy.nerd.remoterequest.RemoteRequest.SuccessResponseListner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class DATypeSelectActivity extends Activity implements OnClickListener, FailResponseListner,
										SuccessResponseListner<ArrayList<DAType>>, OnItemClickListener {
	
	public static final String DATYPE = "datype";
	private static final int REQ_ID_TYPE = 1;
	
	private static final int REQ_CODE_ADDTYPE = 1;
	
	private ListView mListView;
	private ImageView mAddImg;
	private DATypeAdapter mAdapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datype_list);
        mListView = (ListView)findViewById(R.id.type_list);
        mAddImg = (ImageView)findViewById(R.id.type_add);
        mAddImg.setOnClickListener(this);
        requestDATypeList();
    }

    private void requestDATypeList(){
    	DATypeReq request = new DATypeReq(this);
    	request.registerListener(REQ_ID_TYPE, this, this);
    	request.submit();
    }
    
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQ_CODE_ADDTYPE && resultCode == RESULT_OK){
			requestDATypeList();
		}
	}
    
	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this, AddDATypeActivity.class);
		startActivityForResult(intent, REQ_CODE_ADDTYPE);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		DAType type = (DAType) mAdapter.getItem(position);
		Intent data = new Intent();
		data.putExtra(DATYPE, type);
		setResult(RESULT_OK, data);
		finish();
	}

	@Override
	public void onFailure(int requestId, int error, int subErr, String errDescription) {
		
	}

	@Override
	public void onSucess(int requestId, ArrayList<DAType> response) {
		mAdapter = new DATypeAdapter(this, response);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
	}
	
	private static class DATypeAdapter extends BaseAdapter{
		private ArrayList<DAType> mTypeList;
		private Context mContext;
		
		
		public DATypeAdapter(Context context,  ArrayList<DAType> typeList ){
			mContext = context;
			mTypeList = (ArrayList<DAType>) typeList.clone();
		}
		
		@Override
		public int getCount() {
			return mTypeList.size();
		}

		@Override
		public Object getItem(int position) {
			return mTypeList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			DAType type = (DAType) getItem(position);
			if(type == null){
				return null;
			}
			
			if(convertView == null){
				convertView = LayoutInflater.from(mContext).inflate(R.layout.datype_item, parent, false);
			}
			TextView typeView = (TextView) convertView.findViewById(R.id.type_name);
			typeView.setText(type.getSubTypeName());
			return convertView;
		}
		
	}

}
