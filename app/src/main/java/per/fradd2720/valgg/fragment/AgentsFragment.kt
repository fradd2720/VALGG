package per.fradd2720.valgg.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearSnapHelper
import per.fradd2720.valgg.databinding.FragmentAgentsBinding
import per.fradd2720.valgg.recyclerview.AgentAdapter
import per.fradd2720.valgg.recyclerview.SpaceItemDecoration
import per.fradd2720.valgg.retrofit2.Agent
import per.fradd2720.valgg.retrofit2.RetrofitApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AgentsFragment : Fragment() {

    private lateinit var binding: FragmentAgentsBinding
    private val valorantContentsApi = RetrofitApi.valorantContentsApi

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAgentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = AgentAdapter()

        binding.agentList.adapter = adapter
        binding.agentList.addItemDecoration(SpaceItemDecoration(context, 32))
        LinearSnapHelper().attachToRecyclerView(binding.agentList)

        valorantContentsApi.getAgents("ko-KR").enqueue(object : Callback<Agent> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<Agent>, response: Response<Agent>) {
                if (!response.isSuccessful || response.body() == null) return
                val agents = response.body()!!.agents

                agents.forEach {
                    if (!it.isPlayable) return@forEach

                    adapter.add(it)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<Agent>, t: Throwable) {
                Log.d("fradd1503", "fail")
            }
        })
    }
}