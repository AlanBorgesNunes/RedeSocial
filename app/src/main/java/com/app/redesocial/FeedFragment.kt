package com.app.redesocial

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.redesocial.Adapters.RecyclerProfileAdapter
import com.app.redesocial.databinding.FragmentFeedBinding
import com.app.redesocial.databinding.FragmentLoginBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.lang.StringBuilder

class FeedFragment : Fragment() {
    private lateinit var mStorageRef: StorageReference
    private lateinit var binding: FragmentFeedBinding
    private lateinit var rvFeed: RecyclerView
    private lateinit var userArrayList: ArrayList<PostData>
    private lateinit var dbRef: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFeedBinding.inflate(layoutInflater).apply {
            viewLifecycleOwner
        }
        initView()
        return binding.root
    }

    private fun initView(){
        getPostDatabase()
        rvFeed = binding.recyclerFeed
        rvFeed.layoutManager = LinearLayoutManager(requireContext())
        rvFeed.hasFixedSize()
        userArrayList = arrayListOf<PostData>()

    }
    private fun getPostDatabase() {
        val user = Firebase.auth.currentUser?.uid.tos()
        dbRef = FirebaseDatabase.getInstance().reference
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var sb = StringBuilder()
                if (snapshot.exists()) {
                    for (Snapshot in snapshot.children) {

                        val anotacoes =
                            Snapshot.child(user).child("id").getValue(PostData::class.java)
                        if (anotacoes == null) {

                        } else {
                            getImagePost()
                            userArrayList.add(anotacoes)
                        }
                    }
                    rvFeed.adapter = RecyclerProfileAdapter(userArrayList)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })
    }
    private fun getImagePost() {
        val user = Firebase.auth.currentUser?.uid.toString()
        mStorageRef = FirebaseStorage.getInstance().reference.child(user).child("post")

        val localFile = File.createTempFile("tempImage", "jpg")
        mStorageRef.getFile(localFile).addOnSuccessListener {

            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            val imagePost = view?.findViewById<ImageView>(R.id.image_item)
            imagePost?.setImageBitmap(bitmap)

        }.addOnFailureListener {
            Log.e("tag", "error image storage")

        }
    }



}