import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;




public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventViewHolder> {

    private final List<String> events;

    public EventsAdapter(List<String> events) {
        this.events = events;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        String event = events.get(position);
        holder.eventName.setText(event);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView eventName;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(android.R.id.text1);
        }
    }
}
