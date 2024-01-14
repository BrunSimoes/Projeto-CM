package com.example.memoriegame;


import android.content.Context;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.Nullable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.FirebaseFirestore;

public class fragment_register extends Fragment {

    EditText usernameCreate,emailCreate;
    EditText [] PasswordCreate = new EditText[2];
    String userName,email,password,passwordConf;
    TextView LoginRed;
    Button regist;

    Boolean validated_All=false;

    private DatabaseHelper dbHelper;

    public static String hashPassword(String password) {
        String saltedPassword = password;

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(saltedPassword.getBytes());
            byte[] hashedBytes = messageDigest.digest();


            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : hashedBytes) {
                stringBuilder.append(String.format("%02x", b));
            }

            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public void firebaseData(Context context, FirebaseFirestore db) {
        this.db = db;
    }

    private void createFirebase(String username, String email, String password) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("username", username);
        userMap.put("email", email);
        userMap.put("password", password);

        // Add a user to the "users" collection
        db.collection("users")
                .add(userMap)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(requireContext(), "Registration successful", Toast.LENGTH_SHORT).show();

                    getFragmentManager().beginTransaction()
                            .replace(R.id.fragment, fragment_login.class, null)
                            .commit();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Registration failed", Toast.LENGTH_SHORT).show();
                });
    }

    public boolean validate (String hPassword2, String hPassword1, String userName, String email){
        boolean validated = true;
        boolean [] validate_All  = new boolean [5];

        //inicializar array
        for(int i=0; i<validate_All.length; i++){
            validate_All[i]=false;
        }

        //verificar se username existe
        if(userName == null ||userName.isEmpty()){
            usernameCreate.setError("campo vazio");
            validate_All[0]=false;
        }else{
            validate_All[0]=true;
        }

        //verificar se email existe
        if(email == null ||email.isEmpty()){
            validate_All[1]=false;
            emailCreate.setError("campo vazio");
        }else{
            //verificar se email tem a estrutura adequada
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                validate_All[1]=false;
                emailCreate.setError("Email sobre Formato username@email.xxx");
            }else{
                validate_All[1]=true;
            }
        }

        //verificar password existe
        if(hPassword1 == null || hPassword1.isEmpty()){
            validate_All[2]=false;
            PasswordCreate[0].setError("campo vazio");
        }else if(hPassword1.length()<=8){
            validate_All[2]=false;
            PasswordCreate[0].setError("Password deve mais de 8 caracteres");
        }else{
            validate_All[2]=true;
        }


        if(hPassword2 == null || hPassword2.isEmpty()){
            validate_All[3]=false;
            PasswordCreate[1].setError("campo vazio");
        }else{
            validate_All[3]=true;
        }

        //verificar password é igual a confirmação
        if(hPassword1.equals(hPassword2)){
            validate_All[4]=true;
            //PasswordCreate[1].setError("Password condiz com a confirmação");
        }else{
            PasswordCreate[1].setError("Password não condiz com a confirmação");
            validate_All[4]=false;
        }

        //Percorrer todas as verificações para ver se existe alguma vazia ou preenhida incorretamete
        for(int i=0; i<validate_All.length; i++){
            if(!validate_All[i]){
                validated=false;
                //termina o ciclo
                break;
            }
        }
        return validated;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register, container, false);

        dbHelper = new DatabaseHelper(requireActivity().getApplicationContext());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usernameCreate = view.findViewById(R.id.usernameCreate);
        emailCreate = view.findViewById(R.id.emailCreate);
        PasswordCreate[0] = view.findViewById(R.id.passwordCreate);
        PasswordCreate[1] = view.findViewById(R.id.passwordCreateConfirm);

        LoginRed = view.findViewById(R.id.LoginRed);
        regist = view.findViewById(R.id.registCreate);


        regist.setOnClickListener( (v) -> {

            //Load Text Content
            userName = usernameCreate.getText().toString();
            email = emailCreate.getText().toString();
            password = PasswordCreate[0].getText().toString();
            passwordConf = PasswordCreate[1].getText().toString();

            validated_All = validate(password,passwordConf,userName,email);


            if(validated_All) {
                createFirebase(userName, email, hashPassword(password));
                dbHelper.registerUser(email, userName, hashPassword(password));
            }
        });

        LoginRed.setOnClickListener( (v) -> {
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment, fragment_login.class, null)
                    .commit();
        });
    }
}
