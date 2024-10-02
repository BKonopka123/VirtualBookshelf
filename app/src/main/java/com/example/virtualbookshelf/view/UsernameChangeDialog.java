package com.example.virtualbookshelf.view;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.virtualbookshelf.R;
import com.example.virtualbookshelf.model.User;
import com.example.virtualbookshelf.model.db.DBManager;
import com.example.virtualbookshelf.viewmodel.UserViewModel;

import java.util.Objects;

/**
 * A dialog fragment allowing the user to change their username. It validates the input, ensures that the username
 * is not empty and does not exceed 20 characters, then updates the username in the database.
 */
public class UsernameChangeDialog extends DialogFragment {

    /** The ViewModel for managing user-related data. */
    private final UserViewModel userViewModel;

    /** The DBManager used for interacting with the database. */
    private final DBManager dbManager;

    /**
     * Constructor for creating the UsernameChangeDialog.
     *
     * @param userViewModel The ViewModel responsible for user-related data.
     * @param dbManager The manager responsible for database operations.
     */
    public UsernameChangeDialog(UserViewModel userViewModel, DBManager dbManager) {
        this.userViewModel = userViewModel;
        this.dbManager = dbManager;
    }

    /**
     * Inflates the layout for the username change dialog.
     *
     * @param inflater The LayoutInflater object to inflate the layout.
     * @param container The container view group to host the dialog.
     * @param savedInstanceState The saved instance state from a previous dialog session.
     * @return The view of the dialog fragment.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_username_change, container, false);
    }

    /**
     * Creates the dialog and sets its properties, including the title and background.
     *
     * @param savedInstanceState The saved instance state from a previous dialog session.
     * @return The dialog instance.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle("Username Change");
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        return dialog;
    }

    /**
     * Called after the dialog view has been created. This method sets up the dialog's layout and its buttons' actions.
     * It validates the input for the username and updates the database if valid.
     *
     * @param view The view of the dialog.
     * @param savedInstanceState The saved instance state from a previous dialog session.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set the background dim effect for the dialog.
        if (getDialog() != null && getDialog().getWindow() != null) {
            WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
            layoutParams.dimAmount = 0.7f;
            getDialog().getWindow().setAttributes(layoutParams);
            getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }

        // Initialize UI elements.
        EditText editText = view.findViewById(R.id.usernameEditText);
        Button confirmButton = view.findViewById(R.id.usernameConfirmButton);
        Button cancelButton = view.findViewById(R.id.usernameCancelButton);

        // Confirm button action: Validate and update the username.
        confirmButton.setOnClickListener(v -> {
            String inputText = editText.getText().toString();
            if (inputText.isEmpty()) {
                Toast toast = Toast.makeText(getContext(), "Username cannot be empty", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                dismiss();
                return;
            }
            if (inputText.length() > 20) {
                Toast toast = Toast.makeText(getContext(), "Username cannot be longer than 20 characters", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                dismiss();
                return;
            }

            try {
                Cursor cursor = dbManager.getUserById(1);
                if (cursor.moveToFirst()) {
                    User user = new User(cursor.getInt(0), inputText, cursor.getBlob(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5), cursor.getInt(6), cursor.getInt(7));
                    dbManager.updateUser(user);
                }
                cursor.close();
            } catch (Exception e) {
                Log.e("UsernameChangeDialog", "Error updating username - " + e.getMessage(), e);
                Toast toast = Toast.makeText(getContext(), "Error updating username", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                dismiss();
                return;
            }

            userViewModel.refreshUserData();
            dismiss();
        });

        // Cancel button action: Dismiss the dialog.
        cancelButton.setOnClickListener(v -> dismiss());
    }
}
