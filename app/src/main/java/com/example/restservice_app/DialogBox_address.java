package com.example.restservice_app;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class DialogBox_address extends AppCompatDialogFragment {

    private EditText addressline1, addressline2, telephone;
    private DialogAddressListner listner;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        builder.setView(view).setTitle("Add Deliver Address").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        }).setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String add1 = addressline1.getText().toString();
                String add2 = addressline2.getText().toString();
                String tele = telephone.getText().toString();
                listner.applyText(add1, add2, tele);
            }
        });

        addressline1 = view.findViewById(R.id.address1);
        addressline2 = view.findViewById(R.id.address2);
        telephone = view.findViewById(R.id.telephone);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listner = (DialogAddressListner) context;
        } catch (ClassCastException e) {
           throw  new ClassCastException(context.toString() + "Must Implement DialogAddressListener");
        }
    }

    public interface  DialogAddressListner{
        void applyText(String address1, String address2, String telephone);

    }

}
