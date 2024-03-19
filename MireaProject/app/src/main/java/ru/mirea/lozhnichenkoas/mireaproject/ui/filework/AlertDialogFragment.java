package ru.mirea.lozhnichenkoas.mireaproject.ui.filework;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import ru.mirea.lozhnichenkoas.mireaproject.R;

public class AlertDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Сохранить файл")
                .setView(R.layout.save_file_dialog)
                .setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditText editTextFileNameDialog = getDialog().findViewById(R.id.editTextFileNameDialog);
                        EditText editTextDataDialog = getDialog().findViewById(R.id.editTextDataDialog);
                        FileSave.saveFile(getActivity(),
                                editTextFileNameDialog.getText().toString() + ".txt",
                                editTextDataDialog.getText().toString());
                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        return builder.create();
    }
}