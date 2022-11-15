package swe2022.team6.skkumap.fragments;

import android.app.AlertDialog;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentSettingFragmentsBinding.inflate(inflater, container, false);
        binding.tpNoti.setIs24HourView(true);
        binding.swtchNotiActivate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                      @Override
                                                      public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                                          Log.e("TAG", "switch checked");
                                                          Toast.makeText(getActivity().getApplicationContext(), "switch", Toast.LENGTH_SHORT).show();
                                                          if(b){

                                                          }
                                                          else{

                                                          }
                                                      }
                                                  }

        );
//        return inflater.inflate(R.layout.fragment_setting_fragments, container, false);
        return binding.getRoot();
    }
}