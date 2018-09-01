package studio.smartters.moward.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import studio.smartters.moward.Adapters.VideoAdapter;
import studio.smartters.moward.Others.Constants;
import studio.smartters.moward.R;

public class VideoFragment extends Fragment {
    RequestQueue r;
    String url= Constants.URL+"GetVideos";
    List<String> name,path;
    private RecyclerView list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_video, container, false);
        list=v.findViewById(R.id.video_list);
        Cache c=new DiskBasedCache(getActivity().getCacheDir(),1024*1024);
        Network n=new BasicNetwork(new HurlStack());
        r=new RequestQueue(c,n);
        name=new ArrayList<>();
        path=new ArrayList<>();
        r.start();
        JsonArrayRequest j=new JsonArrayRequest(Request.Method.POST, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                int count=0;
                while(count<response.length()){
                    try {
                        JSONObject j=response.getJSONObject(count);
                        name.add(j.getString("desc"));
                        path.add(j.getString("url"));
                        count++;
                    } catch (JSONException e) {
                        Toast.makeText(getActivity(),e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                VideoAdapter p=new VideoAdapter(getActivity(),name,path);
                list.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                list.setHasFixedSize(true);
                list.setAdapter(p);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        r.add(j);
        return v;
    }

}
