package com.android.zh.liveranklistgradientexample.item

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.zh.liveranklistgradientexample.R
import com.android.zh.liveranklistgradientexample.model.RankListItemModel
import me.drakeet.multitype.ItemViewBinder

/**
 * @author : lizhi - hezihao
 * e-mail : hezihao@lizhi.fm
 * time   : 2020/04/01
 * desc   :
 */
class RankListItemBinder : ItemViewBinder<RankListItemModel, RankListItemBinder.ViewHolder>() {
    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.activity_rank_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, item: RankListItemModel) {
        val context = holder.itemView.context
        item.run {
            val textColor = context.resources.getColor(textColorResId)
            holder.vRankNum.run {
                setTextColor(textColor)
                text = rankNum.toString()
            }
            holder.vNickname.run {
                setTextColor(textColor)
                text = nickname
            }
            holder.vAvatar.setImageResource(R.drawable.live_default_user_cover)
            holder.vMoney.run {
                setTextColor(textColor)
                text = money
            }
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val vRankNum: TextView = view.findViewById(R.id.rank_num)
        val vNickname: TextView = view.findViewById(R.id.nickname)
        val vAvatar: ImageView = view.findViewById(R.id.avatar)
        val vMoney: TextView = view.findViewById(R.id.money)
    }
}