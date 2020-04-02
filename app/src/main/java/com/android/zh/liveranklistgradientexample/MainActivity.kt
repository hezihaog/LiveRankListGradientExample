package com.android.zh.liveranklistgradientexample

import android.graphics.*
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.zh.liveranklistgradientexample.item.RankListItemBinder
import com.android.zh.liveranklistgradientexample.model.RankListItemModel
import com.android.zh.liveranklistgradientexample.util.RecyclerViewScrollHelper
import com.android.zh.liveranklistgradientexample.util.ViewUtils
import me.drakeet.multitype.Items
import me.drakeet.multitype.MultiTypeAdapter


class MainActivity : AppCompatActivity() {
    private lateinit var vRankList: RecyclerView

    private val mListItems = Items()
    private val mListAdapter = MultiTypeAdapter(mListItems).apply {
        register(RankListItemModel::class.java, RankListItemBinder())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findView()
        bindView()
        setData()
    }

    private fun findView() {
        vRankList = findViewById(R.id.rank_list)
    }

    private fun bindView() {
        supportActionBar?.title = "打赏榜"
        vRankList.run {
            adapter = mListAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            setupRankListAlphaStyle()
        }
    }

    private fun setData() {
        val textColorResId = android.R.color.white
        for (index in 1..15) {
            mListItems.add(
                RankListItemModel(
                    index,
                    generateNickName(index),
                    "",
                    (100 + index).toString(),
                    textColorResId
                )
            )
        }
        mListAdapter.notifyDataSetChanged()
    }

    /**
     * 生成昵称
     */
    private fun generateNickName(index: Int): String {
        val nicknames = listOf(
            "迪丽热巴", "黄晓明", "杨幂",
            "彭于晏", "柳岩", "李易峰", "陈伟霆", "刘诗诗", "张艺兴", "成龙",
            "蔡徐坤", "赵丽颖", "王一博", "阚清子", "刘亦菲", "郑爽", "杨紫",
            "关晓彤", "唐嫣", "胡歌", "宋茜", "周杰伦", "吴亦凡", "周冬雨", "华晨宇"
        )
        val position = index % nicknames.size
        return nicknames[position]
    }

