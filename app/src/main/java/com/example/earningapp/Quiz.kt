package com.example.earningapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.earningapp.databinding.ActivityQuizBinding
import com.example.earningapp.fragments.Withdrawal
import com.example.earningapp.models.Question
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlin.properties.Delegates


class Quiz : AppCompatActivity() {

    lateinit var binding: ActivityQuizBinding
    lateinit var arrayList: ArrayList<Question>
    private lateinit var database: FirebaseDatabase
    var currentquestion = 0
    var totalcorrectquestion = 0
    lateinit var subject: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)
        arrayList = ArrayList<Question>()
        supportActionBar?.hide()
        var playerwinAmount=0L
        database = FirebaseDatabase.getInstance()
        var image = intent.getIntExtra("key_image", 1)
        subject = intent.getStringExtra("subject").toString()
        Toast.makeText(this, "$subject", Toast.LENGTH_SHORT).show()

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

        database.getReference().child("Quiz")
            .child("$subject").child("Questions")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (snapshot1 in snapshot.children) {
                        var question: Question? = snapshot1.getValue<Question>(Question::class.java)
                        arrayList.add(question!!)
                    }
                    if (arrayList.size != 0) {
                        binding.question.text = arrayList.get(currentquestion).question
                        binding.option1.text = arrayList.get(currentquestion).option1
                        binding.option2.text = arrayList.get(currentquestion).option2
                        binding.option3.text = arrayList.get(currentquestion).option3
                        binding.option4.text = arrayList.get(currentquestion).option4
                    }
                }
            })

        binding.option1.setOnClickListener(View.OnClickListener {
            setQuestion(binding.option1.text.toString())
        })
        binding.option2.setOnClickListener(View.OnClickListener {

            setQuestion(binding.option2.text.toString())
        })
        binding.option3.setOnClickListener(View.OnClickListener {

            setQuestion(binding.option3.text.toString())
        })
        binding.option4.setOnClickListener(View.OnClickListener {

            setQuestion(binding.option4.text.toString())
        })
        binding.subjectimage.setImageResource(image)
        binding.coinwithdrawal1.setOnClickListener(
            View.OnClickListener {
                var withdrawal = Withdrawal()
                withdrawal.show(this.supportFragmentManager, "Text")
                withdrawal.enterTransition

            }
        )
        binding.coinwithdrawal2.setOnClickListener(
            View.OnClickListener {
                var withdrawal = Withdrawal()
                withdrawal.show(this.supportFragmentManager, "Text")
                withdrawal.enterTransition

            }
        )
    }

    fun setQuestion(s: String) {

        if (s == arrayList.get(currentquestion).answer) {
            totalcorrectquestion++

        }
        currentquestion++
        if (currentquestion < arrayList.size) {
            binding.question.text = arrayList.get(currentquestion).question
            binding.option1.text = arrayList.get(currentquestion).option1
            binding.option2.text = arrayList.get(currentquestion).option2
            binding.option3.text = arrayList.get(currentquestion).option3
            binding.option4.text = arrayList.get(currentquestion).option4
        } else {//for win
            if (totalcorrectquestion / arrayList.size * 100 >= 60) {
                binding.linearLayoutwin.visibility = View.VISIBLE
                binding.mainlayout.visibility = View.GONE


              var updateplaychance:Long=0
                database.getReference().child("PlayChance").child(Firebase.auth.currentUser!!.uid).child("Value")
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            updateplaychance = snapshot.value as Long
                                database.getReference().child("PlayChance")
                                    .child(Firebase.auth.currentUser!!.uid).
                                    child("Value").setValue((updateplaychance+1))


                        }

                        override fun onCancelled(error: DatabaseError) {
                          //
                        }
                    }
                    )



            } else {//for lose
                binding.linearLayoutlose.visibility = View.VISIBLE
                binding.mainlayout.visibility = View.GONE
            }

        }
    }
}




