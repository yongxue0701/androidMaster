package sg.edu.nus.learnandroid;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Yongxue on 21/10/17.
 */

public class ProfileEditRVAdapter extends RecyclerView.Adapter {

    private Context context;
    private Activity activity;
    private List<ItemView> editProfileList;

    public ProfileEditRVAdapter(List<ItemView> editProfileList, Context context, Activity activity) {
        this.editProfileList = editProfileList;
        this.context = context;
        this.activity = activity;
    }

    public static class EditTextTypeViewHolder extends RecyclerView.ViewHolder {

        public TextView buttonNameEditTextTV;
        public EditText buttonNameContentET;

        public EditTextTypeViewHolder(View itemView) {
            super(itemView);

            this.buttonNameEditTextTV = (TextView) itemView.findViewById(R.id.profileEdit_edittext_buttonName_TV);
            this.buttonNameContentET = (EditText) itemView.findViewById(R.id.profileEdit_edittext_ET);
        }
    }

    public static class ButtonWithTextTypeViewHolder extends RecyclerView.ViewHolder {

        public TextView buttonNameButtonWithTextTV;
        public TextView buttonNameContentTV;
        public PercentRelativeLayout buttonWithTextPRL;

        public ButtonWithTextTypeViewHolder(View itemView) {
            super(itemView);

            this.buttonNameButtonWithTextTV = (TextView) itemView.findViewById(R.id.profileEdit_btnWithText_buttonName_TV);
            this.buttonNameContentTV = (TextView) itemView.findViewById(R.id.profileEdit_btnWithText_content_TV);
            this.buttonWithTextPRL = (PercentRelativeLayout) itemView.findViewById(R.id.profileEdit_btnWithText_recyclerview_PRL);
        }
    }

    public static class ButtonTypeViewHolder extends RecyclerView.ViewHolder {

        public TextView buttonNameButtonTV;
        public PercentRelativeLayout buttonPRL;

        public ButtonTypeViewHolder(View itemView) {
            super(itemView);

            this.buttonNameButtonTV = (TextView) itemView.findViewById(R.id.profileEdit_button_buttonName_TV);
            this.buttonPRL = (PercentRelativeLayout) itemView.findViewById(R.id.profileEdit_button_recyclerview_PRL);
        }
    }

    public static class ImageTypeViewHolder extends RecyclerView.ViewHolder {

        public TextView buttonNameImageTV;
        public FloatingActionButton userImageFAB;

