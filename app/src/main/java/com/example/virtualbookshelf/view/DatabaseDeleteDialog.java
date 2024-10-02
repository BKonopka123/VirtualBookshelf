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

/**
 * DialogFragment for confirming the deletion of the database. This dialog provides the user with
 * an option to confirm or cancel the database reset operation.
 */
public class DatabaseDeleteDialog extends DialogFragment {

    /** The view model associated with the UserActivity. */
    private final UserViewModel userViewModel;

    /** The database manager used to interact with the database. */
    private final DBManager dbManager;

    /**
     * Constructor for the DatabaseDeleteDialog.
     *
     * @param userViewModel The view model managing user-related data.
     * @param dbManager The database manager responsible for database operations.
     */
    public DatabaseDeleteDialog(UserViewModel userViewModel, DBManager dbManager) {
        this.userViewModel = userViewModel;
        this.dbManager = dbManager;
    }

    /**
     * Inflates the dialog layout.
     *
     * @param inflater The LayoutInflater used to inflate the dialog's layout.
     * @param container The parent view that the dialog's layout will be attached to.
     * @param savedInstanceState If the dialog is being re-created, this bundle contains the saved state.
     * @return The root view of the dialog's layout.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_database_delete, container, false);
    }

    /**
     * Called when the dialog is created. This method sets the dialog's title and background.
     *
     * @param savedInstanceState If the dialog is being re-created, this bundle contains the saved state.
     * @return The created dialog instance.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle("Database delete");
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        return dialog;
    }

    /**
     * Called after the view has been created. This method adjusts the dialog's window settings,
     * including setting the dim amount for the background and adding a flag to dim the background.
     *
     * @param view The root view of the dialog's layout.
     * @param savedInstanceState If the dialog is being re-created, this bundle contains the saved state.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getDialog() != null && getDialog().getWindow() != null) {
            WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
            layoutParams.dimAmount = 0.7f;
            getDialog().getWindow().setAttributes(layoutParams);
            getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }

        // Get the "Confirm" and "Cancel" buttons from the layout.
        Button confirmButton = view.findViewById(R.id.databaseDeleteConfirmButton);
        Button cancelButton = view.findViewById(R.id.databaseDeleteCancelButton);

        // Set click listener for the "Confirm" button to reset the database and refresh user data.
        confirmButton.setOnClickListener(v -> {
            dbManager.reset();
            dbManager.mockData(); // TODO: DELETE
            userViewModel.refreshUserData();
            dismiss();
        });

        // Set click listener for the "Cancel" button to dismiss the dialog without any changes.
        cancelButton.setOnClickListener(v -> dismiss());
    }
}
