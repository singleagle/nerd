package com.enjoy.nerd.home;

import com.enjoy.nerd.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class UserCenterFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	getActivity().getActionBar().setTitle(R.string.user_center);
    	return inflater.inflate(R.layout.user_center, container, false);
    }

}
