package per.fradd2720.valgg.recyclerview

import android.content.Context
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

class SpaceItemDecoration(context: Context?, dp: Int) : RecyclerView.ItemDecoration() {
    private val space: Int = (dp * context!!.resources.displayMetrics.density).roundToInt()

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        if (space == 0) return

        val position = parent.getChildAdapterPosition(view)
        val isFirstItem = position == 0
        val isLastItem = position == parent.adapter!!.itemCount - 1

        val start: Int
        val end: Int
        when {
            isFirstItem -> {
                val firstView = (parent.adapter as AgentAdapter).getFirstView()!!
                val bounds = Rect()
                val textPaint: Paint = firstView.paint
                textPaint.getTextBounds(firstView.text, 0, firstView.text.length, bounds)

                start = (parent.measuredWidth - bounds.width()) / 2
                end = (space / 2f + 0.5f).toInt()
            }
            isLastItem -> {
                val lastView = (parent.adapter as AgentAdapter).getLastView()!!
                val bounds = Rect()
                val textPaint: Paint = lastView.paint
                textPaint.getTextBounds(lastView.text, 0, lastView.text.length, bounds)

                start = (space / 2f + 0.5f).toInt()
                end = (parent.measuredWidth - bounds.width()) / 2
            }
            else -> {
                start = (space / 2f + 0.5f).toInt()
                end = (space / 2f + 0.5f).toInt()

                outRect.set(start, 0, end, 0)
            }
        }

        outRect.set(start, 0, end, 0)
    }
}