package com.lesnyg.mymovieinfoapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.lesnyg.mymovieinfoapp.adapter.MovieFavoriteAdapter;
import com.lesnyg.mymovieinfoapp.MovieViewModel;
import com.lesnyg.mymovieinfoapp.R;
import com.lesnyg.mymovieinfoapp.models.Result;

import java.util.List;


public class FavoritesListFragment extends Fragment {

    private Result mResult;

    public FavoritesListFragment() {
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MovieViewModel viewModel = ViewModelProviders.of(requireActivity())
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
                viewModel.deleteFavorit(mResult);
            }
        });
        helper.attachToRecyclerView(recyclerView);


        recyclerView.setAdapter(adapter);
//        viewModel.favoritList.observe(this, new Observer<List<Favorite>>() {
//            @Override
//            public void onChanged(List<Favorite> favorites) {
//                adapter.setitems(favorites);
//                recyclerView.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
//
//            }
//        });
        viewModel.result.observe(requireActivity(), (List<Result> items) -> {
            adapter.updateItems(items);
        });
    }


    }



