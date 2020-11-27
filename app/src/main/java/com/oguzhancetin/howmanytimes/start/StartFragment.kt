package com.oguzhancetin.howmanytimes.start

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.oguzhancetin.howmanytimes.R
import com.oguzhancetin.howmanytimes.databinding.FragmentStartBinding


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StartFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleClient:GoogleSignInClient
    private val RC_SIGN_IN = 5001


    private lateinit var progressBar: ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentStartBinding.inflate(inflater)

        progressBar = binding.progressbar
        binding.progressbar.visibility = View.GONE

        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        auth = FirebaseAuth.getInstance()

        val gso2 = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestServerAuthCode(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleClient = GoogleSignIn.getClient(requireContext(),gso2)


        val currentUser = auth.currentUser
        updateUI(currentUser)




        binding.signInGoogle.setOnClickListener{
            startAccountChoserIntent()
        }




        return  binding.root
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_SIGN_IN){
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result!!.isSuccess()) {
                // The signed in account is stored in the result.
                val googleSignInAccount = result.signInAccount
                googleSignInAccount?.let {
                    progressBar.visibility = View.VISIBLE
                    firebaseAuthWithGoogle(it)
                }
                }


            }else{
            Toast.makeText(
                context, "Something went wrong.",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if(currentUser != null){
            requireView().findNavController().navigate(R.id.action_startFragment_to_gameFragment2)
        }else{

        }

    }

    private fun startAccountChoserIntent() {
        val signInIntent = googleClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    // Call this both in the silent sign-in task's OnCompleteListener and in the
// Activity's onActivityResult handler.
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d("id", "firebaseAuthWithPlayGames:" + acct.id!!)

        val auth = Firebase.auth
        val credential = GoogleAuthProvider.getCredential(acct.idToken,null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener  { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("status", "signInWithCredential:success")
                    val user = auth.currentUser
                    progressBar.visibility = View.GONE
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("status", "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        context, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }

            }
    }


}