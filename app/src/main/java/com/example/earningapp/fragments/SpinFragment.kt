package com.example.earningapp.fragments

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.earningapp.R
import com.example.earningapp.databinding.FragmentSpinBinding
import com.example.earningapp.models.HistoryModel
import com.example.earningapp.models.Users
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import java.lang.Math.random
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.properties.Delegates
import kotlin.random.Random

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SpinFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SpinFragment : Fragment() {

    lateinit var binding: FragmentSpinBinding
    lateinit var database: FirebaseDatabase
    var chance = 0L
    var playerwinAmount = 0L

    private val itemtitles = arrayOf("100", "OOPS!! Try Again", "500", "OOPS!! Try Again", "200", "OOPS!! Try Again")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSpinBinding.inflate(layoutInflater)

        //for spinning
        database.getReference().child("PlayChance").child(Firebase.auth.currentUser!!.uid)
            .child("Value")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    binding.playchance.text = (snapshot.value as Long).toString()
                    chance = snapshot.value as Long
                }
            })

        //forAmount

        database.getReference().child("PlayWinAmount").child(Firebase.auth.currentUser!!.uid)
            .child("Value")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    playerwinAmount = snapshot.value as Long
                    binding.coinwithdrawal2.text = playerwinAmount.toString()
                }
            })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.coinwithdrawal1.setOnClickListener(
            OnClickListener {
                var withdrawal = Withdrawal()
                withdrawal.show(requireActivity().supportFragmentManager, "Text")
                withdrawal.enterTransition

            }
        )
        binding.coinwithdrawal2.setOnClickListener(
            OnClickListener {
                var withdrawal = Withdrawal()
                withdrawal.show(requireActivity().supportFragmentManager, "Text")
                withdrawal.enterTransition

            }
        )
        database.getReference().child("Users").child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(
                object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {
                        var users = snapshot.getValue<Users>(Users::class.java)
                        binding.name.text = users?.name
                        if (users?.profile != "") {
                            Picasso.get().load(users?.profile).placeholder(R.drawable.profile)
                                .into(binding.profileImage)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
//
                    }
                })


        binding.spinbtn.setOnClickListener {

            if (chance > 0) {
                binding.spinbtn.isEnabled = false

                val spin = Random.nextInt(5)
                val degrees = 60f * spin
                object : CountDownTimer(5000, 50) {
                    var rotation = 0f
                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun onTick(p0: Long) {
                        rotation += 5f
                        if (rotation >= degrees) {
                            rotation = degrees
                            showResult(itemtitles[spin], spin)
                            cancel()
                        }

                        binding.wheel.rotation = rotation

                    }

                    override fun onFinish() {

                    }

                }.start()
            } else {
                Toast.makeText(context, "Don't have chance to play", Toast.LENGTH_SHORT).show()
            }
        }


    }



    @SuppressLint("NewApi")
    private fun showResult(s: String, spin: Int) {
        if (spin % 2 == 0) {
            var playerwinAmount = itemtitles[spin].toInt() + playerwinAmount
            database.getReference().child("PlayWinAmount")
                .child(Firebase.auth.currentUser!!.uid).child("Value").setValue(playerwinAmount)
            binding.coinwithdrawal2.text = playerwinAmount.toString()

            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
            val formatted = current.format(formatter)

            var history:HistoryModel=HistoryModel(formatted,itemtitles[spin].toString(), true)
            database.getReference().child("ForHistory").child(Firebase.auth.currentUser!!.uid)
                .push().setValue(history)
        }
        binding.spinbtn.isEnabled = true
        Toast.makeText(context, "$s", Toast.LENGTH_SHORT).show()
        chance--
        database.getReference().child("PlayChance")
            .child(Firebase.auth.currentUser!!.uid).child("Value").setValue((chance))
        binding.playchance.text = chance.toString()
    }
}














