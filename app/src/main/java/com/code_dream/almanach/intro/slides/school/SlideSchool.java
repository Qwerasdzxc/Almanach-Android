package com.code_dream.almanach.intro.slides.school;

import android.os.Bundle;
import android.support.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.code_dream.almanach.R;
import com.code_dream.almanach.models.School;

import java.util.List;

import agency.tango.materialintroscreen.SlideFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Qwerasdzxc on 7/5/17.
 */

public class SlideSchool extends SlideFragment implements SchoolContract.View {

    @BindView(R.id.intro_header_text)
    TextView headerText;

    @BindView(R.id.intro_school_floating_search_view)
    FloatingSearchView schoolSearchView;

    @BindView(R.id.intro_school_name_edit_text)
    EditText schoolNameEdit;


    private SchoolPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_intro_slide_school, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new SchoolPresenter(this);

        initialize();
    }

    private void initialize() {
        schoolSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                presenter.onQueryChanged(oldQuery, newQuery.trim());
            }
        });

        schoolSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                String queryText = schoolSearchView.getQuery();

                schoolSearchView.clearSearchFocus();
                schoolSearchView.setSearchText(searchSuggestion.getBody());

                // Notifying the presenter that the user clicked on one of the suggestions.
                presenter.onSuggestionClick(searchSuggestion.getBody(), queryText);
            }

            @Override
            public void onSearchAction(String currentQuery) { }
        });
    }


    @Override
    public void switchToAddSchoolLayout(String typedText) {
        schoolSearchView.setVisibility(View.GONE);
        schoolNameEdit.setVisibility(View.VISIBLE);

        schoolNameEdit.setText(typedText.substring(0, 1).toUpperCase() + typedText.substring(1));
        headerText.setText("Please enter new school name:");
    }


    @Override
    public void updateNewSchoolText(String newSchoolText) {
        schoolNameEdit.setText(newSchoolText);
    }

    @Override
    public String getSearchQuery() {
        return schoolSearchView.getQuery();
    }

    @Override
    public String getNewSchoolText() {
        return schoolNameEdit.getText().toString();
    }

    @Override
    public void updateSearchView(List<School> receivedSchools) {
        schoolSearchView.swapSuggestions(receivedSchools);
    }

    @Override
    public void enableSearchProgressBar(boolean enable) {
        if (enable)
            schoolSearchView.showProgress();
        else
            schoolSearchView.hideProgress();
    }

    @Override
    public int backgroundColor() {
        return R.color.colorPrimary;
    }

    @Override
    public int buttonsColor() {
        return R.color.transparent;
    }

    @Override
    public boolean canMoveFurther() {
        return presenter.canMoveFurther();
    }

    @Override
    public String cantMoveFurtherErrorMessage() {
        return presenter.getMovingPreventionErrorMessage();
    }
}
