package com.example.virtualbookshelf.view.Main;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.virtualbookshelf.R;
import com.example.virtualbookshelf.model.Book;
import com.example.virtualbookshelf.model.db.DBManager;
import com.example.virtualbookshelf.viewmodel.Book.BookshelfDetailViewModel;

import java.util.Objects;

/**
 * DialogFragment for changing the status of a book in the bookshelf.
 */
public class BookStatusChangeDialog extends DialogFragment {

    /**
     * Interface for handling changes in the book's status.
     */
    public interface OnBookStatusChangeListener {
        void onStatusChanged(Book updatedBook);
    }

    /**
     * Listener for handling changes in the book's status.
     */
    private OnBookStatusChangeListener statusChangeListener;

    /**
     * ViewModel for the BookshelfDetailActivity.
     */
    private final BookshelfDetailViewModel bookshelfDetailViewModel;

    /**
     * Database manager for managing database operations.
     */
    private final DBManager dbManager;

    /**
     * The book to be updated.
     */
    private Book bookToUpdate;

    /**
     * Constructor for the BookStatusChangeDialog.
     * @param bookshelfDetailViewModel ViewModel for the BookshelfDetailActivity.
     * @param dbManager Database manager for managing database operations.
     * @param bookToUpdate The book to be updated.
     */
    public BookStatusChangeDialog(BookshelfDetailViewModel bookshelfDetailViewModel, DBManager dbManager, Book bookToUpdate) {
        this.bookshelfDetailViewModel = bookshelfDetailViewModel;
        this.dbManager = dbManager;
        this.bookToUpdate = bookToUpdate;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            statusChangeListener = (OnBookStatusChangeListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Host activity must implement OnBookStatusChangeListener");
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
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_book_status_change, container, false);
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
        dialog.setTitle("Book Status Change");
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        return dialog;
    }

    /**
     * Called when the activity is starting. This is where most initialization should go.
     * This method sets up the user interface, handles window insets for system bars,
     * and manages button clicks for navigation.
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
        ImageButton readButton = view.findViewById(R.id.editStatusBookRead);
        ImageButton unreadButton = view.findViewById(R.id.editStatusBookUnread);
        ImageButton currentlyButton = view.findViewById(R.id.editStatusBookCurrently);
        ImageButton queueButton = view.findViewById(R.id.editStatusBookQueue);
        Button cancelButton = view.findViewById(R.id.bookStatusCancelButton);

        // Set click listeners for the buttons to update the book's status in the database
        readButton.setOnClickListener(v -> {
            bookToUpdate.setStatus("Read");
            statusChangeListener.onStatusChanged(bookToUpdate);
            dismiss();
        });

        unreadButton.setOnClickListener(v -> {
            bookToUpdate.setStatus("Unread");
            statusChangeListener.onStatusChanged(bookToUpdate);
            dismiss();
        });

        currentlyButton.setOnClickListener(v -> {
            bookToUpdate.setStatus("Currently");
            statusChangeListener.onStatusChanged(bookToUpdate);
            dismiss();
        });

        queueButton.setOnClickListener(v -> {
            bookToUpdate.setStatus("Queue");
            statusChangeListener.onStatusChanged(bookToUpdate);
            dismiss();
        });

        cancelButton.setOnClickListener(v -> dismiss());
    }
}
