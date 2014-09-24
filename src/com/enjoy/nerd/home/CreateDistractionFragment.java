package com.enjoy.nerd.home;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.enjoy.nerd.R;
import com.enjoy.nerd.remoterequest.AddDistractionReq;
import com.enjoy.nerd.remoterequest.RemoteRequest.FailResponseListner;
import com.enjoy.nerd.remoterequest.RemoteRequest.SuccessResponseListner;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
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
	
	static private final int ADD_DA_ID = 0;
	
	private TextView mTimeView;
	private TextView mDestinationView;
	private TextView mDATypeView;
	private EditText mDescriptionView;
	DatePickerDialog mDatePickerDialog;
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
    	mDATypeView = (TextView)view.findViewById(R.id.type);
    	mDATypeView.setOnClickListener(this);
    	mTimeView = (TextView)view.findViewById(R.id.time);
    	mTimeView.setText(DATAFORMAT.format(Calendar.getInstance().getTime()));
    	mTimeView.setOnClickListener(this);
    	mDestinationView = (TextView)view.findViewById(R.id.destination);
    	mDestinationView.setOnClickListener(this);
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
			
		default:
			break;
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
    	request.setCreatUserId(123L);
    	request.setType(0x10001);
    	request.setAddress("深圳市南山区西丽镇欧陆经典小区");
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
    		sendAddDAReq();
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
