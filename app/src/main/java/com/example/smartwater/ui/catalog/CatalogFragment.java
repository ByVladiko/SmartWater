package com.example.smartwater.ui.catalog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.smartwater.R;
import com.example.smartwater.model.Product;
import com.example.smartwater.util.InternetConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CatalogFragment extends Fragment {

    private ListView listView;
    private List<Product> productList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_catalog, container, false);

        View emptyList = root.findViewById(R.id.emptyList);

        listView = root.findViewById(R.id.productList);
        listView.setEmptyView(emptyList);

        if (InternetConnection.isOnline(getContext())) {
            loadCatalog();
        }

        return root;
    }

    private void loadCatalog() {
        try {
            LoadCatalog getCatalog = new LoadCatalog(getActivity());
            String json = getCatalog.execute().get();
            JSONArray jsonArray = new JSONArray(json);
            productList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                productList.add(new Product(obj.getString("name"),
                        obj.getString("description"),
                        Float.parseFloat(obj.getString("price")), obj.getString("image")));
            }
        } catch (JSONException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        ProductAdapter productAdapter = new ProductAdapter(getActivity(), productList);
        listView.setAdapter(productAdapter);
    }
}

