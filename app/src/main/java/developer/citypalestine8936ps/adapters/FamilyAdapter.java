package developer.citypalestine8936ps.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import developer.citypalestine8936ps.R;

public class FamilyAdapter extends RecyclerView.Adapter<FamilyAdapter.photoViewHolder> {

    private List<String> mStrings;
    Context context;
    public FamilyAdapter(Context context, List<String> Strings) {
        this.mStrings = Strings;
        this.context = context;

    }

    @NonNull
    @Override
    public photoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.family_item, parent, false);
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
           TextView textView;
        public photoViewHolder(@NonNull View itemView) {
            super(itemView);
            textView =itemView.findViewById(R.id.txt_familyName);


        }

        public void onBind(String item) {

            textView.setText(item);
//            Picasso.get()
//                    .load(item)
//                    .resize(50, 50)
//                    .centerCrop().error(R.drawable.ic_launcher_background)
//                    .into(imageView);

        }
    }


}
