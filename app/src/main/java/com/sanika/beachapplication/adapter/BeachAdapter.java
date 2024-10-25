package com.sanika.beachapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.sanika.beachapplication.R;
import com.sanika.beachapplication.activity.BeachDetailsActivity;
import com.sanika.beachapplication.model.Beach;
import com.sanika.beachapplication.model.BeachFeature;
import com.sanika.beachapplication.model.BeachProperties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

public class BeachAdapter extends RecyclerView.Adapter<BeachAdapter.BeachViewHolder> {
    private Context context;
    private List<Beach> beachList; // Current list of beach features
    private List<Beach> originalList; // Original list of beach features for filtering
    private String language; // Original list of beach features for filtering
    private Translator translator; // ML Kit translator

    public BeachAdapter(Context context, List<Beach> beachList,String language) {
        this.context = context;
        this.language = language;
        this.beachList = beachList; // Initialize with a copy of the original list
        this.originalList = beachList; // Keep a separate copy of the original list

        // Set up the ML Kit translator for the selected language
        TranslatorOptions options = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH) // Assuming the original text is in English
                .setTargetLanguage(language) // Target language, e.g., "hi" for Hindi
                .build();
        translator = Translation.getClient(options);

        // Download the language model if needed
        translator.downloadModelIfNeeded().addOnSuccessListener(unused -> {
            // Model downloaded successfully, ready for translation
        }).addOnFailureListener(e -> {
            // Handle the error
            e.printStackTrace();
        });
    }

    @NonNull
    @Override
    public BeachViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_beach, parent, false);
        return new BeachViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BeachViewHolder holder, int position) {
        Beach beach = beachList.get(position);
            // If English, just set the original text
            holder.textViewBeachName.setText(beach.getName());
            holder.textViewBeachDescription.setText(beach.getLocation());

//        holder.textViewBeachName.setText(beachProperties.getName());
//        holder.textViewBeachDescription.setText(beachProperties.getName());

        // Load beach image using Glide
        Glide.with(context)
                .load(beach.getImageUrl())  // Ensure this is the correct URL for the image
                .placeholder(R.drawable.img3) // Optional: Placeholder image while loading
                .into(holder.imageViewBeachImage);

        // Set item click listener
        holder.imageView2.setOnClickListener(v -> {
            Intent intent = new Intent(context, BeachDetailsActivity.class);
            intent.putExtra("BEACH_NAME", beach.getName());
            intent.putExtra("BEACH_Url", beach.getImageUrl());
            intent.putExtra("BEACH_LATITUDE", beach.getLocation()); // Latitude
            intent.putExtra("BEACH_LONGITUDE", beach.getLocation()); // Longitude
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return beachList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public Filter filterStateTypeAndSort() {
        return beachFilter;
    }

    private Filter beachFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<Beach> filteredResults = new ArrayList<>();

            // Check if the constraint is null or empty
            if (constraint == null || constraint.length() == 0) {
                // If no filter is applied, return the original list
                filteredResults.addAll(originalList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                // Perform filtering using a traditional for loop
                for (Beach beach : originalList) {
                    // Ensure that the field you're checking exists and is not null
                    if (beach.getName() != null &&
                            beach.getName().toLowerCase().contains(filterPattern)) {
                        filteredResults.add(beach);
                    }
                }
            }

            // Set the filtered results
            results.values = filteredResults;
            results.count = filteredResults.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            // Clear the current list
            beachList.clear();

            // Only add results if values is not null
            if (results.values != null) {
                beachList.addAll((Collection<? extends Beach>) results.values);
            }

            // Notify that the data has changed
            notifyDataSetChanged();
        }
    };

    public static class BeachViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewBeachImage;
        ImageView imageView2;
        TextView textViewBeachName;
        TextView textViewBeachDescription;

        public BeachViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewBeachImage = itemView.findViewById(R.id.imageViewBeachImage);
            imageView2 = itemView.findViewById(R.id.imageView2);
            textViewBeachName = itemView.findViewById(R.id.textViewBeachName);
            textViewBeachDescription = itemView.findViewById(R.id.country_name);
        }
    }
}
