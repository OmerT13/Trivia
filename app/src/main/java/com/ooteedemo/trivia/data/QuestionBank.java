package com.ooteedemo.trivia.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.ooteedemo.trivia.controller.AppController;
import com.ooteedemo.trivia.model.Question;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import static com.ooteedemo.trivia.controller.AppController.TAG;

public class QuestionBank {
    ArrayList<Question> questionArrayList = new ArrayList<>();
    private String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";

    public List<Question> getQuestions() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                (JSONArray) null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "onResponse: "+response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                }
            }
        );
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
        return null;
    }

}
