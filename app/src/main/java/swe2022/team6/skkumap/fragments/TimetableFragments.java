package swe2022.team6.skkumap.fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.snackbar.Snackbar;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;

import swe2022.team6.skkumap.R;
import swe2022.team6.skkumap.dataclasses.Class;
import swe2022.team6.skkumap.adapters.ClassAdapter;
import swe2022.team6.skkumap.databinding.FragmentTimetableFragmentsBinding;
import swe2022.team6.skkumap.dataclasses.Owner;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimetableFragments#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimetableFragments extends Fragment
                                implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FragmentTimetableFragmentsBinding binding;
    AlertDialog alertDialog;
    //ClassAdapter<Class> adapter;

    private String name;
    private String classroom;
    private int day;
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;
    private final Owner owner = Owner.getInstance();

    EditText etName;
    EditText etClassroom;
    Spinner daySpinner;
    Spinner startHourSpinner;
    Spinner startMinuteSpinner;
    Spinner endHourSpinner;
    Spinner endMinuteSpinner;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TimetableFragments() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TimetableFragments.
     */
    // TODO: Rename and change types and number of parameters
    public static TimetableFragments newInstance(String param1, String param2) {
        TimetableFragments fragment = new TimetableFragments();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_addclass_dialog, null);
        builder.setView(v);
        alertDialog = builder.create();

        etName = (EditText) v.findViewById(R.id.etAddClassName);
        etClassroom = (EditText) v.findViewById(R.id.etAddClassClassroom);

        daySpinner = (Spinner) v.findViewById(R.id.spAddClassDay);
        startHourSpinner = (Spinner) v.findViewById(R.id.spAddClassStartHour);
        startMinuteSpinner = (Spinner) v.findViewById(R.id.spAddClassStartMinute);
        endHourSpinner = (Spinner) v.findViewById(R.id.spAddClassEndHour);
        endMinuteSpinner = (Spinner) v.findViewById(R.id.spAddClassEndMinute);
        ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter.createFromResource(v.getContext(), R.array.days, android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(dayAdapter);
        ArrayAdapter<CharSequence> hourAdapter = ArrayAdapter.createFromResource(v.getContext(), R.array.hours, android.R.layout.simple_spinner_dropdown_item);
        startHourSpinner.setAdapter(hourAdapter);
        endHourSpinner.setAdapter(hourAdapter);
        ArrayAdapter<CharSequence> minuteAdapter = ArrayAdapter.createFromResource(v.getContext(), R.array.minutes, android.R.layout.simple_spinner_dropdown_item);
        startMinuteSpinner.setAdapter(minuteAdapter);
        endMinuteSpinner.setAdapter(minuteAdapter);

        AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int value = 0;
                try {
                    value = Integer.parseInt(parent.getItemAtPosition(position).toString());
                }
                catch (NumberFormatException e) {
                    day = 0;
                }

                switch (parent.getId()) {
                    case R.id.spAddClassDay:
                        day = value;
                        break;
                    case R.id.spAddClassStartHour:
                        startHour = value;
                        break;
                    case R.id.spAddClassStartMinute:
                        startMinute = value;
                        break;
                    case R.id.spAddClassEndHour:
                        endHour = value;
                        break;
                    case R.id.spAddClassEndMinute:
                        endMinute = value;
                        break;
                    default:
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };

        daySpinner.setOnItemSelectedListener(spinnerListener);
        startHourSpinner.setOnItemSelectedListener(spinnerListener);
        startMinuteSpinner.setOnItemSelectedListener(spinnerListener);
        endHourSpinner.setOnItemSelectedListener(spinnerListener);
        endMinuteSpinner.setOnItemSelectedListener(spinnerListener);

        Button btnAdd = (Button) v.findViewById(R.id.btnAddClassAdd);
        btnAdd.setOnClickListener(view -> {
            name = etName.getText().toString();
            classroom = etClassroom.getText().toString();
            Snackbar.make(binding.getRoot(), "Name: " + name + '\n'
                                                     + "Classroom: " + classroom + '\n'
                                                     + "Starts at " + startHour + ':' + startMinute + '\n'
                                                     + "Ends at " + endHour + ':' + endMinute,
                         Snackbar.LENGTH_LONG).setTextMaxLines(40).show();
            owner.addClassToTimeTable(name, classroom, day, startHour, startMinute, endHour, endMinute);
            //adapter.notifyItemChanged(adapter.getItemCount());
            alertDialog.dismiss();
        });
        Button btnCancel = (Button) v.findViewById(R.id.btnAddClassCancel);
        btnCancel.setOnClickListener(view -> {
            alertDialog.dismiss();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTimetableFragmentsBinding.inflate(inflater, container, false);
        binding.btnAddClass.setOnClickListener(this);

        /*
        classList = new ArrayList<Class>();

        adapter = new ClassAdapter<>(classList);
        binding.rvTimeTable.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        binding.rvTimeTable.setLayoutManager(llm);
         */

        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddClass:
                alertDialog.show();
                break;
            default:
        }
    }
}