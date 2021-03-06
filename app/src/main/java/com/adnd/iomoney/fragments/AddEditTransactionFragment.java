package com.adnd.iomoney.fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adnd.iomoney.PickLocationActivity;
import com.adnd.iomoney.adapters.BindingAdapters;
import com.adnd.iomoney.databinding.FragmentAddEditTransactionBinding;
import com.adnd.iomoney.dialogs.AccountPickerDialog;
import com.adnd.iomoney.dialogs.DatePickerFragment;
import com.adnd.iomoney.models.Account;
import com.adnd.iomoney.models.Transaction;
import com.adnd.iomoney.view_models.AddEditTransactionViewModel;

import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class AddEditTransactionFragment extends Fragment {

    private AddEditTransactionViewModel model;
    private FragmentAddEditTransactionBinding binding;

    public static AddEditTransactionFragment newInstance() {
        return new AddEditTransactionFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAddEditTransactionBinding.inflate(inflater);

        if (getActivity() != null) {
            model = ViewModelProviders.of(getActivity()).get(AddEditTransactionViewModel.class);

            model.getTransactionLiveData().observe(getActivity(), new Observer<Transaction>() {
                @Override
                public void onChanged(@Nullable Transaction transaction) {
                    binding.setTransaction(transaction);
                }
            });

            model.getSelectedAccountLiveData().observe(getActivity(), new Observer<Account>() {
                @Override
                public void onChanged(@Nullable Account account) {
                    binding.setSelectedAccount(account);
                }
            });

            final LiveData<List<Account>> accountsLiveData = model.getAccountsLiveData();
            accountsLiveData.observe(getActivity(), new Observer<List<Account>>() {
                @Override
                public void onChanged(@Nullable List<Account> accounts) {
                    // Dummy call in order to trigger initial account selection
                    accountsLiveData.removeObserver(this);
                }
            });

            binding.etDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Date date = BindingAdapters.getDateFromText(binding.etDate.getText().toString());
                    DialogFragment datePickerFragment = DatePickerFragment.newInstance(date.getTime());
                    datePickerFragment.show(getActivity().getSupportFragmentManager()
                            , "date_picker_fragment");
                }
            });

            binding.etAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AccountPickerDialog accountPickerDialog = AccountPickerDialog.newInstance();
                    accountPickerDialog.show(getActivity().getSupportFragmentManager(), "account_picker_dialog");
                }
            });

            binding.btPickFromMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), PickLocationActivity.class);
                    intent.putExtra(PickLocationActivity.EXTRA_LOCATION_LABEL, binding.getTransaction().getLocationLabel());
                    if (!binding.getTransaction().hasNoCoordinates()) {
                        intent.putExtra(PickLocationActivity.EXTRA_LATITUDE, binding.getTransaction().getLat());
                        intent.putExtra(PickLocationActivity.EXTRA_LONGITUDE, binding.getTransaction().getLon());
                    }
                    startActivityForResult(intent, PickLocationActivity.PICK_LOCATION_REQUEST_CODE);
                }
            });

        }

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PickLocationActivity.PICK_LOCATION_REQUEST_CODE) {
            String locationLabel = data.getStringExtra(PickLocationActivity.EXTRA_LOCATION_LABEL);
            Double lat = data.getDoubleExtra(PickLocationActivity.EXTRA_LATITUDE, 0);
            Double lon = data.getDoubleExtra(PickLocationActivity.EXTRA_LONGITUDE, 0);
            binding.getTransaction().setLocationLabel(locationLabel);
            binding.getTransaction().setLat(lat);
            binding.getTransaction().setLon(lon);
        }
    }
}
