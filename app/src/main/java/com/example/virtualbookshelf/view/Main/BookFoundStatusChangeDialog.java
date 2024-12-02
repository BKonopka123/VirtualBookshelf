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
import com.example.virtualbookshelf.viewmodel.Main.BookFoundDetailViewModel;

import java.util.Objects;

/**
 * DialogFragment for changing the status of a found book.
 */
public class BookFoundStatusChangeDialog extends DialogFragment {

    /**
     * Interface for listening to changes in the found book's status.
     */
    public interface OnBookFoundStatusChangeListener {
        void onFoundStatusChanged(Book updatedBook);
    }

    /**
     * Instance of the OnBookFoundStatusChangeListener interface.
     */
    private OnBookFoundStatusChangeListener foundStatusChangeListener;

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
     * Constructor for the BookFoundStatusChangeDialog class.
     * @param bookFoundDetailViewModel Instance of the BookFoundDetailViewModel.
     * @param dbManager Instance of the DBManager.
     * @param bookToUpdate Book to be updated.
     */
    public BookFoundStatusChangeDialog(BookFoundDetailViewModel bookFoundDetailViewModel, DBManager dbManager, Book bookToUpdate) {
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
            foundStatusChangeListener = (BookFoundStatusChangeDialog.OnBookFoundStatusChangeListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Host activity must implement OnBookFoundStatusChangeListener");
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
        return inflater.inflate(R.layout.dialog_book_status_change, container, false);
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
        dialog.setTitle("Found Book Status Change");
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
        ImageButton readButton = view.findViewById(R.id.editStatusBookRead);
        ImageButton unreadButton = view.findViewById(R.id.editStatusBookUnread);
        ImageButton currentlyButton = view.findViewById(R.id.editStatusBookCurrently);
        ImageButton queueButton = view.findViewById(R.id.editStatusBookQueue);
        Button cancelButton = view.findViewById(R.id.bookStatusCancelButton);

        // Set click listeners for the buttons to update the book's status in the database
        readButton.setOnClickListener(v -> {
            bookToUpdate.setStatus("Read");
            foundStatusChangeListener.onFoundStatusChanged(bookToUpdate);
            dismiss();
        });

        unreadButton.setOnClickListener(v -> {
            bookToUpdate.setStatus("Unread");
            foundStatusChangeListener.onFoundStatusChanged(bookToUpdate);
            dismiss();
        });

        currentlyButton.setOnClickListener(v -> {
            bookToUpdate.setStatus("Currently");
            foundStatusChangeListener.onFoundStatusChanged(bookToUpdate);
            dismiss();
        });

        queueButton.setOnClickListener(v -> {
            bookToUpdate.setStatus("Queue");
            foundStatusChangeListener.onFoundStatusChanged(bookToUpdate);
            dismiss();
        });

        cancelButton.setOnClickListener(v -> dismiss());
    }
}
