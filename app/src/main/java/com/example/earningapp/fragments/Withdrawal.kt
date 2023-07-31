package com.example.earningapp.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.earningapp.databinding.FragmentWithdrawalBinding
import com.example.earningapp.models.HistoryModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class Withdrawal : BottomSheetDialogFragment() {
    lateinit var database: FirebaseDatabase
    lateinit var binding: FragmentWithdrawalBinding
    var rupees = 0L
    var playerwinAmount = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWithdrawalBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database.getReference().child("PlayWinAmount").child(Firebase.auth.currentUser!!.uid)
            .child("Value").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
//
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    playerwinAmount = snapshot.value as Long
                    binding.totalcoin.text = playerwinAmount.toString()
                    rupees = (0.01 * playerwinAmount).toLong()
                    binding.rupees.text = rupees.toString()
                }
            })

        binding.transferAmount.setOnClickListener(View.OnClickListener {
            if (binding.enterAmount.text.toString().isEmpty()) {
                binding.enterAmount.setError("please enter Amount")
                return@OnClickListener
            } else {
                val isNumeric: Boolean =
                    binding.enterAmount.text.toString().chars().allMatch(Character::isDigit)
                if (!isNumeric) {
                    binding.enterAmount.setError("please enter valid Amount")
                    return@OnClickListener
                } else if (isNumeric) {
                    if (Integer.parseInt(binding.enterAmount.text.toString()) < 40) {
                        binding.enterAmount.setError("required atleast 40 rupees")
                        return@OnClickListener
                    } else if (Integer.parseInt(binding.enterAmount.text.toString()) > rupees) {
                        binding.enterAmount.setError("not Have Enough Money")
                        return@OnClickListener
                    }
                }
            }


            if (binding.paytmNumber.text.toString().isEmpty()) {
                binding.paytmNumber.setError("please enter Amount")
                return@OnClickListener
            }
            dismiss()
            database.getReference().child("PlayWinAmount").child(Firebase.auth.currentUser!!.uid)
                .child("Value")
                .setValue(playerwinAmount - Integer.parseInt(binding.enterAmount.text.toString()) * 100)
            binding.totalcoin.text = playerwinAmount.toString()
            binding.rupees.text = rupees.toString()


            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
            val formatted = current.format(formatter)

            var history: HistoryModel = HistoryModel(formatted,binding.enterAmount.text.toString(), false)
            database.getReference().child("ForHistory").child(Firebase.auth.currentUser!!.uid)
                .push().setValue(history)
        })


    }

    companion object {

    }
}