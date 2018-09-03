package studio.smartters.moward.Fragments;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import studio.smartters.moward.Adapters.PhotoAdapter;
import studio.smartters.moward.Others.Constants;
import studio.smartters.moward.R;

public class ImageFragment extends Fragment {
    private RequestQueue r;
    private String url= Constants.URL+"getPhotos";
    private List name,path;
    private RecyclerView list;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View v;
    private LinearLayout ln;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_image, container, false);
        list=v.findViewById(R.id.image_list);
        ln=v.findViewById(R.id.image_error);
        swipeRefreshLayout=v.findViewById(R.id.swipe_image);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        Cache c=new DiskBasedCache(getActivity().getCacheDir(),1024*1024);
        Network n=new BasicNetwork(new HurlStack());
        r=new RequestQueue(c,n);
        refresh();

        return v;
    }
    private void refresh(){
        if(isNetworkAvailable()) {
            name = new ArrayList();
            path = new ArrayList();
            r.start();
            JsonArrayRequest j = new JsonArrayRequest(Request.Method.POST, url, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    int count = 0;
                    while (count < response.length()) {
                        try {
                            JSONObject j = response.getJSONObject(count);
                            name.add(j.getString("name"));
                            path.add(j.getString("url"));
                            count++;
                        } catch (JSONException e) {
                            // Toast.makeText(getActivity(),e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        PhotoAdapter p = new PhotoAdapter(getActivity(), name, path);
                        list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        list.setHasFixedSize(true);
                        list.setAdapter(p);
                        ln.setVisibility(View.GONE);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            r.add(j);
        }else{
            ln.setVisibility(View.VISIBLE);
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) v.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
