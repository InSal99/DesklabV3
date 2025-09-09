import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.edts.desklabv3.databinding.ItemEmployeeActivityBinding

class EmployeeActivityAdapter(private val activities: List<Int>) : RecyclerView.Adapter<EmployeeActivityAdapter.ActivityViewHolder>() {

    class ActivityViewHolder(private val binding: ItemEmployeeActivityBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(drawableRes: Int) {
            binding.ivEmployeeActivity.setImageResource(drawableRes)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val binding = ItemEmployeeActivityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActivityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        holder.bind(activities[position])
    }

    override fun getItemCount(): Int = activities.size
}