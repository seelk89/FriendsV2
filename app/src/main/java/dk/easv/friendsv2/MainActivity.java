package dk.easv.friendsv2;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import dk.easv.friendsv2.Model.BEFriend;
import dk.easv.friendsv2.Model.Friends;

public class MainActivity extends ListActivity {

    public static String TAG = "Friend2";
    private final int REQUEST_CODE_DETAIL = 4;

    Friends m_friends;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Friends v2");
        m_friends = new Friends();

        String[] friends;

        friends = m_friends.getNames();

        ListAdapter adapter =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1,
                        friends);

        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView parent, View v, int position,
                                long id) {

        Intent x = new Intent(this, DetailActivity.class);
        Log.d(TAG, "Detail activity will be started");
        BEFriend friend = m_friends.getAll().get(position);
        addData(x, friend, position);
        startActivityForResult(x, REQUEST_CODE_DETAIL);
        Log.d(TAG, "Detail activity is started");
    }

    private void addData(Intent x, BEFriend f, int position)
    {
        x.putExtra("position", position);
        x.putExtra("name", f.getName());
        x.putExtra("phone", f.getPhone());
        x.putExtra("favorite", f.isFavorite());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data )
    {
        if (requestCode == REQUEST_CODE_DETAIL)
            switch (resultCode) {
                case RESULT_OK:
                    int position = data.getExtras().getInt("position");
                    String newName = data.getExtras().get("newName").toString();
                    String newPhoneNumber = data.getExtras().get("newPhoneNumber").toString();
                    updateFriend(position, newName, newPhoneNumber);
                    break;
            }
    }

    private void updateFriend(int position, String newName, String newPhoneNumber)
    {
        m_friends.getAll().get(position).setName(newName);
        m_friends.getAll().get(position).setPhone(newPhoneNumber);
        ListAdapter adapter =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1,
                        m_friends.getNames());

        setListAdapter(adapter);
    }
}
