package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private TextView textViewKnownAs;
    private TextView textViewIngredients;
    private TextView textViewOrigin;
    private TextView textViewDescription;

    //private Sandwich sandwich;

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);

        textViewKnownAs = (TextView)findViewById(R.id.also_known_tv);
        textViewIngredients = (TextView)findViewById(R.id.ingredients_tv);
        textViewOrigin = (TextView)findViewById(R.id.origin_tv);
        textViewDescription = (TextView)findViewById(R.id.description_tv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        Picasso.get()
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());

    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {

        List<String> alsoKnownAs = sandwich.getAlsoKnownAs();
        String alsoKnownAsStr = "";
        for (int i=0; i<alsoKnownAs.size(); i++){
            if (i>0){
                alsoKnownAsStr += ", ";
            }
            alsoKnownAsStr += alsoKnownAs.get(i);
        }
        textViewKnownAs.setText(alsoKnownAsStr);

        List<String> ingredients = sandwich.getIngredients();
        String ingredientsStr = "";
        for (int i=0; i<ingredients.size(); i++){
            if (i>0){
                ingredientsStr += ", ";
            }
            ingredientsStr += ingredients.get(i);
        }
        textViewIngredients.setText(ingredientsStr);

        textViewDescription.setText(sandwich.getDescription());

        textViewOrigin.setText(sandwich.getPlaceOfOrigin());
    }
}
