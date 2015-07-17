package org.ramonaza.officialramonapp.people.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import org.ramonaza.officialramonapp.R;
import org.ramonaza.officialramonapp.helpers.activities.BaseActivity;
import org.ramonaza.officialramonapp.people.backend.ContactDatabaseHandler;
import org.ramonaza.officialramonapp.people.backend.ContactInfoWrapper;
import org.ramonaza.officialramonapp.people.fragments.GeneralContactFragment;

public class ContactDataActivity extends BaseActivity {

    private static final String EXTRA_CONTRUCTION_INFO="org.ramonaza.officialramonapp.ALEPH_ID";
    private static final String EXTRA_LAYER="org.ramonaza.officialramonapp.LAYER_NAME";
    private final String EXTRA_OPENEDPAGE="org.ramonaza.officialramonapp.OPENED_PAGE";
    private int inputId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Intent intent=getIntent();
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Blank Contact Data");
        inputId=intent.getIntExtra(EXTRA_CONTRUCTION_INFO,0);
        new intentToFrag(this).execute(inputId);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                Intent bacIntent=NavUtils.getParentActivityIntent(this);
                bacIntent.putExtra(EXTRA_OPENEDPAGE,2);
                startActivity(bacIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public class intentToFrag extends AsyncTask<Integer,Void,ContactInfoWrapper>{

        private Context context;

        public intentToFrag(Context context){
            this.context=context;
        }

        @Override
        protected ContactInfoWrapper doInBackground(Integer... params) {
            ContactDatabaseHandler handler=new ContactDatabaseHandler(context);
            return handler.getContact(inputId);
        }

        @Override
        protected void onPostExecute(ContactInfoWrapper contactInfoWrapper) {
            super.onPostExecute(contactInfoWrapper);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction().replace(R.id.container, GeneralContactFragment.newInstance(1,contactInfoWrapper));
            transaction.commit();
        }
    }
    public void refreshFrag(){
        new intentToFrag(this).execute(inputId);
    }
}
