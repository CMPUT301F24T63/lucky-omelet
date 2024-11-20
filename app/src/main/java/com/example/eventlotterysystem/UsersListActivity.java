package com.example.eventlotterysystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;

public class UsersListActivity extends AppCompatActivity {

    private ListView usersListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> usersList;
    private ArrayList<User> users;
    private ArrayList<String> filteredUsersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        usersListView = findViewById(R.id.users_list_view);
        usersList = new ArrayList<>();
        filteredUsersList = new ArrayList<>();
        users = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, filteredUsersList);
        usersListView.setAdapter(adapter);

        usersListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedUserName = filteredUsersList.get(position);
            User selectedUser = null;
            for (User user : users) {
                if (user.getName().equals(selectedUserName)) {
                    selectedUser = user;
                    break;
                }
            }
            if (selectedUser != null) {
                Intent intent = new Intent(UsersListActivity.this, AdminViewUserActivity.class);
                intent.putExtra("user", selectedUser);
                startActivity(intent);
            }
        });

        fetchUsers();

        // Set up back button listener
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(view -> finish());

        // Set up search functionality
        SearchView searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterUsers(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterUsers(newText);
                return false;
            }
        });
    }

    private void fetchUsers() {
        FirestoreManager.getInstance().loadUsers(Control.getInstance(), new FirestoreManager.UsersCallback() {
            @Override
            public void onCallback(ArrayList<User> users) {
                UsersListActivity.this.users.clear();
                UsersListActivity.this.users.addAll(users);
                usersList.clear();
                for (User user : users) {
                    usersList.add(user.getName());
                }
                Collections.sort(usersList); // Sort users list alphabetically
                filterUsers(""); // Initialize the filtered list with all users
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("UsersListActivity", "Error fetching users", e);
            }
        });
    }

    private void filterUsers(String query) {
        filteredUsersList.clear();
        for (String userName : usersList) {
            if (userName.toLowerCase().contains(query.toLowerCase())) {
                filteredUsersList.add(userName);
            }
        }
        adapter.notifyDataSetChanged();
    }
}