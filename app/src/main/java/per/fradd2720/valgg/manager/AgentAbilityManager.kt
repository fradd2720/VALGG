package per.fradd2720.valgg.manager

import android.content.Context
import android.graphics.Color
import android.widget.ImageView
import androidx.core.content.ContextCompat
import per.fradd2720.valgg.R

class AgentAbilityManager(private val context: Context) {

    private var onSelectedListener:OnSelectedListener? = null
    private val imageViewList = ArrayList<ImageView>()
    private val imageViewBackgroundList = ArrayList<ImageView>()
    private var selectedAbility = -1

    fun set(imageView: ImageView, imageViewBackground: ImageView, index: Int) {
        imageViewList.add(imageView)
        imageViewBackgroundList.add(imageViewBackground)

        update(index)

        imageView.setOnClickListener {
            imageView.setColorFilter(Color.parseColor(if (index != selectedAbility) "#000000" else "#a6a39e"))
            imageViewBackground.setImageDrawable(ContextCompat.getDrawable(context, if (index != selectedAbility) R.drawable.background_ability else R.drawable.background_ability_off))

            val temp = selectedAbility
            selectedAbility = if (index != selectedAbility) index else -1
            onSelectedListener?.onSelected(selectedAbility)
            update(temp)
        }
    }

    fun setOnSelectedListener(onSelectedListener: OnSelectedListener) {
        this.onSelectedListener = onSelectedListener
    }

    private fun update(index: Int) {
        if (index == -1) return

        imageViewList[index].setColorFilter(Color.parseColor(if (index == selectedAbility) "#000000" else "#a6a39e"))
        imageViewBackgroundList[index].setImageDrawable(ContextCompat.getDrawable(context, if (index == selectedAbility) R.drawable.background_ability else R.drawable.background_ability_off))
    }

    interface OnSelectedListener {
        fun onSelected(index: Int)
    }
}