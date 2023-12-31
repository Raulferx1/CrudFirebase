package com.example.crudfirebase;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapter extends FirebaseRecyclerAdapter<MainModel,MainAdapter.myViewHolder> {


    public MainAdapter(@NonNull FirebaseRecyclerOptions<MainModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, final int position, @NonNull MainModel model) {
        holder.nombre.setText(model.getNombre());
        holder.apellido.setText(model.getApellido());
        holder.email.setText(model.getEmail());

        Glide.with(holder.img.getContext())
                .load(model.getImgURL())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(com.google.firebase.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img);

        holder.editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(holder.img.getContext());
                final View view = LayoutInflater.from(holder.img.getContext()).inflate(R.layout.ventana_emergente, null);

                builder.setView(view);
                final AlertDialog alertDialog = builder.create();

                final EditText nombreEditText = view.findViewById(R.id.nombreText);
                final EditText apellidoText = view.findViewById(R.id.apellidoText);
                final EditText emailText = view.findViewById(R.id.emailText);
                final EditText imageURLText = view.findViewById(R.id.img1Text);

                Button actualizarBtn = view.findViewById(R.id.btn_actualizar);

                nombreEditText.setText(model.getNombre());
                apellidoText.setText(model.getApellido());
                emailText.setText(model.getEmail());
                imageURLText.setText(model.getImgURL());

                actualizarBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String nuevoNombre = nombreEditText.getText().toString().trim();
                        String nuevoApellido = apellidoText.getText().toString().trim();
                        String nuevoEmail = emailText.getText().toString().trim();
                        String nuevaImgURL = imageURLText.getText().toString().trim();

                        if (!nuevoNombre.isEmpty() && !nuevoApellido.isEmpty() && !nuevoEmail.isEmpty() && !nuevaImgURL.isEmpty()) {

                            Map<String, Object> actualizacion = new HashMap<>();
                            actualizacion.put("Nombre", nuevoNombre);
                            actualizacion.put("Apellido", nuevoApellido);
                            actualizacion.put("email", nuevoEmail);
                            actualizacion.put("imgURL", nuevaImgURL);

                            FirebaseDatabase.getInstance().getReference().child("Programación Android")
                                    .child(getRef(position).getKey()).updateChildren(actualizacion)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(holder.img.getContext(), "Actualizado correctamente", Toast.LENGTH_SHORT).show();
                                            alertDialog.dismiss();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(holder.img.getContext(), "Error al actualizar", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(holder.img.getContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                alertDialog.show();
            }
        });

        holder.eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.nombre.getContext());
                builder.setTitle("¿Estás seguro de eliminarlo?");
                builder.setMessage("Eliminado");

                builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        FirebaseDatabase.getInstance().getReference().child("Programación Android")
                                .child(getRef(position).getKey()).removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.nombre.getContext(), "Eliminado correctamente", Toast.LENGTH_SHORT).show();

                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, getItemCount());
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(holder.nombre.getContext(), "Error al eliminar", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(holder.nombre.getContext(), "Cancelar", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.show();
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        CircleImageView img;
        TextView nombre, apellido, email;
        Button editar, eliminar;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img1);
            nombre = itemView.findViewById(R.id.nombreText);
            apellido = itemView.findViewById(R.id.apellidoText);
            email = itemView.findViewById(R.id.emailText);

            editar = itemView.findViewById(R.id.btn_edit);
            eliminar = itemView.findViewById(R.id.btn_eliminar);
        }
    }
}