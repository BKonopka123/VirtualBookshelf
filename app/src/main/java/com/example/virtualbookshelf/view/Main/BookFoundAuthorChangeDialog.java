package com.example.virtualbookshelf.view.Main;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
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
import com.example.virtualbookshelf.model.Book;
import com.example.virtualbookshelf.model.db.DBManager;
import com.example.virtualbookshelf.viewmodel.Main.BookFoundDetailViewModel;

import java.util.Objects;

/**
 * This class represents a dialog fragment for changing the author of a book.
 */
public class BookFoundAuthorChangeDialog extends DialogFragment {

    /**
     * Interface for handling the change of the book's author.
     */
    public interface OnBookFoundAuthorChangeListener {
        void onFoundAuthorChanged(Book updatedBook);
    }

    /**
     * The listener for handling the change of the book's author.
     */
    private OnBookFoundAuthorChangeListener foundAuthorChangeListener;

    /**
     * The view model for managing the UI-related data and logic.
     */
    private final BookFoundDetailViewModel bookFoundDetailViewModel;

    /**
     * The database manager for managing the database operations.
     */
    private final DBManager dbManager;

    /**
     * The book to be updated.
     */
    private Book bookToUpdate;

    /**
     * Constructs a new instance of the dialog fragment.
     * @param bookFoundDetailViewModel view model for the book found detail screen
     * @param dbManager database manager for accessing the database
     * @param bookToUpdate book to be updated
     */
    public BookFoundAuthorChangeDialog(BookFoundDetailViewModel bookFoundDetailViewModel, DBManager dbManager, Book bookToUpdate) {
        this.bookFoundDetailViewModel = bookFoundDetailViewModel;
        this.dbManager = dbManager;
        this.bookToUpdate = bookToUpdate;
    }

    /**
     * Called when the fragment is attached to an activity.
     * @param context the activity context
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            foundAuthorChangeListener = (BookFoundAuthorChangeDialog.OnBookFoundAuthorChangeListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Host activity must implement OnBookFoundAuthorChangeListener");
        }
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return Return the View for the dialog's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_book_author_change, container, false);
    }

    /**
     * Called when the dialog is created.
     * @param savedInstanceState The last saved instance state of the Fragment,
     * or null if this is a freshly created Fragment.
     *
     * @return Return a new Dialog instance to be displayed by the Fragment.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle("Found Book Author Change");
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        return dialog;
    }

    /**
     * Called when the activity is starting. This is where most initialization should go.
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set the background color of the dialog to transparent.
        if (getDialog() != null && getDialog().getWindow() != null) {
            WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
            layoutParams.dimAmount = 0.7f;
            getDialog().getWindow().setAttributes(layoutParams);
            getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }

        // Initialize the UI elements of the dialog
        EditText editText = view.findViewById(R.id.bookAuthorEditText_found);
        Button confirmButton = view.findViewById(R.id.bookAuthorConfirmButton_found);
        Button cancelButton = view.findViewById(R.id.bookAuthorCancelButton_found);

        // Set click listeners for the buttons to update the book's status in the database

        confirmButton.setOnClickListener(v -> {
            String inputText = editText.getText().toString();
            if (inputText.isEmpty()) {
                Toast toast = Toast.makeText(getContext(), "Author cannot be empty", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                dismiss();
                return;
            }
            if (inputText.length() > 30) {
                Toast toast = Toast.makeText(getContext(), "Author cannot be longer than 30 characters", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                dismiss();
                return;
            }
            bookToUpdate.setAuthor(inputText);
            foundAuthorChangeListener.onFoundAuthorChanged(bookToUpdate);
            dismiss();
        });

        cancelButton.setOnClickListener(v -> dismiss());
    }
}
