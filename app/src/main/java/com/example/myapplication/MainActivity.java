package com.example.myapplication;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    RecyclerView rvBook;
    private ArrayList<Book> books;
    private BookAdapter bookAdapter;
    // definition action mode
    private ActionMode mActionMode;

    // create action mode
    private ActionMode.Callback mActionCallback = new ActionMode.Callback() {
        // aksi dari actio n mode
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            // call menu from action mode
            actionMode.getMenuInflater().inflate(R.menu.action_menu,menu);
            // return to display menu
            return true;
        }

        // ketika action mode akan melakukan prubahan data
        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            actionMode.setTitle(String.valueOf(bookAdapter.selectionOnCount()));
            // mengatur setvisible dimana selectioncount == 1
            menu.findItem(R.id.m_info).setVisible(bookAdapter.selectionOnCount()==1);
            // jika setVisible selain satu maka akan hilang
            return false;
        }

        // aksi ketika suatu mode diklik
        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.m_info:
                    showInfo();
                    return true;
                case R.id.m_delete:
                    delete();
                    return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            // mendestroy suatu selected
            mActionMode=null;
            bookAdapter.resetSelection();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvBook = findViewById(R.id.recycler);
        books = new ArrayList<>();
        books.add(new Book(1, "Sidiq Rihardi", "Andoid expert"));
        books.add(new Book(2, "Yusuf Susamti", "Java untuk Android"));
        books.add(new Book(3, "Ramadansyah", "Belajar design manual"));
        books.add(new Book(4, "Andre Bima", "Phyton untuk pemula"));
        books.add(new Book(5, "Radit satya", "cepat belajar programming"));
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        rvBook.setLayoutManager(linearLayoutManager);
        DividerItemDecoration divider= new DividerItemDecoration(this, linearLayoutManager.getOrientation());
        rvBook.addItemDecoration(divider);
        bookAdapter= new BookAdapter(this);
        rvBook.setAdapter(bookAdapter);
        bookAdapter.setBooks(books);
        // set ketika adapter di klik
        bookAdapter.setListener(new OnClickListener() {
            @Override
            public void onClick(int position) {
                if(mActionMode!=null){
                    // membuat adapter akan menambah selected ketika di klik cuma sekali
                    bookAdapter.toogleSelection(position);
                    if(bookAdapter.selectionOnCount()==0){
                        mActionMode.finish();
                    }
                    else{
                        mActionMode.invalidate();
                    }
                }
                Toast.makeText(MainActivity.this, "Title "+books.get(position).getTitle(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onLongClick(int position) {
                Toast.makeText(MainActivity.this, "author "+books.get(position).getAuthor(), Toast.LENGTH_SHORT).show();
                if(mActionMode!=null){
                    return false;
                }
                // ketika action mode tidak sama dengan null
                bookAdapter.toogleSelection(position);
                mActionMode= MainActivity.this.startActionMode(mActionCallback);
                return true;
            }

        });
    }
    // show title and author
    public void showInfo(){
        final  Book selectedBook= books.get(bookAdapter.getmSelectedId().get(0));
        String info="Title"+ selectedBook.getTitle()+"Author"+ selectedBook.getAuthor();
        Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
    }
    // delete arraylist
    public void delete(){
        final ArrayList<Integer> selectedIds= bookAdapter.getmSelectedId();
        // mengurutkan suatu data
        Collections.sort(selectedIds, new Comparator<Integer>() {
            @Override
            public int compare(Integer integer, Integer t1) {
                return t1.compareTo(integer);
            }
        });
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Hapus data");
        builder.setMessage("Yakin Menghapus");
        builder.setNegativeButton("Ya, Hapus", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // aksi hapus
                // kode untuk menghapus-awal
                for (int currentId: selectedIds) {
                    books.remove(currentId);
                }
                bookAdapter.notifyDataSetChanged();
                mActionMode.finish();
                // kode menghapus akhir

            }
        });
        builder.setPositiveButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                mActionMode.finish();
            }
        });
        AlertDialog alertDialog= builder.create();
        alertDialog.show();
    }

}
