package swe2022.team6.skkumap.fragments;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import swe2022.team6.skkumap.R;
import swe2022.team6.skkumap.databinding.FragmentSettingFragmentsBinding;
import swe2022.team6.skkumap.databinding.FragmentTimetableFragmentsBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragments#newInstance} factory method to
 * create an instance of this fragment.
 */


public class SettingFragments extends Fragment {
    FragmentSettingFragmentsBinding binding;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingFragments() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragments.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragments newInstance(String param1, String param2) {
        SettingFragments fragment = new SettingFragments();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    AlertDialog alertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_setting_fragments, null);


    }

    void activateUI(boolean active) {
        binding.tpNoti.setEnabled(active);
        binding.swtchNotiLoc.setEnabled(active);
        binding.rgNotificationMethod.setEnabled(active);
        binding.rgNotificationMethod.getChildAt(0).setEnabled(active);
        binding.rgNotificationMethod.getChildAt(1).setEnabled(active);
        binding.rgNotificationMethod.getChildAt(2).setEnabled(active);
        int textColor;

        textColor = active ? Color.DKGRAY : Color.LTGRAY;

        binding.tvSettingDisplay2.setTextColor(textColor);
        binding.tvSettingDisplay3.setTextColor(textColor);
        binding.tvBefore.setTextColor(textColor);

    }

    void tvBefore(boolean loc) {
        if (loc)
            binding.tvBefore.setText("before departure");
        else
            binding.tvBefore.setText("before classtime");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSettingFragmentsBinding.inflate(inflater, container, false);
        binding.tpNoti.setIs24HourView(true);

        //TODO Firebase에서 불러오기

        //activate 돼있는지에 따라 세부 설정 enable/disable
        activateUI(binding.swtchNotiActivate.isChecked());
        binding.swtchNotiActivate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                                 @Override
                                                                 public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                                                     activateUI(b);
                                                                 }
                                                             }
        );

        //location based 하는지에 따라 tv 텍스트 바꾸기
        tvBefore(binding.swtchNotiLoc.isChecked());
        binding.swtchNotiLoc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                            @Override
                                                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                                                tvBefore(b);
                                                            }
                                                        }
        );

        //초기 시간 설정
        binding.tpNoti.setHour(0);
        binding.tpNoti.setMinute(15);

        return binding.getRoot();
    }
}