    /**
     * 设置排行榜列表透明度风格
     */
    private fun setupRankListAlphaStyle() {
        val context = this
        val paint = Paint()
        val shadowHeight = ViewUtils.dipToPx(context, 80f).toFloat()
        //顶部渐变
        val topShadowStrategy = TopShadowStrategy(shadowHeight, paint)
        //底部渐变
        val bottomShadowStrategy = BottomShadowStrategy(shadowHeight, paint)

        //混合模式
        val xfermode: Xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        var layerId = 0
        vRankList.addItemDecoration(object : RecyclerView.ItemDecoration() {
            /**
             * 可以实现类似绘制背景的效果，内容在上面
             */
            override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                super.onDraw(canvas, parent, state)
                layerId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    canvas.saveLayer(
                        0.0f,
                        0.0f,
                        parent.width.toFloat(),
                        parent.height.toFloat(),
                        paint
                    )
                } else {
                    canvas.saveLayer(
                        0.0f,
                        0.0f,
                        parent.width.toFloat(),
                        parent.height.toFloat(),
                        paint,
                        Canvas.ALL_SAVE_FLAG
                    )
                }
            }

            /**
             * 可以绘制在内容的上面，覆盖内容
             */
            override fun onDrawOver(
                canvas: Canvas,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.onDrawOver(canvas, parent, state)
                paint.xfermode = xfermode

                //画顶部渐变
                paint.shader = topShadowStrategy.getShader(parent)
                topShadowStrategy.onDrawOver(canvas, parent, state)

                //画底部渐变
                paint.shader = bottomShadowStrategy.getShader(parent)
                bottomShadowStrategy.onDrawOver(canvas, parent, state)

                paint.xfermode = null
                canvas.restoreToCount(layerId)
            }
        })
    }

    /**
     * 渐变策略
     */
    private abstract class ShadowStrategy(
        val shadowHeight: Float,
        val paint: Paint
    ) {
        /**
         * 绘制
         */
        abstract fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State)

        /**
         * 获取着色器
         */
        abstract fun getShader(parent: RecyclerView): Shader
    }

    /**
     * 顶部渐变
     */
    private inner class TopShadowStrategy(shadowHeight: Float, paint: Paint) :
        ShadowStrategy(shadowHeight, paint) {

        private lateinit var mScrollHelper: RecyclerViewScrollHelper
        /**
         * 是否可以绘制渐变
         */
        private var isCanDrawShadow: Boolean = false

        override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            if (isCanDrawShadow) {
                val left = 0f
                val top = 0f
                val right = parent.width.toFloat()
                val bottom = shadowHeight
                val topShadowRect = RectF(left, top, right, bottom)
                canvas.drawRect(topShadowRect, paint)
            }
        }

        override fun getShader(parent: RecyclerView): Shader {
            if (!this::mScrollHelper.isInitialized) {
                mScrollHelper = RecyclerViewScrollHelper()
                mScrollHelper.attachRecyclerView(
                    parent,
                    object : RecyclerViewScrollHelper.CallbackAdapter() {
                        override fun onScrolledToTop() {
                            //到了顶部就不能渲染
                            isCanDrawShadow = false
                        }

                        override fun onScrolledToUp() {
                            super.onScrolledToUp()
                            //向上滚动，列表向下移动，则需要渲染
                            isCanDrawShadow = true
                        }
                    })
            }
            return run {
                //渐变起始x，y坐标
                val x0 = 0f
                val y0 = 0f
                //渐变结束x，y坐标
                val x1 = 0f
                val y1 = shadowHeight
                //渐变颜色的开始、结束颜色
                val startColor = Color.TRANSPARENT
                val endColor = Color.BLACK
                val colors = intArrayOf(startColor, endColor)
                //渐变位置数组
                val positions = null

                //指定控件区域大于指定的渐变区域时，空白区域的颜色填充方法
                //CLAMP：边缘拉伸，为被shader覆盖区域，使用shader边界颜色进行填充
                //REPEAT：在水平和垂直两个方向上重复，相邻图像没有间隙
                //MIRROR：以镜像的方式在水平和垂直两个方向上重复，相邻图像有间隙
                val tile = Shader.TileMode.CLAMP
                LinearGradient(
                    x0, y0, x1, y1,
                    colors, positions, tile
                )
            }
        }
    }

    /**
     * 底部渐变
     */
    private inner class BottomShadowStrategy(shadowHeight: Float, paint: Paint) :
        ShadowStrategy(shadowHeight, paint) {

        /**
         * 是否可以绘制渐变
         */
        private var isCanDrawShadow: Boolean = true

        override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            //底部渐变，必须指定数量的条目才可以绘制
            isCanDrawShadow = (parent.adapter?.itemCount ?: 0) > 8
            if (isCanDrawShadow) {
                val left = 0f
                val top = parent.height - shadowHeight
                val right = parent.width.toFloat()
                val bottom = parent.height.toFloat()
                val topShadowRect = RectF(left, top, right, bottom)
                canvas.drawRect(topShadowRect, paint)
            }
        }

        override fun getShader(parent: RecyclerView): Shader {
            return run {
                //渐变起始x，y坐标
                val x0 = 0f
                val y0 = parent.height - shadowHeight
                //渐变结束x，y坐标
                val x1 = 0f
                val y1 = parent.height.toFloat()

                //渐变颜色的开始、结束颜色
                val startColor = Color.BLACK
                val endColor = Color.TRANSPARENT
                val colors = intArrayOf(startColor, endColor)
                //渐变位置数组
                val positions = null

                //指定控件区域大于指定的渐变区域时，空白区域的颜色填充方法
                //CLAMP：边缘拉伸，为被shader覆盖区域，使用shader边界颜色进行填充
                //REPEAT：在水平和垂直两个方向上重复，相邻图像没有间隙
                //MIRROR：以镜像的方式在水平和垂直两个方向上重复，相邻图像有间隙
                val tile = Shader.TileMode.CLAMP
                LinearGradient(
                    x0, y0, x1, y1,
                    colors, positions, tile
                )
            }
        }
    }
}