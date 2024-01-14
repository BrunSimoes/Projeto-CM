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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;



public class fragment_login extends Fragment {
    TextView registerRed;
    EditText usernameLog,emailLog,PasswordLog;
    String userName,email,password,passwordConf;
    Button login;
    boolean validated_All;
    private SessionManager sessionManager;

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

    public boolean validate (String hPassword1, String email){
        boolean validated = true;
        boolean [] validate_All  = new boolean [2];

        //inicializar array
        for(int i=0; i<validate_All.length; i++){
            validate_All[i]=false;
        }

        //verificar se email existe
        if(email == null || email.isEmpty()){
            validate_All[0]=false;
            emailLog.setError("campo vazio");
        }else{
            //verificar se email tem a estrutura adequada
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                validate_All[0]=false;
                emailLog.setError("Email sobre Formato username@email.xxx");
            }else{
                validate_All[0]=true;
            }
        }

        //verificar password existe
        if(hPassword1 == null || hPassword1.isEmpty()){
            validate_All[1]=false;
            PasswordLog.setError("campo vazio");
        }else if(hPassword1.length()<=8){
            validate_All[1]=false;
            PasswordLog.setError("Password deve mais de 8 caracteres");
        }else{
            validate_All[1]=true;
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

    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public void firebaseData(Context context, FirebaseFirestore db) {
        this.db = db;
    }

    private void loginAccountFireBase(String email, String password) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .whereEqualTo("email", email)
                .whereEqualTo("password", password)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            sessionManager.setLoggedIn(true);
                            sessionManager.setUserEmail(email);
                            for (DocumentSnapshot document : task.getResult()) {
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.fragment, MemorieGame_fg.class, null)
                                        .commit();
                            }
                        }
                    }
                });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(requireContext());

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        registerRed = view.findViewById(R.id.registerRed);

        emailLog = view.findViewById(R.id.email);
        PasswordLog = view.findViewById(R.id.password);

        login = view.findViewById(R.id.login);


        login.setOnClickListener( (v) -> {
            email = emailLog.getText().toString();
            password = PasswordLog.getText().toString();

            validated_All = validate(password,email);

            if(validated_All){
                loginAccountFireBase(email,hashPassword(password));
            }
        });

        registerRed.setOnClickListener( (v) -> {
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment, fragment_register.class, null)
                    .commit();
        });

    }


}