package com.example.virtualbookshelf.view;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.virtualbookshelf.R;
import com.example.virtualbookshelf.model.User;
import com.example.virtualbookshelf.model.db.DBManager;
import com.example.virtualbookshelf.viewmodel.UserViewModel;

import java.util.Objects;

public class UsernameChangeDialog extends DialogFragment {

    private final UserViewModel userViewModel;
    private final DBManager dbManager;

    public UsernameChangeDialog(UserViewModel userViewModel, DBManager dbManager) {
        this.userViewModel = userViewModel;
        this.dbManager = dbManager;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_username_change, container, false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle("Custom Dialog");
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

        EditText editText = view.findViewById(R.id.usernameEditText);
        Button confirmButton = view.findViewById(R.id.usernameConfirmButton);
        Button cancelButton = view.findViewById(R.id.usernameCancelButton);

        confirmButton.setOnClickListener(v -> {
            String inputText = editText.getText().toString();
            Cursor cursor = dbManager.getUserById(1);
            if(cursor.moveToFirst()){
                User user = new User(cursor.getInt(0), inputText, cursor.getBlob(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5), cursor.getInt(6), cursor.getInt(7));
                dbManager.updateUser(user);
            }
            cursor.close();
            userViewModel.refreshUserData();
            dismiss();
        });

        cancelButton.setOnClickListener(v -> dismiss());
    }
}
