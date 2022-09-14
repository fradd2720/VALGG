package per.fradd2720.valgg.recyclerview

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import per.fradd2720.valgg.R
import per.fradd2720.valgg.databinding.ItemAgentBinding
import per.fradd2720.valgg.retrofit2.Agent

class AgentAdapter : RecyclerView.Adapter<AgentAdapter.ViewHolder>() {

    private var agentList = ArrayList<Agent.Data>()
    private var selectedIndex = 0
    private val bindingList = ArrayList<ItemAgentBinding>()
    private lateinit var recyclerView: RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        this.recyclerView = recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemAgentBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return agentList.size
    }

    fun get(index: Int): Agent.Data { return agentList[index] }

    fun getFirstView(): TextView {
        return (recyclerView.layoutManager as LinearLayoutManager).findViewByPosition(0)!!.findViewById(R.id.agentName)
    }

    fun getLastView(): TextView {
        return (recyclerView.layoutManager as LinearLayoutManager).findViewByPosition(agentList.lastIndex)!!.findViewById(R.id.agentName)
    }

    fun set(agentList: ArrayList<Agent.Data>) {
        this.agentList = agentList
    }

    fun selectAgent(index: Int) {
        if (index == selectedIndex) return

        try {
            bindingList[index].agentName.setTextColor(Color.parseColor("#ff4655"))
            bindingList[selectedIndex].agentName.setTextColor(Color.parseColor("#0f1923"))
        }catch (e: Exception) {}

        selectedIndex = index
    }

    inner class ViewHolder(private val binding: ItemAgentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(index: Int) {
            val agent = agentList[index]

            binding.agentName.text = agent.name

            binding.agentName.setTextColor(Color.parseColor(if (index == selectedIndex) "#ff4655" else "#0f1923"))

            try {
                bindingList[index] = binding
            } catch (e: Exception){
                bindingList.add(binding)
            }
        }
    }
}