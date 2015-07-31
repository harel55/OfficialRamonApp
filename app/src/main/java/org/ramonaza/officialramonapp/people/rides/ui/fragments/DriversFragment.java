package org.ramonaza.officialramonapp.people.rides.ui.fragments;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import org.ramonaza.officialramonapp.R;
import org.ramonaza.officialramonapp.helpers.backend.InfoWrapper;
import org.ramonaza.officialramonapp.helpers.ui.fragments.InfoWrapperListFragment;
import org.ramonaza.officialramonapp.people.backend.ContactDatabaseContract;
import org.ramonaza.officialramonapp.people.rides.backend.RidesDatabaseHandler;
import org.ramonaza.officialramonapp.people.rides.ui.activities.AddCustomDriverActivity;
import org.ramonaza.officialramonapp.people.rides.ui.activities.RidesDriverManipActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DriversFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriversFragment extends InfoWrapperListFragment {


    public static DriversFragment newInstance() {
        DriversFragment fragment = new DriversFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public DriversFragment() {
        // Required empty public constructor
    }

    @Override
    public ArrayAdapter getAdapter() {
        return new DriverFragAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mLayoutId = R.layout.fragment_rides_drivers;
        View rootView = super.onCreateView(inflater, container, savedInstanceState); //Retrieve the parent's view to manipulate
        Button customButton = (Button) rootView.findViewById(R.id.AddCustomDriveButton);
        customButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent customDriverIntent = new Intent(getActivity(), AddCustomDriverActivity.class);
                startActivity(customDriverIntent);
            }
        });
        refreshData();
        return rootView;
    }


    @Override
    public InfoWrapper[] generateInfo() {
        RidesDatabaseHandler handler=new RidesDatabaseHandler(getActivity());
        return handler.getDrivers(null,ContactDatabaseContract.DriverListTable.COLUMN_NAME+" DESC");
    }

    private class DriverFragAdapter extends ArrayAdapter<InfoWrapper>{

        public DriverFragAdapter(Context context) {
            super(context, 0);
        }

        private class ViewHolder{
            public Button nameButton;
            public Button deleteButton;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final InfoWrapper driver = getItem(position);
            ViewHolder viewHolder;
            if(convertView == null){
                viewHolder= new ViewHolder();
                convertView= LayoutInflater.from(getContext()).inflate(R.layout.infowrapper_textbutbut, parent, false);
                viewHolder.nameButton=(Button) convertView.findViewById(R.id.infowrappername);
                viewHolder.deleteButton=(Button) convertView.findViewById(R.id.infowrapperbutton);
                viewHolder.deleteButton.setText("Delete");
                convertView.setTag(viewHolder);
            } else viewHolder=(ViewHolder) convertView.getTag();
            viewHolder.nameButton.setText(driver.getName());
            viewHolder.nameButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), RidesDriverManipActivity.class);
                    intent.putExtra("DriverId", driver.getId());
                    startActivity(intent);
                }
            });
            viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DeleteCar().execute(driver.getId());
                }
            });
            return convertView;
        }

        public class DeleteCar extends AsyncTask<Integer, Void, Void>{

            @Override
            protected Void doInBackground(Integer... params) {
                RidesDatabaseHandler handler = new RidesDatabaseHandler(getContext());
                for(int id: params){
                    handler.deleteDriver(id);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                refreshData();
            }
        }
    }
}


