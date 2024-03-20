package com.example.localart.user.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.localart.adapter.CarouselAdapter;
import com.example.localart.user.CarouselItem;
import com.example.localart.R;
import com.example.localart.adapter.FeaturedProductsAdapter;
import com.example.localart.dataclass.FeaturedProduct;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private List<CarouselItem> carouselItems;
    private ViewPager2 viewPager;
    private CarouselAdapter carouselAdapter;

    // Handler for auto-scrolling
    private final Handler handler = new Handler(Looper.myLooper());
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // Increment the current item index and set the new item
            int currentItem = viewPager.getCurrentItem();
            int newItem = (currentItem + 1) % carouselAdapter.getItemCount();
            viewPager.setCurrentItem(newItem, true);
            handler.postDelayed(this, 3000); // Delay in milliseconds (3 seconds)
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        viewPager = view.findViewById(R.id.viewPager);

        // Create carousel items with titles and descriptions
        carouselItems = new ArrayList<>();
        carouselItems.add(new CarouselItem(R.drawable.banner_img_01, "Title 1", "Description 1"));
        carouselItems.add(new CarouselItem(R.drawable.banner_img_02, "Title 2", "Description 2"));
        carouselItems.add(new CarouselItem(R.drawable.banner_img_03, "Title 3", "Description 3"));

        carouselAdapter = new CarouselAdapter(requireContext(), carouselItems);
        viewPager.setAdapter(carouselAdapter);

        // Start auto-scrolling when the fragment is created
        startAutoScroll();


        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewFeaturedProducts);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setFocusable(false);
        recyclerView.setLayoutManager(recyclerView.getLayoutManager());
        List<FeaturedProduct> featuredProducts = getFeaturedProducts(); // Replace with your data source
        FeaturedProductsAdapter adapter = new FeaturedProductsAdapter(featuredProducts);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));


        return view;
    }



    // Method to start auto-scrolling
    private void startAutoScroll() {
        handler.postDelayed(runnable, 3000); // Initial delay of 3 seconds
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Stop auto-scrolling when the fragment is destroyed
        handler.removeCallbacks(runnable);
    }

    private List<FeaturedProduct> getFeaturedProducts() {
        // Replace this with your logic to fetch featured products
        return List.of(
                new FeaturedProduct("Meditative Buddha", R.drawable.feature_prod_01),
                new FeaturedProduct("A Street In Kerala", R.drawable.feature_prod_02),
                new FeaturedProduct("Banaras Ghat-12", R.drawable.feature_prod_03)
        );
    }

}
