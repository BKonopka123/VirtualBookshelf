package com.example.virtualbookshelf.view.Book;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.virtualbookshelf.R;
import com.example.virtualbookshelf.model.BlobManager;
import com.example.virtualbookshelf.model.Book;

import java.util.ArrayList;

/**
 * BookshelfItemAdapter is an adapter for the RecyclerView in the BookshelfActivity.
 * Needed to dynamically display data in the application.
 */
public class BookshelfItemAdapter extends RecyclerView.Adapter<BookshelfItemAdapter.ViewHolder> {

    /** List of books in the bookshelf. */
    private ArrayList<Book> booksList;
    /** Context of the activity. */
    private Context context;
    /** Listener for item clicks. */
    private OnItemClickListener listener;

    /**
     * Interface for handling item click events.
     */
    public interface OnItemClickListener {
        void onItemClick(Book item);
    }

    /**
     * Constructor for BookshelfItemAdapter.
     *
     * @param booksList List of books in the bookshelf.
     * @param context Context of the activity.
     * @param listener Listener for item clicks.
     */
    public BookshelfItemAdapter(ArrayList<Book> booksList, Context context, OnItemClickListener listener) {
        this.booksList = booksList;
        this.context = context;
        this.listener = listener;
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book_bookshelf, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull BookshelfItemAdapter.ViewHolder holder, int position) {
        Book book = booksList.get(position);
        holder.bind(book, listener);
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return booksList.size();
    }

    /**
     * ViewHolder class for holding the views for each item in the RecyclerView.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        /** Views in the item layout. */
        ImageView bookImage;
        ImageView statusImage;
        TextView authorText;
        TextView titleText;

        /**
         * Constructor for ViewHolder.
         * @param itemView The View representing the item in the RecyclerView.
         */
        public ViewHolder(View itemView) {
            super(itemView);
            bookImage = itemView.findViewById(R.id.book_image_item_bookshelf);
            statusImage = itemView.findViewById(R.id.status_image_item_bookshelf);
            authorText = itemView.findViewById(R.id.book_author_item_bookshelf);
            titleText = itemView.findViewById(R.id.book_title_item_bookshelf);
        }

        /**
         * Binds the data to the views in the item layout.
         * @param item The Book object representing the data to be displayed.
         * @param listener The listener for item clicks.
         */
        public void bind(final Book item, final OnItemClickListener listener) {
            Bitmap bookImageBitmap = BlobManager.getBitmapFromBlob(item.getPhoto());
            String status = item.getStatus();

            bookImage.setImageBitmap(bookImageBitmap);
            authorText.setText(item.getAuthor());
            titleText.setText(item.getTitle());

            if (status.equals("Read")) {
                statusImage.setImageResource(R.drawable.ic_status_read);
            } else if (status.equals("Unread")) {
                statusImage.setImageResource(R.drawable.ic_status_unread);
            } else if (status.equals("Currently")) {
                statusImage.setImageResource(R.drawable.ic_status_currently);
            } else if (status.equals("Queue")) {
                statusImage.setImageResource(R.drawable.ic_status_queue);
            }

            itemView.setOnClickListener(v -> listener.onItemClick(item));
        }

    }

    /**
     * Updates the list of books in the adapter.
     * @param newBooks The new list of books to be displayed.
     */
    public void updateBooks(ArrayList<Book> newBooks) {
        booksList.clear();
        booksList.addAll(newBooks);
        notifyDataSetChanged();
        Log.d("BookshelfItemAdapter", "Books updated");
    }
}
