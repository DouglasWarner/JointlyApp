package com.douglas.jointlyapp.ui.preferences;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.douglas.jointlyapp.R;
import com.vansuita.materialabout.builder.AboutBuilder;
import com.vansuita.materialabout.views.AboutView;

public class AboutUsFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        AboutView view = AboutBuilder.with(getActivity())
                .setPhoto(R.mipmap.ic_foto_developer)
                .setCover(R.mipmap.profile_cover)
                .setName("Douglas Warner Jurado Peña")
                .setSubTitle("Desarrollador Android")
                .setBrief("Estudiante de Desarrollo de Aplicaciones Multiplataforma.")
                .setAppIcon(R.mipmap.ic_app)
                .setAppName(R.string.app_name)
                .addGitHubLink("DouglasWarner")
                .addLinkedInLink("douglas-jurado-bb2274199")
                .addEmailLink("douglaswarner.jo@gmail.com")
                .setVersionNameAsAppSubTitle()
                .addShareAction(R.string.app_name)
                .setWrapScrollView(true)
                .setLinksAnimated(true)
                .setDividerDashGap(13)
                .setLinksColumnsCount(4)
                .setActionsColumnsCount(2)
                .addFeedbackAction("douglaswarner.jp@gmail.com")
                .setShowAsCard(true)
                .build();


//        .addUpdateAction()
//        .addMoreFromMeAction("")
//        .addFiveStarsAction()
//        .addPrivacyPolicyAction("http...")
//        .addIntroduceAction((Intent) null)
//        .addGooglePlayStoreLink("")
//        .addHelpAction((Intent) null)
//        .addChangeLogAction((Intent) null)
//        .addRemoveAdsAction((Intent) null)
//        .addDonateAction((Intent) null)

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}