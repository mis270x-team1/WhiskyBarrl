package edu.wpi.mis270xteam1.whiskybarrl;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class AccountFragment extends Fragment {
    private String currentUsername;
    private User currentUser;
    private DatabaseHandler db;

    private Button editProfileButton;
    private Button logoutButton;
    private TextView textViewFullName;
    private TextView textViewUsername;
    private TextView textViewEmail;
    private TextView textViewAge;
    private TextView textViewPhoneNumber;
    private TextView textViewGender;
    private TextView textViewCountry;
    private ImageView imageViewProPic;
    private SharedPreferences preferences;

    private static final int UPDATE_USER_REQUEST = 1;

    /**
     * Create a new instance of this fragment with the user's data.
     *
     * @param user the user to get the data from
     * @return a new instance of this fragment
     */
    public static AccountFragment newInstance(User user) {
        AccountFragment newAccountFragment = new AccountFragment();
        Bundle args = new Bundle();

        args.putString("username", user.getUsername());
        args.putString("firstName", user.getFirstName());
        args.putString("lastName", user.getLastName());
        args.putString("email", user.getEmail());
        args.putString("phoneNumber", user.getPhoneNumber());
        args.putString("gender", user.getGender());
        args.putString("country", user.getCountry());
        newAccountFragment.setArguments(args);

        return newAccountFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

        currentUsername = getArguments().getString("username");
        db = new DatabaseHandler(getActivity());
        currentUser = db.getUser(currentUsername);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        editProfileButton = (Button) view.findViewById(R.id.buttonEP);
        logoutButton = (Button) view.findViewById(R.id.logoutButton);
        textViewFullName = (TextView) view.findViewById(R.id.textViewFullName);
        textViewUsername = (TextView) view.findViewById(R.id.textViewUsername);
        textViewAge = (TextView) view.findViewById(R.id.textViewAge);
        textViewEmail = (TextView) view.findViewById(R.id.textViewEmail);
        textViewPhoneNumber = (TextView) view.findViewById(R.id.textViewPN);
        textViewGender = (TextView) view.findViewById(R.id.textViewGender);
        textViewCountry = (TextView) view.findViewById(R.id.textViewCountry);
        imageViewProPic = (ImageView) view.findViewById(R.id.imageViewProPic);

        showUserInfo();

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), EditProfileActivity.class);
                i.putExtra("username", currentUsername);
                startActivityForResult(i, UPDATE_USER_REQUEST);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Return to the login screen.
                preferences.edit().clear().apply();
                Toast.makeText(getActivity(), "You have been logged out.", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == UPDATE_USER_REQUEST && resultCode == Activity.RESULT_OK) {
            Bundle result = data.getExtras();
            String newUsername = result.getString("newUsername");
            String newPassword = result.getString("newPassword");
            String newImgPath = result.getString("newImgPath");
            String newFirstName = result.getString("newFirstName");
            String newLastName = result.getString("newLastName");
            int newAge = result.getInt("newAge");
            String newEmail = result.getString("newEmail");
            String newPhoneNumber = result.getString("newPhoneNumber");
            String newGender = result.getString("newGender");
            String newCountry = result.getString("newCountry");

            currentUsername = newUsername;
            currentUser.setUsername(newUsername);
            currentUser.setFirstName(newFirstName);
            currentUser.setLastName(newLastName);
            currentUser.setImgPath(newImgPath);
            currentUser.setAge(newAge);
            currentUser.setEmail(newEmail);
            currentUser.setPhoneNumber(newPhoneNumber);
            currentUser.setGender(newGender);
            currentUser.setCountry(newCountry);

            if (newPassword != null) {
                currentUser.setPassword(newPassword);
            }

            showUserInfo();
        }
    }

    private void showUserInfo() {
        String fullName = currentUser.getFirstName() + " " + currentUser.getLastName();
        String ageText = currentUser.getAge() + " years old";
        textViewFullName.setText(fullName);
        textViewUsername.setText(currentUsername);

        if (!"".equals(currentUser.getImgPath())) {
            imageViewProPic.setImageURI(Uri.fromFile(new File(currentUser.getImgPath())));
        }
        textViewEmail.setText(currentUser.getEmail());
        textViewPhoneNumber.setText(currentUser.getPhoneNumber());
        textViewGender.setText(currentUser.getGender());
        textViewAge.setText(ageText);
        textViewCountry.setText(currentUser.getCountry());
    }
}
