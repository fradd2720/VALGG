package per.fradd2720.valgg.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import per.fradd2720.valgg.databinding.ItemAgentBinding
import per.fradd2720.valgg.retrofit2.Agent

class AgentAdapter : RecyclerView.Adapter<AgentAdapter.ViewHolder>() {

    private val agentList = ArrayList<Agent.Data>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemAgentBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return agentList.size
    }

    fun add(agent: Agent.Data) {
        agentList.add(agent)
    }

    private var firstView: TextView? = null
    private var lastView: TextView? = null

    fun getFirstView(): TextView? {
        return firstView
    }

    fun getLastView(): TextView? {
        return lastView
    }

    inner class ViewHolder(private val binding: ItemAgentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val agent = agentList[position]

            binding.agentName.text = agent.name

            if (position == 0) {
                firstView = binding.agentName
            } else if (position == agentList.size - 1) {
                lastView = binding.agentName
            }
        }
    }
}