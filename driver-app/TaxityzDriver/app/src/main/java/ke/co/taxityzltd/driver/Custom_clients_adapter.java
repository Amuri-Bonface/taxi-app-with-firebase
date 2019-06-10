package ke.co.taxityzltd.driver;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class Custom_clients_adapter extends ArrayAdapter<RetrieveCordinates> {
    private Activity context;
    private List<RetrieveCordinates> rideList;

    public Custom_clients_adapter(Activity context, List<RetrieveCordinates> rideList)
    {
        super(context,R.layout.custom_requests,rideList);
        this.context=context;
        this.rideList=rideList;
    }

    @NonNull
    @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            LayoutInflater layoutInflater = context.getLayoutInflater();

            View view = layoutInflater.inflate(R.layout.custom_requests, null, true);




            TextView c_name = (TextView) view.findViewById(R.id.c_name);
            TextView c_pickup = (TextView) view.findViewById(R.id.c_pickup);
            TextView c_distance = (TextView) view.findViewById(R.id.c_distance);


            RetrieveCordinates rides=rideList.get(position);

            c_name.setText(rides.getMyName());
            c_pickup.setText("@:"+rides.getPickupName());
            c_distance.setText("Phone:"+rides.getMyPhone());

            return  view;

        }
    }

