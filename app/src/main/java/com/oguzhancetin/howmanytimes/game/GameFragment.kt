package com.oguzhancetin.howmanytimes.game

import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.gms.ads.AdRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.oguzhancetin.howmanytimes.model.Player
import com.oguzhancetin.howmanytimes.R
import com.oguzhancetin.howmanytimes.databinding.FragmentGameBinding

class GameFragment : Fragment() {

    //timer is keep going?
    private var timerSatatus:Boolean = false

    private var score:Long = 0
    private lateinit var leftSecond: String

    //time is done ?
    private var status:Boolean = false

    var backgroundColor = 0

    private val COlORS = listOf<String>("#cfffe5","#ff9e9d","#f0fc99","#e6a8c2","#f4e7b0",
    "#f99888","#f99888","#b1c2fc","#a5f3dd","#fbb1d5","#ff6600","#ff00ff","#99cc00",
    "#ffff00","#d80083","#d80083","#07e2f4","#696966")

    private lateinit var canvas:Canvas
    lateinit var mBitmap: Bitmap
    lateinit var imageView: ImageView

    var mPaint = Paint()
    var mPaintTextTimeOut = Paint(Paint.UNDERLINE_TEXT_FLAG)
    val paintForCircle = Paint()


    val mRect = Rect()
    val timeOutBound = Rect()

    val timeOutText = "Time Out"


    private val OFFSET = 4
    private var mOffSet = OFFSET

    private lateinit var viewModel: GameViewModel
    private lateinit var textView:TextView

    private lateinit var mAuth:FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentGameBinding.inflate(inflater)
        mAuth = Firebase.auth
        backgroundColor = Color.parseColor(COlORS.random())


        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        imageView = binding.gameCanvas
        textView = binding.textView

        imageView.setOnClickListener {
            drawWindow(it)
        }

        viewModel.checTimerStatus.observe(viewLifecycleOwner, Observer {
            timerSatatus = it
        })

        viewModel.timeLeft.observe(viewLifecycleOwner, Observer {
            if(it >0){
                leftSecond = "sec: $it"
            }else{
                drawTimeOutCircle()
                this.status = true
            }

        })

        //what happened when time out
        viewModel.checTimeStatus.observe(viewLifecycleOwner, Observer {status->
            if(status){

                val nickname = mAuth.currentUser!!.displayName
                val player = Player(nickname,score)
                val action = GameFragmentDirections.actionGameFragment2ToScoreFragment(player)
                requireView().findNavController().navigate(action)
                viewModel.cleanTimeStatus()
            }
        })

        paintForCircle.color = ResourcesCompat.getColor(resources,R.color.colorPrimaryDark,null)
        mPaintTextTimeOut.textSize = resources.getDimension(R.dimen.time_out_size)
        mPaintTextTimeOut.getTextBounds(timeOutText,0,timeOutText.length,timeOutBound)



        return binding.root
    }

    fun drawTimeOutCircle(){

        val height = imageView.height
        val widht = imageView.width

        canvas.drawCircle((widht/2f),(height/2f),250f,paintForCircle)
        canvas.drawText(timeOutText,(widht/2)-(timeOutBound.exactCenterX()),height/2f-timeOutBound.centerY(),mPaintTextTimeOut)
        imageView.invalidate()


    }

    fun drawWindow(view: View){
        if(textView.visibility == View.VISIBLE){
            textView.visibility = View.GONE
        }
        Log.e("boyut","${imageView.width}")
        Log.e("boyutt","${view.width}")
        val height = view.height
        val widht = view.width
        val finalX = widht/2
        val finalY = height/2

        if(!timerSatatus){
            viewModel.startCountTime()
        }


        if(!status){
            if(mOffSet == OFFSET){
                mBitmap = Bitmap.createBitmap(imageView.width,imageView.height,Bitmap.Config.ARGB_8888)
                imageView.setImageBitmap(mBitmap)
                canvas = Canvas(mBitmap)

                canvas.drawColor(backgroundColor)
                mOffSet +=OFFSET
                score++
            }else{
                if(mOffSet<finalX && mOffSet<finalY){
                    mPaint.color = Color.parseColor(COlORS.random())
                    mRect.set(mOffSet,mOffSet,widht-mOffSet,height-mOffSet)
                    canvas.drawRect(mRect,mPaint)
                    mOffSet += OFFSET
                    score++
                }else{



                }
            }
        }


        view.invalidate()
    }



}