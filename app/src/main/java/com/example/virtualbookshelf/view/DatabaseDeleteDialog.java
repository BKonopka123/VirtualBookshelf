package com.example.virtualbookshelf.view;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.virtualbookshelf.R;
import com.example.virtualbookshelf.model.db.DBManager;
import com.example.virtualbookshelf.viewmodel.UserViewModel;

import java.util.Objects;

public class DatabaseDeleteDialog extends DialogFragment {

    private final UserViewModel userViewModel;
    private final DBManager dbManager;

    public DatabaseDeleteDialog(UserViewModel userViewModel, DBManager dbManager) {
        this.userViewModel = userViewModel;
        this.dbManager = dbManager;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_database_delete, container, false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle("Database delete");
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        return dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getDialog() != null && getDialog().getWindow() != null) {
            WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
            layoutParams.dimAmount = 0.7f;
            getDialog().getWindow().setAttributes(layoutParams);
            getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }

        Button confirmButton = view.findViewById(R.id.databaseDeleteConfirmButton);
        Button cancelButton = view.findViewById(R.id.databaseDeleteCancelButton);

        confirmButton.setOnClickListener(v -> {
            dbManager.reset();
            dbManager.mockData(); // TODO: DELETE
            userViewModel.refreshUserData();
            dismiss();
        });

        cancelButton.setOnClickListener(v -> dismiss());
    }
}
