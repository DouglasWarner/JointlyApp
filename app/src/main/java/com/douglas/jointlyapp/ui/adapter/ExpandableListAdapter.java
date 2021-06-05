//package com.douglas.jointlyapp.ui.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseExpandableListAdapter;
//import android.widget.TextView;
//
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.douglas.jointlyapp.R;
//import com.douglas.jointlyapp.data.model.Initiative;
//
//import java.util.HashMap;
//import java.util.List;
//
//public class ExpandableListAdapter extends BaseExpandableListAdapter implements InitiativeAdapter.ManageInitiative {
//
//    Context context;
//    HashMap<String, List<String>> listGroup;
//    HashMap<String, List<Initiative>> listItem;
//    String type;
//
//    public ExpandableListAdapter(Context context,  HashMap<String, List<String>> listGroup, HashMap<String, List<Initiative>> listItem) {
//        this.context = context;
//        this.listGroup = listGroup;
//        this.listItem = listItem;
//    }
//
//    @Override
//    public int getGroupCount() {
//        return listGroup.size();
//    }
//
//    @Override
//    public int getChildrenCount(int groupPosition) {
//        return listItem.keySet().toArray().length;
//    }
//
//    @Override
//    public Object getGroup(int groupPosition) {
//        return listGroup.keySet().toArray()[groupPosition];
//    }
//
//    @Override
//    public Object getChild(int groupPosition, int childPosition) {
//        return listItem.keySet().toArray()[childPosition];
//    }
//
//    @Override
//    public long getGroupId(int groupPosition) {
//        return groupPosition;
//    }
//
//    @Override
//    public long getChildId(int groupPosition, int childPosition) {
//        return childPosition;
//    }
//
//    @Override
//    public boolean hasStableIds() {
//        return false;
//    }
//
//    @Override
//    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
//        String group = (String) getGroup(groupPosition);
//        if(convertView == null) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expand_list_header, null);
//            convertView = view;
//        }
//
//        TextView tvHeader = convertView.findViewById(R.id.tvHeader);
//        tvHeader.setText(group);
//
//        return convertView;
//    }
//
//    @Override
//    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
//        String type = (String) getChild(groupPosition, childPosition);
//        List<Initiative> initiative = ((List<Initiative>) listItem.get(type));
//        if(convertView == null) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expand_list_item, null);
//            convertView = view;
//        }
//
//        TextView tvTitle = convertView.findViewById(R.id.tvTitleItem);
//        RecyclerView rvItem = convertView.findViewById(R.id.rvItem);
//        InitiativeAdapter adapter = new InitiativeAdapter(initiative, this, "");
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(parent.getContext(), RecyclerView.HORIZONTAL, false);
//
//        switch (type){
//            case "creados":
//                tvTitle.setText("En curso");
//                break;
//            case "unidos":
//                tvTitle.setText("Historial");
//                break;
//        }
//
//        rvItem.setLayoutManager(layoutManager);
//        rvItem.setAdapter(adapter);
//
//        return convertView;
//    }
//
//    @Override
//    public boolean isChildSelectable(int groupPosition, int childPosition) {
//        return true;
//    }
//
//    @Override
//    public void onClick(View initiative, String status) {
//
//    }
//}
