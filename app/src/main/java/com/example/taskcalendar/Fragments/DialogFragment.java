package com.example.taskcalendar.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.taskcalendar.MainActivity;
import com.example.taskcalendar.Model.EditTextViewModel;
import com.example.taskcalendar.R;
import com.example.taskcalendar.Utils.DatabaseHandler;

public class DialogFragment extends androidx.fragment.app.DialogFragment {

    private EditText editTextDialog;
    private String newDescription;
    private EditTextViewModel model;
    private String taskDescription;
    private DatabaseHandler db;
    private int position;

    // Required empty public constructor
    public DialogFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHandler(requireContext());
        db.openDatabase();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            taskDescription = bundle.getString("text");
            position = bundle.getInt("position");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //View Model
        model = new ViewModelProvider(requireActivity()).get(EditTextViewModel.class);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View inflator = layoutInflater.inflate(R.layout.fragment_dialog, null);   // View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_dialog,null);
        builder.setTitle("Edit your task description here !!!");
        editTextDialog = (EditText) inflator.findViewById(R.id.editTextDialog);
        editTextDialog.setText(taskDescription);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                newDescription = editTextDialog.getText().toString();
                if (!newDescription.isEmpty()) {
                    model.setTaskDescription(newDescription);
                    taskDescription = model.getTaskDescription();
                    db.updateTaskDescription(position, taskDescription);
                } else ;
                ((MainActivity) getActivity()).updateTask();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        builder.setView(inflator);
        return builder.create();
    }

}
