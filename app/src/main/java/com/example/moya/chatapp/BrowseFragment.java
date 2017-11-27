package com.example.moya.chatapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BrowseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */


public class BrowseFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public BrowseFragment() {
        // Required empty public constructor
    }

    RecyclerView recyclerView ;
    DatabaseReference databaseReference ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_browse ,container,false);
        recyclerView = (RecyclerView) v.findViewById(R.id.all_users);
        Toast.makeText(getContext(), recyclerView.toString() , Toast.LENGTH_LONG).show();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        return inflater.inflate(R.layout.fragment_browse, container, false);


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStart(){
        super.onStart();
        Object obj = R.layout.single_user_layout ;
        Toast.makeText(getContext(),"" + obj.toString(),Toast.LENGTH_LONG).show();
        FirebaseRecyclerAdapter<Users, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(Users.class , R.layout.single_user_layout , UsersViewHolder.class , databaseReference) {
            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, Users model, int position) {
                viewHolder.setName(model.getName());
                viewHolder.setImage(model.getImage());
                viewHolder.setStatus(model.getStatus());
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
