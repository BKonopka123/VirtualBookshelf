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
 * Dialog fragment for changing the title of a found book.
 */
public class BookFoundTitleChangeDialog extends DialogFragment {

    /**
     * Interface for listening to changes in the found book's title.
     */
    public interface OnBookFoundTitleChangeListener {
        void onFoundTitleChanged(Book updatedBook);
    }

    /**
     * Instance of the OnBookFoundTitleChangeListener interface.
     */
    private OnBookFoundTitleChangeListener foundTitleChangeListener;

    /**
     * Instance of the BookFoundDetailViewModel.
     */
    private final BookFoundDetailViewModel bookFoundDetailViewModel;

    /**
     * Instance of the DBManager.
     */
    private final DBManager dbManager;

    /**
     * Book to be updated.
     */
    private Book bookToUpdate;

    /**
     * Constructor for the BookFoundTitleChangeDialog class.
     * @param bookFoundDetailViewModel Instance of the BookFoundDetailViewModel.
     * @param dbManager Instance of the DBManager.
     * @param bookToUpdate Book to be updated.
     */
    public BookFoundTitleChangeDialog(BookFoundDetailViewModel bookFoundDetailViewModel, DBManager dbManager, Book bookToUpdate) {
        this.bookFoundDetailViewModel = bookFoundDetailViewModel;
        this.dbManager = dbManager;
        this.bookToUpdate = bookToUpdate;
    }

    /**
     * Called when the fragment is attached to an activity.
     * @param context Context in which the fragment is running.
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            foundTitleChangeListener = (BookFoundTitleChangeDialog.OnBookFoundTitleChangeListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Host activity must implement OnBookFoundTitleChangeListener");
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
        return inflater.inflate(R.layout.dialog_book_title_change, container, false);
    }

    /**
     * Called after the view has been created.
     * @param savedInstanceState The last saved instance state of the Fragment,
     * or null if this is a freshly created Fragment.
     *
     * @return Return a new Dialog instance to be displayed by the Fragment.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle("Found Book Title Change");
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
        EditText editText = view.findViewById(R.id.bookTitleEditText_found);
        Button confirmButton = view.findViewById(R.id.bookTitleConfirmButton_found);
        Button cancelButton = view.findViewById(R.id.bookTitleCancelButton_found);

        // Set click listeners for the buttons to update the book's status in the database

        confirmButton.setOnClickListener(v -> {
            String inputText = editText.getText().toString();
            if (inputText.isEmpty()) {
                Toast toast = Toast.makeText(getContext(), "Title cannot be empty", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                dismiss();
                return;
            }
            if (inputText.length() > 30) {
                Toast toast = Toast.makeText(getContext(), "Title cannot be longer than 30 characters", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                dismiss();
                return;
            }
            bookToUpdate.setTitle(inputText);
            foundTitleChangeListener.onFoundTitleChanged(bookToUpdate);
            dismiss();
        });

        cancelButton.setOnClickListener(v -> dismiss());
    }
}
