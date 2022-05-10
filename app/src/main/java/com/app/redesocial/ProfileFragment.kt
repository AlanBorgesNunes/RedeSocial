package com.app.redesocial

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.redesocial.Adapters.RecyclerProfileAdapter
import com.app.redesocial.databinding.FragmentProfileBinding
import com.google.android.gms.cast.framework.media.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.w3c.dom.Text
import java.io.File
import java.lang.StringBuilder


class ProfileFragment : Fragment() {
    private var titlesList = mutableListOf<String>()
    private var descriptionList = mutableListOf<String>()
    private var imageList = mutableListOf<Int>()
    private lateinit var dbRef: DatabaseReference
    private lateinit var mStorageRef: StorageReference
    private lateinit var binding: FragmentProfileBinding
    private lateinit var userArrayList: ArrayList<PostData>

    private lateinit var rvProfile: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater).apply {
            viewLifecycleOwner
        }
        initView()
        return binding.root
    }

    private fun initView() {
        getPostDatabase()
        getImageProfile()
        getName()
        getDescription()
        postToList()

        rvProfile = binding.recyclerProfile
        rvProfile.layoutManager = LinearLayoutManager(requireContext())
        rvProfile.setHasFixedSize(true)
        userArrayList = arrayListOf<PostData>()

        binding.busines.setOnClickListener {
            findNavController().navigate(
                R.id.action_profileFragment_to_feedFragment
            )
        }

        binding.newPost.setOnClickListener {
            findNavController().navigate(
                R.id.action_profileFragment_to_creatPostFragment
            )
        }

        binding.menu.setOnClickListener {
            if (FirebaseAuth.getInstance().signOut() != null) {
                findNavController().navigate(
                    R.id.action_profileFragment_to_loginFragment
                )
            }
        }


    }

    private fun addList(title: String, description: String, image: Int) {
        titlesList.add(title)
        descriptionList.add(description)
        imageList.add(image)
    }

    private fun postToList() {

        for (i in 1..1) {

            addList(
                "Monday",
                "One Monday afternoon, Mike, adopting a very unusual style, wore a hoody. Nobody would ever think of wearing a hoody on a very hot day. ",
                R.drawable.menino
            )
            addList(
                "Mike",
                "friends also wondered why he was being weird. They became a little aloof with Mike that day.",
                R.drawable.menino
            )

        }


    }

    private fun getDescription() {
        mStorageRef = FirebaseStorage.getInstance().reference
        dbRef = FirebaseDatabase.getInstance().reference

        var getData = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var sb = StringBuilder()
                for (i in snapshot.children) {
                    var description =
                        i.child(Firebase.auth.currentUser?.uid.tos()).child("descrição")
                            .child("description").getValue()
                    sb.append("$description")
                }

                binding.receivDescription.text = sb
            }


            override fun onCancelled(error: DatabaseError) {

            }

        }

        dbRef.addValueEventListener(getData)
        dbRef.addListenerForSingleValueEvent(getData)

    }

    private fun getName() {

        dbRef = FirebaseDatabase.getInstance().reference

        var getData = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var sb = StringBuilder()
                for (i in snapshot.children) {
                    var name = i.child(Firebase.auth.currentUser?.uid.tos()).child("descrição")
                        .child("name").getValue()
                    sb.append("$name")
                }

                binding.receivNameProfile.text = sb
            }

            override fun onCancelled(error: DatabaseError) {

            }

        }

        dbRef.addValueEventListener(getData)
        dbRef.addListenerForSingleValueEvent(getData)

    }

    private fun getImageProfile() {
        val user = Firebase.auth.currentUser?.uid.toString()
        mStorageRef = FirebaseStorage.getInstance().reference.child(user).child("images")

        val localFile = File.createTempFile("tempImage", "jpg")
        mStorageRef.getFile(localFile).addOnSuccessListener {

            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            binding.imageProfilePrinc.setImageBitmap(bitmap)

        }.addOnFailureListener {
            Log.e("tag", "error image storage")

        }
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
                            userArrayList.add(anotacoes!!)
                        }


                    }


                    rvProfile.adapter = RecyclerProfileAdapter(userArrayList)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })
    }

}