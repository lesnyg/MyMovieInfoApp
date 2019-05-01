package com.lesnyg.mymovieinfoapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.lesnyg.mymovieinfoapp.models.Result;
import com.lesnyg.mymovieinfoapp.models.Search;
import com.lesnyg.mymovieinfoapp.repository.AppDatabase;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieViewModel extends AndroidViewModel {
    public MutableLiveData<List<Result>> filteredResult = new MutableLiveData<>();
    public MutableLiveData<List<Result>> searchResult = new MutableLiveData<>();

    private static final String MY_KEY = "0882850438bd0da4458576be7d4a447c";
    private static final String MY_COUNTRY = "ko-KR";
    public MutableLiveData<List<Result>> favoritList = new MutableLiveData<>();
    private List<Result> mResults = new ArrayList<>();

    // 즐겨찾기
    public LiveData<List<Result>> result;
    public AppDatabase mDb;
     private Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
     private MovieService service = retrofit.create(MovieService.class);

    public MovieViewModel(@NonNull Application application) {
        super(application);
        mDb = Room.databaseBuilder(application,
                AppDatabase.class, "database-name")
                .allowMainThreadQueries()   // MainThread에서 DB 사용 가능
                .build();

        result = mDb.resultDao().getAll();

    }


    public void fetchUpComing(){
         service.getUpComing(MY_KEY, MY_COUNTRY).enqueue(new Callback<Search>() {
             @Override
             public void onResponse(Call<Search> call, Response<Search> response) {
                 if(response.body() != null) {
                     filteredResult.setValue(response.body().getResults());
                 }
             }
             @Override
             public void onFailure(Call<Search> call, Throwable t) {

             }
         });
     }
     public  void fetchPopular(){
        service.getPopular(MY_KEY,MY_COUNTRY).enqueue(new Callback<Search>() {
            @Override
            public void onResponse(Call<Search> call, Response<Search> response) {
                if(response.body() != null) {
                    filteredResult.setValue(response.body().getResults());
                }
            }

            @Override
            public void onFailure(Call<Search> call, Throwable t) {

            }
        });
     }

     public void fetchSearch(String quary){
        service.getSearch(MY_KEY,quary,MY_COUNTRY).enqueue(new Callback<Search>() {
            @Override
            public void onResponse(Call<Search> call, Response<Search> response) {
                if(response.body() != null) {
                    filteredResult.setValue(response.body().getResults());
                }
            }

            @Override
            public void onFailure(Call<Search> call, Throwable t) {

            }
        });
     }

     //즐겨찾기 추가하기
     public void addFavorit(Result result){
         mDb.resultDao().insertFavorite(result);
     }
     //즐겨찾기 삭제하기
     public void deleteFavorit(Result result){
         mDb.resultDao().deleteFavorite(result);
     }
}
