package developer.citypalestine8936ps.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import developer.citypalestine8936ps.R;
import developer.citypalestine8936ps.models.LandMark;

public class LandMarkAdapter extends RecyclerView.Adapter<LandMarkAdapter.LandmarkAdapter> {

    private List<LandMark> landMarkList;
    Context context;
    public LandMarkAdapter(Context context, List<LandMark> landMarkList) {
        this.landMarkList = landMarkList;
        this.context = context;

    }

    @NonNull
    @Override
    public LandmarkAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.landmark_item, parent, false);
        return new LandmarkAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LandmarkAdapter holder, int position) {
        holder.onBind(landMarkList.get(position));
    }

    @Override
    public int getItemCount() {
        return landMarkList.size();
    }


    public class LandmarkAdapter extends RecyclerView.ViewHolder {
           TextView textView;
//           ImageView imageView1 ;
//           ImageView imageView2 ;
        RecyclerView recyclerView ;
        public LandmarkAdapter(@NonNull View itemView) {
            super(itemView);
            textView =itemView.findViewById(R.id.txt_landmarkName);

            recyclerView = itemView.findViewById(R.id.photoRc);

//            imageView1 =itemView.findViewById(R.id.imag1_landmarkName);
//            imageView2 =itemView.findViewById(R.id.imag2_landmarkName);



        }

        public void onBind(LandMark item) {

            textView.setText(item.itemName);
            PhotoAdpter photoAdpter = new PhotoAdpter(context,item.photos);
            recyclerView.setAdapter(photoAdpter);
            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

            recyclerView.setLayoutManager(layoutManager);
//            Picasso.get()
//                    .load(item.photo1)
//                    .resize(200, 200)
//                    .centerCrop().error(R.drawable.ic_launcher_background)
//                    .into(imageView1);
//            Picasso.get()
//                    .load(item.photo2)
//                    .resize(200, 200)
//                    .centerCrop().error(R.drawable.ic_launcher_background)
//                    .into(imageView2);
        }
    }


}
