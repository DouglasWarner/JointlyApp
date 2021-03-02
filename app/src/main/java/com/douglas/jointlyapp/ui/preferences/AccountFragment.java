package com.douglas.jointlyapp.ui.preferences;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.douglas.jointlyapp.R;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.data.repository.UserRepository;
import com.douglas.jointlyapp.ui.preferences.JointlyPreferences;
import com.douglas.jointlyapp.ui.utils.CommonUtils;

public class AccountFragment extends PreferenceFragmentCompat {

    private User user;

    private EditTextPreference emailPreferences;
    private EditTextPreference namePreferences;
    private EditTextPreference phonePreferences;
    private EditTextPreference locationPreferences;
    private EditTextPreference descriptionPreferences;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.account_preferences);
        user = UserRepository.getInstance().getUser(JointlyPreferences.getInstance().getUser());

        initPreferences();

        initListenerPreferenceEmail();
        initListenerPreferenceName();
        initListenerPreferenceLocation();
        initListenerPreferencePhone();
        initListenerPreferenceDescription();
    }

    private void initPreferences() {
        emailPreferences = getPreferenceManager().findPreference(getString(R.string.key_user));
        namePreferences = getPreferenceManager().findPreference(getString(R.string.key_name));
        phonePreferences = getPreferenceManager().findPreference(getString(R.string.key_phone));
        locationPreferences = getPreferenceManager().findPreference(getString(R.string.key_location));
        descriptionPreferences = getPreferenceManager().findPreference(getString(R.string.key_description));
    }

    /**
     * Preferencia Email
     * Metodo que inicializa los listener de la preferencia Email
     */
    private void initListenerPreferenceEmail() {

        emailPreferences.setOnBindEditTextListener(editText -> {
            editText.setSingleLine(true);
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            // Esta linea debe ser la ultima para que se seleccione el texto
            editText.selectAll();
        });

        emailPreferences.setOnPreferenceChangeListener((preference, newValue) -> {

            String text = (String)newValue;

            if (noValidEmail(text))
                return false;

            user.setEmail((String)newValue);

            UserRepository.getInstance().update(user);
            Toast.makeText(getContext(), "Email actualizado con exito", Toast.LENGTH_SHORT).show();

            return true;
        });
    }

    /**
     * Preferencia Name
     * Metodo que inicializa los listener de la preferencia Name
     */
    private void initListenerPreferenceName() {

        namePreferences.setOnBindEditTextListener(editText -> {
            editText.setSingleLine(true);
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
            editText.selectAll();
        });

        namePreferences.setOnPreferenceChangeListener((preference, newValue) -> {
            String text = (String)newValue;

            if(TextUtils.isEmpty(text))
            {
                Toast.makeText(getContext(), "El nombre de usuario no puede estar vacio", Toast.LENGTH_SHORT).show();
                return false;
            }

            user.setName(text);
            UserRepository.getInstance().update(user);
            Toast.makeText(getContext(), "El nombre actualizado con exito", Toast.LENGTH_SHORT).show();

            return true;
        });
    }

    /**
     * Preferencia Phone
     * Metodo que inicializa los listener de la preferencia Phone
     */
    private void initListenerPreferencePhone() {

        phonePreferences.setOnBindEditTextListener(editText -> {
            editText.setSingleLine(true);
            editText.setInputType(InputType.TYPE_CLASS_PHONE);
            editText.selectAll();
        });

        phonePreferences.setOnPreferenceChangeListener((preference, newValue) -> {
            String text = (String)newValue;

            if(!TextUtils.isEmpty(text))
            {
                if(!CommonUtils.isPhoneValid(text)) {
                    Toast.makeText(getContext(), "El telefono no es valido", Toast.LENGTH_SHORT).show();
                    return false;
                }

                user.setPhone(text);
                UserRepository.getInstance().update(user);
                Toast.makeText(getContext(), "El telefono actualizado con exito", Toast.LENGTH_SHORT).show();
            }

            return true;
        });
    }

    /**
     * Preferencia Location
     * Metodo que inicializa los listener de la preferencia Location
     */
    private void initListenerPreferenceLocation() {
        locationPreferences.setOnBindEditTextListener(editText -> {
            editText.setSingleLine(true);
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
            editText.selectAll();
        });

        locationPreferences.setOnPreferenceChangeListener((preference, newValue) -> {

            String text = (String)newValue;

            user.setLocation(text);
            UserRepository.getInstance().update(user);
            Toast.makeText(getContext(), "El localidad actualizado con exito", Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    /**
     * Preferencia Description
     * Metodo que inicializa los listener de la preferencia Description
     */
    private void initListenerPreferenceDescription() {
        descriptionPreferences.setOnBindEditTextListener(editText -> {
            editText.setSingleLine(false);
            editText.selectAll();
        });

        descriptionPreferences.setOnPreferenceChangeListener((preference, newValue) -> {
            String text = (String)newValue;

            user.setDescription(text);
            UserRepository.getInstance().update(user);
            Toast.makeText(getContext(), "El descripcion actualizado con exito", Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    /**
     * Valida Email
     * Metodo que valida el texto introducido por el usuario de la preferencia Email
     */
    private boolean noValidEmail(String text) {
        if(TextUtils.isEmpty(text))
        {
            Toast.makeText(getContext(), "El email no puede estar vacio", Toast.LENGTH_SHORT).show();
            return true;
        }

        if(!CommonUtils.isEmailValid(text))
        {
            Toast.makeText(getContext(), "El email no es valido", Toast.LENGTH_SHORT).show();
            return true;
        }

        if(UserRepository.getInstance().getUser(text) != null)
        {
            Toast.makeText(getContext(), "El email ya esta en uso", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}
