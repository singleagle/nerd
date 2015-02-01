package com.enjoy.nerd.home;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.enjoy.nerd.AccountManager;
import com.enjoy.nerd.R;
import com.enjoy.nerd.distraction.DATagSelectActivity;
import com.enjoy.nerd.remoterequest.Account;
import com.enjoy.nerd.remoterequest.AddDistractionReq;
import com.enjoy.nerd.remoterequest.DATagReq.DATag;
import com.enjoy.nerd.remoterequest.RemoteRequest.FailResponseListner;
import com.enjoy.nerd.remoterequest.RemoteRequest.SuccessResponseListner;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreateDistractionFragment extends Fragment implements SuccessResponseListner<String>, FailResponseListner,
								View.OnClickListener, OnDateSetListener{
	static private final int MENU_ID_PUBLISH = 0;
	
	static private final int ADD_DA_ID = 1;
	
	static private final int REQ_CODE_SELECT_DATYPE = 1;
	
	private TextView mTimeView;
	private EditText mDestinationView;
	private View     mDATypeContainer;
	private TextView mDATypeView;
	private EditText mDescriptionView;
	private DatePickerDialog mDatePickerDialog;
	private DATag  mSelectedType;
	
	private final SimpleDateFormat DATAFORMAT = new SimpleDateFormat("yy-MM-dd");
	
	@Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setHasOptionsMenu(true);
    }
	  
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.create_distraction, container, false);
    	mDescriptionView = (EditText)view.findViewById(R.id.description);
    	mDATypeContainer = view.findViewById(R.id.type_container);
    	mDATypeContainer.setOnClickListener(this);
    	mDATypeView = (TextView)view.findViewById(R.id.type);
    	mTimeView = (TextView)view.findViewById(R.id.time);
    	mTimeView.setText(DATAFORMAT.format(Calendar.getInstance().getTime()));
    	mTimeView.setOnClickListener(this);
    	mDestinationView = (EditText)view.findViewById(R.id.destination);
		getActivity().getActionBar().setTitle(R.string.create_distraction);
    	return view;
    }

	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		case R.id.time:
            final Calendar calendar = Calendar.getInstance();
            if(mDatePickerDialog == null){
                mDatePickerDialog = new DatePickerDialog(
                        getActivity(),
                        this,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
            }else{
            	//TODO:
            	//mDatePickerDialog.updateDate(year, monthOfYear, dayOfMonth);
            }
            mDatePickerDialog.show();
			break;
			
		case R.id.type_container:
			Intent intent = new Intent(getActivity(), DATagSelectActivity.class);
			startActivityForResult(intent, REQ_CODE_SELECT_DATYPE);
			
			break;
			
		default:
			break;
		}
		
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(REQ_CODE_SELECT_DATYPE == requestCode && resultCode == Activity.RESULT_OK){
			mSelectedType = data.getParcelableExtra(DATagSelectActivity.DATAG);
			if(mSelectedType != null){
				mDATypeView.setText(mSelectedType.getName());
			}
		}
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		Calendar calendar =  Calendar.getInstance();
		calendar.set(year, monthOfYear, dayOfMonth, 0, 0);
		mTimeView.setText(DATAFORMAT.format(calendar.getTime()));
	}
    
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	super.onCreateOptionsMenu(menu, inflater);
    	MenuItem publish = menu.add(0, MENU_ID_PUBLISH, 0, R.string.publish);
    	publish.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    private void sendAddDAReq(){
    	AddDistractionReq request = new AddDistractionReq(getActivity());
    	request.registerListener(ADD_DA_ID, this, this);
    	request.setDescription(mDescriptionView.getText().toString());
    	request.setPayType(AddDistractionReq.PAYTYPE_AA);
    	request.setCreatUserId(AccountManager.getInstance(getActivity()).getLoginUIN());
    	request.setType(mSelectedType.getId());
    	
    	if(mDestinationView.getText() != null){
    		request.setAddress(mDestinationView.getText().toString());
    	}
    	
    	long startTime = 0;
		try {
			startTime = DATAFORMAT.parse(mTimeView.getText().toString()).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	request.setStartTime(startTime);
    	request.submit();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()){
    	case MENU_ID_PUBLISH:
    		if(!AccountManager.getInstance(getActivity()).isLogin()){
    			Toast.makeText(getActivity(), R.string.login_tips, Toast.LENGTH_LONG).show();
    		}else if(mSelectedType == null || mDescriptionView.getText() == null){
    			Toast.makeText(getActivity(), R.string.invalidate_input, Toast.LENGTH_LONG).show();
    		}else{
    			sendAddDAReq();
    		}
    		
    		break;
    	
    	default:
    		break;
    	}
    	return super.onOptionsItemSelected(item);
    }
	@Override
    public void onDetach() {
        super.onDetach();
        if(mDatePickerDialog != null){
        	mDatePickerDialog.dismiss();
        }
    }

	@Override
	public void onFailure(int requestId, int error, int subErr,
			String errDescription) {
		Toast.makeText(getActivity(), errDescription + ":" + error, Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public void onSucess(int requestId, String response) {
		Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
	}
    
    
}
