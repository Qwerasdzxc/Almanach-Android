package com.code_dream.almanach.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.code_dream.almanach.R;
import com.code_dream.almanach.models.SubjectCategoryItem;
import com.code_dream.almanach.utility.Utility;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

/**
 * Created by Qwerasdzxc on 6/21/17.
 */

public class SubjectCategoryAdapter extends ExpandableRecyclerViewAdapter<SubjectCategoryAdapter.SubjectCategoryViewHolder, SubjectCategoryAdapter.SubjectCategoryItemViewHolder> {

    public interface EventItemClickListener {

        void onEventItemClick(SubjectCategoryItem item);
    }

    public EventItemClickListener itemClickListener;

    public SubjectCategoryAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    public void setItemClickListener(EventItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public SubjectCategoryViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        return new SubjectCategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_subject_cat_header, parent, false));
    }

    @Override
    public SubjectCategoryItemViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        return new SubjectCategoryItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_subject_cat_item, parent, false));
    }

    @Override
    public void onBindChildViewHolder(SubjectCategoryItemViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        SubjectCategoryItem subjectCategoryItem = (SubjectCategoryItem) group.getItems().get(childIndex);

        holder.setCategoryItemTitle(subjectCategoryItem.getTitle());
        holder.setSubjectCategoryItem(subjectCategoryItem);
        holder.setCategoryItemGradeImage(Utility.getImageDrawableByGrade(subjectCategoryItem.getCalendarEvent().getGrade()));
    }

    @Override
    public void onBindGroupViewHolder(SubjectCategoryViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setCategoryTitle(group);
        holder.setCategoryItemItemCount(group.getItemCount());
    }

    public static class SubjectCategoryViewHolder extends GroupViewHolder {

        @BindView(R.id.subject_profile_cat_header_tv) TextView titleTV;
        @BindView(R.id.subject_profile_cat_header_item_count_tv) TextView itemCountTV;
        @BindView(R.id.subject_profile_cat_header_arrow) ImageView arrowIMG;

        public SubjectCategoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setCategoryTitle(ExpandableGroup group){
            titleTV.setText(group.getTitle());
        }

        public void setCategoryItemItemCount(int itemCount){
            itemCountTV.setText("(" + itemCount + ")");
        }

        @Override
        public void expand() {
            animateExpand();
        }

        @Override
        public void collapse() {
            animateCollapse();
        }

        private void animateExpand() {
            RotateAnimation rotate =
                    new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(300);
            rotate.setFillAfter(true);

            arrowIMG.startAnimation(rotate);
        }

        private void animateCollapse() {
            RotateAnimation rotate =
                    new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(300);
            rotate.setFillAfter(true);

            arrowIMG.startAnimation(rotate);
        }
    }

    public class SubjectCategoryItemViewHolder extends ChildViewHolder implements View.OnClickListener {

        @BindView(R.id.subject_profile_cat_item_title_tv) TextView titleTV;
        @BindView(R.id.subject_profile_cat_item_img) ImageView itemIMG;

        SubjectCategoryItem subjectCategoryItem;

        public SubjectCategoryItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        public void setCategoryItemTitle(String title) {
            titleTV.setText(title);
        }

        public void setSubjectCategoryItem(SubjectCategoryItem item) {
            this.subjectCategoryItem = item;
        }

        public void setCategoryItemGradeImage(int imageID) {
            itemIMG.setImageResource(imageID);
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onEventItemClick(subjectCategoryItem);
        }
    }
}