        public ImageTypeViewHolder(View itemView) {
            super(itemView);

            this.buttonNameImageTV = (TextView) itemView.findViewById(R.id.profileEdit_image_buttonName_TV);
            this.userImageFAB = (FloatingActionButton) itemView.findViewById(R.id.profileEdit_image_userImage_FAB);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_edit_edittext_recycler_view, parent, false);
                return new EditTextTypeViewHolder(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_edit_btn_with_text_recycler_view, parent, false);
                return new ButtonWithTextTypeViewHolder(view);
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_edit_image_recycler_view, parent, false);
                return new ImageTypeViewHolder(view);
            case 3:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_edit_btn_recycler_view, parent, false);
                return new ButtonTypeViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {

        ItemView itemView = editProfileList.get(position);
        UserAccountDB userAccountDB = new UserAccountDB(context);

        if (itemView != null) {
            switch (viewHolder.getItemViewType()) {

                case 0:
                    EditTextTypeViewHolder editTextTypeViewHolder = (EditTextTypeViewHolder) viewHolder;
                    editTextTypeViewHolder.buttonNameEditTextTV.setText(itemView.getViewName());

                    if (itemView.getViewName().equals("Username")) {
                        userAccountDB.open();
                        Cursor mCursor = userAccountDB.getRecordByIsLogin(1);

                        if (mCursor != null && mCursor.moveToFirst() && (mCursor.getCount() == 1)) {
                            do {
                                String username = mCursor.getString(mCursor.getColumnIndex("username"));
                                editTextTypeViewHolder.buttonNameContentET.setText(username);
                            } while (mCursor.moveToNext());
                        }

                        mCursor.close();
                        userAccountDB.close();
                    }
                    if (itemView.getViewName().equals("Email")) {
                        userAccountDB.open();
                        Cursor mCursor = userAccountDB.getRecordByIsLogin(1);

                        if (mCursor != null && mCursor.moveToFirst() && (mCursor.getCount() == 1)) {
                            do {
                                String username = mCursor.getString(mCursor.getColumnIndex("email"));
                                editTextTypeViewHolder.buttonNameContentET.setText(username);
                            } while (mCursor.moveToNext());
                        }

                        mCursor.close();
                        userAccountDB.close();
                    }

                    break;

                case 1:
                    ButtonWithTextTypeViewHolder buttonWithTextTypeViewHolder = (ButtonWithTextTypeViewHolder) viewHolder;
                    buttonWithTextTypeViewHolder.buttonNameButtonWithTextTV.setText(itemView.getViewName());

                    if (itemView.getViewName().equals("Gender")) {
                        userAccountDB.open();
                        Cursor mCursor = userAccountDB.getRecordByIsLogin(1);

                        if (mCursor != null && mCursor.moveToFirst() && (mCursor.getCount() == 1)) {
                            do {
                                String username = mCursor.getString(mCursor.getColumnIndex("gender"));
                                buttonWithTextTypeViewHolder.buttonNameContentTV.setText(username);
                            } while (mCursor.moveToNext());
                        }

                        mCursor.close();
                        userAccountDB.close();
                    }

                    buttonWithTextTypeViewHolder.buttonWithTextPRL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            initiateDialog(view);
                        }
                    });

                    break;

                case 2:
                    ImageTypeViewHolder imageTypeViewHolder = (ImageTypeViewHolder) viewHolder;
                    imageTypeViewHolder.buttonNameImageTV.setText(itemView.getViewName());
                    imageTypeViewHolder.userImageFAB.setImageResource(R.drawable.ic_forum_black_24dp);
                    break;

                case 3:
                    ButtonTypeViewHolder buttonTypeViewHolder = (ButtonTypeViewHolder) viewHolder;
                    buttonTypeViewHolder.buttonNameButtonTV.setText(itemView.getViewName());

                    if (itemView.getViewName().equals("Change Password")) {
                        buttonTypeViewHolder.buttonPRL.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent myIntent = new Intent(context, ChangePasswordActivity.class);
                                context.startActivity(myIntent);
                                activity.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                            }
                        });
                    }
                    break;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (editProfileList != null) {
            ItemView itemView = editProfileList.get(position);
            if (itemView != null) {
                if (itemView.getViewType().equals("ET")) {
                    return 0;
                } else if (itemView.getViewType().equals("BtnWithText")) {
                    return 1;
                } else if (itemView.getViewType().equals("Img")) {
                    return 2;
                } else if (itemView.getViewType().equals("Btn")) {
                    return 3;
                }
            }
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return editProfileList.size();
    }

    private void initiatePopupWindow(View view) {

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.gender_popup, null);
        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.showAtLocation(popupView, Gravity.BOTTOM, 0, 0);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.update();

        LinearLayout cancelLL = (LinearLayout) popupView.findViewById(R.id.cancel_textview_LL);
        cancelLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        TextView genderPopupNullTV = (TextView) popupView.findViewById(R.id.gender_popup_null_TV);
        genderPopupNullTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        TextView genderPopupMaleTV = (TextView) popupView.findViewById(R.id.gender_popup_male_TV);
        genderPopupMaleTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        TextView genderPopupFemaleTV = (TextView) popupView.findViewById(R.id.gender_popup_female_TV);
        genderPopupFemaleTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void initiateDialog(View view) {

        final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.gender_popup);

        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogWindow.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

        LinearLayout cancelLL = (LinearLayout) dialog.findViewById(R.id.cancel_textview_LL);
        cancelLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
