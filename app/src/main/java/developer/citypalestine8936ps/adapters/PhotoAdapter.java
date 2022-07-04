package developer.citypalestine8936ps.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import developer.citypalestine8936ps.R;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.photoViewHolder> {

    private List<String> mStrings;
    Context context;
    public PhotoAdapter(Context context, List<String> Strings) {
        this.mStrings = Strings;
        this.context = context;

    }

    @NonNull
    @Override
    public photoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
        return new photoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull photoViewHolder holder, int position) {
        holder.onBind(mStrings.get(position));
    }

    @Override
    public int getItemCount() {
        return mStrings.size();
    }


    public class photoViewHolder extends RecyclerView.ViewHolder {
           ImageView imageView;
        public photoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView =itemView.findViewById(R.id.photo);


        }

        public void onBind(String item) {
            Picasso.get()
                    .load(item)
                    .resize(200, 200)
                    .centerCrop().error(R.drawable.ic_launcher_background)
                    .into(imageView);

        }
    }


}
