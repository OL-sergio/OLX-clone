package exemple.udemy.java.olx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import exemple.udemy.java.olx.R;
import exemple.udemy.java.olx.model.Advert;

public class AdapterAdverts extends RecyclerView.Adapter<AdapterAdverts.MyViewHolder> {

    private List<Advert> adapterAdverts;
    private Context context;

    public AdapterAdverts(List<Advert> adapterAdverts, Context context) {
        this.adapterAdverts = adapterAdverts;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView textViewTitle;
        TextView textViewPrice;
        ImageView imageViewPhoto;

        public MyViewHolder(View itemView){
            super(itemView);
            // Initialize your views here
            textViewTitle = itemView.findViewById(R.id.textView_rowTitle);
            textViewPrice = itemView.findViewById(R.id.textView_rowPrice);
            imageViewPhoto = itemView.findViewById(R.id.imageView_rowAdvert);
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_adverts,parent,false);
      return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Advert adverts = adapterAdverts.get(position);
        holder.textViewTitle.setText(adverts.getTitle());
        holder.textViewPrice.setText(adverts.getPrice());

        List<String> urlsPhotos = adverts.getPhotos();
        String urlCover = urlsPhotos.get(0);


        Picasso.get().load(urlCover)
                .resize(150,100)
                .error(R.drawable.logo)
                .into(holder.imageViewPhoto);



   /*Glide.with(context).load(urlCover)
            .error(R.drawable.logo)
            .apply(RequestOptions.overrideOf(150, 100))
            .into(holder.imageViewPhoto);*/

    }

    @Override
    public int getItemCount() {
        return adapterAdverts.size();
    }
}
