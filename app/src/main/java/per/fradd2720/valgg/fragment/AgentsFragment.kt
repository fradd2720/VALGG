package per.fradd2720.valgg.fragment

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.AnimationUtils.loadAnimation
import android.widget.ImageView
import androidx.appcompat.widget.DrawableUtils
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import per.fradd2720.valgg.databinding.FragmentAgentsBinding
import per.fradd2720.valgg.manager.AgentAbilityManager
import per.fradd2720.valgg.recyclerview.AgentAdapter
import per.fradd2720.valgg.recyclerview.SpaceItemDecoration
import per.fradd2720.valgg.retrofit2.Agent
import per.fradd2720.valgg.retrofit2.RetrofitApi
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.roundToInt

class AgentsFragment : Fragment() {

    private var agentList: ArrayList<Agent.Data>? = null
    private lateinit var binding: FragmentAgentsBinding
    private val valorantContentsApi = RetrofitApi.valorantContentsApi
    private val adapter = AgentAdapter()
    private val snapHelper = LinearSnapHelper()
    private lateinit var abilityManager: AgentAbilityManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAgentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        abilityManager = AgentAbilityManager(context!!)
        binding.agentList.addItemDecoration(SpaceItemDecoration(context, 32))
        snapHelper.attachToRecyclerView(binding.agentList)

        binding.agentImage.alpha = 0f
        binding.agentPositionText.alpha = 0f
        binding.agentPositionName.alpha = 0f
        binding.agentPositionIcon.alpha = 0f
        binding.agentDescription.alpha = 0f
        binding.agentDescriptionText.alpha = 0f
        binding.agentAbility1.alpha = 0f
        binding.agentAbility2.alpha = 0f
        binding.agentAbility3.alpha = 0f
        binding.agentAbility4.alpha = 0f

        CoroutineScope(Dispatchers.Default).launch { load() }

        binding.agentPositionText.animate().alpha(1f).setDuration(500)
        binding.agentDescriptionText.animate().alpha(1f).setDuration(500)

        abilityManager.setOnSelectedListener(object : AgentAbilityManager.OnSelectedListener {
            override fun onSelected(index: Int) {
                val selectedAgent = if (selectedAgent != -1) selectedAgent else 0

                if (index == -1) {
                    binding.agentDescriptionText.text = "// 배경"
                    binding.agentDescription.text = (agentList?.get(selectedAgent)?.description ?: "???")
                } else {
                    var temp: String = ""
                    when (index) {
                        0 -> {
                            temp = "// Q - "
                        }
                        1 -> {
                            temp = "// E - "
                        }
                        2 -> {
                            temp = "// C - "
                        }
                        3 -> {
                            temp = "// X - "
                        }
                    }

                    binding.agentDescriptionText.text = temp + (agentList?.get(selectedAgent)?.abilities?.get(index)?.name ?: "???")
                    binding.agentDescription.text = (agentList?.get(selectedAgent)?.abilities?.get(index)?.description ?: "???")
                }
            }
        })
    }

    private suspend fun load() = withContext(Dispatchers.IO) {
        withContext(Dispatchers.Main) {
            val player = ExoPlayer.Builder(context!!).build().apply {
                repeatMode = Player.REPEAT_MODE_ONE
                playWhenReady = true
                setMediaItem(MediaItem.fromUri("https://assets.contentstack.io/v3/assets/bltb6530b271fddd0b1/blt29d7c4f6bc077e9e/5eb26f54402b8b4d13a56656/agent-background-generic.mp4"))
                prepare()
            }
            binding.agentBackground.apply {
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                this.player = player
            }
        }

        val response = valorantContentsApi.getAgents("ko-KR").execute()
        if (!response.isSuccessful || response.body() == null) return@withContext
        agentList = response.body()!!.agents.filter { it.isPlayable } as ArrayList<Agent.Data>

        withContext(Dispatchers.Main) {
            adapter.set(agentList!!)
            binding.agentList.adapter = adapter
            binding.agentList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        selecteAgent(binding.agentList.getChildAdapterPosition(snapHelper.findSnapView(binding.agentList.layoutManager)!!))
                    }
                }
            })
            selecteAgent(0)
        }
    }

    var selectedAgent: Int = -1
    private fun selecteAgent(index: Int) {
        if (index == selectedAgent) return

        selectedAgent = index
        val agent = agentList!![index]

        binding.agentImage.alpha = 0f
        binding.agentPositionName.alpha = 0f
        binding.agentPositionIcon.alpha = 0f
        binding.agentDescription.alpha = 0f
        binding.agentAbility1.alpha = 0f
        binding.agentAbility2.alpha = 0f
        binding.agentAbility3.alpha = 0f
        binding.agentAbility4.alpha = 0f

        adapter.selectAgent(index)

        binding.agentPositionName.text = agent.position.name
        binding.agentDescription.text = agent.description

        binding.agentPositionName.animate().alpha(1f).setDuration(500)
        binding.agentPositionIcon.animate().alpha(1f).setDuration(500)
        binding.agentDescription.animate().alpha(1f).setDuration(500)

        Glide.with(context!!).load(agent.image).listener(object : RequestListener<Drawable> {
            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                binding.agentImage.animate().alpha(1f).setDuration(500)

                return false
            }

            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                return false
            }

        }).into(binding.agentImage)
        Glide.with(context!!).load(agent.position.icon).listener(object : RequestListener<Drawable> {
            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                binding.agentPositionIcon.animate().alpha(1f).setDuration(500)

                return false
            }

            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                return false
            }

        }).into(binding.agentPositionIcon)
        Glide.with(context!!).load(agent.abilities[0].icon).listener(object : RequestListener<Drawable> {
            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                abilityManager.set(binding.agentAbility1, binding.agentAbility1Background, 0)
                binding.agentAbility1.animate().alpha(1f).setDuration(500)

                return false
            }

            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                return false
            }

        }).into(binding.agentAbility1)
        Glide.with(context!!).load(agent.abilities[1].icon).listener(object : RequestListener<Drawable> {
            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                abilityManager.set(binding.agentAbility2, binding.agentAbility2Background, 1)
                binding.agentAbility2.animate().alpha(1f).setDuration(500)

                return false
            }

            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                return false
            }

        }).into(binding.agentAbility2)
        Glide.with(context!!).load(agent.abilities[2].icon).listener(object : RequestListener<Drawable> {
            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                abilityManager.set(binding.agentAbility3, binding.agentAbility3Background, 2)
                binding.agentAbility3.animate().alpha(1f).setDuration(500)

                return false
            }

            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                return false
            }

        }).into(binding.agentAbility3)
        Glide.with(context!!).load(agent.abilities[3].icon).listener(object : RequestListener<Drawable> {
            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                abilityManager.set(binding.agentAbility4, binding.agentAbility4Background, 3)
                binding.agentAbility4.animate().alpha(1f).setDuration(500)

                return false
            }

            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                return false
            }

        }).into(binding.agentAbility4)
    }
}