//package com.douglas.jointlyapp.ui.preferences;
//
//import android.os.Bundle;
//import android.text.InputType;
//import android.widget.EditText;
//
//import androidx.annotation.NonNull;
//import androidx.preference.EditTextPreference;
//import androidx.preference.PreferenceFragmentCompat;
//
//import com.douglas.jointlyapp.R;
//
//import static android.os.Build.VERSION_CODES.R;
//
//public class AccountFragment extends PreferenceFragmentCompat {
//
//    @Override
//    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
//        addPreferencesFromResource(R.xml.account_preferences);
//        initPreferenceUser();
//        initPreferencePassword();
//    }
//
//    private void initPreferencePassword() {
//        EditTextPreference passwordPreferences = getPreferenceManager().findPreference(getString(R.string.key_password));
//
//        passwordPreferences.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
//            @Override
//            public void onBindEditText(@NonNull EditText editText) {
//                editText.setSingleLine(true);
//                // Ademas de solo introducir caracteres de texto, se muestren los asteriscos para no visualizar
//                // el texto
//                editText.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
//                // Esta linea debe ser la ultima para que se seleccione el texto
//                editText.selectAll();
//            }
//        });
//    }
//
//    private void initPreferenceUser() {
//        EditTextPreference userPreferences = getPreferenceManager().findPreference(getString(R.string.key_user));
//
//        userPreferences.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
//            @Override
//            public void onBindEditText(@NonNull EditText editText) {
//                editText.setSingleLine(true);
//                editText.setInputType(InputType.TYPE_CLASS_TEXT);
//                // Esta linea debe ser la ultima para que se seleccione el texto
//                editText.selectAll();
//            }
//        });
//    }
//}
