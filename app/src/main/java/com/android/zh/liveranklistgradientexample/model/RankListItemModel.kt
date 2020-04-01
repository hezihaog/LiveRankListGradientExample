package com.android.zh.liveranklistgradientexample.model

/**
 * @author : lizhi - hezihao
 * e-mail : hezihao@lizhi.fm
 * time   : 2020/04/01
 * desc   :打赏榜列表模型
 */
data class RankListItemModel(
    /**
     * 排名号码
     */
    val rankNum: Int,
    /**
     * 用户昵称
     */
    val nickname: String,
    /**
     * 用户头像
     */
    val avatarUrl: String,
    /**
     * 打赏金额
     */
    val money: String,
    /**
     * 文字颜色资源Id
     */
    val textColorResId: Int
)