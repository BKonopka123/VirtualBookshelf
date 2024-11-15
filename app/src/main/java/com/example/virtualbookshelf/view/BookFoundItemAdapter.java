package com.example.virtualbookshelf.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.virtualbookshelf.R;
import com.example.virtualbookshelf.model.BlobManager;
import com.example.virtualbookshelf.model.Book;

import java.util.ArrayList;

/**
 * Adapter for the RecyclerView in the BookFoundActivity.
 */
public class BookFoundItemAdapter extends RecyclerView.Adapter<BookFoundItemAdapter.ViewHolder>{

    /**
     * List of books to be displayed in the RecyclerView.
     */
    private ArrayList<Book> booksList;

    /**
     * Context of the activity.
     */
    private Context context;

    /**
     * Listener for item click events.
     */
    private OnItemClickListener listener;


    /**
     * Interface for handling item click events.
     */
    public interface OnItemClickListener {
        void onItemClick(Book item);
    }


    /**
     * Constructor for the BookFoundItemAdapter.
     * @param booksList List of books to be displayed in the RecyclerView.
     * @param context Context of the activity.
     * @param listener Listener for item click events.
     */
    public BookFoundItemAdapter(ArrayList<Book> booksList, Context context, OnItemClickListener listener) {
        this.booksList = booksList;
        this.context = context;
        this.listener = listener;
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public BookFoundItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book_found, parent, false);
        return new BookFoundItemAdapter.ViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull BookFoundItemAdapter.ViewHolder holder, int position) {
        Book book = booksList.get(position);
        holder.bind(book, listener);
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() { return booksList.size(); }

    /**
     * Updates the list of books in the adapter.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        /** Views in the item layout. */
        ImageView bookImage;
        ImageButton isAddedImage;
        TextView authorText;
        TextView titleText;

        /**
         * Constructor for the ViewHolder.
         * @param itemView The View representing the item in the RecyclerView.
         */
        public ViewHolder(View itemView) {
            super(itemView);
            bookImage = itemView.findViewById(R.id.book_image_item_book_found);
            isAddedImage = itemView.findViewById(R.id.isAdded_imageButton_item_book_found);
            authorText = itemView.findViewById(R.id.book_author_item_book_found);
            titleText = itemView.findViewById(R.id.book_title_item_book_found);
        }

        /**
         * Binds the data to the views in the item layout.
         * @param item The Book object representing the data to be displayed.
         * @param listener The listener for item click events.
         */
        public void bind(final Book item, final BookFoundItemAdapter.OnItemClickListener listener) {
            Bitmap bookImageBitmap = BlobManager.getBitmapFromBlob(item.getPhoto());
            bookImage.setImageBitmap(bookImageBitmap);
            authorText.setText(item.getAuthor());
            titleText.setText(item.getTitle());
            boolean isAdded = item.getIsAdded();
            if (isAdded) {
                isAddedImage.setImageResource(R.drawable.ic_ok_gray);
            } else {
                isAddedImage.setImageResource(R.drawable.ic_ok);
            }
            Log.e("BookFoundItemAdapter", "Book bound: " + item.getTitle());

            isAddedImage.setOnClickListener(v -> {
                item.setIsAdded(!item.getIsAdded());

                boolean isAddedAfter = item.getIsAdded();
                if (isAddedAfter) {
                    isAddedImage.setImageResource(R.drawable.ic_ok_gray);
                } else {
                    isAddedImage.setImageResource(R.drawable.ic_ok);
                }

                Log.d("BookFoundItemAdapter", "Book state changed: " + item.getTitle() + " - isAdded: " + item.getIsAdded());
            });

            itemView.setOnClickListener(v -> listener.onItemClick(item));
        }
    }
}
