package com.ferroli.after_sales.utils

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.ferroli.after_sales.R

class FunctionButtonView : ConstraintLayout {

    private lateinit var ivFunctionButtonView: ImageView
    private lateinit var tvFunctionButtonView: TextView

    constructor(context: Context) : super(context) {
        initView(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        initView(attrs)
    }

    private fun initView(attrs: AttributeSet?) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.function_button_view, this, true)

        ivFunctionButtonView = view.findViewById(R.id.ivFunctionButtonView)
        tvFunctionButtonView = view.findViewById(R.id.tvFunctionButtonView)

        if (attrs == null) return

        context.obtainStyledAttributes(attrs, R.styleable.FunctionButtonView).apply {
            try {
                val typedArray: TypedArray =
                    context.obtainStyledAttributes(attrs, R.styleable.FunctionButtonView)
                val drawable: Drawable? =
                    typedArray.getDrawable(R.styleable.FunctionButtonView_iconInfo)

                val titleInfo: String =
                    getString(R.styleable.FunctionButtonView_titleInfo).toString()

                if (drawable != null) {
                    ivFunctionButtonView.setImageDrawable(drawable)

//                    tvFunctionButtonView.background = drawable
                }

                tvFunctionButtonView.text = titleInfo
            } finally {
                recycle()
            }
        }
    }
}