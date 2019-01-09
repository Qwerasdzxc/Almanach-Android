package com.code_dream.almanach.notes.note_list;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.code_dream.almanach.BaseActivity;
import com.code_dream.almanach.R;
import com.code_dream.almanach.models.Note;
import com.code_dream.almanach.notes.add_edit_note.AddNoteActivity;
import com.code_dream.almanach.utility.Registry;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoteListActivity extends BaseActivity implements NoteListContract.View, OnContextMenuClick {

    @BindView(R.id.notes_toolbar) Toolbar toolbar;
    @BindView(R.id.note_list_swipe_refresh) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.notes_gridview) GridView gridView;
    @BindView(R.id.notes_progress_bar) ProgressBar progressBar;

    OnContextMenuClick listener;

    private ArrayList<Note> notes = new ArrayList<>();
    private GridViewAdapter adapter;

    NoteListPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notes);
        setupView();

        // Initializing the context menu click listener.
        listener = this;

        // Load saved Notes.
        presenter.loadNotes();
    }

    @Override
    public void setupPresenter() {
        presenter = new NoteListPresenter(this);
    }

    @Override
    public void setupView() {
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Notes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.colorPrimary));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadNotes();
            }
        });

        adapter = new GridViewAdapter();
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == notes.size() - 1)
                    startActivityForResult(new Intent(NoteListActivity.this, AddNoteActivity.class), Registry.OPEN_ADD_EDIT_NOTE_ACTIVITY);

            }
        });
    }

    @Override
    public void onContextMenuClick(final int position, ImageView view) {
        // If we're in offline mode, we can't edit or delete notes.
        if (!presenter.onlineMode)
            return;

        PopupMenu popup = new PopupMenu(this, view);
        popup.inflate(R.menu.post_context_menu_mine);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_edit_post:
                        presenter.editNote(notes.get(position));
                        break;
                    case R.id.action_delete_post:
                        Log.e("Deleting", "Title: " + notes.get(position).getTitle());
                        presenter.deleteNote(notes.get(position), position);
                        break;
                }
                return false;
            }
        });
        popup.show();
    }

    @Override
    public void showNotes(List<Note> notes) {
        this.notes.clear();
        this.notes.addAll(notes);

        // Add a dummy item for Add Note item purposes.
        this.notes.add(new Note(null, null, null));

        adapter.notifyDataSetChanged();
    }

    @Override
    public void deleteNote(int noteIndex) {
        notes.remove(noteIndex);
        adapter.notifyDataSetChanged();
        gridView.invalidateViews();
    }

    @Override
    public void showProgressBar(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setSwipeRefreshLayoutRefreshing(boolean refreshing) {
        swipeRefreshLayout.setRefreshing(refreshing);
    }

    @Override
    public void openEditNoteActivity(Note note) {
        Intent intent = new Intent(this, AddNoteActivity.class);
        intent.putExtra("note", (Parcelable) note);

        startActivityForResult(intent, Registry.OPEN_ADD_EDIT_NOTE_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Registry.OPEN_ADD_EDIT_NOTE_ACTIVITY && resultCode == RESULT_OK) {
            Note newNote = data.getParcelableExtra("note");
            boolean noteAdded = false;

            for (int i = 0; i < notes.size(); i++) {
                // If ID's match, that means that the new note
                // is already in the list (new note is an edited note)
                if (notes.get(i) != null && newNote.getId() == notes.get(i).getId()) {
                    notes.set(i, newNote);
                    noteAdded = true;
                    break;
                }
            }

            if (!noteAdded)
                notes.add(notes.size() - 1, newNote);

            adapter.notifyDataSetChanged();
            gridView.setAdapter(gridView.getAdapter());
        }
    }

    public class GridViewAdapter extends BaseAdapter {

        public int getCount() {
            return notes.size();
        }

        public Object getItem(int position) {
            return notes.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            // View to be inflated.
            View view;

            // Is this the item made for adding new Notes?
            boolean isAddNoteItem = notes.size() - 1 == position;

            // Re-create the view every time the AddNoteItem is rendering.
            if (convertView == null || isAddNoteItem)
                if (!isAddNoteItem) {
                    view = getLayoutInflater().inflate(R.layout.list_item_note, null);
                    TextView title = view.findViewById(R.id.note_item_title);
                    TextView description = view.findViewById(R.id.note_item_description);
                    final ImageView contextMenu = view.findViewById(R.id.note_item_context_menu);

                    title.setText(notes.get(position).getTitle());
                    description.setText(notes.get(position).getDescription());
                    contextMenu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.onContextMenuClick(position, contextMenu);
                        }
                    });
                }
                else
                    view = getLayoutInflater().inflate(R.layout.list_item_new_note, null);
            else
                view = convertView;

            return view;
        }
    }
}

interface OnContextMenuClick {

    void onContextMenuClick(int position, ImageView view);
}
