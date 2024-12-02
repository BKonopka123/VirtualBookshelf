package com.example.virtualbookshelf.view.Book;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
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
import com.example.virtualbookshelf.model.Book;
import com.example.virtualbookshelf.model.db.DBManager;
import com.example.virtualbookshelf.viewmodel.Book.BookshelfDetailViewModel;

import java.util.Objects;

/**
 * DialogFragment for deleting a book from the bookshelf.
 */
public class BookDeleteDialog extends DialogFragment {

    /**
     * ViewModel for the BookshelfDetailActivity.
     */
    private final BookshelfDetailViewModel bookshelfDetailViewModel;

    /**
     * Database manager for managing database operations.
     */
    private final DBManager dbManager;

    /**
     * The book to be deleted.
     */
    private Book bookToDelete;

    /**
     * Constructor for the BookDeleteDialog.
     * @param bookshelfDetailViewModel ViewModel for the BookshelfDetailActivity.
     * @param dbManager Database manager for managing database operations.
     * @param bookToDelete The book to be deleted.
     */
    public BookDeleteDialog(BookshelfDetailViewModel bookshelfDetailViewModel, DBManager dbManager, Book bookToDelete) {
        this.bookshelfDetailViewModel = bookshelfDetailViewModel;
        this.dbManager = dbManager;
        this.bookToDelete = bookToDelete;
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
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_book_delete, container, false);
    }

    /**
     * Called when the dialog is created.
     * @param savedInstanceState The last saved instance state of the Fragment,
     * or null if this is a freshly created Fragment.
     *
     * @return A new Dialog instance to be displayed by the Fragment.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle("Book delete");
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
        Button confirmButton = view.findViewById(R.id.bookDeleteConfirmButton);
        Button cancelButton = view.findViewById(R.id.bookDeleteCancelButton);

        // Set click listeners for the buttons to delete the book from the database
        confirmButton.setOnClickListener(v -> {
            dbManager.deleteBook(bookToDelete);

            if (getActivity() != null) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("deleted_book", bookToDelete);
                getActivity().setResult(Activity.RESULT_OK, resultIntent);
                getActivity().finish();
            }

            dismiss();
        });

        cancelButton.setOnClickListener(v -> {
            dismiss();
        });
    }
}
