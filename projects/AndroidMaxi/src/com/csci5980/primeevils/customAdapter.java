package com.csci5980.primeevils;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class customAdapter extends BaseAdapter {
	 
	 private static ArrayList<ListItem> listItems;
	 private LayoutInflater mInflater;

	 public customAdapter(Context context, ArrayList<ListItem> results) {
		 listItems = results;
		 mInflater = LayoutInflater.from(context);
	 }

	 public int getCount() {
		 return listItems.size();
	 }

	 public Object getItem(int position) {
		 return listItems.get(position);
	 }

	 public long getItemId(int position) {
		 return position;
	 }

	 public View getView(int position, View convertView, ViewGroup parent) {
		 ViewHolder holder;
		 if (convertView == null) {
		 	 convertView = mInflater.inflate(R.layout.profile_list_layout, null);
		 	 holder = new ViewHolder();
		 	 holder.itemString = (TextView) convertView.findViewById(R.id.ItemString);
		 	 holder.quantity = (TextView) convertView.findViewById(R.id.Quantity);
		 	 holder.boughtBy = (TextView) convertView.findViewById(R.id.BoughtBy);
		 	 
			 convertView.setTag(holder);
		  }
		  else {
			  holder = (ViewHolder) convertView.getTag();
		  }
		  
		  holder.itemString.setText(listItems.get(position).getItemName() + " from " + listItems.get(position).getShopName());
		  holder.quantity.setText("Quantity: " + listItems.get(position).getQuantity());
		  if( !("".equals(listItems.get(position).getBoughtBy())) )
			  holder.boughtBy.setText("Bought By: " + listItems.get(position).getBoughtBy());
	
		  return convertView;
	 }

	 static class ViewHolder {
		 TextView itemString;
		 TextView quantity;
		 TextView boughtBy;
	 }
}