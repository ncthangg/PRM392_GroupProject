package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ArrayAdapter;

import java.util.List;

import com.example.main.models.Category;

public class CategoryAdapter extends ArrayAdapter<Category> {
    private LayoutInflater inflater;

    public CategoryAdapter(Context context, List<Category> categories) {
        super(context, 0, categories);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
        }
        TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
        Category category = getItem(position);
        if (category != null) {
            textView.setText(category.getName());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }
        TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
        Category category = getItem(position);
        if (category != null) {
            textView.setText(category.getName());
        }
        return convertView;
    }
}
