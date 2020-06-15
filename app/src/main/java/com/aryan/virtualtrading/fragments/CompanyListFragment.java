package com.aryan.virtualtrading.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aryan.virtualtrading.R;

public class CompanyListFragment extends Fragment {

    public static boolean market = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_company_list, container, false);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if(market){
            CompanyDetailFragment companyDetailFragment = new CompanyDetailFragment();
            fragmentTransaction.add(R.id.fragmentCompanyContainer, companyDetailFragment);
            fragmentTransaction.commit();
            market = false;
        }
        else{
            MarketFragment marketFragment = new MarketFragment();
            fragmentTransaction.add(R.id.fragmentCompanyContainer, marketFragment);
            fragmentTransaction.commit();
            market = true;
        }
        return root;
    }
}
