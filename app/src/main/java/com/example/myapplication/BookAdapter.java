package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BookAdapter  extends  RecyclerView.Adapter<BookAdapter.viewHolder>{

    private Context context;
    private ArrayList<Book> books;
    // menampung id yang akan di select
    private ArrayList<Integer> mSelectedId;
    private OnClickListener listener;

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    // membuat mselected id agar bisa dipanngil dimain
    public ArrayList<Integer> getmSelectedId() {
        return mSelectedId;
    }

    public BookAdapter(Context context) {
        this.context = context;
        mSelectedId= new ArrayList<>();
    }

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.lists_book_item, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Book currentBook= books.get(position);
        holder.tvTitle.setText(currentBook.getTitle());
        holder.tvAuthor.setText(currentBook.getAuthor());
        // check apakah aplikasi sudh di select
        holder.itemView.setSelected(mSelectedId.contains(position));
    }

    // make toggle staus click
    public void toogleSelection(int position){
        // ketika suatu position sudah terdapat di arraylist
        if(mSelectedId.contains(position)){
            // ketika data di klik lagi maka remove data
            mSelectedId.remove(Integer.valueOf(position));
        }
        // ketika pertama kali di klik
        else{
            mSelectedId.add(position);
        }
        // merubah tampilan sesuai dengan perubahan adapter
        notifyDataSetChanged();
    }

    // menampung jumlah dari array selected
    public int selectionOnCount(){
        return mSelectedId.size();
    }
    // mereet suatu selected
    public void resetSelection(){
        mSelectedId= new ArrayList<>();
        // ketika tampilan berubah
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return books.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvAuthor;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle= itemView.findViewById(R.id.tv_title);
            tvAuthor= itemView.findViewById(R.id.tv_author);
            // give onclick short time
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null) {
                        listener.onClick(getAdapterPosition());
                    }
                }
            });
            // give onclick long time
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return listener.onLongClick(getAdapterPosition());
                }
            });
        }
    }
}
