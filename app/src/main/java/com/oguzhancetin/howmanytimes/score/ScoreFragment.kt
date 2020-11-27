package com.oguzhancetin.howmanytimes.score

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.oguzhancetin.howmanytimes.model.Player
import com.oguzhancetin.howmanytimes.R
import com.oguzhancetin.howmanytimes.adGlobalBuilder
import com.oguzhancetin.howmanytimes.databinding.ScoreFragmentBinding

class ScoreFragment : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleClient: GoogleSignInClient

    private lateinit var viewModel: ScoreViewModel

    private lateinit var player: Player

    private lateinit var mInterstitialAd: InterstitialAd

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = ScoreFragmentBinding.inflate(inflater)
        Log.e("deneme", "deneme")
        //ads settings
        mInterstitialAd = InterstitialAd(requireContext())
        mInterstitialAd.adUnitId = "ca-app-pub-8555100084688788/3478631345"
        val adRequest = AdRequest.Builder().build()
        mInterstitialAd.loadAd(adRequest)

        mAuth = Firebase.auth

        val gso2 = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestServerAuthCode(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleClient = GoogleSignIn.getClient(requireContext(),gso2)

         binding.lifecycleOwner = this
         player = ScoreFragmentArgs.fromBundle(requireArguments()).player

         binding.currentPlayer = player
         Log.e("skor",player.skor.toString())




        val viewModelFactory = ScorViewModelFactory(requireActivity().application,player)
        viewModel = ViewModelProvider(this,viewModelFactory).get(ScoreViewModel::class.java)
        binding.viewModel = viewModel

            binding.playAgain.setOnClickListener {
                if(mInterstitialAd.isLoaded){
                    mInterstitialAd.show()
                    Log.e("reklam", "The interstitial loaded")
                }else{
                    Log.e("reklam", "The interstitial wasn't loaded yet.")
                }
            playAgain()
        }

        binding.buttonSignout.setOnClickListener {
            googleClient.signOut()
            mAuth.signOut()
            it.findNavController().navigate(R.id.action_scoreFragment_to_startFragment)
        }

        val adRequest2 = AdRequest.Builder().build()
        binding.adViewScore.loadAd(adRequest2)

        return binding.root

    }

    //restart the game
    private fun playAgain() {
       findNavController().navigate(R.id.action_scoreFragment_to_gameFragment2)
    }


}