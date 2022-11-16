package swe2022.team6.skkumap.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
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
import swe2022.team6.skkumap.dataclasses.Owner;
import swe2022.team6.skkumap.dataclasses.UserSetting;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragments#newInstance} factory method to
 * create an instance of this fragment.
 */


public class SettingFragments extends Fragment {
    FragmentSettingFragmentsBinding binding;
    private final Owner owner = Owner.getInstance();
    private final UserSetting us = owner.getmUs();


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
        //클래스 업데이트
        us.setNotiActivate(active);

        //UI 업데이트
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
        //클래스 업데이트
        us.setNotiLoc(loc);

        //UI 업데이트
        if (loc) {
            ActivityCompat.requestPermissions(owner.getmActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 0x0000001);
            binding.tvBefore.setText("before departure");
        } else
            binding.tvBefore.setText("before classtime");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        binding = FragmentSettingFragmentsBinding.inflate(inflater, container, false);
        binding.tpNoti.setIs24HourView(true);

        //TODO Firebase에서 클래스 불러오기

        //notiActivate 초기 설정
        binding.swtchNotiActivate.setChecked(us.isNotiActivate());
        //activate 돼있는지에 따라 세부 설정 enable/disable, 클래스 업데이트
        activateUI(binding.swtchNotiActivate.isChecked());
        binding.swtchNotiActivate.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        activateUI(b);
                    }
                }
        );

        //location based 초기 설정
        binding.swtchNotiActivate.setChecked(us.isNotiLoc());
        //location based 하는지에 따라 tv 텍스트 바꾸기, 클래스 업데이트
        tvBefore(binding.swtchNotiLoc.isChecked());
        binding.swtchNotiLoc.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        tvBefore(b);
                    }
                }
        );

        //시간 초기 설정
        binding.tpNoti.setHour(us.getNotiHr());
        binding.tpNoti.setMinute(us.getNotiMin());
        //시간 바꼇을때 클래스 업데이트
        binding.tpNoti.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                us.setNotiHr(hourOfDay);
                us.setNotiMin(minute);
            }
        });

        //알림 method 초기 설정
        ((RadioButton) binding.rgNotificationMethod.getChildAt(us.getNotiMthd())).setChecked(true);
        //알림 method 바꼇을 때 클래스 업데이트
        binding.rgNotificationMethod.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        us.setNotiMthd(binding.rgNotificationMethod.indexOfChild(binding.getRoot().findViewById(i)));
                        if(us.getNotiMthd()==2) {
                            //푸시알림 권한 (높은 API에서만 필요한가봄)
                            ActivityCompat.requestPermissions(owner.getmActivity(), new String[]{Manifest.permission.POST_NOTIFICATIONS}, 0x0000001);
                        }
                    }
                }

        );
        return binding.getRoot();
    }
}