package studio.smartters.moward.ViewHolders;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageRequest;

import studio.smartters.moward.Others.Constants;
import studio.smartters.moward.R;

public class PhotoViewHolder extends RecyclerView.ViewHolder {
    private ImageView img;
    private TextView nameText;
    private View v;
    public PhotoViewHolder(View itemView) {
        super(itemView);
        img=itemView.findViewById(R.id.image_photo);
        nameText=itemView.findViewById(R.id.image_title);

        v=itemView;
    }
    public void setName(String name){
        nameText.setText(name);
    }
    public void setImg(String path){
        RequestQueue r;
        String url= Constants.URL+"images/";
        Cache c=new DiskBasedCache(v.getContext().getCacheDir(),1024*1024);
        Network n=new BasicNetwork(new HurlStack());
        r=new RequestQueue(c,n);
        r.start();
        ImageRequest i=new ImageRequest(url + path, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                int width=img.getWidth();
                img.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,width*2/3));
                img.setImageBitmap(response);
            }
        },0,0,ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565,new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(v.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        r.add(i);
    }
}
