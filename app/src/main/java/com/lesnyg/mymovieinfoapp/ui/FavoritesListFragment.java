package com.lesnyg.mymovieinfoapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.lesnyg.mymovieinfoapp.DetailActivity;
import com.lesnyg.mymovieinfoapp.MovieViewModel;
import com.lesnyg.mymovieinfoapp.R;
import com.lesnyg.mymovieinfoapp.adapter.MovieFavoriteAdapter;
import com.lesnyg.mymovieinfoapp.models.Result;

import java.util.List;


public class FavoritesListFragment extends Fragment implements MovieFavoriteAdapter.OnFavoriteClickListener {

    private Result mResult;
    private MovieViewModel mViewModel;
    private SearchView mSearchView;

    public FavoritesListFragment() {
        setHasOptionsMenu(true);
    }

    public static FavoritesListFragment newInstance() {
        FavoritesListFragment fragment = new FavoritesListFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = ViewModelProviders.of(requireActivity())
                .get(MovieViewModel.class);

        MovieFavoriteAdapter adapter = new MovieFavoriteAdapter(new MovieFavoriteAdapter.OnFavoriteClickListener() {
            @Override
            public void onFavoriteClick(Result item) {
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager()
                        .beginTransaction();
                transaction.replace(R.id.fragment_main, MovieDetailFragment.newInstance(item));
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_favorites);
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                mResult = adapter.mList.get(viewHolder.getAdapterPosition());
                mViewModel.deleteFavorite(mResult);
            }
        });
        helper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);


        mViewModel.result.observe(requireActivity(), (List<Result> items) -> {
            mViewModel.results = items;
            adapter.updateItems(mViewModel.results);
            mViewModel.filteredResult.setValue(items);
        });

        mViewModel.filteredResult.observe(requireActivity(), new Observer<List<Result>>() {
            @Override
            public void onChanged(List<Result> results) {
                mSearchView = view.findViewById(R.id.favorite_search_view);
                mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        mViewModel.searchFavorite(s);
                        adapter.setitems(mViewModel.filteredResult.getValue());
                        return true;
                    }

                });
            }

        });

        adapter.setOnFavoriteClickListener(this);


    }


    @Override
    public void onFavoriteClick(Result item) {
        Intent intent = new Intent(requireActivity(), DetailActivity.class);
        intent.putExtra("result", item);
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.favorite_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                mSearchView.setVisibility(View.VISIBLE);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}



