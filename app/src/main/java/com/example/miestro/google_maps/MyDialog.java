package com.example.miestro.google_maps;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by MIESTRO on 28/01/2018.
 */

public class MyDialog extends DialogFragment implements View.OnClickListener {

    Button yes,no;
    Communicator communicator;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        communicator = (Communicator)getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.my_dialog,null);
        yes = (Button)view.findViewById(R.id.yes);
        no = (Button)view.findViewById(R.id.no);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
        setCancelable(false);
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.yes){
            communicator.onDialogMessage("yes");
            dismiss();


        }else{
            communicator.onDialogMessage("no");
            dismiss();
           // Toast.makeText(getActivity(),"no",Toast.LENGTH_SHORT).show();

        }
    }

    interface Communicator
    {
        public void onDialogMessage(String message);
    }

}
